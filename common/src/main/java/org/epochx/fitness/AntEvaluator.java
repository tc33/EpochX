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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.fitness;

import java.awt.Point;
import java.util.*;

import org.epochx.Individual;
import org.epochx.interpret.*;
import org.epochx.tools.ant.*;


/**
 * 
 */
public class AntEvaluator<T extends Individual> extends AbstractFitnessEvaluator<T> {

	private AntLandscape landscape;
	private Ant ant;
	private List<Point> foodLocations;
	
	private int timesteps;
	
	private Interpreter<T> interpreter;
	
	private double malformedPenalty;
	
	private Parameters params;
	
	public AntEvaluator(Interpreter<T> interpreter, Parameters params, AntLandscape landscape, Ant ant, List<Point> foodLocations, int timesteps) {
		this.interpreter = interpreter;
		this.landscape = landscape;
		this.ant = ant;
		this.foodLocations = foodLocations;
		this.timesteps = timesteps;
		this.params = params;
		
		malformedPenalty = foodLocations.size();
	}
	
	@Override
	public double getFitness(T program) {
		landscape.setFoodLocations(new ArrayList<Point>(foodLocations));
		ant.reset(timesteps, landscape);

		// Run the ant.
		try {
			while (ant.getTimesteps() < ant.getMaxMoves()) {
				interpreter.exec(program, params);
			}
		} catch (MalformedProgramException e) {
			return malformedPenalty;
		}

		// Calculate score.
		return (foodLocations.size() - ant.getFoodEaten());
	}

	public double getMalformedProgramPenalty() {
		return malformedPenalty;
	}
	
	public void setMalformedProgramPenalty(double malformedPenalty) {
		this.malformedPenalty = malformedPenalty;
	}
}
