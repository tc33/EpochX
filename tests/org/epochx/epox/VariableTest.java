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
package org.epochx.epox;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.Variable}
 */
public class VariableTest extends NodeTestCase {

	// Variable that has been initialised with a value.
	private Variable varInit;
	private Integer varInitValue;
	private String varInitName;

	// Variable that has been declared with a type, but no value.
	private Variable varDecl;
	private Class<Number> varDeclType;
	private String varDeclName;

	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	protected Node getNode() {
		return new Variable("identifier", Object.class);
	}

	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		varInitName = "X";
		varInitValue = new Integer(3);
		varInit = new Variable(varInitName, varInitValue);

		varDeclName = "Y";
		varDeclType = Number.class;
		varDecl = new Variable(varDeclName, varDeclType);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.Variable#Variable(String, Class)} 
	 * throws an exception for a null identifier.
	 */
	@Test
	public void testVariableTypeNullIdentifier() {
		try {
			new Variable(null, Boolean.class);
			fail("illegal argument exception not thrown for null identifier");
		} catch (final IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.Variable#Variable(String, Class)} 
	 * throws an exception for a null data-type.
	 */
	@Test
	public void testVariableTypeNullDataType() {
		try {
			new Variable("test", null);
			fail("illegal argument exception not thrown for null data-type");
		} catch (final IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.Variable#Variable(String, Object)} 
	 * throws an exception for a null identifier.
	 */
	@Test
	public void testVariableValueNullIdentifier() {
		try {
			new Variable(null, Boolean.TRUE);
			fail("illegal argument exception not thrown for null identifier");
		} catch (final IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.Variable#Variable(String, Object)} 
	 * throws an exception for a null value.
	 */
	@Test
	public void testVariableValueNullDataType() {
		try {
			new Variable("test", null);
			fail("illegal argument exception not thrown for null value");
		} catch (final IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.Variable#setValue(Object)} allows a 
	 * null value to be set.
	 */
	@Test
	public void testSetValueNull() {
		try {
			varInit.setValue(null);
			assertNull("variables value should be set to null", varInit.getValue());
		} catch (final IllegalArgumentException unexpected) {
			fail("illegal argument exception incorrectly thrown when setting a null value");
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#setValue(Object)} throws an
	 * exception if setting a value of an incompatible type.
	 */
	@Test
	public void testSetValueInvalid() {
		try {
			varInit.setValue(new Double(3));
			fail("illegal argument exception not thrown for invalid value type");
		} catch (final IllegalArgumentException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#setValue(Object)} correctly
	 * sets a variables value if it is of a valid compatible type.
	 */
	@Test
	public void testSetValue() {
		try {
			final Double value = new Double(3);
			varDecl.setValue(value);
			assertSame("variable value not being set", value, varDecl.getValue());
		} catch (final IllegalArgumentException unexpected) {
			fail("illegal argument exception should not be thrown for assignable value type");
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#setValue(Object)} does not
	 * change the return type.
	 */
	@Test
	public void testSetValueType() {
		varDecl.setValue(new Double(3.4));
		final Class<?> returnType = varDecl.getReturnType();

		assertSame("variable's return type should not change once set", Number.class, returnType);
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#evaluate()} correctly returns
	 * the same value instance when evaluated.
	 */
	@Test
	public void testEvaluateInit() {
		final Object result = varInit.evaluate();

		assertSame("variable does not evaluate to its set value", varInitValue, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#evaluate()} correctly returns
	 * the same value instance when evaluated.
	 */
	@Test
	public void testEvaluateDecl() {
		final Integer value = new Integer(2);
		varDecl.setValue(value);
		final Object result = varDecl.evaluate();

		assertSame("variable does not evaluate to its set value", value, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#getReturnType()} correctly
	 * returns the class of the set value as the return type.
	 */
	@Test
	public void testGetReturnTypeVariable() {
		final Class<?> returnType = varInit.getReturnType();

		assertSame("variable's return type should match the varInitValue's Class", varInitValue.getClass(), returnType);
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#clone()} correctly clones
	 * instances.
	 */
	@Test
	public void testCloneVariable() {
		final Variable clone = varInit.clone();

		assertSame("cloned variable does not refer to the same instance", varInit, clone);
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#newInstance()} correctly
	 * constructs new instances.
	 */
	@Test
	public void testNewInstanceVariable() {
		final Variable newInstance = varDecl.newInstance();

		assertSame("new instance of variable does not refer to the same instance", varDecl, newInstance);
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#equals(Object)} returns a
	 * <code>true</code> value if the two references refer to the same Variable
	 * instance.
	 */
	@Test
	public void testEqualsVariable() {
		assertTrue("two references to the same variable instance should be equal", varDecl.equals(varDecl));
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#equals(Object)} returns a
	 * <code>false</code> value if the two references refer to different
	 * Variable instances, even if they have the same value.
	 */
	@Test
	public void testEqualsFalse() {
		varDecl.setValue(varInitValue);

		assertSame("unit test broken", varInit.getValue(), varDecl.getValue());
		assertFalse("two different variable instances cannot be equal", varDecl.equals(varInit));
	}

	/**
	 * Tests that {@link org.epochx.epox.Variable#toString()} returns the
	 * identifier as the string representation.
	 */
	@Test
	public void testToString() {
		assertEquals("string representation of variable should be the identifier", varInitName, varInit.toString());
		assertEquals("string representation of variable should be the identifier", varDeclName, varDecl.toString());
	}

}
