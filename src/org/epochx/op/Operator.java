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
package org.epochx.op;

/**
 * Operators are those operations for which it is possible to provide pluggable
 * version in EpochX. Operators are not limited to the genetic operators of 
 * GPCrossover and GPMutation.
 */
public interface Operator {

		/**
		 * Operator statistics are those statistics which are unique to a specific 
		 * operator. An operator may provide any statistics about its run that it 
		 * wishes. Look at using the OPERATOR_STATS statistics fields for 
		 * requesting these statistics for an operation.
		 * 
		 * @return An Object array of operator specific statistics.
		 */
		public Object[] getOperatorStats();
	
}
