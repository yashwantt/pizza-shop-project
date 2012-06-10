package com.pizza.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;

import com.pizza.util.LobUtils;

public class PopulatorUtils {

	
	public static Blob createBlob(final File file) {
		try {
			return LobUtils.createBlob(new FileInputStream(file));
		}
		catch (final IOException e) {
			throw new RuntimeException("problem creating blob from " + file, e);
		}
	}
	
}
