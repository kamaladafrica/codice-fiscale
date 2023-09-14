package it.kamaladafrica.codicefiscale.city.algo;

/**
 * This stores a {@link SimilarityScore} implementation and a
 * {@link CharSequence} "left" string. The {@link #apply(CharSequence right)}
 * method accepts the "right" string and invokes the comparison function for the
 * pair of strings.
 *
 * <p>
 * The following is an example which finds the most similar string:
 * </p>
 * 
 * <pre>
 * SimilarityScore&lt;Integer&gt; similarityScore = new LevenshteinDistance();
 * String target = "Apache";
 * SimilarityScoreFrom&lt;Integer&gt; similarityScoreFrom = new SimilarityScoreFrom&lt;Integer&gt;(similarityScore, target);
 * String mostSimilar = null;
 * Integer shortestDistance = null;
 *
 * for (String test : new String[] { "Appaloosa", "a patchy", "apple" }) {
 * 	Integer distance = similarityScoreFrom.apply(test);
 * 	if (shortestDistance == null || distance &lt; shortestDistance) {
 * 		shortestDistance = distance;
 * 		mostSimilar = test;
 * 	}
 * }
 *
 * System.out.println("The string most similar to \"" + target + "\" " + "is \"" + mostSimilar + "\" because "
 * 		+ "its distance is only " + shortestDistance + ".");
 * </pre>
 *
 * @param <R> This is the type of similarity score used by the SimilarityScore
 *            function.
 * @since 1.0
 */
public class SimilarityScoreFrom<R> {

	/**
	 * Similarity score.
	 */
	private final SimilarityScore<R> similarityScore;

	/**
	 * Left parameter used in distance function.
	 */
	private final CharSequence left;

	/**
	 * This accepts the similarity score implementation and the "left" string.
	 *
	 * @param similarityScore This may not be null.
	 * @param left            This may be null here, but the
	 *                        SimilarityScore#compare(CharSequence left,
	 *                        CharSequence right) implementation may not accept
	 *                        nulls.
	 */
	public SimilarityScoreFrom(final SimilarityScore<R> similarityScore, final CharSequence left) {
		if (similarityScore == null) {
			throw new NullPointerException("The edit distance may not be null.");
		}

		this.similarityScore = similarityScore;
		this.left = left;
	}

	/**
	 * This compares "left" field against the "right" parameter using the
	 * "similarity score" implementation.
	 *
	 * @param right the second CharSequence
	 * @return The similarity score between two CharSequences
	 */
	public R apply(final CharSequence right) {
		return similarityScore.apply(left, right);
	}

	/**
	 * Gets the left parameter.
	 *
	 * @return The left parameter
	 */
	public CharSequence getLeft() {
		return left;
	}

	/**
	 * Gets the edit distance.
	 *
	 * @return The edit distance
	 */
	public SimilarityScore<R> getSimilarityScore() {
		return similarityScore;
	}

}
