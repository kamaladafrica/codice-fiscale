package it.kamaladafrica.codicefiscale.city.impl;

import static java.util.function.Function.identity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import it.kamaladafrica.codicefiscale.city.CityProvider;
import it.kamaladafrica.codicefiscale.city.algo.JaroWinklerSimilarity;
import it.kamaladafrica.codicefiscale.city.algo.SimilarityScoreFrom;
import it.kamaladafrica.codicefiscale.utils.Pair;

public final class CityProviderImpl implements CityProvider {

	public static final double DEFAULT_MINIMUM_MATCH_SCORE = 0.8;
	public static final double EXACT_MATCH_SCORE = 1.0;

	static final String ITALIA_RESOURCE_PATH = "italia.csv";
	static final String ESTERI_RESOURCE_PATH = "esteri.csv";
    static final String ESTERI_CESSATI_RESOURCE_PATH = "esteri-cessati.csv";

	private final Map<String, City> cityByName;
	private final Map<String, City> cityByBelfiore;

	private final double minimumMatchScore;

	private CityProviderImpl(Set<City> cities, double minimumMatchScore) {
		this.minimumMatchScore = minimumMatchScore;
		this.cityByName = Collections
				.unmodifiableMap(cities.stream().collect(Collectors.toMap(CityProviderImpl::cityName, identity())));
		this.cityByBelfiore = Collections
				.unmodifiableMap(cities.stream().collect(Collectors.toMap(City::getBelfiore, identity())));
	}

	private static String cityName(City city) {
		return String.format("%s(%s)", city.getName(), city.getProv());
	}

	private static String normalize(String s) {
		if (s == null || s.isEmpty()) {
			return "";
		}
		return s.toUpperCase(CodiceFiscale.LOCALE);
	}

	@Override
	public List<City> findAll() {
		return Collections.unmodifiableList(
				cityByName.values().stream().sorted(Comparator.comparing(City::getName)).collect(Collectors.toList()));
	}

	@Override
	public City findByName(String name) {
		City result = null;
		final String term = normalize(name);
		if (!term.isEmpty()) {

			result = cityByName.get(term);
			if (minimumMatchScore != EXACT_MATCH_SCORE && result == null) {
				final SimilarityScoreFrom<Double> score = new SimilarityScoreFrom<>(new JaroWinklerSimilarity(), term);
				result = cityByName.entrySet().stream().map(e -> Pair.of(e.getValue(), score.apply(e.getKey())))
						.filter(e -> e.getValue() >= minimumMatchScore).max(Comparator.comparing(Entry::getValue))
						.map(Entry::getKey).orElse(null);
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("not found: " + term);
		}
		return result;
	}

	@Override
	public City findByBelfiore(String belfiore) {
		final String term = normalize(belfiore);
		City result = cityByBelfiore.get(term);
		if (result == null) {
			throw new IllegalArgumentException("not found: " + term);
		}
		return result;
	}

	public static CityProviderImpl ofDefault() {
		return of(defaultSupplier(), DEFAULT_MINIMUM_MATCH_SCORE);
	}

	public static CityProviderImpl of(Supplier<Set<City>> supplier, double minimumMatchScore) {
		return of(supplier.get(), minimumMatchScore);
	}

	public static CityProviderImpl of(Set<City> cities, double minimumMatchScore) {
		return new CityProviderImpl(cities, minimumMatchScore);
	}

	public static CityProviderImpl of(Supplier<Set<City>> supplier) {
		return of(supplier.get(), DEFAULT_MINIMUM_MATCH_SCORE);
	}

	public static CityProviderImpl of(Set<City> cities) {
		return of(cities, DEFAULT_MINIMUM_MATCH_SCORE);
	}

	private static Supplier<Set<City>> defaultSupplier() {
		return () -> Stream
				.of(ItaliaCsvSupplier.of(CityProviderImpl.class.getResource(ITALIA_RESOURCE_PATH)).get(),
						EsteriCsvSupplier.of(CityProviderImpl.class.getResource(ESTERI_RESOURCE_PATH)).get(),
						EsteriCsvSupplier.of(CityProviderImpl.class.getResource(ESTERI_CESSATI_RESOURCE_PATH)).get())
				.flatMap(identity()).collect(Collectors.toSet());
	}

}
