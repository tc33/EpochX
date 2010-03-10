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

import org.epochx.representation.CandidateProgram;

/**
 * Defines the pool selection events which may be raised.
 */
public interface PoolSelectionListener {

	/**
	 * Event raised before the pool selection operation starts.
	 */
	public void onPoolSelectionStart();
	
	/**
	 * Event raised after the pool selection operation has occurred. The pool
	 * may be modified and returned. This event is revertable by 
	 * returning null which will trigger the discarding of the pool the 
	 * reselection of a new pool. This event will then be raised again. If the 
	 * selection should be accepted then the pool should be returned as it is.
	 * 
	 * @param pool the suggested breeding pool of programs.
	 * @return the breeding pool of CandidatePrograms that should actually be 
	 * used, or null if breeding pool selection should be repeated.
	 */
	public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool);
	
	/**
	 * Event raised after the pool selection operation has ended and been 
	 * accepted.
	 */
	public void onPoolSelectionEnd();
}
