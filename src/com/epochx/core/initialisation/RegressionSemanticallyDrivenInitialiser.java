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
package com.epochx.core.initialisation;

import java.util.*;

import com.epochx.core.GPModel;
import com.epochx.core.representation.*;
import com.epochx.func.dbl.*;
import com.epochx.semantics.*;

/**
 * Regression semantically driven initialisation
 */
public class RegressionSemanticallyDrivenInitialiser implements Initialiser<Double> {

	private GPModel<Double> model;
	private RegressionSemanticModule semMod;
	
	/**
	 * Constructor for regression based semantically driven initialisation
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public RegressionSemanticallyDrivenInitialiser(GPModel<Double> model, SemanticModule<Double> semMod) {
		this.model = model;
		this.semMod = (RegressionSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<Double>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<Double>> generatePopulation() {
        List<RegressionRepresentation> storage = new ArrayList<RegressionRepresentation>();
        
        // load terminals only
        for(TerminalNode<Double> t: model.getTerminals()) {
        	CandidateProgram<Double> c = new CandidateProgram<Double>(t, model);
            RegressionRepresentation rep = (RegressionRepresentation) semMod.codeToBehaviour(c);
            storage.add(rep);
        }
        
        // create random number generator
        Random random = new Random();
        int noOfFunctions = model.getFunctions().size();
        // mash together rest to make full pop
        while(storage.size()<model.getPopulationSize()) {
            int cFunc = random.nextInt(noOfFunctions);
            RegressionRepresentation result = null;
            if(cFunc==0) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = (Node<Double>) semMod.behaviourToCode(rep1).getRootNode().clone();
            	Node<Double> node2 = (Node<Double>) semMod.behaviourToCode(rep2).getRootNode().clone();
            	Node<Double> newTree = new AddFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==1) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = (Node<Double>) semMod.behaviourToCode(rep1).getRootNode().clone();
            	Node<Double> node2 = (Node<Double>) semMod.behaviourToCode(rep2).getRootNode().clone();
            	Node<Double> newTree = new SubtractFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==2) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = (Node<Double>) semMod.behaviourToCode(rep1).getRootNode().clone();
            	Node<Double> node2 = (Node<Double>) semMod.behaviourToCode(rep2).getRootNode().clone();
            	Node<Double> newTree = new MultiplyFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==3) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = (Node<Double>) semMod.behaviourToCode(rep1).getRootNode().clone();
            	Node<Double> node2 = (Node<Double>) semMod.behaviourToCode(rep2).getRootNode().clone();
            	Node<Double> newTree = new ProtectedDivisionFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            }
            // check unique
            if(!storage.contains(result) && !(result.isConstant())) {
                storage.add(result);
            }
        }
        
        // translate back and add to first generation
        List<CandidateProgram<Double>> firstGen = new ArrayList<CandidateProgram<Double>>();
        for(RegressionRepresentation toProg: storage) {
        	CandidateProgram<Double> cp = semMod.behaviourToCode(toProg);
            firstGen.add(cp);
        }
        
        return firstGen;
	}	
}