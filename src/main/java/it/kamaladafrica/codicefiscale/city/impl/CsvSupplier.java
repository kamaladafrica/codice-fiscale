package it.kamaladafrica.codicefiscale.city.impl;

import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import it.kamaladafrica.codicefiscale.City;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter(PRIVATE)
@RequiredArgsConstructor
public class CsvSupplier implements Supplier<Stream<City>> {

	@NonNull
	private final URL csvUrl;
	@NonNull
	private final Charset charset;
	@NonNull
	private final CSVFormat format;
	@NonNull
	private final Function<CSVRecord, City> mapper;

	@Override
	public Stream<City> get() {
		try {
			return streamRecords(parse()).map(mapper);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	protected Stream<CSVRecord> streamRecords(CSVParser parser) {
		return StreamSupport.stream(parser.spliterator(), false);
	}

	protected CSVParser parse() throws IOException {
		return CSVParser.parse(csvUrl, charset, format);
	}

}