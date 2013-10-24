package org.shirdrn.tuscany.service;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oasisopen.sca.NoSuchServiceException;

public class TestWsClient {
	
	private static final Log LOG = LogFactory.getLog(TestWsClient.class);
	private SolrSearchComponentSolrSearchService client;
	private SolrSearchService service;
	
	@Before
	public void initialize() {
		client = new SolrSearchComponentSolrSearchService();
		service = client.getSolrSearchServiceSOAP11Port();
	}
	
	@Test
	public void testSearch() throws NoSuchServiceException, IOException {
		String collection = "h_d_appraise_gsm";
		String params = "q=*:*|fq=prov_id:1|indent=true";
		int start = 0;
		int rows = 10;
		
		String response = service.search(collection, params, start, rows);
		LOG.info("Search result: result=" + response);
	}
	
	@After
	public void destroy() {
	}
}
