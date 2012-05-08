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
package org.epochx.epox.bool;

import org.epochx.epox.Node;
import org.epochx.tools.TypeUtils;

/**
 * A node which performs exclusive disjunction, also known as exclusive OR
 */
public class XorFunction extends Node {

	/**
	 * Constructs an <tt>XorFunction</tt> with two <tt>null</tt> children
	 */
	public XorFunction() {
		this(null, null);
	}

	/**
	 * Constructs an <tt>XorFunction</tt> with two boolean child nodes
	 * 
	 * @param child1 the first child node
	 * @param child2 the second child node
	 */
	public XorFunction(Node child1, Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluates this function. Both child nodes are evaluated, the result of
	 * both must be a <tt>Boolean</tt> instance. The result of this
	 * function will be <tt>true</tt> if either (but not both) children evaluate
	 * to <tt>true</tt>, otherwise the result will be <tt>false</tt>.
	 */
	@Override
	public Boolean evaluate() {
		boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();
		boolean c2 = ((Boolean) getChild(1).evaluate()).booleanValue();

		return c1 != c2;
	}

	/**
	 * Returns the identifier of this function which is <tt>XOR</tt>
	 */
	@Override
	public String getIdentifier() {
		return "XOR";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there are two children, both of which have a return type of 
	 * <tt>Boolean</tt>, then the return type of this function will also be 
	 * <tt>Boolean</tt>. In all other cases this method will return 
	 * <tt>null</tt> to indicate that the inputs are invalid.
	 * 
	 * @return The Boolean class or null if the input type is invalid.
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		if ((inputTypes.length == 2) && TypeUtils.allEqual(inputTypes, Boolean.class)) {
			return Boolean.class;
		} else {
			return null;
		}
	}
}
