package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class DatePartTest {

	private final LocalDate DATE = LocalDate.of(1975, 3, 21);
	private final LocalDate DATE_MILLENNIUM = LocalDate.of(2015, 3, 21);

	@Test
	public void testFrom() {
		assertEquals(DatePart.from("15C21").getDate(), DATE_MILLENNIUM);
		assertEquals(DatePart.from("15C61").getDate(), DATE_MILLENNIUM);
		assertFalse(DatePart.from("15C21").isFemale());
		assertTrue(DatePart.from("15C61").isFemale());

		assertEquals(DatePart.from("75C21").getDate(), DATE);
		assertEquals(DatePart.from("75C61").getDate(), DATE);
		assertFalse(DatePart.from("75C21").isFemale());
		assertTrue(DatePart.from("75C61").isFemale());
	}

	@Test
	public void testOf() {
		assertEquals("75C21", DatePart.of(DATE, false).getValue());
		assertEquals("75C61", DatePart.of(DATE, true).getValue());
	}

	@Test
	public void testToOmocodeLevel() {
		assertEquals("75C6M", DatePart.from("75C61").toOmocodeLevel(1).getValue());
	}

}
