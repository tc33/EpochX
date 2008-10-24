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

package modelsupport.even7parity;

import java.util.ArrayList;
import core.*;

/**
 * The Scorer7Par class provides functionality to compare boolean programs scores to the ideal solution
 * @author Lawrence Beadle
 */
public class Scorer7Par implements core.Scorer {   
    
    private boolean d0, d1, d2, d3, d4, d5, d6, result;
    private boolean[] aa;
    private int sType;
    private SemanticModule semMod;
    
    /**
     * Sets the scoring module to be used
     * @param type 1 for input-output, 2 for semantic
     * @param semModX The semantic module to be used
     */
    public void setScoreMethodType(int type, SemanticModule semModX) {
        sType = type;
        semMod = semModX;
    }
    
    /**
     * Calculates the score for a candidate program
     * @param input The input state
     * @param program The candidate program
     * @return The score the candidate program produce with the defined input state
     */
    public double getScore(ArrayList<String> input, ArrayList<String> program) {
        
        double score = 0;
        if (sType == 1) {
            for (String run : input) {
                // decide whether to increment score
                if (this.doActualScoreCalc(run, program) == this.doExpectedScoreCalc(run)) {
                    score++;
                }
            }
            return 128 - score;
        } else {
            BDDScoreHelper BDDSH = new BDDScoreHelper();
            score = BDDSH.doScore(program, this.getBestProgram(), semMod);
            return score;
        }
    }
    
    /**
     * Calculates the expected score
     * @param alpha The input state
     * @return the return value at this input state
     */
    public boolean doExpectedScoreCalc(String alpha) {
        aa = BoolTrans.doTrans(alpha);
        // assign bit booleans
        d0 = aa[0];
        d1 = aa[1];
        d2 = aa[2];
        d3 = aa[3];
        d4 = aa[4];
        d5 = aa[5];
        d6 = aa[6];
        
        return choseResult();
    }
    
    private boolean choseResult() {
        // scoring solution
        int eCount = 0;
        for(int i = 0; i<7; i++) {
            if(aa[i]==true) {
                eCount++;
            }
        }
        if(eCount%2==0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    

    
    /**
     * Calculates the actual score using the candidate program
     * @param alpha The input state
     * @param program The candidate program
     * @return The boolean value at the input state
     */
    public boolean doActualScoreCalc(String alpha, ArrayList<String> program) {
        aa = BoolTrans.doTrans(alpha);
        // assign bit booleans
        d0 = aa[0];
        d1 = aa[1];
        d2 = aa[2];
        d3 = aa[3];
        d4 = aa[4];
        d5 = aa[5];
        d6 = aa[6];
        
        return resolveExpr(program);
    }
    
    /**
     * Resolves the programs
     * @param expr An ArrayList<String> representation of a candidate boolean program
     * @return A boolean representing the output of the multiplexer
     */
    public boolean resolveExpr(ArrayList<String> expr) {
        
        boolean var1, var2, var3;
        
        int eSize = expr.size();
        
        if(eSize<1) {
            System.out.println("Resolution ERROR - invalid expression - " + expr);
            return false;
        } else if(eSize==1) {
            if(expr.get(0).equalsIgnoreCase("D0")) {
                return d0;
            } else if(expr.get(0).equalsIgnoreCase("D1")) {
                return d1;
            } else if(expr.get(0).equalsIgnoreCase("D2")) {
                return d2;
            } else if(expr.get(0).equalsIgnoreCase("D3")) {
                return d3;
            } else if(expr.get(0).equalsIgnoreCase("D4")) {
                return d4;
            } else if(expr.get(0).equalsIgnoreCase("D5")) {
                return d5;
            } else if(expr.get(0).equalsIgnoreCase("D6")) {
                return d6;
            } else {
                System.out.println("Resolution ERROR - INVALID EXPR DETECTED - " + expr);
                return false;
            }
        } else {
            if(expr.get(0).equalsIgnoreCase("(")) {
                // bracket scenario - remove each end and resolve expr on inside of bracket
                ArrayList<String> test = new ArrayList<String>();
                int top = expr.size()-1;
                for(int i = 1; i<top; i++) {
                    test.add(expr.get(i));
                }
                return resolveExpr(test);
            } else if(expr.get(0).equalsIgnoreCase("NOT")) {
                // NOT scenario                
                if(expr.size()==2) {
                    // if there is a terminal
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(1));
                    if(resolveExpr(test)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList<String>();
                    for(String e: expr) {
                        test.add(e);
                    }
                    test.remove(0);
                    if(resolveExpr(test)) {
                        return false;
                    } else {
                        return true;
                    }
                }               
            } else if(expr.get(0).equalsIgnoreCase("AND")) {
                int loc = 1;
                // first expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    // if it is terminal
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
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
                    var1 = resolveExpr(test);
                    loc++;
                }
                // second expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
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
                    var2 = resolveExpr(test);
                    loc++;
                }
                // resolve AND
                if(var1 && var2) {
                    return true;
                } else {
                    return false;
                }                
            } else if(expr.get(0).equalsIgnoreCase("OR")) {
                int loc = 1;
                // first expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
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
                    var1 = resolveExpr(test);
                    loc++;
                }
                // second expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
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
                    var2 = resolveExpr(test);
                    loc++;
                }
                // resolve OR
                if(var1 || var2) {
                    return true;
                } else {
                    return false;
                }                
            } else if(expr.get(0).equalsIgnoreCase("IF")) {
                int loc = 1;
                // first expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
                    //System.out.println("VAR 1 = " + var1);
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
                    var1 = resolveExpr(test);
                    loc++;
                }
                // second expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
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
                    var2 = resolveExpr(test);
                    loc++;
                }
                // third expr
                if(!expr.get(loc).equalsIgnoreCase("(")) {
                    ArrayList<String> test = new ArrayList<String>();
                    test.add(expr.get(loc));
                    var3 = resolveExpr(test);
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
                    var3 = resolveExpr(test);
                    loc++;
                }
                // resolve IF
                if(var1) {
                    return var2;
                } else {
                    return var3;
                }            
            } else {
                System.out.println("SYNTAX EXCEPTION --- resolution error");
                return false;
            }
        }
    }
    
    /**
     * Returns an ArrayList<String> representing a top scoring program THIS IS NOT IMPLEMENTED
     * @return The top scoring program
     */
    public ArrayList<String> getBestProgram() {
        ArrayList<String> prog = new ArrayList<String>();
        
        // fill this in TO DO at some point
        
        
        return prog;
    }
    
}
