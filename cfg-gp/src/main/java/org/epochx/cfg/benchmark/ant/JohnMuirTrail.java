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
package org.epochx.cfg.benchmark.ant;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.epochx.cfg.CFGIndividual;
import org.epochx.cfg.CFGSourceGenerator;
import org.epochx.cfg.fitness.CFGFitnessFunction;
import org.epochx.cfg.init.Grow;
import org.epochx.cfg.operator.SubtreeCrossover;
import org.epochx.cfg.operator.SubtreeMutation;
import org.epochx.fitness.DoubleFitness;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.ant.AntLandscape;

/**
 * This template sets up EpochX to run the John Muir ant trail benchmark with
 * the GE representation. 
 * 
 * The following configuration is used:
 * 
 * <ul>
 * <li>{@link Population#SIZE}: <code>100</code>
 * <li>{@link GenerationalStrategy#TERMINATION_CRITERIA}: <code>MaximumGenerations</code>, <code>TerminationFitness(0.0)</code>
 * <li>{@link MaximumGenerations#MAXIMUM_GENERATIONS}: <code>50</code>
 * <li>{@link GEIndividual#MAXIMUM_DEPTH}: <code>17</code>
 * <li>{@link BranchedBreeder#SELECTOR}: <code>TournamentSelector</code>
 * <li>{@link TournamentSelector#TOURNAMENT_SIZE}: <code>7</code>
 * <li>{@link Breeder#OPERATORS}: <code>OnePointCrossover</code>, <code>PointMutation</code>
 * <li>{@link OnePointCrossover#PROBABILITY}: <code>0.0</code>
 * <li>{@link PointMutation#PROBABILITY}: <code>1.0</code>
 * <li>{@link Initialiser#METHOD}: <code>GrowInitialiser</code>
 * <li>{@link RandomSequence#RANDOM_SEQUENCE}: <code>MersenneTwisterFast</code>
 * <li>{@link Grammar#GRAMMER}: [Listed below]
 * <li>{@link CodonFactory#CODON_FACTORY}: <code>IntegerCodonFactory</code>
 * <li>{@link CFGFitnessFunction#INTERPRETER}: <code>EpoxInterpreter(GESourceGenerator)</code>
 * <li>{@link MappingComponent#MAPPER}: <code>DepthFirstMapper</code>
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>AntFitnessFunction</code>
 * <li>{@link FoodLocationCount#FOOD_LOCATIONS}: <code>GEJohnMuirTrail.FOOD_LOCATIONS</code>
 * <li>{@link FoodLocationCount#MAXIMUM_TIMESTEPS}: 100
 * 
 * <h3>Grammar</h3>
 * 
 * {@code
 * <function> ::= IF-FOOD-AHEAD( <var> , <function> , <function> ) 
 * 		| SEQ2( <function> , <function> )
 * 		| SEQ3( <function> , <function> , <function> )
 * 		| MOVE( <var> )
 * 		| TURN-LEFT( <var> )
 * 		| TURN-RIGHT( <var> )
 * <var> ::= ANT
 * }
 * 
 * @since 2.0
 */
public class JohnMuirTrail extends GenerationalTemplate {

	/**
	 * The points in the landscape that will be occupied by food
	 */
	public static final List<Point> FOOD_LOCATIONS = Arrays.asList(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0),
			new Point(5, 0), new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(9, 0), new Point(10, 0),
			new Point(10, 1), new Point(10, 2), new Point(10, 3), new Point(10, 4), new Point(10, 5), new Point(10, 6),
			new Point(10, 7), new Point(10, 8), new Point(10, 9), new Point(10, 10), new Point(9, 10),
			new Point(8, 10), new Point(7, 10), new Point(6, 10), new Point(5, 10), new Point(4, 10), new Point(3, 10),
			new Point(3, 9), new Point(3, 8), new Point(3, 7), new Point(3, 6), new Point(3, 5), new Point(2, 5),
			new Point(1, 5), new Point(0, 5), new Point(31, 5), new Point(30, 5), new Point(29, 5), new Point(28, 5),
			new Point(27, 5), new Point(26, 5), new Point(25, 5), new Point(24, 6), new Point(24, 7), new Point(24, 8),
			new Point(24, 9), new Point(24, 10), new Point(23, 11), new Point(22, 11), new Point(21, 11),
			new Point(20, 11), new Point(19, 11), new Point(18, 12), new Point(18, 13), new Point(18, 14),
			new Point(18, 15), new Point(18, 16), new Point(18, 17), new Point(18, 20), new Point(18, 21),
			new Point(18, 22), new Point(18, 23), new Point(18, 24), new Point(18, 25), new Point(17, 27),
			new Point(16, 27), new Point(15, 27), new Point(14, 27), new Point(13, 27), new Point(12, 27),
			new Point(10, 27), new Point(9, 27), new Point(8, 27), new Point(7, 27), new Point(4, 27),
			new Point(4, 26), new Point(4, 25), new Point(4, 24), new Point(5, 22), new Point(7, 21), new Point(8, 18),
			new Point(11, 17), new Point(12, 15), new Point(15, 14), new Point(14, 12), new Point(11, 11),
			new Point(12, 8), new Point(14, 7), new Point(15, 4));
	
	private static final Dimension LANDSCAPE_SIZE = new Dimension(32, 32);
	
	private static final int MAXIMUM_TIMESTEPS = 100;
	
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
        template.put(CFGIndividual.MAXIMUM_DEPTH, 17);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new SubtreeCrossover());
        operators.add(new SubtreeMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(SubtreeCrossover.PROBABILITY, 0.0);
        template.put(SubtreeMutation.PROBABILITY, 1.0);
        template.put(Initialiser.METHOD, new Grow());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup grammar
        String grammarStr = "<function> ::= IF-FOOD-AHEAD( <var> , <function> , <function> ) "
			+ "| SEQ2( <function> , <function> ) "
			+ "| SEQ3( <function> , <function> , <function> ) "
			+ "| MOVE( <var> ) "
			+ "| TURN-LEFT( <var> ) "
			+ "| TURN-RIGHT( <var> )\n"
			+ "<var> ::= ANT";
        
        // Setup syntax
        AntLandscape landscape = new AntLandscape(LANDSCAPE_SIZE, null);
        Ant ant = new Ant(MAXIMUM_TIMESTEPS, landscape);
		
        template.put(Grammar.GRAMMAR, new Grammar(grammarStr));
        template.put(CFGFitnessFunction.INTERPRETER, new EpoxInterpreter<CFGIndividual>(new CFGSourceGenerator()));
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new FoodLocationCount(ant, landscape));
        template.put(FoodLocationCount.FOOD_LOCATIONS, FOOD_LOCATIONS);
        template.put(FoodLocationCount.MAXIMUM_TIMESTEPS, MAXIMUM_TIMESTEPS);
	}
}
