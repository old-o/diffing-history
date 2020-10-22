package net.doepner.hist;

/**
 * Very simple but commonly used static methods that return boolean values
 */
public class Predicates {

	/*
	 * The negation operator in Java is an exclamation mark (!),
	 * which is familiar to C programmers, but notoriously confusing,
	 * at least for regular readers of the English language
	 */
	public static boolean not(boolean b) {
		return !b;
	}

	public static boolean notNull(Object o) {
		return o != null;
	}

	public static boolean notNull(Object... objects) {
		for (Object o : objects) {
			if (o == null) {
				return false;
			}
		}
		return true;
	}
}
