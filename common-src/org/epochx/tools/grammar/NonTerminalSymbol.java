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
 */
package org.epochx.tools.grammar;

import java.util.*;



/**
 *
 */
public class NonTerminalSymbol implements Symbol {

	private List<Symbol> children;
	
	// The associated grammar symbol.
	private GrammarRule grammarRule;
	
	private GrammarRule parentGrammarRule;
	
	public NonTerminalSymbol(GrammarRule grammarRule) {
		this(grammarRule, new ArrayList<Symbol>());
	}
	
	public NonTerminalSymbol(GrammarRule grammarRule, List<Symbol> children) {
		this.grammarRule = grammarRule;
		this.children = children;
	}
	
	public void setChild(int index, Symbol child) {
		children.set(index, child);
	}
	
	public Symbol getChild(int index) {
		return children.get(index);
	}
	
	public void addChild(Symbol child) {
		children.add(child);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(children.size());
		for (Symbol c: children) {
			buffer.append(c.toString());
		}
		
		return buffer.toString();
	}
	
	@Override
	public Object clone() {
		NonTerminalSymbol clone = null;
		try {
			clone = (NonTerminalSymbol) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is 
			// going to blow up anyway.
		}
		
		// Copy cloned child symbols.
		clone.children = new ArrayList<Symbol>();
		for (Symbol c: children) {
			clone.children.add((Symbol) c.clone());
		}
		
		// Shallow copy the grammar rules.
		clone.grammarRule = this.grammarRule;
		clone.parentGrammarRule = this.parentGrammarRule;
		
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof NonTerminalSymbol) {
			NonTerminalSymbol otherSymbol = (NonTerminalSymbol) obj;
			
			return this.toString().equals(otherSymbol.toString());
		} else {
			return false;
		}
	}
}
