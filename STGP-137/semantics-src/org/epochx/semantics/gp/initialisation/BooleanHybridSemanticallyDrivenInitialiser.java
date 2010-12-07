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

import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.init.FullInitialiser;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;
import org.epochx.semantics.BooleanRepresentation;
import org.epochx.semantics.BooleanSemanticModule;
import org.epochx.semantics.SemanticModule;

import net.sf.javabdd.*;

/**
 * Boolean domain hybrid semantically driven initialisation
 */
public class BooleanHybridSemanticallyDrivenInitialiser implements Initialiser {

	private GPModel model;
	private BooleanSemanticModule semMod;
	
	/**
	 * Constructor for Boolean hybrid semantically driven initialisation
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public BooleanHybridSemanticallyDrivenInitialiser(GPModel model, SemanticModule semMod) {
		this.model = model;
		this.semMod = (BooleanSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram> generatePopulation() {
		// initialise BDD stuff
        semMod.start();
        List<BDD> storage = new ArrayList<BDD>();
        FullInitialiser f = new FullInitialiser(model);
        List<CandidateProgram> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram c: firstPass) {
        	BooleanRepresentation b = semMod.codeToBehaviour((GPCandidateProgram) c);
        	if(!b.isConstant()) {
        		storage.add(b.getBDD());
        	}
        }

        // create random number generator
        Random random = new Random();
        int noOfFunctions = model.getSyntax().size();
        // mash together rest to make full pop
        while(storage.size()<model.getPopulationSize()) {
            int cFunc = random.nextInt(noOfFunctions);
            BDD result = null;
            if(cFunc==0) {
                result = storage.get(random.nextInt(storage.size())).ite(storage.get(random.nextInt(storage.size())), storage.get(random.nextInt(storage.size())));
            } else if(cFunc==1) {
                result = storage.get(random.nextInt(storage.size())).and(storage.get(random.nextInt(storage.size())));
            } else if(cFunc==2) {
                result = storage.get(random.nextInt(storage.size())).or(storage.get(random.nextInt(storage.size())));
            } else if(cFunc==3) {
                result = storage.get(random.nextInt(storage.size())).not();
            }
            // check unique
            if(!storage.contains(result) && !(result.nodeCount()<1)) {
                storage.add(result);
            }
        }
        
        // translate back and add to first gen
        List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>();
        for(BDD toProg: storage) {
            firstGen.add(semMod.behaviourToCode(new BooleanRepresentation(toProg)));
        }
        
        // clear up BDD stuff
        semMod.stop();
        
        return firstGen;
	}	
}