package it.kamaladafrica.codicefiscale.city.impl;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
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
public class CsvSupplier implements Supplier<Set<City>> {

	@NonNull
	private final URL csvUrl;
	@NonNull
	private final Charset charset;
	@NonNull
	private final CSVFormat format;
	@NonNull
	private final Function<CSVRecord, City> mapper;

	@Override
	public Set<City> get() {
		try {
			CSVParser parser = CSVParser.parse(csvUrl, charset, format);
			return StreamSupport.stream(parser.spliterator(), false).map(mapper).collect(toSet());
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}