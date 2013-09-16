package org.shirdrn.kodz.inaction.hbase.api;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.hbase.client.HTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStoreFileLinesToHBase {

	StoreFileLinesToHBase client;
	String tableName;
	String file;
	HTable table;
	
	@Before
	public void initialize() throws IOException {
		tableName = "all_domains";
		client = new StoreFileLinesToHBase();
		table = StoreFileLinesToHBase.openTable(tableName);
		file = "/home/shirdrn/storage/domains/all_domains";
	}
	
	@Test
	public void store() {
		try {
			client.storeAll(table, file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void close() throws IOException {
		table.close();
	}
}
