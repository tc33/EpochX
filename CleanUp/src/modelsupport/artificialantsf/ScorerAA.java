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

package modelsupport.artificialantsf;

import java.util.ArrayList;

import core.*;

/**
 * The ScorerAA class provides functionality to compare boolean programs scores to the ideal solution * 
 * @author Lawrence Beadle
 */
public class ScorerAA implements core.Scorer {   
    
    private Ant myAnt;
    private int sType;
    private SemanticModule semMod;
    private ArrayList<String> foodLocations, antModel;
    private String currentLocation;
    
    /**
     * Sets the scoring method to be used
     * @param type 1 for input-output, 2 for semantic
     * @param semModX the semantic module for this problem domain
     */
    public void setScoreMethodType(int type, SemanticModule semModX) {
        sType = type;
        semMod = semModX;
    }
    
    /**
     * Calculates the score for a candidtae program
     * @param input The input state
     * @param program The candidate program
     * @return The score the candidate program produce with the defined input state
     */
    public double getScore(ArrayList<String> input, ArrayList<String> program) {
        
        // sort out input with food locations
        // have to copy accross as we need to retain the original food location and modify
        // the current one so the ant can't keep going over the same food.
        foodLocations = new ArrayList<String>();
        antModel = new ArrayList<String>();
        int timeSteps = 600;
        for(String i: input) {
            if(!i.equalsIgnoreCase("DC")) {
                foodLocations.add(i);
            }
        }
        int fSize = foodLocations.size();
        // sort out scoring
        double score = 0;
        if (sType == 1) {
            myAnt = new Ant(timeSteps);
            // loop over running the ant by the number of steps allowed            
            while(myAnt.getMoves()<timeSteps) {
                this.runAnt(program);
            }
            score = (double) myAnt.getFoodEaten();
            return fSize - score;
        } else {
            // sort out semantic scorer for AA + fix return
            // set up vector scoring for model
            // run the ant
            myAnt = new Ant(timeSteps);
            // loop over running the ant by the number of steps allowed            
            while(myAnt.getMoves()<timeSteps) {
                this.runAnt(program);
            }
            int len = 0;
            if(antModel.size()>input.size()) {
                len = input.size();
            } else {
                len = antModel.size();
            }
            score = fSize;
            // compare the paths and create vector score
            String pos1, pos2;
            for(int i = 0; i<len; i++) {
                //System.out.println("S=" + score);
                pos1 = input.get(i);
                pos2 = antModel.get(i);
                if(!pos1.equalsIgnoreCase("DC")) {
                    if(pos1.equalsIgnoreCase(pos2)) {
                        score--;
                    }                   
                }
            }
            return score;
        }        
    }
    
    /**
     * Resolves the programs
     * @param expr An ArrayList<String> representation of a candidate program
     */
    private void runAnt(ArrayList<String> expr) {
        
        int eSize = expr.size();
        
        if(eSize<1) {
            System.out.println("Resolution ERROR - invalid expression - " + expr);
        } else if(eSize==1) {
            if(expr.get(0).equalsIgnoreCase("MOVE")) {
                myAnt.move();
                currentLocation = myAnt.getXLocation() + ":" + myAnt.getYLocation();
                if(foodLocations.contains(currentLocation)) {
                    myAnt.eatFood();
                    foodLocations.remove(currentLocation);
                }
                antModel.add(currentLocation);
            } else if(expr.get(0).equalsIgnoreCase("TURN-LEFT")) {
                myAnt.turnLeft();
            } else if(expr.get(0).equalsIgnoreCase("TURN-RIGHT")) {
                myAnt.turnRight();
            } else if(expr.get(0).equalsIgnoreCase("SKIP")) {
                // do nothing - is escape charachter to maintain if branch integrity
                myAnt.skip();
            } else {
                System.out.println("Resolution ERROR - INVALID TERMINAL DETECTED - " + expr);
                System.exit(666);
            }
        } else {
            if(expr.get(0).equalsIgnoreCase("(")) {
                // bracket scenario - remove each end and resolve expr on inside of bracket
                ArrayList<String> test = new ArrayList<String>();
                int top = expr.size()-1;
                for(int i = 1; i<top; i++) {
                    test.add(expr.get(i));
                }
                this.runAnt(test);
            } else if(expr.get(0).equalsIgnoreCase("IF-FOOD-AHEAD")) {
                boolean foodAhead = false;
                if(myAnt.getOrientation().equalsIgnoreCase("N")) {
                    int x = myAnt.getXLocation();
                    int y = myAnt.getYLocation();
                    y--;
                    currentLocation = x + ":" + y;
                    if(foodLocations.contains(currentLocation)) {
                        foodAhead = true;
                    } else {
                        foodAhead = false;
                    }
                } else if(myAnt.getOrientation().equalsIgnoreCase("E")) {
                    int x = myAnt.getXLocation();
                    int y = myAnt.getYLocation();
                    x++;
                    currentLocation = x + ":" + y;
                    if(foodLocations.contains(currentLocation)) {
                        foodAhead = true;
                    } else {
                        foodAhead = false;
                    }
                } else if(myAnt.getOrientation().equalsIgnoreCase("S")) {
                    int x = myAnt.getXLocation();
                    int y = myAnt.getYLocation();
                    y++;
                    currentLocation = x + ":" + y;
                    if(foodLocations.contains(currentLocation)) {
                        foodAhead = true;
                    } else {
                        foodAhead = false;
                    }
                } else if(myAnt.getOrientation().equalsIgnoreCase("W")) {
                    int x = myAnt.getXLocation();
                    int y = myAnt.getYLocation();
                    x--;
                    currentLocation = x + ":" + y;
                    if(foodLocations.contains(currentLocation)) {
                        foodAhead = true;
                    } else {
                        foodAhead = false;
                    }
                }
                
                // execute what to do
                if(foodAhead == true) {
                    int loc = 1;
                    // first expr
                    if (!expr.get(loc).equalsIgnoreCase("(")) {
                        // if it is terminal
                        ArrayList<String> test = new ArrayList<String>();
                        test.add(expr.get(loc));
                        this.runAnt(test);
                        loc++;
                    } else {
                        // if there is a bracket expression
                        ArrayList<String> test = new ArrayList<String>();
                        int d = 0;
                        while (true) {
                            if (expr.get(loc).equalsIgnoreCase("(")) {
                                d++;
                            }
                            if (expr.get(loc).equalsIgnoreCase(")")) {
                                d--;
                            }
                            test.add(expr.get(loc));
                            if (d == 0) {
                                break;
                            }
                            loc++;
                        }
                        this.runAnt(test);
                        loc++;
                    }
                } else {
                    int loc = 1;
                    // first expr
                    if (!expr.get(loc).equalsIgnoreCase("(")) {
                        loc++;
                    } else {
                        // if there is a bracket expression
                        int d = 0;
                        while (true) {
                            if (expr.get(loc).equalsIgnoreCase("(")) {
                                d++;
                            }
                            if (expr.get(loc).equalsIgnoreCase(")")) {
                                d--;
                            }
                            if (d == 0) {
                                break;
                            }
                            loc++;
                        }
                        loc++;
                    }
                    // second expr
                    try {
                        expr.get(loc).equalsIgnoreCase("(");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(expr);
                    }
                    if (!expr.get(loc).equalsIgnoreCase("(")) {
                        ArrayList<String> test = new ArrayList<String>();
                        test.add(expr.get(loc));
                        this.runAnt(test);
                        loc++;
                    } else {
                        // if there is a bracket expression
                        ArrayList<String> test = new ArrayList<String>();
                        int d = 0;
                        while (true) {
                            if (expr.get(loc).equalsIgnoreCase("(")) {
                                d++;
                            }
                            if (expr.get(loc).equalsIgnoreCase(")")) {
                                d--;
                            }
                            test.add(expr.get(loc));
                            if (d == 0) {
                                break;
                            }
                            loc++;
                        }
                        this.runAnt(test);
                        loc++;
                    }
                }               
            } else if(expr.get(0).equalsIgnoreCase("PROGN2")) {
                int loc = 1;
                // first expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    this.runAnt(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    int d = 0;
                    while(true) {
                        if(expr.get(loc).equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(expr.get(loc).equalsIgnoreCase(")")) {
                            d--;
                        }
                        test.add(expr.get(loc));
                        if(d==0) {
                            break;
                        }                        
                        loc++;
                    }
                    this.runAnt(test);
                    loc++;
                }
                // second expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    this.runAnt(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    int d = 0;
                    while(true) {
                        if(expr.get(loc).equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(expr.get(loc).equalsIgnoreCase(")")) {
                            d--;
                        }
                        test.add(expr.get(loc));
                        if(d==0) {
                            break;
                        }                        
                        loc++;
                    }
                    this.runAnt(test);
                    loc++;
                }               
            } else if(expr.get(0).equalsIgnoreCase("PROGN3")) {
                int loc = 1;
                // first expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    this.runAnt(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    int d = 0;
                    while(true) {
                        if(expr.get(loc).equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(expr.get(loc).equalsIgnoreCase(")")) {
                            d--;
                        }
                        test.add(expr.get(loc));
                        if(d==0) {
                            break;
                        }                        
                        loc++;
                    }                    
                    this.runAnt(test);
                    loc++;
                }
                // second expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    this.runAnt(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    int d = 0;
                    while(true) {
                        if(expr.get(loc).equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(expr.get(loc).equalsIgnoreCase(")")) {
                            d--;
                        }
                        test.add(expr.get(loc));
                        if(d==0) {
                            break;
                        }                        
                        loc++;
                    }
                    this.runAnt(test);
                    loc++;
                }
                // third expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    this.runAnt(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    int d = 0;
                    while(true) {
                        if(expr.get(loc).equalsIgnoreCase("(")) {
                            d++;
                        }
                        if(expr.get(loc).equalsIgnoreCase(")")) {
                            d--;
                        }
                        test.add(expr.get(loc));
                        if(d==0) {
                            break;
                        }                        
                        loc++;
                    }
                    this.runAnt(test);
                    loc++;
                }           
            } else {
                System.out.println("SYNTAX FUNCTION EXCEPTION --- resolution error");
            }
        }
    }
    
    /**
     * Returns an ArrayList<String> representing a top scoring program
     * @return The top scoring program
     */
    public ArrayList<String> getBestProgram() {
        ArrayList<String> prog = new ArrayList<String>();
        
        // best program for this is actaully a vector of positions covering food
        // locations
        for(String f: foodLocations) {
            prog.add(f);
        }
        
        return prog;
    }
    
}
