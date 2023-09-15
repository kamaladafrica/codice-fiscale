package it.kamaladafrica.codicefiscale.city.impl;

import java.util.stream.Stream;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.CityStreamSupplier;
import lombok.Value;

@Value(staticConstructor = "of")
public class CompositeCityStreamSupplier implements CityStreamSupplier {

	private final CityStreamSupplier[] suppliers;

	@Override
	public Stream<City> get() {
		return Stream.of(suppliers).flatMap(CityStreamSupplier::get);
	}

	public static CompositeCityStreamSupplier of(CityStreamSupplier first, CityStreamSupplier... others) {
		int size = 1 + (others == null ? 0 : others.length);
		CityStreamSupplier[] suppliers = new CityStreamSupplier[size];
		suppliers[0] = first;
		if (others != null && others.length > 0) {
			System.arraycopy(others, 0, suppliers, 1, others.length);
		}
		return of(suppliers);
	}

}
