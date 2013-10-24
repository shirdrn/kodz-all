package org.shirdrn.tuscany.service;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oasisopen.sca.NoSuchServiceException;
import org.shirdrn.tuscany.utils.HttpUtils;

public class TestWsSolrSearchService {
	
	private static final Log LOG = LogFactory.getLog(TestWsSolrSearchService.class);
	private Node node;
	
	@Before
	public void initialize() {
		node = TuscanyRuntime.runComposite("search.ws.composite", "target/classes");
	}
	
	@Test
	public void testSearch() throws NoSuchServiceException, IOException {
		String collection = "h_d_appraise_gsm";
		String params = "q=*:*|fq=prov_id:1|indent=true";
		int start = 0;
		int rows = 10;
		
		String url = "http://tuscany.server.shiyanjun.cn:8080/solrSearchComponent/SolrSearchService?wsdl";
		String data ="";
		data += "collection=" + collection + "&";
		data += "params=" + params + "&";
		data += "start=" + start + "&";
		data += "rows=" + rows;
		LOG.info("data=" + data);
		url += data;
		URL u = new URL(url);
		String response = HttpUtils.request(u);
		LOG.info("Search result: result=" + response);
		
		System.in.read();
	}
	
	@After
	public void destroy() {
		node.stop();
	}
}
