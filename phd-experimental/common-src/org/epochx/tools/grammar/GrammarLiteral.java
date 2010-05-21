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
 * Terminal symbols are those symbols which are never found on the left-hand 
 * side of grammar rules and as such contain no productions. Instead, 
 * TerminalSymbols each have a value which is the string they represent in the 
 * grammar string. The final source of a program that is valid according to a
 * grammar will be made up solely of terminal symbol values.
 */
public class GrammarLiteral implements GrammarNode {

	private String value;
	
	private boolean dynamic;
	
	/**
	 * Constructs a terminal symbol with the specified value.
	 * 
	 * @param value snippet of source that this terminal represents.
	 */
	public GrammarLiteral(String value) {
		setValue(value);
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
	 * Sets the value of this terminal symbol.
	 * 
	 * @param value the new value to set for this terminal.
	 */
	public void setValue(String value) {
		this.value = value;
		
		dynamic = value.startsWith("@");
	}
	
	/**
	 * Returns true if this grammar literal is a dynamic literal - that is, the 
	 * value should be generated at parse tree construction.
	 * 
	 * Currently a dynamic node is defined to be one that starts with an '@' 
	 * character.
	 *  
	 * @return true if this grammar literal has a dynamic value as defined 
	 * above, and false otherwise.
	 */
	public boolean isDynamic() {
		return dynamic;
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
