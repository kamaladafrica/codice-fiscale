package it.kamaladafrica.codicefiscale;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.kamaladafrica.codicefiscale.city.CityProvider;

public class Issue30Test {

	@Test
	public void testSuppressedCountriesCodes() {
		CityProvider provider = CityProvider.ofDefault();

		assertEquals("UNIONE SOVIETICA", provider.findByBelfiore("Z135").getName());
		assertEquals("Z135", provider.findByName("UNIONE SOVIETICA").getBelfiore());
	}

}
