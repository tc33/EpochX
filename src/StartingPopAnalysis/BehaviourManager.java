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

package startingpopanalysis;

import net.sf.javabdd.*;
import java.util.ArrayList;
import core.*;

/**
 * The BDD manager aids analysis of starting populations
 * @author Lawrence Beadle
 */
public class BehaviourManager {
    
    private BehaviourRepresentation model;
    private ArrayList<ArrayList<String>> programs;
    
    /** Creates a new instance of BehaviourManager
     * @param mod The BDD model
     */
    public BehaviourManager(BehaviourRepresentation mod) {
        model = mod;
        programs = new ArrayList<ArrayList<String>>();
    }
    
    /**
     * Returns the BDD model
     * @return The BDD model
     */
    public BehaviourRepresentation getBehaviour() {
        return model;
    }
    
    /**
     * Returns the Frequncy of the programs associated with this ROBDD
     * @return The number of programs associated with a particular ROBDD
     */
    public int getFrequency() {
        return programs.size();
    }
    
    /**
     * Adds a program of the same behaviour to the BDD manager
     * @param prog The behaviourally equivalent program
     */
    public void addProgram(ArrayList<String> prog) {
        programs.add(prog);
    }
    
    /**
     * Returns all the programs associated with a ROBDD
     * @return The collection of programs
     */
    public ArrayList<ArrayList<String>> getPrograms() {
        return programs;
    }    
}
