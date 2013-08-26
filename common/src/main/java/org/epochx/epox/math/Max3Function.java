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
package org.epochx.epox.math;

import org.epochx.epox.Node;

/**
 * A node which performs a comparison of three numeric inputs and
 * returns the greater of the three.
 */
public class Max3Function extends MaxFunction {

	/**
	 * Constructs a Max3Function with three <tt>null</tt> children.
	 */
	public Max3Function() {
		this(null, null, null);
	}

	/**
	 * Constructs a Max3Function with three numerical child nodes.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 * @param child3 The third child node.
	 */
	public Max3Function(Node child1, Node child2, Node child3) {
		super(child1, child2, child3);
	}

	/**
	 * Returns the identifier of this function which is MAX3.
	 */
	@Override
	public String getIdentifier() {
		return "MAX3";
	}

}
