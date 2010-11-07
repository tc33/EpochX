/*  
 *  Copyright 2007-2010 Lawrence Beadle & Tom Castle
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

package org.epochx.semantics.gp.initialisation;

import java.util.*;

import org.epochx.epox.DoubleNode;
import org.epochx.epox.dbl.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.init.FullInitialiser;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;
import org.epochx.semantics.*;


/**
 * Regression hybrid semantically driven initialisation
 */
public class RegressionHybridSemanticallyDrivenInitialiser implements Initialiser {

	private GPModel model;
	private RegressionSemanticModule semMod;
	
	/**
	 * Constructor for the regression hybrid semantically driven initialisation
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public RegressionHybridSemanticallyDrivenInitialiser(GPModel model, SemanticModule semMod) {
		this.model = model;
		this.semMod = (RegressionSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram> generatePopulation() {
        List<RegressionRepresentation> storage = new ArrayList<RegressionRepresentation>();
        FullInitialiser f = new FullInitialiser(model);
        List<CandidateProgram> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram c: firstPass) {
        	RegressionRepresentation b = (RegressionRepresentation) semMod.codeToBehaviour((GPCandidateProgram) c);
        	if(!b.isConstant()) {
        		storage.add(b);
        	}
        }

        // create random number generator
        Random random = new Random();
        int noOfFunctions = model.getSyntax().size();
        // mash together rest to make full pop
        while(storage.size()<model.getPopulationSize()) {
            int cFunc = random.nextInt(noOfFunctions);
            RegressionRepresentation result = null;
            if(cFunc==0) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	DoubleNode node1 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep1)).getRootNode();
            	DoubleNode node2 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep2)).getRootNode();
            	DoubleNode newTree = new AddFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new GPCandidateProgram(newTree, model));
            } else if(cFunc==1) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	DoubleNode node1 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep1)).getRootNode();
            	DoubleNode node2 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep2)).getRootNode();
            	DoubleNode newTree = new SubtractFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new GPCandidateProgram(newTree, model));
            } else if(cFunc==2) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	DoubleNode node1 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep1)).getRootNode();
            	DoubleNode node2 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep2)).getRootNode();
            	DoubleNode newTree = new MultiplyFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new GPCandidateProgram(newTree, model));
            } else if(cFunc==3) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	DoubleNode node1 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep1)).getRootNode();
            	DoubleNode node2 = (DoubleNode) ((GPCandidateProgram) semMod.behaviourToCode(rep2)).getRootNode();
            	DoubleNode newTree = new ProtectedDivisionFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new GPCandidateProgram(newTree, model));
            }
            // check unique
            if(!storage.contains(result) && !(result.isConstant())) {
                storage.add(result);
            }
        }
        
        // translate back and add to first generation
        List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>();
        for(RegressionRepresentation toProg: storage) {
        	CandidateProgram cp = semMod.behaviourToCode(toProg);
            firstGen.add(cp);
        }
        
        return firstGen;
	}	
}