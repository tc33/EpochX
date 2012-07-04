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
	private NodeUtils() {
	}

	/**
	 * Returns those nodes from the given syntax that have an arity of 0. The
	 * given <tt>List</tt> is not modified at all
	 * 
	 * @param syntax a <tt>List</tt> of <tt>Node</tt> objects. The syntax must
	 *        not be <tt>null</tt>
	 * @return a <tt>List</tt> of <tt>Node</tt> objects from the <tt>syntax</tt>
	 *         with arity of <tt>0</tt>
	 */
	public static List<Node> terminals(List<Node> syntax) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}

		List<Node> terminals = new ArrayList<Node>(syntax.size());
		for (Node n: syntax) {
			if (n.isTerminal()) {
				terminals.add(n);
			}
		}

		return terminals;
	}

	/**
	 * Returns those nodes from the given syntax that have an arity of greater
	 * than <tt>0</tt>. The given <tt>List</tt> is not modified at all.
	 * 
	 * @param syntax a <tt>List</tt> of <tt>Node</tt> objects. The syntax must
	 *        not be <tt>null</tt>
	 * @return a <tt>List</tt> of <tt>Node</tt> objects from the <tt>syntax</tt>
	 *         with arity <tt>&gt;0</tt>
	 */
	public static List<Node> nonTerminals(List<Node> syntax) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}

		List<Node> functions = new ArrayList<Node>(syntax.size());
		for (Node n: syntax) {
			if (n.isNonTerminal()) {
				functions.add(n);
			}
		}

		return functions;
	}

	/**
	 * Creates a <tt>List</tt> of <tt>Literal</tt> objects with a range
	 * of values. Given a <tt>start</tt> parameter of <tt>2</tt>, a
	 * <tt>quantity</tt> of <tt>4</tt> and an <tt>interval</tt> of <tt>3</tt>,
	 * the returned <tt>List</tt> will contain 4 literals
	 * with the values: <tt>2, 5, 8, 11</tt>.
	 * 
	 * @param start the value that should be used for the first <tt>Literal</tt>
	 *        in the range
	 * @param interval the interval between each element of the range
	 * @param quantity the number of elements in the range. Must be zero or
	 *        greater
	 * @return a <tt>List</tt> of <tt>Literals</tt> with the range of
	 *         values given
	 */
	public static List<Literal> intRange(int start, int interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}

		List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i = 0; i < quantity; i++) {
			int value = (i * interval) + start;

			range.add(new Literal(value));
		}

		return range;
	}

	/**
	 * Creates a <tt>List</tt> of <tt>Literal</tt> objects with a range
	 * of values. Given a <tt>start</tt> parameter of <tt>2L</tt>, a
	 * <tt>quantity</tt> of <tt>4</tt> and an <tt>interval</tt> of <tt>3L</tt>,
	 * the returned <tt>List</tt> will contain 4 literals
	 * with the values: <tt>2L, 5L, 8L, 11L</tt>.
	 * 
	 * @param start the value that should be used for the first <tt>Literal</tt>
	 *        in the range
	 * @param interval the interval between each element of the range
	 * @param quantity the number of elements in the range
	 * @return a <tt>List</tt> of <tt>Literals</tt> with the range of
	 *         values given
	 */
	public static List<Literal> longRange(long start, long interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}

		List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i = 0; i < quantity; i++) {
			long value = (i * interval) + start;

			range.add(new Literal(value));
		}

		return range;
	}

	/**
	 * Creates a <tt>List</tt> of <tt>Literal</tt> objects with a range
	 * of values. Given a <tt>start</tt> parameter of <tt>2.2</tt>, a
	 * <tt>quantity</tt> of <tt>4</tt> and an <tt>interval</tt> of <tt>3.2</tt>,
	 * the returned <tt>List</tt> will contain 4 literals
	 * with the values: <tt>2.2, 5.4, 8.6, 11.8</tt>.
	 * 
	 * @param start the value that should be used for the first <tt>Literal</tt>
	 *        in the range
	 * @param interval the interval between each element of the range
	 * @param quantity the number of elements in the range
	 * @return a <tt>List</tt> of <tt>Literals</tt> with the range of
	 *         values given
	 */
	public static List<Literal> doubleRange(double start, double interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}

		List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i = 0; i < quantity; i++) {
			double value = (i * interval) + start;

			range.add(new Literal(value));
		}

		return range;
	}

	/**
	 * Creates a <tt>List</tt> of <tt>Literal</tt> objects with a range
	 * of values. Given a <tt>start</tt> parameter of <tt>2.2f</tt>, a
	 * <tt>quantity</tt> of <tt>4</tt> and an <tt>interval</tt> of <tt>3.2f</tt>
	 * , the returned <tt>List</tt> will contain 4 literals
	 * with the values: <tt>2.2f, 5.4f, 8.6f, 11.8f</tt>
	 * 
	 * @param start the value that should be used for the first <tt>Literal</tt>
	 *        in the range.
	 * @param interval the interval between each element of the range.
	 * @param quantity the number of elements in the range.
	 * @return a <tt>List</tt> of <tt>Literals</tt> with the range of
	 *         values given.
	 */
	public static List<Literal> floatRange(float start, float interval, int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity must be 0 or greater");
		}

		final List<Literal> range = new ArrayList<Literal>(quantity);

		for (int i = 0; i < quantity; i++) {
			float value = (i * interval) + start;

			range.add(new Literal(value));
		}

		return range;
	}

	/**
	 * Creates a <tt>List</tt> of <tt>Variable</tt> objects of the given
	 * data type. The number of variables created will match the number of
	 * variable names provided. The variables will all have a <tt>null</tt>
	 * value.
	 * 
	 * @param datatype the data-type for all the variables to be created
	 * @param variableNames the names to assign to each of the variables
	 * @return a <tt>List</tt> of <tt>Variable</tt>s with the given names
	 */
	public static List<Variable> createVariables(Class<?> datatype, String ... variableNames) {
		if (variableNames == null) {
			throw new IllegalArgumentException("variableNames must not be null");
		}

		List<Variable> variables = new ArrayList<Variable>();

		for (String name: variableNames) {
			variables.add(new Variable(name, datatype));
		}

		return variables;
	}

}
