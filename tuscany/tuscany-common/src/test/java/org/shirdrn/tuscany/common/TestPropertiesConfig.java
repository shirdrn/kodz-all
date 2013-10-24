package org.shirdrn.tuscany.common;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestPropertiesConfig {

	Configurable config;
	
	@Before
	public void initialize() {
		config = new PropertiesConfig("test.properties");
	}
	
	@Test
	public void test() {
		assertEquals("master:2181", config.get("test.config.str"));
		assertEquals(30000, config.getInt("test.config.int", 0));
		assertEquals(1000000L, config.getLong("test.config.long", 1000000L));
		assertEquals(false, config.getBoolean("test.config.bool", true));
	}
}
