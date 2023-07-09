package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PartUtilsTest {

	@Test
	public void testNormalizeString() {
		assertEquals("A14B", PartUtils.normalizeString("A1,.4B"));
		assertEquals("AAAAEAECCEEEEIIIIOOOOEOEUUUUESZSSSS",PartUtils.normalizeString("àáâäæçčéèêëíìîï óòôöœúùûüšžßẞ"));
		assertEquals(PartUtils.EMPTY_STRING, PartUtils.normalizeString(null));
	}

	@Test
	public void testExtractConsonants() {
		assertEquals("B", PartUtils.extractConsonants("A1,.4B"));
		assertEquals(PartUtils.EMPTY_STRING, PartUtils.extractConsonants("AEIOU"));
	}

	@Test
	public void testExtractVowels() {
		assertEquals("A", PartUtils.extractVowels("A1,.4B"));
		assertEquals(PartUtils.EMPTY_STRING, PartUtils.extractVowels("BCDF"));
	}

	@Test
	public void testHasVowels() {
		assertTrue(PartUtils.hasVowels("ABC"));
		assertFalse(PartUtils.hasVowels("BCD"));
	}

	@Test
	public void testRemovePlaceholderIfPresent() {
		assertEquals("BXA", PartUtils.removePlaceholderIfPresent("BXA"));
		assertEquals("BO", PartUtils.removePlaceholderIfPresent("BOX"));
		assertEquals("BXX", PartUtils.removePlaceholderIfPresent("BXX"));
	}

}
