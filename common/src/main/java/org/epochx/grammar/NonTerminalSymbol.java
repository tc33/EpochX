/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
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
package org.epochx.grammar;

import java.util.*;

import org.apache.commons.lang.ObjectUtils;

/**
 * A non-terminal node of a parse tree, that was constructed to satisfy a
 * specific rule of a grammar. The underlying <tt>GrammarRule</tt> is
 * provided at construction time. An instance's children are those
 * <tt>Symbol</tt> objects that the non-terminal resolves to, as supported
 * by the grammar rule.
 * 
 * @see TerminalSymbol
 * @see GrammarRule
 */
public class NonTerminalSymbol implements Symbol {

	// The child nodes in the parse tree.
	private List<Symbol> children;

	// The associated grammar node.
	private GrammarRule grammarRule;

	/**
	 * Constructs a <tt>NonTerminalSymbol</tt> for the given
	 * <tt>GrammarRule</tt>.
	 * 
	 * @param grammarRule the <tt>GrammarRule</tt> which this new object is
	 *        representing an instance of.
	 */
	public NonTerminalSymbol(GrammarRule grammarRule) {
		this(grammarRule, new ArrayList<Symbol>());
	}

	/**
	 * Constructs a <tt>NonTerminalSymbol</tt> for the given
	 * <tt>GrammarRule</tt> and with a list of child parse tree symbols.
	 * 
	 * @param grammarRule the <tt>GrammarRule</tt> which this new object is
	 *        representing an instance of
	 * @param children a list of <tt>Symbol</tt> instances which this
	 *        <tt>NonTerminalSymbol</tt> resolves to, as supported by
	 *        the grammar
	 *        rule.
	 */
	public NonTerminalSymbol(GrammarRule grammarRule, List<Symbol> children) {
		this.grammarRule = grammarRule;
		this.children = children;
	}

	/**
	 * Overwrites the <tt>Symbol</tt> at the specified index. Note that the
	 * index must be a currently valid index.
	 * 
	 * @param index the index of the <tt>Symbol</tt> to change.
	 * @param child the <tt>Symbol</tt> to set at the specified index.
	 */
	public void setChild(int index, Symbol child) {
		// Make the change.
		children.set(index, child);
	}

	/**
	 * Returns the <tt>Symbol</tt> at the specified index.
	 * 
	 * @param index the index of the <tt>Symbol</tt> to return.
	 * @return the <tt>Symbol</tt> found at the given index.
	 */
	public Symbol getChild(int index) {
		return children.get(index);
	}

	/**
	 * Appends the given <tt>Symbol</tt> to the list of child nodes.
	 * 
	 * @param child the <tt>Symbol</tt> instance to append.
	 */
	public void addChild(Symbol child) {
		// Make the change.
		children.add(child);
	}

	/**
	 * Removes all currently set child <tt>Symbols</tt>.
	 * 
	 * @return a <tt>List</tt> of the child <tt>Symbol</tt> instances
	 *         that were removed.
	 */
	public List<Symbol> removeChildren() {
		// Make the change.
		children.clear();

		return children;
	}

	/**
	 * Removes a the <tt>Symbol</tt> that is at the specified index.
	 * 
	 * @param index the index of the <tt>Symbol</tt> to remove.
	 * @return the <tt>Symbol</tt> instance that was removed.
	 */
	public Symbol removeChild(int index) {
		return children.remove(index);
	}

	/**
	 * Returns a reference to the underlying <tt>List</tt> of child
	 * <tt>Symbol</tt> instances for this non-terminal. Any changes to the
	 * returned list will be reflected in this symbol.
	 * 
	 * @return a <tt>List</tt> of the child <tt>Symbol</tt> instances.
	 */
	public List<Symbol> getChildren() {
		return children;
	}

	/**
	 * Overwrites this non-terminal's <tt>List</tt> of child
	 * <tt>Symbols</tt>.
	 * 
	 * @param newChildren the <tt>List</tt> of child <tt>Symbol</tt>
	 *        instances to set.
	 */
	public void setChildren(List<Symbol> newChildren) {
		// Make the change.
		children = newChildren;
	}

	/**
	 * Calculates and returns the number of non-terminal symbols that exist
	 * within the tree rooted at this non-terminal symbol, including this
	 * <tt>Symbol</tt>. The result should always be equal or greater than 1.
	 * 
	 * @return a positive integer which is the count of the number of
	 *         <tt>NonTerminalSymbol</tt> instances in the parse tree rooted
	 *         at this <tt>Symbol</tt>, inclusive of this symbol.
	 */
	public int getNoNonTerminalSymbols() {
		// Start by adding self.
		int noNonTerminals = 1;

		// Count all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				noNonTerminals += ((NonTerminalSymbol) child).getNoNonTerminalSymbols();
			}
		}

		assert (noNonTerminals >= 1);

		return noNonTerminals;
	}

	/**
	 * Calculates and returns the number of non-terminal symbols that exist
	 * within the tree rooted at this non-terminal symbol which have the
	 * specified underlying <tt>GrammarRule</tt>. The count is inclusive of
	 * this <tt>NonTerminalSymbol</tt>. A <tt>NonTerminalSymbol</tt>
	 * <tt>x</tt>is included in the count if the following expression is
	 * <tt>true</tt>.
	 * 
	 * <blockquote><tt>
	 * 		this.getGrammarRule().equals(x.getGrammarRule())
	 * </tt></blockquote>
	 * 
	 * @param rule the <tt>GrammarRule</tt> that should be matched in all
	 *        <tt>NonTerminalSymbols</tt> included in the count.
	 * @return the total number of non-terminal symbols that have a matching
	 *         <tt>GrammarRule</tt>.
	 */
	public int getNoNonTerminalSymbols(GrammarRule rule) {
		int noNonTerminals = 0;

		// Start by adding self.
		if (getGrammarRule().equals(rule)) {
			noNonTerminals++;
		}

		// Count all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				noNonTerminals += ((NonTerminalSymbol) child).getNoNonTerminalSymbols(rule);
			}
		}

		return noNonTerminals;
	}

	/**
	 * Calculates and returns the number of terminal symbols that exist at the
	 * leaves of the parse tree rooted at this non-terminal symbol. The count
	 * should always be positive, and will only ever be zero in the case of an
	 * incomplete parse tree.
	 * 
	 * @return an <tt>int</tt> which is the total number of terminal symbols
	 *         in this parse tree.
	 */
	public int getNoTerminalSymbols() {
		int noTerminals = 0;

		// Count all the terminals below each child.
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				noTerminals++;
			} else if (child instanceof NonTerminalSymbol) {
				noTerminals += ((NonTerminalSymbol) child).getNoTerminalSymbols();
			}
		}

		return noTerminals;
	}

	/**
	 * Calculates and returns the total number of <tt>Symbol</tt> instances
	 * that exist in the parse tree rooted at this non-terminal symbol,
	 * including this <tt>Symbol</tt> itself. The result should be equal to
	 * the sum of the results from the <tt>getNoNonTerminalSymbols()</tt>
	 * and <tt>getNoTerminalSymbols()</tt> methods.
	 * 
	 * @return the total number of symbols that exist in the parse tree rooted
	 *         at this <tt>Symbol</tt>.
	 */
	public int getNoSymbols() {
		// Start by adding self.
		int noSymbols = 1;

		// Count all the symbols below each child.
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				noSymbols++;
			} else if (child instanceof NonTerminalSymbol) {
				noSymbols += ((NonTerminalSymbol) child).getNoSymbols();
			}
		}

		return noSymbols;
	}

	/**
	 * Returns the number of direct child <tt>Symbols</tt> this non-terminal
	 * symbol has.
	 * 
	 * @return an <tt>int</tt> which is the number of child symbols this
	 *         non-terminal has.
	 */
	public int getNoChildren() {
		return children.size();
	}

	/**
	 * Removes the nth non-terminal from the parse tree rooted at this
	 * <tt>NonTerminalSymbol</tt> instance, as counted using a pre-order
	 * traversal. Indexing starts at zero for this non-terminal symbol. As such,
	 * valid values of n must be greater than or equal to 1, since it is
	 * impossible to remove a symbol from itself.
	 * 
	 * @param n an <tt>int</tt> with a value of 1 or greater which is the
	 *        index of the <tt>NonTerminalSymbol</tt> that should be
	 *        removed.
	 * @return the <tt>NonTerminalSymbol</tt> that was removed, or
	 *         <tt>null</tt> if none were removed.
	 */
	public NonTerminalSymbol removeNthNonTerminal(int n) {
		return removeNthNonTerminal(n, 0, null);
	}

	/*
	 * Recursive helper method for the removeNthNonTerminal method.
	 */
	private NonTerminalSymbol removeNthNonTerminal(int n, int current, GrammarRule rule) {
		for (int i = 0; i < children.size(); i++) {
			Symbol child = children.get(i);

			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;

				boolean valid = false;
				if ((rule == null) || rule.equals(nt.getGrammarRule())) {
					valid = true;
				}

				if (valid && (n == current + 1)) {
					// It is this child.
					return (NonTerminalSymbol) removeChild(i);
				} else {
					final NonTerminalSymbol nth = nt.removeNthNonTerminal(n, (valid ? current + 1 : current), rule);

					if (nth != null) {
						return nth;
					}

					current += nt.getNoNonTerminalSymbols(rule);
				}
			}
		}

		return null;
	}

	/**
	 * Removes the nth non-terminal with the given <tt>GrammarRule</tt> from
	 * the parse tree rooted at this <tt>NonTerminalSymbol</tt> instance, as
	 * counted using a pre-order traversal. Indexing starts at zero and from
	 * this non-terminal symbol itself. It does not make sense to remove this
	 * <tt>Symbol</tt> from itself however so a value for <tt>n</tt> of
	 * <tt>0</tt> is only possible if the given <tt>GrammarRule</tt>
	 * does not match this <tt>Symbol</tt>.
	 * 
	 * @param n an <tt>int</tt> which is the index of the
	 *        <tt>NonTerminalSymbol</tt> with the given grammar rule that
	 *        should be
	 *        removed.
	 * @param grammarRule the <tt>GrammarRule</tt> that the symbol to be
	 *        removed should have.
	 * @return the <tt>NonTerminalSymbol</tt> that was removed, or
	 *         <tt>null</tt> if none were removed.
	 */
	public NonTerminalSymbol removeNthNonTerminal(int n, GrammarRule grammarRule) {
		return removeNthNonTerminal(n, 0, grammarRule);
	}

	/**
	 * Returns the nth non-terminal from the parse tree rooted at this
	 * <tt>NonTerminalSymbol</tt>. Indexing starts at zero for this symbol
	 * as the root, and proceeds in a pre-order traversal of the tree.
	 * 
	 * @param n the index of the non-terminal to return.
	 * @return the <tt>NonTerminalSymbol</tt> which was the nth in the parse
	 *         tree.
	 */
	public NonTerminalSymbol getNthNonTerminal(int n) {
		return getNthNonTerminal(n, 0);
	}

	/*
	 * Recursive helper method for the getNthNonTerminal method.
	 */
	private NonTerminalSymbol getNthNonTerminal(int n, int current) {
		// Is this the one we're looking for?
		if (n == current) {
			return this;
		}

		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;

				NonTerminalSymbol nth = nt.getNthNonTerminal(n, current + 1);

				if (nth != null) {
					return nth;
				}

				current += nt.getNoNonTerminalSymbols();
			}
		}

		return null;
	}

	/**
	 * Returns the nth terminal from the parse tree rooted at this
	 * <tt>NonTerminalSymbol</tt>. Indexing starts at zero and proceeds
	 * according to the order that terminals are met while performing a
	 * pre-order traversal of the tree from this symbol.
	 * 
	 * @param n the index of the terminal to return.
	 * @return the <tt>TerminalSymbol</tt> which was the nth in the parse
	 *         tree.
	 */
	public TerminalSymbol getNthTerminal(int n) {
		final List<TerminalSymbol> terminals = getTerminalSymbols();

		return terminals.get(n);
	}

	/**
	 * Returns the nth symbol from the parse tree rooted at this symbol.
	 * Indexing starts at zero for this, the root, and proceeds in a pre-order
	 * traversal of the tree until the nth symbol is found.
	 * 
	 * @param n the index of the symbol to be returned.
	 * @return the nth symbol from this parse tree.
	 */
	public Symbol getNthSymbol(int n) {
		return getNthSymbol(n, 0);
	}

	/*
	 * Recursive helper method for the getNthSymbol method.
	 */
	private Symbol getNthSymbol(int n, int current) {
		// Is this the one we're looking for?
		if (n == current) {
			return this;
		}

		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;

				Symbol nth = nt.getNthSymbol(n, current + 1);

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
	 * Overwrites the nth symbol in the parse tree rooted at this symbol.
	 * Indexing starts at zero for this, the root and proceeds in a pre-order
	 * traversal of the tree until the nth symbol is found. However, it is not
	 * possible to set the zeroth symbol since that would mean replacing this
	 * instance itself. To replace it, the <tt>setNthSymbol</tt> method
	 * should be called upon any parent <tt>NonTerminalSymbol</tt> or if it
	 * is the root of the whole tree then by using the replacement directly as
	 * the new parse tree.
	 * 
	 * @param n the index of where to set the new symbol.
	 * @param newSymbol the replacement <tt>Symbol</tt> to set at the nth
	 *        position.
	 */
	public void setNthSymbol(int n, Symbol newSymbol) {
		setNthSymbol(n, newSymbol, 0);
	}

	/*
	 * Recursive helper method for the setNthSymbol method.
	 */
	private void setNthSymbol(int n, Symbol symbol, int current) {
		int noChildren = getNoChildren();
		for (int i = 0; i < noChildren; i++) {
			if (current + 1 == n) {
				setChild(i, symbol);
				break;
			}

			if (children.get(i) instanceof NonTerminalSymbol) {
				NonTerminalSymbol child = (NonTerminalSymbol) children.get(i);
				int noChildSymbols = child.getNoSymbols();

				// Only look at the subtree if it contains the right range of
				// nodes.
				if (n <= current + noChildSymbols) {
					child.setNthSymbol(n, symbol, current + 1);
				}

				current += noChildSymbols;
			} else {
				// It's a terminal so just increment 1.
				current++;
			}
		}
	}

	/**
	 * Returns a <tt>List</tt> of all the non-terminal symbols in the parse
	 * tree below this symbol, including this symbol itself.
	 * 
	 * @return a <tt>List</tt> of <tt>NonTerminalSymbol</tt> instances
	 *         from the parse tree rooted at this symbol.
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
	 * Returns a <tt>List</tt> of the indexes of all the symbols in the
	 * parse tree rooted at this symbol that are instances of
	 * <tt>NonTerminalSymbol</tt>.
	 * 
	 * @return a <tt>List</tt> of <tt>Integers</tt> which are the
	 *         indexes of the non-terminal symbols in the parse tree rooted at
	 *         this symbol.
	 */
	public List<Integer> getNonTerminalIndexes() {
		return getNonTerminalIndexes(0);
	}

	/*
	 * Recursive helper method for the getNonTerminalIndexes method.
	 */
	private List<Integer> getNonTerminalIndexes(int index) {
		List<Integer> nonTerminals = new ArrayList<Integer>();

		// Start by adding self.
		nonTerminals.add(index);

		// Add all the non-terminals below each child.
		for (Symbol child: children) {
			if (child instanceof NonTerminalSymbol) {
				NonTerminalSymbol nt = (NonTerminalSymbol) child;
				nonTerminals.addAll(nt.getNonTerminalIndexes(index + 1));
				index += nt.getNoSymbols();
			} else {
				index++;
			}
		}

		return nonTerminals;
	}

	/**
	 * Returns a <tt>List</tt> of all the terminal symbols in the parse
	 * tree below this non-terminal symbol.
	 * 
	 * @return a <tt>List</tt> of <tt>TerminalSymbol</tt> instances
	 *         from the parse tree rooted at this symbol.
	 */
	public List<TerminalSymbol> getTerminalSymbols() {
		List<TerminalSymbol> terminals = new ArrayList<TerminalSymbol>();

		// Add all terminal children and terminals below a non-terminal child.
		for (Symbol child: children) {
			if (child instanceof TerminalSymbol) {
				terminals.add((TerminalSymbol) child);
			} else if (child instanceof NonTerminalSymbol) {
				terminals.addAll(((NonTerminalSymbol) child).getTerminalSymbols());
			}
		}

		return terminals;
	}

	/**
	 * Returns a <tt>List</tt> of all <tt>Symbol</tt> instances from the
	 * parse tree rooted at this symbol.
	 * 
	 * @return a <tt>List</tt> of <tt>Symbol</tt> instances from the
	 *         parse tree rooted at this symbol.
	 */
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

	/**
	 * Returns this non-terminal symbol's grammar rule.
	 * 
	 * @return the underlying grammar rule this non-terminal symbol is defined
	 *         by.
	 */
	public GrammarRule getGrammarRule() {
		return grammarRule;
	}

	/**
	 * Returns the depth of the parse tree rooted at this
	 * <tt>NonTerminalSymbol</tt>. The depth is considered to be the maximum
	 * number of steps down the tree from this symbol to a terminal symbol. A
	 * tree made up of one non-terminal symbol with all terminal children will
	 * have a depth of <tt>1</tt>.
	 * 
	 * @return the depth of the parse tree rooted at this symbol.
	 */
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

	/**
	 * Returns a string representation of this non-terminal symbol, which is a
	 * conjunction of the string representations of each child symbol.
	 * 
	 * @return a <tt>String</tt> representation of this object.
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(children.size());
		for (Symbol c: children) {
			buffer.append(c.toString());
		}

		return buffer.toString();
	}

	/**
	 * Constructs and returns a copy of this non-terminal symbol. Each child
	 * <tt>Symbol</tt> is itself cloned, but the grammar rule is shallow
	 * copied.
	 * 
	 * @return a copy of this non-terminal symbol.
	 */
	@Override
	public NonTerminalSymbol clone() {
		NonTerminalSymbol clone = null;
		try {
			clone = (NonTerminalSymbol) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is
			// going to blow up anyway.
			assert false;
		}

		// Copy cloned child symbols.
		clone.children = new ArrayList<Symbol>();
		for (Symbol c: children) {
			clone.children.add(c.clone());
		}

		// Shallow copy the grammar rules.
		clone.grammarRule = grammarRule;

		return clone;
	}

	/**
	 * Tests the given <tt>Object</tt> for equality with this non-terminal
	 * symbol. They will be considered equal if the given <tt>Object</tt> is
	 * an instance of <tt>NonTerminalSymbol</tt>, all their child symbols
	 * are equal according to the contract of their <tt>equals</tt> method,
	 * in the same order, and their grammar rules refer to the same instance.
	 * 
	 * @param obj the <tt>Object</tt> to test for equality.
	 * @return <tt>true</tt> if the given <tt>Object</tt> is equal to
	 *         this non-terminal according to the contract outlined above and
	 *         <tt>false</tt> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equal = true;

		if ((obj != null) && (obj instanceof NonTerminalSymbol)) {
			NonTerminalSymbol otherSymbol = (NonTerminalSymbol) obj;

			if (getGrammarRule() == otherSymbol.getGrammarRule()) {
				for (int i = 0; i < children.size(); i++) {
					Symbol thatChild = otherSymbol.getChild(i);
					Symbol thisChild = getChild(i);

					if (!ObjectUtils.equals(thisChild, thatChild)) {
						equal = false;
						break;
					}
				}
			} else {
				equal = false;
			}
		} else {
			equal = false;
		}

		return equal;
	}
}
