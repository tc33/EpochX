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
 * The Syntax Interface defines the basic structure of the Syntax files in each of 
 * the problem specific packages
 * @author Lawrence Beadle
 */
public interface Syntax {
    
    /**
     * Returns the completes syntax structure
     * @return Returns the completes syntax structure
     */
    public ArrayList<ArrayList<String>> getSyntax();
    
    /**
     * Returns the terminals in syntax structure form - it is used in the GenPop methods
     * @return Returns the terminals in syntax structure form
     */
    public ArrayList<ArrayList<String>> getTerms();
    
    /**
     * Returns the terminals and '(' that represent the satrt of an expression.
     * @return Returns the terminals and '(' that represent the satrt of an expression.
     */
    public ArrayList<String> getEStart();
    
    /**
     * Returns a list of the functions in the syntax
     * @return Returns a list of the functions in the syntax
     */
    public ArrayList<String> getFunctions();
    
    /**
     * Returns a list of the terminals in the syntax
     * @return Returns a list of the terminals in the syntax
     */
    public ArrayList<String> getTerminals();
    
}
