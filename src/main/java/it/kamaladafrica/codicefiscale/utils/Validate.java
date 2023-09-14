package it.kamaladafrica.codicefiscale.utils;

import java.util.Objects;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Validate {

	public <T extends CharSequence> T notEmpty(T s, String message, Object... args) {
		if (Objects.requireNonNull(s).length() == 0) {
			throw new IllegalArgumentException(String.format(message, args));
		}
		return s;
	}

	public <T extends CharSequence> T notEmpty(T s) {
		return notEmpty(s, "The string is empty");
	}

	public <T extends CharSequence> T matchesPattern(T s, String pattern, String message, Object... args) {
		if (!Pattern.matches(pattern, Objects.requireNonNull(s))) {
			throw new IllegalArgumentException(String.format(message, args));
		}
		return s;
	}

	public <T extends CharSequence> T matchesPattern(T s, String pattern) {
		return matchesPattern(s, pattern, "The string %s does not match the pattern %s", s, pattern);
	}

	public int inclusiveBetween(int lowerBound, int upperBound, int value, String message, Object... args) {
		if (value < lowerBound || value > upperBound) {
			throw new IllegalArgumentException(String.format(message, args));
		}
		return value;
	}

	public int inclusiveBetween(int lowerBound, int upperBound, int value) {
		return inclusiveBetween(lowerBound, upperBound, value, "%s is not  %s <= %s <= %s", value, lowerBound, value,
				upperBound);
	}

	public <T extends CharSequence> T validIndex(T value, int index, String message, Object... args) {
		inclusiveBetween(0, value.length() - 1, index, message, args);
		return value;
	}

	public <T extends CharSequence> T validIndex(T value, int index) {
		return validIndex(value, index, "invalid index 0 <= %s <= %s", index, value.length());
	}

}
