package org.shirdrn.db.pool.c3p0;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.db.pool.ConnectionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class C3p0ConnectionManager implements ConnectionManager {
	
	private static final Log LOG = LogFactory.getLog(C3p0ConnectionManager.class);
	private ComboPooledDataSource ds;
	private static String C3P0_PROPERTIES = "c3p0.properties";
	private String config;
	
	public C3p0ConnectionManager() {
		this.config = C3P0_PROPERTIES;
		ds = new ComboPooledDataSource(config);
		LOG.info("Create datasource: ds=" + ds);
	}
	
	public C3p0ConnectionManager(String config) {
		LOG.info("Config file: config=" + config);
		ds = new ComboPooledDataSource(config);
		LOG.info("Create datasource: ds=" + ds);
	}
	
	@Override
	public synchronized final Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		if(ds != null) {
			ds.close();
		}
	}

}
