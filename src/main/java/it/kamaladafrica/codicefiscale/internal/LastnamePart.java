package it.kamaladafrica.codicefiscale.internal;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import static it.kamaladafrica.codicefiscale.utils.PartUtils.*;
import static org.apache.commons.lang3.Validate.matchesPattern;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class LastnamePart extends AbstractPart {

	public static final int PART_LENGTH = 3;

	private final static String MISSING_LETTERS_PLACEHOLDER = "XXX";

	private final static String VALIDATION_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z])$";

	String name;

	public static LastnamePart from(String value) {
		Validate.notEmpty(value, "invalid name: %s", value);
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		return of(value);
	}

	public static LastnamePart of(String value) {
		Validate.notEmpty(value, "invalid name: %s", value);
		Validate.validIndex(normalizeString(value), 1, "invalid name: %s", value);
		return new LastnamePart(value);
	}

	@Override
	protected String computeValue() {
		String name = normalizeString(getName());
		StringBuilder part = new StringBuilder();
		part.append(extractConsonants(name));
		part.append(extractVowels(name));
		part.append(MISSING_LETTERS_PLACEHOLDER);
		part.setLength(PART_LENGTH);

		matchesPattern(part, VALIDATION_PATTERN, "unexpected result: %s", part);

		return part.toString();
	}

	@Override
	protected String applyOmocodeLevel(String computeValue) {
		return computeValue;
	}

	@Override
	protected void validateValue(String value) {
		matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

}
