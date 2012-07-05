/* 
 * Copyright 2007-2012
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
 * The latest version is available from: http:/www.epochx.org
 */

package org.epochx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.Config.ConfigKey;
import org.epochx.Config.Template;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;

/**
 * 
 */
public class GenerationalTemplate extends Template {

	/**
	 * The default parameter values:
	 * <ul>
	 * <li>{@link Population#SIZE}: <code>500</code>
	 * <li>{@link MaximumGenerations#MAXIMUM_GENERATIONS}: <code>50</code>
	 * <li>{@link GenerationalStrategy#TERMINATION_CRITERIA}: MaximumGenerations
	 * <li>{@link TournamentSelector#TOURNAMENT_SIZE}: <code>5</code>
	 * <li>{@link BranchedBreeder#SELECTOR}: TournamentSelector
	 * <li>{@link Evolver#STRATEGY}: GenerationalStrategy(BranchedBreeder)
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}: MersenneTwisterFast
	 */
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		template.put(Population.SIZE, 500);
		template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);

		List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
		criteria.add(new MaximumGenerations());
		template.put(GenerationalStrategy.TERMINATION_CRITERIA, criteria);

		template.put(TournamentSelector.TOURNAMENT_SIZE, 5);
		template.put(BranchedBreeder.SELECTOR, new TournamentSelector());
		template.put(RandomSequence.RANDOM_SEQUENCE, new MersenneTwisterFast());

		ArrayList<Component> components = new ArrayList<Component>();
		components.add(new Initialiser());
		components.add(new FitnessEvaluator());
		components.add(new GenerationalStrategy(new BranchedBreeder(), new FitnessEvaluator()));
		template.put(Evolver.COMPONENTS, components);
	}

}