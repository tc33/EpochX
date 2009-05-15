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

import java.io.File;
import java.util.ArrayList;

import com.epochx.util.FileManip;

/**
 * Helper file to combine and make averages of the output statistics from the 
 * starting populations Analyser
 */
public class MakeAverages {
    
    public static void main(String[] args) {
        
        // create file control stuff
        //File dir = new File("D:/JavaProjects/StartingPops6bit/Results");
        File dir = new File("Results");
        String[] files = dir.list();
        System.out.println("NO OF FILES = " + files.length);
        ArrayList<String> filteredFiles = new ArrayList<String>();
                
        // filter the files to desired starting pop generation method
        String testFor = "FULL";
        int noOfFiles = files.length;
        for(int i = 0; i<noOfFiles; i++) {
            String tester = files[i];            
            String part = tester.substring(0, 4);
            if(part.equalsIgnoreCase(testFor)) {
                filteredFiles.add(tester);
                System.out.println(tester);
            }
        }
        
        // create dump arraylist for single averaged readings        
        ArrayList<String> output = new ArrayList<String>();
        output.add("Population\tSyntax_Same\tSyntax_Unique\tSemantic_Same_Min\tSemantic_Same\tSemantic_Same_Max\tSemantic_Unique_Min\tSemantic_Unique\tSemantic_Unique_Max\n");
        
        // read in all files with matching front marker
        for(String name: filteredFiles) {
        	File toLoad = new File("Results/" + name);
            ArrayList<String> dataIn = (ArrayList<String>) FileManip.loadInput(toLoad);
            int[] popID = new int[100];
            int[] syntaxSame = new int[100];
            int[] syntaxUnique = new int[100];
            int[] semanticSame = new int[100];
            int[] semanticUnique = new int[100];
            int[] population = new int[100];
            // for each one pull out raw results and average them out for each of the 4 columns
            // move to fourth line run through data lines and store data
            for(int i = 3; i<103; i++) {
                //System.out.println(dataIn.get(i));
                int[] splits = splitData(dataIn.get(i));
                popID[i-3] = splits[0];
                syntaxSame[i-3] = splits[1];
                syntaxUnique[i-3] = splits[2];
                semanticSame[i-3] = splits[3];
                semanticUnique[i-3] = splits[4];
                population[i-3] = splits[5];
            }
            // make averages for information dump
            double synSameAve = getAverage(syntaxSame);
            double synUniAve = getAverage(syntaxUnique);
            double semSameAve = getAverage(semanticSame);
            double semUniAve = getAverage(semanticUnique);
            int semSameMin = getMin(semanticSame);
            int semSameMax = getMax(semanticSame);
            int semUniMin = getMin(semanticUnique);
            int semUniMax = getMax(semanticUnique);
            
            
            // append this information to info to dump
            output.add(population[0] + "\t" + synSameAve + "\t" + synUniAve + "\t" + semSameMin + "\t" + semSameAve + "\t" + semSameMax + "\t" + semUniMin + "\t" + semUniAve + "\t" + semUniMax + "\n");
        }
        
        // dump to new average file
        String nameX = testFor + "-Averages.txt";
        FileManip.doOutput(dir, output, nameX, false);
        
    }
    
    /**
     * Calculates and returns the average value of a collection of data
     * @param rawData the raw data
     * @return the average of the data
     */
    public static double getAverage(int[] rawData) {
        
        double ave = 0;
        int len = rawData.length;
        
        for(int i =0; i<len; i++) {
            ave = ave + ((double) rawData[i]);
        }
        
        ave = ave / ((double) len);        
        return ave;        
    }
    
    /**
     * Returns the maximum value from a collection of data
     * @param rawData The collection of data
     * @return the maximum value
     */
    public static int getMax(int[] rawData) {
        int max = 0;
        int len = rawData.length;
        for(int i = 0; i<len; i++) {
            if(rawData[i]>max) {
                max = rawData[i];
            }
        }
        return max;
    }
    
    /**
     * Returns the minimum value from a collection of data
     * @param rawData The collection of data
     * @return The minimum value
     */
    public static int getMin(int[] rawData) {
        int min = 1000000;
        int len = rawData.length;
        for(int i = 0; i<len; i++) {
            if(rawData[i]<min) {
                min = rawData[i];
            }
        }
        return min;
    }
    
    /**
     * Splits the raw data ready for analysis - HELPER method
     * @param data The raw text data
     * @return The split data in int[] format
     */
    public static int[] splitData(String data) {
        int[] out = new int[6];
        String[] splits = data.split("\t");
        for(int i = 0; i<6; i++) {
            out[i] = Integer.parseInt(splits[i]);
        }
        return out;
    }
    
}
