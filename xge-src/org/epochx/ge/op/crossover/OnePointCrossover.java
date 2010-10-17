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
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements a one point crossover on two
 * <code>CandidatePrograms</code>.
 * 
 * <p>
 * The operation is performed on the programs' chromosomes. A random codon
 * position is chosen in both parent programs and all the codons from that point
 * onwards are exchanged.
 */
public class OnePointCrossover implements GECrossover, ConfigListener {

	/**
	 * Requests an <code>Integer</code> which is the point chosen in the first
	 * parent's chromosomes for the one point crossover operation.
	 */
	public static final Stat XO_POINT1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests an <code>Integer</code> which is the point chosen in the second
	 * parent's chromosomes for the one point crossover operation.
	 */
	public static final Stat XO_POINT2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>List&lt;Integer&gt;</code> which is the portion of the
	 * first parent program which is being exchanged into the second parent.
	 */
	public static final Stat XO_CODONS1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>List&lt;Integer&gt;</code> which is the portion of the
	 * second parent program which is being exchanged into the first parent.
	 */
	public static final Stat XO_CODONS2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	// The controlling model.
	private final GEModel model;

	// The random number generator in use.
	private RandomNumberGenerator rng;

	public OnePointCrossover(final GEModel model) {
		this.model = model;

		// Configure parameters from the model.
		Life.get().addConfigListener(this, false);
	}

	/*
	 * Configure component with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = model.getRNG();
	}

	/**
	 * Performs a one point crossover operation on the specified parent
	 * programs.
	 * 
	 * <p>
	 * The operation is performed on the programs' chromosomes. A random codon
	 * position is chosen in <b>both</b> parent programs and all the codons from
	 * that point onwards are exchanged.
	 * 
	 * @param parent1 {@inheritDoc}
	 * @param parent2 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public GECandidateProgram[] crossover(final CandidateProgram p1,
			final CandidateProgram p2) {
		final GECandidateProgram parent1 = (GECandidateProgram) p1;
		final GECandidateProgram parent2 = (GECandidateProgram) p2;

		// Choose crossover points.
		int crossoverPoint1 = rng.nextInt(parent1.getNoCodons());
		int crossoverPoint2 = rng.nextInt(parent2.getNoCodons());

		// Add crossover points to the stats manager.
		Stats.get().addData(XO_POINT1, crossoverPoint1);
		Stats.get().addData(XO_POINT2, crossoverPoint2);
		
		// Make copies of the parents.
		final GECandidateProgram child1 = (GECandidateProgram) parent1.clone();
		final GECandidateProgram child2 = (GECandidateProgram) parent2.clone();

		final List<Integer> part1 = child1.removeCodons(crossoverPoint1,
				child1.getNoCodons());
		final List<Integer> part2 = child2.removeCodons(crossoverPoint2,
				child2.getNoCodons());
		
		// Add codon portions into the stats manager.
		Stats.get().addData(XO_CODONS1, part1);
		Stats.get().addData(XO_CODONS2, part2);

		// Swap over the endings at the crossover points.
		child2.appendCodons(part1);
		child1.appendCodons(part2);

		return new GECandidateProgram[]{child1, child2};
	}

}
