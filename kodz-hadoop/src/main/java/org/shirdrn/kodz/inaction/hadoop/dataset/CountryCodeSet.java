package org.shirdrn.kodz.inaction.hadoop.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountryCodeSet {
	
	private static List<String> CODES = new ArrayList<String>();
	private static final String WORKSPACE = System.getProperty("user.dir");
	private static final String SEPARATOR = getPathSeparator();
	private static final String CODE_FILE = WORKSPACE + SEPARATOR
			+ "src" + SEPARATOR + "main" + SEPARATOR + "resources"
			+ SEPARATOR + "country_codes";
	private static final CountryCodeSet INSTANCE = new CountryCodeSet();

	static {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					CODE_FILE)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					CODES.add(line.trim());
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getPathSeparator() {
		String os = System.getProperty("os.name");
		String pathSeparator = null;
		if (os.contains("windows")) {
			pathSeparator = "\\";
		} else {
			pathSeparator = "/";
		}
		return pathSeparator;
	}

	private CountryCodeSet() {
		super();
	}

	public static CountryCodeSet newInstance() {
		return INSTANCE;
	}

	public List<String> getCountryCodes() {
		return CODES;
	}
}
