package it.kamaladafrica.codicefiscale.city.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;

public class CompositeCityStreamSupplierTest {

	CompositeCityStreamSupplier uut = CompositeCityStreamSupplier.of(
			() -> Stream.of(
					City.builder().name("ROMA").prov("RM").belfiore("A").build(),
					City.builder().name("VITERBO").prov("VT").belfiore("B").build()),
			() -> Stream.of(City.builder().name("MILANO").prov("MI").belfiore("C").build()),
			() -> Stream.of(
					City.builder().name("GENOVA").prov("GE").belfiore("D").build(),
					City.builder().name("LA SPEZIA").prov("SP").belfiore("E").build()));

	@Test
	public void testGet() {
		List<String> result = uut.get().map(City::getName).collect(Collectors.toList());
		assertEquals(5, result.size());
		assertTrue(result.contains("ROMA"));
		assertTrue(result.contains("VITERBO"));
		assertTrue(result.contains("MILANO"));
		assertTrue(result.contains("GENOVA"));
		assertTrue(result.contains("LA SPEZIA"));
	}

	@Test
	public void testOfCityStreamSupplierCityStreamSupplierArray() {
		assertEquals(3, uut.getSuppliers().length);
		assertEquals(1,
				CompositeCityStreamSupplier
						.of(() -> Stream.of(City.builder().name("ROMA").prov("RM").belfiore("A").build()),
								new CompositeCityStreamSupplier[0])
						.getSuppliers().length);
		assertEquals(1,
				CompositeCityStreamSupplier
						.of(() -> Stream.of(City.builder().name("ROMA").prov("RM").belfiore("A").build()),
								(CompositeCityStreamSupplier[]) null)
						.getSuppliers().length);
	}

}
