/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.gp.model.ant;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.epochx.core.Controller;
import org.epochx.gp.model.GPAbstractModel;
import org.epochx.gp.representation.*;
import org.epochx.gp.representation.ant.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.GenerationStatField;
import org.epochx.tools.ant.*;


/**
 * 
 */
public class JohnMuirTrail extends GPAbstractModel {

	private AntLandscape landscape;
	private Ant ant;
	
	private static final Point[] foodLocations = {
		new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0),
		new Point(5,0), new Point(6,0), new Point(7,0), new Point(8,0),
		new Point(9,0), new Point(10,0), new Point(10,1), new Point(10,2),
		new Point(10,3), new Point(10,4), new Point(10,5), new Point(10,6),
		new Point(10,7), new Point(10,8), new Point(10,9), new Point(10,10),
		new Point(9,10), new Point(8,10), new Point(7,10), new Point(6,10),
		new Point(5,10), new Point(4,10), new Point(3,10), new Point(3,9),
		new Point(3,8), new Point(3,7), new Point(3,6), new Point(3,5),
		new Point(2,5), new Point(1,5), new Point(0,5), new Point(31,5),
		new Point(30,5), new Point(29,5), new Point(28,5), new Point(27,5),
		new Point(26,5), new Point(25,5), new Point(24,6), new Point(24,7),
		new Point(24,8), new Point(24,9), new Point(24,10), new Point(23,11),
		new Point(22,11), new Point(21,11), new Point(20,11), new Point(19,11),
		new Point(18,12), new Point(18,13), new Point(18,14), new Point(18,15),
		new Point(18,16), new Point(18,17), new Point(18,20), new Point(18,21),
		new Point(18,22), new Point(18,23), new Point(18,24), new Point(18,25),
		new Point(17,27), new Point(16,27), new Point(15,27), new Point(14,27),
		new Point(13,27), new Point(12,27), new Point(10,27), new Point(9,27),
		new Point(8,27), new Point(7,27), new Point(4,27), new Point(4,26),
		new Point(4,25), new Point(4,24), new Point(5,22), new Point(7,21), 
		new Point(8,18), new Point(11,17), new Point(12,15), new Point(15,14), 
		new Point(14,12), new Point(11,11), new Point(12,8), new Point(14,7), 
		new Point(15,4)
	};
	
	public JohnMuirTrail() {
		landscape = new AntLandscape(new Dimension(32, 32), null);
		ant = new Ant(600, landscape);
	}
	
	@Override
	public List<Node> getFunctions() {
		// Define functions.
		List<Node> functions = new ArrayList<Node>();
		functions.add(new IfFoodAheadFunction(ant));
		functions.add(new Seq2Function());
		functions.add(new Seq3Function());
		return functions;
	}

	@Override
	public List<Node> getTerminals() {		
		// Define terminals.
		List<Node> terminals = new ArrayList<Node>();
		terminals.add(new AntMoveAction(ant));
		terminals.add(new AntTurnLeftAction(ant));
		terminals.add(new AntTurnRightAction(ant));
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
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
		GPAbstractModel model = new JohnMuirTrail();
		//model.setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN});
		Controller.run(model);
	}
}
