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

package Core;

import java.util.ArrayList;

/**
 * The Scorer interface works as a guide for building problem specific Scorers 
 * in each of the problem packages
 * @author Lawrence Beadle
 */
public interface Scorer {
    
    /**
     * Sets the scorer type input/output or semantic
     * @param type 1 for input-output, 2 for semantic
     * @param semMod the semantic module to be used
     */
    public void setScoreMethodType(int type, SemanticModule semMod);
    
    /**
     * Returns the score of a program
     * @param input An ArrayList<String> of all the possible input stats that can be presented to the problem
     * @param program An ArrayList<String> representing the program to be tested
     * @return The score the program achieved
     */
    public double getScore(ArrayList<String> input, ArrayList<String> program);
    
    /**
     * Returns an ArrayList<String> representing a top scoring program
     * @return The top scoring program
     */
    public ArrayList<String> getBestProgram();
    
}
