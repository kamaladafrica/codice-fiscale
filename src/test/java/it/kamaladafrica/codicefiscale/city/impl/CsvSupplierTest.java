package it.kamaladafrica.codicefiscale.city.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

public class CsvSupplierTest {

	@Test(expected = IllegalArgumentException.class)
	public void testGetException() throws MalformedURLException {
		new CsvSupplier(new URL("file:///not_existing_file"), StandardCharsets.UTF_8, CSVFormat.DEFAULT, e -> null)
				.get();
	}

	@Test
	public void testGet() throws MalformedURLException {
		new CsvSupplier(getClass().getResource(CityProviderImpl.ISTAT_RESOURCE_PATH), StandardCharsets.UTF_8,
				CSVFormat.DEFAULT, e -> null).get();
	}
}
