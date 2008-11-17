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

package com.epochx.startingpopanalysis;

import core.*;
import java.util.*;
import net.sf.javabdd.*;
import java.lang.reflect.*;

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
        String modelName = "ArtificialAnt";

        // set up Genetic Program instance - USING REFLECTION
        GPRunBasic genProg = null;
        try {
            genProg = (GPRunBasic) Class.forName("Models." + modelName).getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (ClassNotFoundException e) {
            System.out.println("CLASS NOT FOUND");
        } catch (NoSuchMethodException f) {
            System.out.println("NO SUCH METHOD");
        } catch (InstantiationException z) {
            System.out.println("COULD NOT INSTANTIATE CLASS");
        } catch (IllegalAccessException h) {
            System.out.println("ILLEGAL ACCESS EXCEPTION");
        } catch (InvocationTargetException k) {
            System.out.println("INVOCATION EXCEPTION");
        } catch (ClassCastException l) {
            System.out.println(l.toString());
        }

        if (genProg == null) {
            System.out.println("REFLECT FAILED TO LOAD FILE: " + modelName);
        }

        // set up BDD stuff
        SemanticModule semMod = genProg.getSemanticModel();
        semMod.start();

        // create storage
        ArrayList<BehaviourManager> storage = new ArrayList<BehaviourManager>();

        for (int i = 0; i < 100; i++) {

            // create a new pop
            ArrayList<ArrayList<String>> testPop = genProg.buildFirstPop(1000, "RH+H");

            // cycle through programs and add stuff to HashMap
            for (ArrayList<String> prog : testPop) {

                // work out BDD
                BehaviourRepresentation progRep = semMod.createRep(prog);

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

        // print score distribution
        System.out.println("Representation\tFrequency\tNode_Count\tSat_Count");
        //System.out.println("Representation\tFrequency\tMove_Count\tFinal_Orientation");
        for (int i = 0; i < storage.size(); i++) {
            BehaviourManager thisOne = storage.get(i);
            System.out.println(i + "\t" + ((double) thisOne.getFrequency() / 100) + "\t" + countMoves(thisOne.getBehaviour().getArrayList()) + "\t" + getLastO(thisOne.getBehaviour().getArrayList()));
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
        semMod.finish();
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
