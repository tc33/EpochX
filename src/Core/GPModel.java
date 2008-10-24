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

import lineageanalysis.*;
import models.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * The GPModel class controls the high level operations of a genetic programming run
 * @author Lawrence Beadle
 */
public class GPModel {
    
    // create fields
    private ArrayList<String> dataIn;
    private ArrayList<String> output;
    private ArrayList<ArrayList<String>> newPop;
    private int gens, g, cM, sM, scoreMeth;
    private int runs = 0;
    private double standardDev;
    private ArrayList<String> stats;
    private ArrayList<String> stateStats;
    private ArrayList<String> pADepthStats;
    private ArrayList<String> pANodeStats;
    private ArrayList<String> pATerminalStats;
    private ArrayList<String> pADTerminalStats;
    private ArrayList<String> lengthStats;
    private ArrayList<ArrayList<String>> results;
    private GPRunBasic genProg;
    private File cDir;
    private boolean lDump = false;
    
    /** Creates a new instance of GPModel */
    public GPModel() {     
        
        // clear out output ArrayLists
        stats = new ArrayList<String>();
        stateStats = new ArrayList<String>();
        pADepthStats = new ArrayList<String>();
        pANodeStats = new ArrayList<String>();
        pATerminalStats = new ArrayList<String>();
        pADTerminalStats = new ArrayList<String>();
        lengthStats = new ArrayList<String>();
        results = new ArrayList<ArrayList<String>>();
        // set up stats ArrayList
        stats.add("Run\tGeneration\tLowerCI95%\tMean\tUpperCI95%\tStDev\tMin\tMedian\tMax\tRange\n");
        stateStats.add("Run\tGeneration\tReverted Crossover\tTotal Crossover\tReverted Mutations\tTotal Mutations\n");
        pADepthStats.add("Run\tGeneration\tDepth Average\tDepth StDev\tDepth Max\tDepth Min\tDepth Range\n");
        pANodeStats.add("Run\tGeneration\tNode Average\tNode StDev\tNode Max\tNode Min\tNode Range\n");
        pATerminalStats.add("Run\tGeneration\tTerminal Average\tTerminal StDev\tTerminal Max\tTerminal Min\tTerminal Range\n");
        pADTerminalStats.add("Run\tGeneration\tD Terminal Average\tD Terminal StDev\tD Terminal Max\tD Terminal Min\tD Terminal Range\n");
        lengthStats.add("Run\tGeneration\tLength Average\tLength StDev\tLength Max\tLength Min\tLength Range\n");
    }
    
    /**
     * Pulls in the input data from the specified input file
     * @param cDir A File object representing the directory where the input file is located
     * @param fName The anme of the input file
     */
    public void loadRawData(File cDir, String fName) {
        
        // clear out ArrayLists
        dataIn = new ArrayList<String>();
        
        // read in file data
        dataIn = FileManip.loadInput(cDir, fName);
        System.out.println("Loading of Input Data Completed");
        
    }
    
    /**
     * Creates the first generation of programs
     * @param genType A String that sets the type of starting population ot be generated:
     * RH+H = ramped half and half
     * H+H = half and half
     * Full = full
     * Grow = grom
     * Random = random
     * SDAA = state differential Ant
     * SDAB = state differential BOOLEAN
     * @param popSize The size of the first generation of programs
     */
    public void buildFirstGen(int popSize, String genType) {
        
        // create first generation
        newPop = genProg.buildFirstPop(popSize, genType);
        
    }
    
    /**
     * Runs all the epochs required in the GP Run
     * @param genType A String that sets the type of starting population ot be generated:
     * RH+H = ramped half and half
     * H+H = half and half
     * Full = full
     * Grow = grom
     * Random = random
     * SDA = state differential
     * @param reproduction The number of programs to be copied through into the next generation.
     * @param sOMethod The choice of selection method:
     * 1 = Exact Tournament CARE: use popsize of 4, 8, 16, 32, 64 etc.
     * 2 = Fitness proportionate selection
     * 3 = 7 competitor tournament selection
     * 4 = Ranked selection
     * 5 = semantic selection
     * @param modelName The name of the model file to use
     * @param epochs The number of epochs to be evaluated
     * @param popSize the size of the population
     * @param gens the number of generations required
     * @param elites The number of elites required
     * @param pCross The probability of crossover
     * @param pMut The probability of mutation
     * @param mChecker Whether to run the mutation state checker
     * @param cChecker Whether to run the crossover state checker or not
     * @param cOMethod The crossover emthod desired
     * 1 = Single Point crossover
     * 2 = Standard Crossover (uniform swap point distribution)
     * 3 = Koza Crossover (90% on functions)
     * @param sMeth The type of scoring method 1 = input output 2 = semantic
     * @param baseDir The base directory where the input file is held and the outputs will be dumped to file
     */
    public void doGPRun(String modelName, int epochs, int popSize, String genType, int gens, int elites, int reproduction, double pCross, double pMut, boolean cChecker, boolean mChecker, int cOMethod, int sOMethod, int sMeth, File baseDir) {
        // cycle trhough diffferent epochs
        for(int i = 0; i<epochs; i++) {
            
            System.out.println("EPOCH " + (i+1));
            
            // set up Genetic Program instance - USING REFLECTION
            try {                
                genProg = (GPRunBasic) Class.forName("models." + modelName).getConstructor(new Class[] {}).newInstance(new Object[] {});
            } catch(ClassNotFoundException e) {
                System.out.println("CLASS NOT FOUND");
            } catch(NoSuchMethodException f) {
                System.out.println("NO SUCH METHOD");
            } catch(InstantiationException z) {
                System.out.println("COULD NOT INSTANTIATE CLASS");
            } catch(IllegalAccessException h) {
                System.out.println("ILLEGAL ACCESS EXCEPTION");
            } catch(InvocationTargetException k) {
                System.out.println("INVOCATION EXCEPTION");
            } catch(ClassCastException l) {
                System.out.println(l.toString());
            }
            
            if(genProg==null) {
                System.out.println("REFLECT FAILED TO LOAD FILE: " + modelName);
            }
            
            // load first Generation
            this.buildFirstGen(popSize, genType);
            
            // set runs number
            this.setEpoch(i);
            
            // do GP Run
            this.doGPEpoch(gens, elites, reproduction, pCross, pMut, cChecker, mChecker, cOMethod, sOMethod, sMeth, baseDir);
            
            // dump results to files
            this.dumpOutput(baseDir);
            
        }
    }
    
    /**
     * Runs all the generations required in the runs
     * @param reproduction The number of programs to be copied through into the next generation.
     * @param sO The choice of selection method:
     * 1 = Exact Tournament CARE: use popsize of 4, 8, 16, 32, 64 etc.
     * 2 = Fitness proportionate selection
     * 3 = 7 competitor tournament selection
     * 4 = Ranked selection
     * 5 = semantic selection
     * @param gensX The number of generations
     * @param elites The number of programs being carried through to the next generation
     * @param pCross The probability of crossover (Range: 0 --- 1)
     * @param pMut The probability of mutation (Range: 0 --- 1)
     * @param cChecker Choose whether to run a crossover state checker (true = yes / false = no) set to false if you don't have one
     * @param mChecker Choose whether to run a mutation state checker (true = yes / false = no) set to false if you don't have one
     * @param cO Choose the crossover method
     * 1 = Single point crossover
     * 2 = Standard crossover with uniform swap point distribution
     * 3 = Koza crossover (90% bias on functions)
     * @param sMeth The type of scoring method 1= input output 2 = semantic
     * @param dir A File object representing the desired output directory
     */
    public void doGPEpoch(int gensX, int elites, int reproduction, double pCross, double pMut, boolean cChecker, boolean mChecker, int cO, int sO, int sMeth, File dir) {
        
        // set directory and file object
        cDir = dir;       
        
        // execute GP program
        System.out.println("Starting GP Run");
        
        // load in all parameters
        gens = gensX;
        cM = cO;
        sM = sO;
        scoreMeth = sMeth;
        
        // set GP parameters
        genProg.setGPParams(0, dataIn, newPop, elites, reproduction, pCross, pMut, cChecker, mChecker);
        
        // sort out lineage dump
        FamilyStorage fStore = new FamilyStorage();
        if (lDump == true) {
            genProg.setLineageDump(lDump, fStore);
        }
        
        // create iterations for generations {
        for(int gen = 0; gen<gens; gen++) {
            
            // sets generation number for rest of program
            g = gen;
            
            // perform scoring
            genProg.doScoring(scoreMeth);
        
            // perform selection
            if(sM==1) {
                genProg.doSelection1();
            } else if(sM==2) {
                genProg.doSelection2();
            } else if(sM==3) {
                genProg.doSelection3();
            } else if(sM==4) {
                genProg.doSelection4();
            } else if(sM==5) {
                genProg.doSelection5();
            } else {
                System.out.println("NO SELECTION METHOD SELECTED");
            }

            // perform crossover
            if(cM==1) {
                genProg.doCrossOver1();
            } else if(cM==2) {
                genProg.doCrossOver2();
            } else if(cM==3) {
                genProg.doCrossOver3();
            } else {
                System.out.println("NO CROSSOVER METHOD SELECTED");
            }
        
            // perform mutation
            if(pMut>0) {
                genProg.doMutation();
            }
            
            // print progress
            System.out.println("Generation " + (gen+1) + " completed");
            System.out.println("Generation Stats - Total Score = " + genProg.getGenTot() + " - Ave Score = " + genProg.getGenAve() + " - Max Score = " + genProg.getMaxScore());
            
            // load info for stats
            standardDev = genProg.getGenSD();
            stats.add(runs + "\t" + (gen+1) + "\t" + genProg.getLowerCI() + "\t" + genProg.getGenAve() + "\t" + genProg.getUpperCI() + "\t" + standardDev + "\t" + genProg.getMinScore() + "\t" + genProg.getMedian() + "\t" + genProg.getMaxScore() + "\t" + genProg.getRange() + "\n");
            
            // load info for revertion stats
            stateStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getRCross() + "\t" + genProg.getTCross() + "\t" + genProg.getRMut() + "\t" + genProg.getTMut() + "\n");
            
            // load info for depth analysis stats
            pADepthStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getProgramDepthAverage() + "\t" + genProg.getProgramDepthSD() + "\t" + genProg.getProgramDepthMax() + "\t" + genProg.getProgramDepthMin() + "\t" + genProg.getProgramDepthRange() + "\n");
            
            // load info for node analysis stats
            pANodeStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getProgramNodeAverage() + "\t" + genProg.getProgramNodeSD() + "\t" + genProg.getProgramNodeMax() + "\t" + genProg.getProgramNodeMin() + "\t" + genProg.getProgramNodeRange() + "\n");

            // load info for terminal analysis stats
            pATerminalStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getProgramTerminalAverage() + "\t" + genProg.getProgramTerminalSD() + "\t" + genProg.getProgramTerminalMax() + "\t" + genProg.getProgramTerminalMin() + "\t" + genProg.getProgramTerminalRange() + "\n");

            // load info for distinct terminal analysis stats
            pADTerminalStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getProgramDistinctTerminalAverage() + "\t" + genProg.getProgramDistinctTerminalSD() + "\t" + genProg.getProgramDistinctTerminalMax() + "\t" + genProg.getProgramDistinctTerminalMin() + "\t" + genProg.getProgramDistinctTerminalRange() + "\n");
            
            // load info for program length analysis
            lengthStats.add(runs + "\t" + (gen+1) + "\t" + genProg.getProgramLengthAverage() + "\t" + genProg.getProgramLengthSD() + "\t" + genProg.getProgramLengthMax() + "\t" + genProg.getProgramLengthMin() + "\t" + genProg.getProgramLengthRange() + "\n");

        }
        
        // do lineage analysis
        if(lDump==true) {
            LineageAnalysis lA = new LineageAnalysis(fStore, genProg.getScorerModel(), dataIn, genProg.getSemanticModel(), runs, gens, cDir, genProg.getFunctions(), genProg.getTerminals());        
        }
        
        // pull out program results
        output = new ArrayList<String>();
        output.add("Epoch: " + this.getEpochNumber() + " --- Generation: " + (g+1) + " Programs:\n\n");
        results = getWinners();
        int i = 0;
        Scorer scorer = genProg.getScorerModel();
        for(ArrayList<String> res: results) {
            i++;
            output.add("Program " + i  + " score = " + scorer.getScore(dataIn, res) + "\n");
            output.add("Program " + i + "\n");
            for(String r: res) {
                output.add(r + " ");
            }
            output.add("\n-----\n");
        }
        output.add("\n---------------------------------------------------\n\n");
        
    }
    
    /**
     * Dumps output scores to file
     * @param cDir A File object representing the place to dump the output files
     */
    public void dumpOutput(File cDir) {       
        
        // dump results back to file
        System.out.println("Dumping to File");        
        FileManip.doOutput(cDir, output, "GP-Program-Output.txt", true);
        // print stats to stats output file
        FileManip.doOutput(cDir, stats, "GP-Stats-Output.txt", true);
        // print stateStats output to file
        FileManip.doOutput(cDir, stateStats, "GP-State-Stats-Output.txt", true);
        // print out PA Depth stats
        FileManip.doOutput(cDir, pADepthStats, "GP-PA-DEPTH-Stats-Output.txt", true);
        // print out PA Node stats
        FileManip.doOutput(cDir, pANodeStats, "GP-PA-NODE-Stats-Output.txt", true);
        // print out PA Terminal stats
        FileManip.doOutput(cDir, pATerminalStats, "GP-PA-TERMINAL-Stats-Output.txt", true);
        // print out PA Distinct Terminal stats
        FileManip.doOutput(cDir, pADTerminalStats, "GP-PA-DISTINCT-TERMINAL-Stats-Output.txt", true);
        // print out PA Program Length stats
        FileManip.doOutput(cDir, lengthStats, "GP-PA-Length-Stats-Output.txt", true);
        
        // clear out output ArrayLists
        stats = new ArrayList<String>();
        stateStats = new ArrayList<String>();
        pADepthStats = new ArrayList<String>();
        pANodeStats = new ArrayList<String>();
        pATerminalStats = new ArrayList<String>();
        pADTerminalStats = new ArrayList<String>();
        lengthStats = new ArrayList<String>();
        output = new ArrayList<String>();
        
        System.out.println("GP Run Completed");
        // increment g for progress monitor
        g++;
    }
    
    /**
     * Set which runs the over all run is on
     * @param e The runs number
     */
    public void setEpoch(int e) {
        runs = e+1;
    }
    
    /**
     * Returns the winning programs from selection
     * @return The winning programs from this generations selection process
     */
    public ArrayList<ArrayList<String>> getWinners() {
        return genProg.getResult();
    }
    
    /**
     * Returns the generation which is in progress
     * @return The generation number
     */
    public int getGenerationNumber() {
        return g;
    }
    
    /**
     * Returns the current runs number
     * @return The runs number
     */
    public int getEpochNumber() {
        return runs;
    }
    
    /**
     * Returns statisitcs to be displayed in the display pane
     * @return The stats to be displayed in the display pane
     */
    public ArrayList<String> getStats() {
        return stats;
    }
    
    /**
     * Returns the programs to be displayed in the display pane
     * @return The programs to be displayed
     */
    public ArrayList<String> getProgs() {
        return output;
    }
    
    /**
     * Sets whether the GP model should dump lineage information to file
     * @param lD True to dump information fals eto not dump information
     */
    public void setLineageDump(boolean lD) {
        lDump = lD;
    }
}
