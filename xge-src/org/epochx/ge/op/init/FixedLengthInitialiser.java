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
package org.epochx.ge.op.init;

import java.util.*;

import org.epochx.core.Controller;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;


/**
 * Initialisation implementation that randomly generates a chromosome up to a 
 * specified length.
 */
public class FixedLengthInitialiser implements GEInitialiser {
	/*
	 * TODO Implement a similar initialiser that uses variable lengths up to maximum.
	 */
	
	private GEModel model;
	
	private int popSize;
	
	private int initialLength;
	
	/**
	 * Constructs a RandomInitialiser.
	 * 
	 * @param model The GE model that will provide any required control 
	 * 				parameters such as the desired population size.
	 * @param initialLength The initial length that chromosomes should be 
	 * 			  			generated to.
	 */
	public FixedLengthInitialiser(int initialLength) {
		this.initialLength = initialLength;
		
		// Configure parameters from the model.
		LifeCycleManager.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		model = (GEModel) Controller.getModel();
		
		popSize = model.getPopulationSize();
	}
	
	/**
	 * Generate a population of new CandidatePrograms constructed by randomly 
	 * generating their chromosomes. The size of the population will be equal 
	 * to the result of calling getPopulationSize() on the controlling model. 
	 * All programs in the population will be unique. Each candidate program 
	 * will have a chromosome length equal to the initialLength provided to the 
	 * constructor.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the 
	 * initial population for a GE run.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		//TODO No check for program uniqueness is currently made.
		
		// Initialise population of candidate programs.
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Build population.
		int i=0;
		while (i<popSize) {
			GECandidateProgram candidate = new GECandidateProgram(model);
			
			// Initialise the program.
			candidate.appendNewCodons(initialLength);
            //if (candidate.isValid() && !firstGen.contains(candidate)) {
            	firstGen.add(candidate);
            	i++;
            //}
		}
		
		// Return starting population.
		return firstGen;
	}

}
