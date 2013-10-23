package org.shirdrn.tuscany.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.oasisopen.sca.annotation.Remotable;

@Remotable
public interface JaxrsSolrSearchService {
	
	@GET
    @Path("search")
    @Produces(MediaType.TEXT_PLAIN)
	String search(
			@QueryParam("collection") String collection, 
			@QueryParam("params") String params, 
			@QueryParam("start") int start, 
			@QueryParam("rows") int rows
			);
}
