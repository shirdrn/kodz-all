package org.shirdrn.solr.cloud.index.manager;

import org.junit.Test;
import org.shirdrn.solr.cloud.common.AbstractIndexingManager;

public class TestSingleIndexingManager {

	@Test
	public void start() {
		String[] args = new String[] {
				"1", "87318", "88282","sho.h_s_event"
		};
		AbstractIndexingManager.startIndexer(SingleIndexingManager.class, args);
	}
}
