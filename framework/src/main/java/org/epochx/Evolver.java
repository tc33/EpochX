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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.epochx.Config.ConfigKey;

public class Evolver {

	public static final ConfigKey<Initialiser> INITIALISER = new ConfigKey<Initialiser>();

	public static final ConfigKey<FitnessEvaluator> EVALUATOR = new ConfigKey<FitnessEvaluator>();

	public static final ConfigKey<EvolutionaryStrategy> STRATEGY = new ConfigKey<EvolutionaryStrategy>();

	private HashMap<Placeholder, List<Component>> additional;

	public Evolver() {
		additional = new HashMap<Evolver.Placeholder, List<Component>>();

		for (Placeholder position: Placeholder.values()) {
			additional.put(position, new ArrayList<Component>(1));
		}
	}

	public Population run() {
		Pipeline pipeline = new Pipeline();
		setupPipeline(pipeline);

		return pipeline.process(new Population());
	}

	protected void setupPipeline(Pipeline pipeline) {
		pipeline.addAll(additional.get(Placeholder.START));
		pipeline.add(Config.getInstance().get(INITIALISER));
		pipeline.addAll(additional.get(Placeholder.AFTER_INITIALISATION));
		pipeline.add(Config.getInstance().get(EVALUATOR));
		pipeline.addAll(additional.get(Placeholder.AFTER_EVALUATION));
		pipeline.add(Config.getInstance().get(STRATEGY));
		pipeline.addAll(additional.get(Placeholder.END));
	}

	public void add(Placeholder position, Component component) {
		additional.get(position).add(component);
	}

	public void remove(Placeholder position, Component component) {
		additional.get(position).remove(component);
	}

	public void clear(Placeholder position) {
		additional.get(position).clear();
	}

	public enum Placeholder {
		START, END, AFTER_INITIALISATION, AFTER_EVALUATION
	}
}