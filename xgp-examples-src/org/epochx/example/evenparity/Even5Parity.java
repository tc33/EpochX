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
package org.epochx.example.evenparity;

import static org.epochx.gp.stats.GPStatField.*;

import org.epochx.gp.op.crossover.UniformPointCrossover;
import org.epochx.life.GenerationAdapter;
import org.epochx.op.selection.*;


/**
 * 
 */
public class Even5Parity extends org.epochx.gp.model.parity.Even5Parity {

	public Even5Parity() {
		super();
		
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setMutationProbability(0.1);
		setNoRuns(5);
		setNoElites(50);
		setMaxProgramDepth(17);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(null);
		setCrossover(new UniformPointCrossover(this));
		
		getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				getStatsManager().printGenerationStats(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITNESS_AVE, GEN_DEPTH_AVE, GEN_DEPTH_MAX);
			}
		});
	}
	
	public static void main(String[] args) {
		(new Even5Parity()).run();
	}
	
}
