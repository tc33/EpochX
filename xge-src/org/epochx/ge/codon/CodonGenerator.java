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

/**
 * Implementations of <code>CodonGenerator</code> are used to generate new 
 * codon values for use in Chromosomes.
 */
public interface CodonGenerator {

	/**
	 * Generates a new codon value. A codon value is essentially just an 
	 * integer between 0 and the maximum codon size as specified by the 
	 * model.
	 * 
	 * @return a newly generated codon value.
	 */
	public int getCodon();
	
}
