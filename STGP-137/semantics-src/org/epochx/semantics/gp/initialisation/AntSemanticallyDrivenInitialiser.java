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
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;
import org.epochx.semantics.*;


/**
 * Artificial Ant Semantically Driven Initialisation
 */
public class AntSemanticallyDrivenInitialiser implements Initialiser {

	private GPModel model;
	private AntSemanticModule semMod;
	
	/**
	 * Constructor for semantically driven initialisation for artificial ant
	 * @param model The GP model in use
	 * @param semMod The semantic module on use
	 */
	public AntSemanticallyDrivenInitialiser(GPModel model, SemanticModule semMod) {
		this.model = model;
		this.semMod = (AntSemanticModule) semMod;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		return generatePopulation();
	}
	
	private List<CandidateProgram> generatePopulation() {
		// make a random object
		Random rGen = new Random();
		ArrayList<ArrayList<String>> storage = new ArrayList<ArrayList<String>>();
        // seed the basic representations
        ArrayList<ArrayList<String>> seed = makeAntBaseMoves();
        for(ArrayList<String> s: seed) {
            storage.add(s);
        }
        // clear seed
        seed = null;
        
        ArrayList<String> result;
        String oB = ("{");
        String cB = ("}");
        // ---------------------------- PART SIZE MODIFIER
        int partSize = 10;
        // mash up at root
        while(storage.size()<model.getPopulationSize()) {
            int cFunc = rGen.nextInt(model.getSyntax().size());
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
        List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>();
        int i = 1;
        for(ArrayList<String> toProg: storage) {                
            CandidateProgram holder = semMod.behaviourToCode(new AntRepresentation(toProg));
            firstGen.add(holder);
            //System.out.println(holder);
            //System.out.println("Reverse Translation at: " + i);
            i++;
        }
        // clear the storage
        storage = null;
        
        return firstGen;
	}
	
	/**
     * Constructs the 4 basic manoeuvres in behaviour form for semantic ant initialisation
     * @return An ArrayList containing the 4 basic moves
     */
    public ArrayList<ArrayList<String>> makeAntBaseMoves() {
        // make master
        ArrayList<ArrayList<String>> master = new ArrayList<ArrayList<String>>();
        // make behaviours
        ArrayList<String> beh = new ArrayList<String>();
        // move east
        beh.add("E");
        beh.add("M");
        master.add(beh);
        beh = new ArrayList<String>();
        // move south
        beh.add("S");
        beh.add("M");
        master.add(beh);
        beh = new ArrayList<String>();
        // move west
        beh.add("W");
        beh.add("M");
        master.add(beh);
        beh = new ArrayList<String>();
        // move north
        beh.add("N");
        beh.add("M");
        master.add(beh);
        beh = null;
        return master;        
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
