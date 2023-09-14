package it.kamaladafrica.codicefiscale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class CodiceFiscaleTest {

	private final Person PERSON = Person.builder().firstname("Mario").lastname("Rossi")
			.birthDate(LocalDate.of(1975, 3, 22)).isFemale(false)
			.city(City.builder().name("ROMA").prov("RM").belfiore("H501").build()).build();

	private final String CODICE_FISCALE = "RSSMRA75C22H501I";
	private final String CODICE_FISCALE_1 = "RSSMRA75C22H50MA";
	private final String CODICE_FISCALE_2 = "RSSMRA75C22H5LML";

	@Test
	public void testNormalized() {
		assertEquals(CodiceFiscale.of(CODICE_FISCALE_2).normalized().getValue(), CODICE_FISCALE);
	}

	@Test
	public void testIsEqualString() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE_2));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual("RSSMRA75C21H501D"));
	}

	@Test
	public void testIsEqualCodiceFiscale() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE)));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE_2)));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of("RSSMRA75C21H501D")));
	}

	@Test
	public void testIsEqualStringBoolean() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE, true));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE_2, true));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual("RSSMRA75C21H501D", true));

		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE, false));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CODICE_FISCALE_2, false));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual("RSSMRA75C21H501D", false));
	}

	@Test
	public void testIsEqualCodiceFiscaleBoolean() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE), true));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE_2), true));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of("RSSMRA75C21H501D"), true));

		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE), false));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of(CODICE_FISCALE_2), false));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_2).isEqual(CodiceFiscale.of("RSSMRA75C21H501D"), false));
	}

	@Test
	public void testToOmocodeLevel() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_1).toOmocodeLevel(0).isEqual(CODICE_FISCALE, false));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_1).toOmocodeLevel(3).isEqual(CODICE_FISCALE_2, false));
		assertFalse(CodiceFiscale.of(CODICE_FISCALE_1).toOmocodeLevel(3).isEqual(CODICE_FISCALE, false));

		assertThrows(IllegalArgumentException.class, () -> CodiceFiscale.of(CODICE_FISCALE_1).toOmocodeLevel(-5));
		assertThrows(IllegalArgumentException.class, () -> CodiceFiscale.of(CODICE_FISCALE_1).toOmocodeLevel(200));
}

	@Test
	public void testOfString() {
		CodiceFiscale cf = CodiceFiscale.of(CODICE_FISCALE);
		assertEquals("RSS", cf.getLastname().getValue());
		assertEquals("MRA", cf.getFirstname().getValue());
		assertEquals("75C22", cf.getDate().getValue());
		assertEquals("H501", cf.getBelfiore().getValue());
		assertEquals("I", cf.getControl().getValue());
		assertEquals(0, cf.getOmocodeLevel());
		assertEquals(Person.builder().lastname("RSS").firstname("MRA").birthDate(PERSON.getBirthDate())
				.city(PERSON.getCity()).isFemale(PERSON.isFemale()).build(), cf.getPerson());
	}

	@Test
	public void testOfPerson() {
		CodiceFiscale cf = CodiceFiscale.of(PERSON);

		assertEquals(PERSON, cf.getPerson());
		assertEquals(CODICE_FISCALE, cf.getValue());
	}

	@Test
	public void testIsCompatible() {
		assertTrue(CodiceFiscale.of(CODICE_FISCALE).isCompatible(PERSON));
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isCompatible(PERSON));
		assertFalse(CodiceFiscale.of("RSSMRA75C21H501D").isCompatible(PERSON));
	}

	@Test
	public void testIsCompatibleStatic() {
		assertTrue(CodiceFiscale.isCompatible(CODICE_FISCALE, PERSON));
		assertTrue(CodiceFiscale.isCompatible(CODICE_FISCALE_2, PERSON));
		assertFalse(CodiceFiscale.isCompatible("RSSMRA75C21H501D", PERSON));
	}

	@Test
	public void testIsFormatValid() {
		assertTrue(CodiceFiscale.isFormatValid(CODICE_FISCALE));
		assertTrue(CodiceFiscale.isFormatValid(CODICE_FISCALE_2));
		assertFalse(CodiceFiscale.isFormatValid("RSSMRA75C21H501X"));
		assertFalse(CodiceFiscale.isFormatValid("RA75C21H501X"));
	}

	@Test
	public void testValidate() {
		assertThrows(IllegalArgumentException.class, () -> CodiceFiscale.validate("RSSMRA75C21H501X"));
		assertThrows(IllegalArgumentException.class, () -> CodiceFiscale.validate("RSSMRAXXC21H501X"));
	}

	@Test
	public void testGetValue() {
		assertEquals(CODICE_FISCALE, CodiceFiscale.of(PERSON).getValue());
		assertEquals(CODICE_FISCALE, CodiceFiscale.of(CODICE_FISCALE).getValue());
		assertEquals(CODICE_FISCALE_2, CodiceFiscale.of(PERSON).toOmocodeLevel(3).getValue());
		assertEquals(CODICE_FISCALE_2, CodiceFiscale.of(CODICE_FISCALE).toOmocodeLevel(3).getValue());
	}

	@Test
	public void testIsOmocode() {
		assertFalse(CodiceFiscale.of(CODICE_FISCALE).isOmocode());
		assertTrue(CodiceFiscale.of(CODICE_FISCALE_2).isOmocode());
		assertFalse(CodiceFiscale.of(PERSON).isOmocode());
		assertTrue(CodiceFiscale.of(PERSON).toOmocodeLevel(7).isOmocode());
	}

	@Test
	public void testDiacritics() {
		Person p1 = Person.builder().firstname("RENEŽ").lastname("FæO").birthDate(LocalDate.of(1975, 3, 22))
				.isFemale(false).city(City.builder().name("ROMA").prov("RM").belfiore("H501").build()).build();
		Person p2 = Person.builder().firstname("RENEZ").lastname("FAEO").birthDate(LocalDate.of(1975, 3, 22))
				.isFemale(false).city(City.builder().name("ROMA").prov("RM").belfiore("H501").build()).build();

		assertEquals(CodiceFiscale.of(p1).getValue(), CodiceFiscale.of(p2).getValue());
		assertEquals("FAERNZ75C22H501I", CodiceFiscale.of(p1).getValue());
	}

}
