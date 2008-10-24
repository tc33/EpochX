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

import java.io.*;
import java.util.*;
import java.net.InetAddress;

/**
 * Creates a batch run system for executing multiple runs of a genetic program
 * @author Lawrence Beadle
 */
public class Main {
    
    /**
     * Creates a new instance of Main 
     * @param args args
     */
    public static void main(String[] args) {
        
        // measure time in
        long timeIn = System.currentTimeMillis();
        
        // PAR - Set model to the model you want to use
        String modelName = "Majority5";
        
        String thisHost = "";
        try {
        InetAddress chose = InetAddress.getLocalHost();
        thisHost = chose.getHostName();
        } catch(java.net.UnknownHostException e) {
            System.out.println("UNKOWN HOST");
            System.exit(666);
        }
        
        System.out.println(thisHost);
        
        // PAR - Set your base directory
        File baseDir = new File("U:/home/JavaProjects/EpochX/trunk");
        //File baseDir = new File("D:/JavaProjects/EpochX");
        //File baseDir = new File("/pi/home/cug/lb212/" + thisHost + "/EPOCHX");
        //File baseDir = new File("/home/lb212/EPOCHX10");
        
        // PAR - Set basic GP parameters - See parameters document on www.epochx.com for details
        int runs = 1;
        int gens = 10;
        int elites = 50;
        int reprod = 50;
        int popSize = 500;
        double pCross = 0.9;
        double pMut = 0.0;
        
        // PAR - Choose crossover scoring and selection methods
        // 1 = single point / 2 = random point / 3 = koza standard
        int cOMethod = 3;
        // scoring method 1 = input/output / 2 = semantic
        int sMeth = 2;
        // 1 = total tournament / 2 = fitness proportionate / 3 = T7 tournament / 4  = ranked / 5 = T3 tournament
        int sOMethod = 3;
        
        // PAR - Choose staring population tyep
        // Full = full / Grow = grow / H+H = half and half / RH+H = ramped half and half
        // SDIB = state differential BOOLEAN / SDIA = state differential ANT
        String genType = "RH+H";
        
        // PAR - Set to use semantic state checked crossover
        boolean cChecker = false;
        
        // PAR - Set to use semantic state checked mutation
        boolean mChecker = false;
        
        // delete old output files
        FileManip.deleteOld(baseDir, "GP-State-Monitor-Output.txt");
        FileManip.deleteOld(baseDir, "GP-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-State-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-PA-DEPTH-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-PA-NODE-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-PA-TERMINAL-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-PA-DISTINCT-TERMINAL-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-PA-Length-Stats-Output.txt");
        FileManip.deleteOld(baseDir, "GP-Program-Output.txt");
        
        // set up the GP model
        GPModel model = new GPModel();
        
        // set lineage dump boolean
        // WARNING - THIS IS EXPERIMENTAL AND TIME CONSUMING - LEAVE AS FALSE UNLESS
        // YOU KNOW WHAT YOU'RE DOING.
        model.setLineageDump(false);
        
        // PAR - Specify name of input file in EpochX directory
        model.loadRawData(baseDir, "input5bit.txt");
        
        // do GP Run
        model.doGPRun(modelName, runs, popSize, genType, gens, elites, reprod, pCross, pMut, cChecker, mChecker, cOMethod, sOMethod, sMeth, baseDir);
        
        System.out.println("RUN COMPLETED");
        
        // measure time out
        long timeOut = System.currentTimeMillis();
        long totalTime = timeOut - timeIn;
        // print time details to file.
        ArrayList<String> timeDetails = new ArrayList<String>(3);
        timeDetails.add("TIME IN = " + timeIn + "\n");
        timeDetails.add("TIME OUT = " + timeOut + "\n");
        timeDetails.add("TOTAL TIME = " + totalTime);
        FileManip.deleteOld(baseDir, "timeDetails.txt");
        FileManip.doOutput(baseDir, timeDetails, "timeDetails.txt", true);
        
        System.exit(0);        
    }    
}
