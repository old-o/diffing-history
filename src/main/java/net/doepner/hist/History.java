package net.doepner.hist;

import java.util.function.IntConsumer;

/**
 * Manages a stack of serialized objects (snapshots) to support reverting to earlier snapshots
 */
public interface History<T> {

	void capture(T t);

	T undo();
	
	T redo();

	void resetTo(T t);

	boolean hasUndoableChanges();
	
	void addUndoStackSizeListener(IntConsumer listener);
	
	void addRedoStackSizeListener(IntConsumer listener);
	
}
