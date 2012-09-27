/*
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
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
package org.epochx.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.epochx.BranchedBreeder;
import org.epochx.Config;
import org.epochx.Evolver;
import org.epochx.FitnessEvaluator;
import org.epochx.GenerationalStrategy;
import org.epochx.GenerationalTemplate;
import org.epochx.Initialiser;
import org.epochx.MaximumGenerations;
import org.epochx.Operator;
import org.epochx.Population;
import org.epochx.Reproduction;
import org.epochx.TerminationCriteria;
import org.epochx.TerminationFitness;
import org.epochx.Config.Template;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.Listener;
import org.epochx.event.stat.AbstractStat;
import org.epochx.event.stat.GenerationNumber;
import org.epochx.monitor.Monitor;
import org.epochx.refactoring.PopulationNeutrality;
import org.epochx.refactoring.Problem;
import org.epochx.refactoring.initialisation.RampedHalfAndHalf;
import org.epochx.refactoring.initialisation.TreeFactory;
import org.epochx.refactoring.operator.Mutation;
import org.epochx.refactoring.operator.NeutralAwareMutation;
import org.epochx.refactoring.problem.EvenParity;
import org.epochx.refactoring.representation.CoverageFitness;
import org.epochx.selection.TournamentSelector;

/**
 * A <code>StatText</code>.
 */
public class StatText extends JTextArea implements Listener<Event> {

	private Class<? extends AbstractStat<?>> klass;

	public StatText(Class<? extends AbstractStat<?>> klass) {
			super();
			AbstractStat.register(klass);
			this.klass = klass;
	}

	public void onEvent(Event event) {
		
		setText(AbstractStat.get(klass).toString());

	}
	
	public static void main(String[] args) {
		
		
		Config config = Config.getInstance();
		config.set(Template.KEY, new GenerationalTemplate());

		// the problem instance

		// Problem problem = new Multiplexer(2);
		// SexticPolynomial polynomial = new SexticPolynomial();
		Problem problem = new EvenParity(5);
		config.set(Initialiser.METHOD, new RampedHalfAndHalf(problem.getFunctionSet(), problem.getTerminalSet()));
		// config.set(Initialiser.METHOD, new
		// FullMethod(problem.getFunctionSet(), problem.getTerminalSet()));
		config.set(FitnessEvaluator.FUNCTION, problem);

		// some parameters

		config.set(Population.SIZE, 100);
		// config.set(Crossover.PROBABILITY, 0.9);
		// config.set(Reproduction.PROBABILITY, 0.1);
		config.set(BranchedBreeder.ELITISM, 0);
		config.set(MaximumGenerations.MAXIMUM_GENERATIONS,100);
		config.set(TreeFactory.MAX_DEPTH, 17);
		config.set(TreeFactory.INITIAL_DEPTH, 6);
		config.set(TournamentSelector.TOURNAMENT_SIZE, 4);
		config.set(NeutralAwareMutation.NEUTRAL_MOVES_ENABLED, true);

		// genetic operators

		config.set(Reproduction.PROBABILITY, 0.10);
		config.set(Mutation.PROBABILITY, 0.90);

		List<Operator> operators = new ArrayList<Operator>();
		operators.add(new Reproduction());
		operators.add(new Mutation());

		// operators.add(new StrictInflateMutation());
		// operators.add(new StrictDeflateMutation());
		// operators.add(new PointTerminalMutation());

		config.set(BranchedBreeder.OPERATORS, operators);

		// termination criteria

		List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
		criteria.add(new MaximumGenerations());
		criteria.add(new TerminationFitness(new CoverageFitness(0)));
		config.set(GenerationalStrategy.TERMINATION_CRITERIA, criteria);

		AbstractStat.register(PopulationNeutrality.class);
		
		StatText myText = 
	               new StatText(GenerationNumber.class);
		EventManager.getInstance()
	                .add(EndGeneration.class, myText);
	
		Monitor m = new Monitor();
		m.add(myText);
		
		Evolver evolver = new Evolver();
		@SuppressWarnings("unused")
		Population population = evolver.run();
	}

}