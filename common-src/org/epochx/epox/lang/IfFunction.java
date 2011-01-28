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
package org.epochx.epox.lang;

import org.epochx.epox.Node;
import org.epochx.tools.util.TypeUtils;

/**
 * A function node which represents the conditional if-then-else
 * statement.
 */
public class IfFunction extends Node {

	/**
	 * Constructs an IfFunction with three <code>null</code> children.
	 */
	public IfFunction() {
		this(null, null, null);
	}

	/**
	 * Constructs an IfFunction with three child nodes.
	 * 
	 * @param condition a boolean child node which will determine which of the
	 *        other nodes are evaluated.
	 * @param ifStatement the child node to be evaluated if the condition
	 *        evaluates to true.
	 * @param elseStatement the child node to be evaluated if the condition
	 *        evaluates to false.
	 */
	public IfFunction(final Node condition, final Node ifStatement, final Node elseStatement) {
		super(condition, ifStatement, elseStatement);
	}

	/**
	 * Evaluates this function. The first child node is evaluated, the
	 * result of which must be a <code>Boolean</code> instance. If the result
	 * is a true value then the second child is also evaluated, the result of
	 * which becomes the result of this function. If the first child
	 * evaluated to a false value then the third child is evaluated and its
	 * result returned.
	 */
	@Override
	public Object evaluate() {
		final boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();

		if (c1) {
			return getChild(1).evaluate();
		} else {
			return getChild(2).evaluate();
		}
	}

	/**
	 * Returns the identifier of this function which is IF.
	 */
	@Override
	public String getIdentifier() {
		return "IF";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there are three children, the first of which has the data-type
	 * Boolean, then the return type of this function will be whichever of the
	 * second and third children is a super type the other. If neither of the
	 * other two children are a subclass of the other, then these input types
	 * are invalid and <code>null</code> will be returned.
	 * 
	 * @return The <code>Boolean</code> class or <code>null</code> if the input
	 *         type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 3) && (inputTypes[0] == Boolean.class)) {
			return TypeUtils.getSuper(inputTypes[1], inputTypes[2]);
		} else {
			return null;
		}
	}
}
