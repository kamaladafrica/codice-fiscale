package it.kamaladafrica.codicefiscale.city.impl;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.text.similarity.LongestCommonSubsequenceDistance;
import org.apache.commons.text.similarity.SimilarityScoreFrom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.CityProvider;

public class CityProviderImpl implements CityProvider {

	private static final String ISTAT_RESOURCE_PATH = "/istat.csv";

	private final ImmutableMap<String, City> cityByName;
	private final ImmutableMap<String, City> cityByBelfiore;

	boolean exactSearch;
	
	private CityProviderImpl(Set<City> cities, boolean exactSearch) {
		this.exactSearch = exactSearch;
		this.cityByName = cities.stream().collect(toImmutableMap(CityProviderImpl::cityName , identity()));
		this.cityByBelfiore = cities.stream().collect(toImmutableMap(City::getBelfiore , identity()));
	}
	
	private static String cityName(City city){
		return String.format("%s(%s)", city.getName(), city.getProv());
		//return city.getName();
	}
	
	private static String normalize(String s) {
		return nullToEmpty(s).toUpperCase();
	}

	@Override
	public List<City> findAll() {
		return ImmutableList.sortedCopyOf((a, b) -> a.getName().compareTo(b.getName()), cityByName.values());
	}

	@Override
	public City findByName(String name) {
		cityByName.keySet().forEach(System.out::println);
		
		City result = null;
		final String term = normalize(name);
		if (!term.isEmpty()) {

			result = cityByName.get(term);
			if (!exactSearch && result == null) {
				final SimilarityScoreFrom<Integer> score = new SimilarityScoreFrom<>(
						new LongestCommonSubsequenceDistance(), term);

				result = cityByName.entrySet().stream().sorted((e1, e2) -> {
					int score1 = score.apply(e1.getKey());
					int score2 = score.apply(e2.getKey());
					return Integer.compare(score1, score2);
				}).findFirst().map(Entry::getValue).orElse(null);
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
	
	public static final CityProviderImpl ofDefault() {
		return of(defaultSupplier(), false);
	}
	
	public static final CityProviderImpl of(Supplier<Set<City>> supplier, boolean exact) {
		return of(supplier.get(), exact);
	}

	public static final CityProviderImpl of(Set<City> cities, boolean exact) {
		return new CityProviderImpl(cities, exact);
	}

	private static Supplier<Set<City>> defaultSupplier(){
		return IstatCsvSupplier.of(CityProviderImpl.class.getResource(ISTAT_RESOURCE_PATH));
	}
	
}
