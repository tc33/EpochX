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

import java.io.*;
import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.op.mutation.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.stats.*;


public class Fibonnaci extends org.epochx.gx.model.Fibonacci {
	
    public static void main(String[] args) {
		final GXModel model = new Fibonnaci();
		model.setNoRuns(100);
		model.setNoGenerations(1000);
		model.setPopulationSize(1000);
		model.setInitialiser(new ExperimentalInitialiser(model, 3));
		model.setMutation(new ExperimentalMutation(model));
		model.setNoElites(10);
		model.setCrossoverProbability(0.0);
		model.setMutationProbability(1.0);
		model.setTerminationFitness(0.0);
		model.setMaxNoStatements(14);
		model.setMinNoStatements(3);
		/*model.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Object[] stats = model.getStatsManager().getGenerationStats(StatField.GEN_NUMBER, 
						   StatField.GEN_FITNESS_MIN, 
						   StatField.GEN_FITNESS_AVE,
						   StatField.GEN_POPULATION);
				
				for (int i=0; i<3; i++) {
					System.out.print(stats[i]);
					System.out.print('\t');
				}
				
				List<CandidateProgram> pop = (List<CandidateProgram>) stats[3];
				double totalSize = 0;
				for (CandidateProgram p: pop) {
					totalSize += ((GXCandidateProgram) p).getNoStatements();
				}
				double aveSize = totalSize / pop.size();
				System.out.print(aveSize + "\n");
			}
		});*/
		
		model.getLifeCycleManager().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				model.getStatsManager().printRunStats(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
			}
		});
		
		model.run();
	}    
}
