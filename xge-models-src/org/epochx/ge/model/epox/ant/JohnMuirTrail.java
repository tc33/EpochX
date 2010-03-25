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
package org.epochx.ge.model.epox.ant;

import java.awt.*;
import java.util.*;

import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.gp.representation.FunctionParser;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.EpoxEvaluator;
import org.epochx.tools.grammar.Grammar;

public class JohnMuirTrail extends GEAbstractModel {

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
		new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0),
		new Point(5,0), new Point(6,0), new Point(7,0), new Point(8,0),
		new Point(9,0), new Point(10,0), new Point(10,1), new Point(10,2),
		new Point(10,3), new Point(10,4), new Point(10,5), new Point(10,6),
		new Point(10,7), new Point(10,8), new Point(10,9), new Point(10,10),
		new Point(9,10), new Point(8,10), new Point(7,10), new Point(6,10),
		new Point(5,10), new Point(4,10), new Point(3,10), new Point(3,9),
		new Point(3,8), new Point(3,7), new Point(3,6), new Point(3,5),
		new Point(2,5), new Point(1,5), new Point(0,5), new Point(31,5),
		new Point(30,5), new Point(29,5), new Point(28,5), new Point(27,5),
		new Point(26,5), new Point(25,5), new Point(24,6), new Point(24,7),
		new Point(24,8), new Point(24,9), new Point(24,10), new Point(23,11),
		new Point(22,11), new Point(21,11), new Point(20,11), new Point(19,11),
		new Point(18,12), new Point(18,13), new Point(18,14), new Point(18,15),
		new Point(18,16), new Point(18,17), new Point(18,20), new Point(18,21),
		new Point(18,22), new Point(18,23), new Point(18,24), new Point(18,25),
		new Point(17,27), new Point(16,27), new Point(15,27), new Point(14,27),
		new Point(13,27), new Point(12,27), new Point(10,27), new Point(9,27),
		new Point(8,27), new Point(7,27), new Point(4,27), new Point(4,26),
		new Point(4,25), new Point(4,24), new Point(5,22), new Point(7,21), 
		new Point(8,18), new Point(11,17), new Point(12,15), new Point(15,14), 
		new Point(14,12), new Point(11,11), new Point(12,8), new Point(14,7), 
		new Point(15,4)
	};
	
	public JohnMuirTrail() {
		grammar = new Grammar(GRAMMAR_STRING);
		
		landscape = new AntLandscape(new Dimension(32, 32), null);
		ant = new Ant(100, landscape);
		
		// Construct the evaluator to use.
		parser = new FunctionParser();
		evaluator = new EpoxEvaluator(parser);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		// Reset the ant.
		landscape.setFoodLocations(new ArrayList<Point>(Arrays.asList(foodLocations)));
		ant.reset(100, landscape);

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
	
	/*public static void main(String[] args) {
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				boolean food = false;
				
				for (int k=0; k<foodLocations.length; k++) {
					int x = foodLocations[k].x;
					int y = foodLocations[k].y;
					
					if (y == i && x == j) {
						food = true;
						break;
					}
				}
				
				if (food) {
					System.out.print('X');
				} else {
					System.out.print('.');
				}
			}
			System.out.println();
		}
	}*/
}
