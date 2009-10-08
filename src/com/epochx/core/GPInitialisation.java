/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.core;

import java.util.*;

import com.epochx.representation.*;

/**
 * This class is responsible for controlling the initialisation operation. Note 
 * that as with the other core classes, no actual (initialisation) operation is 
 * performed by this class. Rather, it provides the infrastructure around the 
 * operation, and calls the implementation of <code>Initialiser</code> which 
 * will perform the actual operation.
 * 
 * @see Initialiser
 * @see FullInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class GPInitialisation<TYPE> {

	// The controlling model.
	private GPModel<TYPE> model;
	
	// The number of times the initialisation was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of GEInitialisation which will setup the 
	 * initialisation operation. Note that the actual initialisation operation 
	 * will be performed by the subclass of <code>Initialiser</code> returned 
	 * by the models <code>getInitialiser()</code> method.
	 * 
	 * @param model the GPModel which defines the Initialiser operator and 
	 * 				life cycle listener.
	 * @see Initialiser
	 */
	public GPInitialisation(GPModel<TYPE> model) {
		this.model = model;
	}
	
	/**
	 * Initialises a new population of <code>CandidatePrograms</code> by 
	 * calling <code>getInitialPopulation()</code> on the initialiser provided 
	 * by the model.
	 * 
	 * <p>After an initial population is constructed, the model's life cycle 
	 * listener is given an opportunity to confirm or modify it before 
	 * proceeding. The listener's <code>onInitialisation()</code> method is 
	 * called, passing it the newly formed population. If this method returns
	 * null then the initialisation operation will be repeated, otherwise the 
	 * population returned by the life cycle listener will be used as the 
	 * initial population. The number of times the initial population is 
	 * rejected and thus regenerated is available with a call to 
	 * <code>getReversions()</code>.
	 * 
	 * @return a List of CandidatePrograms generated by the model's initialiser.
	 */
	public List<CandidateProgram<TYPE>> initialise() {
		List<CandidateProgram<TYPE>> pop = null;
		
		reversions = -1;
		do {
			// Perform initialisation.
			pop = model.getInitialiser().getInitialPopulation();
			
			// Allow life cycle listener to confirm or modify.
			pop = model.getLifeCycleListener().onInitialisation(pop);
			
			// Increment reversions - starts at -1 to cover first increment.
			reversions++;
		} while(pop == null);
		
		return pop;
	}
	
	/**
	 * Number of times the initial population was rejected and regenerated.
	 * 
	 * <p>After an initial population is constructed, the model's life cycle 
	 * listener is given an opportunity to confirm or modify it before 
	 * proceeding. The listener's <code>onInitialisation()</code> method is 
	 * called, passing it the newly formed population. If this method returns
	 * null then the initialisation operation will be repeated, otherwise the 
	 * population returned by the life cycle listener will be used as the 
	 * initial population. The number of times the initial population is 
	 * rejected and thus regenerated is available with a call to this method.
	 * 
	 * @return the number of times the intialisation was rejected by the life 
	 * cycle listener.
	 */
	public int getReversions() {
		return reversions;
	}
	
}
