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
package org.epochx.gp.util;

import java.util.*;

import org.epochx.epox.*;

/*
 * TODO We seem to have 2 NodeUtils classes?!
 */
public final class NodeUtils {

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

	public static List<Variable> booleanVariables(Class<?> datatype, final String ... variableNames) {
		final List<Variable> variables = new ArrayList<Variable>();

		for (final String name: variableNames) {
			variables.add(new Variable(name, datatype));
		}

		return variables;
	}
}
