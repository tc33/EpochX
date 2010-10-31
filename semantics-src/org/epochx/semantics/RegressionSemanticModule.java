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
package org.epochx.semantics;

import java.util.*;

import org.epochx.epox.*;
import org.epochx.epox.dbl.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;

/**
 * The regression semantic module controls all aspects of the modelling of
 * the behaviour of symbolic regression problems
 */
public class RegressionSemanticModule implements SemanticModule {
	
	private List<DoubleVariable> terminals;
	private GPModel model;
	private DoubleVariable var;
	
	/**
	 * Constructor for Regression Semantic Module
	 * @param list List of terminal nodes
	 * @param model The GPModel object
	 */
	public RegressionSemanticModule(List<DoubleVariable> list, GPModel model) {
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
	public GPCandidateProgram behaviourToCode(Representation representation) {
		// check representation is right type
		RegressionRepresentation regRep;
		if(representation instanceof RegressionRepresentation) {
			regRep = (RegressionRepresentation) representation;
		} else {
			throw new IllegalArgumentException("WRONG INPUT IN BEHAVIOUR TO CODE - REGRESSION SEMANTIC MODULE");
		}
		
		// capture variable
		for(int i = 0; i<terminals.size(); i++) {
			if(terminals.get(i) instanceof DoubleVariable) {
				var = (DoubleVariable) terminals.get(i);
			}
		}
		
		// build CVP tree
		DoubleNode rootNode = this.buildCVPTree(regRep);
		
		// expand the CVPS to normal functions
		rootNode = this.expandCVPTree(rootNode);
		
		return new GPCandidateProgram(rootNode, model);
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public Representation codeToBehaviour(GPCandidateProgram program) {
		
		// clone the program to prevent back modification
		GPCandidateProgram program1 = (GPCandidateProgram) program.clone();
		// extract and simplify program
		DoubleNode rootNode = (DoubleNode) program1.getRootNode();

		// resolve any multiply by zeros
		if(rootNode.getLength()>1) {
			rootNode = this.removeMultiplyByZeros(rootNode);
		}
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		if(rootNode.getLength()>1) {
			rootNode = this.removeAllPDivsWithSameSubtrees(rootNode);
		}
		
		// resolve constant calculations
		if(rootNode.getLength()>1) {
			rootNode = this.resolveConstantCalculations(rootNode);
		}
		
		// resolve any multiply by zeros
		if(rootNode.getLength()>1) {
			rootNode = this.removeMultiplyByZeros(rootNode);
		}
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		if(rootNode.getLength()>1) {
			rootNode = this.removeAllPDivsWithSameSubtrees(rootNode);
		}
		
		// collect up coefficient functions
		rootNode = this.reduceToCVPFormat(rootNode);
		
		RegressionRepresentation regRep = new RegressionRepresentation(this.isolateCVPs(rootNode));
		
		regRep.simplify();
		regRep.order();
		
		return regRep;
	}

	private DoubleNode removeMultiplyByZeros(DoubleNode rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			DoubleNode[] children = (DoubleNode[]) rootNode.getChildren();
			// recurse on other functions
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(i, this.removeMultiplyByZeros(children[i]));
			}
			// check if multiply function
			if(rootNode instanceof MultiplyFunction) {
				// check for zeros
				boolean zeroPresent = false;
				DoubleNode zeroNode = new DoubleLiteral(0d);
				DoubleNode minusZeroNode = new DoubleLiteral(-0d);
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
	
	private DoubleNode removeAllPDivsWithSameSubtrees(DoubleNode rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			DoubleNode[] children = (DoubleNode[]) rootNode.getChildren();
			// recurse on children 1st
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(i, this.removeAllPDivsWithSameSubtrees(children[i]));
			}
			// decide what to do - reduce or recurse
			if(rootNode instanceof ProtectedDivisionFunction) {
				// compare children and resolve root node to 1 if they are equal
				if(children[0].equals(children[1])) {
					DoubleNode oneNode = new DoubleLiteral(1d);
					rootNode = oneNode;
				}
				// check for PDIV by 0
				DoubleNode zeroNode = new DoubleLiteral(0d);
				DoubleNode minusZeroNode = new DoubleLiteral(-0d);
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
	
	private DoubleNode resolveConstantCalculations(DoubleNode rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity>0) {
			// get children
			DoubleNode[] children = (DoubleNode[]) rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(i, this.resolveConstantCalculations(children[i]));
			}
			// check if all child nodes are numbers
			boolean allChildrenAreTerminalNodes = true;
			for(int i = 0; i<arity; i++) {
				if((children[i] instanceof DoubleVariable) || ((children[i] instanceof DoubleNode))) {
					allChildrenAreTerminalNodes = false;
				}
			}
			// decide what to do - reduce or recurse
			if(allChildrenAreTerminalNodes) {
				// resolve root node to constant
				Double result = (Double) rootNode.evaluate();
				rootNode = new DoubleLiteral(result);
			}
		}
		return rootNode;
	}
	
	/**
	 * Reduces the node tree to CVP format
	 * @param rootNode The node to be reduced
	 * @return The reduced form of the nodes
	 */
	private DoubleNode reduceToCVPFormat(DoubleNode rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		// check if terminal
		if(arity==0) {
			if(rootNode instanceof DoubleVariable) {
				rootNode = new CoefficientPowerFunction(new DoubleLiteral(1d), new DoubleVariable("X"), new DoubleLiteral(1d));
			} else {
				double newCoefficient = (Double) rootNode.evaluate();
				rootNode = new CoefficientPowerFunction(new DoubleLiteral(newCoefficient), new DoubleVariable("X"), new DoubleLiteral(0d));
			}
		} else if(arity>0) {
			// get children
			DoubleNode[] children = (DoubleNode[]) rootNode.getChildren();
			// reduce all children 1st - bottom up process
			for(int i = 0; i<arity; i++) {
				rootNode.setChild(i, this.reduceToCVPFormat(children[i]));
			}
			// scan for CVPs to build up
			if(rootNode instanceof MultiplyFunction) {
				// Get CVP list from each side
				ArrayList<CoefficientPowerFunction> cVPLeft = this.isolateCVPs((DoubleNode) rootNode.getChild(0));
				ArrayList<CoefficientPowerFunction> cVPRight = this.isolateCVPs((DoubleNode) rootNode.getChild(1));
				ArrayList<CoefficientPowerFunction> cVPTotal = new ArrayList<CoefficientPowerFunction>();
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
						CoefficientPowerFunction c = new CoefficientPowerFunction(new DoubleLiteral(newCoefficient), new DoubleVariable("X"), new DoubleLiteral(newPower));
						cVPTotal.add(c);						
					}
				}
				RegressionRepresentation regRep = new RegressionRepresentation(cVPTotal);
				regRep.simplify();
				rootNode = this.buildCVPTree(regRep);
			} else if(rootNode instanceof ProtectedDivisionFunction) {
				// Get CVP list from each side
				ArrayList<CoefficientPowerFunction> cVPLeft = this.isolateCVPs((DoubleNode) rootNode.getChild(0));
				ArrayList<CoefficientPowerFunction> cVPRight = this.isolateCVPs((DoubleNode) rootNode.getChild(1));
				ArrayList<CoefficientPowerFunction> cVPTotal = new ArrayList<CoefficientPowerFunction>();
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
						CoefficientPowerFunction c = new CoefficientPowerFunction(new DoubleLiteral(newCoefficient), new DoubleVariable("X"), new DoubleLiteral(newPower));
						cVPTotal.add(c);						
					}
				}
				RegressionRepresentation regRep = new RegressionRepresentation(cVPTotal);
				regRep.simplify();
				rootNode = this.buildCVPTree(regRep);
			}
		}
		return rootNode;
	}
	
	private ArrayList<CoefficientPowerFunction> isolateCVPs(DoubleNode rootNode) {
		ArrayList<CoefficientPowerFunction> cVPList = new ArrayList<CoefficientPowerFunction>();		
		// check if terminal
		if(rootNode instanceof CoefficientPowerFunction) {
			cVPList.add((CoefficientPowerFunction) rootNode);		
		} else if(rootNode instanceof AddFunction) {
			ArrayList<CoefficientPowerFunction> cVPs = new ArrayList<CoefficientPowerFunction>();
			cVPs = this.isolateCVPs((DoubleNode) rootNode.getChild(0));
			// add the retrieved CVP nodes
			for(CoefficientPowerFunction c: cVPs) {
				cVPList.add(c);
			}
			cVPs = this.isolateCVPs((DoubleNode) rootNode.getChild(1));
			// add the retrieved CVP nodes
			for(CoefficientPowerFunction c: cVPs) {
				cVPList.add(c);
			}
		} else if(rootNode instanceof SubtractFunction) {
			ArrayList<CoefficientPowerFunction> cVPs = new ArrayList<CoefficientPowerFunction>();
			cVPs = this.isolateCVPs((DoubleNode) rootNode.getChild(0));
			// add the retrieved CVP nodes
			for(CoefficientPowerFunction c: cVPs) {
				cVPList.add(c);
			}
			cVPs = this.isolateCVPs((DoubleNode) rootNode.getChild(1));
			// add the retrieved CVP nodes AFTER * the coefficients by -1
			for(CoefficientPowerFunction c: cVPs) {
				// * coefficients by -1 before adding them
				double coefficient = (Double) c.getChild(0).evaluate();
				double newCoefficient = coefficient * -1;
				c.setChild(0, new DoubleLiteral(newCoefficient));
				cVPList.add(c);
			}
		}		
		return cVPList;
	}
	
	private DoubleNode buildCVPTree(RegressionRepresentation rep) {
		ArrayList<CoefficientPowerFunction> regRep = rep.getRegressionRepresentation();
		int regRepSize = regRep.size();
		DoubleNode rootNode = null;
		if(regRepSize>1) {
			rootNode = regRep.get(0);
			for(int i = 1; i<regRepSize; i++) {
				// check if second coefficient <0
				double coefficient = (Double) regRep.get(i).getChild(0).evaluate();
				if(coefficient < 0) {
					// modify sign on second CVP node
					double newCoefficient = coefficient * -1;
					regRep.get(i).setChild(0, new DoubleLiteral(newCoefficient));
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
			rootNode = new CoefficientPowerFunction(new DoubleLiteral(0d), new DoubleVariable("X"), new DoubleLiteral(0d));
		}
		return rootNode;
	}
	
	private DoubleNode expandCVPTree(DoubleNode rootNode) {
		// expand if it is CVP
		if(rootNode instanceof CoefficientPowerFunction) {
			double coefficient = (Double) rootNode.getChild(0).evaluate();
			double power = (Double) rootNode.getChild(2).evaluate();
			if(coefficient==0) {
				rootNode = new DoubleLiteral(0d);
			} else if(power==0) {
				rootNode = new DoubleLiteral(coefficient);
			} else if(coefficient==1 && power==1) {
				rootNode = var;
			} else if(coefficient==1 && power ==-1) {
				rootNode = new ProtectedDivisionFunction(new DoubleLiteral(1d), var);
			} else if(coefficient!=1 && power ==-1) {
				rootNode = new ProtectedDivisionFunction(new DoubleLiteral(coefficient), var);
			} else if(power<-1) {
				rootNode = new ProtectedDivisionFunction(new DoubleLiteral(coefficient), new CoefficientPowerFunction(new DoubleLiteral(1d), new DoubleVariable("X"), new DoubleLiteral((power*-1))));
			} else if(coefficient==1 && power>1) {
				rootNode = new MultiplyFunction(var, new CoefficientPowerFunction(new DoubleLiteral(coefficient), new DoubleVariable("X"), new DoubleLiteral((power-1))));
			} else if(coefficient!=1 && power>1) {
				rootNode = new MultiplyFunction(new DoubleLiteral(coefficient), new CoefficientPowerFunction(new DoubleLiteral(1d), new DoubleVariable("X"), new DoubleLiteral((power))));
			} else if(coefficient!=1 && power==1) {
				rootNode = new MultiplyFunction(new DoubleLiteral(coefficient), var);
			} else {
				System.out.println("CATCH = " + rootNode);
			}
		}
		// expand children
		int arity = rootNode.getArity();
		DoubleNode[] children = (DoubleNode[]) rootNode.getChildren();
		if(arity>0) {
			for(int i = 0; i < arity; i++) {
				children[i] = this.expandCVPTree(children[i]);
			}
		}
		return rootNode;
	}

}








