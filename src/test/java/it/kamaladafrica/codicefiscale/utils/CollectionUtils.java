package it.kamaladafrica.codicefiscale.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionUtils {

	public static <T> Set<T> unmodifiableSet(Collection<T> collection) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(collection));
	}

}
