package org.shirdrn.kodz.inaction.hbase.api;

import java.io.IOException;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.shirdrn.kodz.inaction.hbase.api.Conditions.Condition;

public class TestScanHBaseRecords {

	ScanHBaseRecords client;
	String tableName;
	
	@Before
	public void initialize() throws IOException {
		tableName = "all_domains";
		client = new ScanHBaseRecords();
	}
	
	@Test
	public void scanAll() {
		client.scanAll(tableName);
	}
	
	@Test
	public void scanByColumnEqualValue() {
		String column = "cf_basic:domain";
		String value = "022aa.com";
		client.scanByColumnValue(tableName, column, value);
		
	}
	
	@Test
	public void scanByColumnGtValue() {
		String column = "cf_basic:length";
		int value = 10;
		client.scanByColumnValue(tableName, column, value, CompareOp.GREATER_OR_EQUAL);
		
	}
	
	@Test
	public void scanByCompositeConditions() {
		Conditions conditions = new Conditions();
		// cf_basic:length<=8
		conditions.addCondition(new Condition("cf_basic", "length", CompareOp.LESS_OR_EQUAL, Bytes.toBytes(8)));
		// cf_status:is_live=1
		conditions.addCondition(new Condition("cf_status", "is_live", CompareOp.EQUAL, Bytes.toBytes(1)));
		// cf_dynamic:ip_count
		conditions.addCondition(new Condition("cf_dynamic", "ip_count", CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(2)));
		// cf_dynamic:country!=CN
		conditions.addCondition(new Condition("cf_dynamic", "country", CompareOp.NOT_EQUAL, Bytes.toBytes("CN")));
		client.scanByCompositeConditions(tableName, conditions);
	}
	
	@After
	public void close() throws IOException {
		
	}
}
