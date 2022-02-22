package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NamePartTest {

	@Test
	public void testFromString() {
		assertEquals("MRA", NamePart.from("MRA").getName());
	}

	@Test
	public void testOf2Consonants() {
		assertEquals("MRA", NamePart.of("Mario").getValue());
	}

	@Test
	public void testOf4Consonants() {
		assertEquals("FRC", NamePart.of("Federico").getValue());
	}

	@Test
	public void testOf3Consonants() {
		assertEquals("MRC", NamePart.of("Marco").getValue());
	}

	@Test
	public void testFromPlaceholder() {
		assertEquals("BO", NamePart.from("BOX").getName());
	}

	@Test
	public void testOf2Letters() {
		assertEquals("BOX", NamePart.of("Bo").getValue());
	}

}
