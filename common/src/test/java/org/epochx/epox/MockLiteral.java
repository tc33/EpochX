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


/**
 * A mock literal for the purpose of testing.
 */
public class MockLiteral extends Literal {

	private Class<?> getReturnType;
	
	private Object getValue;
	
	private Object evaluate;
	private String getIdentifier;
	
	/**
	 * Constructs a <code>MockLiteral</code> with a <code>null</code> value.
	 */
	public MockLiteral() {
		super(null);
	}
	
	/**
	 * Sets the <code>Class<?></code> that will be returned by the
	 * <code>getReturnType</code> method.
	 * 
	 * @param getReturnType the Class<?> to be returned by getReturnType.
	 */
	public void setGetReturnType(Class<?> getReturnType) {
		this.getReturnType = getReturnType;
	}
	
	/**
	 * Returns the mock return type of this literal.
	 * 
	 * @return the mock return type of this literal.
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		return getReturnType;
	}
	
	/**
	 * Sets the <code>Object</code> that will be returned by the 
	 * <code>getValue</code> method.
	 * 
	 * @param getValue the Object to be returned by getValue.
	 */
	public void setGetValue(Object getValue) {
		this.getValue = getValue;
	}

	/**
	 * Returns the mock value of this literal.
	 * 
	 * @return the mock value of this literal.
	 */
	@Override
	public Object getValue() {
		return getValue;
	}
	
	/**
	 * Sets the <code>String</code> that will be returned by the
	 * <code>getIdentifier</code> method.
	 * 
	 * @param getIdentifier the String to be returned by getIdentifier.
	 */
	public void setGetIdentifier(final String getIdentifier) {
		this.getIdentifier = getIdentifier;
	}
	
	/**
	 * Returns the mock identifier.
	 * 
	 * @return the mock identifier.
	 */
	@Override
	public String getIdentifier() {
		return getIdentifier;
	}
	
	/**
	 * Sets the <code>Object</code> that will be returned by the
	 * <code>evaluate</code> method.
	 * 
	 * @param evaluate the Object to be returned by evaluate.
	 */
	public void setEvaluate(final Object evaluate) {
		this.evaluate = evaluate;
	}

	/**
	 * Returns the mock result of evaluation.
	 */
	@Override
	public Object evaluate() {
		return evaluate;
	}
}
