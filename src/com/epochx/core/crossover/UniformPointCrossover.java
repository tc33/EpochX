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
package com.epochx.core.crossover;

import java.util.*;

import com.epochx.core.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.parent.*;
import com.epochx.core.selection.poule.*;

/**
 * 
 */
public class UniformPointCrossover implements Crossover {
	
	private PouleSelector poolSelector;
	private ParentSelector parentSelector;
	
	private GPConfig config;
	
	public UniformPointCrossover(GPConfig config, PouleSelector poolSelector, ParentSelector parentSelector) {
		this.config = config;
		this.poolSelector = poolSelector;
		this.parentSelector = parentSelector;
	}
	
	public UniformPointCrossover(GPConfig config, ParentSelector parentSelector) {
		//TODO Don't use null, have a default.
		this(config, null, parentSelector);
	}
	
	public UniformPointCrossover(GPConfig config) {
		//TODO Don't use null, have a default.
		this(config, new RandomParentSelector());
	}
	
	@Override
	public List<CandidateProgram> crossover(List<CandidateProgram> pop) {
		List<CandidateProgram> nextPop = new ArrayList<CandidateProgram>();
		List<CandidateProgram> poule;
		
		// Copy parents to children.
		if (poolSelector != null) {
			poule = poolSelector.getPoule(pop);
		} else {
			poule = pop;
		}
		
		while (nextPop.size() < config.getPopulationSize()) {
			CandidateProgram parent1 = parentSelector.getParent(poule);
			CandidateProgram parent2 = parentSelector.getParent(poule);
			
			CandidateProgram[] children = crossover(parent1, parent2);
			
			for (CandidateProgram c: children) {
				nextPop.add(c);
			}
		}
		
		return nextPop;
	}

	@Override
	public CandidateProgram[] crossover(CandidateProgram parent1, CandidateProgram parent2) {
		CandidateProgram child1 = null;
		CandidateProgram child2 = null;
		try {
			child1 = (CandidateProgram) parent1.clone();
			child2 = (CandidateProgram) parent2.clone();
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// check probability
		if(Math.random() < config.getCrossoverProbability()) {
			// select swap and put points
			int swapPoint1 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(parent1));
			int swapPoint2 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(parent2));

			if (swapPoint1 == 0 || swapPoint2 == 0) {
				System.out.println("0");
			}
			
			// do swap
			// get parts to swap
			try {
				// find Nth node
				// Do we actually need to make a clone of this if its a direct swap?
				Node subTree1 = (Node) child1.getNthNode(swapPoint1).clone();
				Node subTree2 = (Node) child2.getNthNode(swapPoint2).clone();
				// set Nth node
				child1.setNthNode(subTree2, swapPoint1);
				child2.setNthNode(subTree1, swapPoint2);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// max depth reversion section
			int pDepth1 = GPProgramAnalyser.getProgramDepth(child1);
			int pDepth2 = GPProgramAnalyser.getProgramDepth(child2);
			// depth check on child one
			if(pDepth1>config.getMaxDepth()) {
				try {
					child1 = (CandidateProgram) parent1.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// depth check on child two
			if(pDepth2>config.getMaxDepth()) {
				try {
					child2 = (CandidateProgram) parent2.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// TODO state change section

			
		}
		
		return new CandidateProgram[]{child1, child2};
	}
	
	/**
	 * @return the poolSelector
	 */
	public PouleSelector getPoolSelector() {
		return poolSelector;
	}

	/**
	 * @param poolSelector the poolSelector to set
	 */
	public void setPoolSelector(PouleSelector poolSelector) {
		this.poolSelector = poolSelector;
	}

	/**
	 * @return the parentSelector
	 */
	public ParentSelector getParentSelector() {
		return parentSelector;
	}

	/**
	 * @param parentSelector the parentSelector to set
	 */
	public void setParentSelector(ParentSelector parentSelector) {
		this.parentSelector = parentSelector;
	}
}
