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
package org.epochx.epox;

import java.util.*;

/**
 * This class provides static utility methods for working with Epox nodes.
 */
public final class NodeUtils {

	/*
	 * Private constructor to prevent instantiation.
	 */
	private NodeUtils() {}
	
	/**
	 * Returns those nodes from the given syntax that have an arity of 0. The 
	 * given <code>List</code> is not modified at all.
	 * 
	 * @param syntax a List of Nodes. The syntax must not be <code>null</code>.
	 * @return a List of those Node objects that have an arity of 0. 
	 */
	public static List<Node> getTerminals(final List<Node> syntax) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}
		
		final List<Node> terminals = new ArrayList<Node>(syntax.size());
		for (final Node n: syntax) {
			if (n.isTerminal()) {
				terminals.add(n);
			}
		}

		return terminals;
	}

	/**
	 * Returns those nodes from the given syntax that have an arity of greater
	 * than 0. The given <code>List</code> is not modified at all.
	 * 
	 * @param syntax a List of Nodes. The syntax must not be <code>null</code>.
	 * @return a List of those Node objects that have an arity of greater than
	 *         0.
	 */
	public static List<Node> getFunctions(final List<Node> syntax) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}
		
		final List<Node> functions = new ArrayList<Node>(syntax.size());
		for (final Node n: syntax) {
			if (n.isFunction()) {
				functions.add(n);
			}
		}

		return functions;
	}

	/**
	 * Creates a <code>List</code> of <code>Literal</code> objects with a range
	 * of values. Given a <code>start</code> parameter of <code>2</code>, a 
	 * <code>quantity</code> of <code>4</code> and an <code>interval</code> of
	 * <code>3</code>, the returned <code>List</code> will contain 4 literals 
	 * with the values: 2, 5, 8, 11.
	 * 
	 * @param start the value that should be used for the first 
	 * <code>Literal</code> in the range.
	 * @param interval the interval between each element of the range.
	 * @param quantity the number of elements in the range. Must be zero or 
	 * greater.
	 * @return a <code>List</code> of <code>Literals</code> with the range of 
	 * values given.
	 */
	public static List<Literal> intRange(int start, int interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}
		
		final List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i=0; i<quantity; i++) {
			int value = (i * interval) + start;
			
			range.add(new Literal(value));
		}
		
		return range;
	}
	
	/**
	 * Creates a <code>List</code> of <code>Literal</code> objects with a range
	 * of values. Given a <code>start</code> parameter of <code>2L</code>, a 
	 * <code>quantity</code> of <code>4</code> and an <code>interval</code> of
	 * <code>3L</code>, the returned <code>List</code> will contain 4 literals 
	 * with the values: 2L, 5L, 8L, 11L.
	 * 
	 * @param start the value that should be used for the first 
	 * <code>Literal</code> in the range.
	 * @param interval the interval between each element of the range.
	 * @param quantity the number of elements in the range.
	 * @return a <code>List</code> of <code>Literals</code> with the range of 
	 * values given.
	 */
	public static List<Literal> longRange(long start, long interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}
		
		final List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i=0; i<quantity; i++) {
			long value = (i * interval) + start;
			
			range.add(new Literal(value));
		}
		
		return range;
	}
	
	/**
	 * Creates a <code>List</code> of <code>Literal</code> objects with a range
	 * of values. Given a <code>start</code> parameter of <code>2.2</code>, a 
	 * <code>quantity</code> of <code>4</code> and an <code>interval</code> of
	 * <code>3.2</code>, the returned <code>List</code> will contain 4 literals 
	 * with the values: 2.2, 5.4, 8.6, 11.8.
	 * 
	 * @param start the value that should be used for the first 
	 * <code>Literal</code> in the range.
	 * @param interval the interval between each element of the range.
	 * @param quantity the number of elements in the range.
	 * @return a <code>List</code> of <code>Literals</code> with the range of 
	 * values given.
	 */
	public static List<Literal> doubleRange(double start, double interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}
		
		final List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i=0; i<quantity; i++) {
			double value = (i * interval) + start;
			
			range.add(new Literal(value));
		}
		
		return range;
	}
	
	/**
	 * Creates a <code>List</code> of <code>Literal</code> objects with a range
	 * of values. Given a <code>start</code> parameter of <code>2.2f</code>, a 
	 * <code>quantity</code> of <code>4</code> and an <code>interval</code> of
	 * <code>3.2f</code>, the returned <code>List</code> will contain 4 literals 
	 * with the values: 2.2f, 5.4f, 8.6f, 11.8f.
	 * 
	 * @param start the value that should be used for the first 
	 * <code>Literal</code> in the range.
	 * @param interval the interval between each element of the range.
	 * @param quantity the number of elements in the range.
	 * @return a <code>List</code> of <code>Literals</code> with the range of 
	 * values given.
	 */
	public static List<Literal> floatRange(float start, float interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}
		
		final List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i=0; i<quantity; i++) {
			float value = (i * interval) + start;
			
			range.add(new Literal(value));
		}
		
		return range;
	}

	/**
	 * Creates a <code>List</code> of <code>Variable</code> objects of the given
	 * data type. The number of variables created will be the same as the number
	 * of variable names provided. The variables will have a <code>null</code>
	 * value.
	 * @param datatype the data-type for all the variables to be created.
	 * @param variableNames the names to assign to each of the variables.
	 * @return a <code>List</code> of <code>Variable</code>s with the given 
	 * names.
	 */
	public static List<Variable> createVariables(final Class<?> datatype, final String ... variableNames) {
		if (variableNames == null) {
			throw new IllegalArgumentException("variableNames must not be null");
		}
		
		final List<Variable> variables = new ArrayList<Variable>();

		for (final String name: variableNames) {
			variables.add(new Variable(name, datatype));
		}

		return variables;
	}

}
