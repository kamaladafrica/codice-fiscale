package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ControlPartTest {

	private final String CODICE_FISCALE_UNCHECKED = "RSSMRA75C22H501";
	private final String CONTROL = "I";

	private final String CODICE_FISCALE_UNCHECKED_2 = "RSSMRA75C22H5LM";
	private final String CONTROL_2 = "L";

	@Test
	public void testOf() {
		assertEquals(ControlPart.of(CODICE_FISCALE_UNCHECKED).getValue(), CONTROL);
		assertEquals(ControlPart.of(CODICE_FISCALE_UNCHECKED_2).getValue(), CONTROL_2);
	}

	@Test
	public void testIsEqualChar() {
		assertTrue(ControlPart.of(CODICE_FISCALE_UNCHECKED).isEqual(CONTROL.charAt(0)));
		assertTrue(ControlPart.of(CODICE_FISCALE_UNCHECKED_2).isEqual(CONTROL_2.charAt(0)));
	}

	@Test
	public void testIsEqualString() {
		assertTrue(ControlPart.of(CODICE_FISCALE_UNCHECKED).isEqual(CONTROL));
		assertTrue(ControlPart.of(CODICE_FISCALE_UNCHECKED_2).isEqual(CONTROL_2));
	}

	@Test
	public void testIsEqualStringBadInput() {
		ControlPart part = ControlPart.of(CODICE_FISCALE_UNCHECKED);
		assertThrows(IllegalArgumentException.class, () -> part.isEqual("5"));
		assertThrows(IllegalArgumentException.class, () -> part.isEqual("AA"));
	}

}
