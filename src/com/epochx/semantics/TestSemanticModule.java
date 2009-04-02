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
		RampedHalfAndHalfInitialiser rhh = new RampedHalfAndHalfInitialiser(model, semMod);
		
		semMod.start();
		
		// pull out first population
		List<CandidateProgram> firstGen = rhh.getInitialPopulation();

		for(CandidateProgram c: firstGen) {
			System.out.println(c.getRootNode());
			semMod.codeToBehaviour(c);
		}

		semMod.stop();
		System.exit(777);

	}

}
