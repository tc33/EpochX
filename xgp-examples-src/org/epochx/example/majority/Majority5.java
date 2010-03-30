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
package org.epochx.example.majority;

import org.epochx.gp.op.crossover.UniformPointCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.op.selection.*;


/**
 * 
 */
public class Majority5 extends org.epochx.gp.model.majority.Majority5 {

	public Majority5() {
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setMutationProbability(0.0);
		setNoRuns(100);
		setPoolSize(50);
		setNoElites(50);
		setInitialMaxDepth(6);
		setMaxProgramDepth(17);
		setPoolSelector(new TournamentSelector(this, 7));
		setProgramSelector(new RandomSelector(this));
		setCrossover(new UniformPointCrossover(this));
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
	}

	public static void main(String[] args) {
		new Majority5().run();
	}
}
