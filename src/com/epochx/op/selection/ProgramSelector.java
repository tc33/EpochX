/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.op.selection;

import java.util.*;

import com.epochx.representation.*;

/**
 * ProgramSelectors are for selecting an individual from a population of 
 * programs. Most selectors will choose an individual based in some way upon 
 * the fitness of the programs.
 * 
 * @see PoolSelector
 */
public interface ProgramSelector<TYPE> {

	/**
	 * The GP system will call this method at the start of every generation to 
	 * provide the selector with the population from which selections should be 
	 * made. Users code would not usually call this method.
	 * 
	 * @param pop the current population for this generation.
	 */
	//TODO This is a little bit nasty, would be better if we can plug it into a standard listener system.
	public void setSelectionPool(List<CandidateProgram<TYPE>> pop);
	
	/**
	 * Select a <code>CandidateProgram</code> from the current population of 
	 * programs. The method of selection would normally be based upon the 
	 * fitness of the program but there is no need for it to be, and there are 
	 * exceptions.
	 * 
	 * @return a CandidateProgram selected from the current population of 
	 * programs.
	 */
	public CandidateProgram<TYPE> getProgram();
	
}
