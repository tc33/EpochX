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
package org.epochx.gp.model.example;

import org.epochx.core.Model;
import org.epochx.gp.model.EvenParity;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gp.op.mutation.PointMutation;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;

import static org.epochx.stats.StatField.*;

/**
 * 
 */
public class Even3Parity extends EvenParity {

	/**
	 * 
	 */
	public Even3Parity() {
		super(3);

		setPopulationSize(4000);
		setCrossoverProbability(1.0);
		setMutationProbability(0.0);
		setProgramSelector(new FitnessProportionateSelector(this));
		setCrossover(new KozaCrossover(this));
		setMutation(new PointMutation(this));
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
		setPoolSize(-1);
		setPoolSelector(null);
		setNoGenerations(50);
		setNoElites(50);
		setMaxDepth(17);
		setMaxInitialDepth(6);
		setNoRuns(1000);
	}

	public static void main(final String[] args) {
		final Model m = new Even3Parity();

		
		Life.get().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Stats.get().print(RUN_NUMBER, GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITNESS_AVE);
			}
		});
		 

		m.run();
	}

}
