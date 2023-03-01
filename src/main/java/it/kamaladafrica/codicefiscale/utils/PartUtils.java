package it.kamaladafrica.codicefiscale.utils;

import static org.apache.commons.lang3.StringUtils.defaultString;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import it.kamaladafrica.codicefiscale.CodiceFiscale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartUtils {

	public static final String EMPTY_STRING = "";
	public static final String PLACEHOLDER = "X";

	public static String normalizeString(String s) {
		return RegexUtils.extract(RegexUtils.CF_ALLOWED_CHARS, defaultString(s).toUpperCase(CodiceFiscale.LOCALE));
	}

	public static String extractConsonants(String s) {
		Validate.notNull(s);
		return RegexUtils.extract(RegexUtils.CONSONANT_PATTERN, s);
	}

	public static String extractVowels(String s) {
		Validate.notNull(s);
		return RegexUtils.extract(RegexUtils.VOWEL_PATTERN, s);
	}

	public static boolean hasVowels(String s) {
		return !extractVowels(s).isEmpty();
	}

	public static String removePlaceholderIfPresent(String s) {
		if (hasVowels(s)) {
			return StringUtils.removeEnd(s, PLACEHOLDER);
		}
		return s;
	}

}
