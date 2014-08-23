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
package org.epochx.ge.benchmark;

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
import org.epochx.epox.bool.NandFunction;
import org.epochx.epox.bool.NorFunction;
import org.epochx.epox.bool.OrFunction;
import org.epochx.ge.CodonFactory;
import org.epochx.ge.GEIndividual;
import org.epochx.ge.GESourceGenerator;
import org.epochx.ge.IntegerCodonFactory;
import org.epochx.ge.fitness.GEFitnessFunction;
import org.epochx.ge.fitness.HitsCount;
import org.epochx.ge.init.GrowInitialisation;
import org.epochx.ge.map.DepthFirstMapper;
import org.epochx.ge.map.MappingComponent;
import org.epochx.ge.operator.OnePointCrossover;
import org.epochx.ge.operator.PointMutation;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
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
 * <li>Population.SIZE: 100
 * <li>MaximumGenerations.MAXIMUM_GENERATIONS: 50
 */
public class GEEven3Parity extends GenerationalTemplate {
	
	private static final int NO_BITS = 3;
	
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
        template.put(Population.SIZE, 100);
        List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
        criteria.add(new TerminationFitness(new DoubleFitness.Minimise(0.0)));
        criteria.add(new MaximumGenerations());
        template.put(EvolutionaryStrategy.TERMINATION_CRITERIA, criteria);
        template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);
        template.put(GEIndividual.MAXIMUM_DEPTH, 17);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new OnePointCrossover());
        operators.add(new PointMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(OnePointCrossover.PROBABILITY, 0.0);
        template.put(PointMutation.PROBABILITY, 1.0);
        template.put(Initialiser.METHOD, new GrowInitialisation());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup syntax
		List<Node> syntaxList = new ArrayList<Node>();
		syntaxList.add(new AndFunction());
		syntaxList.add(new OrFunction());
		syntaxList.add(new NandFunction());
		syntaxList.add(new NorFunction());

		Variable[] variables = new Variable[NO_BITS];
		for (int i=0; i < NO_BITS; i++) {
			variables[i] = new Variable("D"+i, Boolean.class);
			syntaxList.add(new VariableNode(variables[i]));
		}
		
        Node[] syntax = syntaxList.toArray(new Node[syntaxList.size()]);

        template.put(STGPIndividual.SYNTAX, syntax);
        template.put(STGPIndividual.RETURN_TYPE, Boolean.class);
        
        // Setup grammar
        String grammarStr = "<prog> ::= <node>\n"
    			+ "<node> ::= <function> | <terminal>\n"
    			+ "<function> ::= NOT( <node> ) "
    			+ "| OR( <node> , <node> ) "
    			+ "| AND( <node> , <node> ) "
    			+ "| XOR( <node> , <node> )\n"
    			+ "<terminal> ::= D0 | D1 | D2\n";
        Grammar grammar = new Grammar(grammarStr);
        template.put(Grammar.GRAMMAR, grammar);
        template.put(CodonFactory.CODON_FACTORY, new IntegerCodonFactory());
        template.put(GEFitnessFunction.INTERPRETER, new EpoxInterpreter<GEIndividual>(new GESourceGenerator()));
        template.put(MappingComponent.MAPPER, new DepthFirstMapper());
        
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
