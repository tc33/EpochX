package org.epochx.op.selection;

import static org.junit.Assert.*;

import java.util.*;

import org.epochx.gp.model.GPModelDummy;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.Life;
import org.epochx.representation.CandidateProgram;
import org.junit.*;

public class TournamentSelectorTest {

	private TournamentSelector selector;
	private GPModelDummy model;
	private List<CandidateProgram> pop;

	@Before
	public void setUp() {
		model = new GPModelDummy();
		selector = new TournamentSelector(model, 3);
		Life.get().fireConfigureEvent();

		pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(null));
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool of
	 * size 0.
	 */
	@Test
	public void testGetPoolSizeZero() {
		try {
			selector.getPool(pop, 0);
			fail("Illegal argument exception not thrown for a pool size of 0");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * a null population.
	 */
	@Test
	public void testGetPoolPopNull() {
		try {
			selector.getPool(null, 1);
			fail("Illegal argument exception not thrown for a null pop");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * an empty population.
	 */
	@Test
	public void testGetPoolPopEmpty() {
		try {
			pop.clear();
			selector.getPool(pop, 1);
			fail("Illegal argument exception not thrown for an empty pop");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that pool selection returns a pool of the correct size.
	 */
	@Test
	public void testGetPoolSize() {
		final int poolSize = 10;
		pop.clear();
		for (int i = 0; i < 20; i++) {
			final GPModelDummy dummy = new GPModelDummy();
			dummy.setCacheFitness(false);
			dummy.setFitness(i);
			pop.add(new GPCandidateProgram(null, dummy));
		}

		final List<CandidateProgram> pool = selector.getPool(pop, poolSize);
		assertEquals("pool returned is not equal to pool size parameter", poolSize, pool.size());
	}

	/**
	 * Tests that an exception is thrown when attempting to set a null selection
	 * pool.
	 */
	@Test
	public void testGetProgramPoolNull() {
		selector.setSelectionPool(null);
		try {
			selector.getProgram();
			fail("Illegal state exception not thrown for a null pool");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to set an empty
	 * selection pool.
	 */
	@Test
	public void testGetProgramPoolEmpty() {
		pop.clear();
		selector.setSelectionPool(pop);
		try {
			selector.getProgram();
			fail("Illegal state exception not thrown for an empty pool");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to get a program with
	 * no random number generator set.
	 */
	@Test
	public void testGetProgramRNGNull() {
		model.setRNG(null);
		Life.get().fireConfigureEvent();
		selector.setSelectionPool(pop);
		try {
			selector.getProgram();
			fail("Illegal state exception not thrown for a null random number generator");
		} catch (final IllegalStateException e) {
		}
	}

}
