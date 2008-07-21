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

package LineageAnalysis;

/**
 * The Geneology class represents the two parents and two children involved in a
 * crossover that takes place in a specific generation
 * @author Lawrence Beadle 21 March 2008
 */
public class Geneology {
    
    private int parent1, parent2, child1, child2, generation;
    
    /**
     * Geneology constructor
     * @param p1 The identityHashKey of parent 1
     * @param p2 The identityHashKey of parent 2
     * @param c1 The identityHashKey of child 1
     * @param c2 The identityHashKey of child 2
     * @param gen the geenration in which the crossvoer occurs
     */
    public Geneology(int p1, int p2, int c1, int c2, int gen) {
        parent1 = p1;
        parent2 = p2;
        child1 = c1;
        child2 = c2;
        generation = gen;
    }
    
    /**
     * Returns the identityHashKey of the 1st parent
     * @return The identityHashKey of the first parent
     */
    public int getP1() {
        return parent1;
    }
    
    /**
     * Returns the identityHashKey of the 2nd parent
     * @return The identityHashKey of the second parent
     */
    public int getP2() {
        return parent2;
    }
    
    /**
     * Returns the identityHashKey of the 1st child
     * @return The identityHashKey of the 1st child
     */
    public int getC1() {
        return child1;
    }
    
    /**
     * Returns the identityHashKey of the 2nd child
     * @return The identityHashKey of the 2nd child
     */
    public int getC2() {
        return child2;
    }
    
    /**
     * Returns the generation in which the crossover took place
     * @return the generation number
     */
    public int getGeneration() {
        return generation;
    }
}
