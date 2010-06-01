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
package org.epochx.gr.model.epox.example;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.FullInitialiser;
import org.epochx.gr.stats.GRStatField;
import org.epochx.life.*;


public class EvenParity extends org.epochx.gr.model.epox.EvenParity {
	
	public EvenParity(int noInputBits) {
		super(noInputBits);
	}
	
    public static void main(String[] args) {
		final GRModel model = new EvenParity(3);
		model.setNoRuns(50);
		model.setNoGenerations(100);
		model.setMaxDepth(14);
		model.setMaxInitialDepth(14);
		model.setInitialiser(new FullInitialiser(model));
		model.setTerminationFitness(0.01);
		model.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {			
			@Override
			public void onGenerationEnd() {
				model.getStatsManager().printGenerationStats(GRStatField.GEN_NUMBER, GRStatField.GEN_FITNESS_MIN, GRStatField.GEN_FITNESS_AVE, GRStatField.GEN_DEPTH_AVE, GRStatField.GEN_DEPTH_MAX, GRStatField.GEN_FITTEST_PROGRAM);
			}
		});
		
		model.getLifeCycleManager().addRunListener(new RunAdapter() {			
			@Override
			public void onRunEnd() {
				model.getStatsManager().printRunStats(GRStatField.RUN_NUMBER, GRStatField.RUN_FITNESS_MIN, GRStatField.RUN_FITTEST_PROGRAM);
			}
		});
		model.run();
	}
    
}
