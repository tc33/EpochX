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
		// check representation is right type
		RegressionRepresentation regRep;
		if(representation instanceof RegressionRepresentation) {
			regRep = (RegressionRepresentation) representation;
		} else {
			throw new IllegalArgumentException("WRONG INPUT IN BEHAVIOUR TO CODE - REGRESSION SEMANTIC MODULE");
		}
		
		// build CVP tree
		Node<Double> rootNode = this.buildCVPTree(regRep);
		
		// expand the CVPS to normal functions
		rootNode = this.expandCVPTree(rootNode);
		
		return new CandidateProgram(rootNode, model);
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public Representation codeToBehaviour(CandidateProgram program) {
		
		// clone the program to prevent back modification
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
		
		//System.out.println("1 - " + rootNode);
		
		// resolve constant calculations
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.resolveConstantCalculations(rootNode);
		}
		
		//System.out.println("2 - " + rootNode);
		
		// resolve any multiply by zeros
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeMultiplyByZeros(rootNode);
		}
		
		//System.out.println("3 - " + rootNode);
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		if(GPProgramAnalyser.getProgramLength(rootNode)>1) {
			rootNode = this.removeAllPDivsWithSameSubtrees(rootNode);
		}
		
		//System.out.println("4 - " + rootNode);
		
		// collect up coefficient functions
		rootNode = this.reduceToCVPFormat(rootNode);
		
		//System.out.println("5 - " + rootNode);
		
		RegressionRepresentation regRep = new RegressionRepresentation(this.isolateCVPs(rootNode));
		
		//System.out.println("R1 - " + regRep.toString());
		
		regRep.simplify();
		regRep.order();
		
		//System.out.println("R2 - " + regRep.toString());
		//System.out.println("---------------");
		
		return regRep;
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
				TerminalNode<Double> minusZeroNode = new TerminalNode<Double>(-0d);
				for(int i = 0; i<arity; i++) {
					if(children[i].equals(zeroNode) || children[i].equals(minusZeroNode)) {
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
				TerminalNode<Double> minusZeroNode = new TerminalNode<Double>(-0d);
				if(children[1].equals(zeroNode) || children[1].equals(minusZeroNode)) {					
					rootNode = zeroNode;
				}
				// check for 0 PDIV by anything = 0
				if(children[0].equals(zeroNode) || children[0].equals(minusZeroNode)) {					
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
	
	/**
	 * Reduces the node tree to CVP format
	 * @param rootNode The node to be reduced
	 * @return The reduced form of the nodes
	 */
	private Node<Double> reduceToCVPFormat(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity==0) {
			if(rootNode instanceof Variable) {
				rootNode = new CoefficientExponentFunction(new TerminalNode<Double>(1d), new Variable<Double>("X"), new TerminalNode<Double>(1d));
			} else {
				double newCoefficient = (Double) rootNode.evaluate();
				rootNode = new CoefficientExponentFunction(new TerminalNode<Double>(newCoefficient), new Variable<Double>("X"), new TerminalNode<Double>(0d));
			}
		} else if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(this.reduceToCVPFormat(children[i]), i);
			}
			// scan for CVPs to build up
			if(rootNode instanceof MultiplyFunction) {
				// Get CVP list from each side
				ArrayList<CoefficientExponentFunction> cVPLeft = this.isolateCVPs((Node<Double>) rootNode.getChild(0));
				ArrayList<CoefficientExponentFunction> cVPRight = this.isolateCVPs((Node<Double>) rootNode.getChild(1));
				ArrayList<CoefficientExponentFunction> cVPTotal = new ArrayList<CoefficientExponentFunction>();
				int cPVLeftSize = cVPLeft.size();
				int cPVRightSize = cVPRight.size();
				for(int i = 0; i<cPVLeftSize; i++) {
					for(int j = 0; j<cPVRightSize; j++) {
						double coefficient1 = (Double) cVPLeft.get(i).getChild(0).evaluate();
						double coefficient2 = (Double) cVPRight.get(j).getChild(0).evaluate();
						double newCoefficient = coefficient1 * coefficient2;					
						double power1 = (Double) cVPLeft.get(i).getChild(2).evaluate();
						double power2 = (Double) cVPRight.get(j).getChild(2).evaluate();
						double newPower = power1 + power2;
						CoefficientExponentFunction c = new CoefficientExponentFunction(new TerminalNode<Double>(newCoefficient), new Variable<Double>("X"), new TerminalNode<Double>(newPower));
						cVPTotal.add(c);						
					}
				}
				RegressionRepresentation regRep = new RegressionRepresentation(cVPTotal);
				//regRep.simplify();
				rootNode = this.buildCVPTree(regRep);
			} else if(rootNode instanceof ProtectedDivisionFunction) {
				// Get CVP list from each side
				ArrayList<CoefficientExponentFunction> cVPLeft = this.isolateCVPs((Node<Double>) rootNode.getChild(0));
				ArrayList<CoefficientExponentFunction> cVPRight = this.isolateCVPs((Node<Double>) rootNode.getChild(1));
				ArrayList<CoefficientExponentFunction> cVPTotal = new ArrayList<CoefficientExponentFunction>();
				int cPVLeftSize = cVPLeft.size();
				int cPVRightSize = cVPRight.size();
				for(int i = 0; i<cPVLeftSize; i++) {
					for(int j = 0; j<cPVRightSize; j++) {
						double coefficient1 = (Double) cVPLeft.get(i).getChild(0).evaluate();
						double coefficient2 = (Double) cVPRight.get(j).getChild(0).evaluate();
						double newCoefficient = 0;
						if(coefficient2!=0) { 
							newCoefficient = coefficient1 / coefficient2;
						}					
						double power1 = (Double) cVPLeft.get(i).getChild(2).evaluate();
						double power2 = (Double) cVPRight.get(j).getChild(2).evaluate();
						double newPower = power1 - power2;
						CoefficientExponentFunction c = new CoefficientExponentFunction(new TerminalNode<Double>(newCoefficient), new Variable<Double>("X"), new TerminalNode<Double>(newPower));
						cVPTotal.add(c);						
					}
				}
				RegressionRepresentation regRep = new RegressionRepresentation(cVPTotal);
				//regRep.simplify();
				rootNode = this.buildCVPTree(regRep);
			}
		}
		return rootNode;
	}
	
	public ArrayList<CoefficientExponentFunction> isolateCVPs(Node<Double> rootNode) {
		ArrayList<CoefficientExponentFunction> cVPList = new ArrayList<CoefficientExponentFunction>();		
		// check if terminal
		if(rootNode instanceof CoefficientExponentFunction) {
			cVPList.add((CoefficientExponentFunction) rootNode);		
		} else if(rootNode instanceof AddFunction) {
			ArrayList<CoefficientExponentFunction> cVPs = this.isolateCVPs((Node<Double>) rootNode.getChild(0));
			// add the retrieved CVP nodes
			for(CoefficientExponentFunction c: cVPs) {
				cVPList.add(c);
			}
			cVPs = this.isolateCVPs((Node<Double>) rootNode.getChild(1));
			// add the retrieved CVP nodes
			for(CoefficientExponentFunction c: cVPs) {
				cVPList.add(c);
			}
		} else if(rootNode instanceof SubtractFunction) {
			ArrayList<CoefficientExponentFunction> cVPs = this.isolateCVPs((Node<Double>) rootNode.getChild(0));
			// add the retrieved CVP nodes
			for(CoefficientExponentFunction c: cVPs) {
				cVPList.add(c);
			}
			cVPs = this.isolateCVPs((Node<Double>) rootNode.getChild(1));
			// add the retrieved CVP nodes AFTER * the coefficients by -1
			for(CoefficientExponentFunction c: cVPs) {
				// * coefficients by -1 before adding them
				double coefficient = (Double) c.getChild(0).evaluate();
				double newCoefficient = coefficient * -1;
				c.setChild(new TerminalNode<Double>(newCoefficient), 0);
				cVPList.add(c);
			}
		}		
		return cVPList;
	}
	
	private Node<Double> buildCVPTree(RegressionRepresentation rep) {
		ArrayList<CoefficientExponentFunction> regRep = rep.getRegressionRepresentation();
		int regRepSize = regRep.size();
		Node<Double> rootNode = null;
		if(regRepSize>1) {
			rootNode = regRep.get(0);
			for(int i = 1; i<regRepSize; i++) {
				// check if second coefficient <0
				if(((Double) regRep.get(i).getChild(0).evaluate()) < 0) {
					// modify sign on second CVP node
					double coefficient = (Double) regRep.get(i).getChild(0).evaluate();
					double newCoefficient = Math.abs(coefficient);
					regRep.get(i).setChild(new TerminalNode<Double>(newCoefficient), 0);
					// if it is generate subtract function
					rootNode = new SubtractFunction(rootNode, regRep.get(i));
				} else {
					// else generate add function
					rootNode = new AddFunction(rootNode, regRep.get(i));
				}
			}
		} else if(regRepSize==1){
			rootNode = regRep.get(0);
		} else {
			rootNode = new CoefficientExponentFunction(new TerminalNode<Double>(0d), new Variable<Double>("X"), new TerminalNode<Double>(0d));
		}
		return rootNode;
	}
	
	private Node<Double> expandCVPTree(Node<Double> rootNode) {
		// expand if it is CVP
		if(rootNode instanceof CoefficientExponentFunction) {
			double coefficient = (Double) rootNode.getChild(0).evaluate();
			double power = (Double) rootNode.getChild(2).evaluate();
			if(coefficient==0 && power==0) {
				rootNode = new TerminalNode<Double>(0d);
			} else if(power==0) {
				rootNode = new TerminalNode<Double>(coefficient);
			} else if(coefficient==1 && power==1) {
				rootNode = new Variable<Double>("X");
			} else if(coefficient==1 && power ==-1) {
				rootNode = new ProtectedDivisionFunction(new TerminalNode<Double>(coefficient), new Variable<Double>("X"));
			} else if(power <-1) {
				rootNode = new ProtectedDivisionFunction(new TerminalNode<Double>(coefficient), new CoefficientExponentFunction(new TerminalNode<Double>(1d), new Variable<Double>("X"), new TerminalNode<Double>((power*-1))));
			} else if(power>1) {
				rootNode = new MultiplyFunction(new Variable<Double>("X"), new CoefficientExponentFunction(new TerminalNode<Double>(coefficient), new Variable<Double>("X"), new TerminalNode<Double>((power-1))));
			} else if(coefficient>1 && power==1) {
				rootNode = new MultiplyFunction(new TerminalNode<Double>(coefficient), new Variable<Double>("X"));
			}
		}
		// expand children
		int arity = rootNode.getArity();
		Node<Double>[] children = (Node<Double>[]) rootNode.getChildren();
		if(arity>0) {
			for(int i = 0; i < arity; i++) {
				children[i] = this.expandCVPTree(children[i]);
			}
		}
		return rootNode;
	}

}








