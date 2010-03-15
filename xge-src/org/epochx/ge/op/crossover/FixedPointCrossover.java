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

import org.epochx.core.Controller;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.LifeCycleManager;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 * This class implements a fixed point crossover on two 
 * <code>CandidatePrograms</code>.
 * 
 * <p>The operation is performed on the programs' chromosomes in a similar 
 * manner to OnePointCrossover. One random codon position is chosen which is 
 * within the length bounds of both parent programs. Then all codons from that 
 * point onwards in both programs are exchanged.
 * 
 * <p>Fixed point crossover results in two child programs with chromosomes of 
 * equal size to the two parents passed in, thus fixed point crossover prevents
 * chromosome length expanding over a run. However, chromosome length may 
 * still change as a result of other operations in the algorithm such as the 
 * extension property if used during mapping.
 * 
 * @see OnePointCrossover
 */
public class FixedPointCrossover implements GECrossover {

	// Operator statistics store.
	private int crossoverPoint;
	
	// The random number generator in use.
	private RandomNumberGenerator rng;
	
	/**
	 * Constructs an instance of FixedPointCrossover.
	 * 
	 * @param model the current controlling model.
	 */
	public FixedPointCrossover() {
		// Initialise parameters.
		updateModel();
		
		// Re-initialise parameters on each generation.
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				updateModel();
			}
		});
	}
	
	/*
	 * Initialises FixedPointCrossover, in particular all parameters from the 
	 * model should be refreshed incase they've changed since the last call.
	 */
	private void updateModel() {
		rng = Controller.getModel().getRNG();
	}
	
	/**
	 * Performs a fixed point crossover operation on the specified parent 
	 * programs.
	 * 
	 * <p>The operation is performed on the programs' chromosomes in a similar 
	 * manner to OnePointCrossover. One random codon position is chosen which is 
	 * within the length bounds of both parent programs. Then all codons from that 
	 * point onwards in both programs are exchanged.
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
		
		// Pick a point in the shortest parent chromosome.
		crossoverPoint = 0;
		int parent1Codons = parent1.getNoCodons();
		int parent2Codons = parent2.getNoCodons();
		if (parent1Codons < parent2Codons) {
			crossoverPoint = rng.nextInt(parent1Codons);
		} else {
			crossoverPoint = rng.nextInt(parent2Codons);
		}
		
		// Make copies of the parents.
		GECandidateProgram child1 = (GECandidateProgram) parent1.clone();
		GECandidateProgram child2 = (GECandidateProgram) parent2.clone();
		
		List<Integer> part1 = child1.removeCodons(crossoverPoint, child1.getNoCodons());
		List<Integer> part2 = child2.removeCodons(crossoverPoint, child2.getNoCodons());
		
		// Swap over the endings at the crossover points.
		child2.appendCodons(part1);
		child1.appendCodons(part2);

		return new GECandidateProgram[]{child1, child2};
	}
}