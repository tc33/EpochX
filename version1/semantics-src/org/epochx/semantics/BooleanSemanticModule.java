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

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import java.util.*;

import org.epochx.core.Model;
import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;

/**
 * The Boolean Semantic Module provides semantic functionality 
 * for problems in the Boolean domain including reduction to 
 * canonical representations of behaviour
 */
public class BooleanSemanticModule implements SemanticModule {

	private BDDFactory bddLink;
	private List<BDD> bddList;
	private List<BooleanVariable> terminals;
	private Model model;
	private String environment;
	
	/**
	 * Constructor for Boolean Semantic Module
	 * @param list List of terminal nodes
	 * @param model The GPModel object
	 */
	public BooleanSemanticModule(List<BooleanVariable> list, Model model, String environment) {
		this.terminals = list;
		this.model = model;
		this.environment = environment;
	}

	public void start() {
		// set up BDD analyser
		this.bddLink = BDDFactory.init("cudd", (terminals.size()*500), (terminals.size()*500));
		// create all possible base variables
		this.bddList = new ArrayList<BDD>();
		for(int i = 0; i<terminals.size(); i++) {
			bddList.add(bddLink.ithVar(i));
		}
	}

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
		SemanticCandidateProgram prog1 = new SemanticCandidateProgram(program);
		BooleanNode rootNode = (BooleanNode) prog1.getRootNode();
		return this.calculateBooleanRepresentation(rootNode);
	}
		
	private BooleanRepresentation calculateBooleanRepresentation(BooleanNode rootNode) {	
		// break up nodes and call respective bits recursively
		if(rootNode instanceof BooleanVariable) {
			// A TERMINAL
			int index = terminals.indexOf(rootNode);
			return new BooleanRepresentation(bddList.get(index));
		} else if(rootNode instanceof NotFunction) {
			// NOT
			// resolve child behaviour
			BooleanRepresentation childBehaviour = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(0));
			BDD childBDD = childBehaviour.getBDD();
			BDD result = childBDD.not();
			return new BooleanRepresentation(result);
		} else if(rootNode instanceof AndFunction) {            	
			// AND
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(0));
			BooleanRepresentation childBehaviour2 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(1));
			BDD childBDD1 = childBehaviour1.getBDD();
			BDD childBDD2 = childBehaviour2.getBDD();
			BDD result = childBDD1.and(childBDD2);
			return new BooleanRepresentation(result);                
		} else if(rootNode instanceof OrFunction) {
			// OR
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(0));
			BooleanRepresentation childBehaviour2 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(1));
			BDD childBDD1 = childBehaviour1.getBDD();
			BDD childBDD2 = childBehaviour2.getBDD();
			BDD result = childBDD1.or(childBDD2);
			return new BooleanRepresentation(result);                
		} else if(rootNode instanceof IfFunction) {
			// IF
			// resolve child behaviour
			BooleanRepresentation childBehaviour1 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(0));
			BooleanRepresentation childBehaviour2 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(1));
			BooleanRepresentation childBehaviour3 = this.calculateBooleanRepresentation((BooleanNode) rootNode.getChild(2));
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
	public CandidateProgram behaviourToCode(Representation representation) {
		// convert to boolean representation
		BooleanRepresentation booleanRep = (BooleanRepresentation) representation;
		BDD bddRep = booleanRep.getBDD();
		BooleanNode rootNode = this.resolveTranslation(bddRep);		
		CandidateProgram result = null;
		if(environment.equalsIgnoreCase("GP")) {
			result = new GPCandidateProgram(rootNode, (GPModel) model);
		} else if(environment.equalsIgnoreCase("GE")) {
			// TODO construct GE program
		} else if(environment.equalsIgnoreCase("GR")) {
			// TODO construct GR program
		}
			
		return result;
	}
	
	private BooleanNode resolveTranslation(BDD topBDD) {
		// check tautology
		if(topBDD.isOne() || topBDD.isZero()) {
			throw new IllegalArgumentException("CANNOT REVERSE TRANSLATE A CONSTANT");
		}
		// get the BDD children
		BDD highChild = topBDD.high();
		BDD lowChild = topBDD.low();
		// work out what's what and return node structure
		if((!highChild.isOne() && !highChild.isZero()) && (!lowChild.isOne() && !lowChild.isZero())) {
			// IF statement
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			IfFunction thisIF = new IfFunction(tNode, this.resolveTranslation(highChild), this.resolveTranslation(lowChild));
			return thisIF;
		} else if((!highChild.isOne() && !highChild.isZero()) && lowChild.isZero()) {
			// AND statement
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			AndFunction thisAND = new AndFunction(tNode, this.resolveTranslation(highChild));
			return thisAND;
		} else if(highChild.isOne() && (!lowChild.isOne() && !lowChild.isZero())) {
			// OR statement
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			OrFunction thisOR = new OrFunction(tNode, this.resolveTranslation(lowChild));
			return thisOR;
		} else if(highChild.isZero() && lowChild.isOne()) {
			// NOT statement
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			NotFunction thisNOT = new NotFunction(tNode);
			return thisNOT;
		} else if(highChild.isOne() && lowChild.isZero()) {
			// Variable
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			BooleanVariable thisVar = tNode;
			return thisVar;
		} else if(highChild.isZero() && (!lowChild.isOne() && !lowChild.isZero())) {
			// AND ( NOT...
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			NotFunction thisNOT = new NotFunction(tNode);
			AndFunction thisAND = new AndFunction(thisNOT, this.resolveTranslation(lowChild));
			return thisAND;
		} else if((!highChild.isOne() && !highChild.isZero()) && lowChild.isOne()) {
			// OR ( NOT...
			BooleanVariable tNode = (BooleanVariable) terminals.get(topBDD.var()).clone();
			NotFunction thisNOT = new NotFunction(tNode);
			OrFunction thisOR = new OrFunction(thisNOT, this.resolveTranslation(highChild));
			return thisOR;
		}
		// throw exception if it made it to here without returning
		throw new IllegalArgumentException("Unidentified Syntax in Reverse Translation");
	}			
}
