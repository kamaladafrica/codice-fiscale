package it.kamaladafrica.codicefiscale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class Issue6Test {

	private final Person PERSON = Person.builder().firstname("ALESSANDRO").lastname("MANCINELLI")
			.birthDate(LocalDate.of(1975, 3, 22)).isFemale(false)
			.city(City.builder().name("DOLO").prov("VE").belfiore("D325").build()).build();

	@Test
	public void test_omocode() {
		CodiceFiscale cf1 = CodiceFiscale.of("MNCLSN75C22D325B");
		CodiceFiscale cf2 = CodiceFiscale.of("MNCLSN75C22D3N5M");

		assertEquals("MNCLSN75C22D325B", cf1.getValue());
		assertEquals("MNCLSN75C22D3N5M", cf2.getValue());

		assertEquals(cf1.getValue(), cf2.normalized().getValue());
		assertTrue(cf1.isCompatible(cf2.getPerson()));
		assertTrue(cf2.isCompatible(cf1.getPerson()));

		assertEquals(2, cf2.getOmocodeLevel());

		assertEquals(cf1.getValue(), CodiceFiscale.of(PERSON).getValue());
		assertEquals(cf2.getValue(), CodiceFiscale.of(PERSON).toOmocodeLevel(2).getValue());
	}

}
