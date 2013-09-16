package org.shirdrn.solr.cloud.common;

public interface ValueHandler<T> {
	T handle(String value);
}
