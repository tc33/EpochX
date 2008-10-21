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

package Models;

import Majority9.*;
import CoreN.*;

/**
 * An implimentation of the 11 bit Multiplexer for GP
 * @author Lawrence Beadle
 */
public class Majority9 extends GPCoreCode {
    
    /**
     * Creates a new instance of the 11 bit multiplexer model
     */
    public Majority9() {
        
        // instantiate scorer object
        super.scorer = new Scorer9maj();
        
        // instantiate syntax objects
        Syntax9maj syn = new Syntax9maj();
        super.eStart = syn.getEStart();
        super.syntax = syn.getSyntax();
        super.synterms = syn.getTerms();
        super.functions = syn.getFunctions();
        super.terminals = syn.getTerminals();
        
        // instantiate translater object
        super.semanticMod = new GPEquivalence9maj();
        
    }
    
}
