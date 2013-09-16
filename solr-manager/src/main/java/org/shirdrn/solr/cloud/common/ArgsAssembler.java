package org.shirdrn.solr.cloud.common;

import org.shirdrn.solr.cloud.common.AbstractIndexer.Status;

public interface ArgsAssembler<T> {
	T assemble(String[] args) throws Exception;
	String getUsageArgList();
	int getRequiredArgCount();
	String[] showCLIExamples();
	String getType();
	String getName();
	Status getStatus();
}
