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

import org.epochx.core.CrossoverManager;
import org.epochx.representation.CandidateProgram;

/**
 * Provides the interface to be implemented by objects that wish to handle 
 * crossover events. See the {@link CrossoverManager}'s class documentation for 
 * details of when each crossover event will be fired. To listen for crossover 
 * events during execution of a model, instances of 
 * <code>CrossoverListener</code> must be added to the model's 
 * <code>LifeCycleManager</code> which is retrievable through a call to the 
 * model's <code>getLifeCycleManager()</code> method.
 * 
 * <p>
 * It is typical to listen to events using an anonymous class which often makes
 * the <code>abstract</code> <code>CrossoverAdapter</code> class more convenient
 * to implement.
 * 
 * @see CrossoverAdapter
 * @see CrossoverManager
 */
public interface CrossoverListener {

	/**
	 * Event fired before the crossover operation starts.
	 */
	void onCrossoverStart();
	
	/**
	 * Event fired after the selection and crossover operation has occurred. 
	 * The children may be modified and returned. This event is revertable by 
	 * returning <code>null</code> which will trigger the discarding of the 
	 * parents and children, the reselection of new parents, a new crossover 
	 * attempt and this event being raised again. If the crossover should be 
	 * accepted then the children should be returned as they are.
	 * 
	 * @param parents the programs that were selected to undergo crossover.
	 * @param children the programs that were generated as a result of the 
	 * crossover operation.
	 * @return an array of CandidatePrograms to be used as the children of the 
	 * crossover operation, or null if the crossover should be reverted.
	 */
	CandidateProgram[] onCrossover(CandidateProgram[] parents, CandidateProgram[] children);
	
	/**
	 * Event fired after the crossover operation has ended.
	 */
	void onCrossoverEnd();
	
}
