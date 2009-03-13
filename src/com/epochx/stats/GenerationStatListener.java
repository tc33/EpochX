/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.stats;

import com.epochx.stats.GenerationStats.*;

/**
 * 
 */
public interface GenerationStatListener {

	/**
	 * The implementing class must return an array of fields which the 
	 * listening objects are interested in listening to. Changes to the 
	 * set of fields during execution are unlikely to be honoured.
	 */
	public GenStatField[] getStatFields();
	
	public void generationStats(String[] stats);
	
}
