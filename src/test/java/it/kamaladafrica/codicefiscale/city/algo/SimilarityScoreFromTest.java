package it.kamaladafrica.codicefiscale.city.algo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimilarityScoreFromTest {

	SimilarityScoreFrom<CharSequence> uut;
	SimilarityScoreImpl similarityScore;

	@Before
	public void beforeTest() {
		similarityScore = new SimilarityScoreImpl();
		uut = new SimilarityScoreFrom<>(similarityScore, "left");
	}

	@Test
	public void testSimilarityScoreFrom() {
		assertThrows(NullPointerException.class, () -> new SimilarityScoreFrom<>(null, "left"));
	}

	@Test
	public void testApply() {
		CharSequence result = uut.apply("right");
		assertEquals("right", result);
		assertTrue(similarityScore.isApplyCalled());
		
		uut.apply("nextRight");
		uut.apply("oneRightMore");

		assertEquals(3, similarityScore.getCallCount());
	}

	@Test
	public void testGetLeft() {
		assertEquals("left", uut.getLeft());
	}

	@Test
	public void testGetSimilarityScore() {
		assertSame(similarityScore, uut.getSimilarityScore());
	}

	private static class SimilarityScoreImpl implements SimilarityScore<CharSequence> {

		private int callCount = 0;

		@Override
		public CharSequence apply(CharSequence left, CharSequence right) {
			callCount++;
			return right;
		}

		boolean isApplyCalled() {
			return callCount > 0;
		}

		int getCallCount() {
			return callCount;
		}

	}
}
