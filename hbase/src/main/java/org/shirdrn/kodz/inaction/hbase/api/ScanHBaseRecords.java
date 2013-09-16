package org.shirdrn.kodz.inaction.hbase.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class ScanHBaseRecords {

	private static final Log LOG = LogFactory.getLog(ScanHBaseRecords.class);
	private static Map<String, HTable> TABLES = new HashMap<String, HTable>();
	
	public synchronized static HTable openTable(String tableName) throws IOException {
		HTable table = TABLES.get(tableName);
		if(table == null) {
			Configuration conf = HBaseConfiguration.create();
			table = new HTable(conf, tableName);
			TABLES.put(tableName, table);
		}
		return table;
	}
	
	public void scanAll(String tableName) {
		try {
			HTable table = openTable(tableName);
			Scan scan = new Scan();
			ResultScanner scanner = table.getScanner(scan);
			printResults(scanner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printResults(ResultScanner scanner) {
		Iterator<Result> iter = scanner.iterator();
		while(iter.hasNext()) {
			Result result = iter.next();
			byte[] row = result.getRow();
			LOG.info("row=" + new String(row));
			for(KeyValue pair : result.raw()) {
				byte[] family = pair.getFamily();
				byte[] qualifier = pair.getQualifier();
				String q = new String(qualifier);
				byte[] value = pair.getValue();
				Object val = null;
				if(q.equals("length") || q.equals("ip_count") || q.equals("is_live")) {
					val = Bytes.toInt(value);
				} else {
					val = Bytes.toStringBinary(value);
				}
				StringBuilder builder = new StringBuilder();
				builder.append(new String(family)).append(":").append(q);
				LOG.info("column=" + builder.toString() + ",value=" + val);
			}
		}
	}
	
	public void scanByColumnValue(String tableName, String column, String value) {
		this.scanByColumnValue(tableName, column, value, CompareOp.EQUAL);
	}
	
	public void scanByColumnValue(String tableName, String column, String value, CompareOp op) {
		if(!column.trim().isEmpty() && column.indexOf(":") != -1) {
			String[] a = column.trim().split(":");
			String family = a[0].trim();
			String qualifier = a[1].trim();
			try {
				HTable table = openTable(tableName);
				Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), op, Bytes.toBytes(value));
				Scan scan = new Scan();
				scan.setFilter(filter);
				ResultScanner scanner = table.getScanner(scan);
				printResults(scanner);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void scanByColumnValue(String tableName, String column, int value, CompareOp op) {
		if(!column.trim().isEmpty() && column.indexOf(":") != -1) {
			String[] a = column.trim().split(":");
			String family = a[0].trim();
			String qualifier = a[1].trim();
			try {
				HTable table = openTable(tableName);
				Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), op, Bytes.toBytes(value));
				Scan scan = new Scan();
				scan.setFilter(filter);
				ResultScanner scanner = table.getScanner(scan);
				printResults(scanner);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void scanByColumns(String tableName, String[] columns) {
		Scan scan = new Scan();
		for(String col : columns) {
			if(!col.trim().isEmpty() && col.indexOf(":") != -1) {
				String[] a = col.trim().split(":");
				if(a.length == 2 && !a[0].trim().isEmpty() && !a[1].trim().isEmpty()) {
					scan.addColumn(Bytes.toBytes(a[0].trim()), Bytes.toBytes(a[1].trim()));
				}
			}
		}
		try {
			HTable table = openTable(tableName);
			ResultScanner scanner = table.getScanner(scan);
			printResults(scanner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void scanByCompositeConditions(String tableName, Conditions conditions) {
		try {
			HTable table = openTable(tableName);
			List<Filter> filters = conditions.getFilterList();
			FilterList filterList = new FilterList(filters);
			Scan scan = new Scan();
			scan.setFilter(filterList);
			ResultScanner scanner = table.getScanner(scan);
			printResults(scanner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
