package org.shirdrn.solr.cloud.common;

import java.io.IOException;

public interface AssembledRunner {
	void assembleAndRun(String... params) throws IOException;
}
