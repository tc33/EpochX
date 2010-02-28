/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.ge.model.epox.ant;

import java.awt.*;
import java.util.*;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.gp.representation.FunctionParser;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.stats.RunStatField;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.EpoxEvaluator;
import org.epochx.tools.grammar.Grammar;

/**
 *
 */
public class SantaFeTrail extends GEAbstractModel {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= IF-FOOD-AHEAD( <node> , <node> ) " +
					"| SEQ2( <node> , <node> ) " +
					"| SEQ3( <node> , <node> , <node> )\n" +
		"<terminal> ::= MOVE() | TURN-LEFT() | TURN-RIGHT()\n";
	
	private Grammar grammar;
	
	private FunctionParser parser;
	private EpoxEvaluator evaluator;
	
	private AntLandscape landscape;
	private Ant ant;
	
	private static final Point[] foodLocations = {
		new Point(1,0), new Point(2,0), new Point(3,0), new Point(3,1),
		new Point(3,2), new Point(3,3), new Point(3,4), new Point(3,5),
		new Point(4,5), new Point(5,5), new Point(6,5), new Point(8,5),
		new Point(9,5), new Point(10,5), new Point(11,5), new Point(12,5),
		new Point(12,6), new Point(12,7), new Point(12,8), new Point(12,9),
		new Point(12,11), new Point(12,12), new Point(12,13), new Point(12,14),
		new Point(12,17), new Point(12,18), new Point(12,19), new Point(12,20),
		new Point(12,21), new Point(12,22), new Point(12,23), new Point(11,24),
		new Point(10,24), new Point(9,24), new Point(8,24), new Point(7,24),
		new Point(4,24), new Point(3,24), new Point(1,25), new Point(1,26),
		new Point(1,27), new Point(1,28), new Point(2,30), new Point(3,30),
		new Point(4,30), new Point(5,30), new Point(7,29), new Point(7,28),
		new Point(8,27), new Point(9,27), new Point(10,27), new Point(11,27),
		new Point(12,27), new Point(13,27), new Point(14,27), new Point(16,26),
		new Point(16,25), new Point(16,24), new Point(16,21), new Point(16,20),
		new Point(16,19), new Point(16,18), new Point(17,15), new Point(20,14),
		new Point(20,13), new Point(20,10), new Point(20,9), new Point(20,8),
		new Point(20,7), new Point(21,5), new Point(22,5), new Point(24,4),
		new Point(24,3), new Point(25,2), new Point(26,2), new Point(27,2),
		new Point(29,3), new Point(29,4), new Point(29,6), new Point(29,9),
		new Point(29,12), new Point(28,14), new Point(27,14), new Point(26,14),
		new Point(23,15), new Point(24,18), new Point(27,19), new Point(26,22),
		new Point(23,23)
	};
	
	public SantaFeTrail() {
		grammar = new Grammar(GRAMMAR_STRING);
		
		landscape = new AntLandscape(new Dimension(32, 32), null);
		ant = new Ant(600, landscape);
		
		// Construct the evaluator to use.
		parser = new FunctionParser();
		evaluator = new EpoxEvaluator(parser);
		
		//setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		setNoRuns(100);
		setNoElites(50);
		setNoGenerations(100);
		setPopulationSize(500);
		setMaxProgramDepth(15);
		setMaxInitialProgramDepth(15);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(50);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		// Reset the ant.
		landscape.setFoodLocations(new ArrayList<Point>(Arrays.asList(foodLocations)));
		ant.reset(600, landscape);

		//TODO Look at a better solution to the ant parameter problem using executors.
		parser.setAnt(ant);
		
		if (program.isValid()) {
			// Evaluate multiple times until all time moves used.
			while(ant.getMoves() < ant.getMaxMoves()) {
				evaluator.eval(program.getSourceCode(), new String[]{}, new Object[]{});
			}
		}

		// Calculate score.
		double score = (double) (foodLocations.length - ant.getFoodEaten());

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
