/* 
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
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
package org.epochx.gp.init;

import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.gp.STGPIndividual;


/**
 * Initialisation method for <tt>STGPIndividual</tt>s. It does not define any 
 * methods, but initialisation methods that generate <tt>STGPIndividual</tt> 
 * objects should implement this interface. 
 * 
 * <p>
 * Where appropriate, implementations should use the 
 * <tt>MAXIMUM_INITIAL_DEPTH</tt> configuration parameter defined in this 
 * interface to specify the maximum depth of the program trees they generate. 
 * The {@link STGPIndividual#MAXIMUM_DEPTH} parameter should also be enforced.
 */
public interface STGPInitialisation extends InitialisationMethod {

	/**
	 * The key for setting and retrieving the maximum initial depth setting for 
	 * program trees
	 */
	public static final ConfigKey<Integer> MAXIMUM_INITIAL_DEPTH = new ConfigKey<Integer>();
	
}
