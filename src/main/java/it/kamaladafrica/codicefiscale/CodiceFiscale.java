package it.kamaladafrica.codicefiscale;

import static org.apache.commons.lang3.Validate.matchesPattern;

import java.util.Objects;
import java.util.regex.Pattern;

import it.kamaladafrica.codicefiscale.internal.*;
import org.apache.commons.lang3.Validate;

import it.kamaladafrica.codicefiscale.city.CityByBelfiore;
import it.kamaladafrica.codicefiscale.city.CityProvider;
import it.kamaladafrica.codicefiscale.utils.OmocodeUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@EqualsAndHashCode
public final class CodiceFiscale {

	private final static String VALIDATION_PATTERN = "^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$";
	private final static int CONTROL_PART_INDEX = 15;
	private final static int LASTNAME_PART_INDEX = 0;
	private final static int FIRSTNAME_PART_INDEX = 3;
	private final static int DATE_PART_INDEX = 6;
	private final static int BELFIORE_PART_INDEX = 11;

	private static final int OMOCODE_LEVEL_DATE_OFFSET = 3;

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

	private CodiceFiscale(Person person, LastnamePart lastname, NamePart firstname, DatePart date, BelfiorePart belfiore,
			int omocodeLevel) {
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
		return isEqual(CodiceFiscale.of(other));
	}

	public boolean isEqual(CodiceFiscale other) {
		return isEqual(other, true);
	}

	public boolean isEqual(String other, boolean ignoreOmocode) {
		return isEqual(CodiceFiscale.of(other), ignoreOmocode);
	}

	public boolean isEqual(CodiceFiscale other, boolean ignoreOmocode) {
		CodiceFiscale self = ignoreOmocode ? normalized() : this;
		CodiceFiscale that = ignoreOmocode ? other.normalized() : other;
		return Objects.equals(self.getValue(), that.getValue());
	}

	public CodiceFiscale toOmocodeLevel(int level) {
		Validate.inclusiveBetween(0, 7, level, "invalid omocode level: 0 <= %s <= 7", level);

		DatePart datePart = getDate().toOmocodeLevel(Math.max(0, level - OMOCODE_LEVEL_DATE_OFFSET));
		BelfiorePart belfiorePart = getBelfiore().toOmocodeLevel(Math.min(OMOCODE_LEVEL_DATE_OFFSET, level));

		return getOmocodeLevel() == level ? this
				: new CodiceFiscale(getPerson(), getLastname(), getFirstname(), datePart, belfiorePart, level);
	}

	public boolean isOmocode() {
		return getOmocodeLevel() > 0;
	}

	public boolean isCompatible(Person person) {
		return isEqual(CodiceFiscale.of(person));
	}

	private String computeValue() {
		String value = getUncheckedValue() + getControl().getValue();
		return validate(value);
	}

	private String computeUncheckedValue() {
		return new StringBuilder(getLastname().getValue()).append(getFirstname().getValue())
				.append(getDate().getValue()).append(getBelfiore().getValue()).toString();
	}

	private ControlPart computeControl() {
		return ControlPart.of(getUncheckedValue());
	}

	public static final CodiceFiscale of(String code) {
		return of(code, CityProvider.ofDefault());
	}

	public static final CodiceFiscale of(Person person) {

		final NamePart firstname = NamePart.of(person.getFirstname());
		final LastnamePart lastname = LastnamePart.of(person.getLastname());
		final DatePart date = DatePart.of(person.getBirthDate(), person.isFemale());
		final BelfiorePart belfiore = BelfiorePart.of(person.getCity());

		return new CodiceFiscale(person, lastname, firstname, date, belfiore, 0);
	}

	public static final CodiceFiscale of(String value, CityByBelfiore provider) {

		validate(value);

		final LastnamePart lastname = LastnamePart.from(value.substring(LASTNAME_PART_INDEX, FIRSTNAME_PART_INDEX));
		final NamePart firstname = NamePart.from(value.substring(FIRSTNAME_PART_INDEX, DATE_PART_INDEX));
		final DatePart date = DatePart.from(value.substring(DATE_PART_INDEX, BELFIORE_PART_INDEX));
		final BelfiorePart belfiore = BelfiorePart.from(value.substring(BELFIORE_PART_INDEX, CONTROL_PART_INDEX),
				provider);

		final Person person = Person.builder().firstname(firstname.getName()).lastname(lastname.getName())
				.birthDate(date.getDate()).isFemale(date.isFemale()).city(belfiore.getCity()).build();

		final CodiceFiscale result = new CodiceFiscale(person, lastname, firstname, date, belfiore,
				OmocodeUtils.level(value));

		Validate.isTrue(Objects.equals(result.getValue(), value), "expected %s, but found %s", value,
				result.getValue());

		return result;

	}

	public static boolean isCompatible(String code, Person person) {
		return CodiceFiscale.of(code).isCompatible(person);
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
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		final ControlPart control = ControlPart.of(value.substring(LASTNAME_PART_INDEX, CONTROL_PART_INDEX));
		final char currentControl = value.charAt(CONTROL_PART_INDEX);
		Validate.isTrue(control.isEqual(currentControl), "invalid control char: expected %s, but found %s",
				control.getValue(), currentControl);
		return value;
	}

}
