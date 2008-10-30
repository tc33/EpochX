/*  
 *  Copyright 2007-2008 Lawrence Beadle
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

package com.epochx.aasf;

/**
 * This class represents an ant as an object for the aritficial ant simulation
 * @author Lawrence Beadle
 */
public class Ant {
    
    private String orientation;
    private int moves;
    private int xLocation;
    private int yLocation;
    private int foodEaten;
    private int maxMoves;
    
    /**
     * Contructor for the artifical ant model
     * @param timeSteps The maximum number of timesteps the ant is allowed to move - both turns and moves count as one step
     */
    public Ant(int timeSteps) {
        orientation = "E";
        moves = 0;
        xLocation = 0;
        yLocation = 0;
        foodEaten = 0;
        maxMoves = timeSteps;
    }
    
    /**
     * Makes the ant turn left
     */
    public void turnLeft() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation.equalsIgnoreCase("E")) {
            orientation = "N";
        } else if(orientation.equalsIgnoreCase("N")) {
            orientation = "W";
        } else if(orientation.equalsIgnoreCase("W")) {
            orientation = "S";
        } else if(orientation.equalsIgnoreCase("S")) {
            orientation = "E";
        }
        moves++;
    }
    
    /**
     * Makes the ant turn right
     */
    public void turnRight() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation.equalsIgnoreCase("E")) {
            orientation = "S";
        } else if(orientation.equalsIgnoreCase("S")) {
            orientation = "W";
        } else if(orientation.equalsIgnoreCase("W")) {
            orientation = "N";
        } else if(orientation.equalsIgnoreCase("N")) {
            orientation = "E";
        }
        moves++;
    }
    
    /**
     * Simulates one move in the ant world
     */
    public void move() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation.equalsIgnoreCase("E")) {
            if(xLocation<31) {
                xLocation++;
            } else {
                xLocation = 0;
            }
        } else if(orientation.equalsIgnoreCase("N")) {
            if(yLocation>0) {
                yLocation--;
            } else {
                yLocation = 31;
            }
        } else if(orientation.equalsIgnoreCase("W")) {
            if(xLocation>0) {
                xLocation--;
            } else {
                xLocation = 31;
            }
        } else if(orientation.equalsIgnoreCase("S")) {
            if(yLocation<31) {
                yLocation++;
            } else {
                yLocation = 0;
            }
        }
        moves++;
    }
    
    /**
     * Increments the number of food pellets the ant has eaten
     */
    public void eatFood() {
        if(moves>=maxMoves) {
            return;
        }
        foodEaten++;
    }
    
    /**
     * Returns the x location of the ant
     * @return The X axis location of the ant
     */
    public int getXLocation() {
        return xLocation;
    }
    
    /**
     * Returns the Y axis location of the ant
     * @return The Y axis location
     */
    public int getYLocation() {
        return yLocation;
    }
    
    /**
     * Gets the number of moves completed
     * @return The number of moves completed
     */
    public int getMoves() {
        return moves;
    }
    
    /**
     * Returns the number of food pellets eaten
     * @return The number of food pellets eaten
     */
    public int getFoodEaten() {
        return foodEaten;
    }
    
    /**
     * Returns the direction the Ant is facing N S E W
     * @return N S E W
     */
    public String getOrientation() {
        return orientation;
    }
    
    /**
     * Sets the location of the ant
     * @param x The x axis location of the ant
     * @param y The Y axis location of the ant
     */
    public void setLocation(int x, int y) {
        xLocation = x;
        yLocation = y;
    }
    
    /**
     * Sets the orientation of the ant
     * @param o The orientation either N S E W
     */
    public void setOrientation(String o) {
        orientation = o;
    }
    
    /**
     * increments moves for skip algorithm required to prevent ant falling into deadends
     */
    public void skip() {
        moves++;
    }
}
