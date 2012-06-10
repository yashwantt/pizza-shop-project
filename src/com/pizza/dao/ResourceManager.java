package com.pizza.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ResourceManager {

	private static ResourceManager instance = new ResourceManager();
	private Properties prop = new Properties();
	
	/**
	 * Accessible via instance() only.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private ResourceManager() {
		// use instance()
		try {
			prop.load( ResourceManager.class.getResourceAsStream("/messages.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("message resource file not found");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"problem openning the message resource file");
		}
	}

	/**
	 * Allows non-Spring classes to access the singleton instance.
	 * @return the instance
	 */
	public static ResourceManager instance() {
		return instance;
	}
	
	public String getMessage(String key) {
		String result = prop.getProperty(key);
		if (null != result) {
			result = "Message resource missing for the key: " + key;
		}
		return prop.getProperty(key);
	}
	
	
	
}
