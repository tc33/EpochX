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
package org.epochx.core;

import java.util.List;

import org.epochx.op.crossover.*;
import org.epochx.op.initialisation.GPInitialiser;
import org.epochx.op.mutation.GPMutation;
import org.epochx.op.selection.*;
import org.epochx.representation.*;


/**
 * A GPModel defines all those parameters needed to control a run by GPRun. 
 * The first step - and for most problems the only step - to generate a GP  
 * evolved solution with EpochX is to provide a concrete implementation of 
 * this interface.
 * 
 * <p>For most situations, users should look to extend the abstract 
 * <code>GPAbstractModel.</code>
 *
 * @param <TYPE> The return type of CandidatePrograms being evolved.
 * @see GPAbstractModel
 */
public interface GPModel<TYPE> extends Model {
	
	/**
	 * Retrieves the GPInitialiser which will generate the first generation 
	 * population from which the evolution will proceed.
	 * 
	 * @return the GPInitialiser to create the first population.
	 */
	@Override
	public GPInitialiser<TYPE> getInitialiser();

	/**
	 * Retrieves the implementation of GPCrossover to use to perform the genetic 
	 * operation of crossover between 2 parents. The 2 parents to be crossed 
	 * over will be selected using the parent selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of GPCrossover that will perform the genetic 
	 * 		   operation of crossover.
	 * @see UniformPointCrossover
	 * @see KozaCrossover
	 */
	@Override
	public GPCrossover<TYPE> getCrossover();

	/**
	 * Retrieves the implementation of Mutator to use to perform the genetic 
	 * operation of mutation on a GPCandidateProgram. The individual to be 
	 * mutated will be selected using the program selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of Mutator that will perform the genetic 
	 * 		   operation of mutation.
	 */
	@Override
	public GPMutation<TYPE> getMutation();

	/**
	 * Retrieves the selector to use to pick parents from either a pre-selected 
	 * breeding pool (selected by the GPPoolSelector returned by 
	 * getPoolSelector()) or the previous population for use as input to the 
	 * genetic operators.
	 * 
	 * @return the GPProgramSelector which should be used to pick parents for input 
	 * 		   to the genetic operators.
	 * @see TournamentSelector
	 */
	@Override
	public GPProgramSelector<TYPE> getProgramSelector();

	/**
	 * Retrieves the selector to use to construct a breeding pool from which 
	 * parents can be selected using the parent selector returned by 
	 * getProgramSelector() to undergo the genetic operators.
	 * 
	 * @return a GPPoolSelector which can be used to construct a breeding pool, 
	 * 		   or null if a breeding pool shouldn't be used and instead parents 
	 * 		   should be picked straight from the previous population.
	 * @see TournamentSelector
	 */
	public GPPoolSelector<TYPE> getPoolSelector();

	/**
	 * Retrieves the set of terminal nodes. 
	 * 
	 * @return the terminal nodes to be used during evolution.
	 */
	public List<TerminalNode<TYPE>> getTerminals();

	/**
	 * Retrieves the set of function nodes.
	 * 
	 * @return the function nodes to be used during evolution.
	 */
	public List<FunctionNode<TYPE>> getFunctions();

	/**
	 * Retrieves the full set of syntax, that is terminals AND function nodes.
	 * This is usually a combination of the calls from getTerminals() and 
	 * getFunctions().
	 * 
	 * @return the full syntax for use in building node trees.
	 */
	public List<Node<TYPE>> getSyntax();

	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the 
	 * population after initialisation. The exact way in which the 
	 * implementation ensures this depth is kept to may vary.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the 
	 * 		   population after initialisation.
	 */
	public int getInitialMaxDepth();
	
	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the 
	 * population after undergoing genetic operators. The exact way in which 
	 * CandidatePrograms deeper than this limit are dealt with may vary, but 
	 * they will not be allowed to remain into the next generation unaltered.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the 
	 * 		   population after genetic operators.
	 */
	public int getMaxProgramDepth();

	/**
	 * Calculates and returns the fitness score of the given program. The score 
	 * returned by this method provides the underlying way in which Candidate 
	 * Programs are selected. The GP system will attempt to improve the value 
	 * returned by this method over the generations. A fitness value of 0.0 is 
	 * better than a fitness value of 1.0.
	 * 
	 * <p>There are many ways in which a fitness score can be calculated, for 
	 * example mean squared error, standard deviation or a simple count of how 
	 * many known inputs the given program provides incorrect (known) outputs.
	 * For more information, the new user should read some of the genetic 
	 * programming literature.
	 * 
	 * @param program	the GPCandidateProgram to evaluate and calculate a score
	 * 					for.
	 * @return a double representing the quality of the program where a small 
	 * 		   value is considered better than a larger value.
	 */
	public double getFitness(GPCandidateProgram<TYPE> program);

	/**
	 * This method will be called during each crossover operation before the 
	 * crossover is accepted, giving the model the opportunity to reject the 
	 * operation, in which case the operation will be attempted again until it
	 * is accepted. The number of times crossovers are rejected is retrievable 
	 * using the REVERTED_CROSSOVERS stats field. 
	 * 
	 * @param parents The programs which have been crossed over to create the 
	 * given children.
	 * @param children The children which are the result of the crossover 
	 * operation having been performed on the given parents.
	 * @return True if the crossover operation should proceed, false if it is 
	 * rejected and should be retried with new parents.
	 */
	public boolean acceptCrossover(GPCandidateProgram<TYPE>[] parents, 
								   GPCandidateProgram<TYPE>[] children);

	/**
	 * This method will be called during each mutation operation before the 
	 * mutation is accepted, giving the model the opportunity to reject the 
	 * operation, in which case the operation will be attempted again until it
	 * is accepted. The number of times mutations are rejected is retrievable 
	 * using the REVERTED_MUTATIONS stats field.
	 * 
	 * @param parent The program before the mutation operation.
	 * @param child  The program after the mutation operation has been carried 
	 * 				 out.
	 * @return True if the mutation operation should proceed, false if it is 
	 * rejected and should be retried with a new parent.
	 */
	public boolean acceptMutation(GPCandidateProgram<TYPE> parent, 
			   					  GPCandidateProgram<TYPE> child);
}
