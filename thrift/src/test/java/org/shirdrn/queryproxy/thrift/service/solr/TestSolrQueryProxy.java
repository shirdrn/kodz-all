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
		TTransport transport = new TSocket(host, 9933);
		TProtocol protocol = new TBinaryProtocol(transport, true, true);
		transport.open();
		QueryProxyService.Client client = new QueryProxyService.Client(protocol);
		
		QueryParams params = new QueryParams();
		params.setTable("collection1");
		params.setTYPE(QueryType.SOLR);
		params.addToParams("q=上海");
		params.addToParams("fl=*");
		params.addToParams("fq=building_type:1");
		params.addToParams("start=50");
		params.addToParams("rows=10");
		params.addToParams("wt=json");
		
		QueryResult result = client.query(params);
		LOG.info("offset=" + result.getOffset());
		LOG.info("length=" + result.getLength());
		LOG.info("result=" + result.getResults());
		transport.close();
	}
	
	public static void main(String[] args) throws QueryFailureException, TException {
		new TestSolrQueryProxy().query();
	}
}
