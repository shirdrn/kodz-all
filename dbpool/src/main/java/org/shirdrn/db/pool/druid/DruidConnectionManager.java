package org.shirdrn.db.pool.druid;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.shirdrn.db.pool.ConnectionManager;
import org.shirdrn.db.pool.c3p0.C3p0ConnectionManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DruidConnectionManager implements ConnectionManager {

	private static final Log LOG = LogFactory.getLog(DruidConnectionManager.class);
	private DataSource dataSource;
	private static String DEFAULT_PROPERTIES = "druid.properties";
	
	public DruidConnectionManager() {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES));
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	@Override
	public void close() throws IOException {
		
	}


}
