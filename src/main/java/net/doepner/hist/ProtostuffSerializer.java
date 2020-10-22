package net.doepner.hist;

import static java.lang.invoke.MethodHandles.lookup;
import static net.doepner.hist.SerializationMessages.getSerializedToByteArrayMessage;
import static net.doepner.hist.SerializationMessages.getSerializedToStreamBytesMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Uses the protostuff framework to serialize object graphs
 */
public final class ProtostuffSerializer<T> implements Serializer<T> {
	
	private static final Logger logger = Logger.getLogger(lookup().lookupClass().getName());

	private static final DefaultIdStrategy STRATEGY =
			new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS
										  | IdStrategy.MORPH_COLLECTION_INTERFACES
										  | IdStrategy.MORPH_MAP_INTERFACES
										  | IdStrategy.MORPH_NON_FINAL_POJOS
										  | IdStrategy.COLLECTION_SCHEMA_ON_REPEATED_FIELDS
			);

	public static final class Wrapper<T> {

		private final T object;

		public Wrapper(T object) {
			this.object = object;
		}

		public T get() {
			return object;
		}
	}

	@SuppressWarnings("unchecked")
	private final Class<Wrapper<T>> wrapperType = (Class<Wrapper<T>>) (Class<?>) Wrapper.class;
	private final Schema<Wrapper<T>> schema = RuntimeSchema.getSchema(wrapperType, STRATEGY);
	
	@Override
	public byte[] serialize(T object) {
		final LinkedBuffer buffer = LinkedBuffer.allocate();
		final byte[] bytes = GraphIOUtil.toByteArray(new Wrapper<>(object), schema, buffer);
		logger.info(getSerializedToByteArrayMessage(bytes));
		return bytes;
	}
	
	@Override
	public void serialize(T object, OutputStream outputStream) {
		try {
			final LinkedBuffer buffer = LinkedBuffer.allocate();
			final int size = GraphIOUtil.writeTo(outputStream, new Wrapper<>(object), schema, buffer);
			logger.info(getSerializedToStreamBytesMessage(size));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public T deserialize(InputStream inputStream) {
		try {
			final Wrapper<T> wrapper = new Wrapper<>(null);
			GraphIOUtil.mergeFrom(inputStream, wrapper, schema);
			return wrapper.get();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
