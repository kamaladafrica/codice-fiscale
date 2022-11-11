package it.kamaladafrica.codicefiscale.city.impl;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.SimilarityScoreFrom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import it.kamaladafrica.codicefiscale.city.CityProvider;

public final class CityProviderImpl implements CityProvider {

	public static final double DEFAULT_MINIMUM_MATCH_SCORE = 0.8;
	public static final double EXACT_MATCH_SCORE = 1.0;

	static final String ITALIA_RESOURCE_PATH = "/italia.csv";
	static final String ESTERI_RESOURCE_PATH = "/esteri.csv";

	private final ImmutableMap<String, City> cityByName;
	private final ImmutableMap<String, City> cityByBelfiore;

	private final double minimumMatchScore;

	private CityProviderImpl(Set<City> cities, double minimumMatchScore) {
		this.minimumMatchScore = minimumMatchScore;
		this.cityByName = cities.stream().collect(toImmutableMap(CityProviderImpl::cityName, identity()));
		this.cityByBelfiore = cities.stream().collect(toImmutableMap(City::getBelfiore, identity()));
	}

	private static String cityName(City city) {
		return String.format("%s(%s)", city.getName(), city.getProv());
	}

	private static String normalize(String s) {
		return nullToEmpty(s).toUpperCase(CodiceFiscale.LOCALE);
	}

	@Override
	public List<City> findAll() {
		return ImmutableList.sortedCopyOf((a, b) -> a.getName().compareTo(b.getName()), cityByName.values());
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
				.concat(ItaliaCsvSupplier.of(CityProviderImpl.class.getResource(ITALIA_RESOURCE_PATH)).get(),
						EsteriCsvSupplier.of(CityProviderImpl.class.getResource(ESTERI_RESOURCE_PATH)).get())
				.collect(Collectors.toSet());

	}

}
