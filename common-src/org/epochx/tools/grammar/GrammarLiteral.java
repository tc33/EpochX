/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 *//*  
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
package org.epochx.tools.grammar;

/**
 * Terminal symbols are those symbols which are never found on the left-hand 
 * side of grammar rules and as such contain no productions. Instead, 
 * TerminalSymbols each have a value which is the string they represent in the 
 * grammar string. The final source of a program that is valid according to a
 * grammar will be made up solely of terminal symbol values.
 */
public class GrammarLiteral implements GrammarNode {

	private String value;
	
	/**
	 * Constructs a terminal symbol with the specified value.
	 * 
	 * @param value snippet of source that this terminal represents.
	 */
	public GrammarLiteral(String value) {
		this.value = value;
	}
	
	/**
	 * Returns the value of this terminal symbol.
	 * 
	 * @return this terminal's value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Return a string representation of this terminal symbol.
	 * 
	 * @return a string representation of this terminal symbol.
	 */
	@Override
	public String toString() {
		return value;
	}

}
