package org.shirdrn.queryproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.shirdrn.queryproxy.common.Configurable;
import org.shirdrn.queryproxy.thrift.protocol.QueryType;
import org.shirdrn.queryproxy.thrift.protocol.QueryProxyService.Iface;
import org.shirdrn.queryproxy.thrift.protocol.QueryProxyService.Processor;
import org.shirdrn.queryproxy.thrift.service.solr.SolrQueryService;
import org.shirdrn.queryproxy.thrift.service.sql.SQLQueryService;
import org.shirdrn.queryproxy.utils.PropertiesConfig;

public class QueryProxyServer {

	private static final Log LOG = LogFactory.getLog(QueryProxyServer.class);
	static final String config = "config.properties";
	private final Configurable context;
	
	public QueryProxyServer() {
		super();
		context = loadContext();
	}
	
	private Configurable loadContext() {
		return new PropertiesConfig(config);
	}
	
	public void startUp() throws TTransportException {
		int port = context.getInt("query.proxy.thrift.port", 9933);
		LOG.info("Thrift service port: port=" + port);
		TServerSocket serverTransport = new TServerSocket(port);
		
		ThriftQueryService service = new ThriftQueryService();
		service.setContext(context);
		service.register(QueryType.SOLR, SolrQueryService.class);
		service.register(QueryType.SQL, SQLQueryService.class);
		
		int minWorkerThreads = context.getInt("query.proxy.thrift.worker.thread.minCount", 1);
		int maxWorkerThreads = context.getInt("query.proxy.thrift.worker.thread.maxCount", 1);
		LOG.info("Thrift thread pool: minWorkerThreads=" + minWorkerThreads + ", maxWorkerThreads=" + maxWorkerThreads);
		
		TProcessor processor = new Processor<Iface>(service);
		Factory factory = new TBinaryProtocol.Factory(true, true);
		Args tArgs = new Args(serverTransport);
		tArgs
			.minWorkerThreads(minWorkerThreads)
			.maxWorkerThreads(maxWorkerThreads)
			.processor(processor)
			.protocolFactory(factory);
		TServer server = new TThreadPoolServer(tArgs);
		server.serve();
	}
	
	public static void main(String[] args) throws Exception {
		QueryProxyServer server = new QueryProxyServer();
		server.startUp();
	}

}
