package it.kamaladafrica.codicefiscale.internal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class AbstractPart implements Part {

	Omocode omocodeLevel;

	@Getter(lazy = true)
	String value = computeValueInternal();

	protected AbstractPart() {
		this(Omocode.unsupported());
	}

	protected AbstractPart(Omocode omocodeLevel) {
		this.omocodeLevel = omocodeLevel;
	}

	private String computeValueInternal() {
		final String newValue = applyOmocodeLevel(computeValue());
		validateValue(newValue);
		return newValue;
	}

	protected abstract String computeValue();

	protected abstract void validateValue(String value);

	protected abstract String applyOmocodeLevel(String value);

}
