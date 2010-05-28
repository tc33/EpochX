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
package org.epochx.gr.model.epox;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.epochx.gp.representation.EpoxParser;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

/**
 * This class provides the general functionality of ant trail models, but does 
 * not itself define a trail. 
 */
public abstract class AntTrail extends GRModel {

	public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= IF-FOOD-AHEAD( <node> , <node> ) " +
					"| SEQ2( <node> , <node> ) " +
					"| SEQ3( <node> , <node> , <node> )\n" +
		"<terminal> ::= MOVE() | TURN-LEFT() | TURN-RIGHT()\n";
	
	private EpoxParser parser;
	
	private EpoxInterpreter interpreter;
	
	private AntLandscape landscape;
	private Ant ant;
	
	private List<Point> foodLocations;
	
	private final int allowedTimeSteps;
	
	public AntTrail(final Point[] foodLocations, final Dimension landscapeSize, final int allowedTimeSteps) {
		this.foodLocations = new ArrayList<Point>(Arrays.asList(foodLocations));		
		this.allowedTimeSteps = allowedTimeSteps;
		
		landscape = new AntLandscape(landscapeSize, null);
		ant = new Ant(allowedTimeSteps, landscape);
		
		setGrammar(new Grammar(GRAMMAR_STRING));
		
		// Construct the interpreter to use.
		parser = new EpoxParser();
		interpreter = new EpoxInterpreter(parser);
	}
	
	@Override
	public double getFitness(final CandidateProgram p) {
		final GRCandidateProgram program = (GRCandidateProgram) p;
		
		// Reset the ant.
		landscape.setFoodLocations(foodLocations);
		ant.reset(allowedTimeSteps, landscape);

		//TODO Look at a better solution to the ant parameter problem using interpreters.
		parser.setAnt(ant);
		
		// Evaluate multiple times until all time moves used.
		while(ant.getTimesteps() < ant.getMaxMoves()) {
			try {
				interpreter.eval(program.getSourceCode(), new String[]{}, new Object[]{});
			} catch (MalformedProgramException e) {
				// Stop evaluation and give a bad score.
				break;
			}
		}

		// Calculate score.
		return (double) (foodLocations.size() - ant.getFoodEaten());
	}

}
