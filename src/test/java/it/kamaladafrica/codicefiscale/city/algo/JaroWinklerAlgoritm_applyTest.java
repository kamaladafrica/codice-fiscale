package it.kamaladafrica.codicefiscale.city.algo;

import static it.kamaladafrica.codicefiscale.utils.TestUtils.wrap;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JaroWinklerAlgoritm_applyTest {

	JaroWinklerAlgoritm uut = new JaroWinklerAlgoritm();

	@Parameter(0)
	public String left;

	@Parameter(1)
	public String right;

	@Parameter(2)
	public double expected;

	@Parameter(3)
	public double tolerance;

	@Before
	public void setUp() {
		uut = new JaroWinklerAlgoritm();
	}

	@Test
	public void testGetJaroWinklerSimilarity_StringString() {
		assertEquals(expected, uut.apply(wrap(left), right), tolerance);
	}

	@Parameters(name = "apply(\"{0}\", \"{1}\")")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] { { "", "", 1.0, 0.00001d }, { "foo", "foo", 1.0, 0.00001d },
				{ "foo", "foo ", 0.94166d, 0.00001d }, { "foo", "foo  ", 0.90666d, 0.00001d },
				{ "foo", " foo ", 0.86666d, 0.00001d }, { "foo", "  foo", 0.51111d, 0.00001d },
				{ "fog", "frog", 0.92499d, 0.00001d }, { "fly", "ant", 0.0d, 0.00000000000000000001d },
				{ "elephant", "hippo", 0.44166d, 0.00001d }, { "ABC Corporation", "ABC Corp", 0.90666d, 0.00001d },
				{ "D N H Enterprises Inc", "D & H Enterprises, Inc.", 0.95251d, 0.00001d },
				{ "My Gym Children's Fitness Center", "My Gym. Childrens Fitness", 0.942d, 0.00001d },
				{ "PENNSYLVANIA", "PENNCISYLVNIA", 0.898018d, 0.00001d },
				{ "/opt/software1", "/opt/software2", 0.971428d, 0.00001d },
				{ "aaabcd", "aaacdb", 0.941666d, 0.00001d }, { "John Horn", "John Hopkins", 0.911111d, 0.00001d },

		});
	}

}
