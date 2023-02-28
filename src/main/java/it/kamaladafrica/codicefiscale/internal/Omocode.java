package it.kamaladafrica.codicefiscale.internal;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

import com.google.common.collect.Lists;
import com.google.common.primitives.ImmutableIntArray;
import com.google.common.primitives.Ints;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class Omocode {

	private static final ImmutableIntArray OMOCODE_REPLACE_CHARS = ImmutableIntArray.of('L', 'M', 'N', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V');

	int level;

	@Getter(AccessLevel.PRIVATE)
	int mask;

	ImmutableIntArray omocodeIndices;

	private Omocode(int level, int mask, ImmutableIntArray omocodeIndices) {
		inclusiveBetween(0, mask, level, "invalid omocode level: 0 <= %s <= %s", level, mask);
		this.level = level;
		this.mask = mask;
		this.omocodeIndices = omocodeIndices;
	}

	public String apply(String value) {
		if (level == 0) {
			return value;
		}
		StringBuilder sb = new StringBuilder(value);
		for (int i = 0; i < omocodeIndices.length(); i++) {
			boolean isSet = (level & (1 << i)) == 1 << i;
			if (isSet) {
				final int idx = omocodeIndices.get(i);
				sb.setCharAt(idx, digitToOmocodeChar(sb.charAt(idx)));
			}
		}
		return sb.toString();
	}

	public String normalize(String value) {
		StringBuilder sb = new StringBuilder(value);
		for (int i = 0; i < omocodeIndices.length(); i++) {
			final int idx = omocodeIndices.get(i);
			sb.setCharAt(idx, omocodeCharToDigit(value.charAt(idx)));
		}
		return sb.toString();
	}

	public boolean isLevel(int level) {
		return this.level == level;
	}

	public boolean isSameLevel(Omocode other) {
		return getOmocodeIndices().equals(other.getOmocodeIndices()) && isLevel(other.getLevel());
	}

	public int getMaxLevel() {
		return mask;
	}

	public Omocode withLevel(int level) {
		if (this.level == level) {
			return this;
		}
		return new Omocode(level, mask, omocodeIndices);
	}

	public Omocode ofValue(String value) {
		int currentLevel = 0;
		for (int i = 0; i < omocodeIndices.length(); i++) {
			if (isOmocodeChar(value.charAt(omocodeIndices.get(i)))) {
				currentLevel |= (1 << i);
			}
		}
		return withLevel(currentLevel);
	}

	public static Omocode of(int level, int[] omocodeIndices) {
		int mask = ~(~0 << omocodeIndices.length); // 2^omocodeIndices.length - 1
		ImmutableIntArray indices = ImmutableIntArray.copyOf(Lists.reverse(Ints.asList(omocodeIndices)));
		return new Omocode(level, mask, indices);
	}

	public static Omocode of(String value, int[] omocodeIndices) {
		return of(0, omocodeIndices).ofValue(value);
	}

	public static Omocode of(int[] omocodeIndices) {
		return of(0, omocodeIndices);
	}

	public static Omocode of(ImmutableIntArray omocodeIndices) {
		return of(0, omocodeIndices);
	}

	public static Omocode of(int level, ImmutableIntArray omocodeIndices) {
		return of(level, omocodeIndices.toArray());
	}

	public static Omocode of(String value, ImmutableIntArray omocodeIndices) {
		return of(value, omocodeIndices.toArray());
	}

	public static Omocode unsupported() {
		return of(0, new int[0]);
	}

	public static char digitToOmocodeChar(char digit) {
		if (Character.isDigit(digit)) {
			return (char) OMOCODE_REPLACE_CHARS.get(Character.getNumericValue(digit));
		}
		return digit;
	}

	public static char omocodeCharToDigit(char omocodeChar) {
		int index = OMOCODE_REPLACE_CHARS.indexOf(omocodeChar);
		if (index != -1) {
			return Character.forDigit(index, 10);
		}
		return omocodeChar;
	}

	public static boolean isOmocodeChar(char c) {
		return !Character.isDigit(c) && OMOCODE_REPLACE_CHARS.contains(c);
	}
}
