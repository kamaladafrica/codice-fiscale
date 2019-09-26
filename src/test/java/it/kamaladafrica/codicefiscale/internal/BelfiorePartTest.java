package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.kamaladafrica.codicefiscale.City;

public class BelfiorePartTest {

	private final static City ROMA = City.builder().name("ROMA").prov("RM").belfiore("H501").build();

	@Test
	public void testFromString() {
		assertEquals(BelfiorePart.from("H501").getValue(), "H501");
		assertEquals(BelfiorePart.from("H501").getCity(), ROMA);

		assertEquals(BelfiorePart.from("H50M").getValue(), "H50M");
		assertEquals(BelfiorePart.from("H50M").getCity(), ROMA);
	}

	@Test
	public void testOf() {
		assertEquals(BelfiorePart.of(ROMA).getValue(), "H501");
		assertEquals(BelfiorePart.from("H501").getCity(), ROMA);
	}

	@Test
	public void testToOmocodeLevel() {
		assertEquals(BelfiorePart.of(ROMA).toOmocodeLevel(1).getValue(), "H50M");
		assertEquals(BelfiorePart.of(ROMA).toOmocodeLevel(1).toOmocodeLevel(0).getValue(), "H501");
	}

	@Test
	public void testGetValue() {
		assertEquals(BelfiorePart.of(ROMA).getValue(), "H501");
	}

}
