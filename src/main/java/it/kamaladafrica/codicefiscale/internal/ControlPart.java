package it.kamaladafrica.codicefiscale.internal;

import static org.apache.commons.lang3.Validate.matchesPattern;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class ControlPart extends AbstractPart {

	private final static String VALIDATION_INPUT_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))$";
	private final static String VALIDATION_RESULT_PATTERN = "^[A-Z]$";

	private static final int[] EVEN_WEIGHTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
			20, 21, 22, 23, 24, 25 };
	private static final int[] ODD_WEIGHTS = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16,
			10, 22, 25, 24, 23 };

	private static final char CHAR_ZERO = '0';
	private static final char CHAR_A = 'A';

	String code;

	public static ControlPart of(String value) {
		matchesPattern(value, VALIDATION_INPUT_PATTERN, "invalid value: %s", value);
		return new ControlPart(value);
	}

	@Override
	protected String computeValue() {
		int even = 0;
		int odd = 0;
		char[] chars = code.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			int index = charToIndex(c);
			if (isEven(i + 1)) {
				even += EVEN_WEIGHTS[index];
			} else {
				odd += ODD_WEIGHTS[index];
			}
		}

		int controlCharIndex = (even + odd) % 26;
		String value = String.valueOf((char) (CHAR_A + controlCharIndex));
		matchesPattern(value, VALIDATION_RESULT_PATTERN, "unexpected result: %s", value);
		return value;
	}

	private boolean isEven(int i) {
		return (i & 1) == 0;
	}

	private int charToIndex(char c) {
		final char baseValue = Character.isDigit(c) ? CHAR_ZERO : CHAR_A;
		return c - baseValue;
	}

	@Override
	protected String applyOmocodeLevel(String computeValue) {
		return computeValue;
	}
	
	public boolean isEqual(char controlChar) {
		return getValue().charAt(0) == controlChar;
	}

	public boolean isEqual(String controlChar) {
		matchesPattern(controlChar, VALIDATION_RESULT_PATTERN, "unexpected input: %s", controlChar);
		return getValue().equals(controlChar);
	}

	@Override
	protected void validateValue(String value) {
		matchesPattern(value, VALIDATION_RESULT_PATTERN, "unexpected result: %s", value);
	}

}
