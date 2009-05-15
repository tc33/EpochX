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
import com.epochx.semantics.*;

/**
 * @author lb212
 *
 */
public class AntHybridSemanticallyDrivenInitialiser<TYPE> implements Initialiser<TYPE> {

	private GPModel<TYPE> model;
	private AntSemanticModule semMod;
	
	public AntHybridSemanticallyDrivenInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
		this.semMod = (AntSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram<TYPE>> generatePopulation() {
		// make a random object
		Random rGen = new Random();
		ArrayList<ArrayList<String>> storage = new ArrayList<ArrayList<String>>();
        FullInitialiser<TYPE> f = new FullInitialiser<TYPE>(model);
        List<CandidateProgram<TYPE>> firstPass = f.getInitialPopulation();
        
        // generate a full population to start with
        for(CandidateProgram<TYPE> c: firstPass) {
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
        List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>();
        int i = 1;
        for(ArrayList<String> toProg: storage) {                
            CandidateProgram<TYPE> holder = semMod.behaviourToCode(new AntRepresentation(toProg));
            firstGen.add(holder);
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
