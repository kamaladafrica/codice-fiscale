package it.kamaladafrica.codicefiscale.city.algo;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ExactMatchAlgoritmTest {

	ExactMatchAlgoritm uut = new ExactMatchAlgoritm();

	@Test
	public void testGetExactMatchAlgoritm_NullNull() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(null, null));
	}

	@Test
	public void testGetExactMatchAlgoritm_NullString() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(null, "clear"));
	}

	@Test
	public void testExactMatchAlgoritm_StringNull() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(" ", null));
	}

}
