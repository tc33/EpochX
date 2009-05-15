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
 * Defines the concept of an ant action
 */
public abstract class AntAction extends Action {

	private Ant ant;
	
	/**
	 * Supplies an ant for the action to be performed on
	 * @param ant the ant object
	 */
	public AntAction(Ant ant) {
		this.ant = ant;
	}
	
	/**
	 * Returns the ant object
	 * @return the ant object
	 */
	public Ant getAnt() {
		return ant;
	}
}
