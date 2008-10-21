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

package CoreN;

import java.util.ArrayList;

/**
 * A SemanticModule interface to define the structure of semantic contorl modules in 
 * problem specific packages
 * @author Lawrence Beadle
 */
public interface SemanticModule {
    
    /**
     * Starts the semantic module
     */
    public void start();
    
    /**
     * Stops the semantic module
     */
    public void finish();
    
    /**
     * Compares two program semantically
     * @param program1 1st program to be compared
     * @param program2 2nd program to be compared
     * @return True is semantically equivalent
     */
    public boolean comparePrograms(ArrayList<String> program1, ArrayList<String> program2);
    
    /**
     * creates BehaviourRepresentation representation of boolean programs
     * @param prog The program to be abstracted
     * @return The BehaviourRepresentation
     */
    public BehaviourRepresentation createRep(ArrayList<String> prog);
    
    /**
     * Trasnlates a BehaviourRepresentation representation into syntax
     * @param toProg The BehaviourRepresentation representation
     * @return A syntax representation of the program
     */
    public ArrayList<String> repToCode(BehaviourRepresentation toProg);    
}
