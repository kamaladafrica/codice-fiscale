package it.kamaladafrica.codicefiscale.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

	public static final String EMPTY_STRING = "";

	public static String defaultString(String s, String defaultString) {
		return s == null ? defaultString : s;
	}

	public static String defaultString(String s) {
		return defaultString(s, EMPTY_STRING);
	}

	public static String removeEnd(String s, String remove) {
		if (s == null) {
			return null;
		}
		if (remove == null) {
			return s;
		}
		if (s.endsWith(remove)) {
			return s.substring(0, s.lastIndexOf(remove));
		}
		return s;
	}

	public static String replaceEach(String s, String[] searchList, String[] replaceList) {
		if (s == null) {
			return null;
		}
		if (searchList == null || replaceList == null) {
			return s;
		}
		if (searchList.length != replaceList.length) {
			throw new IllegalArgumentException("searchList and replaceList must have same size");
		}

		for (int i = 0; i < replaceList.length; i++) {
			if (searchList[i] != null && replaceList[i] != null) {
				s = s.replaceAll(searchList[i], replaceList[i]);
			}
		}
		return s;
	}

	public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
		if (cs1 == cs2) {
			return true;
		}
		if (cs1 == null || cs2 == null || (cs1.length() != cs2.length())) {
			return false;
		}
		if (cs1 instanceof String && cs2 instanceof String) {
			return cs1.equals(cs2);
		}
		// Step-wise comparison
		final int length = cs1.length();
		for (int i = 0; i < length; i++) {
			if (cs1.charAt(i) != cs2.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}
