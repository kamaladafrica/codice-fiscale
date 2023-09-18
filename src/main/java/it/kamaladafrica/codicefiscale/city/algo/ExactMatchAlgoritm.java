package it.kamaladafrica.codicefiscale.city.algo;

import java.util.Objects;

/**
 * A similarity algorithm that returns 1.0 only if left and right matches, 0.0
 * otherwise.
 *
 * By default computation ignore the case,so "AAA" and "aaa" are equals
 */
public class ExactMatchAlgoritm implements ScoreAlgoritm<Double> {

	public static final double MATCH_SCORE = 1.0;
	public static final double NO_MATCH_SCORE = 0.0;

	private final boolean ignoreCase;

	public ExactMatchAlgoritm(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public ExactMatchAlgoritm() {
		this(true);
	}

	/**
	 * Computes the Exact Match Similarity between two character sequences.
	 *
	 * <pre>
	 * sim.apply(null, null)          = IllegalArgumentException
	 * sim.apply("foo", null)         = IllegalArgumentException
	 * sim.apply(null, "foo")         = IllegalArgumentException
	 * sim.apply("", "")              = 1.0
	 * sim.apply("foo", "foo")        = 1.0
	 * sim.apply("foo", "foo ")       = 0.0
	 * sim.apply("", "a")             = 0.0
	 * sim.apply("frog", "fog")       = 0.0
	 * sim.apply("fly", "ant")        = 0.0
	 * sim.apply("fly", "FLY")        = 1.0 if ignoreCase is true, 0.0 otherwise
	 * sim.apply("fly", "fLy")        = 1.0 if ignoreCase is true, 0.0 otherwise
	 * </pre>
	 *
	 * @param left  the first CharSequence, must not be null
	 * @param right the second CharSequence, must not be null
	 * @return result similarity
	 * @throws IllegalArgumentException if either CharSequence input is {@code null}
	 */
	@Override
	public Double apply(final CharSequence left, final CharSequence right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException("CharSequences must not be null");
		}
		return toScore(areEquals(left, right, ignoreCase));
	}

	private static boolean areEquals(final CharSequence left, final CharSequence right, boolean ignoreCase) {
		if (Objects.equals(left, right)) {
			return true;
		}

		if (left.length() != right.length()) {
			return false;
		}

		// Step-wise comparison
		final int length = left.length();
		for (int i = 0; i < length; i++) {
			char lc = left.charAt(i);
			char rc = right.charAt(i);
			if (ignoreCase && (Character.isLowerCase(lc) != Character.isLowerCase(rc))) {
				lc = Character.toLowerCase(lc);
				rc = Character.toLowerCase(rc);
			}
			if (lc != rc) {
				return false;
			}
		}
		return true;
	}

	private static double toScore(boolean match) {
		return match ? MATCH_SCORE : NO_MATCH_SCORE;
	}

}
