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
 * A GrammarProduction is a sequence of one or more Symbols that together produce a 
 * valid mapping of a non-terminal symbol (the left-hand side of a grammar 
 * rule).
 */
public class GrammarProduction {

	private List<GrammarNode> grammarNodes;
	
	private Map<String, Object> attributes;
	
	private double weight;
	
	public GrammarProduction(List<GrammarNode> grammarNodes, double weight) {
		this.grammarNodes = grammarNodes;
		this.weight = weight;
		
		attributes = new HashMap<String, Object>();
	}
	
	/**
	 * Constructs a production around the specified sequence of grammarNodes.
	 * 
	 * @param grammarNodes a list of grammarNodes that provides the mapping sequence for 
	 * 				  this production.
	 */
	public GrammarProduction(List<GrammarNode> grammarNodes) {
		this(grammarNodes, 1);
	}
	
	/**
	 * Constructs a production with no grammarNodes. Symbols should be added to the 
	 * production before use.
	 */
	public GrammarProduction() {
		this(new ArrayList<GrammarNode>());
	}
	
	/**
	 * Append the specified symbol to the list of grammarNodes in the production.
	 * 
	 * @param symbol the symbol to be appended to this production.
	 */
	public void addGrammarNode(GrammarNode symbol) {
		grammarNodes.add(symbol);
	}
	
	/**
	 * Returns a list of the grammarNodes that make up this production.
	 * 
	 * @return the sequence of grammarNodes that make up this production.
	 */
	public List<GrammarNode> getGrammarNodes() {
		return grammarNodes;
	}
	
	/**
	 * Returns the symbol at the specified index in the production.
	 * 
	 * @return the symbol at the specified index in this production.
	 */
	public GrammarNode getGrammarNode(int index) {
		return grammarNodes.get(index);
	}
	
	/**
	 * Returns the quantity of grammarNodes in this production.
	 * 
	 * @return the number of grammarNodes in this production.
	 */
	public int getNoChildren() {
		return grammarNodes.size();
	}
	
	/**
	 * Overwrite the weight associated with this production. Weights are used 
	 * as suggestions to bias the construction of parse trees. It is the 
	 * responsibility of the Initialiser (or other class creating a parse 
	 * tree) to use or ignore these weights.
	 * 
	 * @param weight the new weight to set to this production.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Return the weight associated with this production. Weights are used 
	 * as suggestions to bias the construction of parse trees. It is the 
	 * responsibility of the Initialiser (or other class creating a parse 
	 * tree) to use or ignore these weights. The default weight for a 
	 * production is 1.0.
	 * 
	 * @return the weight bias associated with this production.
	 */
	public double getWeight() {
		return weight;
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Set<String> getAttributeKeys() {
		return attributes.keySet();
	}
	
	/**
	 * Gets the minimum depth required to lead to all terminal grammarNodes.
	 * 
	 * @return the minimum depth required to lead to all terminal grammarNodes.
	 */
	public int getMinDepth() {
		// We have to use the largest of all the production's grammarNodes' minimum depths.
		int max = 0;
		for (GrammarNode s: grammarNodes) {
			int d = 0;
			if (s instanceof GrammarRule) {
				d = ((GrammarRule) s).getMinDepth();
			}
			
			if (d > max) {
				max = d;
			}
		}
		
		return max;
	}
	
	/**
	 * A production is recursive if any of its child grammarNodes are recursive.
	 * 
	 * @return true if this production is recursive and false otherwise.
	 */
	public boolean isRecursive() {
		boolean recursive = false;
		
		for (GrammarNode s: grammarNodes) {
			if (s instanceof GrammarRule) {
				recursive = ((GrammarRule) s).isRecursive();
			}
			
			if (recursive) {
				break;
			}
		}
		
		return recursive;
	}
	
	/**
	 * Returns a string representation of this production and its grammarNodes.
	 * 
	 * @return a string representation of this production.
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (GrammarNode s: grammarNodes) {
			if (s instanceof GrammarLiteral) {
				//TODO Need to implement escaping.
				buffer.append(((GrammarLiteral) s).toString());
			}
			if (s instanceof GrammarRule) {
				buffer.append('<');
				buffer.append(((GrammarRule) s).getName());
				buffer.append('>');
			}
			buffer.append(' ');
		}
		// Remove the last space.
		buffer.deleteCharAt(buffer.length()-1);	
			
		return buffer.toString();
	}
}
