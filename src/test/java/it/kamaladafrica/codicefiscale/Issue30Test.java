package it.kamaladafrica.codicefiscale;

import it.kamaladafrica.codicefiscale.city.CityProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Issue30Test {

	@Test
	public void testSuppressedCountriesCodes() {
		CityProvider provider = CityProvider.ofDefault();

		assertEquals("UNIONE SOVIETICA", provider.findByBelfiore("Z135").getName());
		assertEquals("Z135", provider.findByName("UNIONE SOVIETICA").getBelfiore());
	}

}
