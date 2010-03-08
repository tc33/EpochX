/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.example.java.ant;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.FileManip;


public class SantaFeTrail extends GEAbstractModel {

	private Grammar grammar;
	
	private List<Point> foodLocations;
	private AntLandscape landscape;
	private Ant ant;
	
	public SantaFeTrail() {
		grammar = new Grammar(new File("example-grammars/Java/SantaFeTrail.bnf"));
		
		landscape = new AntLandscape(new Dimension(32, 32), null);
		ant = new Ant(600, landscape);
		
		List<String> inputs = FileManip.loadInput(new File("inputsantafe.txt"));
		// create list of food locations
		foodLocations = new ArrayList<Point>();
		for(String i: inputs) {
			if(!i.equalsIgnoreCase("DC")) {
				String[] parts = i.split(":");
				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				Point p = new Point(x, y);
				foodLocations.add(p);
			}
		}
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.RUN_TIME, GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.FITNESS_STDEV, GenerationStatField.LENGTH_AVE, GenerationStatField.LENGTH_STDEV, GenerationStatField.BEST_PROGRAM});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
	
		DepthFirstMapper mapper = new DepthFirstMapper(this);
		mapper.setWrapping(true);
		mapper.setRemovingUnusedCodons(false);
		setMapper(mapper);
		
		setNoRuns(100);
		setNoElites(10);
		setNoGenerations(100);
		setPopulationSize(500);
		setMaxProgramDepth(12);
		setMaxInitialProgramDepth(8);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 3));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(0);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		landscape.setFoodLocations(new ArrayList<Point>(foodLocations));
		ant.reset(600, landscape);
		
		// Construct argument arrays.
		String[] argNames = {"ant"};
		Object[] argValues = {ant};
		
		// Evaluate multiple times until all time moves used.
		Evaluator evaluator = new JavaEvaluator();
		while(ant.getMoves() < ant.getMaxMoves()) {
			evaluator.eval(program.getSourceCode(), argNames, argValues);
		}

		// Calculate score.
		double score = (double) (foodLocations.size() - ant.getFoodEaten());
		
		return score;
	}

	@Override
	public Grammar getGrammar() {
		return grammar;
	}

	public static void main(String[] args) {
		Controller.run(new SantaFeTrail());
	}
}
