package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImmutableIntArrayTest {

	ImmutableIntArray uut = ImmutableIntArray.of(1, 2, 3);

	@Test
	public void testGet() {
		assertEquals(3, uut.get(2));
		assertEquals(1, uut.get(0));

		assertThrows(ArrayIndexOutOfBoundsException.class, () -> uut.get(-1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> uut.get(4));
	}

	@Test
	public void testIndexOf() {
		assertEquals(0, uut.indexOf(1));
		assertEquals(1, uut.indexOf(2));
		assertEquals(2, uut.indexOf(3));

		assertEquals(-1, uut.indexOf(4));
	}

	@Test
	public void testContains() {
		assertTrue(uut.contains(1));
		assertTrue(uut.contains(2));
		assertTrue(uut.contains(3));

		assertFalse(uut.contains(4));
	}

	@Test
	public void testOf() {
		int[] array = new int[] { 1, 2, 3 };
		ImmutableIntArray expected = ImmutableIntArray.of(array);
		assertEquals(expected, uut);

		array[1] = 5;
		assertFalse(expected.contains(5));

		assertEquals(ImmutableIntArray.wrap(new int[0]), ImmutableIntArray.of());
	}

	@Test
	public void testLength() {
		assertEquals(3, uut.length());
	}

	@Test
	public void testToArray() {
		int[] array = new int[] { 1, 2, 3 };
		assertArrayEquals(array, uut.toArray());

		ImmutableIntArray other = ImmutableIntArray.of(array);
		array[0] = 5;
		assertArrayEquals(new int[] { 1, 2, 3 }, other.toArray());

	}

	@Test
	public void testReverse() {
		ImmutableIntArray reversed = uut.reverse();
		assertArrayEquals(new int[] {3,2,1}, reversed.toArray());
	}

	@Test
	public void testWrap() {
		int[] array = new int[] {1,2,3};
		ImmutableIntArray uut = ImmutableIntArray.wrap(array);
		array[0] = 5;
		assertTrue(uut.contains(5));
	}

}
