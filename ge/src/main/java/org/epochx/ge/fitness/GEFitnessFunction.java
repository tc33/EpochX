/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
package org.epochx.ge.fitness;

import org.epochx.AbstractFitnessFunction;
import org.epochx.Config.ConfigKey;
import org.epochx.ge.GEIndividual;
import org.epochx.interpret.Interpreter;

/**
 * A fitness function for evaluating GE individuals
 * 
 * @since 2.0
 */
public abstract class GEFitnessFunction extends AbstractFitnessFunction {

	/**
	 * The key for setting the interpreter to use to execute individuals
	 */
	public static final ConfigKey<Interpreter<GEIndividual>> INTERPRETER = new ConfigKey<Interpreter<GEIndividual>>();
	
	/**
	 * The key for setting the program's argument identifiers
	 */
	public static final ConfigKey<String[]> INPUT_IDENTIFIERS = new ConfigKey<String[]>();
	
	/**
	 * The key for setting the sets of values to use as inputs
	 */
	public static final ConfigKey<Object[][]> INPUT_VALUE_SETS = new ConfigKey<Object[][]>();

	/**
	 * The key for setting the fitness score to assign to malformed programs
	 */
	public static final ConfigKey<Double> MALFORMED_PENALTY = new ConfigKey<Double>();
}
