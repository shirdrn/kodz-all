package org.shirdrn.kodz.inaction.hadoop.hbase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.shirdrn.kodz.inaction.hadoop.utils.GeoIPLookupService;
import org.xbill.DNS.Address;

/**
 * Store local file content to HBase table. And , the DDL for table 'all_domains' is:
 * <dd>
 * create 'all_domains', 'cf_basic', 'cf_dynamic', 'cf_status', 'cf_time'
 * </dd>
 * 
 * @author shirdrn
 */
public class HBaseClient {

	static final Log LOG = LogFactory.getLog(HBaseClient.class);
	static int	BATCH_SIZE = 1000;
	static Map<String, HTable> TABLES = new HashMap<String, HTable>();
	
	private synchronized static HTable openTable(String tableName) throws IOException {
		HTable table = TABLES.get(tableName);
		if(table == null) {
			Configuration conf = HBaseConfiguration.create();
			table = new HTable(conf, tableName);
			TABLES.put(tableName, table);
		}
		return table;
	}
	
	public void storeAll(HTable table, String file) throws FileNotFoundException, IOException {
		FileInputStream fis = null;
		BufferedReader reader = null;
		int counter = 0;
		try {
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis, Charset.defaultCharset()));
			String line = null;
			while((line=reader.readLine())!=null) {
				try {
					line = line.trim();
					if(!line.isEmpty()) {
						Put put = createPut(line.toLowerCase());
						table.put(put);
						LOG.info("Put a record;domain=" + line);
						if(++counter % BATCH_SIZE == 0) {
							table.flushCommits();
							LOG.info("Flush and commit;counter=" + counter);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			if(reader != null) {
				reader.close();
			}
			table.flushCommits();
			LOG.info("Final flush and commit;counter=" + counter);
		}
	}

	private Put createPut(String domain) {
		Put put = new Put(Bytes.toBytes(domain));
		put.add(Bytes.toBytes("cf_basic"), Bytes.toBytes("domain"), Bytes.toBytes(domain));
		put.add(Bytes.toBytes("cf_basic"), Bytes.toBytes("length"), Bytes.toBytes(domain.length()));
		
		InetAddress[] addresses = null;
		try {
			addresses = Address.getAllByName(domain);
		} catch (Exception e) {
			LOG.warn("Fail to get address;domain=" + domain, e);
		}
		if(addresses != null && addresses.length>0) {
			put.add(Bytes.toBytes("cf_status"), Bytes.toBytes("is_live"), Bytes.toBytes(1));
			List<String> ips = new ArrayList<String>(1);
			for(InetAddress ip : addresses) {
				ips.add(ip.getHostAddress());
			}
			if(!ips.isEmpty()) {
				put.add(Bytes.toBytes("cf_dynamic"), Bytes.toBytes("ipaddress"), Bytes.toBytes(ips.toString()));
			}
			put.add(Bytes.toBytes("cf_dynamic"), Bytes.toBytes("ip_count"), 
					Bytes.toBytes(ips.size()));
			String code = GeoIPLookupService.newInstance().getCountry(domain).getCode();
			if(!code.equals("--")) {
				put.add(Bytes.toBytes("cf_dynamic"), Bytes.toBytes("country"), 
						Bytes.toBytes(code));
			}
		} else {
			put.add(Bytes.toBytes("cf_status"), Bytes.toBytes("is_live"), Bytes.toBytes(0));
		}
		return put;
	}
	
	public static void main(String[] args) throws IOException {
		String tableName = "all_domains";
		HTable table = openTable(tableName);
		String file = "/home/shirdrn/all_domains";
		if(args.length == 1) {
			file = args[0].trim();
		}
		HBaseClient client = new HBaseClient();
		client.storeAll(table, file);
	}

}
