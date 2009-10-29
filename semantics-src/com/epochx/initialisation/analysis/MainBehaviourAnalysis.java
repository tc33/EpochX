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
import java.io.File;
import net.sf.javabdd.*;
import java.lang.reflect.*;

import com.epochx.example.artificialant.ArtificialAntSantaFe;
import com.epochx.example.regression.RegressionModelCUBIC;
import com.epochx.op.initialisation.*;
import com.epochx.representation.*;
import com.epochx.semantics.*;
import com.epochx.util.FileManip;

/**
 * Runs a full analysis of a starting population for a specific model for
 * varying sizes.
 */
public class MainBehaviourAnalysis {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// CODE TO ANALYSE STARTING POPULATIONS
		System.out.println("STARTING POP ANALYSIS - PROGRAM STARTED");

		// set up model and initialiser -- configure up here
		ArtificialAntSantaFe model = new ArtificialAntSantaFe();
		String modelName = "AASF";
		model.setInitialMaxDepth(4);
		SemanticModule semMod = new AntSemanticModule(model.getTerminals(), model, model.getAnt(), model.getAntLandScape());
		FullInitialiser initialiser = new FullInitialiser(model);
		String genType = "FULL";
		File place = new File("Results");
		

		// set up the different sizes of population to be analysed
		ArrayList<Integer> sizes = new ArrayList<Integer>();

		sizes.add(new Integer(500));
		sizes.add(new Integer(1000));
		sizes.add(new Integer(1500));
		sizes.add(new Integer(2000));
		sizes.add(new Integer(2500));
		sizes.add(new Integer(3000));
		sizes.add(new Integer(3500));
		sizes.add(new Integer(4000));
		sizes.add(new Integer(4500));
		sizes.add(new Integer(5000));
		/**
		 * sizes.add(new Integer(5500)); sizes.add(new Integer(6000));
		 * sizes.add(new Integer(6500)); sizes.add(new Integer(7000));
		 * sizes.add(new Integer(7500)); sizes.add(new Integer(8000));
		 * sizes.add(new Integer(8500)); sizes.add(new Integer(9000));
		 * sizes.add(new Integer(9500)); sizes.add(new Integer(10000));
		 * sizes.add(new Integer(10500)); sizes.add(new Integer(11000));
		 * sizes.add(new Integer(11500)); sizes.add(new Integer(12000));
		 * sizes.add(new Integer(12500)); sizes.add(new Integer(13000));
		 * sizes.add(new Integer(13500)); sizes.add(new Integer(14000));
		 * sizes.add(new Integer(14500)); sizes.add(new Integer(15000));
		 * **/

		// set up equivalence storage
		ArrayList<Representation> behaviours;
		ArrayList<CandidateProgram> progs, newPop;
		ArrayList<String> dump;
		int syntaxSame, semanticSame;
		Representation specimin;

		for (Integer size : sizes) {

			// progress monitor
			System.out.println("Working on: " + size.toString());
			
			model.setPopulationSize(size);

			dump = new ArrayList<String>();
			dump.add("Experiment: " + genType + " - " + size.toString() + "-" + modelName + "\n\n");
			dump.add("Pop_ID\tSyntax_Same\tSyntax_Unique\tSemantic_Same\tSemantic_Unique\tPopulation\n");

			// do 100 runs of each type and pop size
			for (int i = 0; i < 100; i++) {

				// generate population
				newPop = (ArrayList<CandidateProgram>) initialiser.getInitialPopulation();

				// start equivalence module
				behaviours = new ArrayList<Representation>();
				progs = new ArrayList<CandidateProgram>();
				semMod.start();
				syntaxSame = 0;
				semanticSame = 0;

				for (CandidateProgram testProg : newPop) {

					// check for syntax equivalence
					if (progs.contains(testProg)) {
						syntaxSame++;
					} else {
						progs.add(testProg);
					}

					// check for semantic equivalence
					specimin = semMod.codeToBehaviour(testProg);
					boolean marker = false;
					for (Representation b : behaviours) {
						if (b.equals(specimin)) {
							semanticSame++;
							marker = true;
							break;
						}
					}
					// add if not found
					if (!marker) {
						behaviours.add(specimin);
					}
				}

				// store run details
				dump.add(i + "\t" + syntaxSame + "\t" + progs.size() + "\t"
						+ semanticSame + "\t" + behaviours.size() + "\t"
						+ size.toString() + "\n");

				// close equivalence module
				semMod.stop();

				// dump everything and force GC
				newPop = null;
				behaviours = null;
				progs = null;
				semMod = null;
				System.gc();

			}

			// dump to file
			String name = genType + "-B+S-" + size.toString() + "-" + modelName + ".txt";
			FileManip.doOutput(place, dump, name, false);

			// dump files and force gc
			dump = null;
			System.gc();
		}

		// final output
		System.out.println("STARTING POPS ANALYSIS COMPLETE!");
	}
}
