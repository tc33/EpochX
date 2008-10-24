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

package modelsupport.mux37bitcode;

import java.util.ArrayList;
import core.*;

/**
 * The Scorer37bit class provides functionality to compare boolean programs scores to the ideal solution
 * 
 * 
 * @author Lawrence Beadle
 */
public class Scorer37bit implements core.Scorer {   
    
    private boolean a0, a1, a2, a3, a4, result;
    private boolean d0, d1, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15;
    private boolean d16, d17, d18, d19, d20, d21, d22, d23, d24, d25, d26, d27, d28, d29;
    private boolean d30, d31;
    private boolean[] aa;
    private int sType;
    private SemanticModule semMod;
    
    /**
     * Sets the type of scoring to be completed
     * @param type 1 for input-output, 2 for semantic
     * @param semModX the semantic module associated with this model
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

        double score = 10000000;
        BDDScoreHelper BDDSH = new BDDScoreHelper();
        score = BDDSH.doScore(program, this.getBestProgram(), semMod);
        return score;

    }

    /**
     * Calculates the expected score
     * @param alpha The input state
     * @return the return value at this input state
     */
    public boolean doExpectedScoreCalc(String alpha) {
        aa = BoolTrans.doTrans(alpha);
        // assign bit booleans
        a0 = aa[0];
        a1 = aa[1];
        a2 = aa[2];
        a3 = aa[3];
        a4 = aa[4];
        d0 = aa[5];
        d1 = aa[6];
        d2 = aa[7];
        d3 = aa[8];
        d4 = aa[9];
        d5 = aa[10];
        d6 = aa[11];
        d7 = aa[12];
        d8 = aa[13];
        d9 = aa[14];
        d10 = aa[15];
        d11 = aa[16];
        d12 = aa[17];
        d13 = aa[18];
        d14 = aa[19];
        d15 = aa[20];
        d16 = aa[21];
        d17 = aa[22];
        d18 = aa[23];
        d19 = aa[24];
        d20 = aa[25];
        d21 = aa[26];
        d22 = aa[27];
        d23 = aa[28];
        d24 = aa[29];
        d25 = aa[30];
        d26 = aa[31];
        d27 = aa[32];
        d28 = aa[33];
        d29 = aa[34];
        d30 = aa[35];
        d31 = aa[36];
        
        return choseResult();
    }
    
    private boolean choseResult() {
        // scoring solution
        String locator = "";
        if(a0==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(a1==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(a2==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(a3==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(a4==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        
        int location = (31 - Integer.parseInt(locator, 2)) + 5;
        
        result = aa[location];
        
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
        a0 = aa[0];
        a1 = aa[1];
        a2 = aa[2];
        a3 = aa[3];
        a4 = aa[4];
        d0 = aa[5];
        d1 = aa[6];
        d2 = aa[7];
        d3 = aa[8];
        d4 = aa[9];
        d5 = aa[10];
        d6 = aa[11];
        d7 = aa[12];
        d8 = aa[13];
        d9 = aa[14];
        d10 = aa[15];
        d11 = aa[16];
        d12 = aa[17];
        d13 = aa[18];
        d14 = aa[19];
        d15 = aa[20];
        d16 = aa[21];
        d17 = aa[22];
        d18 = aa[23];
        d19 = aa[24];
        d20 = aa[25];
        d21 = aa[26];
        d22 = aa[27];
        d23 = aa[28];
        d24 = aa[29];
        d25 = aa[30];
        d26 = aa[31];
        d27 = aa[32];
        d28 = aa[33];
        d29 = aa[34];
        d30 = aa[35];
        d31 = aa[36];
        
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
            if(expr.get(0).equalsIgnoreCase("A0")) {
                return a0;
            } else if(expr.get(0).equalsIgnoreCase("A1")) {
                return a1;
            } else if(expr.get(0).equalsIgnoreCase("A2")) {
                return a2;
            } else if(expr.get(0).equalsIgnoreCase("A3")) {
                return a3;
            } else if(expr.get(0).equalsIgnoreCase("A4")) {
                return a4;
            } else if(expr.get(0).equalsIgnoreCase("D0")) {
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
            } else if(expr.get(0).equalsIgnoreCase("D7")) {
                return d7;
            } else if(expr.get(0).equalsIgnoreCase("D8")) {
                return d8;
            } else if(expr.get(0).equalsIgnoreCase("D9")) {
                return d9;
            } else if(expr.get(0).equalsIgnoreCase("D10")) {
                return d10;
            } else if(expr.get(0).equalsIgnoreCase("D11")) {
                return d11;
            } else if(expr.get(0).equalsIgnoreCase("D12")) {
                return d12;
            } else if(expr.get(0).equalsIgnoreCase("D13")) {
                return d13;
            } else if(expr.get(0).equalsIgnoreCase("D14")) {
                return d14;
            } else if(expr.get(0).equalsIgnoreCase("D15")) {
                return d15;
            } else if(expr.get(0).equalsIgnoreCase("D16")) {
                return d16;
            } else if(expr.get(0).equalsIgnoreCase("D17")) {
                return d17;
            } else if(expr.get(0).equalsIgnoreCase("D18")) {
                return d18;
            } else if(expr.get(0).equalsIgnoreCase("D19")) {
                return d19;
            } else if(expr.get(0).equalsIgnoreCase("D20")) {
                return d20;
            } else if(expr.get(0).equalsIgnoreCase("D21")) {
                return d21;
            } else if(expr.get(0).equalsIgnoreCase("D22")) {
                return d22;
            } else if(expr.get(0).equalsIgnoreCase("D23")) {
                return d23;
            } else if(expr.get(0).equalsIgnoreCase("D24")) {
                return d24;
            } else if(expr.get(0).equalsIgnoreCase("D25")) {
                return d25;
            } else if(expr.get(0).equalsIgnoreCase("D26")) {
                return d26;
            } else if(expr.get(0).equalsIgnoreCase("D27")) {
                return d27;
            } else if(expr.get(0).equalsIgnoreCase("D28")) {
                return d28;
            } else if(expr.get(0).equalsIgnoreCase("D29")) {
                return d29;
            } else if(expr.get(0).equalsIgnoreCase("D30")) {
                return d30;
            } else if(expr.get(0).equalsIgnoreCase("D31")) {
                return d31;
            } else {
                System.out.println("Resolution ERROR - INVALID EXPR DETECTED - " + expr);
                return false;
            }
        } else {
            if(expr.get(0).equalsIgnoreCase("(")) {
                // bracket scenario - remove each end and resolve expr on inside of bracket
                ArrayList<String> test = new ArrayList();
                int top = expr.size()-1;
                for(int i = 1; i<top; i++) {
                    test.add(expr.get(i));
                }
                return resolveExpr(test);
            } else if(expr.get(0).equalsIgnoreCase("NOT")) {
                // NOT scenario                
                if(expr.size()==2) {
                    // if there is a terminal
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(1));
                    if(resolveExpr(test)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var1 = resolveExpr(test);
                    //System.out.println("VAR 1 = " + var1);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var2 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
                    ArrayList<String> test = new ArrayList();
                    test.add(expr.get(loc));
                    var3 = resolveExpr(test);
                    loc++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> test = new ArrayList();
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
     * Returns an ArrayList<String> representing a top scoring program
     * @return The top scoring program
     */
    public ArrayList<String> getBestProgram() {
        ArrayList<String> prog = new ArrayList<String>();
        
        prog.add("IF");
        prog.add("A0");        
        prog.add("(");
            prog.add("IF");
            prog.add("A1");
            prog.add("(");
                prog.add("IF");
                prog.add("A2");        
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D0");
                        prog.add("D1");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D2");
                        prog.add("D3");
                    prog.add(")");
                prog.add(")");        
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D4");
                        prog.add("D5");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D6");
                        prog.add("D7");
                    prog.add(")");
                prog.add(")");
            prog.add(")");
            prog.add("(");
                prog.add("IF");
                prog.add("A2");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D8");
                        prog.add("D9");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D10");
                        prog.add("D11");
                    prog.add(")");
                prog.add(")");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D12");
                        prog.add("D13");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D14");
                        prog.add("D15");
                    prog.add(")");
                prog.add(")");
            prog.add(")");
        prog.add(")");        
        prog.add("(");
            prog.add("IF");
            prog.add("A1");        
            prog.add("(");
                prog.add("IF");
                prog.add("A2");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D16");
                        prog.add("D17");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D18");
                        prog.add("D19");
                    prog.add(")");
                prog.add(")");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D20");
                        prog.add("D21");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D22");
                        prog.add("D23");
                    prog.add(")");
                prog.add(")");
            prog.add(")");        
            prog.add("(");
                prog.add("IF");
                prog.add("A2");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D24");
                        prog.add("D25");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D26");
                        prog.add("D27");
                    prog.add(")");
                prog.add(")");
                prog.add("(");
                    prog.add("IF");
                    prog.add("A3");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D28");
                        prog.add("D29");
                    prog.add(")");
                    prog.add("(");
                        prog.add("IF");
                        prog.add("A4");
                        prog.add("D30");
                        prog.add("D31");
                    prog.add(")");
                prog.add(")");
            prog.add(")");
        prog.add(")");     
        
        return prog;
    }
    
}
