package it.kamaladafrica.codicefiscale.city.impl;

import static org.junit.Assert.assertFalse;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.impl.csv.EsteriCsvSupplier;

public class EsteriCsvSupplierTest {

	private static final String ENCODING_ERROR_MARKER = "\uFFFD";

	@Test
	public void testOf() {
		Set<City> cities = EsteriCsvSupplier.of(getClass().getResource(CityProviderImpl.ESTERI_RESOURCE_PATH)).get()
				.collect(Collectors.toSet());
		Assert.assertFalse(cities.isEmpty());
	}

	@Test
	public void testEncoding() {
		EsteriCsvSupplier.of(getClass().getResource(CityProviderImpl.ESTERI_RESOURCE_PATH)).get().forEach(c -> {
			assertFalse(c.getBelfiore() + ": belfiore encode error", c.getBelfiore().contains(ENCODING_ERROR_MARKER));
			assertFalse(c.getName() + ": name encode error", c.getName().contains(ENCODING_ERROR_MARKER));
			assertFalse(c.getProv() + ": prov encode error", c.getProv().contains(ENCODING_ERROR_MARKER));
		});
	}
}
