package it.kamaladafrica.codicefiscale.internal;

import static org.apache.commons.lang3.Validate.matchesPattern;

import org.apache.commons.lang3.Validate;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.CityByBelfiore;
import it.kamaladafrica.codicefiscale.city.CityProvider;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.NONE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class BelfiorePart extends AbstractPart {

	private static final String VALIDATION_PATTERN = "^(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))$";

	private static final ImmutableIntArray OMOCODE_INDEXES = ImmutableIntArray.of(1, 2, 3);

	City city;

	private BelfiorePart(City city, Omocode level) {
		super(level);
		this.city = city;
	}

	public static BelfiorePart from(String value) {
		return from(value, CityProvider.ofDefault());
	}

	public static BelfiorePart from(String value, CityByBelfiore provider) {
		Validate.notEmpty(value);
		Validate.notNull(provider);
		matchesPattern(value, VALIDATION_PATTERN);
		Omocode omocodeLevel = Omocode.of(value, OMOCODE_INDEXES);
		City input = toInput(omocodeLevel.normalize(value), provider);
		Validate.notNull(input, "belfiore not found");
		return new BelfiorePart(input, omocodeLevel);
	}

	public static BelfiorePart of(City city) {
		return new BelfiorePart(city, Omocode.of(OMOCODE_INDEXES));
	}

	@Override
	protected String computeValue() {
		String value = city.getBelfiore();
		matchesPattern(value, VALIDATION_PATTERN);
		return value;
	}

	private static City toInput(String normalizedValue, CityByBelfiore provider) {
		return provider.findByBelfiore(normalizedValue);
	}

	@Override
	protected String applyOmocodeLevel(String value) {
		return getOmocodeLevel().apply(value);
	}

	public BelfiorePart toOmocodeLevel(int level) {
		return getOmocodeLevel().isLevel(level) ? this : new BelfiorePart(city, getOmocodeLevel().withLevel(level));
	}

	@Override
	protected void validateValue(String value) {
		matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

}
