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
package org.epochx.epox.ant;

import org.epochx.epox.*;
import org.epochx.tools.ant.Ant;

/**
 * This class defines an action which when executed will trigger the ant
 * to move one position in its ant landscape.
 */
public class AntMoveFunction extends Node {

	/**
	 * Constructs an AntMoveFunction.
	 */
	public AntMoveFunction() {
		this(null);
	}
	
	public AntMoveFunction(final Node child) {
		super(child);
	}

	@Override
	public Void evaluate() {
		Ant ant = (Ant) getChild(0).evaluate();
		
		ant.move();

		return null;
	}

	@Override
	public String getIdentifier() {
		return "MOVE";
	}

	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && Ant.class.isAssignableFrom(inputTypes[0])) {
			return Void.class;
		} else {
			return null;
		}
	}
}
