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
 * Reads records from Anagrafe nazionale della popolazione residente csv file
 * {@link https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv}
 */
@Getter(PRIVATE)
public class ItaliaCsvSupplier extends CsvSupplier {

	private ItaliaCsvSupplier(URL resource) {
		super(resource, StandardCharsets.UTF_8, buildFormat(), mapper());
	}

	private static Function<CSVRecord, City> mapper() {
		return record -> City.builder().name(record.get(1).toUpperCase()).prov(record.get(2).toUpperCase())
				.belfiore(record.get(0).toUpperCase()).build();
	}

	private static CSVFormat buildFormat() {
		return CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader().withTrim();
	}

	public static ItaliaCsvSupplier of(URL url) {
		return new ItaliaCsvSupplier(url);
	}

}