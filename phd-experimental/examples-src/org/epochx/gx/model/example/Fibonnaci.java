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

import org.epochx.gx.model.*;
import org.epochx.gx.op.crossover.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.op.mutation.*;
import org.epochx.gx.stats.GXStatField;
import org.epochx.life.*;
import org.epochx.stats.*;


public class Fibonnaci extends org.epochx.gx.model.Fibonacci {
	
    public static void main(String[] args) {
    	final double crossoverProbability = 0.5;//Double.valueOf(args[0]);
    	final double mutationProbability = 1.0 - crossoverProbability;
    	
    	final String outputPath = (args.length > 1) ? args[1] : "results/";
    	
		final GXModel model = new Fibonnaci();
		model.setNoRuns(100);
		model.setNoGenerations(1000);
		model.setPopulationSize(1000);
		model.setInitialiser(new ExperimentalInitialiser(model));
		model.setMutation(new ExperimentalMutation(model));
		model.setCrossover(new ExperimentalCrossover(model));
		model.setNoElites(1);
		model.setCrossoverProbability(crossoverProbability);
		model.setMutationProbability(mutationProbability);
		model.setTerminationFitness(0.0);
		model.setMaxNoStatements(6);
		model.setMinNoStatements(4);
		
//		Life.get().addGenerationListener(new GenerationAdapter() {
//			@Override
//			public void onGenerationEnd() {
//				Stats.get().print(StatField.GEN_NUMBER, 
//						   StatField.GEN_FITNESS_MIN, 
//						   StatField.GEN_FITNESS_AVE,
//						   GXStatField.GEN_NO_STATEMENTS_MIN,
//						   GXStatField.GEN_NO_STATEMENTS_MAX,
//						   GXStatField.GEN_NO_STATEMENTS_AVE);
//			}
//		});
		
		try {
			final FileOutputStream fileout = new FileOutputStream(new File(outputPath+"/results-x"+crossoverProbability+".txt"));
			
			Life.get().addRunListener(new RunAdapter() {
				@Override
				public void onRunEnd() {
					Stats.get().printToStream(fileout, StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		model.run();
	}
}
