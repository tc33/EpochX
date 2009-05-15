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

package com.epochx.initialisation.analysis;

import java.util.ArrayList;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.semantics.Representation;

/**
 * The behaviour manager aids analysis of starting populations by recording
 * how many programs are associated with a particular behaviour.
 */
public class BehaviourManager {
    
    private Representation model;
    private ArrayList<CandidateProgram> programs;
    
    /** Creates a new instance of BehaviourManager
     * @param mod The representation
     */
    public BehaviourManager(Representation mod) {
        model = mod;
        programs = new ArrayList<CandidateProgram>();
    }
    
    /**
     * Returns the representation
     * @return The representation
     */
    public Representation getBehaviour() {
        return model;
    }
    
    /**
     * Returns the Frequency of the programs associated with this representation
     * @return The number of programs associated with a particular representation
     */
    public int getFrequency() {
        return programs.size();
    }
    
    /**
     * Adds a program of the same behaviour to the representation manager
     * @param prog The behaviourally equivalent program
     */
    public void addProgram(CandidateProgram prog) {
        programs.add(prog);
    }
    
    /**
     * Returns all the programs associated with a representation
     * @return The collection of programs
     */
    public ArrayList<CandidateProgram> getPrograms() {
        return programs;
    }    
}
