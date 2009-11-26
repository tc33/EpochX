/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
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
package org.epochx.semantics;
import java.util.*;

import org.epochx.example.regression.RegressionModelCUBIC;
import org.epochx.op.initialisation.*;
import org.epochx.representation.*;


/**
 * Class written to formally test semantic modules by converting
 * code to behaviour, behaviour to code and code to behaviour again.
 * The 2 behaviours are then compared for equivalence
 */
public class TestSemanticModule {

	public static void main(String[] args) throws CloneNotSupportedException {
		
		RegressionModelCUBIC model = new RegressionModelCUBIC();
		RegressionSemanticModule semMod = new RegressionSemanticModule(model.getTerminals());
		FullInitialiser<Double> rhh = new FullInitialiser<Double>(model);
		model.setPopulationSize(5000);
		
		semMod.start();
		
		// pull out first population
		List<CandidateProgram<Double>> pop = rhh.getInitialPopulation();
		List<Node<Double>> firstGen = new ArrayList<Node<Double>>();
		for (CandidateProgram<Double> p: pop) {
			firstGen.add(p.getRootNode());
		}

		List<RegressionRepresentation> firstRep = new ArrayList<RegressionRepresentation>();
		List<Node<Double>> secondGen = new ArrayList<Node<Double>>();
		List<RegressionRepresentation> secondRep = new ArrayList<RegressionRepresentation>();

		// generate 1st behaviours
		for(Node<Double> c: firstGen) {
			//System.out.println("1 - " + c.getRootNode());
			RegressionRepresentation regRep1 = (RegressionRepresentation) semMod.codeToBehaviour(c);
			firstRep.add((RegressionRepresentation) regRep1.clone());
			//System.out.println("2 - " + regRep1);			
			Node<Double> cp = semMod.behaviourToCode(regRep1);			
			secondGen.add(cp);
			//System.out.println("3 - " + cp.getRootNode());
			RegressionRepresentation regRep2 = (RegressionRepresentation) semMod.codeToBehaviour(cp);
			secondRep.add(regRep2);
			//System.out.println("4 - " + regRep2);
		}
		
		// test for equality
		int size = firstRep.size();
		int inequalities = 0;
		for(int i = 0; i<size; i++) {
			if(!firstRep.get(i).equals(secondRep.get(i))) {
				inequalities++;
				System.out.println("INEQUALITY " + inequalities + "--------------------------------");
				System.out.println("1 - " + firstGen.get(i));
				System.out.println("2 - " + firstRep.get(i));
				System.out.println("3 - " + secondGen.get(i));
				System.out.println("4 - " + secondRep.get(i));
				System.out.println("--------------------------------------------");
			}
		}
		
		System.out.println((firstGen.size()-inequalities) + " CORRECT OUT OF " + firstGen.size());

		semMod.stop();
		System.exit(777);
	}
}
