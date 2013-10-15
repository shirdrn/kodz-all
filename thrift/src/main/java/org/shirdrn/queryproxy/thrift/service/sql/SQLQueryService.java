package org.shirdrn.queryproxy.thrift.service.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.thrift.TException;
import org.shirdrn.queryproxy.common.Configurable;
import org.shirdrn.queryproxy.common.ConfiguredQueryService;
import org.shirdrn.queryproxy.thrift.protocol.QueryFailureException;
import org.shirdrn.queryproxy.thrift.protocol.QueryParams;
import org.shirdrn.queryproxy.thrift.protocol.QueryResult;
import org.shirdrn.queryproxy.utils.ResultUtils;

public class SQLQueryService extends ConfiguredQueryService {

	public SQLQueryService(Configurable context) {
		super(context);
	}

	@Override
	public QueryResult query(QueryParams params) throws QueryFailureException, TException {
		String table = params.getTable();
		String sql = params.getParams().get(0);
		ResultSet rs = null;
		// execute SQL statement
		// ...
		QueryResult result = new QueryResult();
		try {
			result.setResults(ResultUtils.extractJSONResults(rs));
		} catch (SQLException e) {
			throw new QueryFailureException(e.toString());
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}


}
