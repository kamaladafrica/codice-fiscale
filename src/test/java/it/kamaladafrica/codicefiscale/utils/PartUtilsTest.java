package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class PartUtilsTest {

	@Test
	public void testNormalizeString() {
		assertEquals("A14B", PartUtils.normalizeString("A1,.4B"));
		assertEquals("", PartUtils.normalizeString(null));
	}

	@Test
	public void testExtractConsonants() {
		assertEquals("B", PartUtils.extractConsonants("A1,.4B"));
	}

	@Test
	public void testExtractVowels() {
		assertEquals("A", PartUtils.extractVowels("A1,.4B"));
	}

}
