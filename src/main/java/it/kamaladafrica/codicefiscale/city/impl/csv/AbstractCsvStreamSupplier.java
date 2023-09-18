package it.kamaladafrica.codicefiscale.city.impl.csv;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

@Getter(AccessLevel.PRIVATE)
public abstract class AbstractCsvStreamSupplier<T> implements Supplier<Stream<T>> {

	private final URL csvUrl;

	private final SimpleCsvParser parser;

	private final Charset charset;

	protected AbstractCsvStreamSupplier(@NonNull URL csvUrl, char separator, char quotes, @NonNull Charset charset) {
		parser = new SimpleCsvParser(separator, quotes);
		this.charset = charset;
		this.csvUrl = csvUrl;
	}

	protected AbstractCsvStreamSupplier(@NonNull URL csvUrl, char separator) {
		this(csvUrl, separator, SimpleCsvParser.DEFAULT_QUOTE_CHARACTER, StandardCharsets.UTF_8);
	}

	@Override
	public Stream<T> get() {
		return streamRecords().map(this::mapper);
	}

	protected Stream<String[]> streamRecords() {
		return parser.parse(csvUrl, charset).skip(1); // skip headers
	}

	protected abstract T mapper(String[] row);

}