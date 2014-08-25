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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx.ge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.BranchedBreeder;
import org.epochx.Component;
import org.epochx.Config.ConfigKey;
import org.epochx.Config.Template;
import org.epochx.Evolver;
import org.epochx.FitnessEvaluator;
import org.epochx.GenerationalStrategy;
import org.epochx.Initialiser;
import org.epochx.MaximumGenerations;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.TerminationCriteria;
import org.epochx.ge.map.MappingComponent;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;

/**
 * Default configuration template for generational evolution in GE
 * 
 * @see Config
 * @see Template
 * 
 * @since 2.0
 */
public class GEGenerationalTemplate extends Template {

	/**
	 * The default parameter values:
	 * <ul>
	 * <li>{@link Population#SIZE}: <code>500</code>
	 * <li>{@link GenerationalStrategy#TERMINATION_CRITERIA}: <code>MaximumGenerations</code>
	 * <li>{@link MaximumGenerations#MAXIMUM_GENERATIONS}: <code>50</code>
	 * <li>{@link TournamentSelector#TOURNAMENT_SIZE}: <code>5</code>
	 * <li>{@link BranchedBreeder#SELECTOR}: <code>TournamentSelector</code>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}: <code>MersenneTwisterFast</code>
	 * <li>{@link Evolver#COMPONENTS}: <code>Initialiser</code>, <code>FitnessEvaluator</code>, <code>GenerationalStrategy(BranchedBreeder, MappingComponent, FitnessEvaluator)</code>
	 */
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		template.put(Population.SIZE, 500);

		List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
		criteria.add(new MaximumGenerations());
		template.put(GenerationalStrategy.TERMINATION_CRITERIA, criteria);
		template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);

		template.put(TournamentSelector.TOURNAMENT_SIZE, 5);
		template.put(BranchedBreeder.SELECTOR, new TournamentSelector());
		template.put(RandomSequence.RANDOM_SEQUENCE, new MersenneTwisterFast());

		ArrayList<Component> components = new ArrayList<Component>();
		components.add(new Initialiser());
		components.add(new MappingComponent());
		components.add(new FitnessEvaluator());
		components.add(new GenerationalStrategy(new BranchedBreeder(), new MappingComponent(), new FitnessEvaluator()));
		template.put(Evolver.COMPONENTS, components);
	}

}