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
import org.epochx.DoubleFitness;
import org.epochx.EvolutionaryStrategy;
import org.epochx.FitnessEvaluator;
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
import org.epochx.epox.bool.AndFunction;
import org.epochx.epox.bool.NotFunction;
import org.epochx.epox.bool.OrFunction;
import org.epochx.epox.lang.IfFunction;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.fitness.HitsCount;
import org.epochx.stgp.init.FullInitialisation;
import org.epochx.stgp.operator.SubtreeCrossover;
import org.epochx.stgp.operator.SubtreeMutation;
import org.epochx.tools.BenchmarkSolutions;
import org.epochx.tools.BooleanUtils;

/**
 * This template sets up EpochX to run the 11-bit multiplexer benchmark with the 
 * STGP representation. The 11-bit multiplexer problem involves evolving a program
 * which receives an array of 11 boolean values. The first 3 values are address bits, 
 * which the program should convert into an index for which of the remaining data
 * registers to return. {a0, a1, a2, d0, d1, d2, d3, d4, d5, d6, d7}.
 * 
 * <table>
 * <tr>
 * <td>a0</td><td>a1</td><td>a2</td><td>return value</td>
 * <td>false</td><td>false</td><td>false</td><td>d0</td>
 * <td>true</td><td>false</td><td>false</td><td>d1</td>
 * <td>false</td><td>true</td><td>false</td><td>d2</td>
 * <td>true</td><td>true</td><td>false</td><td>d3</td>
 * <td>false</td><td>false</td><td>true</td><td>d4</td>
 * <td>true</td><td>false</td><td>true</td><td>d5</td>
 * <td>false</td><td>true</td><td>true</td><td>d6</td>
 * <td>true</td><td>true</td><td>true</td><td>d7</td>
 * </tr>
 * </table>
 *  
 * The following configuration is used:
 * 
 * <li>Population.SIZE: 100
 * <li>MaximumGenerations.MAXIMUM_GENERATIONS: 50
 */
public class STGPMultiplexer11Bit extends GenerationalTemplate {
	
	private static final int NO_BITS = 11;
	
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
		int noAddressBits = BenchmarkSolutions.multiplexerAddressBits(NO_BITS);
		int noDataBits = NO_BITS - noAddressBits;
		
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
        template.put(Initialiser.METHOD, new FullInitialisation());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup syntax
		List<Node> syntaxList = new ArrayList<Node>();
		syntaxList.add(new AndFunction());
		syntaxList.add(new OrFunction());
		syntaxList.add(new NotFunction());
		syntaxList.add(new IfFunction());

		Variable[] variables = new Variable[noAddressBits + noDataBits];
		
		for (int i=0; i < noAddressBits; i++) {
			variables[i] = new Variable("A"+i, Boolean.class);
			syntaxList.add(new VariableNode(variables[i]));
		}
		for (int i=noAddressBits; i < (noAddressBits+noDataBits); i++) {
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
        	expectedOutputs[i] = BenchmarkSolutions.multiplexer(inputValues[i], noAddressBits);
        }
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new HitsCount());
        template.put(HitsCount.INPUT_VARIABLES, variables);
        template.put(HitsCount.INPUT_VALUE_SETS, inputValues);
        template.put(HitsCount.EXPECTED_OUTPUTS, expectedOutputs);
	}
}
