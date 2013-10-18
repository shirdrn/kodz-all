package org.shirdrn.queryproxy.thrift.service.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.shirdrn.queryproxy.common.Configurable;
import org.shirdrn.queryproxy.common.ConfiguredQueryService;
import org.shirdrn.queryproxy.thrift.protocol.QueryFailureException;
import org.shirdrn.queryproxy.thrift.protocol.QueryParams;
import org.shirdrn.queryproxy.thrift.protocol.QueryResult;
import org.shirdrn.queryproxy.utils.PropertiesConfig;
import org.shirdrn.queryproxy.utils.ResultUtils;

public class SQLQueryService extends ConfiguredQueryService {

	private static final Log LOG = LogFactory.getLog(SQLQueryService.class);
	private static String JDBC_PROPERTIES = "jdbc.properties";
	Configurable jdbcConf;
	private String jdbcUrl;
	private String user;
	private String password;
	Connection connection;
	
	public SQLQueryService(Configurable context) {
		super(context);
		jdbcConf = new PropertiesConfig(JDBC_PROPERTIES);
		String driverClass = jdbcConf.get("jdbc.driverClass");
		try {
			Class.forName(driverClass);
			jdbcUrl = jdbcConf.get("jdbc.jdbcUrl");
			user = jdbcConf.get("jdbc.user");
			password = jdbcConf.get("jdbc.password");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			LOG.info("JDBC: driver=" + driverClass + ", url=" + jdbcUrl + ", user=" + user + ", password=******");
		}
	}

	@Override
	public QueryResult query(QueryParams params) throws QueryFailureException, TException {
		QueryResult result = new QueryResult();
		if(!params.getParamList().isEmpty()) {
			// get SQL statement
			String sql = params.getParamList().remove(0);
			Connection conn = getConnection();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				result.setResults(ResultUtils.getJSONResults(rs, params.getParamList()));
			} catch (SQLException e) {
				throw new QueryFailureException(e.toString());
			}
		}
		return result;
	}
	
	private synchronized final Connection getConnection() {
		try {
			if(connection == null || !connection.isClosed()) {
				if(user != null) {
					connection = DriverManager.getConnection(jdbcUrl, user, password);
				} else {
					connection = DriverManager.getConnection(jdbcUrl);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public void close() throws IOException {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new IOException(e);
			}
		}
	}


}
