package org.shirdrn.tuscany.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oasisopen.sca.NoSuchServiceException;

public class TestSolrSearchService {

	private static final Log LOG = LogFactory.getLog(TestSolrSearchService.class);
	private Node node;
	
	@Before
	public void initialize() {
		node = TuscanyRuntime.runComposite("search.spring.composite", "target/classes");
	}
	
	@Test
	public void testSearch() throws NoSuchServiceException {
		SolrSearchService solrSearchService = node.getService(SolrSearchService.class, "solrSearchComponent");
		String collection = "h_d_appraise_gsm";
		String params = "q=*:*|fq=prov_id:1|indent=true";
		int start = 0;
		int rows = 10;
		String result = solrSearchService.search(collection, params, start, rows);
		LOG.info("Search result: result=" + result);
	}
	
	@After
	public void destroy() {
		node.stop();
	}
}
