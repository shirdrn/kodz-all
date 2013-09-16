package org.shirdrn.solr.cloud.common;

import org.apache.solr.common.SolrInputDocument;

public interface DocCreator<T> {
	SolrInputDocument createDoc(T docValue);
}


