/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.life;

import java.util.*;

import org.epochx.representation.*;


/**
 *
 */
public interface PoolSelectionListener {

	public void onPoolSelectionStart();
	
	/**
	 * Called after selection of the breeding pool. If the size of the breeding
	 * pool is set in the model to <=0, then this method will still be called 
	 * at the appropriate time, but with a list containing every program in the 
	 * population. The population essentially becomes the breeding pool in this 
	 * circumstance.
	 * 
	 * @param pool the suggested breeding pool of programs.
	 * @return the breeding pool of CandidatePrograms that should actually be 
	 * used, or null if breeding pool selection should be repeated.
	 */
	public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool);
	
	public void onPoolSelectionEnd();
}
