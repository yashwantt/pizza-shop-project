package com.pizza.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

import org.hibernate.engine.jdbc.NonContextualLobCreator;
import org.hibernate.engine.jdbc.StreamUtils;

public class LobUtils {

	/**
	 * create a new Blob.
	 * @param stream binary stream
	 * @return the new Blob
	 * @throws IOException stream read exception
	 * @see org.hibernate.Hibernate#createBlob(InputStream)
	 */
	public static Blob createBlob(final InputStream stream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(stream.available());
		StreamUtils.copy(stream, buffer);
		return createBlob(buffer.toByteArray());
	}
	
	
	/**
	 * Create a new Blob.
	 * @param bytes a byte array
	 * @return the Blob
	 * @see org.hibernate.Hibernate#createBlob(byte[])
	 */
	public static Blob createBlob(final byte[] bytes) {
		return NonContextualLobCreator.INSTANCE.wrap(
				NonContextualLobCreator.INSTANCE.createBlob(bytes)
		);
	}
	
}
