package org.shirdrn.solr.cloud.index;

import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.shirdrn.solr.cloud.common.DocCreator;

public class MapDocCreator implements DocCreator<Map<String, String>> {
	private FieldMappingBuilder buidler;
	
	public MapDocCreator(FieldMappingBuilder buidler) {
		super();
		this.buidler = buidler;
	}

	@Override
	public SolrInputDocument createDoc(Map<String, String> docValue) {
		SolrInputDocument doc = new SolrInputDocument();
		for(Map.Entry<String, String> entry : docValue.entrySet()) {
			doc.addField(entry.getKey(), 
					buidler.getMappedField(entry.getKey()).valueHandler.handle(entry.getValue()));
		}
		return doc;
	}
}
