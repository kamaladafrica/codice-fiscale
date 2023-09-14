package it.kamaladafrica.codicefiscale.city.algo;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class JaroWinklerAlgoritmTest {

	JaroWinklerAlgoritm uut = new JaroWinklerAlgoritm();;

	@Test
	public void testGetJaroWinklerSimilarity_NullNull() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(null, null));
	}

	@Test
	public void testGetJaroWinklerSimilarity_NullString() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(null, "clear"));
	}

	@Test
	public void testGetJaroWinklerSimilarity_StringNull() {
		assertThrows(IllegalArgumentException.class, () -> uut.apply(" ", null));
	}

}
