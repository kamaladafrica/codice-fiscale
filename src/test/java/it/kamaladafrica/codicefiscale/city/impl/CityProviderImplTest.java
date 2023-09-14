package it.kamaladafrica.codicefiscale.city.impl;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assume;
import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.utils.TestUtils;

public class CityProviderImplTest {

	private final Map<String, City> CITIES = Collections.unmodifiableMap(new LinkedHashMap<String, City>() {
		private static final long serialVersionUID = 1L;
		{
			put("ZAGAROLO(RM)", City.builder().name("ZAGAROLO").prov("RM").belfiore("C789").build());
			put("MORLUPO(RM)", City.builder().name("MORLUPO").prov("RM").belfiore("B456").build());
			put("CAPENA(RM)", City.builder().name("CAPENA").prov("RM").belfiore("A123").build());
		}
	});

	@Test
	public void testOfSet() {
		final Set<City> source = TestUtils.unmodifiableSet(CITIES.values());
		Assume.assumeThat(source.iterator().next(), is(CITIES.get("ZAGAROLO(RM)")));

		final CityProviderImpl provider = CityProviderImpl.of(source);
		final List<City> cities = provider.findAll();
		assertEquals(CITIES.size(), cities.size());
		assertThat(cities, hasItems(CITIES.values().toArray(new City[0])));
		assertEquals("CAPENA", cities.iterator().next().getName());
		assertEquals("MORLUPO", provider.findByName("MORLOPE").getName());
	}

	@Test
	public void testOfSupplier() {
		final Set<City> source = TestUtils.unmodifiableSet(CITIES.values());
		Assume.assumeThat(source.iterator().next(), is(CITIES.get("ZAGAROLO(RM)")));

		final CityProviderImpl provider = CityProviderImpl.of(() -> source);
		final List<City> cities = provider.findAll();
		assertEquals(CITIES.size(), cities.size());
		assertThat(cities, hasItems(CITIES.values().toArray(new City[0])));
		assertEquals("CAPENA", cities.iterator().next().getName());
		assertEquals("MORLUPO", provider.findByName("MORLOPE").getName());
	}

	@Test
	public void testOfSetScore() {
		final Set<City> source = TestUtils.unmodifiableSet(CITIES.values());
		Assume.assumeThat(source.iterator().next(), is(CITIES.get("ZAGAROLO(RM)")));

		final CityProviderImpl provider = CityProviderImpl.of(source, CityProviderImpl.EXACT_MATCH_SCORE);
		final List<City> cities = provider.findAll();
		assertEquals(CITIES.size(), cities.size());
		assertThat(cities, hasItems(CITIES.values().toArray(new City[0])));
		assertEquals("CAPENA", cities.iterator().next().getName());
		try {
			provider.findByName("MORLOPE");
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testOfSupplierScore() {
		final Set<City> source = TestUtils.unmodifiableSet(CITIES.values());
		Assume.assumeThat(source.iterator().next(), is(CITIES.get("ZAGAROLO(RM)")));

		final CityProviderImpl provider = CityProviderImpl.of(() -> source, CityProviderImpl.EXACT_MATCH_SCORE);
		final List<City> cities = provider.findAll();
		assertEquals(CITIES.size(), cities.size());
		assertThat(cities, hasItems(CITIES.values().toArray(new City[0])));
		assertEquals("CAPENA", cities.iterator().next().getName());
		try {
			provider.findByName("MORLOPE");
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testOfDefault() {
		final CityProviderImpl provider = CityProviderImpl.ofDefault();
		final City romeByBelfiore = provider.findByBelfiore("H501");
		assertEquals("ROMA", romeByBelfiore.getName());
		final City romeByName = provider.findByName("Roma");
		assertEquals(romeByBelfiore, romeByName);

		final City albaniaByBelfiore = provider.findByBelfiore("Z100");
		assertEquals("ALBANIA", albaniaByBelfiore.getName());

		final City albaniaByName = provider.findByName("Albania");
		assertEquals(albaniaByName, albaniaByBelfiore);

		assertTrue(provider.findAll().size() > 1000);
	}

	@Test
	public void testFindAll() {
		final CityProviderImpl provider = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		final List<City> cities = provider.findAll();
		assertEquals(CITIES.size(), cities.size());
		assertThat(cities, hasItems(CITIES.values().toArray(new City[0])));
	}

	@Test
	public void testFindByNameExact() {
		final CityProviderImpl providerExact = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		assertEquals("MORLUPO", providerExact.findByName("MORLUPO(RM)").getName());
	}

	@Test
	public void testFindByNameNotExact() {
		final CityProviderImpl providerNotExact = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.DEFAULT_MINIMUM_MATCH_SCORE);
		assertEquals("MORLUPO", providerNotExact.findByName("MORLUPO(RM)").getName());
		assertEquals("MORLUPO", providerNotExact.findByName("MORLUPO (RM)").getName());
		assertEquals("MORLUPO", providerNotExact.findByName("MORLUPO RM").getName());
		assertEquals("MORLUPO", providerNotExact.findByName("MOLUPO").getName());
		assertEquals("MORLUPO", providerNotExact.findByName("MORLOPE").getName());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindByNameExactNotFound() {
		final CityProviderImpl providerExact = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		providerExact.findByName("MORLOPE");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindByNameEmptyTerm() {
		final CityProviderImpl provider = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		provider.findByName(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindByNameNotExactNotFound() {
		final CityProviderImpl providerExact = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.DEFAULT_MINIMUM_MATCH_SCORE);
		providerExact.findByName("XXXX");
	}

	@Test
	public void testFindByBelfiore() {
		final CityProviderImpl provider = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		assertEquals("MORLUPO", provider.findByBelfiore("B456").getName());
	}

	@Test
	public void testFindByBelfioreNotFound() {
		final CityProviderImpl provider = CityProviderImpl.of(TestUtils.unmodifiableSet(CITIES.values()),
				CityProviderImpl.EXACT_MATCH_SCORE);
		assertThrows(IllegalArgumentException.class, () -> provider.findByBelfiore("XXXX"));
	}
	
}
