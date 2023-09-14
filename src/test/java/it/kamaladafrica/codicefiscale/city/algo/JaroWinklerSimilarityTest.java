package it.kamaladafrica.codicefiscale.city.algo;

import static it.kamaladafrica.codicefiscale.utils.TestUtils.wrap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class JaroWinklerSimilarityTest {

	JaroWinklerSimilarity similarity;

	@Before
	public void setUp() {
		similarity = new JaroWinklerSimilarity();
	}

	@Test
	public void testGetJaroWinklerSimilarity_NullNull() {
		assertThrows(IllegalArgumentException.class, () -> similarity.apply(null, null));
	}

	@Test
	public void testGetJaroWinklerSimilarity_NullString() {
		assertThrows(IllegalArgumentException.class, () -> similarity.apply(null, "clear"));
	}

	@Test
	public void testGetJaroWinklerSimilarity_StringNull() {
		assertThrows(IllegalArgumentException.class, () -> similarity.apply(" ", null));
	}

	@Test
	public void testGetJaroWinklerSimilarity_StringString() {
		assertEquals(1d, similarity.apply(wrap(""), ""), 0.00001d);
		assertEquals(1d, similarity.apply(wrap("foo"), "foo"), 0.00001d);
		assertEquals(0.94166d, similarity.apply(wrap("foo"), "foo "), 0.00001d);
		assertEquals(0.90666d, similarity.apply(wrap("foo"), "foo  "), 0.00001d);
		assertEquals(0.86666d, similarity.apply(wrap("foo"), " foo "), 0.00001d);
		assertEquals(0.51111d, similarity.apply(wrap("foo"), "  foo"), 0.00001d);
		assertEquals(0.92499d, similarity.apply(wrap("frog"), "fog"), 0.00001d);
		assertEquals(0.0d, similarity.apply(wrap("fly"), "ant"), 0.00000000000000000001d);
		assertEquals(0.44166d, similarity.apply(wrap("elephant"), "hippo"), 0.00001d);
		assertEquals(0.90666d, similarity.apply(wrap("ABC Corporation"), "ABC Corp"), 0.00001d);
		assertEquals(0.95251d, similarity.apply(wrap("D N H Enterprises Inc"), "D & H Enterprises, Inc."), 0.00001d);
		assertEquals(0.942d, similarity.apply(wrap("My Gym Children's Fitness Center"), "My Gym. Childrens Fitness"),
				0.00001d);
		assertEquals(0.898018d, similarity.apply(wrap("PENNSYLVANIA"), "PENNCISYLVNIA"), 0.00001d);
		assertEquals(0.971428d, similarity.apply(wrap("/opt/software1"), "/opt/software2"), 0.00001d);
		assertEquals(0.941666d, similarity.apply(wrap("aaabcd"), "aaacdb"), 0.00001d);
		assertEquals(0.911111d, similarity.apply(wrap("John Horn"), "John Hopkins"), 0.00001d);
	}

}
