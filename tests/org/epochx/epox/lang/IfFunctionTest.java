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
package org.epochx.epox.lang;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.epox.lang.IfFunction;
import org.epochx.gp.representation.AbstractBooleanNodeTestCase;
import org.junit.Test;

/**
 * 
 */
public class IfFunctionTest extends AbstractBooleanNodeTestCase {

	private IfFunction ifFunction;

	@Override
	public Node getNode() {
		return new IfFunction();
	}

	@Override
	public void setUp() {
		super.setUp();

		ifFunction = (IfFunction) getNode();
		final Node[] children = new Node[]{null, new Literal(true), new Literal(false)};
		ifFunction.setChildren(children);
	}

	@Test
	public void testEvaluateT() {
		ifFunction.setChild(0, new Literal(true));
		final boolean result = (Boolean) ifFunction.evaluate();

		assertTrue("IF did not evaluate 2nd child when 1st was true", result);
	}

	@Test
	public void testEvaluateF() {
		ifFunction.setChild(0, new Literal(false));
		final boolean result = (Boolean) ifFunction.evaluate();

		assertFalse("IF did not evaluate 3rd child when 1st was false", result);
	}

}
