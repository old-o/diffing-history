package net.doepner.hist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * (Un)compress byte arrays using Gzip
 */
public final class Compressor {
	
	public byte[] compress(byte[] bytes) { 
		if (bytes == null) {
			return null;
		}
		final ByteArrayOutputStream outBytesStream = new ByteArrayOutputStream();
		try (final OutputStream out = new GZIPOutputStream(outBytesStream)) {
			out.write(bytes);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return outBytesStream.toByteArray();
	}
	
	public byte[] uncompress(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		try (final InputStream in = new GZIPInputStream(new ByteArrayInputStream(bytes));
			 final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			copy(in, out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void copy(InputStream in, ByteArrayOutputStream out) throws IOException {
		final byte[] buffer = new byte[COPY_BUFFER_SIZE];
		for (int n = in.read(buffer); n != -1; n = in.read(buffer)) {
			out.write(buffer, 0, n);
		}
	}

	private static final int COPY_BUFFER_SIZE = 8024;

}
