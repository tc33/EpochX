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
package com.epochx.action;

/**
 * Actions provide a system for performing events or actions that have side-effects 
 * rather than returning a literal value. For problems such as the artificial ant, 
 * instead of <code>CandidatePrograms</code> being defined as having a return type 
 * of boolean or double, they have a return type of Action. As such different Action 
 * 'values' are used. The model will define <code>TerminalsNodes</code> with values 
 * that are Action objects such as <code>AntMoveAction<code> or 
 * <code>AntTurnLeftAction</code>.
 * 
 * <p>When these TerminalNodes are evaluated, their action values get executed - which 
 * may result in an artificial ant moving across an artificial landscape. But all 
 * evaluations must return a result, and since the TYPE of these Nodes is Action, they 
 * return Action.DO_NOTHING which indicates that no further execution is required, and 
 * if executed will do nothing.</p>
 */
public abstract class Action {
	
	/**
	 * This predefined Action object should be used when no action should be 
	 * performed. Typically it is the return value of TerminalNode<Action> that
	 * have been evaluated/executed to indicate that there is no further action 
	 * to be carried out.
	 */
	public static final Action DO_NOTHING = new Action() {
		public void execute() {}
	};
	
	/**
	 * The execute method should be called to perform whatever action this 
	 * <code>Action</code> represents. Generally Actions are executed at 
	 * evaluation.
	 */
	public abstract void execute();
	
}
