package it.kamaladafrica.codicefiscale.utils;

import java.util.Arrays;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtils {

	public static void reverse(int[] array) {
		int i = 0;
		int j = array.length - 1;
		while (i < j) {
			int x = array[i];
			array[i] = array[j];
			array[j] = x;
			i++;
			j--;
		}
	}

	public static int[] copyOf(int[] array) {
		return Arrays.copyOf(array, array.length);
	}

}
