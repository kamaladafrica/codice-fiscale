package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ArrayUtils_reverseTest {

	@Parameter(0)
	public int[] array;

	@Parameter(1)
	public int[] expected;

	@Test
	public void testReverse() {
		ArrayUtils.reverse(array);
		assertArrayEquals(expected, array);
	}

	@Parameters(name="length {index}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{new int[0], new int[0] },
			{new int[] { 1 }, new int[] { 1 } },
			{new int[] { 1, 2 }, new int[] { 2, 1 } },
			{new int[] { 1, 2, 3 }, new int[] { 3, 2, 1 } },
			{new int[] { 1, 2, 3, 4 }, new int[] { 4, 3, 2, 1 } },
			{new int[] { 1, 3, 2, 4, 6 }, new int[] { 6, 4, 2, 3, 1 } } });
	}

}
