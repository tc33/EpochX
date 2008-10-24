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

package core;

import java.util.*;
import net.sf.javabdd.*;

/**
 * This class provides functionality to egnerate boolean starting populations
 * @author Lawrence Beadle
 */
public class GenPop {
    
    // poulation size
    private int pop, ran, j, k, progSize, maxDepth, depthCount;
    // create random object for number generator
    private Random rGen = new Random();
    // base syntax arraylist
    private ArrayList<ArrayList<String>> syntax = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> terminals = new ArrayList<ArrayList<String>>();
    private ArrayList<String> program = new ArrayList<String>();
    private ArrayList<ArrayList<String>> firstGen = new ArrayList<ArrayList<String>>();
    private ArrayList<String> part = new ArrayList<String>();
    private int noFuncs = 0;
    private SemanticModule semMod;
    
    /**
     * Creates a new instance of GenPop
     * @param syntaxM An ArrayList<ArrayList<String>> representing the syntax basic structure to be used in 
     * population generation
     * @param terminalsM An ArrayList<ArrayList<String>> representing all the terminals to be used in the
     * new population
     * @param semModM A SemanticModule Object to be used in the SDA creation method
     * @param popSize the size of the starting population required
     */
    public GenPop(int popSize, ArrayList<ArrayList<String>> syntaxM, ArrayList<ArrayList<String>> terminalsM, SemanticModule semModM) {
        pop = popSize;
        syntax = syntaxM;
        terminals = terminalsM;
        noFuncs = syntax.size()-terminals.size();
        semMod = semModM;        
    }
    
    /**
     * Creates the population
     * @param genType A String representing the type of starting population to be generated:
     * RH+H = ramped half and half
     * H+H = half and half
     * Full = full
     * Grow = grow
     * Random = random
     * SDA = state differential
     */
    public void createPop(String genType) {
        
        //set up depth
        maxDepth = 6;
        
        // create right number of starting programs
        if(genType.equalsIgnoreCase("Random")) {
            for(int i = 0; i<pop; i++) {                               
                firstGen.add(makeRandom());                
            }
        }
        
        // if gen type = full
        if(genType.equalsIgnoreCase("Full")) {            
            for(int i=0; i<pop; i++) {
                ArrayList<String> candidate = makeFull();
                while(firstGen.contains(candidate)) {
                    candidate = makeFull();
                }
                firstGen.add(candidate);
            }
        } 
        
        // if gen type = grow
        if(genType.equalsIgnoreCase("Grow")) {
            for(int i=0; i<pop; i++) {
                ArrayList<String> candidate = makeGrow();
                while(firstGen.contains(candidate)) {
                    candidate = makeGrow();
                }
                firstGen.add(candidate);
            }
        }
        
        // if gen type = half and half
        if(genType.equalsIgnoreCase("H+H")) {
            for(int i=0; i<pop/2; i++) {                
                ArrayList<String> candidate = makeGrow();
                while(firstGen.contains(candidate)) {
                    candidate = makeGrow();
                }
                firstGen.add(candidate);
                candidate = makeFull();
                while(firstGen.contains(candidate)) {
                    candidate = makeFull();
                }
                firstGen.add(candidate);
            }
        }
        
        // if gen type = ramped half and half
        if(genType.equalsIgnoreCase("RH+H")) {
            int marker = Math.round(pop/10);
            for(int i=0; i<pop/2; i++) {
                if(i<marker) {
                    maxDepth = 2;
                } else if(i<(marker*2)) {
                    maxDepth = 3;
                } else if(i<(marker*3)) {
                    maxDepth = 4;
                } else if(i<(marker*4)) {
                    maxDepth = 5;
                } else {
                    maxDepth = 6;
                }
                ArrayList<String> candidate = makeGrow();
                while(firstGen.contains(candidate)) {
                    candidate = makeGrow();
                }
                firstGen.add(candidate);
                candidate = makeFull();
                while(firstGen.contains(candidate)) {
                    candidate = makeFull();
                }
                firstGen.add(candidate);
            }
        }
        
        // state differential boolean
        if(genType.equalsIgnoreCase("SDIB")) {
            // initialise BDD stuff
            semMod.start();
            ArrayList<BDD> storage = new ArrayList<BDD>();
            
            // load terminals only
            for(ArrayList<String> t: terminals) {
                BDD rep = semMod.createRep(t).getBDD();
                storage.add(rep);
            }

            // mash together rest to make full pop
            while(storage.size()<pop) {
                int cFunc = rGen.nextInt(noFuncs);
                BDD result = null;
                if(cFunc==0) {
                    result = storage.get(rGen.nextInt(storage.size())).ite(storage.get(rGen.nextInt(storage.size())), storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==1) {
                    result = storage.get(rGen.nextInt(storage.size())).and(storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==2) {
                    result = storage.get(rGen.nextInt(storage.size())).or(storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==3) {
                    result = storage.get(rGen.nextInt(storage.size())).not();
                }
                // check unique
                if(!storage.contains(result) && !(result.nodeCount()<1)) {
                    storage.add(result);
                }
                // -------------------------------------------------------------
            }
            
            // translate back and add to first gen
            int i = 1;
            for(BDD toProg: storage) {
                firstGen.add(semMod.repToCode(new BehaviourRepresentation(toProg)));
                //System.out.println("Reverse Translation at: " + i);
                i++;
            }
            
            // clear up BDD stuff
            semMod.finish();
        }
        
        // HYBRID SDI Boolean
        if(genType.equalsIgnoreCase("HSDIB")) {
            
            // set max depth
            maxDepth = 4;
            
            // initialise BDD stuff
            semMod.start();
            ArrayList<BDD> storage = new ArrayList<BDD>();
            
            // load terminals only
            for(int i = 0; i<pop; i++) {
                BDD rep = semMod.createRep(this.makeFull()).getBDD();
                if(!storage.contains(rep) && !(rep.nodeCount()<1)) {
                    storage.add(rep);
                }
            }

            // mash together rest to make full pop
            while(storage.size()<pop) {
                int cFunc = rGen.nextInt(noFuncs);
                BDD result = null;
                if(cFunc==0) {
                    result = storage.get(rGen.nextInt(storage.size())).ite(storage.get(rGen.nextInt(storage.size())), storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==1) {
                    result = storage.get(rGen.nextInt(storage.size())).and(storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==2) {
                    result = storage.get(rGen.nextInt(storage.size())).or(storage.get(rGen.nextInt(storage.size())));
                } else if(cFunc==3) {
                    result = storage.get(rGen.nextInt(storage.size())).not();
                }
                // check unique
                if(!storage.contains(result) && !(result.nodeCount()<1)) {
                    storage.add(result);
                }
                // -------------------------------------------------------------
            }
            
            // translate back and add to first gen
            int i = 1;
            for(BDD toProg: storage) {
                firstGen.add(semMod.repToCode(new BehaviourRepresentation(toProg)));
                //System.out.println("Reverse Translation at: " + i);
                i++;
            }
            
            // clear up BDD stuff
            semMod.finish();
        }
        
        // state differential for the artificial ant
        if(genType.equalsIgnoreCase("SDIA")) {
            // create representation storage
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
            while(storage.size()<pop) {
                int cFunc = rGen.nextInt(noFuncs);
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
                    result = joinPaths(p1, p2, "E");
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
                    result = joinPaths(p1, p2, "E");
                    ArrayList<String> p3 = storage.get(rGen.nextInt(storage.size()));
                    while(getMoves(p3)> partSize) {
                        p3 = storage.get(rGen.nextInt(storage.size()));
                    }
                    result = joinPaths(result, p3, "E");
                }
                
                // CYCLE THROUGH AND REDUCE DUPLICATES
                result = condenseAntRep(result);

                // check unique
                if (!storage.contains(result) && result.size() > 2) {
                    storage.add(result);
                }
            }
            
            // backwards translate
            int i = 1;
            for(ArrayList<String> toProg: storage) {                
                ArrayList<String> holder = semMod.repToCode(new BehaviourRepresentation(toProg));
                firstGen.add(holder);
                //System.out.println("Reverse Translation at: " + i);
                i++;
            }
            // clear the storage
            storage = null;
            // call garbage collection to free up memory
            System.gc();
        }
        
        // HYBRID SDI for the artificial ant
        if(genType.equalsIgnoreCase("HSDIA")) {
            // set max depth
            maxDepth = 4;
            // create representation storage
            ArrayList<BehaviourRepresentation> storage = new ArrayList<BehaviourRepresentation>();
            // seed the basic representations
            ArrayList<String> candidate = new ArrayList<String>();
            BehaviourRepresentation candidateRep = null;
            for(int i = 0; i<pop; i++) {
                candidate = this.makeFull();
                candidateRep = semMod.createRep(candidate);
                if(!storage.contains(candidateRep) && !candidateRep.isTautology()) {
                    storage.add(candidateRep);
                }
            }
            
            ArrayList<String> result;
            String oB = ("{");
            String cB = ("}");
            // ---------------------------- PART SIZE MODIFIER
            int partSize = 10;
            // mash up at root
            while(storage.size()<pop) {
                int cFunc = rGen.nextInt(noFuncs);
                result = new ArrayList<String>();
                if(cFunc==0) {
                    // IF-FOOD-AHEAD - add brackets and just stick two paths together
                    ArrayList<String> part1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    ArrayList<String> part2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    while(getMoves(part1)> partSize) {
                        part1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    while(getMoves(part2)> partSize) {
                        part2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
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
                    ArrayList<String> p1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    ArrayList<String> p2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    while(getMoves(p1)> partSize) {
                        p1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    while(getMoves(p2)> partSize) {
                        p2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    result = joinPaths(p1, p2, "E");
                } else if (cFunc==2) {
                    // PROGN3
                    ArrayList<String> p1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    ArrayList<String> p2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    while(getMoves(p1)> partSize) {
                        p1 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    while(getMoves(p2)> partSize) {
                        p2 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    result = joinPaths(p1, p2, "E");
                    ArrayList<String> p3 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    while(getMoves(p3)> partSize) {
                        p3 = storage.get(rGen.nextInt(storage.size())).getArrayList();
                    }
                    result = joinPaths(result, p3, "E");
                }
                
                // -------------------------------------------------------------

                // cycle through removing duplicate subsets
                result = condenseAntRep(result);

                // check unique
                if (!storage.contains(result) && !(this.getMoves(result)<1)) {
                    storage.add(new BehaviourRepresentation(result));
                }
            }
            
            // backwards translate
            //int i = 1;
            for(BehaviourRepresentation toProg: storage) {
                ArrayList<String> holder = semMod.repToCode(toProg);
                firstGen.add(holder);
                //System.out.println(holder);
                //System.out.println("Reverse Translation at: " + i);
                //i++;
            }
            // clear the storage
            storage = null;
            // call garbage collection to free up memory
            System.gc();
        }
        
        // washed code section -------------------------------------------------
        // if gen type = full
        if(genType.equalsIgnoreCase("UNWASHED")) {
            semMod.start();
            maxDepth = 4;
            while(firstGen.size()<pop) {
                ArrayList<String> candidate = makeFull();
                BehaviourRepresentation rep = semMod.createRep(candidate);
                if(!(rep.isTautology() || firstGen.contains(candidate))) {
                    firstGen.add(candidate);
                }
            }
            semMod.finish();
        }
        
        if(genType.equalsIgnoreCase("WASHED")) {
            semMod.start();
            maxDepth = 4;
            while(firstGen.size()<pop) {
                ArrayList<String> candidate = makeFull();
                BehaviourRepresentation rep = semMod.createRep(candidate);
                if(!(rep.isTautology() || firstGen.contains(candidate))) {
                    firstGen.add(candidate);
                }
            }
            for(int i = 0; i<firstGen.size(); i++) {
                ArrayList<String> prog = firstGen.get(i);
                firstGen.set(i, semMod.repToCode(semMod.createRep(prog)));
            }
            semMod.finish();
        }        
        // end of washed code --------------------------------------------------
    }
    
    /**
     * Returns the first generation of programs
     * @return The first generation of programs
     */
    public ArrayList<ArrayList<String>> getFirstGen() {
        return firstGen;
    }
    
    /**
     * Combines syntax to form trees of depth 0-4 to be used in the mutation function
     * @return A random program
     */
    public ArrayList<String> makeRandom() {

        // set max depth
        maxDepth = 4;

        // make full program
        program = new ArrayList<String>();

        // fill basic program structure
        ran = rGen.nextInt(syntax.size());
        part = syntax.get(ran);
        for (String p : part) {
            program.add(p);
        }

        // first function in
        depthCount = 1;

        // pass through replacing all the *'s with more functions + terminals
        progSize = program.size();
        while (depthCount <= maxDepth) {

            int zSize = program.size();
            for (int z = 0; z < zSize; z++) {
                if (program.get(z).equalsIgnoreCase("*")) {
                    program.set(z, "!");
                }
            }

            j = 0;
            while (j < progSize) {
                // fill it up with functions
                if (program.get(j).equalsIgnoreCase("!")) {
                    if (depthCount < maxDepth) {
                        ran = rGen.nextInt(syntax.size());
                        part = syntax.get(ran);
                    } else {
                        ran = rGen.nextInt(terminals.size());
                        part = terminals.get(ran);
                    }
                    k = j;
                    program.remove(k);
                    for (String p : part) {
                        program.add(k, p);
                        k++;
                    }
                }
                j++;
                progSize = program.size();
            }
            depthCount++;
        }
        return program;
    }
    
    /**
     * Creates a program which is filled up to a maximum depth of 6
     * @return A program filled up to maximum depth with functions and terminals
     */
    public ArrayList<String> makeFull() {
        // make full program
        program = new ArrayList<String>();
        
        // fill basic program structure
        ran = rGen.nextInt(noFuncs);
        part = syntax.get(ran);
        for(String p: part) {
            program.add(p);
        }
        
        // first function in
        depthCount = 1;
        
        // pass through replacing all the *'s with more functions + terminals
        progSize = program.size();
        while(depthCount<=maxDepth) {
            
            int zSize = program.size();
            for(int z=0; z<zSize; z++) {
                if(program.get(z).equalsIgnoreCase("*")) {
                    program.set(z, "!");
                }
            }
            
            j = 0;
            while(j<progSize) {
                if(depthCount<maxDepth) {
                    // fill it up with functions
                    if(program.get(j).equalsIgnoreCase("!")) {
                        ran = rGen.nextInt(noFuncs);
                        part = syntax.get(ran);
                        k = j;
                        program.remove(k);
                        for(String p: part) {
                            program.add(k, p);
                            k++;
                        }
                    }
                } else {
                    // fill it with terminals
                    if(program.get(j).equalsIgnoreCase("!")) {
                        ran = rGen.nextInt(terminals.size());
                        part = terminals.get(ran);
                        k = j;
                        program.remove(k);
                        for(String p: part) {
                            program.add(k, p);
                            k++;
                        }
                    }
                }
                j++;
                progSize = program.size();
            }
            depthCount++;
        }
        
        return program;
    }
    
    /**
     * Creates a random program which is restricted by a depth value of 6
     * @return A random program which has a maximum depth
     */
    public ArrayList<String> makeGrow() {
        // make grow program
        program = new ArrayList<String>();
        
        // fill basic program structure
        ran = rGen.nextInt(noFuncs);
        part = syntax.get(ran);
        for(String p: part) {
            program.add(p);
        }
        
        // first function in
        depthCount = 1;
        
        // pass through replacing all the *'s with more functions + terminals
        progSize = program.size();
        while(depthCount<=maxDepth) {
            
            int zSize = program.size();
            for(int z=0; z<zSize; z++) {
                if(program.get(z).equalsIgnoreCase("*")) {
                    program.set(z, "!");
                }
            }
            
            j = 0;
            while(j<progSize) {
                if(depthCount<maxDepth) {
                    // fill it up with functions & terminals
                    if(program.get(j).equalsIgnoreCase("!")) {
                        ran = rGen.nextInt(syntax.size());
                        part = syntax.get(ran);
                        k = j;
                        program.remove(k);
                        for(String p: part) {
                            program.add(k, p);
                            k++;
                        }
                    }
                } else {
                    // fill it with terminals
                    if(program.get(j).equalsIgnoreCase("!")) {
                        ran = rGen.nextInt(terminals.size());
                        part = terminals.get(ran);
                        k = j;
                        program.remove(k);
                        for(String p: part) {
                            program.add(k, p);
                            k++;
                        }
                    }
                }
                j++;
                progSize = program.size();
            }
            depthCount++;
        }
        
        return program;
    }
    
    /**
     * Constructs the 4 basic manouvers in behaviour form for semantic ant initialisation
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
    
    /**
     * Helper method for Semantic Artificial Ant Initialisation
     * @param path1 The first path
     * @param path2 The second path
     * @param p2SO The inital direction of the second path if not E
     * @return The combined path with with the second path positions updated relative to the 1st path
     */
    public static ArrayList<String> joinPaths(ArrayList<String> path1, ArrayList<String> path2, String p2SO) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> part1 = new ArrayList<String>();
        ArrayList<String> part2 = new ArrayList<String>();
        for (String p : path1) {
            part1.add(p);
        }
        for (String p : path2) {
            part2.add(p);
        }
        // pull off last direction
        String lastOrientation = "E";
        for(int i = (part1.size()-1); i>=0; i--) {
            if(part1.get(i).equalsIgnoreCase("N") || part1.get(i).equalsIgnoreCase("S") || part1.get(i).equalsIgnoreCase("E") || part1.get(i).equalsIgnoreCase("W")) {
                lastOrientation = part1.get(i);
            }
        }
        
        // update all orientations
        // work out turning
        if(lastOrientation.equalsIgnoreCase("N")) {
            if(p2SO.equalsIgnoreCase("S")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("E")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("W")) {
                part2 = turnL(part2);                
            }
        } else if(lastOrientation.equalsIgnoreCase("S")) {
            if(p2SO.equalsIgnoreCase("N")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("E")) {
                part2 = turnL(part2);                
            } else if(p2SO.equalsIgnoreCase("W")) {
                part2 = turnL(part2); 
                part2 = turnL(part2);
                part2 = turnL(part2);
            }
        } else if(lastOrientation.equalsIgnoreCase("E")) {
            if(p2SO.equalsIgnoreCase("S")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("N")) {
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("W")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
            }
        } else if(lastOrientation.equalsIgnoreCase("W")) {
            if(p2SO.equalsIgnoreCase("S")) {
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("E")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
            } else if(p2SO.equalsIgnoreCase("N")) {
                part2 = turnL(part2);
                part2 = turnL(part2);
                part2 = turnL(part2);
            }
        }
        
        // add all together
        for (String p : part1) {
            result.add(p);
        }
        for (String p : part2) {
            result.add(p);
        }
        part1 = null;
        part2 = null;
        return result;
    }
    
    private static ArrayList<String> turnL(ArrayList<String> toTurn) {
        for(int i = 0; i<toTurn.size(); i++) {
            if(toTurn.get(i).equalsIgnoreCase("N")) {
                toTurn.set(i, "W");
            } else if(toTurn.get(i).equalsIgnoreCase("W")) {
                toTurn.set(i, "S");
            } else if(toTurn.get(i).equalsIgnoreCase("S")) {
                toTurn.set(i, "E");
            } else if(toTurn.get(i).equalsIgnoreCase("E")) {
                toTurn.set(i, "N");
            }
        }
        return toTurn;
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
    
    public static boolean parseBrackets(ArrayList<String> toTest) {
        boolean result = false;
        
        int counter = 0;
        String holder;
        Iterator<String> it = toTest.iterator();
        while(it.hasNext()) {
            holder = it.next();
            if(holder.equalsIgnoreCase("{")) {
                counter++;
            }
            if(holder.equalsIgnoreCase("}")) {
                counter++;
            }
        }
        
        if(counter%4==0) {
            result = true;
        } else {
            result = false;
        }
        
        return result;
    }
    
    public static ArrayList<String> condenseAntRep(ArrayList<String> result) {
        
        // cycle through removing duplicate subsets
        // work out total depth                   
        int maxDepth = 0;
        int depth = 0;      
        
        // ---------------------------------------------------------------------
        for (String s : result) {
            if (s.equals("{")) {
                depth++;
            }
            if (s.equals("}")) {
                depth--;
            }
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        //condense brackets only if there are brackets i.e. maxDepth>0
        if (maxDepth > 0) {
            boolean reduce = true;
            while (reduce) {
                reduce = false;
                // cycle through and condense
                int masterDepth = 0;
                depth = 0;
                int[] tracker = new int[maxDepth + 1];
                // set all to zero
                for (int i = 0; i < tracker.length; i++) {
                    tracker[i] = 0;
                }
                ArrayList<String> subset1, subset2;
                for (int i = 0; i < result.size() - 1; i++) {
                    if (result.get(i).equalsIgnoreCase("{")) {
                        tracker[masterDepth]++;
                    }
                    if (tracker[masterDepth] % 2 == 1 && result.get(i).equalsIgnoreCase("{")) {
                        subset1 = new ArrayList<String>();
                        subset2 = new ArrayList<String>();
                        depth = 0;
                        int endPoint1 = 0;
                        int endPoint2 = 0;
                        for (int y = i; y < result.size(); y++) {
                            if (result.get(y).equalsIgnoreCase("{")) {
                                depth++;
                            }
                            if (result.get(y).equalsIgnoreCase("}")) {
                                depth--;
                            }
                            subset1.add(result.get(y));
                            if (depth == 0) {
                                endPoint1 = y;
                                break;
                            }
                        }
                        for (int y = endPoint1 + 1; y < result.size(); y++) {
                            if (result.get(y).equalsIgnoreCase("{")) {
                                depth++;
                            }
                            if (result.get(y).equalsIgnoreCase("}")) {
                                depth--;
                            }
                            subset2.add(result.get(y));
                            if (depth == 0) {
                                endPoint2 = y;
                                break;
                            }
                        }
                        // check if subsets equivalent
                        if (subset1.equals(subset2)) {                                                
                            
                            // pull up pre if code
                            ArrayList<String> preif = new ArrayList<String>();
                            // work out expected orientation before IF
                            String expectedO = "E";
                            if (i > 0) {
                                for (int k = 0; k < i; k++) {
                                    preif.add(result.get(k));
                                    if (result.get(k).equalsIgnoreCase("N") || result.get(k).equalsIgnoreCase("S") || result.get(k).equalsIgnoreCase("E") || result.get(k).equalsIgnoreCase("W")) {
                                        expectedO = result.get(k);
                                    }
                                }
                            }
                            
                            // add subset1 to pre if
                            subset1.remove(0);
                            subset1.remove(subset1.size()-1);
                            for(String s: subset1) {
                                preif.add(s);
                            }
                            
                            // get post if code if necessary
                            if(result.size()>endPoint2) {
                                // get post if code
                                ArrayList<String> postif = new ArrayList<String>();
                                for(int k = (endPoint2+1); k<result.size(); k++) {
                                    postif.add(result.get(k));
                                }
                                // add post if code to preif+subset1 - care with orientation
                                result = joinPaths(preif, postif, expectedO);                                
                            } else {
                                result = preif;
                            }
                            
                            reduce = true;
                            break;
                        }
                    }
                    // fix depth afterwards
                    if (result.get(i).equalsIgnoreCase("{")) {
                        masterDepth++;
                    }
                    if (result.get(i).equalsIgnoreCase("}")) {
                        masterDepth--;
                    }
                }
            }
        }
        // ---------------------------------------------------------------------
        
        // pull out orientation letters in sequence
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).equalsIgnoreCase("N") || result.get(i).equalsIgnoreCase("W") || result.get(i).equalsIgnoreCase("S") || result.get(i).equalsIgnoreCase("E")) {
                if (result.get(i + 1).equalsIgnoreCase("N") || result.get(i + 1).equalsIgnoreCase("W") || result.get(i + 1).equalsIgnoreCase("S") || result.get(i + 1).equalsIgnoreCase("E")) {
                    result.remove(i);
                    i--;
                }
            }
        }
        
        ArrayList<String> controlStack = new ArrayList<String>();
        controlStack.add("E");
        depth = 0;
        for(int i = 0; i<result.size(); i++) {
            if(result.get(i).equalsIgnoreCase("{")) {
                // amend depth
                depth++;
                // move up previous depths orientation
                controlStack.add(controlStack.get(depth-1));
            } else if(result.get(i).equalsIgnoreCase("}")){
                // remove orientation from top of control stack
                controlStack.remove(depth);
                // amend depth
                depth--;
            } else if(result.get(i).equalsIgnoreCase("M")){
                // do nothing
            } else {
                if(result.get(i).equalsIgnoreCase(controlStack.get(depth))) {
                    // remove duplicate orientation
                    result.remove(i);
                    i--;
                } else {
                    controlStack.set(depth, result.get(i));
                }
            }            
        }
        
        return result;
    }
}
