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

import org.epochx.gp.representation.Node;
import org.epochx.representation.TreeChangeListener;



/**
 *
 */
public class NonTerminalSymbol implements Symbol, TreeChangeListener {

	private List<TreeChangeListener> changeListeners;
	
	private List<Symbol> children;
	
	// The associated grammar symbol.
	private GrammarRule grammarRule;
	
	public NonTerminalSymbol(GrammarRule grammarRule) {
		this(grammarRule, new ArrayList<Symbol>());
	}
	
	public NonTerminalSymbol(GrammarRule grammarRule, List<Symbol> children) {
		this.grammarRule = grammarRule;
		this.children = children;
		
		changeListeners = new ArrayList<TreeChangeListener>();
		
		// Add our listener on the new children.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				((NonTerminalSymbol) child).addTreeChangeListener(this);
			}
		}
	}
	
	public void setChild(int index, Symbol child) {
		// Remove our listener on the old child.
		if (children.get(index) instanceof NonTerminalSymbol) {
			((NonTerminalSymbol) children.get(index)).removeTreeChangeListener(this);
		}
		
		// Add our listener on the new child.
		if (child instanceof NonTerminalSymbol) {
			((NonTerminalSymbol) child).addTreeChangeListener(this);
		}
		
		// Make the change.
		children.set(index, child);

		// Inform our listeners that the tree has changed.
		treeChanged();
	}
	
	public void addChild(Symbol child) {
		// Add our listener on the new child.
		if (child instanceof NonTerminalSymbol) {
			((NonTerminalSymbol) child).addTreeChangeListener(this);
		}
		
		// Make the change.
		children.add(child);
		
		// Inform our listeners that the tree has changed.
		treeChanged();
	}
	
	/**
	 * There is no getChildren() method because it is important a child has only
	 * one parent for the sake of caching fitness.
	 * @return The children that were removed.
	 */
	public List<Symbol> removeChildren() {
		// Remove our listener on the children.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				((NonTerminalSymbol) child).removeTreeChangeListener(this);
			}
		}
		
		// Store the children to return.
		List<Symbol> oldChildren = children;
		
		// Make the change.
		children = new ArrayList<Symbol>();
		
		// Inform our listeners that the tree has changed.
		treeChanged();
		
		return oldChildren;
	}
	
	public Symbol removeChild(int index) {
		// Remove our listener on the child.
		if (children.get(index) instanceof NonTerminalSymbol) {
			((NonTerminalSymbol) children.get(index)).removeTreeChangeListener(this);
		}
		
		// Make the change.
		Symbol child = children.remove(index);
		
		// Inform our listeners that the tree has changed.
		treeChanged();
		
		return child;
	}
	
	public void setChildren(List<Symbol> newChildren) {
		// Remove our listener on the children.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				((NonTerminalSymbol) child).removeTreeChangeListener(this);
			}
		}
		
		// Add our listener on the new children.
		for (Symbol child: newChildren) {
			if (child instanceof NonTerminalSymbol) {
				((NonTerminalSymbol) child).addTreeChangeListener(this);
			}
		}
		
		// Make the change.
		this.children = newChildren;
		
		// Inform our listeners that the tree has changed.
		treeChanged();
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
	
	/*
	 * This MUST stay private to preserve internal consistency.
	 */
	private NonTerminalSymbol getNthNonTerminal(int n) {
		return getNthNonTerminal(n, 0);
	}
	
	/*
	 * This MUST stay private to preserve internal consistency.
	 */
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
	 * This MUST stay private to preserve internal consistency.
	 */
	private Symbol getNthSymbol(int n) {	
		return getNthSymbol(n, 0);
	}
	
	/*
	 * This MUST stay private to preserve internal consistency.
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
				if (n == current++) {
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
	 * This method simplifies the common process of swapping subtrees between 
	 * two parse trees below NonTerminalSymbols, by performing it internally 
	 * where caches can be preserved.
	 * 
	 * This method must be used carefully to preserve grammatical validity. It 
	 * is advised that a check is made that the two NonTerminalSymbols to be 
	 * swapped have the same underlying GrammarRule to ensure this.
	 * 
	 * @param n1 the index of the node in this parse tree to swap.
	 * @param nt the other non-terminal symbol root node.
	 * @param n2 the index of the node in the other parse tree to swap.
	 */
	public void swapSubtree(int n1, NonTerminalSymbol nt, int n2) {
		//TODO Need to deal with the situation that n1 or n2 is 0.
		
		// Stash the other tree's nth node temporarily.
		Symbol temp = nt.getNthSymbol(n2);
		
		// Set the other tree's nth node to be our subtree.
		nt.setNthSymbol(n2, this.getNthSymbol(n1));
		
		// Replace our subtree with the temp subtree from the other tree.
		this.setNthSymbol(n1, temp);
	}
	
	/**
	 * Returns the symbol index of a non-terminal from it's non-terminal index.
	 * @param n
	 * @return
	 */
	public int getNonTerminalSymbolIndex(int n){
		return getNonTerminalSymbolIndex(n, 0, 0);
	}
	
	private int getNonTerminalSymbolIndex(int targetNTIndex, int currentNTIndex, int currentSymbolIndex) {
		// Is this the one we're looking for?
		if (targetNTIndex == currentNTIndex) {
			return currentSymbolIndex;
		}
		
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				
				int symbolIndex = nt.getNonTerminalSymbolIndex(targetNTIndex, currentNTIndex+1, currentSymbolIndex+1);
				
				if (symbolIndex != -1) {
					return symbolIndex;
				}
				
				currentNTIndex += nt.getNoNonTerminalSymbols();
				currentSymbolIndex += nt.getNoSymbols();
			} else {
				currentSymbolIndex++;
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns the symbol index of a non-terminal from it's non-terminal index.
	 * @param n
	 * @return
	 */
	public int getNonTerminalSymbolIndex(int n, GrammarRule rule){
		return getNonTerminalSymbolIndex(n, rule, 0, 0);
	}
	
	private int getNonTerminalSymbolIndex(int targetNTIndex, GrammarRule rule, int currentNTIndex, int currentSymbolIndex) {
		if ((targetNTIndex == currentNTIndex) && rule.equals(this.getGrammarRule())) {
			return currentSymbolIndex;
		}
		
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				
				int nextNTIndex = currentNTIndex;
				if (rule.equals(this.getGrammarRule())) {
					nextNTIndex++;
				}
				
				int symbolIndex = nt.getNonTerminalSymbolIndex(targetNTIndex, rule, nextNTIndex, currentSymbolIndex+1);
				
				if (symbolIndex != -1) {
					return symbolIndex;
				}
				
				currentNTIndex += nt.getNoNonTerminalSymbols(rule);
				currentSymbolIndex += nt.getNoSymbols();
			} else {
				currentSymbolIndex++;
			}
		}
		
		return -1;
	}
	
	public int getTerminalSymbolIndex(int n) {
		return 0;
	}
	
	public GrammarRule getGrammarRule() {
		return grammarRule;
	}
	
	/**
	 * Get the grammar rule of the nth symbol. If the nth symbol is not a 
	 * NonTerminalSymbol and so doesn't have a grammar rule, then null will be 
	 * returned.
	 */
	public GrammarRule getGrammarRule(int n) {		
		Symbol nth = getNthSymbol(n);
		GrammarRule rule = null;
		
		if (nth instanceof NonTerminalSymbol) {
			rule = ((NonTerminalSymbol) nth).getGrammarRule();
		}
		
		return rule;
	}
	
	public void addTreeChangeListener(TreeChangeListener listener) {
		changeListeners.add(listener);
	}
	
	public void removeTreeChangeListener(TreeChangeListener listener) {
		changeListeners.remove(listener);
	}
	
	@Override
	public void treeChanged() {
		/*
		 * This method is called by the objects we're listening on, but it is 
		 * also called by this object, since in both cases all we want to do is 
		 * inform our listeners of a change.
		 */
		
		for (TreeChangeListener listener: changeListeners) {
			listener.treeChanged();
		}
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
		clone.changeListeners = new ArrayList<TreeChangeListener>();
		for (Symbol c: children) {
			Symbol childClone = (Symbol) c.clone();
			if (childClone instanceof NonTerminalSymbol) {
				((NonTerminalSymbol) childClone).addTreeChangeListener(clone);
			}
			
			clone.children.add(childClone);
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
