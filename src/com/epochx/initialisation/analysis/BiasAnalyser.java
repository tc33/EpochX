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

import java.util.*;
import net.sf.javabdd.*;
import java.lang.reflect.*;

import com.epochx.core.initialisation.RampedHalfAndHalfInitialiser;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.example.artificialant.ArtificialAntSantaFe;
import com.epochx.semantics.AntRepresentation;
import com.epochx.semantics.Representation;
import com.epochx.semantics.SemanticModule;

/**
 * Bias Analyser is used to analyse starting populations
 * @author Lawrence Beadle
 */
public class BiasAnalyser {

    /**
     * Runs the bias analyser
     * @param args No input parameters
     */
    public static void main(String[] args) {

        // decide which model
        ArtificialAntSantaFe model = new ArtificialAntSantaFe();

        // set up BDD stuff
        SemanticModule semMod = model.getSemanticModule();
        semMod.start();

        // create storage
        ArrayList<BehaviourManager> storage = new ArrayList<BehaviourManager>();

        for (int i = 0; i < 100; i++) {

            // create a new pop
        	RampedHalfAndHalfInitialiser rhh = new RampedHalfAndHalfInitialiser(model);
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
        
        // TODO
        // USE instanceof to sort out specific output sections----------------------------------------------

        // print score distribution
        System.out.println("Representation\tFrequency\tNode_Count\tSat_Count");
        //System.out.println("Representation\tFrequency\tMove_Count\tFinal_Orientation");
        for (int i = 0; i < storage.size(); i++) {
            BehaviourManager thisOne = storage.get(i);
            System.out.println(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + countMoves(((AntRepresentation) thisOne.getBehaviour()).getAntRepresentation()) + "\t" + getLastO(((AntRepresentation) thisOne.getBehaviour()).getAntRepresentation()));
            //System.out.println(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + thisOne.getBehaviour().getBDD().nodeCount() + "\t" + thisOne.getBehaviour().getBDD().satCount());
        
//        if(thisOne.getFrequency()>1) {
//        ArrayList<ArrayList<String>> progs = thisOne.getPrograms();
//        for(ArrayList<String> part: progs) {
//        System.out.println(part);
//        }
//        thisOne.getBDD().printDot();
//        System.out.println(gPE.bddToBooleanCode(thisOne.getBDD()));
//        System.out.println("---------");
//        }
        }

        // close BDD thingy
        semMod.stop();
    }
    
    /**
     * Helper method for artifical ant analysis
     * @param toProcess The behavioural representaiton of the ants moves
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
    
    public static String getLastO(ArrayList<String> toProcess) {
        for(int i = (toProcess.size()-1); i>=0; i--) {
            if(toProcess.get(i).equalsIgnoreCase("N") || toProcess.get(i).equalsIgnoreCase("S") || toProcess.get(i).equalsIgnoreCase("E") || toProcess.get(i).equalsIgnoreCase("W")) {
                return toProcess.get(i);
            }
        }
        return "E";
    }
}
