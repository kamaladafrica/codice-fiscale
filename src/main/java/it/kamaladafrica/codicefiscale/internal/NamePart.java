package it.kamaladafrica.codicefiscale.internal;

import static it.kamaladafrica.codicefiscale.utils.PartUtils.extractConsonants;
import static it.kamaladafrica.codicefiscale.utils.PartUtils.extractVowels;
import static it.kamaladafrica.codicefiscale.utils.PartUtils.normalizeString;
import static org.apache.commons.lang3.Validate.matchesPattern;

import org.apache.commons.lang3.Validate;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class NamePart extends AbstractPart {

	private final static String MISSING_LETTERS_PLACEHOLDER = "XXX";

	private final static String VALIDATION_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z])$";

	String name;

	public static NamePart from(@NonNull String value) {
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		return of(value);
	}

	public static NamePart of(@NonNull String value) {
		Validate.validIndex(normalizeString(value), 1, "invalid name: %s", value);
		return new NamePart(value);
	}

	@Override
	protected String computeValue() {
		String name = normalizeString(getName());
		StringBuilder part = new StringBuilder();
		part.append(extractConsonants(name));
		part.append(extractVowels(name));
		part.append(MISSING_LETTERS_PLACEHOLDER);
		part.setLength(3);

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
