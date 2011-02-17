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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.epox;

import static org.junit.Assert.*;

import java.util.*;

import org.epochx.epox.bool.*;
import org.epochx.epox.math.*;
import org.epochx.interpret.*;
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
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareLiteral(String, Object)} 
	 * declares a new literal which is parsed correctly.
	 */
	@Test
	public void testDeclareLiteral() {
		Object value = new Object();
		parser.declareLiteral("MOCK", value);
		
		try {
			Node n = parser.parse("MOCK");
			
			assertTrue("literal node was expected", (n instanceof Literal));
			Literal literal = (Literal) n;
			assertSame("value of literal was not as expected", value, literal.getValue());
		} catch (MalformedProgramException e) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareLiteral(String, Object)} 
	 * overwrites existing literal declarations.
	 */
	@Test
	public void testDeclareLiteralOverwrite() {
		parser.declareLiteral("MOCK", new Object());
		
		Object value2 = new Object();
		parser.declareLiteral("MOCK", value2);
		
		try {
			Node n = parser.parse("MOCK");
			
			assertTrue("literal node was expected", (n instanceof Literal));
			Literal literal = (Literal) n;
			assertSame("value of literal was not as expected", value2, literal.getValue());
		} catch (MalformedProgramException e) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#undeclareLiteral(String)} 
	 * removes the specified literal.
	 */
	@Test
	public void testUndeclareLiteral() {
		Object value = new Object();
		parser.declareLiteral("MOCK", value);
		
		try {
			Literal literal = (Literal) parser.parse("MOCK");
			assertSame("value of literal was not as expected", value, literal.getValue());
			parser.undeclareLiteral("MOCK");
		} catch (MalformedProgramException e) {
			fail("unexpected malformed program exception thrown");
		}
		
		try {
			parser.parse("MOCK");
			fail("malformed program exception should have been thrown for using undeclared literal");
		} catch (MalformedProgramException eexpected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareFunction(String, Class)} 
	 * declares a new useable function.
	 */
	@Test
	public void testDeclareFunction() {
		// Test the function does not already exist.
		try {
			parser.parse("mock()");
			fail("malformed program exception should have been thrown for unknown function");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		// Declare the function.
		parser.declareFunction("mock", MockNode.class);
		
		// Test that an exception is no longer thrown.
		try {
			parser.parse("mock()");
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareFunction(String, Class)} 
	 * overwrites existing standard functions.
	 */
	@Test
	public void testDeclareFunctionOverwrite() {
		// Test the ADD function is parsed as an AddFunction.
		try {
			Node n = parser.parse("ADD(3 4)");
			assertTrue("ADD should be parsed as an AddFunction", (n instanceof AddFunction));
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
		
		// Overwrite the ADD function.
		parser.declareFunction("ADD", MockNode.class);
		
		// Test the ADD function now parses to MockNode.
		try {
			Node n = parser.parse("ADD()");
			assertTrue("ADD function should have been overwritten with a MockNode", (n instanceof MockNode));
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareFunction(String, Node)} 
	 * declares a new useable function.
	 */
	@Test
	public void testDeclareFunctionNode() {
		// Test the function does not already exist.
		try {
			parser.parse("mock()");
			fail("malformed program exception should have been thrown for unknown function");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		// Declare the function.
		parser.declareFunction("mock", new MockNode());
		
		// Test that an exception is no longer thrown.
		try {
			parser.parse("mock()");
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareFunction(String, Node)} 
	 * does not overwrite standard functions.
	 */
	@Test
	public void testDeclareFunctionOverwriteStandard() {
		// Test the ADD function is parsed as an AddFunction.
		try {
			Node n = parser.parse("ADD(3 4)");
			assertTrue("ADD should be parsed as an AddFunction", (n instanceof AddFunction));
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
		
		// Overwrite the ADD function.
		parser.declareFunction("ADD", new MockNode());
		
		// Test the ADD function is still parsed as an AddFunction.
		try {
			Node n = parser.parse("ADD()");
			assertTrue("ADD should have been overwritten with MockNode", (n instanceof MockNode));
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#undeclareFunction(String)} 
	 * removes the specified function.
	 */
	@Test
	public void testUndeclareFunction() {
		parser.declareFunction("MOCK", MockNode.class);
		parser.declareFunction("MOCK", new MockNode());
		
		// Test the MOCK function is parsed.
		try {
			parser.parse("MOCK()");
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
		
		parser.undeclareFunction("MOCK");
		
		// Test the MOCK function is no longer parsed because both functions are removed.
		try {
			parser.parse("MOCK()");
			fail("malformed program exception should have been thrown for undeclared function");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#undeclareFunction(String)} 
	 * removes the specified standard function.
	 */
	@Test
	public void testUndeclareFunctionStandard() {
		// Test the ADD function is parsed.
		try {
			parser.parse("ADD(3 4)");
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
		
		parser.undeclareFunction("ADD");
		
		// Test the ADD function no longer parses.
		try {
			parser.parse("ADD(3 4)");
			fail("malformed program exception should have been thrown for undeclared function");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareVariables(java.util.List)}
	 * declares multiple variables.
	 */
	@Test
	public void testDeclareVariables() {
		// Test the MOCK1 variable does not parse.
		try {
			parser.parse("MOCK1");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		// Test the MOCK2 variable does not parse.
		try {
			parser.parse("MOCK2");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		// Declare the variables.
		List<Variable> vars = new ArrayList<Variable>();
		Variable var1 = new Variable("MOCK1", Boolean.class);
		Variable var2 = new Variable("MOCK2", Boolean.class);
		vars.add(var1);
		vars.add(var2);
		parser.declareVariables(vars);
		
		// Test both MOCK1 and MOCK2 variables now parse.
		try {
			Node mock1 = parser.parse("MOCK1");
			Node mock2 = parser.parse("MOCK2");
			assertSame("Declared variables not parsing correctly", var1, mock1);
			assertSame("Declared variables not parsing correctly", var2, mock2);
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareVariables(java.util.List)}
	 * throws an exception for a <code>null</code> list.
	 */
	@Test
	public void testDeclareVariablesNull() {
		try {
			parser.declareVariables(null);
			fail("illegal argument exception should have been thrown for null list");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#declareVariable(Variable)}
	 * declares the given variable.
	 */
	@Test
	public void testDeclareVariable() {
		// Test the MOCK variable does not parse.
		try {
			parser.parse("MOCK");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		// Declare the variable.
		Variable var = new Variable("MOCK", Boolean.class);
		parser.declareVariable(var);
		
		// Test MOCK variable now parses.
		try {
			Node mock = parser.parse("MOCK");
			assertSame("Declared variable not parsing correctly", var, mock);
		} catch (MalformedProgramException unexpected) {
			fail("unexpected malformed program exception thrown");
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#undeclareVariable(Variable)}
	 * removes a declared variable.
	 */
	@Test
	public void testUndeclareVariable() {
		// Declare the variables.
		Variable var = new Variable("MOCK", Boolean.class);
		parser.declareVariable(var);
		
		// Test MOCK variable now parses.
		try {
			Node mock = parser.parse("MOCK");
			assertSame("Declared variables not parsing correctly", var, mock);
		} catch (MalformedProgramException expected) {
			fail("unexpected malformed program exception thrown");
		}
		
		// Undeclare variable.
		parser.undeclareVariable(var);
		
		// Test that variables does not now parse.
		try {
			parser.parse("MOCK");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.EpoxParser#undeclareAllVariables()}
	 * removes all declared variables.
	 */
	@Test
	public void testUndeclareAllVariables() {
		// Declare the variables.
		List<Variable> vars = new ArrayList<Variable>();
		Variable var1 = new Variable("MOCK1", Boolean.class);
		Variable var2 = new Variable("MOCK2", Boolean.class);
		vars.add(var1);
		vars.add(var2);
		parser.declareVariables(vars);
		
		// Test both MOCK1 and MOCK2 variables now parse.
		try {
			Node mock1 = parser.parse("MOCK1");
			Node mock2 = parser.parse("MOCK2");
			assertSame("Declared variables not parsing correctly", var1, mock1);
			assertSame("Declared variables not parsing correctly", var2, mock2);
		} catch (MalformedProgramException expected) {
			fail("malformed program exception should have been thrown for unknown variable");
		}
		
		// Undeclare all variables.
		parser.undeclareAllVariables();
		
		// Test that neither of the variables now parse.
		try {
			parser.parse("MOCK1");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
		
		try {
			parser.parse("MOCK2");
			fail("malformed program exception should have been thrown for unknown variable");
		} catch (MalformedProgramException expected) {
			assertTrue(true);
		}
	}
}
