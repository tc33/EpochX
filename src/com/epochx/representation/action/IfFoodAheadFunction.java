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
package com.epochx.representation.action;

import com.epochx.action.*;
import com.epochx.ant.*;
import com.epochx.representation.*;

/**
 * A <code>FunctionNode</code> which represents the conditional if-then-else 
 * statement typically used in the artificial ant domain. This version of the 
 * if statement has the condition predefined as a check for whether the next 
 * move in the landscape contains a food item.
 */
public class IfFoodAheadFunction extends FunctionNode<Action> {
	
	// The artificial ant the Actions will be controlling.
	private Ant ant;
	
	// The landscape the ant is roaming.
	private AntLandscape landscape;
	
	/**
	 * Construct an IfFoodAheadFunction with no children.
	 * @param ant the ant which this function will be controlling.
	 * @param landscape the landscape upon which the ant is roaming which will
	 * be used to check for food locations.
	 */
	public IfFoodAheadFunction(Ant ant, AntLandscape landscape) {
		this(ant, landscape, null, null);
	}
	
	/**
	 * Construct an IfFoodAheadFunction with two children. When evaluated, 
	 * if given ant's next location on the provided landscape is a food 
	 * location then the first child node will be evaluated and executed, 
	 * otherwise the second child node is evaluated and executed.
	 * @param ant the ant which this function will be controlling.
	 * @param landscape the landscape upon which the ant is roaming which will
	 * be used to check for food locations.
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public IfFoodAheadFunction(Ant ant, AntLandscape landscape, Node<Action> child1, Node<Action> child2) {
		super(child1, child2);
		
		this.ant = ant;
		this.landscape = landscape;
	}
	
	/**
	 * Evaluating an <code>IfFoodAheadFunction</code> involves identifying the 
	 * next location the ant would move to on the landscape were it to be moved. 
	 * If this position contains a food item then the first child is evaluated 
	 * and executed, else the second child is evaluated and executed.
	 * 
	 * <p>One of the children will thus have been evaluated (triggering 
	 * execution of actions at the <code>TerminalNodes</code>) and then this 
	 * method which must return an Action, returns Action.DO_NOTHING which any  
	 * functions higher up in the program tree will execute, but with no 
	 * effect.</p>
	 */
	@Override
	public Action evaluate() {
		if (ant.isFoodAhead()) {
			((Action) getChild(0).evaluate()).execute();
		} else {
			((Action) getChild(1).evaluate()).execute();
		}
		
		return Action.DO_NOTHING;
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the IfFoodAheadFunction which is 
	 * IF-FOOD-AHEAD.
	 */
	@Override
	public String getFunctionName() {
		return "IF-FOOD-AHEAD";
	}

	@Override
	public boolean equals(Object obj) {
		//TODO This should probably check it has the same ant and landscape too.
		return super.equals(obj);
	}
}
