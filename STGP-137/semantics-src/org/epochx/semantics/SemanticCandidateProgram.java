/*  
 *  Copyright 2007-2010 Lawrence Beadle & Tom Castle
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

import org.epochx.epox.EpoxParser;
import org.epochx.epox.Node;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.eval.MalformedProgramException;

public class SemanticCandidateProgram {
	
	private CandidateProgram program;
	private EpoxParser parser;
	
	public SemanticCandidateProgram(CandidateProgram program) {
		this.program = program;
		parser = new EpoxParser();
	}
	
	public SemanticCandidateProgram(CandidateProgram program, Ant ant) {
		this.program = program;
		parser = new EpoxParser();
		parser.setAnt(ant);
	}
	
	public Node getRootNode() {
		if(program instanceof GPCandidateProgram) {
			return ((GPCandidateProgram) program).getRootNode();
		} else if(program instanceof GRCandidateProgram) {
			try {
				return parser.parse(program.toString());
			} catch (MalformedProgramException e) {
				System.out.println("ERROR PARSING CANDIDATE PROGRAM IN SEMANTIC CANDIDATE PROGRAM");
				e.printStackTrace();
			}
		} else if(program instanceof GECandidateProgram) {
			try {
				return parser.parse(program.toString());
			} catch (MalformedProgramException e) {
				System.out.println("ERROR PARSING CANDIDATE PROGRAM IN SEMANTIC CANDIDATE PROGRAM");
				e.printStackTrace();
			}
		}
		System.out.println("UNIDENTIFIED CANDIDATE PROGRAM IN SEMANTIC CANDIDATE PROGRAM");
		return null;		
	}	
}
