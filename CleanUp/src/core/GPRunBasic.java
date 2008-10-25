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
 * This interface provides the template method that are requried in each Genetic Programming Run
 * @author Lawrence Beadle
 */
public interface GPRunBasic {
    
    // General Functions ------------------
    
    /**
     * Build the first population
     * @return An ArrayList<ArrayList<String>> representing the program in the first population
     * @param genType A String representing the starting population type:
     * RH+H = ramped half and half
     * H+H = half and half
     * Full = full
     * Grow = grow
     * Random = random
     * SDA = state differential
     * @param size The size of the 1st population
     */
    public ArrayList<ArrayList<String>> buildFirstPop(int size, String genType);
    
    /**
     * This base method sets all the parameter required to run each Genetic Program generation
     * @param reporduction The number of programs to be copied through into the next generation
     * @param generations int - The number of generations in an Epoch
     * @param testData The input output data for the program
     * @param firstGen The first generation of programs required
     * @param elites1 int - The number of elites retained after selection
     * @param pC double - probability of crossover
     * @param pM double - probability of mutation
     * @param doSM boolean - run the crossover state checker or not - if you dont use a state checker just leave it as "false"
     * @param mChecker boolean - run the mutation state checker or not - if you dont use a state checker just leave it as "false"
     */
    public void setGPParams(int generations, ArrayList<String> testData, ArrayList<ArrayList<String>> firstGen, int elites1, int reporduction, double pC, double pM, boolean doSM, boolean mChecker);
    
    /**
     * Single Point Crossover
     */
    public void doCrossOver1();
    
    /**
     * Standard Crossover with uniform distributed selection points
     */
    public void doCrossOver2();
    
    /**
     * Swap points 90% on functions and 10% on terminals
     */
    public void doCrossOver3();
    
    /**
     * An exact implimentation of crossover as per Koza with fitness proportionate parent
     * selection and 90% bias onf functions at the swap point
     */
    public void doCrossOver4();
    
    /**
     * The method which performs mutation on the node tree
     */
    public void doMutation();
    
    /**
     * standard input-output scoring method
     * @param sMeth The score method 1 = input/output 2 = semantic
     */
    public void doScoring(int sMeth);
    
    /**
     * This method provides the algorithm for selecting the best programs from the population
     * Selection 1 is reserved for exact tournament selection CARE use pop size of 4, 8, 16, 32, 64 etc...
     */
    public void doSelection1();
    
    /**
     * This method provides the algorithm for selecting the best programs from the population
     * Selection 2 is resevred for fitness proportionate selection
     */
    public void doSelection2();
    
    /**
     * This method provides the algorithm for selecting the best programs 
     * according to tournament selection as in banzhafs book (7 competitor)
     */
    public void doSelection3();
    
    /**
     * Ranked Selection method
     */
    public void doSelection4();
    
    /**
     * T3 Selection method
     */
    public void doSelection5();
    
    /**
     * Returns the best programs back to the control model
     * @return An ArrayList of all the best programs to retunr to the controller model
     */
    public ArrayList<ArrayList<String>> getResult();
    
    // -------------------------------------
    
    // General stats -----------------------
    
    /**
     * Returns the total score of all the programs in the generation
     * @return The total fitness score of all the programs in the population
     */
    public double getGenTot();
    
    /**
     * Returns the average score of all the programs in the population for the generation
     * @return The average fitness score for the generation
     */
    public double getGenAve();
    
    /**
     * Returns the standard deviation of the score of the program in this generation
     * @return The standard deviation of the score of the programs in this generation
     */
    public double getGenSD();
    
    /**
     * Returns the maximum score of the programs in the population for this generation
     * @return The maximum fitness score for this generation
     */
    public double getMaxScore();
    
    /**
     * Returns the manimum score of the programs in the population for this generation
     * @return The manimum fitness score for this generation
     */
    public double getMinScore();
    
    /**
     * Returns the median score for all the programs in the generation ((min score + max score) / 2)
     * @return The median fitness score of the programs in the generation
     */
    public double getMedian();
    
    /**
     * Returns the range of scores for this generation of programs (max score - min score)
     * @return The range of scores of this generation
     */
    public double getRange();
    
    /**
     * Returns the lower confidence interval (95%) for the programs in this generation 
     * (average score - (standard deviation * 1.96))
     * @return The lower 95% condfidence interval of scores for this generation
     */
    public double getLowerCI();
    
    /**
     * Returns the upper confidence interval (95%) for the programs in this generation 
     * (average score + (standard deviation * 1.96))
     * @return The upper 95% condfidence interval of scores for this generation
     */
    public double getUpperCI();
    
    // crossover stats
    
    /**
     * Returns the total number of crossovers
     * @return The total number of crossovers
     */
    public int getTCross();
    
    /**
     * Returns the total number of reverted crossovers
     * @return The total number of reverted crossovers
     */
    public int getRCross();
    
    /**
     * Returns the number of reverted mutations
     * @return The number of reverted mutations
     */
    public int getRMut();
    
    /**
     * Returns the total number of mutations
     * @return The total number of mutations
     */
    public int getTMut();
    
    // depth stats
    
    /**
     * Returns the average program depth
     * @return The average program depth
     */
    public double getProgramDepthAverage();
    
    /**
     * Returns the program depth standard deviation
     * @return The SDev of program depth
     */
    public double getProgramDepthSD();
    
    /**
     * Returns the maximum depth of programs
     * @return The maximum program depth
     */
    public int getProgramDepthMax();
    
    /**
     * Returns the minimum program depth
     * @return The minimum program depth
     */
    public int getProgramDepthMin();
    
    /**
     * Returns the rnage of program depths
     * @return The range of program depths
     */
    public int getProgramDepthRange();
    
    // node stats
    
    /**
     * Returns the average number of Nodes
     * @return The avergae number of nodes
     */
    public double getProgramNodeAverage();
    
    /**
     * The SDev of the nodes
     * @return The sDev of the nodes
     */
    public double getProgramNodeSD();
    
    /**
     * Returns the maximum number of nodes
     * @return The maximum number of nodes
     */
    public int getProgramNodeMax();
    
    /**
     * Returns the minimum number of nodes
     * @return The minimum number of nodes
     */
    public int getProgramNodeMin();
    
    /**
     * Returns the range of the number of nodes
     * @return The range of the number of nodes
     */
    public int getProgramNodeRange();
    
    // terminal stats
    
    /**
     * Returns the average number of terminals
     * @return The average number of temrinals
     */
    public double getProgramTerminalAverage();
    
    /**
     * Returns the SDev of the number of terminals
     * @return The SDev of the number of terminals
     */
    public double getProgramTerminalSD();
    
    /**
     * Returns the maximum number of terminals
     * @return The maximum number of terminals
     */
    public int getProgramTerminalMax();
    
    /**
     * Returns the minimum number of terminals
     * @return The minimum number of terminals
     */
    public int getProgramTerminalMin();
    
    /**
     * Returns the range of the number of terminals
     * @return The range of the number of terminals
     */
    public int getProgramTerminalRange();
    
    // distinct terminal stats
    
    /**
     * Returns the average of the distinct number of terminals
     * @return The average number of distinct terminals
     */
    public double getProgramDistinctTerminalAverage();
    
    /**
     * Returns the SDev of the number of distinct terminals
     * @return The SDev of the number of distinct temrinals
     */
    public double getProgramDistinctTerminalSD();
    
    /**
     * Returns the maximum number of distinct terminals
     * @return The maximum number of distinct terminals
     */
    public int getProgramDistinctTerminalMax();
    
    /**
     * Returns the minimum number of distinct terminals
     * @return The minimum number of distinct terminals
     */
    public int getProgramDistinctTerminalMin();
    
    /**
     * Returns the range of the number of distinct terminals
     * @return The range of the distinct temrinals
     */
    public int getProgramDistinctTerminalRange();
    
    // -------------------------------------
    
    // program length stats
    
    /**
     * Returns the average length of the program
     * @return The average length of the program
     */
    public double getProgramLengthAverage();
    
    /**
     * Returns the SDev of the program length
     * @return The SDev of the number of the program length
     */
    public double getProgramLengthSD();
    
    /**
     * Returns the maximum program length
     * @return The maximum program length
     */
    public int getProgramLengthMax();
    
    /**
     * Returns the minimum program length
     * @return The minimum program length
     */
    public int getProgramLengthMin();
    
    /**
     * Returns the range of the program lengths
     * @return The range of the program lengths
     */
    public int getProgramLengthRange();
    
    // -------------------------------------
    
    /**
     * Returns the Scorer model used
     * @return Returns the Scorer model used
     */
    public Scorer getScorerModel();
    
    /**
     * Retursn the semantic model used
     * @return The sematic model used
     */
    public SemanticModule getSemanticModel();
    
    /**
     * Allows the user to dump a lineage file to a txt file - 
     * WARING this will be very time consuming!!!
     * @param toDump true to do the dump - is set as false automatically
     * @param famStore The FamilyStorage Object used
     */
    public void setLineageDump(boolean toDump, FamilyStorage famStore);
    
    /**
     * Returns the functions used in the model
     * @return The functions used
     */
    public ArrayList<String> getFunctions();
    
    /**
     * Returns the terminals used in the model
     * @return The terminals used
     */
    public ArrayList<String> getTerminals();
    
}
