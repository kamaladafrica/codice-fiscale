package it.kamaladafrica.codicefiscale.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LastnamePartTest {

	@Test
	public void testFromString() {
		assertEquals("RSS", LastnamePart.from("RSS").getValue());
	}

	@Test
	public void testOf() {
		assertEquals("RSS", LastnamePart.of("Rossini").getValue());
	}

}
