/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.model;

import org.epochx.ge.codon.CodonGenerator;
import org.epochx.ge.mapper.Mapper;
import org.epochx.ge.op.crossover.*;
import org.epochx.ge.op.init.GEInitialiser;
import org.epochx.ge.op.mutation.GEMutation;
import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;

/**
 * A GEModel defines all those parameters needed to control a run by GERun. 
 * The first step - and for most problems the only step - to generate a GE  
 * evolved solution with XGE is to provide a concrete implementation of 
 * this interface.
 * 
 * <p>For most situations, users should look to extend the abstract 
 * <code>GEAbstractModel.</code>
 *
 * @see GEAbstractModel
 */
public interface GEModel extends Model {

	/**
	 * Retrieves the GEInitialiser which will generate the first generation 
	 * population from which the evolution will proceed.
	 * 
	 * @return the GEInitialiser to create the first population.
	 */
	public GEInitialiser getInitialiser();

	/**
	 * Retrieves the implementation of GECrossover to use to perform the genetic 
	 * operation of crossover between 2 parents. The 2 parents to be crossed 
	 * over will be selected using the parent selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of GECrossover that will perform the genetic 
	 * 		   operation of crossover.
	 * @see FixedPointCrossover
	 * @see OnePointCrossover
	 */
	public GECrossover getCrossover();

	/**
	 * Retrieves the implementation of Mutator to use to perform the genetic 
	 * operation of mutation on a GECandidateProgram. The individual to be 
	 * mutated will be selected using the program selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of Mutator that will perform the genetic 
	 * 		   operation of mutation.
	 */
	public GEMutation getMutation();

	/**
	 * Returns the grammar instance that determines the structure of the 
	 * programs to be evolved. As well as defining the syntax of solutions, the 
	 * grammar also essentially determines the function and terminal sets which 
	 * are features of tree GP. 
	 * 
	 * @return the language grammar that defines the syntax of solutions.
	 */
	public Grammar getGrammar();

	/**
	 * Returns the mapper which should be used to perform the mapping from a 
	 * chromosome (List of Codons) to a source string with a syntax matching 
	 * the grammar. 
	 * 
	 * @return a Mapper to be used to map from chromosome to source.
	 */
	public Mapper getMapper();

	/**
	 * Returns the CodonGenerator that the system should use for generating any 
	 * new Codons.
	 * 
	 * @return the CodonGenerator to use for generating new codons.
	 */
	public CodonGenerator getCodonGenerator();
	
	/**
	 * Returns the maximum value of a codon. Codon values are positive integers 
	 * from zero to this size.
	 * 
	 * @return the maximum value of a codon.
	 */
	public int getMaxCodonSize();
	
	/**
	 * Returns the maximum number of codons that should be allowed in a 
	 * chromosome. Crossovers or mutations that result in a larger chromosome
	 * size will not be allowed.
	 * 
	 * @return the maximum number of codons to be allowed in a chromosome.
	 */
	public int getMaxChromosomeLength();
	
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
	 * Programs are selected. The GE system will attempt to improve the value 
	 * returned by this method over the generations. A fitness value of 0.0 is 
	 * better than a fitness value of 1.0.
	 * 
	 * <p>There are many ways in which a fitness score can be calculated, for 
	 * example mean squared error, standard deviation or a simple count of how 
	 * many known inputs the given program provides incorrect (known) outputs.
	 * For more information, the new user should read some of the genetic 
	 * programming literature.
	 * 
	 * <p>XGE provides Evaluators for executing programs evolved in a 
	 * recognised grammar. For those languages not currently supported, the 
	 * user may wish to implement their own Evaluator. Advice on doing this 
	 * should be found in the XGE documentation.
	 * 
	 * @param program	the GECandidateProgram to evaluate and calculate a score
	 * 					for.
	 * @return a double representing the quality of the program where a small 
	 * 		   value is considered better than a larger value.
	 */
	@Override
	public double getFitness(CandidateProgram program);
	
	/**
	 * Whether CandidatePrograms should cache their source code after mapping 
	 * to reduce the need for mapping when the codons are unchanged. Caching 
	 * the source potentially gives a large performance improvement and is 
	 * generally desirable but if the grammar might change during a run then 
	 * caching shouldn't be used as the same codons will evaluate to a 
	 * different source.
	 * 
	 * @return true if the source should be cached after mapping and false 
	 * otherwise.
	 */
	public boolean cacheSource();
}
