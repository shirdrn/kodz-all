package org.shirdrn.spring.jdbc;

import java.util.List;

public interface PostDAO {

	Post queryForObject(int id);
	List<Post> queryForList(int author);
}
