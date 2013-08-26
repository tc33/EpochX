/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.interpret;

import java.util.*;


/**
 * Can hold multiple sets of named values.
 */
public class Parameters {

	// Parameter names.
	private String[] identifiers;
	
	// Parameter values.
	private List<Object[]> values;
	
	public Parameters(String[] identifiers) {
		this(identifiers, new ArrayList<Object[]>());
	}
	
	public Parameters(String[] identifiers, Object[] values) {
		this(identifiers);
		
		this.values.add(values);
	}
	
	public Parameters(String[] identifiers, List<Object[]> values) {
		this.identifiers = identifiers;		
		this.values = values;
	}
	
	public void addParameterSet(Object[] values) {
		this.values.add(values);
	}
	
	public Object[] removeParameterSet(int index) {
		return values.remove(index);
	}
	
	public Object[] getParameterSet(int index) {
		return values.get(index);
	}
	
	public String getIdentifier(int index) {
		//TODO This opens up a way of modifying the identifiers.
		return identifiers[index];
	}
	
	public int getNoParameters() {
		return identifiers.length;
	}
	
	public int getNoParameterSets() {
		return values.size();
	}
}
