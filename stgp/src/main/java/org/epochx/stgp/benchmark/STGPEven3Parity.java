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
package org.epochx.stgp.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.Breeder;
import org.epochx.Config.ConfigKey;
import org.epochx.BranchedBreeder;
import org.epochx.EvolutionaryStrategy;
import org.epochx.FitnessEvaluator;
import org.epochx.GenerationalStrategy;
import org.epochx.GenerationalTemplate;
import org.epochx.Initialiser;
import org.epochx.MaximumGenerations;
import org.epochx.Operator;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.TerminationCriteria;
import org.epochx.TerminationFitness;
import org.epochx.epox.Node;
import org.epochx.epox.Variable;
import org.epochx.epox.VariableNode;
import org.epochx.epox.bool.And;
import org.epochx.epox.bool.Nand;
import org.epochx.epox.bool.Nor;
import org.epochx.epox.bool.Or;
import org.epochx.fitness.DoubleFitness;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.fitness.HitsCount;
import org.epochx.stgp.init.Full;
import org.epochx.stgp.operator.SubtreeCrossover;
import org.epochx.stgp.operator.SubtreeMutation;
import org.epochx.tools.BenchmarkSolutions;
import org.epochx.tools.BooleanUtils;

/**
 * This template sets up EpochX to run the even-3-parity benchmark with the 
 * STGP representation. The even-3-parity problem involves evolving a program
 * which receives an array of 3 boolean values. A program that solves the 
 * even-n-parity problem will return true in all circumstances where an even 
 * number of the inputValues are true (or 1), and return false whenever there 
 * is an odd number of true inputValues.
 *  
 * The following configuration is used:
 * 
 * <li>{@link Population#SIZE}: <code>100</code>
 * <li>{@link GenerationalStrategy#TERMINATION_CRITERIA}: <code>MaximumGenerations</code>, <code>TerminationFitness(0.0)</code>
 * <li>{@link MaximumGenerations#MAXIMUM_GENERATIONS}: <code>50</code>
 * <li>{@link STGPIndividual#MAXIMUM_DEPTH}: <code>6</code>
 * <li>{@link BranchedBreeder#SELECTOR}: <code>TournamentSelector</code>
 * <li>{@link TournamentSelector#TOURNAMENT_SIZE}: <code>7</code>
 * <li>{@link Breeder#OPERATORS}: <code>SubtreeCrossover</code>, <code>SubtreeMutation</code>
 * <li>{@link SubtreeMutation#PROBABILITY}: <code>0.0</code>
 * <li>{@link SubtreeCrossover#PROBABILITY}: <code>1.0</code>
 * <li>{@link Initialiser#METHOD}: <code>FullInitialisation</code>
 * <li>{@link RandomSequence#RANDOM_SEQUENCE}: <code>MersenneTwisterFast</code>
 * <li>{@link STGPIndividual#SYNTAX}: <code>AndFunction</code>, <code>OrFunction</code>, <code>NandFunction<code>, 
 * <code>NorFunction<code>, <code>VariableNode("D0", Boolean)<code>, <code>VariableNode("D1", Boolean)<code>, 
 * <code>VariableNode("D2", Boolean)<code>
 * <li>{@link STGPIndividual#RETURN_TYPE}: <code>Boolean</code>
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>HitsCount</code>
 * <li>{@link HitsCount#INPUT_VARIABLES}: <code>D0</code>, <code>D1</code>, <code>D2</code>
 * <li>{@link HitsCount#INPUT_VALUE_SETS}: [all possible binary input combinations]
 * <li>{@link HitsCount#EXPECTED_OUTPUTS}: [correct output for input value sets]
 * 
 * @since 2.0
 */
public class STGPEven3Parity extends GenerationalTemplate {
	
	private static final int NO_BITS = 3;
	
	/**
	 * Sets up the given template with the benchmark config settings
	 * 
	 * @param template a map to be filled with the template config
	 */
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
        template.put(Population.SIZE, 100);
        List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
        criteria.add(new TerminationFitness(new DoubleFitness.Minimise(0.0)));
        criteria.add(new MaximumGenerations());
        template.put(EvolutionaryStrategy.TERMINATION_CRITERIA, criteria);
        template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);
        template.put(STGPIndividual.MAXIMUM_DEPTH, 6);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new SubtreeCrossover());
        operators.add(new SubtreeMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(SubtreeCrossover.PROBABILITY, 1.0);
        template.put(SubtreeMutation.PROBABILITY, 0.0);
        template.put(Initialiser.METHOD, new Full());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup syntax
		List<Node> syntaxList = new ArrayList<Node>();
		syntaxList.add(new And());
		syntaxList.add(new Or());
		syntaxList.add(new Nand());
		syntaxList.add(new Nor());

		Variable[] variables = new Variable[NO_BITS];
		for (int i=0; i < NO_BITS; i++) {
			variables[i] = new Variable("D"+i, Boolean.class);
			syntaxList.add(new VariableNode(variables[i]));
		}
		
        Node[] syntax = syntaxList.toArray(new Node[syntaxList.size()]);

        template.put(STGPIndividual.SYNTAX, syntax);
        template.put(STGPIndividual.RETURN_TYPE, Boolean.class);
        
        // Generate inputs and expected outputs
        Boolean[][] inputValues = BooleanUtils.generateBoolSequences(NO_BITS);
        Boolean[] expectedOutputs = new Boolean[inputValues.length];
        for (int i=0; i<inputValues.length; i++) {
        	expectedOutputs[i] = BenchmarkSolutions.evenParity(inputValues[i]);
        }
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new HitsCount());
        template.put(HitsCount.INPUT_VARIABLES, variables);
        template.put(HitsCount.INPUT_VALUE_SETS, inputValues);
        template.put(HitsCount.EXPECTED_OUTPUTS, expectedOutputs);
	}
}
