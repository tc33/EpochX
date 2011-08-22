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
import java.util.Collection;
import java.util.HashMap;

import org.epochx.Config.ConfigKey;

/**
 * The <code>Evolver</code> class is responsible for performing an evolutionary
 * simulation. By default, an evolutionary simulation consists of three
 * {@link Component}s that are executed in sequence:
 * <ol>
 * <li>{@link Initialiser}
 * <li>{@link FitnessEvaluator}
 * <li>{@link EvolutionaryStrategy}
 * </ol>
 * 
 * The specific implementation of {@link Initialiser} and
 * {@link EvolutionaryStrategy} components used are obtained from the
 * {@link Config}, using the appropriate <code>ConfigKey</code>. This default
 * behaviour can be extended by inserting additional <code>Component</code>s
 * between those specified here, by using the <code>add</code> method. The
 * position of new components is specified with a <code>Placeholder</code>.
 * 
 * <p>
 * The default set up of components can be replaced by overriding the
 * <code>setupPipeline(Pipeline)</code> method to initialise the provided
 * <code>Pipeline</code> with an alternative set of <code>Component</code>s or
 * an alternative ordering.
 */
public class Evolver {

	/**
	 * The key for setting and retrieving the <code>Initialiser</code>
	 * component.
	 */
	public static final ConfigKey<Initialiser> INITIALISER = new ConfigKey<Initialiser>();

	/**
	 * The key for setting and retrieving the <code>EvolutionaryStrategy</code>
	 * component.
	 */
	public static final ConfigKey<EvolutionaryStrategy> STRATEGY = new ConfigKey<EvolutionaryStrategy>();

	/**
	 * The mapping of placeholder components.
	 */
	private HashMap<Placeholder, ArrayList<Component>> additional;

	/**
	 * Constructs an <code>Evolver</code>.
	 */
	public Evolver() {
		additional = new HashMap<Evolver.Placeholder, ArrayList<Component>>();

		for (Placeholder position: Placeholder.values()) {
			additional.put(position, new ArrayList<Component>(1));
		}
	}

	/**
	 * Performs an evolutionary run. Each component in the pipeline returned by
	 * the <code>setupPipeline</code> method is processed in sequence. An empty
	 * {@link Population} is provided to the first component, and each
	 * succeeding component is supplied with the <code>Population</code>
	 * returned by the previous component.
	 * 
	 * @return a <code>Population</code> that is the result of processing the
	 *         pipeline of components, as returned by the final component in
	 *         that pipeline
	 */
	public Population run() {
		Pipeline pipeline = new Pipeline();
		setupPipeline(pipeline);

		return pipeline.process(new Population());
	}

	/**
	 * Initialises the supplied <code>Pipeline</code> with the components that
	 * an evolutionary run is composed of. The sequence of components is:
	 * <p>
	 * <ol>
	 * <li>[<code>START</code> placeholder]
	 * <li>{@link Initialiser}
	 * <li>[<code>AFTER_INITIALISATION</code> placeholder]
	 * <li>{@link FitnessEvaluator}
	 * <li>[<code>AFTER_EVALUATION</code> placeholder]
	 * <li>{@link EvolutionaryStrategy}
	 * <li>[<code>END</code> placeholder]
	 * </ol>
	 * <p>
	 * Where each placeholder consists of zero or more components supplied by
	 * calls to the {@link Evolver#add(Placeholder, Component)} method. The
	 * <code>setupPipeline</code> method can also be overridden to modify the
	 * components and sequence further.
	 * 
	 * @param pipeline the <code>Pipeline</code> that should be initialised with
	 *        a sequence of components that comprise an evolutionary run
	 */
	protected void setupPipeline(Pipeline pipeline) {
		pipeline.addAll(additional.get(Placeholder.START));
		pipeline.add(Config.getInstance().get(INITIALISER));
		pipeline.addAll(additional.get(Placeholder.AFTER_INITIALISATION));
		pipeline.add(new FitnessEvaluator());
		pipeline.addAll(additional.get(Placeholder.AFTER_EVALUATION));
		pipeline.add(Config.getInstance().get(STRATEGY));
		pipeline.addAll(additional.get(Placeholder.END));
	}

	/**
	 * Adds a component within the evolutionary pipeline at the specified
	 * <code>Placeholder</code> position. The placeholder position determines
	 * the provided component's position relative to the default components.
	 * Multiple components may be added using the same placeholder, with their
	 * order of execution matching the order in which they are added.
	 * 
	 * @param position a <code>Placeholder</code> that specifies the position
	 *        that the supplied component should be added to the evolutionary
	 *        pipeline
	 * @param component the <code>Component</code> that should be inserted into
	 *        the evolutionary pipeline at the specified position
	 */
	public void add(Placeholder position, Component component) {
		additional.get(position).add(component);
	}

	/**
	 * Removes the first occurrence of the specified component from the
	 * evolutionary pipeline if it exists at the given <code>Placeholder</code>
	 * position.
	 * 
	 * @param position a <code>Placeholder</code> that specifies the position
	 *        within the evolutionary pipeline from which the given component
	 *        should be removed
	 * @param component the <code>Component</code> that should be removed from
	 *        the evolutionary pipeline if it exists at the given
	 *        <code>Placeholder</code>
	 */
	public void remove(Placeholder position, Component component) {
		additional.get(position).remove(component);
	}

	/**
	 * Clears all components at the specified <code>Placeholder</code> position
	 * within the evolutionary pipeline.
	 * 
	 * @param position the <code>Placeholder</code> from which all components
	 *        should be removed
	 */
	public void clear(Placeholder position) {
		additional.get(position).clear();
	}

	/**
	 * Returns all components at the specified <code>Placeholder</code> position
	 * within the evolutionary pipeline.
	 * 
	 * @param position the <code>Placeholder</code> from which all components
	 *        should be returned.
	 * 
	 * @return all components at the specified <code>Placeholder</code>
	 *         position.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Component> get(Placeholder position) {
		return (ArrayList<Component>) additional.get(position).clone();
	}

	/**
	 * A <code>Placeholder</code> specifies a valid position for components to
	 * be inserted within the default sequence.
	 */
	public enum Placeholder {
		/**
		 * Defines a position within an evolutionary pipeline that is before any
		 * of the default components.
		 */
		START,

		/**
		 * Defines a position within an evolutionary pipeline that is
		 * immediately after the <code>Initialiser</code> component.
		 */
		AFTER_INITIALISATION,

		/**
		 * Defines a position within an evolutionary pipeline that is
		 * immediately after the <code>FitnessEvaluator</code> component.
		 */
		AFTER_EVALUATION,

		/**
		 * Defines a position within an evolutionary pipeline that is
		 * after all other default components.
		 */
		END
	}

}