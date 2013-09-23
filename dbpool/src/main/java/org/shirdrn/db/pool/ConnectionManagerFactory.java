package org.shirdrn.db.pool;

public interface ConnectionManagerFactory {
	void register(Class<? extends ConnectionManager> managerClass, ConnectionManager manager);
	ConnectionManager getConnectionManager(Class<? extends ConnectionManager> managerClass);
}
