package net.doepner.hist;

import java.io.IOException;

import com.nothome.delta.Delta;
import com.nothome.delta.GDiffPatcher;

/**
 * Computes binary diffs and applies binary patches on byte arrays
 */
public final class BinaryPatcher {

	public byte[] diff(byte[] source, byte[] target) {
		try {
			return new Delta().compute(source, target);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public byte[] patch(byte[] source, byte[] diff) {
		try {
			return new GDiffPatcher().patch(source, diff);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
