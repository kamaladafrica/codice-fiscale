package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class OmocodeUtilsTest {

	private final String CODICE_FISCALE = "RSSMRA75C22H501";
	private final String CODICE_FISCALE_1 = "RSSMRA75C22H50M";

	@Test(expected = IndexOutOfBoundsException.class)
	public void testApplyIndexOutOfBound() {
		assertEquals("AM2", OmocodeUtils.apply("A12", new int[] {0,1,4}));
	}

	@Test
	public void testApply() {
		assertEquals("AM2", OmocodeUtils.apply("A12", new int[] {0,1}));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testNormalizeIndexOutOfBound() {
		assertEquals("A12", OmocodeUtils.normalize("AM2", new int[] {0,1,4}));
	}

	@Test
	public void testNormalizeStringIntArray() {
		assertEquals("A12", OmocodeUtils.normalize("AM2", new int[] {0,1}));
	}

	@Test
	public void testNormalizeString() {
		assertEquals(CODICE_FISCALE, OmocodeUtils.normalize(CODICE_FISCALE_1));
	}
	
	@Test
	public void testToDigit() {
		assertEquals('1', OmocodeUtils.toDigit('M'));
		assertEquals('1', OmocodeUtils.toDigit('1'));
	}
	
	@Test
	public void testToOmocodeChar() {
		assertEquals('M', OmocodeUtils.toOmocodeChar('1'));
		assertEquals('M', OmocodeUtils.toOmocodeChar('M'));
	}
	
	@Test
	public void testLevelStringIntArray() {
		assertEquals(0, OmocodeUtils.level("11A", new int[] {0,1}));
		assertEquals(2, OmocodeUtils.level("AM2", new int[] {0,1}));
	}

	@Test
	public void testLevelString() {
		assertEquals(0, OmocodeUtils.level(CODICE_FISCALE));
		assertEquals(1, OmocodeUtils.level(CODICE_FISCALE_1));
	}

	
}
