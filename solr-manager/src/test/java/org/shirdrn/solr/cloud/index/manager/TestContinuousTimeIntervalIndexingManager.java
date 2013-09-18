package org.shirdrn.solr.cloud.index.manager;

import org.junit.Test;
import org.shirdrn.solr.cloud.common.AbstractIndexingManager;

public class TestContinuousTimeIntervalIndexingManager {

	@Test
	public void start() {
		String[] args = new String[] {};
		AbstractIndexingManager.startIndexer(ContinuousTimeIntervalIndexingManager.class, args);
	}
}
