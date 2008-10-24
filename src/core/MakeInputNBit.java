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

import java.util.ArrayList;
import java.io.*;

/**
 * Use this file to make input files for larger boolean problems
 * @author Lawrence Beadle
 */
public class MakeInputNBit {
    
    /**
     * Makes inputs for larger Boolean problems
     * @param args - There are no command line parameters to input
     */
    public static void main(String args[]) {
        
        System.out.println("SAMPLE CREATION STARTING");
        String start = "1111111";
        int total = Integer.parseInt(start, 2);
        System.out.println("Total = " + total);
        System.out.println("Score out of: " + total);
        File baseDir = new File("U:/home/JavaProjects/EpochX1_0");
        
        ArrayList<String> output = new ArrayList<String>();
        for(int i = 0; i<total; i++) {
            String input = "0000000" + Integer.toBinaryString(i);
            input = input.substring(input.length()-7, input.length());
            output.add(input + "\n");
            System.out.println("PROGRESS = " + i + " out of " + total);
        }
        
        System.out.println("READINGS LOADED = " + output.size());
        
        System.out.println("DUMPING TO FILE - input7bit.txt");
        
        FileManip.doOutput(baseDir, output, "input7bit.txt", false);
        
        System.out.println("N BIT MUX SAMPLE INPUT LOADED");
        
    }
    
}
