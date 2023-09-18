package it.kamaladafrica.codicefiscale.internal;

import static it.kamaladafrica.codicefiscale.utils.PartUtils.extractConsonants;
import static it.kamaladafrica.codicefiscale.utils.PartUtils.extractVowels;
import static it.kamaladafrica.codicefiscale.utils.PartUtils.normalizeString;

import it.kamaladafrica.codicefiscale.utils.PartUtils;
import it.kamaladafrica.codicefiscale.utils.Validate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class NamePart extends AbstractPart {

	public static final int PART_LENGTH = 3;

	public static final int INDEX_TO_REMOVE = 1;

	private static final String MISSING_LETTERS_PLACEHOLDER = "XXX";

	private static final String VALIDATION_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z])$";

	String name;

	public static NamePart from(String value) {
		Validate.notEmpty(value);
		Validate.matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		value = PartUtils.removePlaceholderIfPresent(value);
		return of(value);
	}

	public static NamePart of(String value) {
		Validate.notEmpty(value);
		Validate.validIndex(normalizeString(value), 1);
		return new NamePart(value);
	}

	@Override
	protected String computeValue() {
		String normalizedName = normalizeString(getName());
		StringBuilder part = new StringBuilder();
		part.append(nameConsonants(normalizedName));
		part.append(extractVowels(normalizedName));
		part.append(MISSING_LETTERS_PLACEHOLDER);
		part.setLength(PART_LENGTH);

		Validate.matchesPattern(part, VALIDATION_PATTERN, "unexpected result: %s", part);

		return part.toString();
	}

	private String nameConsonants(String name) {
		String consonants = extractConsonants(name);
		if (consonants.length() > PART_LENGTH) {
			consonants = new StringBuilder(consonants).deleteCharAt(INDEX_TO_REMOVE).toString();
		}
		return consonants;
	}

	@Override
	protected String applyOmocodeLevel(String computeValue) {
		return computeValue;
	}

	@Override
	protected void validateValue(String value) {
		Validate.matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

}
