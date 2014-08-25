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
package org.epochx.ge.benchmark.ant;

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
import org.epochx.fitness.DoubleFitness;
import org.epochx.ge.CodonFactory;
import org.epochx.ge.GEIndividual;
import org.epochx.ge.GESourceGenerator;
import org.epochx.ge.IntegerCodonFactory;
import org.epochx.ge.fitness.GEFitnessFunction;
import org.epochx.ge.init.Grow;
import org.epochx.ge.map.DepthFirstMapper;
import org.epochx.ge.map.MappingComponent;
import org.epochx.ge.operator.OnePointCrossover;
import org.epochx.ge.operator.PointMutation;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.ant.AntLandscape;

/**
 * This template sets up EpochX to run the Los Altos Hills ant trail benchmark with
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
 * <li>{@link GEFitnessFunction#INTERPRETER}: <code>EpoxInterpreter(GESourceGenerator)</code>
 * <li>{@link MappingComponent#MAPPER}: <code>DepthFirstMapper</code>
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>AntFitnessFunction</code>
 * <li>{@link FoodLocationCount#FOOD_LOCATIONS}: <code>GELosAltosHillsTrail.FOOD_LOCATIONS</code>
 * <li>{@link FoodLocationCount#MAXIMUM_TIMESTEPS}: 3000
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
public class GELosAltosHillsTrail extends GenerationalTemplate {

	/**
	 * The points in the landscape that will be occupied by food.
	 */
	public static final List<Point> FOOD_LOCATIONS = Arrays.asList(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(3, 1),
		new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(4, 5), new Point(5, 5),
		new Point(6, 5), new Point(8, 5), new Point(9, 5), new Point(10, 5), new Point(11, 5), new Point(12, 5),
		new Point(12, 6), new Point(12, 7), new Point(12, 8), new Point(12, 9), new Point(12, 11),
		new Point(12, 12), new Point(12, 13), new Point(12, 14), new Point(12, 17), new Point(12, 18),
		new Point(12, 19), new Point(12, 20), new Point(12, 21), new Point(12, 22), new Point(12, 23),
		new Point(11, 24), new Point(10, 24), new Point(9, 24), new Point(8, 24), new Point(7, 24),
		new Point(4, 24), new Point(3, 24), new Point(1, 25), new Point(1, 26), new Point(1, 27), new Point(1, 28),
		new Point(1, 29), new Point(1, 30), new Point(1, 31), new Point(2, 33), new Point(3, 33), new Point(4, 33),
		new Point(5, 33), new Point(7, 32), new Point(7, 31), new Point(8, 30), new Point(9, 30),
		new Point(10, 30), new Point(11, 30), new Point(12, 30), new Point(17, 30), new Point(18, 30),
		new Point(20, 29), new Point(20, 28), new Point(20, 27), new Point(20, 26), new Point(20, 25),
		new Point(20, 24), new Point(20, 21), new Point(20, 20), new Point(20, 19), new Point(20, 18),
		new Point(21, 15), new Point(24, 14), new Point(24, 13), new Point(24, 10), new Point(24, 9),
		new Point(24, 8), new Point(24, 7), new Point(25, 5), new Point(26, 5), new Point(27, 5), new Point(28, 5),
		new Point(29, 5), new Point(30, 5), new Point(31, 5), new Point(32, 5), new Point(33, 5), new Point(34, 5),
		new Point(35, 5), new Point(36, 5), new Point(38, 4), new Point(38, 3), new Point(39, 2), new Point(40, 2),
		new Point(41, 2), new Point(43, 3), new Point(43, 4), new Point(43, 6), new Point(43, 9),
		new Point(43, 12), new Point(42, 14), new Point(41, 14), new Point(40, 14), new Point(37, 15),
		new Point(38, 18), new Point(41, 19), new Point(40, 22), new Point(37, 23), new Point(37, 24),
		new Point(37, 25), new Point(37, 26), new Point(37, 27), new Point(37, 28), new Point(37, 29),
		new Point(37, 30), new Point(37, 31), new Point(37, 32), new Point(37, 33), new Point(37, 34),
		new Point(35, 34), new Point(35, 35), new Point(35, 36), new Point(35, 37), new Point(35, 38),
		new Point(35, 39), new Point(35, 40), new Point(35, 41), new Point(35, 42), new Point(34, 42),
		new Point(33, 42), new Point(32, 42), new Point(31, 42), new Point(30, 42), new Point(30, 44),
		new Point(30, 45), new Point(30, 46), new Point(30, 47), new Point(30, 48), new Point(30, 49),
		new Point(28, 50), new Point(28, 51), new Point(28, 52), new Point(28, 53), new Point(28, 54),
		new Point(28, 55), new Point(28, 56), new Point(27, 56), new Point(26, 56), new Point(25, 56),
		new Point(24, 56), new Point(23, 56), new Point(22, 58), new Point(22, 59), new Point(22, 60),
		new Point(22, 61), new Point(22, 62), new Point(22, 63), new Point(22, 64), new Point(22, 65),
		new Point(22, 66));
	
	private static final Dimension LANDSCAPE_SIZE = new Dimension(100, 100);
	
	private static final int MAXIMUM_TIMESTEPS = 3000;
	
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
        template.put(GEIndividual.MAXIMUM_DEPTH, 17);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new OnePointCrossover());
        operators.add(new PointMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(OnePointCrossover.PROBABILITY, 0.0);
        template.put(PointMutation.PROBABILITY, 1.0);
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
        template.put(CodonFactory.CODON_FACTORY, new IntegerCodonFactory());
        template.put(GEFitnessFunction.INTERPRETER, new EpoxInterpreter<GEIndividual>(new GESourceGenerator()));
        template.put(MappingComponent.MAPPER, new DepthFirstMapper());
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new FoodLocationCount(ant, landscape));
        template.put(FoodLocationCount.FOOD_LOCATIONS, FOOD_LOCATIONS);
        template.put(FoodLocationCount.MAXIMUM_TIMESTEPS, MAXIMUM_TIMESTEPS);
	}
}
