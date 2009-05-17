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
 * This class defines an action which when executed will trigger the ant 
 * to do nothing for one timestep.
 */
public class AntSkipAction extends AntAction {

	/**
	 * Constructs an AntSkipAction, supplying an ant that the action can be 
	 * performed on.
	 * @param ant the Ant that will skip a timestep upon execution.
	 */
	public AntSkipAction(Ant ant) {
		super(ant);
	}
	
	/**
	 * Execute this action, which will trigger the ant to fill one timestep 
	 * without moving in its ant landscape.
	 */
	@Override
	public void execute() {
		getAnt().skip();
	}	
	
	/**
	 * String representation of this action which identifies the action type.
	 */
	@Override
	public String toString() {
		return "SKIP";
	}
}
