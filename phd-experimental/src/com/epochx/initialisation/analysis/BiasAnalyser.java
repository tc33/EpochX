/*  
 *  Copyright 2007-2008 Lawrence Beadle
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

package com.epochx.initialisation.analysis;

import java.io.File;
import java.util.*;
import net.sf.javabdd.*;

import com.epochx.core.initialisation.RampedHalfAndHalfInitialiser;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.example.regression.RegressionModelCUBIC;
import com.epochx.semantics.*;
import com.epochx.util.FileManip;

/**
 * Bias Analyser is used to analyse starting populations by checking 
 * how many programs are associated with a particular behaviour
 */
public class BiasAnalyser {

    /**
     * Runs the bias analyser
     * @param args No input parameters
     */
    public static void main(String[] args) {

        // decide which model
    	RegressionModelCUBIC model = new RegressionModelCUBIC();

        // set up BDD stuff
        SemanticModule semMod = model.getSemanticModule();
        semMod.start();

        // create storage
        ArrayList<BehaviourManager> storage = new ArrayList<BehaviourManager>();
        
        for (int i = 0; i < 100; i++) {

            // create a new pop
        	RampedHalfAndHalfInitialiser rhh = new RampedHalfAndHalfInitialiser(model);
        	model.setPopulationSize(1000);
            List<CandidateProgram> testPop = rhh.getInitialPopulation();

            // cycle through programs and add stuff to HashMap
            for (CandidateProgram prog : testPop) {

                // work out BDD
                Representation progRep = semMod.codeToBehaviour(prog);

                // cycle through storage and find match
                boolean match = false;
                for (BehaviourManager bddMan : storage) {
                    if (bddMan.getBehaviour().equals(progRep)) {
                        match = true;
                        bddMan.addProgram(prog);
                    }
                }
                if (match == false) {
                    BehaviourManager bddMan = new BehaviourManager(progRep);
                    bddMan.addProgram(prog);
                    storage.add(bddMan);
                }
            }
        }

        // print number of programs produced
        System.out.println("BIAS ANALYSER RESULTS");
        System.out.println("Unique Behaviours = " + storage.size());
        
        ArrayList<String> output = new ArrayList<String>();
        
        // print score distribution
        BehaviourManager example = storage.get(0);
        if(example.getBehaviour() instanceof BooleanRepresentation) {
        	output.add("Representation\tFrequency\tNode_Count\tSat_Count" + "\n");
        } else if(example.getBehaviour() instanceof AntRepresentation) {
        	output.add("Representation\tFrequency\tMove_Count\tFinal_Orientation" + "\n");
        } else if(example.getBehaviour() instanceof RegressionRepresentation) {
        	output.add("Representation\tFrequency\tNo_Terms\tIs_Constant?" + "\n");
        }
        for (int i = 0; i < storage.size(); i++) {
            BehaviourManager thisOne = storage.get(i);            
            if(thisOne.getBehaviour() instanceof BooleanRepresentation) {
            	output.add(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + ((BooleanRepresentation) thisOne.getBehaviour()).getBDD().nodeCount() + "\t" + ((BooleanRepresentation) thisOne.getBehaviour()).getBDD().satCount() + "\n");
            } else if(thisOne.getBehaviour() instanceof AntRepresentation) {
            	output.add(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + countMoves(((AntRepresentation) thisOne.getBehaviour()).getAntRepresentation()) + "\t" + getLastO(((AntRepresentation) thisOne.getBehaviour()).getAntRepresentation()) + "\n");
            } else if(thisOne.getBehaviour() instanceof RegressionRepresentation) {
            	output.add(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + ((RegressionRepresentation) thisOne.getBehaviour()).getRegressionRepresentation().size() + "\t" + ((RegressionRepresentation) thisOne.getBehaviour()).isConstant() + "\n");
            }
        }
        
        FileManip.doOutput(new File("Results"), output, "biasoutput-REGRESSION.txt", false);

        // close BDD link
        semMod.stop();
    }
    
    /**
     * Helper method for artificial ant analysis
     * @param toProcess The behavioural representation of the ants moves
     * @return The number of positions covered
     */
    public static int countMoves(ArrayList<String> toProcess) {
        int i = 0;
        for(String p: toProcess) {
            if(p.equalsIgnoreCase("M")) {
                i++;
            }
        }
        return i;
    }
    
    /**
     * Helper method for artificial ant analysis
     * @param toProcess - the behaviour to process
     * @return the final orientation of the behaviour
     */
    public static String getLastO(ArrayList<String> toProcess) {
        for(int i = (toProcess.size()-1); i>=0; i--) {
            if(toProcess.get(i).equalsIgnoreCase("N") || toProcess.get(i).equalsIgnoreCase("S") || toProcess.get(i).equalsIgnoreCase("E") || toProcess.get(i).equalsIgnoreCase("W")) {
                return toProcess.get(i);
            }
        }
        return "E";
    }
}
