package it.kamaladafrica.codicefiscale.internal;

import it.kamaladafrica.codicefiscale.utils.ArrayUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "wrap")
public class ImmutableIntArray {

	@NonNull
	@Getter(AccessLevel.NONE)
	private final int[] array;

	public int get(int index) {
		return array[index];
	}

	public int indexOf(int value) {
		for (int i = 0; i < array.length; i++) {
			if (get(i) == value) {
				return i;
			}
		}
		return -1;
	}

	public boolean contains(int value) {
		return indexOf(value) >= 0;
	}

	public static ImmutableIntArray of(int... array) {
		return wrap(ArrayUtils.copyOf(array));
	}

	public int length() {
		return array.length;
	}

	public int[] toArray() {
		return ArrayUtils.copyOf(array);
	}

	public ImmutableIntArray reverse() {
		int[] array = toArray();
		ArrayUtils.reverse(array);
		return wrap(array);
	}
}
