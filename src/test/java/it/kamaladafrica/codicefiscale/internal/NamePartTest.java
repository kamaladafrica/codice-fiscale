package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NamePartTest {

	@Test
	public void testFromString() {
		assertEquals("MRA", NamePart.from("MRA").getValue());
	}

	@Test
	public void testOf() {
		assertEquals("MRA", NamePart.of("Mario").getValue());
	}

}
