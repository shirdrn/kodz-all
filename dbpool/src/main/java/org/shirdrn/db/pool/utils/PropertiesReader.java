package org.shirdrn.db.pool.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PropertiesReader {

	private static final Log LOG = LogFactory.getLog(PropertiesReader.class);
	private Properties props = new Properties();
	
	public PropertiesReader(String prop) {
		super();
		load(prop);
	}
	
	private void load(String prop) {
		LOG.info("Load properties file: prop=" + prop);
		InputStream in = null;
		try {
			in = getClass().getClassLoader().getResourceAsStream(prop);
			if(in != null) {
				props.load(in);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String get(String key, String defaultValue) {
		String value = defaultValue;
		String v = props.getProperty(key);
		if(v != null) {
			value = v;
		}
		return value;
	}
	
	public int getInt(String key, int defaultValue) {
		int value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Integer.parseInt(v);
		} catch (Exception e) { }
		return value;
	}
	
	public long getLong(String key, long defaultValue) {
		long value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Long.parseLong(v);
		} catch (Exception e) { }
		return value;
	}
	
	public double getDouble(String key, double defaultValue) {
		double value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Double.parseDouble(v);
		} catch (Exception e) { }
		return value;
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		boolean value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Boolean.parseBoolean(v);
		} catch (Exception e) { }
		return value;
	}
	
}
