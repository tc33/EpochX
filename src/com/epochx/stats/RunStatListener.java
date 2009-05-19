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


/**
 * RunStatsListener uses the listener pattern to provide a flexible way 
 * of accessing statistics about each run.
 * 
 * Models which require statistics about runs at the end of each run 
 * would typically implement this interface. GPAbstractModel already provides 
 * an implementation. There are 2 steps to using this interface to gain access 
 * to run statistics.
 * <ul>
 * <li>Implement <b>getRunStatFields()</b> to return an array of the 
 * RunStatFields required, in the order desired.</li>
 * <li>Implement <b>runStats(Object[])</b> which will receive these 
 * statistics in that order to use them however required - print to screen, 
 * file, etc.</li>
 * </ul>
 */
public interface RunStatListener {

	/**
	 * The implementing class must return an array of fields which the 
	 * listening objects are interested in listening to. The values for 
	 * each of the fields this method returns will be given as part of the 
	 * stats array passed to the runStats(Object[]) array.
	 */
	public RunStatField[] getRunStatFields();
	
	/**
	 * This method will be called after every run completes with an array 
	 * containing the statistics requested with getRunStatFields().
	 * @param stats An array of statistics relating to the last run 
	 * completed. The order of the array will match the order that the 
	 * fields were requested in the return of getRunStatFields() method. 
	 * The array is of type Object[] but the dynamic type of each element will 
	 * vary depending on the field. For information of types view the comments 
	 * on the stats fields.
	 */
	public void runStats(int run, Object[] stats);
	
}
