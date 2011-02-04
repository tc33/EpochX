package org.epochx.gx.study;

import java.io.*;

import org.epochx.gp.representation.*;
import org.epochx.gx.model.*;
import org.epochx.gx.node.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.op.mutation.*;
import org.epochx.gx.stats.*;
import org.epochx.life.*;
import org.epochx.stats.*;

public class OverSelectionProportionTest {
    public static void main(String[] args) {
    	final double crossoverProbability = 0.0;//Double.valueOf(args[0]);
    	final double mutationProbability = 1.0 - crossoverProbability;
    	
    	final String outputPath = (args.length > 1) ? args[1] : "results/";
    	
		final GXModel model = new OverSelectionProportion();
		model.setNoRuns(100);
		model.setNoGenerations(1000);
		model.setPopulationSize(1000);
		model.setInitialiser(new ImperativeInitialiser(model));
		model.setMutation(new ImperativeMutation(model, 0.25, 0.25, 0.5, 0.2));
		model.setCrossover(null);
		model.setNoElites(1);
		model.setCrossoverProbability(crossoverProbability);
		model.setMutationProbability(mutationProbability);
		model.setTerminationFitness(0.0);
		model.setMaxNoStatements(6);
		model.setMinNoStatements(3);
		model.setMaxLoopDepth(1);
		model.setMaxExpressionDepth(4);
		
		Life.get().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Stats.get().print(StatField.GEN_NUMBER, 
						   StatField.GEN_FITNESS_MIN, 
						   StatField.GEN_FITNESS_AVE,
						   GXStatField.GEN_NO_STATEMENTS_MIN,
						   GXStatField.GEN_NO_STATEMENTS_MAX,
						   GXStatField.GEN_NO_STATEMENTS_AVE);
				
				GPCandidateProgram p = (GPCandidateProgram) Stats.get().getStat(StatField.GEN_FITTEST_PROGRAM);
				ASTNode root = (ASTNode) p.getRootNode();
				System.out.println(root.toJava());
			}
		});
		
		try {
			final FileOutputStream fileout = new FileOutputStream(new File(outputPath+"/varswap-x"+crossoverProbability+".txt"));
			
			Life.get().addRunListener(new RunAdapter() {
				@Override
				public void onRunEnd() {
					Stats.get().printToStream(fileout, StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		model.run();
	}
}
