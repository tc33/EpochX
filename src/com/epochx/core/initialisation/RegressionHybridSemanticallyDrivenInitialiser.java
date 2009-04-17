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
 * @author Lawrence Beadle & Tom Castle
 *
 */
public class RegressionHybridSemanticallyDrivenInitialiser<TYPE> implements Initialiser<TYPE> {

	private GPModel<TYPE> model;
	private RegressionSemanticModule semMod;
	
	public RegressionHybridSemanticallyDrivenInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
		this.semMod = (RegressionSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<TYPE>> generatePopulation() {
        List<RegressionRepresentation> storage = new ArrayList<RegressionRepresentation>();
        FullInitialiser<TYPE> f = new FullInitialiser<TYPE>(model);
        List<CandidateProgram<TYPE>> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram<TYPE> c: firstPass) {
        	RegressionRepresentation b = (RegressionRepresentation) semMod.codeToBehaviour(c);
        	if(!b.isConstant()) {
        		storage.add(b);
        	}
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
            	Node node1 = semMod.behaviourToCode(rep1).getRootNode();
            	Node node2 = semMod.behaviourToCode(rep1).getRootNode();
            	Node newTree = new AddFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram(newTree, model));
            } else if(cFunc==1) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node node1 = semMod.behaviourToCode(rep1).getRootNode();
            	Node node2 = semMod.behaviourToCode(rep1).getRootNode();
            	Node newTree = new SubtractFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram(newTree, model));
            } else if(cFunc==2) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node node1 = semMod.behaviourToCode(rep1).getRootNode();
            	Node node2 = semMod.behaviourToCode(rep1).getRootNode();
            	Node newTree = new MultiplyFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram(newTree, model));
            } else if(cFunc==3) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node node1 = semMod.behaviourToCode(rep1).getRootNode();
            	Node node2 = semMod.behaviourToCode(rep1).getRootNode();
            	Node newTree = new ProtectedDivisionFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram(newTree, model));
            }
            // check unique
            if(!storage.contains(result) && !(result.isConstant())) {
                storage.add(result);
            }
        }
        
        // translate back and add to first generation
        List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>();
        for(RegressionRepresentation toProg: storage) {
            firstGen.add(semMod.behaviourToCode(toProg));
        }
        
        return firstGen;
	}	
}