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

import org.epochx.core.*;
import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.*;
import org.epochx.gp.op.mutation.*;
import org.epochx.gp.stats.*;
import org.epochx.life.*;
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
	
	public static void main(String[] args) {
		final Model m = new SexticRegression();
		
		/*m.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				m.getStatsManager().printGenerationStats(GPStatField.GEN_NUMBER, GPStatField.GEN_FITNESS_MIN, GPStatField.GEN_FITTEST_PROGRAM);
			}
		});*/
		m.getLifeCycleManager().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				m.getStatsManager().printRunStats(GPStatField.RUN_NUMBER, GPStatField.RUN_FITNESS_MIN);
			}
		});
		
		m.run();
	}
}
