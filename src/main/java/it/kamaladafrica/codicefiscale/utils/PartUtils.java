package it.kamaladafrica.codicefiscale.utils;

import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartUtils {

	public final static String EMPTY_STRING = "";

	public static String normalizeString(String s) {
		return RegexUtils.extract(RegexUtils.CF_ALLOWED_CHARS, defaultString(s).toUpperCase());
	}

	public static String extractConsonants(@NonNull String s) {
		return RegexUtils.extract(RegexUtils.CONSONANT_PATTERN, s);
	}

	public static String extractVowels(@NonNull String s) {
		return RegexUtils.extract(RegexUtils.VOWEL_PATTERN, s);
	}

}
