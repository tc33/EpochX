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
import org.epochx.stats.*;

/**
 * Model implementation for the experimental nano-java system.
 */
public abstract class GXModel extends Model {
	
	// Control parameters.
	private int maxDepth;
	private int maxInitialDepth;
	
	private ProgramGenerator programGenerator;
	
	/**
	 * Construct a GXModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GXModel() {
		// Use the default stats engine.
		getStatsManager().setStatsEngine(new StatsEngine(this));
		
		// Set default parameter values.
		maxDepth = 14;
		maxInitialDepth = 8;
		
		programGenerator = new ProgramGenerator();
		
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
		super.run();
	}
	
	public ProgramGenerator getProgramGenerator() {
		return programGenerator;
	}

	/**
	 * Returns the maximum depth of the derivation trees allowed. Crossovers or 
	 * mutations that result in a larger chromosome will not be allowed.
	 * 
	 * <p>Defaults to 14.
	 * 
	 * @return the maximum depth of derivation trees to allow.
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation 
	 * tree.
	 * 
	 * <p>Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxDepth the maximum depth to allow a program's derivation tree.
	 */
	public void setMaxDepth(final int maxDepth) {
		if (maxDepth >= 1 || maxDepth == -1) {
			this.maxDepth = maxDepth;
		} else {
			throw new IllegalArgumentException("maxDepth must either be -1 or greater than 0");
		}
		
		assert (this.maxDepth >= 1 || this.maxDepth == -1);
	}
	
	/**
	 * Returns the maximum depth of the derivation trees allowed at 
	 * initialisation.
	 * 
	 * <p>Defaults to 8.
	 * 
	 * @return the maximum depth of derivation trees to allow after 
	 * initialisation.
	 */
	public int getMaxInitialDepth() {
		return maxInitialDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation 
	 * tree after initialisation.
	 * 
	 * <p>Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxDepth the maximum depth to allow a program's derivation tree
	 * 				   after initialisation.
	 */
	public void setMaxInitialDepth(final int maxInitialDepth) {
		if (maxInitialDepth >= 1 || maxInitialDepth == -1) {
			this.maxInitialDepth = maxInitialDepth;
		} else {
			throw new IllegalArgumentException("maxInitialDepth must either be -1 or greater than 0");
		}
		
		assert (this.maxInitialDepth >= 1 || this.maxInitialDepth == -1);
	}
}
