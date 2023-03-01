package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;

public class BelfiorePartTest {

	private final static City ROMA = City.builder().name("ROMA").prov("RM").belfiore("H501").build();

	@Test
	public void testFromString() {
		assertEquals("H501", BelfiorePart.from("H501").getValue());
		assertEquals(ROMA, BelfiorePart.from("H501").getCity());

		assertEquals("H50M", BelfiorePart.from("H50M").getValue());
		assertEquals(ROMA, BelfiorePart.from("H50M").getCity());
	}

	@Test
	public void testOf() {
		assertEquals("H501", BelfiorePart.of(ROMA).getValue());
		assertEquals(ROMA, BelfiorePart.from("H501").getCity());
	}

	@Test
	public void testToOmocodeLevel() {
		assertEquals("H50M", BelfiorePart.of(ROMA).toOmocodeLevel(1).getValue());
		assertEquals("H501", BelfiorePart.of(ROMA).toOmocodeLevel(1).toOmocodeLevel(0).getValue());
	}

	@Test
	public void testGetValue() {
		assertEquals("H501", BelfiorePart.of(ROMA).getValue());
	}

}
