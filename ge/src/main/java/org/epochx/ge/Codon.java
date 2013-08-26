/*
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.ge;

import org.epochx.Config.ConfigKey;

/**
 * 
 */
public interface Codon extends Cloneable {
	
	/**
	 * The key for setting and retrieving the maximum value for an integer
	 * codon
	 */
	public static final ConfigKey<Long> MAXIMUM_VALUE = new ConfigKey<Long>();
	
	/**
	 * The key for setting and retrieving the minimum value for an integer
	 * codon
	 */
	public static final ConfigKey<Long> MINIMUM_VALUE = new ConfigKey<Long>();

	public long value();
	
}
