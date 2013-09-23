package org.shirdrn.db.pool;

import java.io.Closeable;
import java.sql.Connection;

public interface ConnectionManager extends Closeable {
	Connection getConnection();
}
