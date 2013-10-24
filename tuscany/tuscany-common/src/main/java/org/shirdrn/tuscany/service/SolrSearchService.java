package org.shirdrn.tuscany.service;

import org.oasisopen.sca.annotation.Remotable;

@Remotable
public interface SolrSearchService {

    String search(String collection, String params, int start, int rows);

}
