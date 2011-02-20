/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.gp.model;

import org.epochx.core.Evolver;
import org.epochx.ge.model.epox.QuarticRegression;
import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gp.op.mutation.SubtreeMutation;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

/**
 * 
 */
public class AntTrailTest extends ModelTest {
	
	private void setupModel(final AntTrail model) {
		Evolver evolver = getEvolver();
		
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);

		model.setCrossover(new KozaCrossover(evolver));

		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(evolver, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(evolver));
		model.setNoElites(0);

		model.setTerminationFitness(0.0);
		
		//setupRunPrinting(evolver.getStats(model));
		//setupGenPrinting(evolver.getStats(model));
	}

	/**
	 * Tests Santa Fe trail with standard setup.
	 * 
	 * Koza's success rate: 16% (p202).
	 * OR: 14%/50%/46% with full/grow/RH+H (p599).
	 * 
	 * Expecting success rate between 12% and 20%.
	 */
	@Test
	public void testSantaFeTrail() {
		final int LOWER_SUCCESS = 12;
		final int UPPER_SUCCESS = 20;

		final SantaFeTrail model = new SantaFeTrail(getEvolver(), 600);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Santa Fe trail", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
