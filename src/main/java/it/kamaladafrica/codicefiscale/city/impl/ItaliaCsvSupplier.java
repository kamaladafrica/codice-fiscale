package it.kamaladafrica.codicefiscale.city.impl;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import lombok.Getter;

/**
 * Reads records from Anagrafe nazionale della popolazione residente csv file
 * <a href=
 * "https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv">https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv</a>
 */
@Getter(PRIVATE)
public final class ItaliaCsvSupplier extends CsvSupplier {

	private ItaliaCsvSupplier(URL resource) {
		super(resource, StandardCharsets.UTF_8, buildFormat(), mapper());
	}

	private static Function<CSVRecord, City> mapper() {
		return record -> City.builder().name(record.get(1).toUpperCase(CodiceFiscale.LOCALE))
				.prov(record.get(2).toUpperCase(CodiceFiscale.LOCALE))
				.belfiore(record.get(0).toUpperCase(CodiceFiscale.LOCALE)).build();
	}

	private static CSVFormat buildFormat() {
		return CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader().withTrim();
	}

	public static ItaliaCsvSupplier of(URL url) {
		return new ItaliaCsvSupplier(url);
	}

}