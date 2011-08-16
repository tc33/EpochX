/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.ge.model.epox;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.epochx.core.*;
import org.epochx.fitness.*;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.interpret.*;
import org.epochx.source.SourceGenerator;
import org.epochx.tools.ant.*;
import org.epochx.tools.grammar.Grammar;

/**
 * Abstract XGE model for ant trail problems in the Epox language. This class
 * provides the general functionality of ant trail models, but does not itself
 * define a trail. Ant trail models for the Epox language should extend this
 * class.
 */
public abstract class AntTrail extends GEModel {

	/**
	 * The grammar that defines valid solution space.
	 */
	public static final String GRAMMAR_STRING = "<function> ::= IF-FOOD-AHEAD( <var> , <function> , <function> ) "
			+ "| SEQ2( <function> , <function> ) "
			+ "| SEQ3( <function> , <function> , <function> ) "
			+ "| MOVE( <var> ) "
			+ "| TURN-LEFT( <var> ) "
			+ "| TURN-RIGHT( <var> )\n"
			+ "<var> ::= ANT";
	
	// Ant components.
	private final AntLandscape landscape;
	private final Ant ant;

	/**
	 * Constructs a new AntTrail with the given <code>FOOD_LOCATIONS</code>
	 * which
	 * must all be positioned at points within the given <code>landscapeSize
	 * </code>.
	 * 
	 * @param foodLocations the points on the landscape which will be occupied
	 *        by food
	 * @param landscapeSize the dimensions of the landscape that the ant will
	 *        operate within
	 * @param allowedTimeSteps the number of moves and turns the ant will be
	 *        allowed to collect the food before timing out.
	 */
	public AntTrail(Evolver evolver, final Point[] foodLocations, final Dimension landscapeSize, final int allowedTimeSteps) {
		super(evolver);
		
		List<Point> foodLocationsList = Arrays.asList(foodLocations);

		landscape = new AntLandscape(landscapeSize, null);
		ant = new Ant(allowedTimeSteps, landscape);

		setGrammar(new Grammar(GRAMMAR_STRING));

		Parameters params = new Parameters(new String[]{"ANT"}, new Object[]{ant});
		
		SourceGenerator<GECandidateProgram> generator = new DepthFirstMapper(evolver);
		Interpreter<GECandidateProgram> interpreter = new EpoxInterpreter<GECandidateProgram>(generator);
		FitnessEvaluator<GECandidateProgram> evaluator = new AntEvaluator<GECandidateProgram>(interpreter, params, landscape, ant, foodLocationsList, allowedTimeSteps);
		
		setFitnessEvaluator(evaluator);

	}
}
