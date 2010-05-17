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
package org.epochx.gr.model.java;

import java.awt.*;
import java.util.*;

import org.epochx.gr.model.*;
import org.epochx.gr.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;


public class SantaFeTrail extends GRModel {

	public static final String GRAMMAR_STRING = 
		  "<code> ::= <line> | <code> <line>\n"
		+ "<line> ::= <expr>\n"
		+ "<expr> ::= <condition> | <opcode>\n"
		+ "<condition> ::= if(ant.isFoodAhead()){ <opcode> }else{ <opcode> }\n"
		+ "<opcode> ::=  ant.turnLeft(); | ant.turnRight(); | ant.move();\n";
	
	private Grammar grammar;
	
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
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
		landscape.setFoodLocations(new ArrayList<Point>(Arrays.asList(foodLocations)));
		ant.reset(600, landscape);
		
		// Construct argument arrays.
		String[] argNames = {"ant"};
		Object[] argValues = {ant};
		
		// Evaluate multiple times until all time moves used.
		Executor executor = new JavaEvaluator();
		while(ant.getMoves() < ant.getMaxMoves()) {
			executor.exec(program.getSourceCode(), argNames, argValues);
		}

		// Calculate score.
		double score = (double) (foodLocations.length - ant.getFoodEaten());
		
		return score;
	}

	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
