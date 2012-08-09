package org.epochx.gui;

import java.util.ArrayList;
import java.util.List;

import org.epochx.BranchedBreeder;
import org.epochx.Config;
import org.epochx.Config.Template;
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
import org.epochx.event.stat.AbstractStat;
import org.epochx.event.stat.GenerationNumber;
import org.epochx.monitor.Monitor;
import org.epochx.monitor.graph.Graph;
import org.epochx.refactoring.PopulationNeutrality;
import org.epochx.refactoring.Problem;
import org.epochx.refactoring.initialisation.RampedHalfAndHalf;
import org.epochx.refactoring.initialisation.TreeFactory;
import org.epochx.refactoring.operator.Mutation;
import org.epochx.refactoring.operator.NeutralAwareMutation;
import org.epochx.refactoring.problem.EvenParity;
import org.epochx.refactoring.representation.CoverageFitness;
import org.epochx.selection.TournamentSelector;


public class MonitorGraphTest {
	
	/**
	 * @param args
	 */
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

		config.set(Population.SIZE, 50);
		// config.set(Crossover.PROBABILITY, 0.9);
		// config.set(Reproduction.PROBABILITY, 0.1);
		config.set(BranchedBreeder.ELITISM, 0);
		config.set(MaximumGenerations.MAXIMUM_GENERATIONS,50);
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
		AbstractStat.register(GenerationNumber.class);

		// our stats monitor

		Monitor monitor = new Monitor("Frame Graph Test");
		
		Graph g = new Graph();
		monitor.add(g);

		// we are ready to go!

		Evolver evolver = new Evolver();
		@SuppressWarnings("unused")
		Population population = evolver.run();
	}

}
