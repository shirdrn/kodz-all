package org.shirdrn.kodz.inaction.hadoop.dataset;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCountryCodeSet {

	@Test
	public void getSize() {
		assertEquals(245, 
				CountryCodeSet.newInstance().getCountryCodes().size());
	}
}
