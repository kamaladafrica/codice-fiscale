package it.kamaladafrica.codicefiscale.utils;

import java.io.Serializable;
import java.util.Map;

import lombok.Value;

@Value(staticConstructor = "of")
public class Pair<L, R> implements Map.Entry<L, R>, Serializable {

	private static final long serialVersionUID = -4440235638868672592L;

	private final L left;
	private final R right;

	@Override
	public L getKey() {
		return left;
	}

	@Override
	public R getValue() {
		return right;
	}

	@Override
	public R setValue(R value) {
		throw new UnsupportedOperationException();
	}

}
