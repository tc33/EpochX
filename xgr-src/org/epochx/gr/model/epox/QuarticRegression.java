package org.epochx.gr.model.epox;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

/**
 * Grammar model that describes a quartic symbolic regression problem to be 
 * solved with the evolution of an Epox program.
 * 
 * <p>
 * The target program is the function: x + x^2 + x^3 + x^4
 * 
 * <p>
 * The grammar used is:
 * 
 * <blockquote><code>
 * &lt;prog&gt; ::= &lt;node&gt;
 * &lt;node&gt; ::= &lt;function&gt; | &lt;terminal&gt;
 * &lt;function&gt; ::= ADD( &lt;node&gt; , &lt;node&gt; )
 *                    | SUB( &lt;node&gt; , &lt;node&gt; )
 *                    | MUL( &lt;node&gt; , &lt;node&gt; )
 *                    | PDIV( &lt;node&gt; , &lt;node&gt; )
 * &lt;terminal&gt; ::= X | 1.0
 * </code></blockquote>
 */
public class QuarticRegression extends GRModel {
	
	/**
	 * The grammar that defines valid solution space.
	 */
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= ADD( <node> , <node> ) " +
					"| SUB( <node> , <node> ) " +
					"| MUL( <node> , <node> ) " +
					"| PDIV( <node> , <node> )\n" +
		"<terminal> ::= X | 1.0\n";
	
	// Epox interpreter for performing evaluation.
	private final Interpreter interpreter;
	
	/**
	 * Constructs an instance of the CubicRegression model.
	 */
	public QuarticRegression() {
		setGrammar(new Grammar(GRAMMAR_STRING));
		interpreter = new EpoxInterpreter();
	}

	/**
	 * Calculates the fitness score for the given program. The fitness of a 
	 * program is calculated as the sum of the difference between the evolved 
	 * program and the actual function. The set of values of x used are between
	 * -1.0 and +1.0 inclusive, at 0.1 intervals.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GRCandidateProgram program = (GRCandidateProgram) p;
		
        // Execute on all possible inputs.
        double fitness = 0;
        
        for (double x = -1; x <= 1; x+=0.1){
        	Double result = null;
			try {
				result = (Double) interpreter.eval(program.getSourceCode(), new String[]{"X"}, new Double[]{x});
			} catch (final MalformedProgramException e) {
				// This should not ever happen unless user changes grammar.
				assert false;
			}
        	
			if (result != null) {
        		fitness += Math.abs(result - function(x));
        	} else {
        		// Assign worst possible fitness and stop evaluating.
        		fitness = Double.POSITIVE_INFINITY;
        		break;
        	}
        }
        
        return fitness;
	}

	/*
	 * The actual function we are trying to evolve.
	 */
    public double function(final double x){
        return x + x*x + x*x*x + x*x*x*x;
    }
}
