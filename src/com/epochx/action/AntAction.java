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
package com.epochx.action;

import com.epochx.ant.*;

/**
 * An AntAction is the superclass for all actions in the artificial ant domain.
 * It provides a mechanism for accessing the Ant object to be acted upon.
 * @see Ant
 */
public abstract class AntAction extends Action {

	// The ant this action acts upon.
	private Ant ant;
	
	/**
	 * Constructs an AntAction, supplying an ant that the action can be 
	 * performed on.
	 * @param ant the Ant that can be acted upon.
	 */
	public AntAction(Ant ant) {
		this.ant = ant;
	}
	
	/**
	 * Returns the Ant object that this Action acts upon.
	 * @return the Ant object that this Action acts upon.
	 */
	public Ant getAnt() {
		return ant;
	}
}
