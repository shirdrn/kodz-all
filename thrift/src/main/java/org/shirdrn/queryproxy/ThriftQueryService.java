package org.shirdrn.queryproxy;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.shirdrn.queryproxy.common.Configurable;
import org.shirdrn.queryproxy.thrift.protocol.QueryFailureException;
import org.shirdrn.queryproxy.thrift.protocol.QueryParams;
import org.shirdrn.queryproxy.thrift.protocol.QueryProxyService.Iface;
import org.shirdrn.queryproxy.thrift.protocol.QueryResult;
import org.shirdrn.queryproxy.thrift.protocol.QueryType;
import org.shirdrn.queryproxy.utils.ReflectionUtils;

public class ThriftQueryService implements Iface {

	private static final Log LOG = LogFactory.getLog(ThriftQueryService.class);
	private Configurable context;
	static Map<QueryType, Iface> SERVICES = new HashMap<QueryType, Iface>(0);
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for(Map.Entry<QueryType, Iface> entry : SERVICES.entrySet()) {
					try {
						((Closeable) entry.getValue()).close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						LOG.info("Closed: type=" + entry.getKey() + ", service=" + entry.getValue());
					}
				}
			}
		});
	}
	
	@Override
	public QueryResult query(QueryParams params) throws QueryFailureException, TException {
		int type = params.getTYPE().getValue();
		Iface service = SERVICES.get(QueryType.findByValue(type));
		if(service == null) {
			throw new QueryFailureException("Unknown service: type=" + params.getTYPE().name());
		}
		return service.query(params);
	}

	public void register(QueryType queryType, Class<?> serviceClass) {
		Iface service = (Iface) ReflectionUtils.getInstance(serviceClass, new Object[] {context});
		SERVICES.put(queryType, service);
	}
	
	public void setContext(Configurable context) {
		this.context = context;
	}


}
