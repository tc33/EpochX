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
package org.epochx.gx.model;

import org.epochx.core.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.representation.*;
import org.epochx.stats.*;

/**
 * Model implementation for the experimental nano-java system.
 */
public abstract class GXModel extends Model {
	
	// Control parameters.
	private int maxNoStatements;
	private int minNoStatements;
	
	private VariableHandler variableHandler;
	
	private String methodName;
	
	private DataType returnType;
	
	/**
	 * Construct a GXModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GXModel() {
		// Use the default stats engine.
		getStatsManager().setStatsEngine(new StatsEngine(this));
		
		// Set default parameter values.
		maxNoStatements = 10;
		minNoStatements = 3;
		
		methodName = "getResult";
		
		variableHandler = new VariableHandler(this);
		
		// Operators.
		setInitialiser(new ExperimentalInitialiser(this));
		setCrossover(null);
		setMutation(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (getReturnType() == null) {
			throw new IllegalStateException("no return type set");
		}
		
		super.run();
	}
	
	public VariableHandler getVariableHandler() {
		return variableHandler;
	}
	
	public void setVariableHandler(VariableHandler variableHandler) {
		this.variableHandler = variableHandler;
	}

	/**
	 * 
	 */
	public int getMaxNoStatements() {
		return maxNoStatements;
	}

	/**
	 * 
	 */
	public void setMaxNoStatements(final int maxNoStatements) {
		if (maxNoStatements >= 1 || maxNoStatements == -1) {
			this.maxNoStatements = maxNoStatements;
		} else {
			throw new IllegalArgumentException("maximum number of statements must either be -1 or greater than 0");
		}
		
		assert (this.maxNoStatements >= 1 || this.maxNoStatements == -1);
	}
	
	/**
	 * 
	 */
	public int getMinNoStatements() {
		return minNoStatements;
	}

	/**
	 * 
	 */
	public void setMinNoStatements(final int minNoStatements) {
		this.minNoStatements = minNoStatements;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the returnType
	 */
	public DataType getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(DataType returnType) {
		this.returnType = returnType;
	}
}
