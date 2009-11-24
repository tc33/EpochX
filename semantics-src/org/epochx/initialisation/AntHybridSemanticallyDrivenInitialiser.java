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

import org.epochx.action.Action;
import org.epochx.core.GPModel;
import org.epochx.op.initialisation.*;
import org.epochx.representation.*;
import org.epochx.semantics.*;


/**
 * Artificial Ant hybrid semantically driven initialisation
 */
public class AntHybridSemanticallyDrivenInitialiser implements Initialiser<Action> {

	private GPModel<Action> model;
	private AntSemanticModule semMod;
	
	/**
	 * Construcor for AA hybrid SDI method
	 * @param model The GP model in use
	 * @param semMod The relevant semantic module
	 */
	public AntHybridSemanticallyDrivenInitialiser(GPModel<Action> model, SemanticModule<Action> semMod) {
		this.model = model;
		this.semMod = (AntSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see org.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<Action>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<Action>> generatePopulation() {
		// make a random object
		Random rGen = new Random();
		ArrayList<ArrayList<String>> storage = new ArrayList<ArrayList<String>>();
        FullInitialiser<Action> f = new FullInitialiser<Action>(model);
        List<CandidateProgram<Action>> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram<Action> c: firstPass) {
        	AntRepresentation b = (AntRepresentation) semMod.codeToBehaviour(c);
        	if(!b.isConstant()) {
        		storage.add(b.getAntRepresentation());
        	}
        }
        
        ArrayList<String> result;
        String oB = ("{");
        String cB = ("}");
        // ---------------------------- PART SIZE MODIFIER
        int partSize = 10;
        // mash up at root
        while(storage.size()<model.getPopulationSize()) {
            int cFunc = rGen.nextInt(model.getFunctions().size());
            result = new ArrayList<String>();
            if(cFunc==0) {
                // IF-FOOD-AHEAD - add brackets and just stick two paths together
                ArrayList<String> part1 = storage.get(rGen.nextInt(storage.size()));
                ArrayList<String> part2 = storage.get(rGen.nextInt(storage.size()));
                while(getMoves(part1)> partSize) {
                    part1 = storage.get(rGen.nextInt(storage.size()));
                }
                while(getMoves(part2)> partSize) {
                    part2 = storage.get(rGen.nextInt(storage.size()));
                }
                result.add(oB);                 
                for(String p: part1) {
                    result.add(p);
                }
                result.add(cB);
                result.add(oB);
                for(String p: part2) {
                    result.add(p);
                }
                result.add(cB);
            } else if(cFunc==1) {
                // PROGN2 - get two paths and transpose the position of the second
                // path using the last position of the end path
                ArrayList<String> p1 = storage.get(rGen.nextInt(storage.size()));
                ArrayList<String> p2 = storage.get(rGen.nextInt(storage.size()));
                while(getMoves(p1)> partSize) {
                    p1 = storage.get(rGen.nextInt(storage.size()));
                }
                while(getMoves(p2)> partSize) {
                    p2 = storage.get(rGen.nextInt(storage.size()));
                }
                result = semMod.joinPaths(p1, p2, "E");
            } else if (cFunc==2) {
                // PROGN3
                ArrayList<String> p1 = storage.get(rGen.nextInt(storage.size()));
                ArrayList<String> p2 = storage.get(rGen.nextInt(storage.size()));
                while(getMoves(p1)> partSize) {
                    p1 = storage.get(rGen.nextInt(storage.size()));
                }
                while(getMoves(p2)> partSize) {
                    p2 = storage.get(rGen.nextInt(storage.size()));
                }
                result = semMod.joinPaths(p1, p2, "E");
                ArrayList<String> p3 = storage.get(rGen.nextInt(storage.size()));
                while(getMoves(p3)> partSize) {
                    p3 = storage.get(rGen.nextInt(storage.size()));
                }
                result = semMod.joinPaths(result, p3, "E");
            }
            
            // CYCLE THROUGH AND REDUCE DUPLICATES
            result = semMod.condenseAntRep(result);

            // check unique
            if (!storage.contains(result) && result.size() > 2) {
                storage.add(result);
            }
        }
        
        // backwards translate
        List<CandidateProgram<Action>> firstGen = new ArrayList<CandidateProgram<Action>>();
        int i = 1;
        for(ArrayList<String> toProg: storage) {                
            Node<Action> holder = semMod.behaviourToCode(new AntRepresentation(toProg));
            firstGen.add(new CandidateProgram<Action>(holder, model));
            //System.out.println(holder);
            //System.out.println("Reverse Translation at: " + i);
            i++;
        }
        // clear the storage
        storage = null;
        
        return firstGen;
	}
    
    private int getMoves(ArrayList<String> part) {
        int count = 0;
        for(String p: part) {
            if(p.equalsIgnoreCase("M")) {
                count++;
            }
        }
        return count;
    }
}
