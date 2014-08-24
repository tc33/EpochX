/*
 * Copyright 2007-2013
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
package org.epochx.cfg.operator;

import org.epochx.Individual;
import org.epochx.event.OperatorEvent;
import org.epochx.grammar.NonTerminalSymbol;

/**
 * An event fired at the end of a subtree crossover
 * 
 * @see SubtreeCrossover
 * 
 * @since 2.0
 */
public class SubtreeCrossoverEndEvent extends OperatorEvent.EndOperator {

	private NonTerminalSymbol subtree1;
	private NonTerminalSymbol subtree2;
	private int crossoverPoint1;
	private int crossoverPoint2;

	/**
	 * Constructs a <code>SubtreeCrossoverEndEvent</code> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the crossover
	 * @param parents an array of two individuals that the operator was
	 *        performed on
	 */
	public SubtreeCrossoverEndEvent(SubtreeCrossover operator, Individual[] parents) {
		super(operator, parents);
	}

	/**
	 * Returns an integer which is the index of the point chosen for the subtree crossover 
	 * operation. The index is from the list of all non-terminal symbols in the parse tree 
	 * of the second program, as would be returned by the <code>getNonTerminalSymbols</code> 
	 * method.
	 * 
	 * @return an integer which is the index of the crossover point
	 */
	public int getCrossoverPoint1() {
		return crossoverPoint1;
	}
	
	/**
	 * Sets the crossover position in the first parent's list of non-terminals
	 * 
	 * @param crossoverPoint1 index used as the crossover point in the first parent
	 */
	public void setCrossoverPoint1(int crossoverPoint1) {
		this.crossoverPoint1 = crossoverPoint1;
	}
	
	/**
	 * Returns an integer which is the index of the point chosen for the subtree crossover 
	 * operation. The index is from the list of all non-terminal symbols in the parse tree 
	 * of the second program, as would be returned by the <code>getNonTerminalSymbols</code> 
	 * method.
	 * 
	 * @return an integer which is the index of the crossover point
	 */
	public int getCrossoverPoint2() {
		return crossoverPoint2;
	}
	
	/**
	 * Sets the crossover position in the second parent's list of non-terminals
	 * 
	 * @param crossoverPoint2 index used as the crossover point in the second parent
	 */
	public void setCrossoverPoint2(int crossoverPoint2) {
		this.crossoverPoint2 = crossoverPoint2;
	}

	/**
	 * Returns the subtree taken from the first parent that was exchanged with the
	 * subtree from the second parent (as returned by <code>getSubtree2()</code>)
	 * 
	 * @return the root node of the first subtree
	 */
	public NonTerminalSymbol getSubtree1() {
		return subtree1;
	}

	/**
	 * Sets the subtree taken from the first parent that was exchanged with the subtree 
	 * from the second parent
	 * 
	 * @param subtree1 the root node of the first subtree
	 */
	public void setSubtree1(NonTerminalSymbol subtree1) {
		this.subtree1 = subtree1;
	}
	
	/**
	 * Returns the subtree taken from the second parent that was exchanged with the
	 * subtree from the first parent (as returned by <code>getSubtree1()</code>)
	 * 
	 * @return the root node of the second subtree
	 */
	public NonTerminalSymbol getSubtree2() {
		return subtree2;
	}

	/**
	 * Sets the subtree taken from the second parent that was exchanged with the subtree 
	 * from the first parent
	 * 
	 * @param subtree1 the root node of the second subtree
	 */
	public void setSubtree2(NonTerminalSymbol subtree2) {
		this.subtree2 = subtree2;
	}
}
