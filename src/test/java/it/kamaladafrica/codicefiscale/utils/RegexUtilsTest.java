package it.kamaladafrica.codicefiscale.utils;

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexUtilsTest {

	private final String PATTERN = "[A-F1-5]";
	private final String TEST = "ABC.defGH0123,45678";
	private final String RESULT = "ABC12345";

	@Test
	public void testDoWithMatchedGroupsMatcherConsumerOfString() {
		StringBuilder sb = new StringBuilder();
		Matcher matcher = Pattern.compile(PATTERN).matcher(TEST);
		RegexUtils.doWithMatchedGroups(matcher, sb::append);
		assertEquals(RESULT, sb.toString());
	}

	@Test
	public void testDoWithMatchedGroupsStringStringConsumerOfString() {
		StringBuilder sb = new StringBuilder();
		RegexUtils.doWithMatchedGroups(PATTERN, TEST, sb::append);
		assertEquals(RESULT, sb.toString());
	}

	@Test
	public void testDoWithMatchedGroupsPatternStringConsumerOfString() {
		StringBuilder sb = new StringBuilder();
		RegexUtils.doWithMatchedGroups(Pattern.compile(PATTERN), TEST, sb::append);
		assertEquals(RESULT, sb.toString());
	}

	@Test
	public void testExtractMatcher() {
		Matcher matcher = Pattern.compile(PATTERN).matcher(TEST);
		assertEquals(RESULT, RegexUtils.extract(matcher));
	}

	@Test
	public void testExtractStringString() {
		assertEquals(RESULT, RegexUtils.extract(PATTERN, TEST));
	}

	@Test
	public void testExtractPatternString() {
		assertEquals(RESULT, RegexUtils.extract(Pattern.compile(PATTERN), TEST));
	}

}
