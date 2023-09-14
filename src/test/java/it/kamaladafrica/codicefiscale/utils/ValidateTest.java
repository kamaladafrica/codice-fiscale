package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ValidateTest {

	@Test
	public void testNotEmptyTStringObjectArray() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty("", "message: %s", "arg"));
		assertEquals("message: arg", e.getMessage());

		assertThrows(NullPointerException.class, () -> Validate.notEmpty(null, "message: %s", "arg"));

		Validate.notEmpty("string", "message: %s", "arg");
	}

	@Test
	public void testNotEmptyT() {
		assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(""));
		assertThrows(NullPointerException.class, () -> Validate.notEmpty(null));
		Validate.notEmpty("string");
	}

	@Test
	public void testMatchesPatternTStringStringObjectArray() {
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> Validate.matchesPattern("string", "x*", "message: %s", "arg"));
		assertEquals("message: arg", e.getMessage());

		assertThrows(NullPointerException.class, () -> Validate.matchesPattern(null, "x*", "message: %s", "arg"));

		Validate.matchesPattern("string", "\\wtr.*", "message: %s", "arg");
	}

	@Test
	public void testMatchesPatternTString() {
		assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern("string", "x*"));
		assertThrows(NullPointerException.class, () -> Validate.matchesPattern(null, "x*"));

		Validate.matchesPattern("string", "\\wtr.*");
	}

	@Test
	public void testInclusiveBetweenIntIntIntStringObjectArray() {
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> Validate.inclusiveBetween(1, 3, 5, "message: %s", "arg"));
		assertEquals("message: arg", e.getMessage());

		Validate.inclusiveBetween(1, 3, 2, "message: %s", "arg");
		Validate.inclusiveBetween(0, 0, 0, "message: %s", "arg");
	}

	@Test
	public void testInclusiveBetweenIntIntInt() {
		assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1, 3, 5));

		Validate.inclusiveBetween(1, 3, 2);
		Validate.inclusiveBetween(0, 0, 0);
	}

	@Test
	public void testValidIndexTIntStringObjectArray() {
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> Validate.validIndex("abc", 3, "message: %s", "arg"));
		assertEquals("message: arg", e.getMessage());
		assertThrows(NullPointerException.class, () -> Validate.validIndex(null, 2));

		Validate.validIndex("abc", 2, "message: %s", "arg");
		Validate.validIndex("abc", 0, "message: %s", "arg");
	}

	@Test
	public void testValidIndexTInt() {
		assertThrows(IllegalArgumentException.class, () -> Validate.validIndex("abc", 3));
		assertThrows(NullPointerException.class, () -> Validate.validIndex(null, 2));

		Validate.validIndex("abc", 2);
		Validate.validIndex("abc", 0);
	}

}
