package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class DatePartTest {

	private final LocalDate DATE = LocalDate.of(1975, 3, 21);

	@Test
	public void testFrom() {
		assertEquals(DatePart.from("75C21").getDate(), DATE);
		assertEquals(DatePart.from("75C61").getDate(), DATE);
		assertFalse(DatePart.from("75C21").isFemale());
		assertTrue(DatePart.from("75C61").isFemale());
	}

	@Test
	public void testOf() {
		assertEquals(DatePart.of(DATE, false).getValue(), "75C21");
		assertEquals(DatePart.of(DATE, true).getValue(), "75C61");
	}

	@Test
	public void testToOmocodeLevel() {
		assertEquals(DatePart.from("75C61").toOmocodeLevel(4).getValue(), "75C6M");
	}

}
