package net.doepner.hist;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static net.doepner.hist.Predicates.not;
import static net.doepner.hist.Predicates.notNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.function.IntConsumer;
import java.util.logging.Logger;

/**
 * Undo/Redo history using stacks of binary diffs to keep memory footprint small
 */
public final class DiffingHistory<T> implements History<T> {
	
	private static final Logger logger = Logger.getLogger(lookup().lookupClass().getName());
	
	private final Serializer<T> serializer = new ProtostuffSerializer<>();
	private final Compressor compressor = new Compressor();
	private final BinaryPatcher patcher = new BinaryPatcher();
	
	private final Deque<byte[]> undoDiffStack = new ArrayDeque<>();
	private final Deque<byte[]> redoDiffStack = new ArrayDeque<>();
	private byte[] compressedSnapshot;
	
	private final Collection<IntConsumer> undoStackSizeListeners = new ArrayList<>();
	private final Collection<IntConsumer> redoStackSizeListeners = new ArrayList<>();
	
	@Override
	public void addUndoStackSizeListener(IntConsumer listener) {
		undoStackSizeListeners.add(listener);
		listener.accept(undoDiffStack.size());
	}
	
	@Override
	public void addRedoStackSizeListener(IntConsumer listener) {
		redoStackSizeListeners.add(listener);
		listener.accept(redoDiffStack.size());
	}
	
	@Override
	public void capture(T t) {
		final byte[] snapshot = serializer.serialize(t);
		if (notNull(compressedSnapshot)) {
			final byte[] previous = compressor.uncompress(compressedSnapshot);
			if (Arrays.equals(snapshot, previous)) {
				logger.info("No state change detected. Nothing added to history.");
				return;
			} else {
				final byte[] undoDiff = patcher.diff(snapshot, previous);
				undoDiffStack.push(undoDiff);
				logger.info(String.format("Added byte array of length %d to diff stack (size=%d)",
										  undoDiff.length, undoDiffStack.size()));
			}
		}
		compressedSnapshot = compressor.compress(snapshot);
		redoDiffStack.clear();
		logger.info(String.format("Captured current %s. Compressed snapshot size = %d",
								  t.getClass().getSimpleName(), compressedSnapshot.length));
		notifySizeChanged();
	}

	@Override
	public T undo() {
		return applyPatchAndPushReverse(undoDiffStack, "undo", redoDiffStack);
	}

	@Override
	public T redo() {
		return applyPatchAndPushReverse(redoDiffStack, "redo", undoDiffStack);
	}

	private T applyPatchAndPushReverse(Deque<byte[]> diffStack, String action, Deque<byte[]> reverseDiffStack) {
		final byte[] diff = diffStack.pollFirst();
		if (isNull(diff)) {
			throw new IllegalStateException(String.format("The %s stack is empty. Nothing to %s!", action, action));
		}
		requireNonNull(compressedSnapshot);
		final byte[] current = compressor.uncompress(compressedSnapshot);
		final byte[] changed = patcher.patch(current, diff);
		final byte[] reverseDiff = patcher.diff(changed, current);
		reverseDiffStack.push(reverseDiff);
		compressedSnapshot = compressor.compress(changed);
		final T object = serializer.deserialize(new ByteArrayInputStream(changed));
		notifySizeChanged();
		return object;
	}

	@Override
	public void resetTo(T t) {
		undoDiffStack.clear();
		redoDiffStack.clear();
		compressedSnapshot = null;
		logger.info("Cleared undo history");
		capture(t);
	}

	@Override
	public boolean hasUndoableChanges() {
		return not(undoDiffStack.isEmpty());
	}
	
	private void notifySizeChanged() {
		undoStackSizeListeners.forEach(l -> l.accept(undoDiffStack.size()));
		redoStackSizeListeners.forEach(l -> l.accept(redoDiffStack.size()));
	}

}
