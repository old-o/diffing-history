package net.doepner.hist;

import java.util.function.IntConsumer;

/**
 * Undo/Redo history
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
