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

import org.epochx.representation.*;



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
	
	public void removeChildren() {
		children.clear();
	}
	
	public void removeChild(int index) {
		children.remove(index);
	}
	
	public void setChildren(List<Symbol> children) {
		this.children = children;
	}
	
	public List<Symbol> getChildren() {
		return children;
	}

	/**
	 * Returns a list of all non-terminal symbols in the parse tree below this 
	 * symbol, including this symbol itself.
	 * 
	 * @return
	 */
	public List<NonTerminalSymbol> getNonTerminalSymbols() {
		List<NonTerminalSymbol> nonTerminals = new ArrayList<NonTerminalSymbol>();
		
		// Start by adding self.
		nonTerminals.add(this);
		
		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				nonTerminals.addAll(((NonTerminalSymbol) child).getNonTerminalSymbols());
			}
		}
		
		return nonTerminals;
	}
	
	/**
	 * Returns a list of all terminal symbols in the parse tree below this 
	 * symbol.
	 * 
	 * @return
	 */
	public List<TerminalSymbol> getTerminalSymbols() {
		List<TerminalSymbol> terminals = new ArrayList<TerminalSymbol>();
		
		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				terminals.add((TerminalSymbol) child);
			} else if (child instanceof NonTerminalSymbol) {
				terminals.addAll(((NonTerminalSymbol) child).getTerminalSymbols());
			}
		}
		
		return terminals;
	}
	
	public List<Symbol> getAllSymbols() {
		List<Symbol> symbols = new ArrayList<Symbol>();
		
		symbols.add(this);
		
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				symbols.add(child);
			} else if (child instanceof NonTerminalSymbol) {
				symbols.addAll(((NonTerminalSymbol) child).getAllSymbols());
			}
		}
		
		return symbols;
	}
	
	public int getNoChildren() {
		return children.size();
	}
	
	public GrammarRule getGrammarRule() {
		return grammarRule;
	}
	
	public GrammarRule getParentGrammarRule() {
		return parentGrammarRule;		
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
			
			//TODO Think carefully about whether this is correct.
			return this.getGrammarRule() == otherSymbol.getGrammarRule();
		} else {
			return false;
		}
	}
}
