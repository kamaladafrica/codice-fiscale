package it.kamaladafrica.codicefiscale.city.algo;

import static it.kamaladafrica.codicefiscale.utils.TestUtils.wrap;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExactMatchAlgoritm_applyTest {

	@Parameter(0)
	public boolean ignoreCase;

	@Parameter(1)
	public CharSequence left;

	@Parameter(2)
	public CharSequence right;

	@Parameter(3)
	public double expected;

	ExactMatchAlgoritm uutIgnoreCase = new ExactMatchAlgoritm(true);
	ExactMatchAlgoritm uutCaseSensitive = new ExactMatchAlgoritm(false);

	@Test
	public void testGetExactMatchAlgoritm_StringString() {
		ExactMatchAlgoritm uut = ignoreCase ? uutIgnoreCase : uutCaseSensitive;
		assertEquals(expected, uut.apply(left, right), 0.0);
	}

	@Parameters(name = "[{0}] apply(\"{1}\", \"{2}\")")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ false, wrap(""), "", 1.0 },
			{ true, wrap(""), "", 1.0 },
			{ false, wrap("aaa"), "aaa", 1.0 },
			{ false, wrap("aaa"), "AAA", 0.0 },
			{ true, wrap("aaa"), "AAA", 1.0 },
			{ true, wrap("aAa"), "AaA", 1.0 },
			{ false, wrap("aaa"), "bbb", 0.0 },
			{ true, wrap("AAA"), "BBB", 0.0 },
			{ true, wrap("AAA"), "AAAA", 0.0 },
			{ true, "AAA", "AAA", 1.0 },
			{ true, new String("AAA"), new StringBuilder("AAA"), 1.0 },
			{ false, new String("aaa"), new String("aaa"), 1.0 },
			{ true, new String("aaa"), new String("AAA"), 1.0 },
		});
	}
}
