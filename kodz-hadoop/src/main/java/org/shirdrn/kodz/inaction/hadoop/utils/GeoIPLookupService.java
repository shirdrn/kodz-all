package org.shirdrn.kodz.inaction.hadoop.utils;

import java.io.IOException;
import java.net.URL;

import org.shirdrn.kodz.inaction.hadoop.utils.maxmind.api.Country;
import org.shirdrn.kodz.inaction.hadoop.utils.maxmind.api.LookupService;

public class GeoIPLookupService {

	private static String GEO_IP_FILE = "GeoIP-db.dat";
	private LookupService lookupService;
	private static GeoIPLookupService INSTANCE = null;
	static {
		INSTANCE = new GeoIPLookupService();
	}
	
	private GeoIPLookupService() {
		try {
			URL u = this.getClass().getClassLoader().getResource(GEO_IP_FILE);
			lookupService = new LookupService(u.getFile(), LookupService.GEOIP_MEMORY_CACHE);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if(lookupService != null) {
						lookupService.close();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GeoIPLookupService newInstance() {
		return INSTANCE;
	}
	
	public Country getCountry(String domain) {
		return INSTANCE.lookupService.getCountry(domain);
	}

}
