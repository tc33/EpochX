package org.epochx.gui;

import java.awt.BorderLayout;
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
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.stat.AbstractStat;
import org.epochx.event.stat.GenerationAverageDoubleFitness;
import org.epochx.event.stat.GenerationBestFitness;
import org.epochx.event.stat.GenerationNumber;
import org.epochx.event.stat.GenerationWorstFitness;
import org.epochx.monitor.Monitor;
import org.epochx.monitor.graph.Graph;
import org.epochx.monitor.table.Table;
import org.epochx.monitor.tree.TreeVertex;
import org.epochx.monitor.visualization.AncestryFinder;
import org.epochx.monitor.visualization.InformationPanel;
import org.epochx.monitor.visualization.OperationPanel;
import org.epochx.refactoring.PopulationNeutrality;
import org.epochx.refactoring.Problem;
import org.epochx.refactoring.initialisation.RampedHalfAndHalf;
import org.epochx.refactoring.initialisation.TreeFactory;
import org.epochx.refactoring.operator.Crossover;
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

		config.set(Population.SIZE, 100);
		config.set(Crossover.PROBABILITY, 1.0);
		config.set(Reproduction.PROBABILITY, 0.0);
		config.set(Mutation.PROBABILITY, 0.0);
		config.set(BranchedBreeder.ELITISM, 0);
		config.set(MaximumGenerations.MAXIMUM_GENERATIONS, 100);
		config.set(TreeFactory.MAX_DEPTH, 5);
		config.set(TreeFactory.INITIAL_DEPTH, 3);
		config.set(TournamentSelector.TOURNAMENT_SIZE, 4);
		config.set(NeutralAwareMutation.NEUTRAL_MOVES_ENABLED, true);

		// genetic operators

		config.set(Reproduction.PROBABILITY, 0.10);
		config.set(Mutation.PROBABILITY, 0.90);

		List<Operator> operators = new ArrayList<Operator>();
		operators.add(new Reproduction());
		operators.add(new Mutation());
		operators.add(new Crossover());

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

		Monitor monitor = new Monitor("Frame Graph Test", 1, 2);

		Table table1 = new Table("Fitnesses Table");
		table1.addStat(GenerationNumber.class);
		table1.addStat(GenerationBestFitness.class);
		table1.addStat(GenerationWorstFitness.class);
		table1.addStat(GenerationAverageDoubleFitness.class);
		table1.addListener(EndGeneration.class);

		monitor.add(table1, 1, 1);

		Graph g = new Graph();
		monitor.add(g, 1, 1);
		
		InformationPanel ip = new InformationPanel();
		g.getViewModel().addGraphViewListener(ip);
		g.add(ip, BorderLayout.SOUTH);
		
		TreeVertex tvPane = new TreeVertex();
		g.getViewModel().addGraphViewListener(tvPane);
		monitor.add(tvPane, 1, 2);
		
		OperationPanel opPane = new OperationPanel();
		g.getViewModel().addGraphViewListener(opPane);
		monitor.add(opPane, 1, 2);
		
		AncestryFinder af = new AncestryFinder(g.getViewModel());
		g.getViewModel().addGraphViewListener(af);
		monitor.add(af, 1, 2);
		
		

		//EventManager.getInstance().add(EndRun.class, new GraphModelWriter(g.getModel(), "backup.ser"));
		// we are ready to go!
		long start = System.currentTimeMillis();
		Evolver evolver = new Evolver();
		@SuppressWarnings("unused")
		Population population = evolver.run();
		System.out.println("EVOLVER ENDED : " + (System.currentTimeMillis() - start));
	}

}
