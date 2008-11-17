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

package com.epochx.startingpopanalysis;

import core.*;
import java.util.ArrayList;
import net.sf.javabdd.*;
import java.lang.reflect.*;

/**
 * Runs a full analysis of a starting population for a specific model for varying sizes
 * @author Lawrence Beadle
 */
public class MainISpeedAnalysis {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // CODE TO ANALYSE STARTING POPULATIONS
        System.out.println("STARTING INITIALISATION ANALYSIS - PROGRAM STARTED");
        
        // decide which model
        String modelName = "ArtificialAnt";
        String genType = "RH+H";
        
        // set up Genetic Program instance - USING REFLECTION
        GPRunBasic genProg = null;
        try {
            genProg = (GPRunBasic) Class.forName("Models." + modelName).getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (ClassNotFoundException e) {
            System.out.println("CLASS NOT FOUND");
        } catch (NoSuchMethodException f) {
            System.out.println("NO SUCH METHOD");
        } catch (InstantiationException z) {
            System.out.println("COULD NOT INSTANTIATE CLASS");
        } catch (IllegalAccessException h) {
            System.out.println("ILLEGAL ACCESS EXCEPTION");
        } catch (InvocationTargetException k) {
            System.out.println("INVOCATION EXCEPTION");
        } catch (ClassCastException l) {
            System.out.println(l.toString());
        }

        if (genProg == null) {
            System.out.println("REFLECT FAILED TO LOAD FILE: " + modelName);
        }

        ArrayList<ArrayList<String>> newPop;
        
        // SET FIRST COUNT
        long fC = System.currentTimeMillis();
        
        // print to console
        System.out.println("TIME IN: " + fC);
        
        // do 100 runs of each type and pop size
        for (int i = 0; i < 100; i++) {

            // generate population                 
            newPop = genProg.buildFirstPop(500, genType);

        }
        
        // final count
        long lC = System.currentTimeMillis();
        System.out.println("TIME OUT: " + lC);
        long time = (lC-fC)/100;
        System.out.println("TIME TAKEN: " + time);
        
        System.out.println("ANALYSIS COMPLETE!");
    }
}

    