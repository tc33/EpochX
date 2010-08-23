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
package org.epochx.gx.model.example;

import org.epochx.gx.model.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.op.mutation.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.stats.*;


public class EvenParity extends org.epochx.gx.model.EvenParity {
	
	public EvenParity(int noInputBits) {
		super(noInputBits);
	}
	
    public static void main(String[] args) {
		final GXModel model = new EvenParity(3);
		model.setNoRuns(100);
		model.setNoGenerations(50);
		model.setPopulationSize(500);
		model.setInitialiser(new ExperimentalInitialiser(model));
		model.setMutation(new ExperimentalMutation(model));
		model.setCrossoverProbability(0.0);
		model.setMutationProbability(1.0);
		model.setTerminationFitness(0.01);
		model.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				model.getStatsManager().printGenerationStats(StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
			}
		});
		
		model.getLifeCycleManager().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				System.out.println("-----------------------");
				model.getStatsManager().printRunStats(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
				GXCandidateProgram best = (GXCandidateProgram) model.getStatsManager().getRunStat(StatField.RUN_FITTEST_PROGRAM);
				System.out.println(ProgramGenerator.format(best.toString()));
				System.out.println("-----------------------");
			}
		});
		model.run();
	}
    
}
