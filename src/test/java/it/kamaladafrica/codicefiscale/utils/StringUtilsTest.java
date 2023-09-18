package it.kamaladafrica.codicefiscale.utils;

import static it.kamaladafrica.codicefiscale.utils.TestUtils.wrap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testDefaultStringStringString() {
		assertEquals(null, StringUtils.defaultString(null, null));
		assertEquals("defaultString", StringUtils.defaultString(null, "defaultString"));
		assertEquals("string", StringUtils.defaultString("string", null));
		assertEquals("string", StringUtils.defaultString("string", "defaultString"));
	}

	@Test
	public void testDefaultStringString() {
		assertEquals("", StringUtils.defaultString(null));
		assertEquals("string", StringUtils.defaultString("string"));
	}

	@Test
	public void testRemoveEnd() {
		assertEquals("abab", StringUtils.removeEnd("ababab", "ab"));
		assertEquals("ababab", StringUtils.removeEnd("ababab", "ba"));
		assertEquals(null, StringUtils.removeEnd(null, "ba"));
		assertEquals("", StringUtils.removeEnd("", "ba"));
		assertEquals("ababab", StringUtils.removeEnd("ababab", null));
		assertEquals("ababab", StringUtils.removeEnd("ababab", ""));
	}

	@Test
	public void testReplaceEach() {
		assertEquals(null, StringUtils.replaceEach(null, new String[] {"abb", "ccd"}, new String[] {"xx", "yy"}));
		assertEquals("", StringUtils.replaceEach("", new String[] {"abb", "ccd"}, new String[] {"xx", "yy"}));
		assertEquals("aabbccdd", StringUtils.replaceEach("aabbccdd", null, null));
		assertEquals("aabbccdd", StringUtils.replaceEach("aabbccdd", new String[] {"aa"}, null));
		assertEquals("aabbccdd", StringUtils.replaceEach("aabbccdd", null, new String[] {"aa"}));
		assertEquals("aabbccdd", StringUtils.replaceEach("aabbccdd", new String[] {"aa"}, new String[] {null}));
		assertEquals("aabbccdd", StringUtils.replaceEach("aabbccdd", new String[] {null}, new String[] {"aa"}));
		assertEquals("wxyzz", StringUtils.replaceEach("aabbccdd", new String[] {"aa", "bb", "cc", "d"}, new String[] {"w", "x", "y", "z"}));
		assertEquals("xbbzdd", StringUtils.replaceEach("aabbccdd", new String[] {"aa", "bb", "cc"}, new String[] {"x", null, "z"}));
		assertEquals("xbbzdd", StringUtils.replaceEach("aabbccdd", new String[] {"aa", null, "cc"}, new String[] {"x", "y", "z"}));
		assertEquals("axxaxxyydyyd", StringUtils.replaceEach("aabbaabbccddccdd", new String[] {"abb", "ccd"}, new String[] {"xx", "yy"}));

		assertThrows(IllegalArgumentException.class, () -> StringUtils.replaceEach("string", new String[] {"a"},  new String[] {"a", "b"}));
	}

	@Test
	public void testEquals() {
		String s = "aaa";
		assertTrue(StringUtils.equals(wrap(s), s));
		assertFalse(StringUtils.equals(null, s));
		assertFalse(StringUtils.equals(wrap(s), null));
		assertTrue(StringUtils.equals(null, null));
		assertFalse(StringUtils.equals(wrap("aaa"), "aaaa"));
		assertFalse(StringUtils.equals("aaa", "bbb"));
		assertTrue(StringUtils.equals(new String("aaa"), new String("aaa")));
		assertTrue(StringUtils.equals(wrap(new String("aaa")), new String("aaa")));
		assertFalse(StringUtils.equals(wrap(new String("aaa")), new String("bbb")));
		assertFalse(StringUtils.equals(wrap(new String("aaa")), wrap(new String("bbb"))));
		assertTrue(StringUtils.equals(wrap(new String("aaa")), wrap(new String("aaa"))));
	}

}
