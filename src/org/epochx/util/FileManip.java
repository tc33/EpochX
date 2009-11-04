/*  
 *  Copyright 2007-2008 Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epochx.util;

import java.util.*;
import java.io.*;

/**
 * The File Manip class controls all text output and input file requirements
 */
public class FileManip {
    
    /**
     * This method controls sending information files
     * @param cDir A file object representing the directory to send the output files to.
     * @param infoToSave An ArrayList<String> of the information to be saved to the file.  Each element in the 
     * ArrayList represents a new line in the output file.
     * @param fileName A String which will be the name of the output file.
     * @param append True if you want information to be added to the file and false if you want the 
     * file to be overwritten
     */
    public static void doOutput(File cDir, ArrayList<String> infoToSave, String fileName, boolean append) {
        
        File saveNow = new File(cDir, fileName);
        
        try {
            FileWriter dataIn = new FileWriter(saveNow, append);
            for(String lineToSave: infoToSave) {
                dataIn.write(lineToSave);
                dataIn.flush();
            }
            dataIn.close();
        } catch (java.io.IOException e) {
            System.out.println("ERROR CREATING FILE WRITER " + e);
        }
        
    }
    
    /**
     * Loads an input file
     * @param fileToLoad the input file to load.
     * @return An ArrayList<String> representing the input data.  Each element in the ArrayList represents a 
     * line of data from the input file
     */
    public static List<String> loadInput(File fileToLoad) {
        
        List<String> data = new ArrayList<String>();
        
        try {
            FileReader readFile = new FileReader(fileToLoad);
            BufferedReader read = new BufferedReader(readFile);
            while(true) {                
                String line = read.readLine();
                if(line==null) {
                    break;
                } else {
                    data.add(line);
                }                
            }
            read.close();        
        } catch (java.io.IOException e) {
            System.out.println("ERROR READING FILE: " + e);
        }
        
        return data;
        
    }
    
    /**
     * This method deletes a file
     * @param cDir A File object representing the location fo the file to be deleted
     * @param fName The name of the file to be deleted
     */
    public static void deleteOld(File cDir, String fName) {
        
        File deleteNow = new File(cDir, fName);
        deleteNow.delete();
        
    }
    
}