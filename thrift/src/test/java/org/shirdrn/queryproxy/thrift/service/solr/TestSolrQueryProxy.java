package org.shirdrn.queryproxy.thrift.service.solr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;
import org.shirdrn.queryproxy.thrift.protocol.QueryFailureException;
import org.shirdrn.queryproxy.thrift.protocol.QueryParams;
import org.shirdrn.queryproxy.thrift.protocol.QueryProxyService;
import org.shirdrn.queryproxy.thrift.protocol.QueryResult;
import org.shirdrn.queryproxy.thrift.protocol.QueryType;

public class TestSolrQueryProxy {

	private static final Log LOG = LogFactory.getLog(TestSolrQueryProxy.class);
	
	@Test
	public void query() throws QueryFailureException, TException {
		String host = "server.query-proxy.local";
		TTransport transport = new TSocket(host, 9966);
		TProtocol protocol = new TBinaryProtocol(transport, true, true);
		transport.open();
		QueryProxyService.Client client = new QueryProxyService.Client(protocol);
		
		QueryParams params = new QueryParams();
		params.setTable("collection1");
		params.setType(QueryType.SOLR);
		params.addToParamList("q=上海");
		params.addToParamList("fl=*");
		params.addToParamList("fq=building_type:1");
		params.addToParamList("start=50");
		params.addToParamList("rows=10");
		params.addToParamList("wt=json");
		
		QueryResult result = client.query(params);
		LOG.info("offset=" + result.getOffset());
		LOG.info("length=" + result.getLength());
		LOG.info("result=" + result.getResults());
		transport.close();
	}
	
}
