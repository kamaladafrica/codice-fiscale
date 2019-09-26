package it.kamaladafrica.codicefiscale.city;

import java.util.List;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.city.impl.CityProviderImpl;

public interface CityProvider extends CityByName, CityByBelfiore {

	List<City> findAll();

	public static CityProvider ofDefault() {
		return CityProviderImpl.ofDefault();
	}

}
