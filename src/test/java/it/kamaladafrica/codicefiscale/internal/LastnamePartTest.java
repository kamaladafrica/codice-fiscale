package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LastnamePartTest {

	@Test
	public void testFromString() {
		assertEquals("RSS", LastnamePart.from("RSS").getValue());
	}

	@Test
	public void testOf() {
		assertEquals("RSS", LastnamePart.of("Rossini").getValue());
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
