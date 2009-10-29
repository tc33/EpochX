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

package com.epochx.initialisation.analysis;

import java.util.ArrayList;
import java.util.List;

import net.sf.javabdd.*;
import java.lang.reflect.*;
import com.epochx.example.regression.*;
import com.epochx.initialisation.RegressionHybridSemanticallyDrivenInitialiser;
import com.epochx.op.initialisation.*;
import com.epochx.representation.*;
import com.epochx.semantics.SemanticModule;

/**
 * Runs a full analysis of a starting population for a specific model for varying sizes
 */
public class MainISpeedAnalysis {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // CODE TO ANALYSE STARTING POPULATIONS
        System.out.println("STARTING INITIALISATION ANALYSIS - PROGRAM STARTED");
        
        // decide which model
        RegressionModelCUBIC model = new RegressionModelCUBIC();
        model.setPopulationSize(500);
        SemanticModule<Double> semMod = model.getSemanticModule();

        ArrayList<CandidateProgram<Double>> newPop;
        
        // SET FIRST COUNT
        long fC = System.currentTimeMillis();
        
        // print to console
        System.out.println("TIME IN: " + fC);
        
        // do 100 runs of each type and pop size
        for (int i = 0; i < 100; i++) {

            // generate population              
        	RegressionHybridSemanticallyDrivenInitialiser initialiser = new RegressionHybridSemanticallyDrivenInitialiser(model, semMod);
            List<CandidateProgram<Double>> testPop = initialiser.getInitialPopulation();

        }
        
        // final count
        long lC = System.currentTimeMillis();
        System.out.println("TIME OUT: " + lC);
        long time = (lC-fC)/100;
        System.out.println("TIME TAKEN: " + time);
        
        System.out.println("ANALYSIS COMPLETE!");
    }
}

    