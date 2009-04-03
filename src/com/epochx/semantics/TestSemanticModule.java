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
package com.epochx.semantics;
import com.epochx.core.initialisation.RampedHalfAndHalfInitialiser;
import com.epochx.core.representation.*;
import com.epochx.example.regression.RegressionModelTomDoNotTouch;

import java.util.*;

public class TestSemanticModule {

	public static void main(String[] args) {
		
		RegressionModelTomDoNotTouch model = new RegressionModelTomDoNotTouch();
		RegressionSemanticModule semMod = new RegressionSemanticModule(model.getTerminals(), model);
		RampedHalfAndHalfInitialiser rhh = new RampedHalfAndHalfInitialiser(model);
		
		semMod.start();
		
		// pull out first population
		List<CandidateProgram> firstGen = rhh.getInitialPopulation();
		List<RegressionRepresentation> firstRep = new ArrayList<RegressionRepresentation>();
		List<CandidateProgram> secondGen = new ArrayList<CandidateProgram>();
		List<RegressionRepresentation> secondRep = new ArrayList<RegressionRepresentation>();

		// generate 1st behaviours
		for(CandidateProgram c: firstGen) {
			System.out.println("1 - " + c.getRootNode());
			RegressionRepresentation regRep1 = (RegressionRepresentation) semMod.codeToBehaviour(c);
			firstRep.add(regRep1);
			System.out.println("2 - " + regRep1);			
			CandidateProgram cp = semMod.behaviourToCode(regRep1);			
			secondGen.add(cp);
			System.out.println("3 - " + cp.getRootNode());
			RegressionRepresentation regRep2 = (RegressionRepresentation) semMod.codeToBehaviour(cp);
			secondRep.add(regRep2);
			System.out.println("4 - " + regRep2);
		}
		
		// test for equality
//		int size = firstRep.size();
//		for(int i = 0; i<size; i++) {
//			if(!firstRep.get(i).equals(secondRep.get(i))) {
//				System.out.println("1 - " + firstGen.get(i).getRootNode());
//				System.out.println("2 - " + firstRep.get(i));
//				System.out.println("3 - " + secondGen.get(i).getRootNode());
//				System.out.println("4 - " + secondRep.get(i));
//			}
//		}

		semMod.stop();
		System.exit(777);

	}

}
