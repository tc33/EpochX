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
package org.epochx.gp.representation;

public class BooleanVariable extends BooleanNode {

	private String identifier;
	
	private Boolean value;
	
	public BooleanVariable(String identifier) {
		this(identifier, null);
	}
	
	public BooleanVariable(String identifier, Boolean value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public Boolean evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String toString() {
		return identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj == this);
	}
	
	@Override
	public BooleanVariable clone() {
		return this;
	}
}
