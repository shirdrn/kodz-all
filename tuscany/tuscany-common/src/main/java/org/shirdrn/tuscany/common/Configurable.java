package org.shirdrn.tuscany.common;

public interface Configurable {

	String get(String key);	
	String get(String key, String defaultValue);
	String[] getStrings(String key);	
	int getInt(String key, int defaultValue);	
	long getLong(String key, long defaultValue);	
	double getDouble(String key, double defaultValue);	
	boolean getBoolean(String key, boolean defaultValue);
	Object getObject(String key, Object defaultValue);
	
}
