package org.epochx.semantics.gp.example;

import java.util.ArrayList;
import java.util.List;

import org.epochx.epox.DoubleVariable;
import org.epochx.epox.Node;
import org.epochx.epox.dbl.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.Life;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.StatField;
import org.epochx.stats.Stats;

public class TestCubic {
	public static void main(String[] args) {
		
		// syntax
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new AddFunction());
		syntax.add(new SubtractFunction());
		syntax.add(new ProtectedDivisionFunction());
		syntax.add(new MultiplyFunction());
		DoubleVariable x = new DoubleVariable("X");
		syntax.add(x);
		
		// parameters
		GPModel reg = new CubicRegression();
		//reg.setSyntax(syntax);
		reg.setPopulationSize(500);
		reg.setCrossover(new KozaCrossover(reg));
		reg.setCrossoverProbability(0.9);
		reg.setMutationProbability(0);
		reg.setInitialiser(new RampedHalfAndHalfInitialiser(reg));
		reg.setNoGenerations(50);
		reg.setNoRuns(1);
		reg.setPoolSelector(new TournamentSelector(reg, 7));
		reg.setPoolSize(50);
		reg.setReproductionProbability(0.1);
		reg.setMaxDepth(17);
		reg.setMaxInitialDepth(6);
		reg.setTerminationFitness(-1);
		
		// set up stats
		evolver.getLife().addGenerationListener(new GenerationAdapter() {
			public void onGenerationEnd() {
				Stats.get().print(StatField.GEN_NUMBER, StatField.GEN_FITNESS_MAX, StatField.GEN_FITNESS_MIN, StatField.GEN_FITTEST_PROGRAM);
			}
		});
		
		// run
		reg.run();
	}
}
