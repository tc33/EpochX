/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.initialisation;

import java.util.*;

import org.epochx.core.GPModel;
import org.epochx.op.initialisation.*;
import org.epochx.representation.*;
import org.epochx.representation.dbl.*;
import org.epochx.semantics.*;


/**
 * Regression hybrid semantically driven initialisation
 */
public class RegressionHybridSemanticallyDrivenInitialiser implements Initialiser<Double> {

	private GPModel<Double> model;
	private RegressionSemanticModule semMod;
	
	/**
	 * Constructor for the regression hybrid semantically driven initialisation
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public RegressionHybridSemanticallyDrivenInitialiser(GPModel<Double> model, SemanticModule<Double> semMod) {
		this.model = model;
		this.semMod = (RegressionSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see org.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<Double>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<Double>> generatePopulation() {
        List<RegressionRepresentation> storage = new ArrayList<RegressionRepresentation>();
        FullInitialiser<Double> f = new FullInitialiser<Double>(model);
        List<CandidateProgram<Double>> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram<Double> c: firstPass) {
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
            	Node<Double> node1 = semMod.behaviourToCode(rep1);
            	Node<Double> node2 = semMod.behaviourToCode(rep1);
            	Node<Double> newTree = new AddFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==1) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = semMod.behaviourToCode(rep1);
            	Node<Double> node2 = semMod.behaviourToCode(rep1);
            	Node<Double> newTree = new SubtractFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==2) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = semMod.behaviourToCode(rep1);
            	Node<Double> node2 = semMod.behaviourToCode(rep1);
            	Node<Double> newTree = new MultiplyFunction(node1, node2);
                result = (RegressionRepresentation) semMod.codeToBehaviour(new CandidateProgram<Double>(newTree, model));
            } else if(cFunc==3) {
            	RegressionRepresentation rep1 = storage.get(random.nextInt(storage.size()));
            	RegressionRepresentation rep2 = storage.get(random.nextInt(storage.size()));
            	Node<Double> node1 = semMod.behaviourToCode(rep1);
            	Node<Double> node2 = semMod.behaviourToCode(rep1);
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
        	Node<Double> cp = semMod.behaviourToCode(toProg);
            firstGen.add(new CandidateProgram<Double>(cp, model));
        }
        
        return firstGen;
	}	
}