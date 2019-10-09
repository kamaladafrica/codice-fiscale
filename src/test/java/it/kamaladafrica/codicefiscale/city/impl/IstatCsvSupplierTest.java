package it.kamaladafrica.codicefiscale.city.impl;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;

public class IstatCsvSupplierTest {

	@Test
	public void testOf() {
		Set<City> cities = IstatCsvSupplier.of(getClass().getResource(CityProviderImpl.ISTAT_RESOURCE_PATH)).get();
		Assert.assertFalse(cities.isEmpty());
	}

}
