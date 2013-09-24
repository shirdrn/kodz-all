package org.shirdrn.db.pool.bonecp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.shirdrn.db.pool.ConnectionManager;
import org.shirdrn.db.pool.utils.PropertiesReader;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class BoneCPConnectionManager implements ConnectionManager {

	private BoneCP pool;
	private final PropertiesReader reader;
	
	@SuppressWarnings("deprecation")
	public BoneCPConnectionManager(String config) {
		reader = new PropertiesReader(config);
		try {
			BoneCPConfig bonecpConfig = null;
			if(config != null) {
				String driverClass = reader.get("bonecp.driverClass");
				Class.forName(driverClass);
				bonecpConfig = new BoneCPConfig();
				bonecpConfig.setJdbcUrl(reader.get("bonecp.jdbcUrl", null));
				bonecpConfig.setUser(reader.get("bonecp.username", ""));
				bonecpConfig.setPassword(reader.get("bonecp.password", ""));
				bonecpConfig.setPartitionCount(reader.getInt("bonecp.partitionCount", 1));
				bonecpConfig.setInitSQL(reader.get("bonecp.initSQL", "SELECT 1"));
				bonecpConfig.setAcquireIncrement(reader.getInt("bonecp.acquireIncrement", 2));
				bonecpConfig.setAcquireRetryAttempts(reader.getInt("bonecp.acquireRetryAttempts", 5));
				bonecpConfig.setMinConnectionsPerPartition(reader.getInt("bonecp.minConnectionsPerPartition", 0));
				bonecpConfig.setMaxConnectionsPerPartition(reader.getInt("bonecp.maxConnectionsPerPartition", 10));
				bonecpConfig.setMaxConnectionAgeInSeconds(reader.getLong("bonecp.maxConnectionAgeInSeconds", 0L));
				bonecpConfig.setStatementCacheSize(reader.getInt("bonecp.statementsCacheSize", 0));
				bonecpConfig.setAcquireRetryDelay(reader.getInt("bonecp.acquireRetryDelayInMs", 7000));
				bonecpConfig.setReleaseHelperThreads(reader.getInt("bonecp.releaseHelperThreads", 0));
			} else {
				throw new Exception("config==null");
			}
			pool = new BoneCP(bonecpConfig);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = pool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	@Override
	public void close() throws IOException {
		if(pool != null) {
			pool.close();
		}
	}

}
