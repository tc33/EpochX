/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
package org.epochx.ge;

import org.epochx.Config.ConfigKey;

/**
 * Codon factories are responsible for generating new codons
 * 
 * @since 2.0
 */
public interface CodonFactory {

	/**
	 * The key for setting and retrieving the codon factory to generate new codons
	 */
	public static final ConfigKey<CodonFactory> CODON_FACTORY = new ConfigKey<CodonFactory>();
	
	/**
	 * Generates and returns a new random codon.
	 * 
	 * @return a new random codon
	 */
	public Codon codon();
	
	/**
	 * Generates and returns a new codon with the given value.
	 * 
	 * @param value the value to be assigned to the new codon
	 * @return a new codon instance
	 */
	public Codon codon(long value);
	
}
