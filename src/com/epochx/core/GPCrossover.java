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

import com.epochx.core.crossover.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.parent.*;
import com.epochx.core.selection.poule.*;

/**
 * 
 */
public class GPCrossover {

	private PouleSelector pouleSelector;
	private ParentSelector parentSelector;
	private Crossover crossover;
	
	private GPConfig config;
	
	public GPCrossover(GPConfig config) {
		this.config = config;
		
		this.pouleSelector = config.getPouleSelector();
		this.parentSelector = config.getParentSelector();
		this.crossover = config.getCrossover();
	}
	
	public List<CandidateProgram> crossover(List<CandidateProgram> pop) {
		List<CandidateProgram> nextPop = new ArrayList<CandidateProgram>();
		List<CandidateProgram> poule;
		
		if (pouleSelector != null) {
			poule = pouleSelector.getPoule(pop);
		} else {
			poule = pop;
		}
		
		while (nextPop.size() < config.getPopulationSize()) {
			CandidateProgram parent1 = parentSelector.getParent(poule);
			CandidateProgram parent2 = parentSelector.getParent(poule);
			
			CandidateProgram[] children = crossover.crossover(parent1, parent2);
			
			for (CandidateProgram c: children) {
				nextPop.add(c);
			}
		}
		
		return nextPop;
	}
}
