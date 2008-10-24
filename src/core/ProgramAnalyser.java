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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Program Analyser class provides tools for checking the boolean states of two programs
 * @author Lawrence Beadle
 */
public class ProgramAnalyser {
    
    /**
     * Checks the state of the pre and post representations of the boolean programs to see if they are the same
     * @param preState The pre change program
     * @param postState The post change program
     * @param semMod The semantic module to be used
     * @return Returns FALSE if the states before and after the change operation are the same and TRUE if they are not
     */
    public static boolean testStateChange(ArrayList<String> preState, ArrayList<String> postState, SemanticModule semMod) {
        boolean stateChange;
        SemanticModule sMod = semMod;
        sMod.start();
        stateChange = !semMod.comparePrograms(preState, postState);
        sMod.finish();
        return stateChange;
    }
    
    /**
     * Checks to see if the post and pre change operation are the same or have been modified.
     * @param preState The pre change program
     * @param postState The post change program
     * @return Returns True if the post program is different syntactically from the pre operation program
     */
    public static boolean testProgChange(ArrayList<String> preState, ArrayList<String> postState) {
        // TEST FOR PROG CHANGE
        boolean progChange;
        if(preState.size()==postState.size()) {
            progChange = false;
            int i = 0;
            for(String s: preState) {
                if(!s.equalsIgnoreCase(postState.get(i))) {
                    progChange = true;
                    break;
                }
                i++;
            }
        } else {
            progChange = true;
        }
        return progChange;
    }
    
    /**
     * Returns the depth of the program tree
     * @param prog The candidate program
     * @return The depth fo the candidate program
     */
    public static int getDepthOfTree(ArrayList<String> prog) {
        int count = 0;
        int maxDepth = 0;
        // count by brackets
        for(String p: prog) {
            if(p.equalsIgnoreCase("(")) {
                count++;
                if(count>maxDepth) {
                    maxDepth = count;
                }
            }
            if(p.equalsIgnoreCase(")")) {
                count--;
            }
        }
        return maxDepth;
    }
    
    /**
     * Returns the number of nodes in a program
     * @param nodes An ArrayList<Srtring> of all the functions
     * @param prog The candidate program
     * @return The number of nodes (functions)
     */
    public static int getNoOfFunctions(ArrayList<String> prog, ArrayList<String> nodes) {
        int noOfNodes = 0;
        for(String p: prog) {
            if(nodes.contains(p)) {
                noOfNodes++;
            }
        }
        return noOfNodes;
    }
    
    /**
     * Returns the number of terminals in a program
     * @return The number of terminals in the candidate program
     * @param terms An ArrayList<String> of all the terminals for comparison
     * @param prog The candidate program
     */
    public static int getNoOfTerminals(ArrayList<String> prog, ArrayList<String> terms) {
        int noOfTerms = 0;
        for(String p: prog) {
            if(terms.contains(p)) {
                noOfTerms++;
            }
        }
        return noOfTerms;
    }
    
    /**
     * Returns the number of distinct terminals in a program
     * @return The number of distinct (different) terminals
     * @param terms An ArrayList<String> of all the terminals for comparison
     * @param prog The candidate program
     */
    public static int getDistinctTerminals(ArrayList<String> prog, ArrayList<String> terms) {
        int noOfTerms = 0;
        ArrayList<String> passed = new ArrayList();
        for(String p: prog) {
            if(terms.contains(p) && !passed.contains(p)) {
                noOfTerms++;
                passed.add(p);
            }
        }
        return noOfTerms;
    }
    
    /**
     * Returns the modifaction depth in a tree
     * @param prog1 The first tree to compare
     * @param prog2 The second tree to compare
     * @return the modification depth
     */
    public static int getModificationDepth(ArrayList<String> prog1, ArrayList<String> prog2) {
        int count = 0;
        int len = 0;
        if(prog1.size()<prog2.size()) {
            len = prog1.size();
        } else {
            len = prog2.size();
        }
        for(int i = 0; i<len; i++) {
            if(!prog1.get(i).equalsIgnoreCase(prog2.get(i))) {
                break;
            }
            if(prog1.get(i).equalsIgnoreCase("(")) {
                count++;
            }
            if(prog1.get(i).equalsIgnoreCase(")")) {
                count--;
            }
        }
        return count;
    }
    
    /**
     * Returns the percentage syntax similarity between two programs
     * @param prog1 The first program to be compared
     * @param prog2 The second program to be compared
     * @return The percentage similarity 100 = exact match | 0 = completely different
     */
    public static double getSyntaxPercentageSimilarity(ArrayList<String> prog1, ArrayList<String> prog2) {
        // wokr out counts for %
        double total = 0;
        double count = 0;
        // decide which program has more nodes
        if(ProgramAnalyser.getProgramLength(prog1)<ProgramAnalyser.getProgramLength(prog2)) {
            total = ProgramAnalyser.getProgramLength(prog2);
        } else {
            total = ProgramAnalyser.getProgramLength(prog1);
        }
        // recursive count of the matching nodes
        count = ProgramAnalyser.compareNodes(prog1, prog2);
        // return calculation
        return (count/total)*100;
    }
    
    private static double compareNodes(ArrayList<String> part1, ArrayList<String> part2) {
        // set up count
        double count = 0;
        
        // roll into program and compare functions and terminals
        Iterator<String> it1 = part1.iterator();
        Iterator<String> it2 = part2.iterator();
        String p1, p2, p;
        while(it1.hasNext() && it2.hasNext()) {
            p1 = it1.next();
            p2 = it2.next();
            // if symbols are equal and not brackets
            if(p1.equalsIgnoreCase(p2)) {
                if(p1.equalsIgnoreCase("(")) {
                    // extract and submit to compareNodes again
                    // set up 1st arraylist part
                    int d = 1;
                    ArrayList<String> bit1 = new ArrayList<String>();
                    while(it1.hasNext()) {
                        p = it1.next();
                        if(p.equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(p.equalsIgnoreCase(")")) {
                            d--;
                        }
                        if(d==0) {
                            break;
                        }
                        bit1.add(p);
                    }
                    // set up 2nd arraylist part
                    d = 1;
                    ArrayList<String> bit2 = new ArrayList<String>();
                    while(it2.hasNext()) {
                        p = it2.next();
                        if(p.equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(p.equalsIgnoreCase(")")) {
                            d--;
                        }
                        if(d==0) {
                            break;
                        }
                        bit2.add(p);
                    }
                    count = count + ProgramAnalyser.compareNodes(bit1, bit2);
                } else {
                    count++;
                }
            } else {
                // if they dont equal only need to cycle forward if they are (
                // cycle through 1st iterator
                if(p1.equalsIgnoreCase("(")) {
                    int d = 1;
                    while(it1.hasNext()) {
                        p = it1.next();
                        if(p.equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(p.equalsIgnoreCase(")")) {
                            d--;
                        }
                        if(d==0) {
                            break;
                        }
                    }
                }
                // cycle thorugh 2nd iterator
                if (p2.equalsIgnoreCase("(")) {
                    int d = 1;
                    while (it2.hasNext()) {
                        p = it2.next();
                        if (p.equalsIgnoreCase("(")) {
                            d++;
                        }
                        if (p.equalsIgnoreCase(")")) {
                            d--;
                        }
                        if (d == 0) {
                            break;
                        }
                    }
                }                
            }
        }        
        return count;
    }
    
    /**
     * Returns the length of a program
     * @param prog The program to be measured
     * @return The length of the program
     */
    public static int getProgramLength(ArrayList<String> prog) {
        int count = 0;
        for(String p: prog) {
            if(!(p.equalsIgnoreCase("(") || p.equalsIgnoreCase(")"))) {
                count++;
            }
        }
        return count;
    }    
}
