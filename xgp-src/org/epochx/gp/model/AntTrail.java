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
package org.epochx.gp.model;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.epochx.epox.*;
import org.epochx.epox.ant.*;
import org.epochx.epox.lang.SeqNFunction;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.*;

/**
 * Abstract XGP model for ant trail problems. This class
 * provides the general functionality of ant trail models, but does not itself
 * define a trail. GP ant trail models should extend this class.
 */
public abstract class AntTrail extends GPModel {

	// Ant components.
	private final AntLandscape landscape;
	private final Ant ant;
	private final Variable antVariable;

	// Trail settings.
	private final List<Point> foodLocations;
	private final int allowedTimeSteps;

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
	public AntTrail(final Point[] foodLocations, final Dimension landscapeSize, final int allowedTimeSteps) {
		this.foodLocations = Arrays.asList(foodLocations);
		this.allowedTimeSteps = allowedTimeSteps;

		landscape = new AntLandscape(landscapeSize, null);
		ant = new Ant(allowedTimeSteps, landscape);
		antVariable = new Variable("ANT", ant);

		// Define functions.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new IfFoodAheadFunction(ant));
		syntax.add(new SeqNFunction(2));
		syntax.add(new SeqNFunction(3));
		syntax.add(new AntMoveFunction(ant));
		syntax.add(new AntTurnLeftFunction(ant));
		syntax.add(new AntTurnRightFunction(ant));
		// syntax.add(antVariable);

		setSyntax(syntax);
	}

	/**
	 * Calculates and returns the fitness of the given program. The fitness of a
	 * program is calculated as the number of food pieces that the ant did not
	 * manage to reach. That is, a fitness of 0.0 means the ant found every food
	 * item.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;

		landscape.setFoodLocations(new ArrayList<Point>(foodLocations));
		ant.reset(allowedTimeSteps, landscape);

		// Run the ant.
		while (ant.getTimesteps() < ant.getMaxMoves()) {
			// System.out.println(landscape.toString());
			program.evaluate();
		}

		// Calculate score.
		return (foodLocations.size() - ant.getFoodEaten());
	}

	public Ant getAnt() {
		return ant;
	}

	public AntLandscape getAntLandScape() {
		return landscape;
	}

	@Override
	public Class<?> getReturnType() {
		return Void.class;
	}
}
