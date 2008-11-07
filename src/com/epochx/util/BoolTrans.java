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

package com.epochx.util;

/**
 * This class provides functionality to translate input strings into boolean[] representations.
 * @author Lawrence Beadle
 */
public class BoolTrans {
    
    /**
     * Translates a String to a boolean[]
     * @param input The input string of 1's and 0's
     * @return A boolean[] representing the input String
     */
    public static boolean[] doTrans(String input) {        
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
}
