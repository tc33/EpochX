/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.semantics;

import java.util.ArrayList;
import java.util.List;
import com.epochx.core.GPModel;
import com.epochx.core.GPProgramAnalyser;
import com.epochx.core.representation.*;
import com.epochx.func.*;
import com.epochx.func.dbl.*;

/**
 * @author Lawrence Beadle & Tom Castle
 *
 */
public class RegressionSemanticModule implements SemanticModule {
	
	private List<TerminalNode<?>> terminals;
	private GPModel model;
	
	/**
	 * Constructor for Regression Semantic Module
	 * @param list List of terminal nodes
	 * @param model The GPModel object
	 */
	public RegressionSemanticModule(List<TerminalNode<?>> list, GPModel model) {
		this.terminals = list;
		this.model = model;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#start()
	 */
	@Override
	public void start() {
		// Not required as not accessing external software for this model
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#stop()
	 */
	@Override
	public void stop() {
		// Not required as not accessing external software for this model
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#behaviourToCode(com.epochx.semantics.Representation)
	 */
	@Override
	public CandidateProgram behaviourToCode(Representation representation) {
		// TODO Auto-generated method stub
		
		// do reverse translation
		// 1 - get length of array and create right number of polynomials
		// 2 - assign correct coefficients to correct variable and build node tree
		// 3 - assemble candidate program and return
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public Representation codeToBehaviour(CandidateProgram program) {
		
		// clone the program to prevent back modfication
		CandidateProgram program1 = (CandidateProgram) program.clone();
		// extract and simplify program
		Node<Double> rootNode = program1.getRootNode();
		
		// resolve any multiply by zeros
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeMultiplyByZeros(rootNode);
		}
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeAllPDivsWithSameSubtrees(rootNode);
		}
		
		// resolve constant calculations
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.resolveConstantCalculations(rootNode);
		}
		
		// resolve any multiply by zeros
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeMultiplyByZeros(rootNode);
		}
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeAllPDivsWithSameSubtrees(rootNode);
		}
		
		// collect up multiply and PDiv into powers
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.collectUpPowers(rootNode);
		}
		
		// collect up coefficient functions
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.reduceToCVPFormat(rootNode);
		}
		
		System.out.println(rootNode);
		System.out.println("---------------");
		
		// put all coefficient functions into linear process and simplify
		// TODO
		
		// construct new RegressionBehaviour
		// TODO
		
		return null;
	}

	private Node<Double> removeMultiplyByZeros(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// recurse on other functions
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.removeMultiplyByZeros(children[i]), i);
			}
			// check if multiply function
			if(rootNode instanceof MultiplyFunction) {
				// check for zeros
				boolean zeroPresent = false;
				TerminalNode<Double> zeroNode = new TerminalNode<Double>(0d);
				for(int i = 0; i<arity; i++) {
					if(children[i].equals(zeroNode)) {
						zeroPresent = true;
					}
				}
				if(zeroPresent) {
					rootNode = zeroNode;
				}
			}
		}
		return rootNode;
	}	
	
	private Node<Double> removeAllPDivsWithSameSubtrees(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// recurse on children 1st
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.removeAllPDivsWithSameSubtrees(children[i]), i);
			}
			// decide what to do - reduce or recurse
			if(rootNode instanceof ProtectedDivisionFunction) {
				// compare children and resolve root node to 1 if they are equal
				if(children[0].equals(children[1])) {
					TerminalNode<Double> oneNode = new TerminalNode<Double>(1d);
					rootNode = oneNode;
				}
				// check for PDIV by 0
				TerminalNode<Double> zeroNode = new TerminalNode<Double>(0d);
				if(children[1].equals(zeroNode)) {					
					rootNode = zeroNode;
				}
				// check for 0 PDIV by anything = 0
				if(children[0].equals(zeroNode)) {					
					rootNode = zeroNode;
				}
			}
		}
		return rootNode;
	}
	
	private Node<Double> resolveConstantCalculations(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.resolveConstantCalculations(children[i]), i);
			}
			// check if all child nodes are numbers
			boolean allChildrenAreTerminalNodes = true;
			for(int i = 0; i<arity; i++) {
				if((children[i] instanceof Variable) || ((children[i] instanceof FunctionNode))) {
					allChildrenAreTerminalNodes = false;
				}
			}
			// decide what to do - reduce or recurse
			if(allChildrenAreTerminalNodes) {
				// resolve root node to constant
				Double result = (Double) rootNode.evaluate();
				rootNode = new TerminalNode<Double>(result);
			}
		}
		return rootNode;
	}
	
	private Node<Double> collectUpPowers(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.collectUpPowers(children[i]), i);
			}
			// decide what to do - reduce or recurse
			if(rootNode instanceof MultiplyFunction) {
				Variable<Double> x = new Variable<Double>("X");
				if((children[0] instanceof Variable) && (children[1] instanceof Variable)) {
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(2d));
					rootNode = powerNode;
				} else if((children[0] instanceof ExponentFunction) && (children[1] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() + 1;
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[1] instanceof ExponentFunction) && (children[0] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[1].getChild(1).evaluate() + 1;
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[0] instanceof ExponentFunction) && (children[1] instanceof ExponentFunction)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() + (Double) children[1].getChild(1).evaluate();
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				}
			} else if(rootNode instanceof ProtectedDivisionFunction) {
				Variable<Double> x = new Variable<Double>("X");
				if((children[0] instanceof ExponentFunction) && (children[1] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() - 1;
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[1] instanceof ExponentFunction) && (children[0] instanceof Variable)) {
					// work out new power
					// need to negate this to make it accurate
					Double power = ((Double) children[1].getChild(1).evaluate() - 1) * -1;
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[0] instanceof ExponentFunction) && (children[1] instanceof ExponentFunction)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() - (Double) children[1].getChild(1).evaluate();
					Node<Double> powerNode = new ExponentFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				}
			}
		}
		return rootNode;
	}
	
	private Node<Double> reduceToCVPFormat(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.reduceToCVPFormat(children[i]), i);
			}
			// scan for CVPs to build up
			if(rootNode instanceof MultiplyFunction) {
				if((children[0] instanceof Variable) && (children[1] instanceof TerminalNode)) {
					rootNode = new CoefficientVariablePowerFunction(children[1], children[0], new TerminalNode<Double>(1d));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof Variable)) {
					rootNode = new CoefficientVariablePowerFunction(children[0], children[1], new TerminalNode<Double>(1d));
				} else if((children[0] instanceof PowerFunction) && (children[1] instanceof TerminalNode)) {
					rootNode = new CoefficientVariablePowerFunction(children[1], children[0].getChild(0), children[0].getChild(1));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof PowerFunction)) {
					rootNode = new CoefficientVariablePowerFunction(children[0], children[1].getChild(0), children[1].getChild(1));
				} else if((children[0] instanceof CoefficientVariablePowerFunction) && (children[1] instanceof TerminalNode)) {
					double coefficient = (Double) children[0].getChild(0).evaluate();
					double terminal = (Double) children[1].evaluate();
					double newCoefficient = coefficient * terminal;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[0].getChild(1), children[0].getChild(2));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof CoefficientVariablePowerFunction)) {
					double coefficient = (Double) children[1].getChild(0).evaluate();
					double terminal = (Double) children[0].evaluate();
					double newCoefficient = coefficient * terminal;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[1].getChild(1), children[1].getChild(2));
				}
			} else if(rootNode instanceof ProtectedDivisionFunction) {
				if((children[0] instanceof Variable) && (children[1] instanceof TerminalNode)) {
					double terminal = (Double) children[1].evaluate();
					double newCoefficient = 1 / terminal;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[0], new TerminalNode<Double>(1d));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof Variable)) {
					rootNode = new CoefficientVariablePowerFunction(children[0], children[1], new TerminalNode<Double>(-1d));
				} else if((children[0] instanceof PowerFunction) && (children[1] instanceof TerminalNode)) {
					double terminal = (Double) children[1].evaluate();
					double newCoefficient = 1 / terminal;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[0].getChild(0), children[0].getChild(1));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof PowerFunction)) {
					double power = (Double) children[1].getChild(1).evaluate();
					double newPower = power * -1;
					rootNode = new CoefficientVariablePowerFunction(children[0], children[1].getChild(0), new TerminalNode<Double>(newPower));
				} else if((children[0] instanceof CoefficientVariablePowerFunction) && (children[1] instanceof TerminalNode)) {
					double coefficient = (Double) children[0].getChild(0).evaluate();
					double terminal = (Double) children[1].evaluate();
					double newCoefficient = coefficient / terminal;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[0].getChild(1), children[0].getChild(2));
				} else if((children[0] instanceof TerminalNode) && (children[1] instanceof CoefficientVariablePowerFunction)) {
					double coefficient = (Double) children[1].getChild(0).evaluate();
					double terminal = (Double) children[0].evaluate();
					double newCoefficient = terminal / coefficient;
					double power = (Double) children[1].getChild(2).evaluate();
					double newPower = power * -1;
					rootNode = new CoefficientVariablePowerFunction(new TerminalNode<Double>(newCoefficient), children[1].getChild(1), new TerminalNode<Double>(newPower));
				}
			}
		}
		return rootNode;
	}

}








