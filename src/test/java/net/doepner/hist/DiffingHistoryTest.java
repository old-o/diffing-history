package net.doepner.hist;

import static net.doepner.hist.TestEnum.ONE;
import static net.doepner.hist.TestEnum.TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

/**
 * Tests DiffingHistory with some test data object graphs
 */
public final class DiffingHistoryTest {
	
	private final History<TestData1> history = new DiffingHistory<>();
	
	private int undoStackSize;
	private int redoStackSize;

	@Test
	public void test() {
		history.addUndoStackSizeListener(n -> undoStackSize = n);
		history.addRedoStackSizeListener(n -> redoStackSize = n);
		verifyStackSizes(0, 0);
		
		final TestData1 original = getOriginal();
		history.capture(original);
		verifyStackSizes(0, 0);

		final TestData1 changed = getModified();
		history.capture(changed);
		verifyStackSizes(1, 0);
		assertTrue(history.hasUndoableChanges());
		
		final TestData1 undone = history.undo();
		verifyStackSizes(0, 1);
		assertDeepEquals(original, undone);
		assertFalse(history.hasUndoableChanges());
		
		final TestData1 redone = history.redo();
		verifyStackSizes(1, 0);
		assertDeepEquals(changed, redone);
		
		history.resetTo(original);
		verifyStackSizes(0, 0);
	}

	private void assertDeepEquals(Object o1, Object o2) {
		assertTrue(EqualsBuilder.reflectionEquals(o1, o2, /* testTransients */ false, /* reflectUpToClass */ null, /* testRecursive */ true));
	}

	public TestData1 getOriginal() {
		return new TestData1(1, ONE, new TestData2("blah-di-blah"));
	}

	public TestData1 getModified() {
		return new TestData1(2, TWO, new TestData2("blah-di-blah"), new TestData2("blubber"));
	}

	private void verifyStackSizes(int expectedUndoStackSize, int expectedRedoStackSize) {
		assertEquals(expectedUndoStackSize, undoStackSize);
		assertEquals(expectedRedoStackSize, redoStackSize);
	}

}
