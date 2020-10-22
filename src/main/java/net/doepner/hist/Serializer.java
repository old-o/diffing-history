package net.doepner.hist;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementors of this class can serialize objects to and from xml strings.
 * <p>
 * Used primarily by the AppletMessageSender, NonNetworkMessageSender and the AppletServlet.
 */
public interface Serializer<T> {
	
	byte[] serialize(T object);
	
	T deserialize(InputStream inputStream);
	
	void serialize(T object, OutputStream outputStream);

}
