package org.shirdrn.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PostRowMapper implements RowMapper<Post> {

	@Override
	public Post mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Post post = new Post();
		post.setId(rs.getInt("ID"));
		post.setPostAuthor(rs.getInt("post_author"));
		post.setPostDate(rs.getString("post_date"));
		post.setPostContent(rs.getString("post_content"));
		return post;
	}

}
