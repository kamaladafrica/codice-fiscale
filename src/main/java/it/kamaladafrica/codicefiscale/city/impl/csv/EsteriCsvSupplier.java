package it.kamaladafrica.codicefiscale.city.impl.csv;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;
import java.util.stream.Stream;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import it.kamaladafrica.codicefiscale.city.CityStreamSupplier;
import lombok.Getter;

/**
 * Reads record from istat csv file <a href=
 * "https://www.istat.it/it/files//2011/01/Elenco-codici-e-denominazioni-unita-territoriali-estere.zip">https://www.istat.it/it/files//2011/01/Elenco-codici-e-denominazioni-unita-territoriali-estere.zip</a>
 */
@Getter(PRIVATE)
public final class EsteriCsvSupplier extends AbstractCsvStreamSupplier<City> implements CityStreamSupplier {

	public static final char SEPARATOR = ';';

	private EsteriCsvSupplier(URL resource) {
		super(resource, SEPARATOR);
	}

	@Override
	protected City mapper(String[] row) {
		return City.builder()
				.name(row[6].toUpperCase(CodiceFiscale.LOCALE))
				.prov(row[2].toUpperCase(CodiceFiscale.LOCALE))
				.belfiore(row[9].toUpperCase(CodiceFiscale.LOCALE))
				.build();
	}

	@Override
	protected Stream<String[]> streamRecords() {
		return super.streamRecords().filter(row -> row[9].startsWith("Z"));
	}

	public static EsteriCsvSupplier of(URL url) {
		return new EsteriCsvSupplier(url);
	}

}