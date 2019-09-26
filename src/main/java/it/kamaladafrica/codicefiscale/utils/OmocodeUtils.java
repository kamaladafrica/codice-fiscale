package it.kamaladafrica.codicefiscale.utils;

import com.google.common.primitives.ImmutableIntArray;

public class OmocodeUtils {

	private static final ImmutableIntArray OMOCODE_REPLACE_CHARS = ImmutableIntArray.of('L', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V');

	public static final ImmutableIntArray OMOCODE_INDEXES = ImmutableIntArray.of(14, 13, 12, 10, 9, 7, 6);

	public static char toOmocodeChar(char digit) {
		if (Character.isDigit(digit)) {
			return (char) OMOCODE_REPLACE_CHARS.get(Character.getNumericValue(digit));
		}
		return digit;
	}

	public static char toDigit(char omocodeChar) {
		int index = OMOCODE_REPLACE_CHARS.indexOf(omocodeChar);
		if (index != -1) {
			return Character.forDigit(index, 10);
		}
		return omocodeChar;
	}

	public static int level(String value, int[] indexes) {
		int i = 0;
		while(i < indexes.length && !Character.isDigit(value.charAt(indexes[i]))) {
			i++;
		}
		return i;
	}

	public static String normalize(String value, int[] indexes) {
		StringBuilder sb = new StringBuilder(value);
		for (int i : indexes) {
			char c = value.charAt(i);
			sb.setCharAt(i, toDigit(c));
		}
		return sb.toString();
	}

	public static String normalize(String value) {
		return normalize(value, OMOCODE_INDEXES.toArray());
	}

	public static int level(String value) {
		return level(value, OMOCODE_INDEXES.toArray());
	}

	public static String apply(String value, int[] indexes) {
		StringBuilder sb = new StringBuilder(value);
		for (int i : indexes) {
			char c = value.charAt(i);
			sb.setCharAt(i, toOmocodeChar(c));
		}
		return sb.toString();
	}

}
