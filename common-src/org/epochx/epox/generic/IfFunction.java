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
package org.epochx.epox.generic;




/**
 * 
 */
public class IfFunction extends GenericNode {

	public IfFunction() {
		this(null, null, null);
	}

	public IfFunction(final GenericNode child1, final GenericNode child2, final GenericNode child3) {
		super(child1, child2, child3);
	}
	
	@Override
	public Object evaluate() {
		final boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();

		if (c1) {
			return getChild(1).evaluate();
		} else {
			return getChild(2).evaluate();
		}
	}

	@Override
	public String getIdentifier() {
		return "IF";
	}

	@Override
	public Class<?> getReturnType() {
		if (getChild(1) != null && getChild(2) != null) {
			if (getChild(1).getClass().isInstance(getChild(2))) {
				// child3 is subclass of child2.
				return getChild(1).getClass();
			} else if (getChild(2).getClass().isInstance(getChild(1))) {
				// child2 is subclass of child3.
				return getChild(2).getClass();
			}
		}
		
		return null;
	}

	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		super.getReturnType(inputTypes);
		
		if (inputTypes[0].isAssignableFrom(Boolean.class)) {
			if (inputTypes[1].isAssignableFrom(inputTypes[2])) {
				// child3 is subclass of child2.
				return inputTypes[1];
			} else if (inputTypes[2].isAssignableFrom(inputTypes[1])) {
				// child2 is subclass of child3.
				return inputTypes[2];
			}
		}
		
		return null;
	}
}
