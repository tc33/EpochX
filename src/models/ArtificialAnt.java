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

package models;

import com.epochx.aasf.*;

import core.*;

/**
 * An implementation of the 4 Parity model in GP
 * @author Lawrence Beadle
 */
public class ArtificialAnt extends GPCoreCode {
    
    /**
     * Creates a new instance of the Even4Parity model
     */
    public ArtificialAnt() {
        
        // instantiate scorer object
        super.scorer = new ScorerAA();
        
        // instantiate syntax objects
        SyntaxAA syn = new SyntaxAA();
        super.eStart = syn.getEStart();
        super.syntax = syn.getSyntax();
        super.synterms = syn.getTerms();
        super.functions = syn.getFunctions();
        super.terminals = syn.getTerminals();
        
        // instantiate translater object
        super.semanticMod = new GPEquivalenceAA();
        
    }
    
}