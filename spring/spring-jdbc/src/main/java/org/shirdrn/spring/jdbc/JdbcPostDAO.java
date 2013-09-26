package org.shirdrn.spring.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcPostDAO implements PostDAO {

	private JdbcTemplate jdbcTemplate;
	private RowMapper<Post> rowMapper;
	
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	public void setRowMapper(RowMapper<Post> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	@Override
	public Post queryForObject(int id) {
		return jdbcTemplate.queryForObject(
				"SELECT * FROM wordpress.wp_posts where id=" + id, rowMapper);
	}
	
	@Override
	public List<Post> queryForList(int author) {
		return jdbcTemplate.query(
				"SELECT * FROM wordpress.wp_posts where post_author=" + author, rowMapper);
	}



}
