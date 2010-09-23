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
import org.epochx.gx.op.crossover.*;
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
		model.setInitialiser(new ExperimentalInitialiser(model));
		model.setMutation(new ExperimentalMutation(model));
		model.setCrossover(new ExperimentalCrossover(model));
		model.setNoElites(1);
		model.setCrossoverProbability(0.8);
		model.setMutationProbability(0.2);
		model.setTerminationFitness(0.0);
		model.setMaxNoStatements(6);
		model.setMinNoStatements(4);
		model.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
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
				int maxNoStatements = Integer.MIN_VALUE;
				int minNoStatements = Integer.MAX_VALUE;
				double totalSize = 0;
				for (CandidateProgram p: pop) {
					int noStatements = ((GXCandidateProgram) p).getNoStatements();
					totalSize += noStatements;
					if (noStatements > maxNoStatements) {
						maxNoStatements = noStatements;
					}
					if (noStatements < minNoStatements) {
						minNoStatements = noStatements;
					}
				}
				double aveSize = totalSize / pop.size();
				System.out.print(minNoStatements + "\t" + maxNoStatements + "\t" + aveSize + "\n");
			}
		});
		
		model.getLifeCycleManager().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				Object[] stats = model.getStatsManager().getRunStats(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN, StatField.RUN_FITTEST_PROGRAM);
			
				System.out.println(stats[0] + "\t" + stats[1]);
				
				BufferedWriter bw = null;

			    try {
			      	bw = new BufferedWriter(new FileWriter("results/best-programs.txt", true));
			      	bw.write("\n"+stats[0]+" |***************************| "+stats[1]+"\n");
			        bw.write(ProgramGenerator.format(stats[2].toString()));
			         
			        bw.newLine();
			        bw.flush();
			    } catch (IOException ioe) {
			    	ioe.printStackTrace();
			    } finally {                       // always close the file
			    	if (bw != null) 
			    		try {
			    			bw.close();
			    		} catch (IOException ioe2) {
			    			// just ignore it
			    		}
			    } // end try/catch/finally

			}
		});
		
		model.run();
	}    
}
