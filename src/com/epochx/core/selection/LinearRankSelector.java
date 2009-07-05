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

import com.epochx.core.representation.*;

/**
 * 
 */
public class LinearRankSelector<TYPE> implements ParentSelector<TYPE>, PoolSelector<TYPE> {
	
	private List<CandidateProgram<TYPE>> pop;
	
	private double probabilities[];
	private double nPlus;
	private double nMinus;
	
	private double gradient;
	
	public LinearRankSelector(double gradient) {
		this.gradient = gradient;
		
		nMinus = 2/(gradient+1);
		nPlus = (2*gradient)/(gradient+1);
	}
	
	@Override
	public void onGenerationStart(List<CandidateProgram<TYPE>> pop) {
		Collections.sort(pop);
		this.pop = pop;
		
		int popSize = pop.size();
		probabilities = new double[popSize];
		double total = 0;
		for (int i=1; i<=popSize; i++) {
			double N = (double) popSize;
			
			double p = (1/N) * (nMinus + ((nPlus - nMinus) * ((i-1)/(N-1))));
			total += p;
			probabilities[i-1] = total;
		}
	}

	@Override
	public CandidateProgram<TYPE> getParent() {
		double ran = Math.random();
		
		for (int i=0; i<probabilities.length; i++) {
			if (ran < probabilities[i]) {
				return pop.get(i);
			}
		}
		
		// This shouldn't ever happen assuming the probabilities add up to 1 and Math.ran is never >1.
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.selection.PouleSelector#getPoule(java.util.List, int)
	 */
	@Override
	public List<CandidateProgram<TYPE>> getPool(
			List<CandidateProgram<TYPE>> pop, int pouleSize) {
		
		ParentSelector<TYPE> parentSelector = new LinearRankSelector<TYPE>(gradient);
		parentSelector.onGenerationStart(pop);
		List<CandidateProgram<TYPE>> poule = new ArrayList<CandidateProgram<TYPE>>();
		
		for (int i=0; i<pouleSize; i++) {
			poule.add(parentSelector.getParent());
		}
		
		return poule;
	}
}
