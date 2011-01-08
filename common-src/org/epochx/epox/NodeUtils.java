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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox;

import java.util.*;

/**
 * This class provides static utility methods for working with Epox nodes.
 */
public final class NodeUtils {

	/**
	 * Returns those nodes from the given syntax that have an arity of 0.
	 * 
	 * @param syntax a List of Nodes.
	 * @return a List of those Node objects that have an arity of 0.
	 */
	public static List<Node> getTerminals(final List<Node> syntax) {
		final List<Node> terminals = new ArrayList<Node>(syntax.size());
		for (final Node n: syntax) {
			if (n.getArity() == 0) {
				terminals.add(n);
			}
		}

		return terminals;
	}

	/**
	 * Returns those nodes from the given syntax that have an arity of greater
	 * than 0.
	 * 
	 * @param syntax a List of Nodes.
	 * @return a List of those Node objects that have an arity of greater than
	 *         0.
	 */
	public static List<Node> getFunctions(final List<Node> syntax) {
		final List<Node> functions = new ArrayList<Node>(syntax.size());
		for (final Node n: syntax) {
			if (n.getArity() > 0) {
				functions.add(n);
			}
		}

		return functions;
	}

	public static List<Literal> intRange(int start, int end) {
		final List<Literal> range = new ArrayList<Literal>();
		if (start > end) {
			final int temp = start;
			start = end;
			end = temp;
		}

		while (start <= end) {
			start++;

			range.add(new Literal(start));
		}

		return range;
	}

	public static List<Variable> booleanVariables(final Class<?> datatype, final String ... variableNames) {
		final List<Variable> variables = new ArrayList<Variable>();

		for (final String name: variableNames) {
			variables.add(new Variable(name, datatype));
		}

		return variables;
	}

}
