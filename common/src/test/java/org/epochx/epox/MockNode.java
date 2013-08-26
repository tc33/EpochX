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

/**
 * A mock node for the purpose of testing.
 */
public class MockNode extends Node {
	
	private int evaluateCount;
	
	private Object evaluate;
	private String getIdentifier;
	
	private Class<?> getReturnType;
	
	/**
	 * Constructs a <code>MockNode</code> with an identifier of 'mock' and a 
	 * return type of Void.
	 */
	public MockNode() {
		this.getIdentifier = "mock";
		this.getReturnType = Void.class;
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
	 * Sets the <code>String</code> that will be returned by the
	 * <code>getIdentifier</code> method.
	 * 
	 * @param getIdentifier the String to be returned by getIdentifier.
	 */
	public void setGetIdentifier(final String getIdentifier) {
		this.getIdentifier = getIdentifier;
	}

	/**
	 * Returns the mock result of evaluation and increments the counter of how
	 * many times this method has been called.
	 */
	@Override
	public Object evaluate() {
		evaluateCount++;

		return evaluate;
	}

	/**
	 * Returns the mock identifier.
	 */
	@Override
	public String getIdentifier() {
		return getIdentifier;
	}

	/**
	 * Returns a count for how many times the <code>evaluate</code> method has 
	 * been called.
	 * 
	 * @return an integer count of how many times the evaluate method has been 
	 * called.
	 */
	public int getEvaluateCount() {
		return evaluateCount;
	}
	
	/**
	 * Sets the <code>Class<?></code> that will be returned by the 
	 * <code>getReturnType</code> method.
	 * @param getReturnType the return type to be returned by getReturnType.
	 */
	public void setGetReturnType(Class<?> getReturnType) {
		this.getReturnType = getReturnType;
	}
	
	/**
	 * Returns the mock return type of this node.
	 * 
	 * @return the mock return type of this node.
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		return getReturnType;
	}
}
