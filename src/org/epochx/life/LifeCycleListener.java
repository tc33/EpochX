/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.life;


/**
 * Implementations of LifeCycleListener can be used to listen for events 
 * throughout the life of a GP run. The provided methods will be called at the 
 * relevant times during execution, and have the opportunity to confirm the 
 * operation by returning the appropriate argument value, modify the operation 
 * by altering the argument before returning and in some cases it is possible 
 * to trigger a 'reversion' by returning null. A reversion is where the 
 * operation is discarded and rerun. In the case of reversion, the life cycle 
 * method will be recalled in the same way during the rerun, allowing the life 
 * cycle listener the ability to revert indefinitely. It is worth noting that 
 * this leaves room for the potential of an infinite loop if any of these 
 * methods were defined to return null in all possible circumstances. The 
 * number of reversions for crossover and mutation is obtainable from the 
 * crossover stats and mutation stats respectively.
 */
public interface LifeCycleListener<TYPE> extends CrossoverListener<TYPE>, ElitismListener<TYPE>,
		GenerationListener, InitialisationListener<TYPE>, MutationListener<TYPE>,
		PoolSelectionListener<TYPE>, ReproductionListener<TYPE>, TerminationListener {
	
	
}
