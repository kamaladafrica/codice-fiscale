package it.kamaladafrica.codicefiscale.city.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;

public class ItaliaCsvSupplierTest {

	@Test
	public void testOf() {
		Set<City> cities = ItaliaCsvSupplier.of(getClass().getResource(CityProviderImpl.ITALIA_RESOURCE_PATH)).get()
				.collect(Collectors.toSet());
		Assert.assertFalse(cities.isEmpty());
	}

}
