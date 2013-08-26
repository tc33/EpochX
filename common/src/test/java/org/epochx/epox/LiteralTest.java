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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.Literal}
 */
public class LiteralTest extends NodeTestCase {

	private Literal literal;
	private Object value;

	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	protected Node getNode() {
		return new Literal(new Object());
	}

	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		value = new Object();
		literal = new Literal(value);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#evaluate()} correctly returns
	 * the same value instance when evaluated.
	 */
	@Test
	public void testEvaluate() {
		final Object result = literal.evaluate();

		assertSame("literal does not evaluate to its set value", value, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#dataType()} correctly
	 * returns
	 * the class of the set value as the return type.
	 */
	@Test
	public void testGetReturnTypeLiteral() {
		final String strValue = "";
		literal.setValue(strValue);
		final Class<?> returnType = literal.dataType();

		assertSame("literal's return type should match the value's Class", String.class, returnType);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#dataType()} correctly
	 * returns
	 * the class of the set value as the return type.
	 */
	@Test
	public void testGetReturnTypeLiteralNull() {
		literal.setValue(null);
		final Class<?> returnType = literal.dataType();

		assertNull("getReturnType should be null for a null value", returnType);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#clone()} correctly clones
	 * instances.
	 */
	@Test
	public void testCloneLiteral() {
		final Literal clone = literal.clone();

		assertNotSame("literal has not been cloned", literal, clone);
		assertSame("value does not refer to the same instance", value, clone.getValue());
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#newInstance()} correctly
	 * constructs new instances.
	 */
	@Test
	public void testNewInstanceLiteral() {
		final Literal newInstance = literal.newInstance();

		assertNotSame("literal has not been cloned", literal, newInstance);
		assertSame("value does not refer to the same instance", value, newInstance.getValue());
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#toString()} returns the
	 * string representation of the literal's value.
	 */
	@Test
	public void testToStringLiteral() {
		final Object str = literal.toString();

		assertEquals("toString should be the values string representation", value.toString(), str);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#toString()} returns an
	 * empty string value if the literal's value is <code>null</code>.
	 */
	@Test
	public void testToStringNull() {
		literal.setValue(null);
		final Object str = literal.toString();

		assertEquals("toString should be an empty string for a null value", "", str);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#equals(Object)} returns a
	 * <code>true</code> value if the two literals' values are equal.
	 */
	@Test
	public void testEqualsLiteral() {
		final String strValue = "test";
		final String strValueCopy = new String(strValue);
		literal.setValue(strValue);

		final Literal alt = new Literal(strValueCopy);

		assertEquals("test is broken", strValue, strValueCopy);
		assertTrue("literals with equal values should be equal", literal.equals(alt));
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#equals(Object)} returns a
	 * <code>false</code> value if the two literals' values are not equal.
	 */
	@Test
	public void testEqualsLiteralFalse() {
		final String strValue = "test";
		literal.setValue(strValue);

		final Literal alt = new Literal(null);

		assertTrue("literals with non-equal values should not be equal", !literal.equals(alt));
	}
}
