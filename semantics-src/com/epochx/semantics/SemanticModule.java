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

package com.epochxge.semantics;

import com.epochxge.representation.*;

/**
 * The semantic module interface sets out the basic methods required for all semantic modules.
 */
public interface SemanticModule {
	
	/**
	 * Converts a syntax tree into a canonical representation of behaviour
	 * @param program The syntax tree to convert
	 * @return The canonical behaviour
	 */
	public Representation codeToBehaviour(CandidateProgram program);
	
	/**
	 * Converts a given behaviour back to a syntax tree
	 * @param representation The behaviour to convert to syntax
	 * @return The syntax tree
	 */
	public CandidateProgram behaviourToCode(Representation representation);
	
	/**
	 * Method to start actions in an external piece of software - for CUDD
	 */
	public void start();
	
	/**
	 * Method to stop actions in an external piece of software - for CUDD
	 */
	public void stop();

}
