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

import lineageanalysis.*;

/**
 * Provides the core functionality for the basic processes of the genetic programming 
 * operations such as crossover, mutation and selection.
 * @author Lawrence Beadle
 */
public class GPCoreCode implements GPRunBasic {
    
    private ArrayList<String> input = new ArrayList<String>();
    private ArrayList<String> output = new ArrayList<String>();
    private ArrayList<ArrayList<String>> newPop = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> fGen = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> thisGen = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> winners = new ArrayList<ArrayList<String>>();      
    //private ArrayList<String> states;
    private int pop, elites, gSize, count2, d, g, tCross, rCross, tMut, rMut;
    private int remEnd, remStart, eS, p1Len, p2Len, z, k, progSize, sType;
    private int reproduction;
    private double pCross, pMut, genAve, genTot, standardDev, score1, score2;
    private double maxScore, minScore;
    private ArrayList<String> prog1, prog2, prog3, prog4;
    private GenPop firstPop;
    /**
     * General ArrayList<ArrayList<String>> of all of the syntax available.  This is to be set
     * by a call from the sub class in the models directory
     */
    protected ArrayList<ArrayList<String>> syntax = new ArrayList<ArrayList<String>>();
    /**
     * An ArrayList<ArrayList<String>> of all the terminals used in the syntax.  This 
     * is to be set by a call from the sub class
     */
    protected ArrayList<ArrayList<String>> synterms = new ArrayList<ArrayList<String>>();
    /**
     * An ArrayList<String> of all syntax which symbolises the start of an expression 
     * as defined by the syntax tree.  In most cases this is all the terminals and the '('.
     * This is to be set by a call from the sub class.
     */
    protected ArrayList<String> eStart = new ArrayList<String>();
    /**
     * An ArrayList<String> of all the terminals in the syntax.  This is to be set 
     * by a call from the subclass
     */
    protected ArrayList<String> terminals = new ArrayList<String>();
    /**
     * An ArrayList<String> representing all the functions in the syntax. 
     * This is to be set by a call from the subclass.
     */
    protected ArrayList<String> functions = new ArrayList<String>();
    private Random rGen = new Random();    
    private double[] sDMem, depthMem, nodeMem, termMem, distinctTermMem, lengthMem;
    private boolean stateMon, stateMon2, dSM, mSM, trip1, trip2;
    private double[] scores;
    private boolean lDump = false;
    private FamilyStorage fStore;
    /**
     * A representation of the Scorer implementation.  This is to be set by a call from 
     * the subclass.
     */
    protected Scorer scorer;
    /**
     * A representation of the SemanticModule implementation.  This is to be set by a call from
     * the subclass.
     */
    protected SemanticModule semanticMod;
    
    /**
     * Creates the first population
     * @return The first population
     * @param genType The type of starting population to be generated:
     * RH+H = ramped half and half
     * H+H = half and half
     * Grow = grow
     * Full = full
     * Random = random
     * SDA = state differential
     * @param popSize the size of the first population
     */
    public ArrayList<ArrayList<String>> buildFirstPop(int popSize, String genType) {
        // create first generation of programs & validate them
        output.add("\n\n1st Generation Programs\n\n");
        firstPop = new GenPop(popSize, syntax, synterms, semanticMod);
        firstPop.createPop(genType);
        newPop = firstPop.getFirstGen();
        for(ArrayList<String> nP: newPop) {
            // print to output
            output.add("Output = ");
            for(String n: nP) {
                output.add(n + " ");
            }
            output.add("\n");
        }        
        return newPop;
    }
    
    /**
     * Returns the first population to print
     * @return The first population in printable-ish form
     */
    public ArrayList<String> FirstPopToPrint() {
        return output;
    }
            
    /**
     * Sets the Genetic Program parameters
     * @param reproduction1 the number of programs to be copied through to the next generation
     * @param genX The number of generations
     * @param testData The input and output states
     * @param firstGen The first generation
     * @param elites1 The number of prorgams to be retained at the end fo each generation
     * @param pC The probability of crossover
     * @param pM The probability of mutation
     * @param mChecker Boolean TRUE to run mutation state checker
     * @param cChecker Boolean TRUE to run crossover state checker
     */
    public void setGPParams(int genX, ArrayList<String> testData, ArrayList<ArrayList<String>> firstGen, int elites1, int reproduction1, double pC, double pM, boolean cChecker, boolean mChecker) {
        
        // load in all parameters
        input = testData;
        fGen = firstGen;
        pop = firstGen.size();
        elites = elites1;
        reproduction = reproduction1;
        for(ArrayList<String> f: fGen) {
            thisGen.add(f);
        }
        pCross = pC;
        pMut = pM;
        dSM = cChecker;
        mSM = mChecker;
        g = genX;
        //states = new ArrayList();

    }
    
    /**
     * The first crossover method - Single point crossover
     */
    public void doCrossOver1() {
        
        // update generation info
        g++;
        
        // copy through elites of needed
        if(elites>0) {
            for(int i = 0; i<elites; i++) {
                thisGen.add(winners.get(i));
            }
        }
        
        // set up crossover counters
        tCross = 0;
        rCross = 0;
        
        while(thisGen.size()<pop) {
            
            // pull out 2 programs
            // pull out parent programs and set up child
            prog1 = winners.get(rGen.nextInt(reproduction));
            prog2 = winners.get(rGen.nextInt(reproduction));
            prog3 = new ArrayList<String>();
            prog4 = new ArrayList<String>();
            
            // add all of prog1 into 3
            for(String n: prog1) {
                prog3.add(n);
            }
            
            // add all of prog2 into 4
            for(String n: prog2) {
                prog4.add(n);
            }
            
            // if cross over passes probability test
            if(Math.random()<=pCross) {
                
                // total up cross over
                tCross = tCross + 2;
                
                // work out lengths to iterate over
                p1Len = countExprs(prog3);
                p2Len = countExprs(prog4);
                
                // decide which expressions to swap
                int swapTake = 0;
                if(p1Len<p2Len) {
                    swapTake = rGen.nextInt(p1Len) + 1;
                } else {
                    swapTake = rGen.nextInt(p2Len) + 1;
                }
                
                // roll into prog 3 and pull out expression from take point
                int count = 0;
                eS = 0;
                String swap;
                ArrayList<String> subtree1 = new ArrayList<String>();
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        subtree1 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree1.add(prog3.get(count));
                        } else {
                            subtree1.add(prog3.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree1.add(prog3.get(count2));
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog 4 and pull out expression from put point -
                count = 0;
                eS = 0;
                ArrayList<String> subtree2 = new ArrayList<String>();
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        subtree2 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree2.add(prog4.get(count));
                        } else {
                            subtree2.add(prog4.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree2.add(prog4.get(count2));
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog3 and put in new expression at take point -
                count = 0;
                eS = 0;
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog3.remove(count);
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog3.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog4 and put in new expression at put point --
                count = 0;
                eS = 0;
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog4.remove(count);
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog4.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // MAX DEPTH 17 section ------------------------------------
                if(ProgramAnalyser.getDepthOfTree(prog3)>17) {
                    // revert 3 back to 1
                    prog3 = new ArrayList<String>();
                    for(String part: prog1) {
                        prog3.add(part);
                    }
                }
                if(ProgramAnalyser.getDepthOfTree(prog4)>17) {
                    // revert 4 back to 2
                    prog4 = new ArrayList<String>();
                    for(String part: prog2) {
                        prog4.add(part);
                    }
                }
                // ---------------------------------------------------------
                
                // STATE CHANGE SECTION ------------------------------------
                trip1 = false;
                trip2 = false;
                if(dSM==true) {
                    // check if P1 OR P2 has same state as P3
                    stateMon = ProgramAnalyser.testStateChange(prog1, prog3, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog2, prog3, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip1 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 1 back into 3
                        prog3 = new ArrayList<String>();
                        for(String nP: prog1) {
                            prog3.add(nP);
                        }
                    }
                    
                    // check if P1 OR P2 has same state as P4
                    stateMon = ProgramAnalyser.testStateChange(prog2, prog4, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog1, prog4, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip2 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 2 back into 4
                        prog4 = new ArrayList<String>();
                        for(String nP: prog2) {
                            prog4.add(nP);
                        }
                    }
                }
                // ---------------------------------------------------------                
            }
            // add new program to population
            if(trip1==false) {
                if (thisGen.size() < pop) {
                    thisGen.add(prog3);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
            if(trip2==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog4);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
        }       
    }
    
    /**
     * The second crossover method - random point crossover with uniform distribution swap point selection
     */
    public void doCrossOver2() {
        
        // update generation info
        g++;
        
        // copy through elites of needed
        if(elites>0) {
            for(int i = 0; i<elites; i++) {
                thisGen.add(winners.get(i));
            }
        }
        
        // set up crossover counters
        tCross = 0;
        rCross = 0;
        
        while(thisGen.size()<pop) {
            
            // pull out 2 programs
            // pull out parent programs and set up child
            prog1 = winners.get(rGen.nextInt(reproduction));
            prog2 = winners.get(rGen.nextInt(reproduction));
            prog3 = new ArrayList<String>();
            prog4 = new ArrayList<String>();
            
            // add all of prog1 into 3
            for(String n: prog1) {
                prog3.add(n);
            }
            
            // add all of prog2 into 4
            for(String n: prog2) {
                prog4.add(n);
            }
            
            // if cross over passes probability test
            if(Math.random()<=pCross) {
                
                // total up cross over
                tCross = tCross + 2;               
                
                // work out lengths to iterate over
                p1Len = countExprs(prog3);
                p2Len = countExprs(prog4);
                
                // decide which expressions to swap
                int swapTake = rGen.nextInt(p1Len) + 1;
                int swapPut = rGen.nextInt(p2Len) + 1;
                
                // roll into prog 3 and pull out expression from take point
                int count = 0;
                eS = 0;
                String swap;
                ArrayList<String> subtree1 = new ArrayList<String>();
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        subtree1 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree1.add(prog3.get(count));
                        } else {
                            subtree1.add(prog3.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree1.add(prog3.get(count2));
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog 4 and pull out expression from put point -
                count = 0;
                eS = 0;
                ArrayList<String> subtree2 = new ArrayList<String>();
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        subtree2 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree2.add(prog4.get(count));
                        } else {
                            subtree2.add(prog4.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree2.add(prog4.get(count2));
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog3 and put in new expression at take point -
                count = 0;
                eS = 0;
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog3.remove(count);
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog3.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog4 and put in new expression at put point --
                count = 0;
                eS = 0;
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog4.remove(count);
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog4.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // MAX DEPTH 17 section ------------------------------------
                if(ProgramAnalyser.getDepthOfTree(prog3)>17) {
                    // revert 3 back to 1
                    prog3 = new ArrayList<String>();
                    for(String part: prog1) {
                        prog3.add(part);
                    }
                }
                if(ProgramAnalyser.getDepthOfTree(prog4)>17) {
                    // revert 4 back to 2
                    prog4 = new ArrayList<String>();
                    for(String part: prog2) {
                        prog4.add(part);
                    }
                }
                // ---------------------------------------------------------
                
                // STATE CHANGE SECTION ------------------------------------
                trip1 = false;
                trip2 = false;
                if(dSM==true) {
                    // check if P1 OR P2 has same state as P3
                    stateMon = ProgramAnalyser.testStateChange(prog1, prog3, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog2, prog3, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip1 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 1 back into 3
                        prog3 = new ArrayList<String>();
                        for(String nP: prog1) {
                            prog3.add(nP);
                        }
                    }
                    
                    // check if P1 OR P2 has same state as P4
                    stateMon = ProgramAnalyser.testStateChange(prog2, prog4, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog1, prog4, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip2 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 2 back into 4
                        prog4 = new ArrayList<String>();
                        for(String nP: prog2) {
                            prog4.add(nP);
                        }
                    }
                }
                // ---------------------------------------------------------
            }
            // add new program to population
            if(trip1==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog3);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
            if(trip2==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog4);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
        }
    }
    
    /**
     * Performs an implimentation of Koza crossover - Random swap points with a 90% selection bias on functions
     */
    public void doCrossOver3() {
        
        // update generation info
        g++;
        
        // set up crossover counters
        tCross = 0;
        rCross = 0;
        
        // copy through elites of needed
        if(elites>0) {
            for(int i = 0; i<elites; i++) {
                thisGen.add(winners.get(i));
            }
        }
        
        while(thisGen.size()<pop) {
            
            // pull out 2 programs
            // pull out parent programs and set up child
            prog1 = winners.get(rGen.nextInt(reproduction));
            prog2 = winners.get(rGen.nextInt(reproduction));
            prog3 = new ArrayList<String>();
            prog4 = new ArrayList<String>();
            
            // add all of prog1 into 3
            for(String n: prog1) {
                prog3.add(n);
            }
            
            // add all of prog2 into 4
            for(String n: prog2) {
                prog4.add(n);
            }
            
            // if cross over passes probability test
            if(Math.random()<=pCross) {
                
                // total up cross over
                tCross = tCross + 2;
                
                // work out lengths to iterate over
                p1Len = countExprs(prog3);
                p2Len = countExprs(prog4);
                
                // decide which expressions to swap
                int swapTake = 0;
                int swapPut = 0;
                
                // work out how mnay functions and terminals
                int func1 = ProgramAnalyser.getNoOfFunctions(prog3, functions);
                int func2 = ProgramAnalyser.getNoOfFunctions(prog4, functions);
                int term1 = ProgramAnalyser.getNoOfTerminals(prog3, terminals);
                int term2 = ProgramAnalyser.getNoOfTerminals(prog4, terminals);
                
                // work out whether to SWAP OUT a function or temrinal 90% on functions
                // for take point
                int expCount = 0;
                if(rGen.nextDouble()<=0.9) {
                    // choose a function
                    if(func1>0) {
                        int funcToSwap = rGen.nextInt(func1) + 1;
                        int funcCount = 0;
                        for(String part: prog3) {
                            // check if function
                            if(functions.contains(part)) {
                                funcCount++;
                            }
                            // check if expression start
                            if(eStart.contains(part)) {
                                expCount++;
                            }
                            if(funcCount==funcToSwap) {
                                swapTake = expCount;
                                break;
                            }
                        }
                    } else {
                        swapTake = 1;
                    }
                } else {
                    // swap a terminal
                    int termToSwap = rGen.nextInt(term1) + 1;
                    int termCount = 0;
                    for(String part: prog3) {
                        // check if function
                        if(terminals.contains(part)) {
                            termCount++;
                        }
                        // check if expression start
                        if(eStart.contains(part)) {
                            expCount++;
                        }
                        if(termCount==termToSwap) {
                            swapTake = expCount;
                            break;
                        }
                    }
                }
                
                // work out whether to PUT IN a function or temrinal 90% on functions
                // for put point
                expCount = 0;
                if(rGen.nextDouble()<=0.9) {
                    // choose a function
                    if(func2>0) {
                        int funcToSwap = rGen.nextInt(func2) + 1;
                        int funcCount = 0;
                        for(String part: prog4) {
                            // check if function
                            if(functions.contains(part)) {
                                funcCount++;
                            }
                            // check if expression start
                            if(eStart.contains(part)) {
                                expCount++;
                            }
                            if(funcCount==funcToSwap) {
                                swapPut = expCount;
                                break;
                            }
                        }
                    } else {
                        swapPut = 1;
                    }
                } else {
                    // swap a terminal
                    int termToSwap = rGen.nextInt(term2) + 1;
                    int termCount = 0;
                    for(String part: prog4) {
                        // check if function
                        if(terminals.contains(part)) {
                            termCount++;
                        }
                        // check if expression start
                        if(eStart.contains(part)) {
                            expCount++;
                        }
                        if(termCount==termToSwap) {
                            swapPut = expCount;
                            break;
                        }
                    }
                }
                
                // roll into prog 3 and pull out expression from take point
                int count = 0;
                eS = 0;
                String swap;
                ArrayList<String> subtree1 = new ArrayList<String>();
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        subtree1 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree1.add(prog3.get(count));
                        } else {
                            subtree1.add(prog3.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree1.add(prog3.get(count2));
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog 4 and pull out expression from put point -
                count = 0;
                eS = 0;
                ArrayList<String> subtree2 = new ArrayList<String>();
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        subtree2 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree2.add(prog4.get(count));
                        } else {
                            subtree2.add(prog4.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree2.add(prog4.get(count2));
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog3 and put in new expression at take point -
                count = 0;
                eS = 0;
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog3.remove(count);
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog3.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog4 and put in new expression at put point --
                count = 0;
                eS = 0;
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog4.remove(count);
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog4.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // MAX DEPTH 17 section ------------------------------------
                // OR MAX LENGTH 500 section -------------------------------
                if(ProgramAnalyser.getDepthOfTree(prog3)>17) {
                //if(ProgramAnalyser.getProgramLength(prog3)>500) {
                    // revert 3 back to 1
                    prog3 = new ArrayList<String>();
                    for(String part: prog1) {
                        prog3.add(part);
                    }
                }
                if(ProgramAnalyser.getDepthOfTree(prog4)>17) {
                //if(ProgramAnalyser.getProgramLength(prog4)>500) {
                    // revert 4 back to 2
                    prog4 = new ArrayList<String>();
                    for(String part: prog2) {
                        prog4.add(part);
                    }
                }
                // ---------------------------------------------------------
                
                // STATE CHANGE SECTION ------------------------------------
                trip1 = false;
                trip2 = false;
                if(dSM==true) {
                    // check if P1 OR P2 has same state as P3
                    stateMon = ProgramAnalyser.testStateChange(prog1, prog3, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog2, prog3, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip1 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 1 back into 3
                        prog3 = new ArrayList<String>();
                        for(String nP: prog1) {
                            prog3.add(nP);
                        }
                    }
                    
                    // check if P1 OR P2 has same state as P4
                    stateMon = ProgramAnalyser.testStateChange(prog2, prog4, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog1, prog4, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip2 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 2 back into 4
                        prog4 = new ArrayList<String>();
                        for(String nP: prog2) {
                            prog4.add(nP);
                        }
                    }
                }
                // ---------------------------------------------------------
       
                // do lineage dump if true -------------------------------------
                if (lDump == true) {
                    fStore.storeRecord(prog1, prog2, prog3, prog4, g);
                }
                // ----------------------------------------------------------------- 
            }

            // add new program to population
            if(trip1==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog3);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
            if(trip2==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog4);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
        }
    }
    
    /**
     * Perfroms Exact Koza Style crossover with fitness proportionate selection of parents
     * and 90% bias on funcitons and 10% bias on terminals
     */
    public void doCrossOver4() {
        // update generation info
        g++;
        
        // set up crossover counters
        tCross = 0;
        rCross = 0;
        
        // copy through elites of needed
        if(elites>0) {
            for(int i = 0; i<elites; i++) {
                thisGen.add(winners.get(i));
            }
        }
        
        while(thisGen.size()<pop) {
            
            // pull out 2 programs
            prog1 = winners.get(rGen.nextInt(reproduction));
            prog2 = winners.get(rGen.nextInt(reproduction));
            prog3 = new ArrayList<String>();
            prog4 = new ArrayList<String>();
            
            // add all of prog1 into 3
            for(String n: prog1) {
                prog3.add(n);
            }
            
            // add all of prog2 into 4
            for(String n: prog2) {
                prog4.add(n);
            }
            
            // if cross over passes probability test
            if(Math.random()<=pCross) {
                
                // total up cross over
                tCross = tCross + 2;
                
                // work out lengths to iterate over
                p1Len = countExprs(prog3);
                p2Len = countExprs(prog4);
                
                // decide which expressions to swap
                int swapTake = 0;
                int swapPut = 0;
                
                // work out how mnay functions and terminals
                int func1 = ProgramAnalyser.getNoOfFunctions(prog3, functions);
                int func2 = ProgramAnalyser.getNoOfFunctions(prog4, functions);
                int term1 = ProgramAnalyser.getNoOfTerminals(prog3, terminals);
                int term2 = ProgramAnalyser.getNoOfTerminals(prog4, terminals);
                
                // work out whether to SWAP OUT a function or temrinal 90% on functions
                // for take point
                int expCount = 0;
                if(rGen.nextDouble()<=0.9) {
                    // choose a function
                    if(func1>0) {
                        int funcToSwap = rGen.nextInt(func1) + 1;
                        int funcCount = 0;
                        for(String part: prog3) {
                            // check if function
                            if(functions.contains(part)) {
                                funcCount++;
                            }
                            // check if expression start
                            if(eStart.contains(part)) {
                                expCount++;
                            }
                            if(funcCount==funcToSwap) {
                                swapTake = expCount;
                                break;
                            }
                        }
                    } else {
                        swapTake = 1;
                    }
                } else {
                    // swap a terminal
                    int termToSwap = rGen.nextInt(term1) + 1;
                    int termCount = 0;
                    for(String part: prog3) {
                        // check if function
                        if(terminals.contains(part)) {
                            termCount++;
                        }
                        // check if expression start
                        if(eStart.contains(part)) {
                            expCount++;
                        }
                        if(termCount==termToSwap) {
                            swapTake = expCount;
                            break;
                        }
                    }
                }
                
                // work out whether to PUT IN a function or temrinal 90% on functions
                // for put point
                expCount = 0;
                if(rGen.nextDouble()<=0.9) {
                    // choose a function
                    if(func2>0) {
                        int funcToSwap = rGen.nextInt(func2) + 1;
                        int funcCount = 0;
                        for(String part: prog4) {
                            // check if function
                            if(functions.contains(part)) {
                                funcCount++;
                            }
                            // check if expression start
                            if(eStart.contains(part)) {
                                expCount++;
                            }
                            if(funcCount==funcToSwap) {
                                swapPut = expCount;
                                break;
                            }
                        }
                    } else {
                        swapPut = 1;
                    }
                } else {
                    // swap a terminal
                    int termToSwap = rGen.nextInt(term2) + 1;
                    int termCount = 0;
                    for(String part: prog4) {
                        // check if function
                        if(terminals.contains(part)) {
                            termCount++;
                        }
                        // check if expression start
                        if(eStart.contains(part)) {
                            expCount++;
                        }
                        if(termCount==termToSwap) {
                            swapPut = expCount;
                            break;
                        }
                    }
                }
                
                // roll into prog 3 and pull out expression from take point
                int count = 0;
                eS = 0;
                String swap;
                ArrayList<String> subtree1 = new ArrayList<String>();
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        subtree1 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree1.add(prog3.get(count));
                        } else {
                            subtree1.add(prog3.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree1.add(prog3.get(count2));
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog 4 and pull out expression from put point -
                count = 0;
                eS = 0;
                ArrayList<String> subtree2 = new ArrayList<String>();
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        subtree2 = new ArrayList<String>();
                        // pull out whole expression to swap
                        if(!swap.equalsIgnoreCase("(")) {
                            subtree2.add(prog4.get(count));
                        } else {
                            subtree2.add(prog4.get(count));
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                subtree2.add(prog4.get(count2));
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog3 and put in new expression at take point -
                count = 0;
                eS = 0;
                while(eS<p1Len) {
                    swap = prog3.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapTake) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog3.remove(count);
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog3.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog3.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog3.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree2.size()-1); i>=0; i--) {
                                prog3.add(count, subtree2.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // roll into prog4 and put in new expression at put point --
                count = 0;
                eS = 0;
                while(eS<p2Len) {
                    swap = prog4.get(count);
                    if(eStart.contains(swap)) {
                        // increment expression count
                        eS++;
                    }
                    if(eS==swapPut) {
                        // insert swapped expression
                        if(!swap.equalsIgnoreCase("(")) {
                            // pull out old terminal
                            prog4.remove(count);
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        } else {
                            // pull out expression
                            remStart = count;
                            count2 = count + 1;
                            d = 1;
                            while(true) {
                                if(prog4.get(count2).equalsIgnoreCase("(")) {
                                    d++;
                                }
                                if(prog4.get(count2).equalsIgnoreCase(")")) {
                                    d--;
                                }
                                if(d==0) {
                                    break;
                                }
                                count2++;
                            }
                            remEnd = count2;
                            for(int k1 = remEnd; k1>=remStart; k1--) {
                                prog4.remove(k1);
                            }
                            // put in new prog piece
                            for(int i=(subtree1.size()-1); i>=0; i--) {
                                prog4.add(count, subtree1.get(i));
                            }
                        }
                        break;
                    }
                    count++;
                }
                // ---------------------------------------------------------
                
                // MAX DEPTH 17 section ------------------------------------
                if(ProgramAnalyser.getDepthOfTree(prog3)>17) {
                    // revert 3 back to 1
                    prog3 = new ArrayList<String>();
                    for(String part: prog1) {
                        prog3.add(part);
                    }
                }
                if(ProgramAnalyser.getDepthOfTree(prog4)>17) {
                    // revert 4 back to 2
                    prog4 = new ArrayList<String>();
                    for(String part: prog2) {
                        prog4.add(part);
                    }
                }
                // ---------------------------------------------------------
                
                // STATE CHANGE SECTION ------------------------------------
                trip1 = false;
                trip2 = false;
                if(dSM==true) {
                    // check if P1 OR P2 has same state as P3
                    stateMon = ProgramAnalyser.testStateChange(prog1, prog3, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog2, prog3, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip1 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 1 back into 3
                        prog3 = new ArrayList<String>();
                        for(String nP: prog1) {
                            prog3.add(nP);
                        }
                    }
                    
                    // check if P1 OR P2 has same state as P4
                    stateMon = ProgramAnalyser.testStateChange(prog2, prog4, semanticMod);
                    stateMon2 = ProgramAnalyser.testStateChange(prog1, prog4, semanticMod);
                    if(stateMon==false || stateMon2==false) {
                        trip2 = true;
                        // tally up revertions
                        rCross++;
                        // revert crossover - copy 2 back into 4
                        prog4 = new ArrayList<String>();
                        for(String nP: prog2) {
                            prog4.add(nP);
                        }
                    }
                }
                // ---------------------------------------------------------
            }
            
            // add new program to population
            if(trip1==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog3);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
            if(trip2==false) {
                if(thisGen.size()<pop) {
                    thisGen.add(prog4);
                } else {
                    tCross--;
                }
            } else {
                tCross--;
            }
        }
    }

    /**
     * The mutation method
     */
    public void doMutation() {
        // reset counters
        rMut = 0;
        tMut = 0;
        
        // go though every program and tinker with it slightly if P<pMut
        for(int i = elites; i<pop; i++) {
           
            // if check to see if prog is changed
            if (rGen.nextDouble() <= pMut) {
                // add one to total mutation count
                tMut++;
              
                // set up to repeat until new mutation attained
                int masterDrop = 0;
                boolean changeMarker = false;
                while (!changeMarker) {
                    changeMarker = true;

                    // get program
                    prog1 = thisGen.get(i);

                    // copy program
                    ArrayList<String> copyProg = new ArrayList<String>();
                    for (String s : prog1) {
                        copyProg.add(s);
                    }

                    // find the expression to edit
                    int s = this.countExprs(prog1);
                    int toAlter = rGen.nextInt(s);

                    //System.out.println("1 : " + prog1);

                    // cycle through looking for the correct expression
                    int j = 0;
                    int e = 0;
                    while (j < prog1.size()) {
                        // update e if necessary
                        if (eStart.contains(prog1.get(j))) {
                            e++;
                        }
                        // if at the right expression
                        if (e == toAlter) {
                            if (prog1.get(j).equalsIgnoreCase("(")) {
                                // if function replace function
                                int depth = 0;
                                while (true) {
                                    if (prog1.get(j).equalsIgnoreCase("(")) {
                                        depth++;
                                    }
                                    if (prog1.get(j).equalsIgnoreCase(")")) {
                                        depth--;
                                    }
                                    if (prog1.get(j).equalsIgnoreCase(")") && depth == 0) {
                                        prog1.set(j, "*");
                                        break;
                                    }
                                    prog1.remove(j);
                                }
                            } else {
                                // if terminal replace one for one
                                prog1.set(j, "*");
                            }
                            break;
                        }
                        j++;
                    }

                    //System.out.println("2 : " + prog1);

                    // pass through replacing all the *'s with more statements            
                    z = 0;
                    progSize = prog1.size();
                    while (z < progSize) {
                        if (prog1.get(z).equalsIgnoreCase("*")) {
                            prog2 = firstPop.makeRandom();
                            k = z;
                            prog1.remove(k);
                            for (String p : prog2) {
                                prog1.add(k, p);
                                k++;
                            }
                            break;
                        }
                        z++;
                    }

                    //System.out.println("3 : " + prog1);

                    // check if size is in valid range
                    if (ProgramAnalyser.getDepthOfTree(prog1) > 17) {
                        prog1 = new ArrayList<String>();
                        for (String p : copyProg) {
                            prog1.add(p);
                        }
                    }

                    // compare before and after semantically
                    if (mSM) {
                        changeMarker = ProgramAnalyser.testStateChange(prog1, copyProg, semanticMod);
                        if (changeMarker==false) {
                            // false if not change so increment counter and revert
                            if(masterDrop==0) {
                                rMut++;
                            }
                            prog1 = new ArrayList<String>();
                            for (String p : copyProg) {
                                prog1.add(p);
                            }
                            //System.out.println("REPEAT TRIP: " + i + " : " + rMut);
                            masterDrop++;
                        }
                    }

                    // set thisGen value to the new prog1
                    thisGen.set(i, prog1);

                    // clear copy prog
                    copyProg = null;
                    
                    // set a master drop out @5 in case mutation cant change behaviour
                    if(masterDrop>=5) {
                        rMut--;
                        break;
                    }
                }
            }
        }        
    }

    /**
     * Scores all programs based on the scorer model.  doScoring 1 is reserved for 
     * scoring based on input output models.
     * @param sMeth An integer 1 for input-output scoring 2 for semantic
     */
    public void doScoring(int sMeth) {
        // set max score and min score defaults
        sType = sMeth;
        scorer.setScoreMethodType(sType, semanticMod);
        maxScore = input.size();
        minScore = 0;
        genTot = 0;
        // Store statistics ----------------------------------------------------
        sDMem = new double[pop];
        depthMem = new double[pop];
        nodeMem = new double[pop];
        termMem = new double[pop];
        distinctTermMem = new double[pop];
        lengthMem = new double[pop];
        // ---------------------------------------------------------------------
        // run through and score every program
        scores = new double[pop];
        genTot = 0;
        for(int i = 0; i<pop; i++) {
            scores[i] = scorer.getScore(input, thisGen.get(i));
            // sort out max and mins
            if(scores[i]<maxScore) {
                maxScore = scores[i];
            }
            if(scores[i]>minScore) {
                minScore = scores[i];
            }
            // add scores for calc of Stats ------------------------------------
            sDMem[i] = scores[i];
            // add depth scores
            depthMem[i] = ProgramAnalyser.getDepthOfTree(thisGen.get(i));
            // add node scores
            nodeMem[i] = ProgramAnalyser.getNoOfFunctions(thisGen.get(i), functions);
            // add terminal scores
            termMem[i] = ProgramAnalyser.getNoOfTerminals(thisGen.get(i), terminals);
            // add distinct terminal scores
            distinctTermMem[i] = ProgramAnalyser.getDistinctTerminals(thisGen.get(i), terminals);
            // add program lengths
            lengthMem[i] = ProgramAnalyser.getProgramLength(thisGen.get(i));
            // end of stats ----------------------------------------------------
            genTot = genTot + scores[i];
        }        
    }

    /**
     * Full Tournament selection - be sure to use a pop size eg. 2, 4, 8, 16, 32, 64 etc
     */
    public void doSelection1() {
        
        winners = new ArrayList<ArrayList<String>>();

        // run tournament selection
        while(thisGen.size()>reproduction) {
            
            gSize = thisGen.size()-1;           
            
            for(int i = 0; i<gSize; i=i+2) {
                score1 = 0;
                score2 = 0;
                
                score1 = scores[i];
                //System.out.println("S1=" + score1);
                score2 = scores[i+1];
                //System.out.println("S2=" + score2);
                
                // decide winner and add it to winners list
                if(score1<=score2) {
                    winners.add(thisGen.get(i));
                } else {
                    winners.add(thisGen.get(i+1));
                }
            }          
        }
        // clear pop for next gen
        thisGen = new ArrayList<ArrayList<String>>();
    }
    
    /**
     * An implementation of fitness proportionate selection
     */
    public void doSelection2() {
        // fitness proportionate selection
        winners = new ArrayList<ArrayList<String>>();
        
        // compute normalised fitness
        double scores2[] = new double[pop];
        // pass once and do adjusted fitness
        double sTotal = 0;
        for(int i = 0; i<pop; i++) {
            scores2[i] = 1 / (1 + scores[i]);
            sTotal = sTotal + scores2[i];
        }
        // pass again and normalise scores
        for(int i = 0; i<pop; i++) {
           scores2[i] = scores2[i] / sTotal;
        }
        
        double chose, tot2;
        //System.out.print("Programs Chosen: ");
        for(int j= 0; j<reproduction; j++) {
            chose = Math.random();
            //System.out.print(chose + "-");
            // cycle through scores[] until score total = choose
            tot2 = 0;
            for(int k1 = 0; k1<(scores2.length); k1++) {                
                if(chose>=tot2 && chose<(tot2+scores2[k1])) {
                    // pull out index and add to winners
                    //System.out.print(k1 + ", ");
                    winners.add(thisGen.get(k1));
                    break;
                }
                tot2 = tot2 + scores2[k1];
            }
        }
        //System.out.println("WINNERS SIZE=" + winners.size());
        // clear pop for next gen
        thisGen = new ArrayList<ArrayList<String>>();
    }

    
    /**
     * A seven competitor tournament selection
     */
    public void doSelection3() {
        // 3 tournament selection as in Banzhafs Book
        winners = new ArrayList<ArrayList<String>>();
        // number in tournament
        int r1, r2, r3, r4, r5, r6, r7, tmpWin;
        // select competition randomly and choose best program
        for(int i = 0; i<reproduction; i++) {
            // choose programs for competition
            r1 = rGen.nextInt(pop);
            r2 = rGen.nextInt(pop);
            r3 = rGen.nextInt(pop);
            r4 = rGen.nextInt(pop);
            r5 = rGen.nextInt(pop);
            r6 = rGen.nextInt(pop);
            r7 = rGen.nextInt(pop);
            // choose winning program
            if(scores[r1]<scores[r2]) {
                tmpWin = r1;
            } else {
                tmpWin = r2;
            }
            if(scores[tmpWin]>scores[r3]) {
                tmpWin = r3;
            }
            if(scores[tmpWin]>scores[r4]) {
                tmpWin = r4;
            }
            if(scores[tmpWin]>scores[r5]) {
                tmpWin = r5;
            }
            if(scores[tmpWin]>scores[r6]) {
                tmpWin = r6;
            }
            if(scores[tmpWin]>scores[r7]) {
                tmpWin = r7;
            }
            winners.add(thisGen.get(tmpWin));
        }
        // clear winner for next generation
        thisGen = new ArrayList<ArrayList<String>>();
    }
    
    /**
     * A ranked selection method
     */
    public void doSelection4() {
        // RANKED SELECTION
        winners = new ArrayList<ArrayList<String>>();
        // select competition randomly and choose best program
        // count how mnay programs have been added
        int eCount = 0;
        double cScore = maxScore;
        while(eCount<reproduction) {
            for(int j = 0; j<pop; j++) {
                if(scores[j]<=cScore) {
                    winners.add(thisGen.get(j));
                    eCount++;
                    if(eCount>=reproduction) {
                        break;
                    }
                }
            }
            cScore++;
        }
        // copy winners back to pop
        thisGen = new ArrayList<ArrayList<String>>();
    }
    
    /**
     * A 3 Tournament selection method
     */
    public void doSelection5() {
        // 3 tournament selection as in Banzhafs Book
        winners = new ArrayList<ArrayList<String>>();
        // number in tournament
        int r1, r2, r3, tmpWin;
        // select competition randomly and choose best program
        for(int i = 0; i<reproduction; i++) {
            // choose programs for competition
            r1 = rGen.nextInt(pop);
            r2 = rGen.nextInt(pop);
            r3 = rGen.nextInt(pop);
            // choose winning program
            if(scores[r1]<scores[r2]) {
                tmpWin = r1;
            } else {
                tmpWin = r2;
            }
            if(scores[tmpWin]>scores[r3]) {
                tmpWin = r3;
            }
            winners.add(thisGen.get(tmpWin));
        }
        // clear winner for next generation
        thisGen = new ArrayList<ArrayList<String>>();
    }

    /**
     * Returns the number of expressions in a program
     * @param program The program which we want to count the expressions of
     * @return The number of expressions contained within the the supplied program
     */
    public int countExprs(ArrayList<String> program) {
        
        int noExpr = 0;
        for(String e: program) {
            if(eStart.contains(e)) {
                noExpr++;
            }
        }
        return noExpr;
        
    }
    
    /**
     * Returns the winning programs
     * @return The winning programs
     */
    public ArrayList<ArrayList<String>> getResult() {
        return winners;
    }
    
    // -------------------------------------------------------------------------
    // GENERAL STATS
    // -------------------------------------------------------------------------
    
    /**
     * Retursn the generation total score
     * @return The total score
     */
    public double getGenTot() {
        return genTot;
    }
    
    /**
     * Returns the generation score average
     * @return The average score for the generation
     */
    public double getGenAve() {
        genAve = genTot / pop;
        return genAve;
    }
    
    /**
     * The generation score standard deviation
     * @return The standard deviation for the generation score
     */
    public double getGenSD() {
        genAve = getGenAve();
        double[] sDMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            sDMem1[i] = (sDMem[i]-genAve)*(sDMem[i]-genAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + sDMem1[i];
        }
        standardDev = Math.sqrt(tot/pop);
        return standardDev;
    }
    
    /**
     * Returns the max score for the generation
     * @return The max score
     */
    public double getMaxScore() {
        return maxScore;
    }
    
    /**
     * Returns the mix score for the generation
     * @return The min score
     */
    public double getMinScore() {
        return minScore;
    }
    
    /**
     * Returns the median score for the generation
     * @return The median score
     */
    public double getMedian() {
        return minScore + ((maxScore-minScore)/2);
    }
    
    /**
     * Returns the range of scores for the generation
     * @return The range of scores
     */
    public double getRange() {
        return minScore-maxScore;
    }
    
    /**
     * Retursn the lower confidence interval (95%) for the generation
     * @return The lower CI for the generations
     */
    public double getLowerCI() {
        return getGenAve()-(standardDev*1.96);
    }
    
    /**
     * Returns the upper confidence interval (95%) for the generation
     * @return The upper confidence interval
     */
    public double getUpperCI() {
        return getGenAve()+(standardDev*1.96);
    }
    
    // -------------------------------------------------------------------------
    // Program Analysis - Revertion statistics
    // -------------------------------------------------------------------------
    
    /**
     * Returns the total number of crossovers
     * @return The total number of crossovers
     */
    public int getTCross() {
        return tCross;
    }
    
    /**
     * Return the reverted number of crossovers
     * @return The reverted number of crossovers
     */
    public int getRCross() {
        return rCross;
    }
    
    /**
     * Return the total number of mutations occuring
     * @return the total number of mutations
     */
    public int getTMut() {
        return tMut;
    }
    
    /**
     * Return the number of reverted mutations
     * @return the reverted number of mutations
     */
    public int getRMut() {
        return rMut;
    }
    
    // -------------------------------------------------------------------------
    // Program Analysis - Depth of tree stats
    // -------------------------------------------------------------------------
    
    /**
     * Returns the average program depth
     * @return The average program depth
     */
    public double getProgramDepthAverage() {
        double depthTot = 0;
        for(int j = 0; j<pop; j++) {
            depthTot = depthTot + depthMem[j];
        }
        return depthTot/pop;
    }
    
    /**
     * Returns the maximum program depth
     * @return The maximum program depth
     */
    public int getProgramDepthMax() {
        int depthMax = 0;
        for(int j = 0; j<pop; j++) {
            if(depthMem[j]>depthMax) {
                depthMax = (int) depthMem[j];
            }
        }
        return depthMax;
    }
    
    /**
     * Retursn the minimum program depth
     * @return The minimum program depth
     */
    public int getProgramDepthMin() {
        int depthMin = 1000000;
        for(int j = 0; j<pop; j++) {
            if(depthMem[j]<depthMin) {
                depthMin = (int) depthMem[j];
            }
        }
        return depthMin;
    }
    
    /**
     * Returns the range of program depth over a generation
     * @return The range of program depths
     */
    public int getProgramDepthRange() {
        return getProgramDepthMax() - getProgramDepthMin();
    }
    
    /**
     * returns the standard deviation of program depths over a generation
     * @return The standard deviaiton of program depths
     */
    public double getProgramDepthSD() {
        double depthAve = getProgramDepthAverage();
        double[] depthMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            depthMem1[i] = (depthMem[i]-depthAve)*(depthMem[i]-depthAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + depthMem1[i];
        }
        return Math.sqrt(tot/pop); 
    }
    
    // -------------------------------------------------------------------------
    // Prorgam Analysis - Nodes in Tree stats
    // -------------------------------------------------------------------------
    
    /**
     * Returns the avergae number of nodes in prorgams over a generation
     * @return The average number of nodes in a program
     */
    public double getProgramNodeAverage() {
        double nodeTot = 0;
        for(int j = 0; j<pop; j++) {
            nodeTot = nodeTot + nodeMem[j];
        }
        return nodeTot/pop;
    }
    
    /**
     * Returns the maximum number of nodes in prorgams over a generation
     * @return The maximum number of nodes
     */
    public int getProgramNodeMax() {
        int nodeMax = 0;
        for(int j = 0; j<pop; j++) {
            if(nodeMem[j]>nodeMax) {
                nodeMax = (int) nodeMem[j];
            }
        }
        return nodeMax;
    }
    
    /**
     * Return the minimum number of nodes on a generation
     * @return The minimum number of nodes
     */
    public int getProgramNodeMin() {
        int nodeMin = 1000000;
        for(int j = 0; j<pop; j++) {
            if(nodeMem[j]<nodeMin) {
                nodeMin = (int) nodeMem[j];
            }
        }
        return nodeMin;
    }
    
    /**
     * Return the range of nodes in prorgams over a generation
     * @return The range of nodes
     */
    public int getProgramNodeRange() {
        return getProgramNodeMax() - getProgramNodeMin();
    }
    
    /**
     * Returns the standard deviation of the number of nodes in prorgams over a generation
     * @return The standard deviation of nodes
     */
    public double getProgramNodeSD() {
        double nodeAve = getProgramNodeAverage();
        double[] nodeMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            nodeMem1[i] = (nodeMem[i]-nodeAve)*(nodeMem[i]-nodeAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + nodeMem1[i];
        }
        return Math.sqrt(tot/pop); 
    }
    
    // -------------------------------------------------------------------------
    // Prorgam Analysis - Terminals in Tree stats
    // -------------------------------------------------------------------------
    
    /**
     * Returns average number of terminals in prorgams over a generation
     * @return Average number of terminals
     */
    public double getProgramTerminalAverage() {
        double termTot = 0;
        for(int j = 0; j<pop; j++) {
            termTot = termTot + termMem[j];
        }
        return termTot/pop;
    }
    
    /**
     * Returns the maximum number of terminals in programs over the generation
     * @return The maximum number of terminals
     */
    public int getProgramTerminalMax() {
        int termMax = 0;
        for(int j = 0; j<pop; j++) {
            if(termMem[j]>termMax) {
                termMax = (int) termMem[j];
            }
        }
        return termMax;
    }
    
    /**
     * Returns the minimum number of terminals in prorgams over a generation
     * @return The minimum number of terminals
     */
    public int getProgramTerminalMin() {
        int termMin = 1000000;
        for(int j = 0; j<pop; j++) {
            if(termMem[j]<termMin) {
                termMin = (int) termMem[j];
            }
        }
        return termMin;
    }
    
    /**
     * Returns the range of the number of terminals in the generation programs
     * @return The range of terminals
     */
    public int getProgramTerminalRange() {
        return getProgramTerminalMax() - getProgramTerminalMin();
    }
    
    /**
     * Returns standard deviaiton of terminals over a generation
     * @return The standard deviation of terminals
     */
    public double getProgramTerminalSD() {
        double termAve = getProgramTerminalAverage();
        double[] termMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            termMem1[i] = (termMem[i]-termAve)*(termMem[i]-termAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + termMem1[i];
        }
        return Math.sqrt(tot/pop); 
    }
    
    // -------------------------------------------------------------------------
    // Program Analysis - Distinct Terminals in Tree stats
    // -------------------------------------------------------------------------
    
    /**
     * Returns the average distinct number of temrinals over a generation
     * @return The avergae distinct number of terminals
     */
    public double getProgramDistinctTerminalAverage() {
        double dTermTot = 0;
        for(int j = 0; j<pop; j++) {
            dTermTot = dTermTot + distinctTermMem[j];
        }
        return dTermTot/pop;
    }
    
    /**
     * Returns the max distinct number of terminals over a generation
     * @return The maximum distinct number of terminals
     */
    public int getProgramDistinctTerminalMax() {
        int dTermMax = 0;
        for(int j = 0; j<pop; j++) {
            if(distinctTermMem[j]>dTermMax) {
                dTermMax = (int) distinctTermMem[j];
            }
        }
        return dTermMax;
    }
    
    /**
     * Returns the minimum number of distinct number of terminals over a generation
     * @return The minimum distinct number of terminals
     */
    public int getProgramDistinctTerminalMin() {
        int dTermMin = 1000000;
        for(int j = 0; j<pop; j++) {
            if(distinctTermMem[j]<dTermMin) {
                dTermMin = (int) distinctTermMem[j];
            }
        }
        return dTermMin;
    }
    
    /**
     * The range of the distinct number of terminals over a generation
     * @return The range of the distinct number of terminals
     */
    public int getProgramDistinctTerminalRange() {
        return getProgramDistinctTerminalMax() - getProgramDistinctTerminalMin();
    }
    
    /**
     * The standard deviaiton of the distinct number of terminals in a generation
     * @return The standard deviaiton of the distinct number of terminals
     */
    public double getProgramDistinctTerminalSD() {
        double dTermAve = getProgramDistinctTerminalAverage();
        double[] distinctTermMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            distinctTermMem1[i] = (distinctTermMem[i]-dTermAve)*(distinctTermMem[i]-dTermAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + distinctTermMem1[i];
        }
        return Math.sqrt(tot/pop); 
    }
    
    // -------------------------------------------------------------------------
    // Program Analysis - Tree Length stats
    // -------------------------------------------------------------------------
    
    /**
     * Returns the average program length over a generation
     * @return The average program length
     */
    public double getProgramLengthAverage() {
        double lengthTot = 0;
        for(int j = 0; j<pop; j++) {
            lengthTot = lengthTot + lengthMem[j];
        }
        return lengthTot/pop;
    }
    
    /**
     * Returns the max program length over a generation
     * @return The maximum program length
     */
    public int getProgramLengthMax() {
        int lengthMax = 0;
        for(int j = 0; j<pop; j++) {
            if(lengthMem[j]>lengthMax) {
                lengthMax = (int) lengthMem[j];
            }
        }
        return lengthMax;
    }
    
    /**
     * Returns the minimum program length over a generation
     * @return The minimum program length
     */
    public int getProgramLengthMin() {
        int lengthMin = 1000000;
        for(int j = 0; j<pop; j++) {
            if(lengthMem[j]<lengthMin) {
                lengthMin = (int) lengthMem[j];
            }
        }
        return lengthMin;
    }
    
    /**
     * The range of the program lengths over a generation
     * @return The range of the program lengths
     */
    public int getProgramLengthRange() {
        return getProgramLengthMax() - getProgramLengthMin();
    }
    
    /**
     * The standard deviation of the program lengths in a generation
     * @return The standard deviation of the program lengths
     */
    public double getProgramLengthSD() {
        double lengthAve = getProgramLengthAverage();
        double[] lengthMem1 = new double[pop];
        for(int i=0; i<pop; i++) {
            lengthMem1[i] = (lengthMem[i]-lengthAve)*(lengthMem[i]-lengthAve);
        }
        double tot = 0;
        for(int i=0; i<pop; i++) {
            tot = tot + lengthMem1[i];
        }
        return Math.sqrt(tot/pop); 
    }
    
    /**
     * Returns the Scorer model used
     * @return Returns the Scorer model used
     */
    public Scorer getScorerModel() {
        return scorer;
    }
    
    /**
     * Retursn the semantic model used
     * @return The sematic model used
     */
    public SemanticModule getSemanticModel() {
        return semanticMod;
    }
    
    /**
     * Allows the user to dump a lineage file to a txt file - 
     * WARING this will be very time consuming!!!
     * @param toDump true to do the dump - is set as false automatically
     */
    public void setLineageDump(boolean toDump, FamilyStorage famStore) {
        lDump = toDump;
        fStore = famStore;
    }
    
    /**
     * Returns the terminals used in the GP model
     * @return The terminals used in the GP model
     */
    public ArrayList<String> getTerminals() {
        return terminals;
    }
    
    /**
     * Returns the functions used in the GP model
     * @return Returns the functions used in the GP model
     */
    public ArrayList<String> getFunctions() {
        return functions;
    }
    
}
