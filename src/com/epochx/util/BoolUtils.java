/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
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
package com.epochx.util;


/**
 * This class provides functionality to translate input strings into boolean[] representations.
 */
public class BoolUtils {
    
    /**
     * Translates a String to a boolean[]
     * @param input The input string of 1's and 0's
     * @return A boolean[] representing the input String
     */
    public static boolean[] toArray(String input) {        
        int len = input.length();
        boolean[] transData = new boolean[len];
        for(int i = 0; i<len; i++) {
            if(input.charAt(i)=='1') {
                transData[i] = true;
            } else {
                transData[i] = false;
            }
        }        
        return transData;        
    }
    
    public static boolean[][] generateBoolSequences(int noBits) {
    	int noInputs = (int) Math.pow(2, noBits);
    	
    	boolean[][] inputs = new boolean[noInputs][noBits];
    	
    	for(int i=0; i<noBits; i++) {
    		int rep = (int) Math.pow(2, i+1);
    		
    		for(int j=0; j<noInputs; j++) {
    			inputs[j][i] = (j % rep) > Math.floor(rep / 2)-1;
    		}
    	}
    	
    	return inputs;
    }
    
    /**
     * Provides an alternative to generateBoolSequences(int) particularly for 
     * large numbers of bits greater than 31 which that method will struggle 
     * with.
     * @param noBits
     * @param index
     * @return
     */
    public static boolean[] generateBoolSequence(int noBits, long index) {
    	boolean[] inputs = new boolean[noBits];
    	
    	for(int i=0; i<noBits; i++) {
    		int rep = (int) Math.pow(2, i+1);
    		
    		inputs[i] = (index % rep) > Math.floor(rep / 2)-1;
    	}
    	
    	return inputs;
    }
}
