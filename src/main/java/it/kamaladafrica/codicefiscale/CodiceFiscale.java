package it.kamaladafrica.codicefiscale;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import it.kamaladafrica.codicefiscale.city.CityByBelfiore;
import it.kamaladafrica.codicefiscale.city.CityProvider;
import it.kamaladafrica.codicefiscale.internal.BelfiorePart;
import it.kamaladafrica.codicefiscale.internal.ControlPart;
import it.kamaladafrica.codicefiscale.internal.DatePart;
import it.kamaladafrica.codicefiscale.internal.LastnamePart;
import it.kamaladafrica.codicefiscale.internal.NamePart;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@EqualsAndHashCode
public final class CodiceFiscale {

	public static final Locale LOCALE = Locale.ITALY;

	private static final String VALIDATION_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$";
	private static final int CONTROL_PART_INDEX = 15;
	private static final int LASTNAME_PART_INDEX = 0;
	private static final int FIRSTNAME_PART_INDEX = 3;
	private static final int DATE_PART_INDEX = 6;
	private static final int BELFIORE_PART_INDEX = 11;

	private static final int OMOCODE_LEVEL_DATE_OFFSET = 3;
	private static final int OMOCODE_LEVEL_BELFIORE_OFFSET = 0;
	private static final int OMOCODE_LEVEL_DATE_LOCAL_MASK = 0b1111;
	private static final int OMOCODE_LEVEL_BELFIORE_LOCAL_MASK = 0b111;
	private static final int OMOCODE_LEVEL_DATE_MASK = OMOCODE_LEVEL_DATE_LOCAL_MASK << OMOCODE_LEVEL_DATE_OFFSET;
	private static final int OMOCODE_LEVEL_BELFIORE_MASK = OMOCODE_LEVEL_BELFIORE_LOCAL_MASK << OMOCODE_LEVEL_BELFIORE_OFFSET;

	private static final int OMOCODE_LEVEL_MASK = OMOCODE_LEVEL_DATE_MASK | OMOCODE_LEVEL_BELFIORE_MASK;

	private final Person person;
	private final LastnamePart lastname;
	private final NamePart firstname;
	private final DatePart date;
	private final BelfiorePart belfiore;

	@Getter(lazy = true)
	private final ControlPart control = computeControl();

	private final int omocodeLevel;

	@Getter(lazy = true)
	private final String value = computeValue();

	@Getter(lazy = true, value = AccessLevel.PRIVATE)
	private final String uncheckedValue = computeUncheckedValue();

	private CodiceFiscale(Person person, LastnamePart lastname, NamePart firstname, DatePart date,
			BelfiorePart belfiore, int omocodeLevel) {
		this.person = person;
		this.lastname = lastname;
		this.firstname = firstname;
		this.date = date;
		this.belfiore = belfiore;
		this.omocodeLevel = omocodeLevel;
	}

	public CodiceFiscale normalized() {
		return toOmocodeLevel(0);
	}

	public boolean isEqual(String other) {
		return isEqual(of(other));
	}

	public boolean isEqual(CodiceFiscale other) {
		return isEqual(other, true);
	}

	public boolean isEqual(String other, boolean ignoreOmocode) {
		return isEqual(of(other), ignoreOmocode);
	}

	public boolean isEqual(CodiceFiscale other, boolean ignoreOmocode) {
		CodiceFiscale self = ignoreOmocode ? normalized() : this;
		CodiceFiscale that = ignoreOmocode ? other.normalized() : other;
		return Objects.equals(self.getValue(), that.getValue());
	}

	public CodiceFiscale toOmocodeLevel(int level) {
		if ((level & OMOCODE_LEVEL_MASK) != 0) {
			if (level < 0 || level > OMOCODE_LEVEL_MASK) {
				throw new IllegalArgumentException(
						String.format("invalid omocode level: 0 <= %s <= %s", level, OMOCODE_LEVEL_MASK));
			}
		}
		DatePart datePart = getDate().toOmocodeLevel(level & OMOCODE_LEVEL_DATE_MASK);
		BelfiorePart belfiorePart = getBelfiore().toOmocodeLevel(level & OMOCODE_LEVEL_BELFIORE_MASK);

		return getOmocodeLevel() == level ? this
				: new CodiceFiscale(getPerson(), getLastname(), getFirstname(), datePart, belfiorePart, level);
	}

	public boolean isOmocode() {
		return getOmocodeLevel() > 0;
	}

	public boolean isCompatible(Person person) {
		return isEqual(of(person));
	}

	private String computeValue() {
		String computedValue = getUncheckedValue() + getControl().getValue();
		return validate(computedValue);
	}

	private String computeUncheckedValue() {
		return new StringBuilder(getLastname().getValue()).append(getFirstname().getValue())
				.append(getDate().getValue()).append(getBelfiore().getValue()).toString();
	}

	private ControlPart computeControl() {
		return ControlPart.of(getUncheckedValue());
	}

	public static CodiceFiscale of(String code) {
		return of(code, CityProvider.ofDefault());
	}

	public static CodiceFiscale of(Person person) {

		final NamePart firstname = NamePart.of(person.getFirstname());
		final LastnamePart lastname = LastnamePart.of(person.getLastname());
		final DatePart date = DatePart.of(person.getBirthDate(), person.isFemale());
		final BelfiorePart belfiore = BelfiorePart.of(person.getCity());

		return new CodiceFiscale(person, lastname, firstname, date, belfiore, 0);
	}

	public static CodiceFiscale of(String value, CityByBelfiore provider) {

		validate(value);

		final LastnamePart lastname = LastnamePart.from(value.substring(LASTNAME_PART_INDEX, FIRSTNAME_PART_INDEX));
		final NamePart firstname = NamePart.from(value.substring(FIRSTNAME_PART_INDEX, DATE_PART_INDEX));
		final DatePart date = DatePart.from(value.substring(DATE_PART_INDEX, BELFIORE_PART_INDEX));
		final BelfiorePart belfiore = BelfiorePart.from(value.substring(BELFIORE_PART_INDEX, CONTROL_PART_INDEX),
				provider);

		final Person person = Person.builder().firstname(firstname.getName()).lastname(lastname.getName())
				.birthDate(date.getDate()).isFemale(date.isFemale()).city(belfiore.getCity()).build();

		final int level = (date.getOmocodeLevel().getLevel() << OMOCODE_LEVEL_DATE_OFFSET)
				| (belfiore.getOmocodeLevel().getLevel() << OMOCODE_LEVEL_BELFIORE_OFFSET);

		final CodiceFiscale result = new CodiceFiscale(person, lastname, firstname, date, belfiore, level);

		if (!Objects.equals(result.getValue(), value)) {
			throw new IllegalArgumentException(String.format("expected %s, but found %s", value, result.getValue()));
		}

		return result;

	}

	public static boolean isCompatible(String code, Person person) {
		return of(code).isCompatible(person);
	}

	public static boolean isFormatValid(String value) {
		if (Pattern.matches(VALIDATION_PATTERN, value)) {
			final ControlPart control = ControlPart.of(value.substring(LASTNAME_PART_INDEX, CONTROL_PART_INDEX));
			final char currentControl = value.charAt(CONTROL_PART_INDEX);
			return control.isEqual(currentControl);
		}
		return false;
	}

	public static String validate(String value) {
		if (!value.matches(VALIDATION_PATTERN)) {
			throw new IllegalArgumentException(
					String.format("The string %s does not match the pattern %s", value, VALIDATION_PATTERN));
		}
		final ControlPart control = ControlPart.of(value.substring(LASTNAME_PART_INDEX, CONTROL_PART_INDEX));
		final char currentControl = value.charAt(CONTROL_PART_INDEX);

		if (!control.isEqual(currentControl)) {
			throw new IllegalArgumentException(String.format("invalid control char: expected %s, but found %s",
					control.getValue(), currentControl));
		}
		return value;
	}

}
