/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.ge.codon;

import org.epochx.ge.core.*;

/**
 * Generates codon values randomly between zero and the maximum codon size as
 * specified by the model given as an argument to the constructor.
 */
public class StandardGenerator implements CodonGenerator {

	private GEModel model;
	
	/**
	 * Construct a StandardGenerator.
	 * 
	 * @param model the model that controls the run, providing the maximum 
	 * 				codon size.
	 */
	public StandardGenerator(GEModel model) {
		this.model = model;
	}
	
	/**
	 * Generates and returns a new codon value, randomly generated with a 
	 * uniform distribution between zero and the maximum codon size. 
	 * 
	 * @return the newly generated codon value.
	 */
	@Override
	public int getCodon() {
		return model.getRNG().nextInt(model.getMaxCodonSize());
	}
}
