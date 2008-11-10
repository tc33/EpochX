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

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import java.util.*;

import com.epochx.core.representation.*;

/**
 * @author Lawrence Beadle
 *
 */
public class BooleanSemanticModule implements SemanticModule {

	private BDDFactory bddLink;
	private HashMap<Node, BDD> bddList;
	private List<Node> terminals;

	public BooleanSemanticModule(List<Node> terminals) {
		this.terminals = terminals;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#start()
	 */
	@Override
	public void start() {
		// set up BDD analyser
		this.bddLink = BDDFactory.init("cudd", (terminals.size()*500), (terminals.size()*500));
		// create all possible base variables
		this.bddList = new HashMap<Node, BDD>();
		for(int i = 0; i<terminals.size(); i++) {
			bddList.put(terminals.get(i), bddLink.ithVar(i));
		}
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#stop()
	 */
	@Override
	public void stop() {
		// close down bdd link
		bddLink.done();		
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public BooleanRepresentation codeToBehaviour(CandidateProgram program) {
		// pull root node out of candidate program
		Node rootNode = program.getRootNode();
		// break up nodes and call respective bits recursively
		if(rootNode instanceof Variable) {
			// A TERMINAL
			return new BooleanRepresentation(bddList.get(rootNode));
		} else if(rootNode instanceof NotFunction) {
			// NOT
			// resolve child behaviour
			BooleanRepresentation childBehaviour = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(0)));
			BDD childBDD = childBehaviour.getBDD();
			BDD result = childBDD.not();
			return new BooleanRepresentation(result);
		} else if(rootNode instanceof AndFunction) {            	
			// AND
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(0)));
			BooleanRepresentation childBehaviour2 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(1)));
			BDD childBDD1 = childBehaviour1.getBDD();
			BDD childBDD2 = childBehaviour2.getBDD();
			BDD result = childBDD1.and(childBDD2);
			return new BooleanRepresentation(result);                
		} else if(rootNode instanceof OrFunction) {
			// OR
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(0)));
			BooleanRepresentation childBehaviour2 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(1)));
			BDD childBDD1 = childBehaviour1.getBDD();
			BDD childBDD2 = childBehaviour2.getBDD();
			BDD result = childBDD1.or(childBDD2);
			return new BooleanRepresentation(result);                
		} else if(rootNode instanceof IfFunction) {
			// IF
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(0)));
			BooleanRepresentation childBehaviour2 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(1)));
			BooleanRepresentation childBehaviour3 = this.codeToBehaviour(new CandidateProgram(rootNode.getChild(2)));
			BDD childBDD1 = childBehaviour1.getBDD();
			BDD childBDD2 = childBehaviour2.getBDD();
			BDD childBDD3 = childBehaviour3.getBDD();
			BDD result = childBDD1.ite(childBDD2, childBDD3);
			return new BooleanRepresentation(result);            	                
		}
		// if it gets here without returning something is wrong - unrecognised node present
		throw new IllegalArgumentException("BDD Resolution error - Unidentified Syntax Present");
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#behaviourToCode(com.epochx.semantics.Behaviour)
	 */
	@Override
	public CandidateProgram behaviourToCode(Behaviour representation) {
		// convert to boolean representation
		BooleanRepresentation booleanRep = (BooleanRepresentation) representation;
		BDD bddRep = booleanRep.getBDD();
		Node rootNode = this.resolveTranslation(bddRep);
		// TODO variable labels!!!
		
		
		
		CandidateProgram result = new CandidateProgram(rootNode);	
		return result;
	}
	
	private Node resolveTranslation(BDD topBDD) {
		// check tautology
		if(topBDD.isOne() || topBDD.isZero()) {
			throw new IllegalArgumentException("CANNOT REVERSE TRANSLATE A TAUTOLOGY");
		}
		// get the BDD children
		BDD highChild = topBDD.high();
		BDD lowChild = topBDD.low();
		// work out what's what and return node structure
		if((!highChild.isOne() && !highChild.isZero()) && (!lowChild.isOne() && !lowChild.isZero())) {
			// IF statement
			IfFunction thisIF = new IfFunction(new TerminalNode(topBDD.var()), this.resolveTranslation(highChild), this.resolveTranslation(lowChild));
			return thisIF;
		} else if((!highChild.isOne() && !highChild.isZero()) && lowChild.isZero()) {
			// AND statement
			AndFunction thisAND = new AndFunction(new TerminalNode(topBDD.var()), this.resolveTranslation(highChild));
			return thisAND;
		} else if(highChild.isOne() && (!lowChild.isOne() && !lowChild.isZero())) {
			// OR statement
			OrFunction thisOR = new OrFunction(new TerminalNode(topBDD.var()), this.resolveTranslation(lowChild));
			return thisOR;
		} else if(highChild.isZero() && lowChild.isOne()) {
			// NOT statement
			NotFunction thisNOT = new NotFunction(new TerminalNode(topBDD.var()));
			return thisNOT;
		} else if(highChild.isOne() && lowChild.isZero()) {
			// Variable
			TerminalNode thisVar = new TerminalNode(topBDD.var());
			return thisVar;
		} else if(highChild.isZero() && (!lowChild.isOne() && !lowChild.isZero())) {
			// AND ( NOT...
			NotFunction thisNOT = new NotFunction(new TerminalNode(topBDD.var()));
			AndFunction thisAND = new AndFunction(thisNOT, this.resolveTranslation(lowChild));
			return thisAND;
		} else if((!highChild.isOne() && !highChild.isZero()) && lowChild.isOne()) {
			// OR ( NOT...
			NotFunction thisNOT = new NotFunction(new TerminalNode(topBDD.var()));
			OrFunction thisOR = new OrFunction(thisNOT, this.resolveTranslation(highChild));
			return thisOR;
		}
		// throw eception if it made it to here without returning
		throw new IllegalArgumentException("Unidentified Syntax in Reverse Translation");
	}			
}
