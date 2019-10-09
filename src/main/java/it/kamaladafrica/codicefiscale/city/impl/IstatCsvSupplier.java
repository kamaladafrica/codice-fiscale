package it.kamaladafrica.codicefiscale.city.impl;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import it.kamaladafrica.codicefiscale.City;
import lombok.Getter;

/**
 * Reads recordo from istat csv file
 * {@link https://www.istat.it/storage/codici-unita-amministrative/Elenco-codici-statistici-e-denominazioni-delle-unita-territoriali.zip}
 */
@Getter(PRIVATE)
public class IstatCsvSupplier extends CsvSupplier {

	private IstatCsvSupplier(URL resource) {
		super(resource, StandardCharsets.UTF_8, buildFormat(), mapper());
	}

	private static Function<CSVRecord, City> mapper() {
		return record -> City.builder().name(record.get(6).toUpperCase()).prov(record.get(13).toUpperCase())
				.belfiore(record.get(18).toUpperCase()).build();
	}

	private static CSVFormat buildFormat() {
		return CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().withTrim();
	}

	public static CsvSupplier of(URL url) {
		return new IstatCsvSupplier(url);
	}

}