package org.shirdrn.queryproxy.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.queryproxy.common.Configurable;

public final class PropertiesConfig implements Configurable {

	private static final Log LOG = LogFactory.getLog(PropertiesConfig.class);
	private final String prop;
	private Properties props = new Properties();
	
	public PropertiesConfig(String prop) {
		super();
		this.prop = prop;
		load();
	}
	
	private void load() {
		LOG.debug("Load properties file: prop=" + prop);
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
	
	@Override
	public String get(String key) {
		return props.getProperty(key);
	}
	
	@Override
	public String get(String key, String defaultValue) {
		String value = defaultValue;
		String v = props.getProperty(key);
		if(v != null) {
			value = v;
		}
		return value;
	}
	
	@Override
	public int getInt(String key, int defaultValue) {
		int value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Integer.parseInt(v);
		} catch (Exception e) { }
		return value;
	}
	
	@Override
	public long getLong(String key, long defaultValue) {
		long value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Long.parseLong(v);
		} catch (Exception e) { }
		return value;
	}
	
	@Override
	public double getDouble(String key, double defaultValue) {
		double value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Double.parseDouble(v);
		} catch (Exception e) { }
		return value;
	}
	
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		boolean value = defaultValue;
		String v = props.getProperty(key);
		try {
			value = Boolean.parseBoolean(v);
		} catch (Exception e) { }
		return value;
	}

	@Override
	public Object getObject(String key, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		PropertiesConfig other = (PropertiesConfig) obj;
		return other.prop.equals(this.prop);
	}

	@Override
	public String[] getStrings(String key) {
		String value = (String) props.get(key);
		if(value != null) {
			return value.split(",");
		}
		return new String[] {};
	}
	
}
