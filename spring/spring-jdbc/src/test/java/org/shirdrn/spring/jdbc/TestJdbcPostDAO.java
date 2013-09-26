package org.shirdrn.spring.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJdbcPostDAO {

	PostDAO postDAO;
	
	@Before
	public void initialize() {
		String config = "applicationContext-dbcp-test.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
        postDAO = (PostDAO) context.getBean("postDao");
	}
	
	@Test
	public void queryForObject() {
		int id = 100;
		Post post = postDAO.queryForObject(id);
		System.out.println(post);
	}
	
	@Test
	public void queryForList() {
		int author = 2;
		for(Post post : postDAO.queryForList(author)) {
			System.out.println(post);
		}
	}
}
