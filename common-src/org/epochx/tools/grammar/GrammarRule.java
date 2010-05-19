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
 * A GrammarRule is a component of a grammar parse tree that matches a 
 * rule of a BNF language grammar. Each non-terminal should have one or more 
 * grammarProductions which are the valid mappings for that rule.
 */
public class GrammarRule implements GrammarNode, Cloneable {

	private List<GrammarProduction> grammarProductions;
	
	private String name;
	
	private boolean recursive;
	
	private int minDepth;
	
	/**
	 * Constructs a GrammarRule with the specified name label and the 
	 * possible grammarProductions.
	 * 
	 * @param name the label that identifies this non-terminal rule.
	 * @param grammarProductions a list of all the grammarProductions that are possible 
	 * 					  mappings for this symbol rule.
	 */
	public GrammarRule(String name, List<GrammarProduction> grammarProductions) {
		this.name = name;
		this.grammarProductions = grammarProductions;
	}
	
	/**
	 * Constructs a GrammarRule with the specified name label but no 
	 * grammarProductions. Productions can be added after construction using 
	 * <code>addProduction(GrammarProduction)</code>.
	 * 
	 * @param name the label that identifies this non-terminal rule.
	 */
	public GrammarRule(String name) {
		this(name, new ArrayList<GrammarProduction>());
	}
	
	/**
	 * Constructs a GrammarRule with no specified name or grammarProductions.
	 */
	public GrammarRule() {
		this(null);
	}
	
	/**
	 * Append the specified production to the list of GrammarProduction options.
	 * 
	 * @param grammarProduction the production instance to be appended to the 
	 * 					 production options.
	 */
	public void addProduction(GrammarProduction grammarProduction) {
		grammarProductions.add(grammarProduction);
	}
	
	/**
	 * Inserts the specified production at the specified position in the list 
	 * of production options. The production currently at that position (if 
	 * any) and any subsequent grammarProductions will be shifted to the right.
	 * 
	 * @param index index at which the specified production is to be inserted.
	 * @param grammarProduction production element to be inserted.
	 */
	public void addProduction(int index, GrammarProduction grammarProduction) {
		grammarProductions.add(index, grammarProduction);
	}
	
	/**
	 * Replaces the production at the specified position in the list of 
	 * grammarProductions.
	 * 
	 * @param index index of the production to replace.
	 * @param grammarProduction production to be stored at the specified position.
	 */
	public void setProduction(int index, GrammarProduction grammarProduction) {
		grammarProductions.set(index, grammarProduction);
	}
	
	/**
	 * Returns the production at the specified position in the list of 
	 * grammarProductions.
	 * 
	 * @param index index of the element to return.
	 * @return the production at the specified position in the list of 
	 * grammarProductions.
	 */
	public GrammarProduction getProduction(int index) {
		return grammarProductions.get(index);
	}
	
	/**
	 * Returns a list of all the grammarProductions in this rule.
	 * 
	 * @return a list of this rule's grammarProductions.
	 */
	public List<GrammarProduction> getProductions() {
		return grammarProductions;
	}
	
	/**
	 * Returns the quantity of grammarProductions in this rule.
	 * 
	 * @return the number of grammarProductions in this rule.
	 */
	public int getNoProductions() {
		return grammarProductions.size();
	}
	
	/**
	 * Returns the name of the rule symbol as given in the grammar string.
	 * 
	 * @return the name that references this non-terminal symbol.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns whether this grammar symbol refers to itself directly or 
	 * indirectly.
	 * 
	 * <p>A rule is defined as recursive if any of the following are true:
	 * <ul>
	 * <li>The right hand side of the production rule contains the non-terminal
	 * on the left hand side.</li>
	 * <li>The right hand side of the rule contains a non-terminal which points
	 * to a rule that is recursive due to any of the other two reasons.</li>
	 * <li>The right hand side of the rule may contain a non-terminal that 
	 * leads back to the same production rule. Consider the mutually recursive 
	 * rules given below.</li>
	 * </ul>
	 * 
	 * @return true if this symbol's grammar rule is recursive, false 
	 * otherwise.
	 */
	public boolean isRecursive() {
		return recursive;
	}
	
	/**
	 * Specifies whether this non-terminal recursively refers to itself either 
	 * directly or indirectly.
	 * 
	 * @param recursive whether this non-terminal symbol recursively refers to 
	 * itself.
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
	
	/**
	 * Gets the minimum depth required to lead to all terminal symbols.
	 * 
	 * @return the minimum depth required to lead to all terminal symbols.
	 */
	public int getMinDepth() {
		/*int min = Integer.MAX_VALUE;
		for (GrammarProduction p: grammarProductions) {
			//TODO We should probably be doing something special for recursive symbols.
			int d = p.getMinDepth();
			if (d < min) {
				min = d;
			}
		}
		
		// We plus one for this symbol.
		return min + 1;*/
		
		return minDepth;
	}
	
	/**
	 * Sets the minimum depth required for this non-terminal symbol to lead to
	 * all terminals.
	 * 
	 * @param minDepth the minimum depth required to get to all terminals.
	 */
	public void setMinDepth(int minDepth) {
		this.minDepth = minDepth;
	}
	
	@Override
	public GrammarRule clone() {
		GrammarRule clone = null;
		try {
			clone = (GrammarRule) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		clone.name = this.name;
		clone.recursive = this.recursive;
		clone.minDepth = this.minDepth;
		
		// Clone the grammar productions (but this will not clone their rules).
		clone.grammarProductions = new ArrayList<GrammarProduction>(this.grammarProductions.size());
		for (GrammarProduction p: this.grammarProductions) {
			clone.grammarProductions.add(p.clone());
		}
		
		return clone;
	}
	
	/**
	 * Returns a string representation of this non-terminal symbol's rule.
	 * 
	 * @return a string representation of this symbol.
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('<');
		buffer.append(name);
		buffer.append('>');
		buffer.append(" ::= ");
		for (int i=0; i<grammarProductions.size(); i++) {
			if (i > 0) {
				buffer.append(" | ");
			}
			buffer.append(grammarProductions.get(i).toString());
		}
		return buffer.toString();
	}
}
