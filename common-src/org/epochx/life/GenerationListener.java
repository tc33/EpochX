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

import org.epochx.core.GenerationManager;
import org.epochx.representation.CandidateProgram;

/**
 * Provides the interface to be implemented by objects that wish to handle
 * generation events. See the {@link GenerationManager}'s class documentation
 * for details of when each generation event will be fired. To listen for
 * generation events during execution of a model, instances of
 * <code>GenerationListener</code> must be added to the model's
 * <code>Life</code> which is retrievable through a call to the
 * model's <code>getLifeCycleManager()</code> method.
 * 
 * <p>
 * It is typical to listen to events using an anonymous class which often makes
 * the <code>abstract</code> <code>GenerationAdapter</code> class more
 * convenient to implement.
 * 
 * @see GenerationAdapter
 * @see GenerationManager
 */
public interface GenerationListener extends Listener {

	/**
	 * Event fired before a generation starts.
	 */
	void onGenerationStart();

	/**
	 * Event fired after a generation has been carried out. The resultant
	 * population may be modified and returned. This event is revertable by
	 * returning <code>null</code> which will trigger the population to be
	 * discarded and for the whole generation to be performed again from the
	 * previous population. This event will then be raised again. If the
	 * generation should be accepted then the population should be returned as
	 * it is.
	 * 
	 * @param pop the population that is the result of carrying out the
	 *        generation.
	 * @return the list of programs that should become the next population, or
	 *         null if the generation should be reverted.
	 */
	List<CandidateProgram> onGeneration(List<CandidateProgram> pop);

	/**
	 * Event fired once a generation has ended and been accepted.
	 */
	void onGenerationEnd();

}
