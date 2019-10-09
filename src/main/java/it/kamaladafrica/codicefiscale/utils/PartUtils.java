package it.kamaladafrica.codicefiscale.utils;

import static org.apache.commons.lang3.StringUtils.defaultString;

import org.apache.commons.lang3.Validate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PartUtils {

	public final static String EMPTY_STRING = "";

	public static String normalizeString(String s) {
		return RegexUtils.extract(RegexUtils.CF_ALLOWED_CHARS, defaultString(s).toUpperCase());
	}

	public static String extractConsonants(String s) {
		Validate.notNull(s);
		return RegexUtils.extract(RegexUtils.CONSONANT_PATTERN, s);
	}

	public static String extractVowels(String s) {
		Validate.notNull(s);
		return RegexUtils.extract(RegexUtils.VOWEL_PATTERN, s);
	}

}
