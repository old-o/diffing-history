package net.doepner.hist;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Object graph serialization from/to byte arrays or byte stream
 */
public interface Serializer<T> {
	
	byte[] serialize(T object);
	
	T deserialize(InputStream inputStream);
	
	void serialize(T object, OutputStream outputStream);

}
