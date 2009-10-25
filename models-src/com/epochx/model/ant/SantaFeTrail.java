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
package com.epochx.model.ant;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.epochx.action.*;
import com.epochx.ant.*;
import com.epochx.core.*;
import com.epochx.representation.*;
import com.epochx.representation.action.*;
import com.epochx.stats.GenerationStatField;

/**
 * 
 */
public class SantaFeTrail extends GPAbstractModel<Action> {

	private HashMap<String, TerminalNode<Action>> variables = new HashMap<String, TerminalNode<Action>>();	
	private AntLandscape landscape;
	private Ant ant;
	
	private static final Point[] foodLocations = {
		new Point(1,0), new Point(2,0), new Point(3,0), new Point(3,1),
		new Point(3,2), new Point(3,3), new Point(3,4), new Point(3,5),
		new Point(4,5), new Point(5,5), new Point(6,5), new Point(8,5),
		new Point(9,5), new Point(10,5), new Point(11,5), new Point(12,5),
		new Point(12,6), new Point(12,7), new Point(12,8), new Point(12,9),
		new Point(12,11), new Point(12,12), new Point(12,13), new Point(12,14),
		new Point(12,17), new Point(12,18), new Point(12,19), new Point(12,20),
		new Point(12,21), new Point(12,22), new Point(12,23), new Point(11,24),
		new Point(10,24), new Point(9,24), new Point(8,24), new Point(7,24),
		new Point(4,24), new Point(3,24), new Point(1,25), new Point(1,26),
		new Point(1,27), new Point(1,28), new Point(2,30), new Point(3,30),
		new Point(4,30), new Point(5,30), new Point(7,29), new Point(7,28),
		new Point(8,27), new Point(9,27), new Point(10,27), new Point(11,27),
		new Point(12,27), new Point(13,27), new Point(14,27), new Point(16,26),
		new Point(16,25), new Point(16,24), new Point(16,21), new Point(16,20),
		new Point(16,19), new Point(16,18), new Point(17,15), new Point(20,14),
		new Point(20,13), new Point(20,10), new Point(20,9), new Point(20,8),
		new Point(20,7), new Point(21,5), new Point(22,5), new Point(24,4),
		new Point(24,3), new Point(25,2), new Point(26,2), new Point(27,2),
		new Point(29,3), new Point(29,4), new Point(29,6), new Point(29,9),
		new Point(29,12), new Point(28,14), new Point(27,14), new Point(26,14),
		new Point(23,15), new Point(24,18), new Point(27,19), new Point(26,22),
		new Point(23,23)
	};
	
	public SantaFeTrail() {
		landscape = new AntLandscape(new Dimension(32, 32), null);
		ant = new Ant(600, landscape);
		
		configure();
	}
	
	public void configure() {
		// Define variables.
		variables.put("MOVE", new TerminalNode<Action>(new AntMoveAction(ant)));
		variables.put("TURN-LEFT", new TerminalNode<Action>(new AntTurnLeftAction(ant)));
		variables.put("TURN-RIGHT", new TerminalNode<Action>(new AntTurnRightAction(ant)));
	}
	
	@Override
	public List<FunctionNode<Action>> getFunctions() {
		// Define functions.
		List<FunctionNode<Action>> functions = new ArrayList<FunctionNode<Action>>();
		functions.add(new IfFoodAheadFunction(ant, landscape));
		functions.add(new Seq2Function());
		functions.add(new Seq3Function());
		return functions;
	}

	@Override
	public List<TerminalNode<Action>> getTerminals() {		
		// Define terminals.
		List<TerminalNode<Action>> terminals = new ArrayList<TerminalNode<Action>>();
		terminals.add(variables.get("MOVE"));
		terminals.add(variables.get("TURN-LEFT"));
		terminals.add(variables.get("TURN-RIGHT"));		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram<Action> program) {		
		landscape.setFoodLocations(new ArrayList<Point>(Arrays.asList(foodLocations)));
		ant.reset(600, landscape);

		// Run the ant.
		while(ant.getMoves() < ant.getMaxMoves()) {
			program.evaluate();
		}

		// Calculate score.
		double score = (double) (foodLocations.length - ant.getFoodEaten());

		return score;
	}
	
	public Ant getAnt() {
		return ant;
	}
	
	public AntLandscape getAntLandScape() {
		return landscape;
	}
	
	public static void main(String[] args) {
		GPAbstractModel<Action> model = new SantaFeTrail();
		model.setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN});
		GPController.run(model);
	}
}
