package org.epochx.op.selection;

import java.util.*;

import org.epochx.gp.model.*;
import org.epochx.gp.representation.*;
import org.epochx.representation.*;

import junit.framework.*;

public class FitnessProportionateSelectorTest extends TestCase {
	
	private FitnessProportionateSelector selector;
	private GPModelDummy model;
	private List<CandidateProgram> pop;
	
	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();
		selector = new FitnessProportionateSelector(model);
		model.getLifeCycleManager().fireConfigureEvent();
		
		pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(model));
	}
	
	/**
	 * Tests that an exception is thrown when attempting to select a pool of 
	 * size 0.
	 */
	public void testGetPoolSizeZero() {
		try {
			selector.getPool(pop, 0);
			fail("Illegal argument exception not thrown for a pool size of 0");
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * a null population.
	 */
	public void testGetPoolPopNull() {
		try {
			selector.getPool(null, 1);
			fail("Illegal argument exception not thrown for a null pop");
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * an empty population.
	 */
	public void testGetPoolPopEmpty() {
		try {
			pop.clear();
			selector.getPool(pop, 1);
			fail("Illegal argument exception not thrown for an empty pop");
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests that pool selection returns a pool of the correct size.
	 */
	public void testGetPoolSize() {
		int poolSize = 10;
		pop.clear();
		for (int i=0; i<20; i++) {
			GPModelDummy dummy = new GPModelDummy();
			dummy.setCacheFitness(false);
			dummy.setFitness(i);
			pop.add(new GPCandidateProgram(null, dummy));
		}
		
		List<CandidateProgram> pool = selector.getPool(pop, poolSize);
		assertEquals("pool returned is not equal to pool size parameter", poolSize, pool.size());
	}
	
	/**
	 * Tests that an exception is thrown when attempting to set a null selection
	 * pool.
	 */
	public void testSetSelectionPoolNull() {
		try {
			selector.setSelectionPool(null);
			fail("Illegal argument exception not thrown for a null pool");
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests that an exception is thrown when attempting to set an empty 
	 * selection pool.
	 */
	public void testSetSelectionPoolEmpty() {
		try {
			pop.clear();
			selector.setSelectionPool(pop);
			fail("Illegal argument exception not thrown for an empty pool");
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests that an exception is thrown when attempting to get a program with 
	 * no random number generator set.
	 */
	public void testGetProgramRNGNull() {
		model.setRNG(null);
		model.setFitness(1.0);
		model.setCacheFitness(false);
		model.getLifeCycleManager().fireConfigureEvent();
		selector.setSelectionPool(pop);
		try {
			selector.getProgram();
			fail("Illegal state exception not thrown for a null random number generator");
		} catch (IllegalStateException e) {}
	}
}
