package org.shirdrn.queryproxy.thrift.service.sql;

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

public class TestSQLQueryService {

private static final Log LOG = LogFactory.getLog(TestSQLQueryService.class);
	
	@Test
	public void query() throws QueryFailureException, TException {
		String host = "server.query-proxy.local";
		TTransport transport = new TSocket(host, 9933);
		TProtocol protocol = new TBinaryProtocol(transport, true, true);
		transport.open();
		QueryProxyService.Client client = new QueryProxyService.Client(protocol);
		
		QueryParams params = new QueryParams();
		params.setTable("wp_posts");
		params.setTYPE(QueryType.SQL);
		params.addToParams("select id, post_author from wordpress.wp_posts");
		params.addToParams("id");
		params.addToParams("post_author");
		
		QueryResult result = client.query(params);
		LOG.info("result=" + result.getResults());
		transport.close();
	}
}
