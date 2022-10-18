package it.kamaladafrica.codicefiscale.city.impl;

import static lombok.AccessLevel.PRIVATE;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import it.kamaladafrica.codicefiscale.City;
import it.kamaladafrica.codicefiscale.CodiceFiscale;
import lombok.Getter;

/**
 * Reads record from istat csv file <a href=
 * "https://www.istat.it/it/files//2011/01/Elenco-codici-e-denominazioni-unita-territoriali-estere.zip">https://www.istat.it/it/files//2011/01/Elenco-codici-e-denominazioni-unita-territoriali-estere.zip</a>
 */
@Getter(PRIVATE)
public final class EsteriCsvSupplier extends CsvSupplier {

	private EsteriCsvSupplier(URL resource) {
		super(resource, StandardCharsets.UTF_8, buildFormat(), mapper());
	}

	private static Function<CSVRecord, City> mapper() {
		return record -> City.builder().name(record.get(6).toUpperCase(CodiceFiscale.LOCALE))
				.prov(record.get(2).toUpperCase(CodiceFiscale.LOCALE))
				.belfiore(record.get(9).toUpperCase(CodiceFiscale.LOCALE)).build();
	}

	@Override
	protected Stream<CSVRecord> streamRecords(CSVParser parser) {
		// ignore Italy and n.d.
		return super.streamRecords(parser).filter(record -> record.get(9).startsWith("Z"));
	}

	private static CSVFormat buildFormat() {
		return CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().withTrim();
	}

	public static EsteriCsvSupplier of(URL url) {
		return new EsteriCsvSupplier(url);
	}

}