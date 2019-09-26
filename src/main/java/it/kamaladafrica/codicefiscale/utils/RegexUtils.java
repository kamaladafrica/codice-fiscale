package it.kamaladafrica.codicefiscale.utils;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtils {

	public final static Pattern CF_ALLOWED_CHARS = Pattern.compile("[A-Z0-9]", CASE_INSENSITIVE);
	public final static Pattern CONSONANT_PATTERN = Pattern.compile("[B-DF-HJ-NP-TV-Z]", CASE_INSENSITIVE);
	public final static Pattern VOWEL_PATTERN = Pattern.compile("[AEIOU]", CASE_INSENSITIVE);

	public void doWithMatchedGroups(Matcher m, Consumer<String> consumer) {
		while (m.find()) {
			consumer.accept(m.group());
		}
	}

	public void doWithMatchedGroups(String regex, String s, Consumer<String> consumer) {
		doWithMatchedGroups(Pattern.compile(regex), s, consumer);
	}

	public void doWithMatchedGroups(Pattern pattern, String s, Consumer<String> consumer) {
		doWithMatchedGroups(pattern.matcher(s), consumer);
	}

	public String extract(Matcher m) {
		final StringBuilder extracted = new StringBuilder();
		doWithMatchedGroups(m, extracted::append);
		return extracted.toString();
	}

	public String extract(String regex, String s) {
		return extract(Pattern.compile(regex).matcher(s));
	}

	public String extract(Pattern pattern, String s) {
		return extract(pattern.matcher(s));
	}

}
