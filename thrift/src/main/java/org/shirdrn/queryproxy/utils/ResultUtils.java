package org.shirdrn.queryproxy.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

public class ResultUtils {

	private static final String KEY_VERSION = "_version_";
	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static List<String> getJSONResults(QueryResponse response) {
		ListIterator<SolrDocument> iter = response.getResults().listIterator();
		List<String> resultDocs = new ArrayList<String>();
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
			resultDocs.add(jDoc.toString());
		}
		return resultDocs;
	}
	
	public static List<String> getJSONResults(ResultSet rs, List<String> fields) throws SQLException {
		List<String> results = new ArrayList<String>();
		while(rs.next()) {
			JSONObject jo = new JSONObject();
			for(String field : fields) {
				jo.put(field, rs.getObject(field).toString());
			}
			results.add(jo.toString());
		}
		return results;
	}
}
