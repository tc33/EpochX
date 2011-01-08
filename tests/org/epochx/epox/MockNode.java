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


/**
 * A mock node for the purpose of testing.
 */
public class MockNode extends Node {

	private Object evaluate;
	private String getIdentifier;
	
	/**
	 * Sets the <code>Object</code> that will be returned by the 
	 * <code>evaluate</code> method.
	 * 
	 * @param evaluate the Object to be returned by evaluate.
	 */
	public void setEvaluate(Object evaluate) {
		this.evaluate = evaluate;
	}
	
	/**
	 * Sets the <code>String</code> that will be returned by the 
	 * <code>getIdentifier</code> method.
	 * 
	 * @param getIdentifier the String to be returned by getIdentifier.
	 */
	public void setGetIdentifier(String getIdentifier) {
		this.getIdentifier = getIdentifier;
	}
	
	/**
	 * Returns the mock result of evaluation.
	 */
	@Override
	public Object evaluate() {
		return evaluate;
	}

	/**
	 * Returns the mock identifier.
	 */
	@Override
	public String getIdentifier() {
		return getIdentifier;
	}

}
