package org.shirdrn.db.pool.bonecp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringManagedBoneCP {

	public static void main(String[] args) throws SQLException {
		final String config = "applicationContext-bonecp.xml";
		AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
		context.start();
		
		DataSource ds = (DataSource) context.getBean("dataSource");
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from wp_users");
		while(rs.next()) {
			System.out.println(rs.getBoolean("user_login"));
		}
	}

}
