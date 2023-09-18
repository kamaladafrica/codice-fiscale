package it.kamaladafrica.codicefiscale.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class ArrayUtilsTest {

	@Test
	public void testImmutableSet() {
		Collection<String> collection = Arrays.asList("B", "A", "C");

		Set<String> set = TestUtils.unmodifiableSet(collection);

		assertNotSame(collection, set);
		assertEquals(3, set.size());
		assertThat(set, CoreMatchers.hasItems("A", "B", "C"));
	}

}
