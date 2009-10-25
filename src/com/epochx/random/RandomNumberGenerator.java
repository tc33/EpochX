/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.random;

/**
 * 
 */
public interface RandomNumberGenerator {

    /**
     * Get the next int, where n is max
     * @param n max in value
     * @return next random int
     */
    public int nextInt(int n);

    /**
     * Get the next int
     * @return next int
     */
    public int nextInt();

    /**
     * Get the next double 0<=x<1
     * @return next double
     */
    public double nextDouble();

    /**
     * Get the next boolean
     * @return next boolean
     */
    public boolean nextBoolean();

    /**
     * Set the seed
     * @param l seed
     */
    public void setSeed(long l);
    
}