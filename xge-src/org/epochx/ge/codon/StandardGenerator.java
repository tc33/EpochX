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
package org.epochx.ge.codon;

import org.epochx.ge.model.GEModel;
import org.epochx.life.ConfigAdapter;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Generates codon values randomly between zero and the maximum codon size as
 * specified by the model given as an argument to the constructor.
 */
public class StandardGenerator implements CodonGenerator {

	// The controlling model.
	private GEModel model;

	private RandomNumberGenerator rng;

	private int maxCodonSize;

	public StandardGenerator(RandomNumberGenerator rng, int maxCodonSize) {
		this.rng = rng;
		this.maxCodonSize = maxCodonSize;
	}
	
	/**
	 * Construct a StandardGenerator.
	 * 
	 * @param model the model that controls the run, providing the maximum
	 *        codon size.
	 */
	public StandardGenerator(final GEModel model) {
		this.model = model;

		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {

			@Override
			public void onConfigure() {
				configure();
			}
		});
	}

	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		rng = model.getRNG();
		maxCodonSize = model.getMaxCodonSize();
	}

	/**
	 * Generates and returns a new codon value, randomly generated with a
	 * uniform distribution between zero and the maximum codon size.
	 * 
	 * @return the newly generated codon value.
	 */
	@Override
	public int getCodon() {
		return rng.nextInt(maxCodonSize);
	}

	
	/**
	 * @return the model
	 */
	public GEModel getModel() {
		return model;
	}

	
	/**
	 * @param model the model to set
	 */
	public void setModel(GEModel model) {
		this.model = model;
	}

	
	/**
	 * @return the rng
	 */
	public RandomNumberGenerator getRng() {
		return rng;
	}

	
	/**
	 * @param rng the rng to set
	 */
	public void setRng(RandomNumberGenerator rng) {
		this.rng = rng;
	}

	
	/**
	 * @return the maxCodonSize
	 */
	public int getMaxCodonSize() {
		return maxCodonSize;
	}

	
	/**
	 * @param maxCodonSize the maxCodonSize to set
	 */
	public void setMaxCodonSize(int maxCodonSize) {
		this.maxCodonSize = maxCodonSize;
	}
	
	
}
