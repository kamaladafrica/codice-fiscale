package it.kamaladafrica.codicefiscale.internal;

import java.time.LocalDate;
import java.util.Objects;

import it.kamaladafrica.codicefiscale.utils.Validate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.NONE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class DatePart extends AbstractPart {

	private static final String VALIDATION_PATTERN = "^(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])$";

	private static final ImmutableIntArray OMOCODE_INDEXES = ImmutableIntArray.of(0, 1, 3, 4);

	private static final String DATE_PART_FORMAT = "%02d%s%02d";
	private static final String MONTHS_CHARS = "ABCDEHLMPRST";
	private static final int FEMAIL_DAY_OFFSET = 40;
	private static final int MILLENNIUM = Math.floorDiv(LocalDate.now().getYear(), 1000) * 1000;

	LocalDate date;
	boolean female;

	private DatePart(LocalDate date, boolean female, Omocode level) {
		super(level);
		this.date = date;
		this.female = female;
	}

	public static DatePart from(String value) {
		Validate.notEmpty(value);
		Validate.matchesPattern(value, VALIDATION_PATTERN);
		final Omocode omocodeLevel = Omocode.of(value, OMOCODE_INDEXES);
		DatePartInput input = toInput(omocodeLevel.normalize(value));
		return new DatePart(input.getDate(), input.isFemale(), omocodeLevel);
	}

	public static DatePart of(LocalDate date, boolean isFemale) {
		Objects.requireNonNull(date);
		return new DatePart(date, isFemale, Omocode.of(OMOCODE_INDEXES));
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
		Validate.matchesPattern(value, VALIDATION_PATTERN);
		return value;
	}

	private static DatePartInput toInput(String normalizedValue) {
		int year = Integer.parseInt(normalizedValue.substring(0, 2)) + MILLENNIUM;
		int month = 1 + MONTHS_CHARS.indexOf(normalizedValue.substring(2, 3));
		int day = Integer.parseInt(normalizedValue.substring(3, 5));

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

	@Override
	protected String applyOmocodeLevel(String value) {
		return getOmocodeLevel().apply(value);
	}

	public DatePart toOmocodeLevel(int level) {
		return getOmocodeLevel().isLevel(level) ? this : new DatePart(date, female, getOmocodeLevel().withLevel(level));
	}

	@Override
	protected void validateValue(String value) {
		Validate.matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

	@Value
	private static class DatePartInput {
		LocalDate date;
		boolean female;
	}
}
