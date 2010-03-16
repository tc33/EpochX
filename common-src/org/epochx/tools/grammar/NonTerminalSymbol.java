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

import java.util.*;

/**
 *
 */
public class NonTerminalSymbol implements Symbol {

	private List<Symbol> children;
	
	// The associated grammar symbol.
	private GrammarRule grammarRule;

	public NonTerminalSymbol(GrammarRule grammarRule) {
		this(grammarRule, new ArrayList<Symbol>());
	}
	
	public NonTerminalSymbol(GrammarRule grammarRule, List<Symbol> children) {
		this.grammarRule = grammarRule;
		this.children = children;
	}
	
	public void setChild(int index, Symbol child) {		
		// Make the change.
		children.set(index, child);
	}
	
	public Symbol getChild(int index) {
		return children.get(index);
	}
	
	public void addChild(Symbol child) {
		// Make the change.
		children.add(child);
	}
	
	/**
	 * There is no getChildren() method because it is important a child has only
	 * one parent for the sake of caching fitness.
	 * @return The children that were removed.
	 */
	public List<Symbol> removeChildren() {
		// Make the change.
		children.clear();
		
		return children;
	}
	
	public Symbol removeChild(int index) {		
		return children.remove(index);
	}
	
	public List<Symbol> getChildren() {
		return children;
	}
	
	public void setChildren(List<Symbol> newChildren) {		
		// Make the change.
		this.children = newChildren;
	}
	
	public int getNoNonTerminalSymbols() {		
		// Start by adding self.
		int noNonTerminals = 1;

		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				noNonTerminals += ((NonTerminalSymbol) child).getNoNonTerminalSymbols();
			}
		}
		
		return noNonTerminals;
	}
	
	/**
	 * Counts the number of non-terminal symbols below this non-terminal 
	 * (inclusive), that have the specified underlying grammar rule.
	 * 
	 * @param rule
	 * @return
	 */
	public int getNoNonTerminalSymbols(GrammarRule rule) {		
		int noNonTerminals = 0;
		
		// Start by adding self.
		if (this.getGrammarRule().equals(rule)) {
			noNonTerminals++;;
		}

		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				noNonTerminals += ((NonTerminalSymbol) child).getNoNonTerminalSymbols(rule);
			}
		}
		
		return noNonTerminals;
	}
	
	public int getNoTerminalSymbols() {
		int noTerminals = 0;
		
		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				noTerminals++;
			} else if (child instanceof NonTerminalSymbol) {
				noTerminals += ((NonTerminalSymbol) child).getNoTerminalSymbols();
			}
		}
		
		return noTerminals;
	}
	
	public int getNoSymbols() {
		// Start by adding self.
		int noSymbols = 1;
		
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				noSymbols++;
			} else if (child instanceof NonTerminalSymbol) {
				noSymbols += ((NonTerminalSymbol) child).getNoSymbols();
			}
		}
		
		return noSymbols;
	}
	
	public int getNoChildren() {
		return children.size();
	}
	
	public NonTerminalSymbol removeNthNonTerminal(int n) {
		// n must be greater than 0 because you cannot remove the current node.
		
		return removeNthNonTerminal(n, 0, null);
	}
	
	private NonTerminalSymbol removeNthNonTerminal(int n, int current, GrammarRule rule) {
		for (int i=0; i<children.size(); i++) {
			Symbol child = children.get(i);
			
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				
				boolean valid = false;
				if (rule == null || rule.equals(nt.getGrammarRule())) {
					valid = true;
				}
				
				if (valid && n == current+1) {
					// It is this child.
					return (NonTerminalSymbol) removeChild(i);
				} else {
					NonTerminalSymbol nth = nt.removeNthNonTerminal(n, (valid ? current+1 : current), rule);
					
					if (nth != null) {
						return nth;
					}
					
					current += nt.getNoNonTerminalSymbols(rule);
				}
			}
		}
		
		return null;
	}
	
	public NonTerminalSymbol removeNthNonTerminal(int n, GrammarRule grammarRule) {
		return removeNthNonTerminal(n, 0, grammarRule);
	}
	
	/*public Symbol removeNthSymbol(int n) {
		
	}
	
	public TerminalSymbol removeNthTerminal(int n) {
		
	}*/
	
	public NonTerminalSymbol getNthNonTerminal(int n) {
		return getNthNonTerminal(n, 0);
	}
	
	private NonTerminalSymbol getNthNonTerminal(int n, int current) {
		// Is this the one we're looking for?
		if (n == current) {
			return this;
		}
		
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				
				NonTerminalSymbol nth = nt.getNthNonTerminal(n, current+1);
				
				if (nth != null) {
					return nth;
				}
				
				current += nt.getNoNonTerminalSymbols();
			}
		}
		
		return null;
	}
	
	/*
	 * 
	 */
	public Symbol getNthSymbol(int n) {	
		return getNthSymbol(n, 0);
	}
	
	/*
	 * 
	 */
	private Symbol getNthSymbol(int n, int current) {
		// Is this the one we're looking for?
		if (n == current) {
			return this;
		}
		
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				
				Symbol nth = nt.getNthSymbol(n, current+1);
				
				if (nth != null) {
					return nth;
				}
				
				current += nt.getNoSymbols();
			} else {
				if (n == ++current) {
					return child;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * n must be greater than 0 because you cannot replace the root node.
	 * @param n
	 * @param nt
	 */
	public void setNthSymbol(int n, Symbol nt) {
		// Must remove and add listeners.
		setNthSymbol(n, nt, 0);
	}
	
	private void setNthSymbol(int n, Symbol symbol, int current) {
		int noChildren = getNoChildren();
		for (int i=0; i<noChildren; i++) {
			if (current+1 == n) {
				setChild(i, symbol);
				break;
			}
			
			if (children.get(i) instanceof NonTerminalSymbol) {
				NonTerminalSymbol child = (NonTerminalSymbol) children.get(i);
				int noChildSymbols = child.getNoSymbols();
				
				// Only look at the subtree if it contains the right range of nodes.
				if (n <= current + noChildSymbols) {
					child.setNthSymbol(n, symbol, current+1);
				}
				
				current += noChildSymbols;
			} else {
				// It's a terminal so just increment 1.
				current++;
			}
		}
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
	
	public List<Integer> getNonTerminalIndexes() {
		return getNonTerminalIndexes(0);
	}
	
	private List<Integer> getNonTerminalIndexes(int index) {
		List<Integer> nonTerminals = new ArrayList<Integer>();
		
		// Start by adding self.
		nonTerminals.add(index);
		
		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				nonTerminals.addAll(nt.getNonTerminalIndexes(index+1));
				index += nt.getNoSymbols();
			} else {
				index++;
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
	
	public GrammarRule getGrammarRule() {
		return grammarRule;
	}
	
	public int getDepth() {
		int maxChildDepth = 0;
		
		for (Symbol child: children) {
			int childDepth;
			if (child instanceof NonTerminalSymbol) {
				childDepth = ((NonTerminalSymbol) child).getDepth() + 1;
			} else {
				childDepth = 1;
			}
			
			if (childDepth > maxChildDepth) {
				maxChildDepth = childDepth;
			}
		}
		
		return maxChildDepth;
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
