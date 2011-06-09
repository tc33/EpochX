/* 
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx;

import org.epochx.Config.ConfigKey;

public class Evolver {

	public static final ConfigKey<Initialiser> INITIALISER = new ConfigKey<Initialiser>();

	public static final ConfigKey<FitnessEvaluator> EVALUATOR = new ConfigKey<FitnessEvaluator>();

	public static final ConfigKey<EvolutionaryStrategy> STRATEGY = new ConfigKey<EvolutionaryStrategy>();

	private final Pipeline pipeline;

	public Evolver() {
		pipeline = new Pipeline();
	}

	public Population run() {
		setupPipeline();

		return pipeline.process(new Population());
	}

	protected void setupPipeline() {
		pipeline.add(Config.getInstance().get(INITIALISER));
		pipeline.add(Config.getInstance().get(EVALUATOR));
		pipeline.add(Config.getInstance().get(STRATEGY));
	}
}
