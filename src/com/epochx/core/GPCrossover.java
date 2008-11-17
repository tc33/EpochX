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

import org.apache.log4j.*;

import com.epochx.core.crossover.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;

/**
 * 
 */
public class GPCrossover<TYPE> {

	static Logger logger = Logger.getLogger(GPCrossover.class);
	
	private ParentSelector<TYPE> parentSelector;
	private Crossover<TYPE> crossover;
	
	public GPCrossover(GPModel<TYPE> model) {
		logger.debug("Setting up GPCrossover");
		
		this.parentSelector = model.getParentSelector();
		this.crossover = model.getCrossover();
	}
	
	public CandidateProgram<TYPE>[] crossover(List<CandidateProgram<TYPE>> pop) {		
		CandidateProgram<TYPE> parent1 = parentSelector.getParent(pop);
		CandidateProgram<TYPE> parent2 = parentSelector.getParent(pop);
		
		return crossover.crossover(parent1, parent2);
	}
}
