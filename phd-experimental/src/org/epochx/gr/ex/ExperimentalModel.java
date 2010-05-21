package org.epochx.gr.ex;

import java.io.*;

import org.epochx.gr.model.*;
import org.epochx.gr.op.init.*;
import org.epochx.representation.*;
import org.epochx.tools.grammar.*;

public class ExperimentalModel extends GRModel {
	
	public ExperimentalModel() {
		setGrammar(new Grammar(new File("grammars/1c.cg")));
		
		setInitialiser(new ExperimentalInitialiser(this));
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		return 0.0;
	}
}
