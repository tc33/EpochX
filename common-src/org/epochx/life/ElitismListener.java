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
package org.epochx.life;

import java.util.List;

import org.epochx.core.ElitismManager;
import org.epochx.representation.CandidateProgram;

/**
 * Provides the interface to be implemented by objects that wish to handle
 * elitism events. See the {@link ElitismManager}'s class documentation for
 * details of when each elitism event will be fired. To listen for elitism
 * events during execution of a model, instances of <code>ElitismListener</code>
 * must be added to the model's <code>LifeCycleManager</code> which is
 * retrievable through a call to the model's <code>getLifeCycleManager()</code>
 * method.
 * 
 * <p>
 * It is typical to listen to events using an anonymous class which often makes
 * the <code>abstract</code> <code>ElitismAdapter</code> class more convenient
 * to implement.
 * 
 * @see ElitismAdapter
 * @see ElitismManager
 */
public interface ElitismListener {

	/**
	 * Event fired before the elitism operation starts.
	 */
	void onElitismStart();

	/**
	 * Event fired after the elitism operation has been carried out.
	 * The elites may be modified and returned to be used, but it is not
	 * possible to (nor does it make sense to) revert elitism.
	 * 
	 * @param elites the selection of chosen elites.
	 * @return a list of <code>CandidatePrograms</code> to use as the set of
	 *         elites. Note that it is not appropriate to return a value of null
	 *         and
	 *         this will cause undefined behaviour.
	 */
	List<CandidateProgram> onElitism(List<CandidateProgram> elites);

	/**
	 * Event fired after the crossover operation has ended and been accepted.
	 */
	void onElitismEnd();
}
