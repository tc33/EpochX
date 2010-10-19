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
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gp.op.mutation.PointMutation;
import org.epochx.op.selection.*;

/**
 * 
 */
public class SexticRegression extends org.epochx.gp.model.SexticRegression {

	public SexticRegression() {
		setPopulationSize(1024);
		setCrossoverProbability(0.8);
		setMutationProbability(0.1);
		setPoolSelector(new TournamentSelector(this, 7));
		setProgramSelector(new LinearRankSelector(this, 0.5));
		setCrossover(new KozaCrossover(this));
		setMutation(new PointMutation(this));
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
		setPoolSize(-1);
		setNoGenerations(50);
		setNoElites(50);
		setMaxDepth(17);
		setNoRuns(50);
	}

	public static void main(final String[] args) {
		final Model m = new SexticRegression();


		m.run();
	}
}
