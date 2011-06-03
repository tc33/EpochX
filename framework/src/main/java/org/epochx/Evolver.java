package org.epochx;

import org.epochx.Config.ConfigKey;

public class Evolver {

	public static final ConfigKey<Initialiser> INITIALISER = new ConfigKey<Initialiser>();

	public static final ConfigKey<FitnessEvaluator> EVALUATOR = new ConfigKey<FitnessEvaluator>();

	public static final ConfigKey<EvolutionaryStrategy> STRATEGY = new ConfigKey<EvolutionaryStrategy>();

	private final Pipeline pipeline;

	public Evolver() {
		pipeline = new Pipeline();
	}

	public Population run() {
		setupPipeline();

		return pipeline.process(new Population());
	}

	protected void setupPipeline() {
		pipeline.add(Config.getInstance().get(INITIALISER));
		pipeline.add(Config.getInstance().get(EVALUATOR));
		pipeline.add(Config.getInstance().get(STRATEGY));
	}
}
