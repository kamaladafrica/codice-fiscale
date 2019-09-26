package it.kamaladafrica.codicefiscale.internal;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class AbstractPart implements Part {

	int omocodeLevel;

	@Getter(lazy = true)
	String value = computeValueInternal();

	public AbstractPart() {
		this(0);
	}

	public AbstractPart(int omocodeLevel) {
		inclusiveBetween(0, 7, omocodeLevel, "invalid omocode level: 0 <= %s <= 7", omocodeLevel);
		this.omocodeLevel = omocodeLevel;
	}

	private String computeValueInternal() {
		final String value = applyOmocodeLevel(computeValue());
		validateValue(value);
		return value;
	}

	protected abstract String computeValue();

	protected abstract void validateValue(String value);

	protected abstract String applyOmocodeLevel(String value);

}
