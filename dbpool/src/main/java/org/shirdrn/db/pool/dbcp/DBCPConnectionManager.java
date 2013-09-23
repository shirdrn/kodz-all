package org.shirdrn.db.pool.dbcp;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.db.pool.ConnectionManager;
import org.shirdrn.db.pool.utils.PropertiesReader;

public final class DBCPConnectionManager implements ConnectionManager {
	
	private static final Log LOG = LogFactory.getLog(DBCPConnectionManager.class);
	private BasicDataSource ds;
	private static String DBCP_PROPERTIES = "dbcp.properties";
	private String config;
	private final PropertiesReader reader;
	
	public DBCPConnectionManager() {
		this.config = DBCP_PROPERTIES;
		reader = new PropertiesReader(config);
		ds = new BasicDataSource();
		LOG.info("Configure datasource: ds=" + ds);
		setDs();
	}
	
	public DBCPConnectionManager(String prop) {
		this.config = prop;
		reader = new PropertiesReader(config);
		ds = new BasicDataSource();
		LOG.info("Configure datasource: ds=" + ds);
		setDs();
	}
	
	private void setDs() {
		ds.setUrl(reader.get("jdbc.url", null));
		ds.setDriverClassName(reader.get("jdbc.driverClassName", null));
		ds.setUsername(reader.get("jdbc.username", ""));
		ds.setPassword(reader.get("jdbc.password", ""));
		ds.setInitialSize(reader.getInt("initialSize", 1));
		ds.setMaxActive(reader.getInt("maxActive", 1));
		ds.setMinIdle(reader.getInt("minIdle", 0));
		ds.setMaxIdle(reader.getInt("maxIdle", 0));
		ds.setMaxWait(reader.getLong("maxWait", 0L));
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
			try {
				ds.close();
			} catch (SQLException e) {
				e.printStackTrace();
			};
		}
	}

}
