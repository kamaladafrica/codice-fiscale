package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class PairTest {

	Pair<Integer, String> uut = Pair.of(1, "string");

	@Test
	public void testGetKey() {
		assertEquals(Integer.valueOf(1), uut.getKey());
		assertEquals(uut.getLeft(), uut.getKey());
	}

	@Test
	public void testGetValue() {
		assertEquals("string", uut.getValue());
		assertEquals(uut.getRight(), uut.getValue());
	}

	@Test
	public void testSetValue() {
		assertThrows(UnsupportedOperationException.class, () -> uut.setValue("newString"));
	}

}
