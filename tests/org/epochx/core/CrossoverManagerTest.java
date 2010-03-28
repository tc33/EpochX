/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.core;

import junit.framework.TestCase;

import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class CrossoverManagerTest extends TestCase {

	private Model model;
	private CrossoverManager crossoverManager;
	
	@Override
	protected void setUp() throws Exception {
		model = new Model() {
			@Override
			public double getFitness(CandidateProgram program) {
				return 0;
			}
		};
		crossoverManager = new CrossoverManager(model);
	}
	
	/**
	 * Tests that an exception is thrown if the crossover is null but crossover
	 * probability is not null.
	 */
	public void testCrossoverNotSet() {
		model.setCrossover(null);
		model.setCrossoverProbability(0.1);
		
		try {
			crossoverManager.crossover();
			fail("illegal state exception not thrown for a model with crossover enabled but null operator");
		} catch(IllegalStateException e) {}
	}
	
}
