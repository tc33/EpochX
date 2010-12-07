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
public class ArrayAddFunction extends GenericNode {

	public ArrayAddFunction() {
		this(null, null);
	}

	public ArrayAddFunction(final GenericNode child1, final GenericNode child2) {
		super(child1, child2);
	}
	
	@Override
	public Class<?> getReturnType() {
		if (getChild(0) != null && getChild(1) != null) {
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
		
		int child1Dimensions = getArrayDimensions(inputTypes[0]);
		int child2Dimensions = getArrayDimensions(inputTypes[1]);
		
		if (child1Dimensions == child2Dimensions) {
			Class<?> arrayType1 = getArrayElementType(inputTypes[0]);
			Class<?> arrayType2 = getArrayElementType(inputTypes[1]);
			
			// Test whether we can add elements of this type.
		}
		
		return null;
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "ARRAY-ADD";
	}
	
	public static int getArrayDimensions(Class<?> arrayClass) { 
		int dim = 0; 
		while (arrayClass.isArray()) { 
			dim++; 
			arrayClass = arrayClass.getComponentType(); 
		}
		
		return dim;
	}
	
	public static Class<?> getArrayElementType(Class<?> arrayClass) {
		while (arrayClass.isArray()) { 
			arrayClass = arrayClass.getComponentType(); 
		}
		
		return arrayClass;
	}
}
