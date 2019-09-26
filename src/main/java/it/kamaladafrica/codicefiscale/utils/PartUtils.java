package it.kamaladafrica.codicefiscale.utils;

import static it.kamaladafrica.codicefiscale.utils.RegexUtils.CF_ALLOWED_CHARS;
import static it.kamaladafrica.codicefiscale.utils.RegexUtils.CONSONANT_PATTERN;
import static it.kamaladafrica.codicefiscale.utils.RegexUtils.VOWEL_PATTERN;
import static it.kamaladafrica.codicefiscale.utils.RegexUtils.extract;
import static org.apache.commons.lang3.StringUtils.defaultString;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PartUtils {

	public final static String EMPTY_STRING = "";

	public static String normalizeString(String s) {
		return extract(CF_ALLOWED_CHARS, defaultString(s).toUpperCase());
	}

	public static String extractConsonants(@NonNull String s) {
		return extract(CONSONANT_PATTERN, s);
	}

	public static String extractVowels(@NonNull String s) {
		return extract(VOWEL_PATTERN, s);
	}

}
