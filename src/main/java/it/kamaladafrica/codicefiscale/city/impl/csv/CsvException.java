package it.kamaladafrica.codicefiscale.city.impl.csv;

public class CsvException extends RuntimeException {

	private static final long serialVersionUID = -4707593681808158601L;

	public CsvException() {
	}

	public CsvException(String message) {
		super(message);
	}

	public CsvException(Throwable cause) {
		super(cause);
	}

	public CsvException(String message, Throwable cause) {
		super(message, cause);
	}

	public CsvException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
