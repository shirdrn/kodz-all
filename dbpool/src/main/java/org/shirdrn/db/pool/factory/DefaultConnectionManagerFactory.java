package org.shirdrn.db.pool.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.shirdrn.db.pool.ConnectionManager;
import org.shirdrn.db.pool.ConnectionManagerFactory;

public class DefaultConnectionManagerFactory implements ConnectionManagerFactory {

	private static final ConcurrentMap<Class<? extends ConnectionManager>, ConnectionManager> CACHED = 
			new ConcurrentHashMap<Class<? extends ConnectionManager>, ConnectionManager>();
	
	@Override
	public ConnectionManager getConnectionManager(Class<? extends ConnectionManager> managerClass) {
		return CACHED.get(managerClass);
	}
	
	@Override
	public void register(Class<? extends ConnectionManager> managerClass, ConnectionManager manager) {
		CACHED.putIfAbsent(managerClass, manager);
	}

}
