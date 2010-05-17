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
package org.epochx.ge.op.crossover;

import java.util.List;

import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 * This class implements a one point crossover on two 
 * <code>CandidatePrograms</code>.
 * 
 * <p>The operation is performed on the programs' chromosomes. A random codon 
 * position is chosen in both parent programs and all the codons from that 
 * point onwards are exchanged.
 */
public class OnePointCrossover implements GECrossover {
	/*
	 * TODO This seems ridiculous. Crossing over like this will completely 
	 * remove all context so how can it crossover building blocks!?
	 */
	
	// The controlling model.
	private GEModel model;
	
	// Operator statistics store.
	private int crossoverPoint1;
	private int crossoverPoint2;
	
	// The random number generator in use.
	private RandomNumberGenerator rng;
	
	public OnePointCrossover(GEModel model) {
		this.model = model;
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		rng = model.getRNG();
	}
	
	/**
	 * Performs a one point crossover operation on the specified parent 
	 * programs.
	 * 
	 * <p>The operation is performed on the programs' chromosomes. A random 
	 * codon position is chosen in <b>both</b> parent programs and all the 
	 * codons from that point onwards are exchanged.
	 * 
	 * @param parent1 {@inheritDoc}
	 * @param parent2 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public GECandidateProgram[] crossover(CandidateProgram p1,
										CandidateProgram p2) {
		GECandidateProgram parent1 = (GECandidateProgram) p1;
		GECandidateProgram parent2 = (GECandidateProgram) p2;
		
		// Choose crossover points.
		crossoverPoint1 = rng.nextInt(parent1.getNoCodons());
		crossoverPoint2 = rng.nextInt(parent2.getNoCodons());
		
		// Make copies of the parents.
		GECandidateProgram child1 = (GECandidateProgram) parent1.clone();
		GECandidateProgram child2 = (GECandidateProgram) parent2.clone();
		
		List<Integer> part1 = child1.removeCodons(crossoverPoint1, child1.getNoCodons());
		List<Integer> part2 = child2.removeCodons(crossoverPoint2, child2.getNoCodons());
		
		// Swap over the endings at the crossover points.
		child2.appendCodons(part1);
		child1.appendCodons(part2);

		return new GECandidateProgram[]{child1, child2};
	}

}
