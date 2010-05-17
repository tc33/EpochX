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
package org.epochx.tools.grammar;


/**
 *
 */
public class TerminalSymbol implements Symbol {

	private GrammarLiteral literal;
	
	public TerminalSymbol(GrammarLiteral literal) {
		this.literal = literal;
	}
	
	@Override
	public String toString() {
		return literal.toString();
	}
	
	@Override
	public Object clone() {
		TerminalSymbol clone = null;
		try {
			clone = (TerminalSymbol) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is 
			// going to blow up anyway.
		}
		
		// Shallow copy the grammar rules.
		clone.literal = this.literal;
		
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TerminalSymbol) {
			TerminalSymbol objSymbol = (TerminalSymbol) obj;
			
			return this.toString().equals(objSymbol.toString());
		} else {
			return false;
		}
	}
}
