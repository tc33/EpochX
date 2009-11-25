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

import net.sf.javabdd.BDD;

import org.epochx.core.GPModel;
import org.epochx.op.initialisation.*;
import org.epochx.representation.*;
import org.epochx.semantics.*;

/**
 * Boolean domian hybrid semantically driven initalisation
 */
public class BooleanHybridSemanticallyDrivenInitialiser implements Initialiser<Boolean> {

	private GPModel<Boolean> model;
	private BooleanSemanticModule semMod;
	
	/**
	 * Constructor for Boolean hybrid semantically driven initialisation
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public BooleanHybridSemanticallyDrivenInitialiser(GPModel<Boolean> model, SemanticModule<Boolean> semMod) {
		this.model = model;
		this.semMod = (BooleanSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see org.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<Boolean>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<Boolean>> generatePopulation() {
		// initialise BDD stuff
        semMod.start();
        List<BDD> storage = new ArrayList<BDD>();
        FullInitialiser<Boolean> f = new FullInitialiser<Boolean>(model);
        List<CandidateProgram<Boolean>> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram<Boolean> c: firstPass) {
        	BooleanRepresentation b = semMod.codeToBehaviour(c);
        	if(!b.isConstant()) {
        		storage.add(b.getBDD());
        	}
        }

        // create random number generator
        Random random = new Random();
        int noOfFunctions = model.getFunctions().size();
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
        List<CandidateProgram<Boolean>> firstGen = new ArrayList<CandidateProgram<Boolean>>();
        for(BDD toProg: storage) {
            Node<Boolean> holder = semMod.behaviourToCode(new BooleanRepresentation(toProg));
            firstGen.add(new CandidateProgram<Boolean>(holder, model));
        }
        
        // clear up BDD stuff
        semMod.stop();
        
        return firstGen;
	}	
}