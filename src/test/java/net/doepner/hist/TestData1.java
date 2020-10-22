package net.doepner.hist;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

/**
 * Some test data class
 */
public class TestData1 {
	
	private final int integer;
	private final TestEnum testEnum;
	private final List<TestData2> list = new ArrayList<>();

	public TestData1(int integer, TestEnum testEnum, TestData2... list) {
		this.integer = integer;
		this.testEnum = testEnum;
		this.list.addAll(asList(list));
	}

	public int getInteger() {
		return integer;
	}

	public TestEnum getEnum() {
		return testEnum;
	}

	public Iterable<TestData2> getList() {
		return list;
	}
}
