package org.shirdrn.queryproxy.common;

import java.io.Closeable;

import org.shirdrn.queryproxy.thrift.protocol.QueryProxyService.Iface;

public abstract class ConfiguredQueryService implements Iface, Closeable {
	
	protected final Configurable context;
	
	public ConfiguredQueryService(Configurable context) {
		super();
		this.context = context;
	}
}
