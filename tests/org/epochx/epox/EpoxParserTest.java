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

import org.epochx.epox.bool.*;
import org.epochx.epox.math.*;
import org.epochx.tools.eval.*;
import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.EpoxParser}
 */
public class EpoxParserTest {

	private EpoxParser parser;
	
	private Node expectedTree;
	
	/**
	 * Sets up the test environment.
	 */
	@Before
	public void setup() {
		parser = new EpoxParser();
		
		expectedTree = new AndFunction(new Literal(Boolean.TRUE), new Literal(Boolean.FALSE));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} returns 
	 * <code>null</code> for a <code>null</code> source string.
	 */
	@Test
	public void testParseNull() {
		try {
			assertNull("a null source string should be parsed as null", parser.parse(null));
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a null source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} parses a 
	 * simple function with comma argument separation correctly.
	 */
	@Test
	public void testParseComma() {
		try {
			Node tree = parser.parse("AND(true,false)");
			assertEquals("unexpected tree constructed from parse", expectedTree, tree);
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a valid source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} parses a 
	 * simple function with space argument separation correctly.
	 */
	@Test
	public void testParseSpace() {
		try {
			Node tree = parser.parse("AND(true false)");
			assertEquals("unexpected tree constructed from parse", expectedTree, tree);
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a valid source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} parses a 
	 * simple function with comma and space argument separation correctly.
	 */
	@Test
	public void testParseCommaSpace() {
		try {
			Node tree = parser.parse("AND(true, false)");
			assertEquals("unexpected tree constructed from parse", expectedTree, tree);
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a valid source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} parses a 
	 * simple function with unnecessary white space, correctly.
	 */
	@Test
	public void testParseExtraSpace() {
		try {
			Node tree = parser.parse(" AND ( true, false ) ");
			assertEquals("unexpected tree constructed from parse", expectedTree, tree);
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a valid source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} throws an 
	 * exception when parsing an unknown function.
	 */
	@Test
	public void testParseUnknownFunction() {
		try {
			parser.parse("XXX(true false)");
			fail("malformed program exception not thrown for an unknown function");
		} catch (MalformedProgramException unexpected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} throws an 
	 * exception when the data-types are incompatible.
	 */
	@Test
	public void testParseDataTypes() {
		try {
			parser.parse("NOT(3)");
			fail("malformed program exception not thrown for incompatible data-types");
		} catch (MalformedProgramException unexpected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} throws an 
	 * exception when the data-types are incompatible.
	 */
	@Test
	public void testParseNullType() {
		try {
			MockVariable var = new MockVariable();
			var.setGetReturnType(null);
			var.setGetIdentifier("MOCK");
			
			parser.declareVariable(var);
			parser.parse("NOT(MOCK)");
			fail("malformed program exception not thrown for null return type");
		} catch (MalformedProgramException unexpected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parse(String)} correctly 
	 * parses a tree of depth 2.
	 */
	@Test
	public void testParseNested() {
		try {
			Variable varX = new Variable("X", Integer.class);
			parser.declareVariable(varX);
			Node tree = parser.parse("ADD(MUL(2 X) SUB(4 2))");
			
			Node expectedTree2 = new AddFunction(new MultiplyFunction(new Literal(2), varX), new SubtractFunction(new Literal(4), new Literal(2)));
			assertEquals("unexpected tree constructed from parse", expectedTree2, tree);
		} catch (MalformedProgramException unexpected) {
			fail("malformed program exception thrown for a valid source string");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses boolean literals.
	 */
	@Test
	public void testParseLiteralBoolean() {
		// True value.
		Literal literal = parser.parseLiteral(" true ");
		assertSame("boolean true literal not parsed as Boolean", Boolean.class, literal.getReturnType());
		assertEquals("boolean true literal not parsed correctly", Boolean.TRUE, literal.getValue());
		
		// False value.
		literal = parser.parseLiteral("false");
		assertSame("boolean false literal not parsed as Boolean", Boolean.class, literal.getReturnType());
		assertEquals("boolean false literal not parsed correctly", Boolean.FALSE, literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses String literals.
	 */
	@Test
	public void testParseLiteralString() {
		Literal literal = parser.parseLiteral("\"str test\"");
		assertSame("string literal not parsed as String", String.class, literal.getReturnType());
		assertEquals("string literal not parsed correctly", "str test", literal.getValue());
		
		// Empty string value.
		literal = parser.parseLiteral("\"\"");
		assertSame("empty string literal not parsed as String", String.class, literal.getReturnType());
		assertEquals("empty string literal not parsed correctly", "", literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses int literals.
	 */
	@Test
	public void testParseLiteralInt() {
		// Positive ints.
		Literal literal = parser.parseLiteral("3");
		assertSame("positive int literal not parsed as Integer", Integer.class, literal.getReturnType());
		assertEquals("positive int literal not parsed correctly", 3, literal.getValue());
		
		// Negative ints.
		literal = parser.parseLiteral("-3");
		assertSame("negative int literal not parsed as Integer", Integer.class, literal.getReturnType());
		assertEquals("negative int literal not parsed correctly", -3, literal.getValue());
		
		// Zero ints.
		literal = parser.parseLiteral("0");
		assertSame("zero int literal not parsed as Integer", Integer.class, literal.getReturnType());
		assertEquals("zero int literal not parsed correctly", 0, literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses long literals.
	 */
	@Test
	public void testParseLiteralLong() {
		// Long with capital L.
		Literal literal = parser.parseLiteral("3L");
		assertSame("long literal not parsed as Long", Long.class, literal.getReturnType());
		assertEquals("long literal not parsed correctly", 3L, literal.getValue());
		
		// Long with lower l.
		literal = parser.parseLiteral("-3l");
		assertSame("long literal not parsed as Long", Long.class, literal.getReturnType());
		assertEquals("long literal not parsed correctly", -3L, literal.getValue());
		
		// Long out of range of int.
		literal = parser.parseLiteral("" + (Integer.MAX_VALUE + 1L));
		assertSame("large long literal not parsed as Long", Long.class, literal.getReturnType());
		assertEquals("large long literal not parsed correctly", 2147483648L, literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses double literals.
	 */
	@Test
	public void testParseLiteralDouble() {
		// Double with capital D.
		Literal literal = parser.parseLiteral("3D");
		assertSame("double literal not parsed as Double", Double.class, literal.getReturnType());
		assertEquals("double literal not parsed correctly", 3.0d, literal.getValue());
		
		// Double with lower d.
		literal = parser.parseLiteral("-3d");
		assertSame("double literal not parsed as Double", Double.class, literal.getReturnType());
		assertEquals("double literal not parsed correctly", -3.0d, literal.getValue());
		
		// Double with decimal.
		literal = parser.parseLiteral("2.3");
		assertSame("double literal not parsed as Double", Double.class, literal.getReturnType());
		assertEquals("double literal not parsed correctly", 2.3d, literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses float literals.
	 */
	@Test
	public void testParseLiteralFloat() {
		// Float with capital F.
		Literal literal = parser.parseLiteral("3F");
		assertSame("float literal not parsed as Float", Float.class, literal.getReturnType());
		assertEquals("float literal not parsed correctly", 3.0f, literal.getValue());
		
		// Float with lower f.
		literal = parser.parseLiteral("-3f");
		assertSame("float literal not parsed as Float", Float.class, literal.getReturnType());
		assertEquals("float literal not parsed correctly", -3.0f, literal.getValue());
		
		// Float with decimal.
		literal = parser.parseLiteral("2.3f");
		assertSame("float literal not parsed as Float", Float.class, literal.getReturnType());
		assertEquals("float literal not parsed correctly", 2.3f, literal.getValue());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#parseLiteral(String)} 
	 * correctly parses char literals.
	 */
	@Test
	public void testParseLiteralChar() {
		Literal literal = parser.parseLiteral("'s'");
		assertSame("char literal not parsed as Character", Character.class, literal.getReturnType());
		assertEquals("char literal not parsed correctly", 's', literal.getValue());
		
		// Escaped char value.
		literal = parser.parseLiteral("'\t'");
		assertSame("escaped char literal not parsed as Character", Character.class, literal.getReturnType());
		assertEquals("escaped char literal not parsed correctly", '\t', literal.getValue());
	}
}
