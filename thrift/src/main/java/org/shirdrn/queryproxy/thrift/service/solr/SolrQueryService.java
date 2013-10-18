package org.shirdrn.queryproxy.thrift.service.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.thrift.TException;
import org.shirdrn.queryproxy.common.Configurable;
import org.shirdrn.queryproxy.common.ConfiguredQueryService;
import org.shirdrn.queryproxy.thrift.protocol.QueryFailureException;
import org.shirdrn.queryproxy.thrift.protocol.QueryParams;
import org.shirdrn.queryproxy.thrift.protocol.QueryResult;
import org.shirdrn.queryproxy.utils.ResultUtils;

public class SolrQueryService extends ConfiguredQueryService {

	private static final Log LOG = LogFactory.getLog(SolrQueryService.class);
	private CloudSolrServer solrServer;
	private static final String writerType = "json";
	
	public SolrQueryService(Configurable context) {
		super(context);
		String zkHost = context.get("query.proxy.solr.zkHost");
		try {
			solrServer = new CloudSolrServer(zkHost);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public QueryResult query(QueryParams params) throws QueryFailureException, TException {
		int offset = 0;
		int length = 10;
		Map<String,String> map = new HashMap<>();
		Iterator<String> iter = params.getParamListIterator();
		while(iter.hasNext()) {
			String kv = iter.next();
			if(kv != null) {
				String[] items = kv.split("=");
				if(items.length == 2) {
					String key = items[0].trim();
					String value = items[1].trim();
					map.put(key, value);
					if(key.equals(CommonParams.START)) {
						offset = Integer.parseInt(value);
					}
					if(key.equals(CommonParams.ROWS)) {
						length = Integer.parseInt(value);
					}
				}
			}
		}
		map.put("collection", params.getTable());
		map.put("wt", writerType);
		LOG.info("Solr params: " + map);
		
		// query using Solr
		QueryResponse response = null;
		SolrParams solrParams = new MapSolrParams(map);
		try {
			response = solrServer.query(solrParams);
		} catch (SolrServerException e) {
			LOG.error("Failed to query solr server: ", e);
			throw new QueryFailureException(e.toString());
		}
		
		// process result
		QueryResult result = new QueryResult();
		result.setOffset(offset);
		result.setLength(length);
		if(response != null) {
			result.setResults(ResultUtils.getJSONResults(response));
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		solrServer.shutdown();	
	}
	
}
