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
package com.epochx.core.selection;

import java.util.*;

import com.epochx.core.*;
import com.epochx.core.representation.*;

/**
 * 
 */
public class TournamentSelector implements ParentSelector, PouleSelector {

	private int tournamentSize;
	private GPModel model;
	
	public TournamentSelector(int tournamentSize, GPModel model) {
		this.tournamentSize = tournamentSize;
		this.model = model;
	}

	@Override
	public CandidateProgram getParent(List<CandidateProgram> pop) {
		// Use a random selector to construct a tournament.
		RandomParentSelector randomSelector = new RandomParentSelector();
		CandidateProgram[] tournament = new CandidateProgram[tournamentSize];
		
		for (int i=0; i<tournamentSize; i++) {
			tournament[i] = randomSelector.getParent(pop);
		}
		
		// Calculate fitness.
		double bestFitness = Double.POSITIVE_INFINITY;
		CandidateProgram bestProgram = null;
		for (CandidateProgram p: tournament) {
			double fitness = model.getFitness(p);
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestProgram = p;
			}
		}
				
		return bestProgram;
	}


	@Override
	public List<CandidateProgram> getPoule(List<CandidateProgram> pop, int pouleSize) {
		// If pouleSize is 0 or less then we use the whole population.
		if (pouleSize <= 0) {
			return pop;
		}
		
		List<CandidateProgram> poule = new ArrayList<CandidateProgram>(pouleSize);
		
		for (int i=0; i<pouleSize; i++) {
			poule.add(getParent(pop));
		}
		
		return poule;
	}
}
