package it.kamaladafrica.codicefiscale.city.impl.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class SimpleCsvParserTest {

	SimpleCsvParser uut = new SimpleCsvParser(';', '"');

	@Test
	public void testParseSingleLine() throws IOException {
		String csv = "A;B;C\nPino;\"1\";2\nGino;\"3\";4\nFranco;\"5\";\"\\\"6\\\"\"";
		ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
		List<String[]> result = uut.parse(in, StandardCharsets.UTF_8).skip(1).collect(Collectors.toList());

		List<String> colA = result.stream().map(s -> s[0]).collect(Collectors.toList());
		List<String> colB = result.stream().map(s -> s[1]).collect(Collectors.toList());
		List<String> colC = result.stream().map(s -> s[2]).collect(Collectors.toList());

		assertEquals(3, colA.size());
		assertEquals(3, colB.size());
		assertEquals(3, colC.size());

		assertFalse(colA.contains("A"));
		assertFalse(colB.contains("B"));
		assertFalse(colC.contains("C"));

		assertEquals(1, colA.indexOf("Gino"));
		assertEquals(0, colB.indexOf("1"));
		assertEquals(2, colC.indexOf("\"6\""));

	}

}
