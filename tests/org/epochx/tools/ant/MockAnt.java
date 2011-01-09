/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.tools.ant;

import java.awt.Dimension;


/**
 * A mock ant for the purpose of testing.
 */
public class MockAnt extends Ant {

	private int move;
	private int skip;
	private int turnLeft;
	private int turnRight;
	
	private boolean isFoodAhead;
	
	/**
	 * Constructs a <code>MockAnt</code>.
	 */
	public MockAnt() {
		this(0, new AntLandscape(new Dimension(0, 0)));
	}
	
	/**
	 * Constructs a <code>MockAnt</code>.
	 * 
	 * @param timeSteps will be passed to superclass.
	 * @param landscape will be passed to superclass.
	 */
	public MockAnt(int timeSteps, AntLandscape landscape) {
		super(timeSteps, landscape);
	}
	
	/**
	 * Increments the count for how many times this method has been called, then
	 * calls the superclass <code>move</code> method.
	 */
	@Override
	public void move() {
		move++;
		
		super.move();
	}
	
	/**
	 * Returns a count for how many times the <code>move</code> method has been
	 * called.
	 * 
	 * @return an integer count of how many times the move method has been 
	 * called.
	 */
	public int getMoveCount() {
		return move;
	}
	
	/**
	 * Increments the count for how many times this method has been called, then
	 * calls the superclass <code>skip</code> method.
	 */
	@Override
	public void skip() {
		skip++;
		
		super.skip();
	}
	
	/**
	 * Returns a count for how many times the <code>skip</code> method has been
	 * called.
	 * 
	 * @return an integer count of how many times the skip method has been 
	 * called.
	 */
	public int getSkipCount() {
		return skip;
	}
	
	/**
	 * Increments the count for how many times this method has been called, then
	 * calls the superclass <code>turnLeft</code> method.
	 */
	@Override
	public void turnLeft() {
		turnLeft++;
		
		super.turnLeft();
	}
	
	/**
	 * Returns a count for how many times the <code>turnLeft</code> method has 
	 * been called.
	 * 
	 * @return an integer count of how many times the turnLeft method has been 
	 * called.
	 */
	public int getTurnLeftCount() {
		return turnLeft;
	}
	
	/**
	 * Increments the count for how many times this method has been called, then
	 * calls the superclass <code>turnLeft</code> method.
	 */
	@Override
	public void turnRight() {
		turnRight++;
		
		super.turnRight();
	}
	
	/**
	 * Returns a count for how many times the <code>turnRight</code> method has 
	 * been called.
	 * 
	 * @return an integer count of how many times the turnRight method has been 
	 * called.
	 */
	public int getTurnRightCount() {
		return turnRight;
	}
	
	/**
	 * Sets the <code>boolean</code> that will be returned by the
	 * <code>isFoodAhead</code> method.
	 * 
	 * @param isFoodAhead the boolean to be returned by isFoodAhead.
	 */
	public void setIsFoodAhead(boolean isFoodAhead) {
		this.isFoodAhead = isFoodAhead;
	}
	
	/**
	 * Returns the mock result of whether food is ahead.
	 */
	@Override
	public boolean isFoodAhead() {
		return isFoodAhead;
	}
}
