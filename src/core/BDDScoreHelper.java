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

import net.sf.javabdd.*;
import java.util.*;

/**
 * The BDD score helper is a common helper file to score Boolean programs semantically
 * @author Lawrence Beadle 21 March 2008
 */
public class BDDScoreHelper {
    
    /**
     * Calculates the % difference between to two Boolean program models.
     * @param thisProg The Boolean program to be tested
     * @param bestProg The Boolean ideal solution
     * @param semanticMod The semantic module to be used
     * @return The percentage difference between the two models with 0 representing no difference and 100 representing completely different
     */
    public double doScore(ArrayList<String> thisProg, ArrayList<String> bestProg, SemanticModule semanticMod) {
        semanticMod.start();
        double score;
        BDD thisRep = semanticMod.createRep(thisProg).getBDD();
        BDD idealRep = semanticMod.createRep(bestProg).getBDD();
        BDD diffRep1 = thisRep.and(idealRep.not());
        BDD diffRep2 = idealRep.and(thisRep.not());
        BDD finalDiff = diffRep1.or(diffRep2);
        score = finalDiff.satCount() * 100;
        semanticMod.finish();
        return score;
    }    
}