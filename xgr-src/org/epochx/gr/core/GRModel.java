package org.epochx.gr.core;

import org.epochx.core.*;
import org.epochx.ge.op.crossover.*;
import org.epochx.gr.op.crossover.*;
import org.epochx.gr.op.init.*;
import org.epochx.gr.op.mutation.*;
import org.epochx.representation.*;
import org.epochx.tools.grammar.*;

/**
 * A GRModel defines all those parameters needed to control a run by GRRun. 
 * The first step - and for most problems the only step - to generate a GR  
 * evolved solution with XGR is to provide a concrete implementation of 
 * this interface.
 * 
 * <p>For most situations, users should look to extend the abstract 
 * <code>GRAbstractModel.</code>
 *
 * @see GRAbstractModel
 */
public interface GRModel extends Model {

	/**
	 * Retrieves the GPInitialiser which will generate the first generation 
	 * population from which the evolution will proceed.
	 * 
	 * @return the GPInitialiser to create the first population.
	 */
	public GRInitialiser getInitialiser();

	/**
	 * Retrieves the implementation of GPCrossover to use to perform the genetic 
	 * operation of crossover between 2 parents. The 2 parents to be crossed 
	 * over will be selected using the parent selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of GPCrossover that will perform the genetic 
	 * 		   operation of crossover.
	 * @see FixedPointCrossover
	 * @see OnePointCrossover
	 */
	public GRCrossover getCrossover();

	/**
	 * Retrieves the implementation of Mutator to use to perform the genetic 
	 * operation of mutation on a GRCandidateProgram. The individual to be 
	 * mutated will be selected using the program selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of Mutator that will perform the genetic 
	 * 		   operation of mutation.
	 */
	public GRMutation getMutation();

	/**
	 * Returns the grammar instance that determines the structure of the 
	 * programs to be evolved. As well as defining the syntax of solutions, the 
	 * grammar also essentially determines the function and terminal sets which 
	 * are features of tree GP. 
	 * 
	 * @return the language grammar that defines the syntax of solutions.
	 */
	public Grammar getGrammar();
	
	// This refers to the depth of the derivation tree.
	/**
	 * Returns the maximum depth of the derivation trees allowed. Crossovers or 
	 * mutations that result in a larger chromosome will not be allowed.
	 * 
	 * @return the maximum depth of derivation trees to allow.
	 */
	public int getMaxProgramDepth();
	
	/**
	 * Returns the maximum depth of the derivation trees allowed at 
	 * initialisation.
	 * 
	 * @return the maximum depth of derivation trees to allow after 
	 * initialisation.
	 */
	public int getMaxInitialProgramDepth();
	
	/**
	 * Calculates and returns the fitness score of the given program. The score 
	 * returned by this method provides the underlying way in which Candidate 
	 * Programs are selected. The GR system will attempt to improve the value 
	 * returned by this method over the generations. A fitness value of 0.0 is 
	 * better than a fitness value of 1.0.
	 * 
	 * <p>There are many ways in which a fitness score can be calculated, for 
	 * example mean squared error, standard deviation or a simple count of how 
	 * many known inputs the given program provides incorrect (known) outputs.
	 * For more information, the new user should read some of the genetic 
	 * programming literature.
	 * 
	 * <p>XGR provides Evaluators for executing programs evolved in a 
	 * recognised grammar. For those languages not currently supported, the 
	 * user may wish to implement their own Evaluator. Advice on doing this 
	 * should be found in the XGR documentation.
	 * 
	 * @param program	the GRCandidateProgram to evaluate and calculate a score
	 * 					for.
	 * @return a double representing the quality of the program where a small 
	 * 		   value is considered better than a larger value.
	 */
	@Override
	public double getFitness(CandidateProgram program);
	
	/**
	 * Whether CandidatePrograms should cache the program fitness after 
	 * evaluating to reduce the need for further evaluation when the codons 
	 * are unchanged, and so the fitness should not have changed. Caching 
	 * the fitness potentially gives a large performance improvement and is 
	 * generally desirable but if it is possible for the fitness function 
	 * to return different values for two programs with the same source then 
	 * caching shouldn't be used.
	 * 
	 * @return true if the fitness should be cached after evaluation and false 
	 * otherwise.
	 */
	public boolean cacheFitness();
}
