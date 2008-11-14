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

import com.epochx.core.representation.*;

import java.util.*;

/**
 * @author lb212
 *
 */
public class TestBooleanModule {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList<Node> terminals = new ArrayList<Node>();
		ArrayList<Node> functions = new ArrayList<Node>();
		
		functions.add(new IfFunction(null, null, null));
		functions.add(new AndFunction(null, null));
		functions.add(new OrFunction(null, null));
		functions.add(new NotFunction(null));
		
		terminals.add(new Variable<Boolean>("D3"));
		terminals.add(new Variable<Boolean>("D2"));
		terminals.add(new Variable<Boolean>("D1"));
		terminals.add(new Variable<Boolean>("D0"));
		terminals.add(new Variable<Boolean>("A1"));
		terminals.add(new Variable<Boolean>("A0"));
		
		BooleanSemanticModule semMod = new BooleanSemanticModule(terminals, null);
		semMod.start();
		
		// make prog 1
		Node<Boolean> rootNode1 = (Node<Boolean>) functions.get(2).clone();
		rootNode1.setChild((Node<Boolean>)terminals.get(5).clone(), 0);
		rootNode1.setChild((Node<Boolean>)terminals.get(4).clone(), 1);
		CandidateProgram program1 = new CandidateProgram(rootNode1, null);
		
		// make prog 2
		Node<Boolean> rootNode2 = (Node<Boolean>) functions.get(1).clone();
		Node<Boolean> nodePart = (Node<Boolean>) functions.get(1).clone();
		nodePart.setChild((Node<Boolean>)terminals.get(4).clone(), 0);
		nodePart.setChild((Node<Boolean>)terminals.get(5).clone(), 1);
		rootNode2.setChild(nodePart, 0);
		rootNode2.setChild((Node<Boolean>)terminals.get(5).clone(), 1);
		CandidateProgram program2 = new CandidateProgram(rootNode2, null);
		
		// make representations
		Behaviour rep1 = semMod.codeToBehaviour(program1);
		Behaviour rep2 = semMod.codeToBehaviour(program2);
		
		// logical test
		if(rep1.equals(rep2)) {
			System.out.println("SUCCESS - " + program1.toString() + " === " + program2.toString());
		} else {
			System.out.println("NOT EQUAL - " + program1.toString() + " != " + program2.toString());
		}
		
		System.out.println("\nBACK TRANSLATE: " + semMod.behaviourToCode(rep2).toString());
		
		semMod.stop();
		System.exit(777);

	}

}
