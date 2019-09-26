package it.kamaladafrica.codicefiscale.internal;

import static it.kamaladafrica.codicefiscale.utils.OmocodeUtils.level;
import static org.apache.commons.lang3.Validate.matchesPattern;

import org.apache.commons.lang3.Validate;

import com.google.common.primitives.ImmutableIntArray;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.CityByBelfiore;
import it.kamaladafrica.codicefiscale.city.CityProvider;
import it.kamaladafrica.codicefiscale.utils.OmocodeUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BelfiorePart extends AbstractPart {

	private final static String VALIDATION_PATTERN = "^(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))$";

	private static final ImmutableIntArray OMOCODE_INDEXES = ImmutableIntArray.of(3, 2, 1);

	City city;

	private BelfiorePart(City city, int level) {
		super(level);
		this.city = city;
	}

	public static BelfiorePart from(String value) {
		return from(value, CityProvider.ofDefault());
	}

	public static BelfiorePart from(@NonNull String value, @NonNull CityByBelfiore provider) {
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		City input = toInput(value, provider);
		Validate.notNull(input, "belfiore not found");
		return new BelfiorePart(input, getOmocodeLevel(value));
	}

	private static int getOmocodeLevel(String value) {
		return level(value, OMOCODE_INDEXES.toArray());
	}

	public static BelfiorePart of(City city) {
		return new BelfiorePart(city);
	}

	@Override
	protected String computeValue() {
		String value = city.getBelfiore();
		matchesPattern(value, VALIDATION_PATTERN, "invalid value: %s", value);
		return value;
	}

	private static City toInput(String value, CityByBelfiore provider) {
		value = normalizeOmocode(value);
		return provider.findByBelfiore(value);
	}

	private static String normalizeOmocode(String value) {
		return OmocodeUtils.normalize(value, OMOCODE_INDEXES.toArray());
	}

	@Override
	protected String applyOmocodeLevel(String value) {
		final int level = getOmocodeLevel();
		if (level > 0) {
			return OmocodeUtils.apply(value,
					OMOCODE_INDEXES.subArray(0, Math.min(level, OMOCODE_INDEXES.length())).toArray());
		}
		return value;
	}

	public BelfiorePart toOmocodeLevel(int level) {
		return getOmocodeLevel() == level ? this : new BelfiorePart(city, level);
	}

	@Override
	protected void validateValue(String value) {
		matchesPattern(value, VALIDATION_PATTERN, "unexpected result: %s", value);
	}

	public static void main(String[] args) {
		CityProvider provider = CityProvider.ofDefault();

		BelfiorePart part = BelfiorePart.from("H501", provider);
		System.out.println(part);

		City morlupo = provider.findByName("roma");
		part = BelfiorePart.of(morlupo);
		System.out.println(part);

		System.out.println(part.toOmocodeLevel(2).getValue());

		// provider.findAll().forEach(System.out::println);

//		part = DatePart.of("77B59");
//
//		System.out.println(part.getInput());
//		System.out.println(part.getValue());
//
//		part = DatePart.of("77B5V");
//
//		System.out.println(part.getInput());
//		System.out.println(part.getValue());

	}

}
