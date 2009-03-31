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
import com.epochx.core.representation.*;

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
		// TODO
		
		// extract and simplify program
		Node<Double> rootNode = program.getRootNode();
		
		// resolve any multiply by zeros
		this.removeMultiplyByZeros(rootNode);
		
		// resolve PDIVs with equal subtrees and PDIV by 0 to 0
		this.removeAllPDivsWithSameSubtrees(rootNode);
		
		// resolve constant calculations
		this.resolveConstantCalculations(rootNode);
		
		// collect up multiply and PDiv into powers
		this.collectUpPowers(rootNode);
		
		// scan for largest power and smallest power
		
		// extract coefficients and put into double array
		
		// construct new RegressionBehaviour
		
		return null;
	}

	private void removeMultiplyByZeros(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
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
			} else {
				// recurse on other functions
				for(int i = 0; i<arity; i++) {
					this.removeMultiplyByZeros(children[i]);
				}
			}
		}
	}
	
	private void resolveConstantCalculations(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();
			// check if all child nodes are numbers
			boolean allChildrenAreTerminalNodes = true;
			for(int i = 0; i<arity; i++) {
				if(!(children[i] instanceof TerminalNode)) {
					allChildrenAreTerminalNodes = false;
				}
			}
			// decide what to do - reduce or recurse
			if(allChildrenAreTerminalNodes) {
				// resolve root node to constant
				Double result = rootNode.evaluate();
				rootNode = new TerminalNode<Double>(result);
			} else {
				for(int i = 0; i<arity; i++) {
					this.resolveConstantCalculations(children[i]);
				}
			}
		}
	}
	
	private void removeAllPDivsWithSameSubtrees(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();			
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
			} else {
				for(int i = 0; i<arity; i++) {
					this.removeAllPDivsWithSameSubtrees(children[i]);
				}
			}
		}
	}
	
	private void collectUpPowers(Node<Double> rootNode) {
		// get the child nodes
		int arity = rootNode.getArity();		
		
		// check if terminal
		if(arity>0) {
			// get children
			Node[] children = rootNode.getChildren();			
			// decide what to do - reduce or recurse
			if(rootNode instanceof MultiplyFunction) {
				Variable<Double> x = new Variable<Double>("X");
				if((children[0] instanceof Variable) && (children[1] instanceof Variable)) {
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(2d));
					rootNode = powerNode;
				} else if((children[0] instanceof PowerFunction) && (children[1] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() + 1;
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[1] instanceof PowerFunction) && (children[0] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[1].getChild(1).evaluate() + 1;
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[0] instanceof PowerFunction) && (children[1] instanceof PowerFunction)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() + (Double) children[1].getChild(1).evaluate();
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else {
					for(int i = 0; i<arity; i++) {
						this.collectUpPowers(children[i]);
					}
				}
			} else if(rootNode instanceof ProtectedDivisionFunction) {
				Variable<Double> x = new Variable<Double>("X");
				if((children[0] instanceof PowerFunction) && (children[1] instanceof Variable)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() - 1;
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[1] instanceof PowerFunction) && (children[0] instanceof Variable)) {
					// work out new power
					// need to negate this to make it accurate
					Double power = ((Double) children[1].getChild(1).evaluate() - 1) * -1;
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else if((children[0] instanceof PowerFunction) && (children[1] instanceof PowerFunction)) {
					// work out new power
					Double power = (Double) children[0].getChild(1).evaluate() - (Double) children[1].getChild(1).evaluate();
					Node<Double> powerNode = new PowerFunction(x, new TerminalNode<Double>(power));
					rootNode = powerNode;
				} else {
					for(int i = 0; i<arity; i++) {
						this.collectUpPowers(children[i]);
					}
				}
			} else {
				for(int i = 0; i<arity; i++) {
					this.collectUpPowers(children[i]);
				}
			}
		}
	}

}
