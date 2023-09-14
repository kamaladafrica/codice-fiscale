package it.kamaladafrica.codicefiscale.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

	public static <T> Set<T> unmodifiableSet(Collection<T> collection) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(collection));
	}

	/**
	 * Wrap the string to a {@link CharSequence}. This ensures that using the
	 * {@link Object#equals(Object)} method on the input CharSequence to test for
	 * equality will fail.
	 *
	 * @param string the string
	 * @return the char sequence
	 */
	public static CharSequence wrap(final String string) {
		return new CharSequence() {
			@Override
			public char charAt(final int index) {
				return string.charAt(index);
			}

			@Override
			public int length() {
				return string.length();
			}

			@Override
			public CharSequence subSequence(final int start, final int end) {
				return string.subSequence(start, end);
			}
		};
	}

}
