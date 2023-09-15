package it.kamaladafrica.codicefiscale.city.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import it.kamaladafrica.codicefiscale.city.impl.csv.AbstractCsvStreamSupplier;
import it.kamaladafrica.codicefiscale.city.impl.csv.CsvException;

public class AbstractCsvStreamSupplierTest {

	@Test
	public void testGetException() throws MalformedURLException {
		AbstractCsvStreamSupplier<String> supplier = new AbstractCsvStreamSupplier<String>(
				new URL("file:///not_existing_file"), ';') {
			@Override
			protected String mapper(String[] row) {
				return row.toString();
			}
		};
		assertThrows(CsvException.class, () -> supplier.get());
	}

	@Test
	public void testGet() throws MalformedURLException {
		AbstractCsvStreamSupplier<String> supplier = new AbstractCsvStreamSupplier<String>(
				getClass().getResource(CityProviderImpl.ITALIA_RESOURCE_PATH), ';') {
			@Override
			protected String mapper(String[] row) {
				return row.toString();
			}
		};
		try {
			supplier.get();
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testNullResource() throws MalformedURLException {
		assertThrows(NullPointerException.class, () -> new AbstractCsvStreamSupplier<String>(null, ';') {
			@Override
			protected String mapper(String[] row) {
				return row.toString();
			}
		});

	}
}
