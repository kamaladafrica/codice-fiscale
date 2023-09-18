package it.kamaladafrica.codicefiscale.city.impl.csv;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import it.kamaladafrica.codicefiscale.city.CityStreamSupplier;
import lombok.Getter;

/**
 * Reads records from Anagrafe nazionale della popolazione residente csv file
 * <a href=
 * "https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv">https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv</a>
 */
@Getter(PRIVATE)
public final class ItaliaCsvSupplier extends AbstractCsvStreamSupplier<City> implements CityStreamSupplier {

	public static final char SEPARATOR = ',';

	private ItaliaCsvSupplier(URL resource) {
		super(resource, SEPARATOR);
	}

	@Override
	protected City mapper(String[] row) {
		return City.builder()
				.name(row[1].toUpperCase(CodiceFiscale.LOCALE))
				.prov(row[2].toUpperCase(CodiceFiscale.LOCALE))
				.belfiore(row[0].toUpperCase(CodiceFiscale.LOCALE))
				.build();
	}

	public static ItaliaCsvSupplier of(URL url) {
		return new ItaliaCsvSupplier(url);
	}

}