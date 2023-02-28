package it.kamaladafrica.codicefiscale.city.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

public class CsvSupplierTest {

	@Test
	public void testGetException() throws MalformedURLException {
		CsvSupplier supplier = new CsvSupplier(new URL("file:///not_existing_file"), StandardCharsets.UTF_8,
				CSVFormat.DEFAULT, e -> null);
		assertThrows(IllegalArgumentException.class, () -> supplier.get());
	}

	@Test
	public void testGet() throws MalformedURLException {
		CsvSupplier supplier = new CsvSupplier(getClass().getResource(CityProviderImpl.ITALIA_RESOURCE_PATH),
				StandardCharsets.UTF_8, CSVFormat.DEFAULT, e -> null);
		try {
			supplier.get();
		} catch (Exception e) {
			fail();
		}
	}
}
