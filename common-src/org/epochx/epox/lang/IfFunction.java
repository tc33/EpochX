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
package org.epochx.epox.lang;

import org.epochx.epox.*;
import org.epochx.tools.util.TypeUtils;

/**
 * A <code>FunctionNode</code> which represents the conditional if-then-else
 * statement.
 */
public class IfFunction extends Node {

	/**
	 * Construct an IfFunction with no children.
	 */
	public IfFunction() {
		this(null, null, null);
	}

	/**
	 * Construct an IfFunction with three children. When evaluated, if the first
	 * child evaluates to true then the second child is evaluated and return,
	 * otherwise the third child is evaluated and returned.
	 * 
	 * @param condition The first child node.
	 * @param ifStatement The second child node.
	 * @param elseStatement The third child node.
	 */
	public IfFunction(final Node condition, final Node ifStatement, final Node elseStatement) {
		super(condition, ifStatement, elseStatement);
	}

	/**
	 * Evaluating an <code>IfFunction</code> involves evaluating the first
	 * child,
	 * if it evaluates to true then the second child is evaluated as the result.
	 * Otherwise the third child is evaluated and returned.
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
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the IfFunction which is IF.
	 */
	@Override
	public String getIdentifier() {
		return "IF";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if ((inputTypes.length == 3) 
				&& (inputTypes[0] == Boolean.class)) {
			return TypeUtils.getSuper(inputTypes[1], inputTypes[2]);
		} else {
			return null;
		}
	}
}
