package net.doepner.hist;

/**
 * Helper methods for serialization logging 
 */
public final class SerializationMessages {
	
	static String getSerializedToByteArrayMessage(byte[] bytes) {
		return String.format("Serialized object to byte array of length %d", bytes.length);
	}
	
	static String getSerializedToStreamBytesMessage(long size) {
		return String.format("Serialized object to outputStream: %d bytes written", size);
	}
}
