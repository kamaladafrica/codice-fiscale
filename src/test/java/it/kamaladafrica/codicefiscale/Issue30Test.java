package it.kamaladafrica.codicefiscale;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class Issue30Test {

	private final Person PERSON = Person.builder().firstname("NLA").lastname("TTV")
			.birthDate(LocalDate.of(1961, 2, 15)).isFemale(false)
			.city(City.builder().name("UNIONE SOVIETICA").prov("EUROPA").belfiore("Z135").build()).build();

	@Test
	public void test_esteri_codes() {
		CodiceFiscale cf = CodiceFiscale.of("TTVNLA61B15Z135B");

		assertEquals("TTVNLA61B15Z135B", cf.getValue());

		assertEquals(cf.getValue(), CodiceFiscale.of(PERSON).getValue());
		assertEquals(cf.getPerson(), PERSON);

		assertEquals(cf.getBelfiore().getValue(), "Z135");
		assertEquals(cf.getBelfiore().getCity().getName(), "UNIONE SOVIETICA");
		assertEquals(cf.getBelfiore().getCity().getProv(), "EUROPA");
	}

}
