package org.shirdrn.db.pool.bonecp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Test;
import org.shirdrn.db.pool.ConnectionManager;

public class TestBoneCPConnectionManager {

	ConnectionManager cm;
	
	@Test
	public void getConnection() throws Exception {
		cm = new BoneCPConnectionManager("bonecp-test.properties");
		Connection conn = cm.getConnection();
		assertNotNull(conn);
		
		select(conn);
	}

	private void select(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from wp_users");
		while(rs.next()) {
			String userLogin = rs.getString("user_login");
			System.out.println(userLogin);
		}
	}
	
	@After
	public void close() throws IOException {
		cm.close();
	}
}
