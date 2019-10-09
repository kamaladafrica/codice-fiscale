package it.kamaladafrica.codicefiscale.internal;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;

import java.time.LocalDate;

import org.apache.commons.lang3.Validate;

import com.google.common.primitives.ImmutableIntArray;

import it.kamaladafrica.codicefiscale.utils.OmocodeUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DatePart extends AbstractPart {

	private final static String VALIDATION_PATTERN = "^(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])$";

	private static final ImmutableIntArray OMOCODE_INDEXES = ImmutableIntArray.of(4, 3, 1, 0);

	private static final String DATE_PART_FORMAT = "%02d%s%02d";
	private static final String MONTHS_CHARS = "ABCDEHLMPRST";
	private static final int FEMAIL_DAY_OFFSET = 40;
	private static final int MILLENNIUM = (LocalDate.now().getYear() / 1000) * 1000;

	LocalDate date;
	boolean female;

	private DatePart(LocalDate date, boolean female, int level) {
		super(level);
		inclusiveBetween(0, OMOCODE_INDEXES.length(), level, "invalid omocode level for date part: 0 <= %s <= %s",
				level, OMOCODE_INDEXES.length());
		this.date = date;
		this.female = female;
	}

	public static DatePart from(String value) {
		Validate.notEmpty(value, "invalid value: %s", value);
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		DatePartInput input = toInput(value);
		return new DatePart(input.getDate(), input.isFemale(), getOmocodeLevel(value));
	}

	private static int getOmocodeLevel(String value) {
		return OmocodeUtils.level(value, OMOCODE_INDEXES.toArray());
	}

	public static DatePart of(LocalDate date, boolean isFemale) {
		Validate.notNull(date);
		return new DatePart(date, isFemale);
	}

	@Override
	protected String computeValue() {
		int day = date.getDayOfMonth();
		int month = date.getMonth().getValue() - 1;
		int year = date.getYear() % 100;

		if (isFemale()) {
			day += FEMAIL_DAY_OFFSET;
		}

		String value = String.format(DATE_PART_FORMAT, year, MONTHS_CHARS.charAt(month), day);
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		return value;
	}

	private static DatePartInput toInput(String value) {
		value = normalizeOmocode(value);
		int year = Integer.parseInt(value.substring(0, 2)) + MILLENNIUM;
		int month = 1 + MONTHS_CHARS.indexOf(value.substring(2, 3));
		int day = Integer.parseInt(value.substring(3, 5));

		boolean female = day > FEMAIL_DAY_OFFSET;
		if (female) {
			day -= FEMAIL_DAY_OFFSET;
		}

		LocalDate date = LocalDate.of(year, month, day);
		if (date.isAfter(LocalDate.now())) {
			date = date.minusYears(100);
		}

		return new DatePartInput(date, female);
	}

	private static String normalizeOmocode(String value) {
		return OmocodeUtils.normalize(value, OMOCODE_INDEXES.toArray());
	}

	@Override
	protected String applyOmocodeLevel(String value) {
		final int level = getOmocodeLevel();
		if (level > 0) {
			return OmocodeUtils.apply(value, OMOCODE_INDEXES.subArray(0, level).toArray());
		}
		return value;
	}

	public DatePart toOmocodeLevel(int level) {
		return getOmocodeLevel() == level ? this : new DatePart(date, female, level);
	}

	@Override
	protected void validateValue(String value) {
		matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

	@Value
	private static class DatePartInput {
		LocalDate date;
		boolean female;
	}

}
