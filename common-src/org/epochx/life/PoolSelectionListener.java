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

import org.epochx.core.PoolSelectionManager;
import org.epochx.representation.CandidateProgram;

/**
 * Provides the interface to be implemented by objects that wish to handle pool
 * selection events. See the {@link PoolSelectionManager}'s class documentation
 * for details of when each pool selection event will be fired. To listen for
 * pool selection events during execution of a model, instances of
 * <code>PoolSelectionListener</code> must be added to the model's
 * <code>Life</code> which is retrievable through a call to the
 * model's <code>getLifeCycleManager()</code> method.
 * 
 * <p>
 * It is typical to listen to events using an anonymous class which often makes
 * the <code>abstract</code> <code>PoolSelectionAdapter</code> class more
 * convenient to implement.
 * 
 * @see PoolSelectionAdapter
 * @see PoolSelectionManager
 */
public interface PoolSelectionListener extends Listener {

	/**
	 * Event fired before the pool selection operation starts.
	 */
	void onPoolSelectionStart();

	/**
	 * Event fired after the pool selection operation has occurred. The pool
	 * may be modified and returned. This event is revertable by
	 * returning <code>null</code> which will trigger the discarding of the pool
	 * the reselection of a new pool. This event will then be raised again. If
	 * the selection should be accepted then the pool should be returned as it
	 * is.
	 * 
	 * @param pool the suggested breeding pool of programs.
	 * @return the breeding pool of CandidatePrograms that should actually be
	 *         used, or <code>null</code> if breeding pool selection should be
	 *         reverted.
	 */
	List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool);

	/**
	 * Event fired after the pool selection operation has ended and been
	 * accepted.
	 */
	void onPoolSelectionEnd();
}
