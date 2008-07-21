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

import net.sf.javabdd.BDD;
import java.util.ArrayList;

/**
 * This class acts as a holder for the different forms of Behavioural Representations
 * @author Lawrence Beadle
 */
public class BehaviourRepresentation {
    
    // representation type 1 = BDD // 2 = ArrayList<String>
    private int repType = 0;
    private BDD thisBDDRep;
    private ArrayList<String> thisArrayListRep;
    
    /**
     * Creates a BehvaiourRepresentation object based on a BDD
     * @param bDDRep the BDD to be represented
     */
    public BehaviourRepresentation(BDD bDDRep) {
        thisBDDRep = bDDRep;
        repType = 1;
    }
    
    /**
     * Creates a BehaviourRepresentation Object which is based on an ArrayList<String>
     * @param arrayListRep the ArrayList to be represented
     */
    public BehaviourRepresentation(ArrayList<String> arrayListRep) {
        thisArrayListRep = arrayListRep;
        repType = 2;
    }
    
    /**
     * Returns the type of the Behaviour Stored
     * 1 = BDD
     * 2 = ArrayList<String>
     * @return An int representing the type of the Object stored
     */
    public int getRepType() {
        return repType;
    }
    
    /**
     * Returns the BDD representation stored
     * @return The BDD representation
     */
    public BDD getBDD() {
        return thisBDDRep;
    }
    
    /**
     * Returns the ArrayList model stored
     * @return The ArrayList<String> model
     */
    public ArrayList<String> getArrayList() {
        return thisArrayListRep;
    }
    
    /**
     * Compares two BehaviourRepresentation Objects
     * @param toTest the BehvaiourRepresentation Object to compare this one to
     * @return TRUE if BEHAVIOURALLY EQUIVALENT
     */
    public boolean equals(BehaviourRepresentation toTest) {
        boolean value = false;
        if(toTest.getRepType()==1) {
            if(toTest.getBDD().equals(thisBDDRep)) {
                return true;
            } else {
                return false;
            }
        } else if(toTest.getRepType()==2) {
            if(toTest.getArrayList().equals(thisArrayListRep)) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("INVALID REPRESENTATION DETECTED!!!");
        }        
        return value;
    }
    
    public boolean isTautology() {
        if(repType==1) {
            if(thisBDDRep.satCount()==1 || thisBDDRep.satCount()==0) {
                return true;
            } else {
                return false;
            }
        } else if(repType==2) {
            for(String t: thisArrayListRep) {
                if(t.equalsIgnoreCase("M")) {
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("INVALID REP TYPE ERROR IN BEHAVIOUR REPRESENTATION");
            return false;
        }
    }
}
