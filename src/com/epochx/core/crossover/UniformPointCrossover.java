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

import java.util.ArrayList;
import java.util.List;

import com.epochx.core.representation.*;
import core.SemanticModule;

/**
 * 
 */
public class UniformPointCrossover extends Crossover {
	
	public UniformPointCrossover(ArrayList<CandidateProgram> population, 
			double pCrossover, SemanticModule semMod, int maxDepth,
			boolean stateChecker, int populationSize, int elites) {
		super(population, pCrossover, semMod, maxDepth, stateChecker, populationSize, elites);
	}
	
	public void doCrossover() {
		// copy parents to children
		List<CandidateProgram> poule = super.getPoulePrograms();
		Node parent1 = poule.get((int) Math.floor(Math.random()*super.getPouleSize())).getRootNode();
		Node parent2 = poule.get((int) Math.floor(Math.random()*super.getPouleSize())).getRootNode();
		Node child1 = null;
		Node child2 = null;
		try {
			child1 = (Node) parent1.clone();
			child2 = (Node) parent2.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// check probability
		if(Math.random()<super.getProbOfCrossover()) {

			// select swap and put points
			int swapPoint1 = (int) Math.floor(Math.random()*super.getGPAnalyser().getProgramLength(parent1));
			int swapPoint2 = (int) Math.floor(Math.random()*super.getGPAnalyser().getProgramLength(parent2));

			// do swap
			// get parts to swap
			try {
				// find Nth node
				Node subTree1 = (Node) child1.getNthNode(child1, swapPoint1).clone();
				Node subTree2 = (Node) child2.getNthNode(child2, swapPoint2).clone();
				// set Nth node
				child1.setNthNode(child1, subTree2, swapPoint1);
				child2.setNthNode(child2, subTree1, swapPoint2);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		

			// max depth reversion section
			int pDepth1 = super.getGPAnalyser().getProgramDepth(child1);
			int pDepth2 = super.getGPAnalyser().getProgramDepth(child2);
			// depth check on one
			if(pDepth1>super.getMaxDepth()) {
				try {
					child1 = (Node) parent1.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// depth check on two
			if(pDepth2>super.getMaxDepth()) {
				try {
					child2 = (Node) parent2.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// TODO state change section


		}
		
		// set children
		super.addProgToPop(new CandidateProgram(child1));
		super.addProgToPop(new CandidateProgram(child2));
		
	}
}
