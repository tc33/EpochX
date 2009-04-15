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
package com.epochx.example.artificialant;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import com.epochx.action.*;
import com.epochx.ant.*;
import com.epochx.core.*;
import com.epochx.core.crossover.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.action.*;
import com.epochx.semantics.AntSemanticModule;
import com.epochx.stats.GenerationStats.GenStatField;
import com.epochx.stats.RunStats.RunStatField;
import com.epochx.util.*;

/**
 * 
 */
public class ArtificialAntSantaFe extends GPAbstractModel<AntAction> {

	private List<String> inputs = new ArrayList<String>();;
	private HashMap<String, TerminalNode<AntAction>> variables = new HashMap<String, TerminalNode<AntAction>>();
	private Dimension dimension = new Dimension(32, 32);	
	private ArrayList<Point> foodLocations = new ArrayList<Point>();
	private AntLandscape antLandscape = new AntLandscape(dimension, null);
	private Ant ant = new Ant(600, antLandscape);
	private int noOfFoodPellets;
	private int run = 1;
	private int fitAssessed = 0;
	
	public ArtificialAntSantaFe() {		
		inputs = FileManip.loadInput(new File("inputsantafe.txt"));
		// create list of food locations
		foodLocations = new ArrayList<Point>();
		for(String i: inputs) {
			if(!i.equalsIgnoreCase("DC")) {
				String[] parts = i.split(":");
				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				Point p = new Point(x, y);
				foodLocations.add(p);
			}
		}
		
		noOfFoodPellets = foodLocations.size();
		
		configure();
	}
	
	public void configure() {
		
		// Define variables.
		variables.put("MOVE", new TerminalNode<AntAction>(new AntMoveAction(ant)));
		variables.put("TURN-LEFT", new TerminalNode<AntAction>(new AntTurnLeftAction(ant)));
		variables.put("TURN-RIGHT", new TerminalNode<AntAction>(new AntTurnRightAction(ant)));
		
		setPopulationSize(50);
		setNoGenerations(10);
		setCrossoverProbability(0.9);
		setReproductionProbability(0.1);
		setNoRuns(1);
		setPouleSize(10);
		setNoElites(10);
		setInitialMaxDepth(6);
		setMaxDepth(10);
		setPouleSelector(new TournamentSelector<AntAction>(7, this));
		setParentSelector(new RandomSelector<AntAction>());
		setCrossover(new UniformPointCrossover<AntAction>());
		setStateCheckedCrossover(false);
		setSemanticModule(new AntSemanticModule(getTerminals(), this));
		setInitialiser(new RampedHalfAndHalfInitialiser<AntAction>(this));
	}
	
	@Override
	public List<FunctionNode<?>> getFunctions() {
		// Define functions.
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
		functions.add(new IfFoodAheadFunction(ant, antLandscape));
		functions.add(new Seq2Function());
		functions.add(new Seq3Function());
		return functions;
	}

	@Override
	public List<TerminalNode<?>> getTerminals() {		
		// Define terminals.
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
		terminals.add(variables.get("MOVE"));
		terminals.add(variables.get("TURN-LEFT"));
		terminals.add(variables.get("TURN-RIGHT"));		
		return terminals;
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.GPModel#getFitness(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public double getFitness(CandidateProgram<AntAction> program) {
		fitAssessed++;
		System.out.println(program.getRootNode());
		System.out.println(fitAssessed);
		// refresh food list before evaluation
		// hard copy foodLocations
		ArrayList<Point> fl = new ArrayList<Point>();
		for(Point p: foodLocations) {
			fl.add(p);
		}
		System.out.println(fl);
		antLandscape.setFoodLocations(fl);
		ant.resetAnt(600, antLandscape);
		System.out.println("A1 = " + ant);
		// run the ant
		while(ant.getMoves()<ant.getMaxMoves()) {
			program.evaluate();
		}
		System.out.println("A2 = " + ant);
		// calculate score
		double score = (double) (noOfFoodPellets - ant.getFoodEaten());
		System.out.println(score);
		return score;
	}
	
	public static void main(String[] args) {
		GPController.run(new ArtificialAntSantaFe());
	}
	
	public void runStats(int runNo, Object[] stats) {
		this.run = runNo;
		System.out.print("Run number " + runNo + " complete.");
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}

	public RunStatField[] getRunStatFields() {
		return new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM};
	}
	
	@Override
	public void generationStats(int generation, Object[] stats) {
		ArrayList<String> output = new ArrayList<String>();
		System.out.println(run + "\t" + generation + "\t");
		String part = run + "\t" + generation + "\t";
		for (Object s: stats) {
			part = part + s;
			part = part + "\t";
		}
		part = part + "\n";
		output.add(part);
		FileManip.doOutput(null, output, "output.txt", true);
	}

	@Override
	public GenStatField[] getGenStatFields() {
		return new GenStatField[]{GenStatField.FITNESS_AVE, GenStatField.FITNESS_MIN, GenStatField.LENGTH_AVE};
	}
}
