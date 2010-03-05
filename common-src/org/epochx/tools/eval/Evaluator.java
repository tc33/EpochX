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
 *//*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.eval;


/**
 * Evaluators are for executing CandidatePrograms. Evaluators are not currently 
 * used by the core XGE system, but are a facility for use in fitness functions 
 * to evaluate a range of programs.
 */
public interface Evaluator {

	/**
	 * Evaluate a <code>CandidateProgram</code> with the given argument values 
	 * assigned to the argument names specified.
	 * 
	 * @param program the CandidateProgram to be executed.
	 * @param argNames an array of arguments that the argValues should be 
	 * 				   assigned to. The array should have equal length to the 
	 * 				   argValues array.
	 * @param argValues an array of argument values to be assigned to the 
	 * 				    specified argument names. The array should have equal 
	 * 				    length to the argNames array.
	 * @return the return value of the CandidateProgram. The runtime type of 
	 * the returned Object may vary from program to program.
	 */
	public Object eval(String program, String[] argNames, Object[] argValues);
	
	/**
	 * Evaluate a <code>CandidateProgram</code> with a series of argument 
	 * values assigned to the argument names specified. The returned array will
	 * be the return values from the candidate program for each of the sets of 
	 * argument values in order. The number of evaluations that take place 
	 * should be equal to the size of the argNames/argValues arrays and also 
	 * equal to the size of the returned Object array.
	 * 
	 * <p>Some Evaluators may choose to simply implement this method by calling 
	 * the single use eval method, but for certain evaluators it may be 
	 * possible to get large performance gains with an interpreter by 
	 * performing all evaluations in one.
	 * 
	 * @param program the CandidateProgram to be executed.
	 * @param argNames an array of arguments that each of the sets of argValues 
	 * 				   should be assigned to. The array should have equal 
	 * 				   length to the argValues array.
	 * @param argValues argument values to be assigned to the specified argument 
	 * 					names. Each element is an array of argument values for 
	 * 					one evaluation. As such there should be argValues.length 
	 * 					evaluations and argValues.length elements in the 
	 * 					returned Object array. The array should also have equal 
	 * 				    length to the argNames array.
	 * @return the return value of the CandidateProgram. The type of the 
	 * returned Object may vary from program to program.
	 */
	public Object[] eval(String program, String[] argNames, Object[][] argValues);
	
}
