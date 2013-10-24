package org.shirdrn.tuscany.service.impl;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.shirdrn.tuscany.common.Configurable;
import org.shirdrn.tuscany.common.PropertiesConfig;
import org.shirdrn.tuscany.service.SolrSearchService;

public class SolrSearchServiceImpl implements SolrSearchService, Closeable {

	private static final Log LOG = LogFactory.getLog(SolrSearchServiceImpl.class);
	private CloudSolrServer solrServer;
	private static final String SOLR_CONFIG = "solr.properties";
	private Configurable solrConf;
	
	public SolrSearchServiceImpl() {
		super();
		solrConf = new PropertiesConfig(SOLR_CONFIG);
		String zkHost = solrConf.get("solr.zk.host");
		int zkConnectTimeout = solrConf.getInt("solr.zk.connectTimeout", 10000);
		int zkClientTimeout = solrConf.getInt("solr.zk.clientTimeout", 30000);
		try {
			solrServer = new CloudSolrServer(zkHost);
			solrServer.setZkConnectTimeout(zkConnectTimeout);
			solrServer.setZkClientTimeout(zkClientTimeout);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
    public String search(String collection, String params, int start, int rows) {
        String[] a = params.split("\\|");
        Map<String,String> map = new HashMap<>();
        for(String pair : a) {
        	String[] kvs = pair.split("=");
        	map.put(kvs[0], kvs[1]);
        }
        map.put("collection", collection);
        map.put(CommonParams.START, String.valueOf(start));
        map.put(CommonParams.ROWS, String.valueOf(rows));
        
        QueryResponse response = null;
		SolrParams solrParams = new MapSolrParams(map);
		try {
			response = solrServer.query(solrParams);
		} catch (SolrServerException e) {
			LOG.error("Failed to query solr server: ", e);
			throw new RuntimeException(e);
		}
		return getJSONResults(response);
    }
    
    private static final String KEY_VERSION = "_version_";
	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String getJSONResults(QueryResponse response) {
		ListIterator<SolrDocument> iter = response.getResults().listIterator();
		JSONArray ja = new JSONArray();
		while(iter.hasNext()) {
			SolrDocument doc = iter.next();
			JSONObject jDoc = new JSONObject();
			Set<String> ks = doc.keySet();
			if(ks.contains(KEY_VERSION)) {
				ks.remove(KEY_VERSION);
			}
			for(String key : ks) {
				Object v = doc.getFieldValue(key);
				if(v instanceof Date) {
					jDoc.put(key, DF.format((Date) v));
					continue;
				}
				jDoc.put(key, v);
			}
			ja.add(jDoc);
		}
		return ja.toString();
	}

	@Override
	public void close() throws IOException {
		if(solrServer != null) {
			solrServer.shutdown();
		}
	}

}
