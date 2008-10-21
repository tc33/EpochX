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

package Mux20bitCode;

import net.sf.javabdd.*;
import java.util.*;
import CoreN.*;

/**
 * A SemanticModule specifi to the 11 bit multiplexer problem
 * @author Lawrence Beadle
 */
public class GPEquivalence20bit implements CoreN.SemanticModule {
    
    private BDDFactory b;
    private BDD aZero, aOne, aTwo, aThree;
    private BDD dZero, dOne, dTwo, dThree, dFour, dFive, dSix, dSeven, dEight;
    private BDD dNine, dTen, dEleven, dTwelve, dThirteen, dFourteen, dFifteen;
    private int depth;
    private ArrayList<String> cProg = new ArrayList<String>();
    private ArrayList<String> parts = new ArrayList<String>();
    private HashMap<Integer, XNode> baseInfo = new HashMap<Integer, XNode>();
    
    /**
     * Starts the SemanticModule
     */
    public void start() {
        // set up BDD analyser
        b = BDDFactory.init("cudd", 10000, 10000);
        // create all possible base variables
        aZero = b.ithVar(1);
        aOne = b.ithVar(2);
        aTwo = b.ithVar(3);
        aThree = b.ithVar(4);
        dZero = b.ithVar(5);
        dOne = b.ithVar(6);
        dTwo = b.ithVar(7);
        dThree = b.ithVar(8);
        dFour = b.ithVar(9);
        dFive = b.ithVar(10);
        dSix = b.ithVar(11);
        dSeven = b.ithVar(12);
        dEight = b.ithVar(13);
        dNine = b.ithVar(14);
        dTen = b.ithVar(15);
        dEleven = b.ithVar(16);
        dTwelve = b.ithVar(17);
        dThirteen = b.ithVar(18);
        dFourteen = b.ithVar(19);
        dFifteen = b.ithVar(20);
    }
    
    // compares two programs and return true if semantically equivalent
    /**
     * Compares two program semantically
     * @param program1 1st program
     * @param program2 2nd program
     * @return TRUE if the programs are semantically equivalent
     */
    public boolean comparePrograms(ArrayList<String> program1, ArrayList<String> program2) {
        
        BDD tmp1 = createRep(program1).getBDD();
        BDD tmp2 = createRep(program2).getBDD();
        
        return tmp1.equals(tmp2);
    }
    
    /**
     * Stoprs the SemanticModule
     */
    public void finish() {
        b.done();
    }
    
    // break down boolean program into BDD
    /**
     * Creates a semantic representation of a program
     * @param prog program to be represented semantically
     * @return The semantic representation of the program
     */
    public BehaviourRepresentation createRep(ArrayList<String> prog) {
        
        // break up prog and call respective bits recursively
        if(prog.size()==1) {
            // A TERMINAL
            if(prog.get(0).equalsIgnoreCase("A0")) {
                return new BehaviourRepresentation(aZero);
            } else if(prog.get(0).equalsIgnoreCase("A1")) {
                return new BehaviourRepresentation(aOne);
            } else if(prog.get(0).equalsIgnoreCase("A2")) {
                return new BehaviourRepresentation(aTwo);
            } else if(prog.get(0).equalsIgnoreCase("A3")) {
                return new BehaviourRepresentation(aThree);
            } else if(prog.get(0).equalsIgnoreCase("D0")) {
                return new BehaviourRepresentation(dZero);
            } else if(prog.get(0).equalsIgnoreCase("D1")) {
                return new BehaviourRepresentation(dOne);
            } else if(prog.get(0).equalsIgnoreCase("D2")) {
                return new BehaviourRepresentation(dTwo);
            } else if(prog.get(0).equalsIgnoreCase("D3")) {
                return new BehaviourRepresentation(dThree);
            } else if(prog.get(0).equalsIgnoreCase("D4")) {
                return new BehaviourRepresentation(dFour);
            } else if(prog.get(0).equalsIgnoreCase("D5")) {
                return new BehaviourRepresentation(dFive);
            } else if(prog.get(0).equalsIgnoreCase("D6")) {
                return new BehaviourRepresentation(dSix);
            } else if(prog.get(0).equalsIgnoreCase("D7")) {
                return new BehaviourRepresentation(dSeven);
            } else if(prog.get(0).equalsIgnoreCase("D8")) {
                return new BehaviourRepresentation(dEight);
            } else if(prog.get(0).equalsIgnoreCase("D9")) {
                return new BehaviourRepresentation(dNine);
            } else if(prog.get(0).equalsIgnoreCase("D10")) {
                return new BehaviourRepresentation(dTen);
            } else if(prog.get(0).equalsIgnoreCase("D11")) {
                return new BehaviourRepresentation(dEleven);
            } else if(prog.get(0).equalsIgnoreCase("D12")) {
                return new BehaviourRepresentation(dTwelve);
            } else if(prog.get(0).equalsIgnoreCase("D13")) {
                return new BehaviourRepresentation(dThirteen);
            } else if(prog.get(0).equalsIgnoreCase("D14")) {
                return new BehaviourRepresentation(dFourteen);
            } else if(prog.get(0).equalsIgnoreCase("D15")) {
                return new BehaviourRepresentation(dFifteen);
            }
        } else {
            // A FUNCTION            
            if(prog.get(0).equalsIgnoreCase("(")) {
                // BRACKETS
                // wind through to closing bracket monitoring depth
                // submit code for back to createRep
                depth = 1;
                int i = 1;
                ArrayList<String> temp = new ArrayList<String>();
                while(true) {
                    if(prog.get(i).equalsIgnoreCase("(")) {
                        depth++;
                    }
                    if(prog.get(i).equalsIgnoreCase(")")) {
                        depth--;
                    }
                    if(depth==0) {
                        break;
                    }
                    temp.add(prog.get(i));
                    i++;
                }
                return createRep(temp);
            } else if(prog.get(0).equalsIgnoreCase("NOT")) {
                // NOT
                // knock the NOT off the front and submit the rest to createRep()
                ArrayList<String> temp = new ArrayList<String>();
                for(int i = 1; i<prog.size(); i++) {
                    temp.add(prog.get(i));
                }
                BDD tmp1 = createRep(temp).getBDD();
                BDD t = tmp1.not();
                return new BehaviourRepresentation(t);
            } else if(prog.get(0).equalsIgnoreCase("AND")) {
                // AND
                // first expr
                int i = 1;
                BDD tmp1, tmp2;
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    // if it is terminal
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp1 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp1 = createRep(temp).getBDD();
                    i++;
                }
                // second expr
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp2 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp2 = createRep(temp).getBDD();
                    i++;
                }
                // return AND of two BDDs
                BDD t = tmp1.and(tmp2);
                return new BehaviourRepresentation(t);
            } else if(prog.get(0).equalsIgnoreCase("OR")) {
                // OR
                // first expr
                int i = 1;
                BDD tmp1, tmp2;
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    // if it is terminal
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp1 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp1 = createRep(temp).getBDD();
                    i++;
                }
                // second expr
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp2 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp2 = createRep(temp).getBDD();
                    i++;
                }
                // return AND of two BDDs
                BDD t = tmp1.or(tmp2);
                return new BehaviourRepresentation(t);
            } else if(prog.get(0).equalsIgnoreCase("IF")) {
                // IF
                int i = 1;
                BDD tmp1, tmp2, tmp3;
                // first expr
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp1 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp1 = createRep(temp).getBDD();
                    i++;
                }
                // second expr
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp2 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp2 = createRep(temp).getBDD();
                    i++;
                }
                // third expr
                if(!prog.get(i).equalsIgnoreCase("(")) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(prog.get(i));
                    tmp3 = createRep(temp).getBDD();
                    i++;
                } else {
                    // if there is a bracket expression
                    ArrayList<String> temp = new ArrayList<String>();
                    depth = 0;
                    while(true) {
                        if(prog.get(i).equalsIgnoreCase("(")) {
                            depth++;
                        }
                        if(prog.get(i).equalsIgnoreCase(")")) {
                            depth--;
                        }
                        temp.add(prog.get(i));
                        if(depth==0) {
                            break;
                        }
                        i++;
                    }
                    tmp3 = createRep(temp).getBDD();
                    i++;
                }
                // resolve IF
                BDD t = tmp1.ite(tmp2, tmp3);
                return new BehaviourRepresentation(t);
            }
        }
        System.err.println("BDD Resolution error - Unidentified Syntax Present");
        return null;
    }  
    
    /**
     * Translates a semantic representation of a program back to code form
     * @param bddToTranslate The sematic representation to translate
     * @return Code form of semantic representation
     */
    public ArrayList<String> repToCode(BehaviourRepresentation bddToTranslate) {
        
        // convert BDD back to a string
        String base = bddToTranslate.getBDD().dotToString();
        String[] p = base.split(" aa ");
        // shove into an ArrayList for processing
        for(int i = 0; i<p.length; i++) {
            parts.add(p[i]);
        }
        // print ArrayList for diagnostic
        // System.out.println("PROG PARTS: " + parts);
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 2 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // build hash map ------------------------------------------------------
        for(String bit: parts) {
            // sort out hash map
            if(bit.contains("Variable")) {
                String[] bits = bit.split(" ");
                String fBit = bits[0].substring(6);
                int id = Integer.parseInt(fBit);
                baseInfo.put(id, new XNode(id));
            }
        }
        for(String bit: parts) {
            if(bit.contains("TLink=") || bit.contains("FLink=")) {
                String bit2 = bit.substring(6);
                String[] bit3 = bit2.split(":");
                int pIndex = Integer.parseInt(bit3[0]);
                int cIndex = Integer.parseInt(bit3[1]);
                if(bit.contains("TLink")) {
                    baseInfo.get(new Integer(pIndex)).setTCID(cIndex);
                } else {
                    baseInfo.get(new Integer(pIndex)).setFCID(cIndex);
                }
            }
        }
        // ---------------------------------------------------------------------
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 3 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // work out the top node - node that never features on RHS of links ----
        ArrayList<Integer> lHS = new ArrayList<Integer>();
        ArrayList<Integer> rHS = new ArrayList<Integer>();
        // load up arraylists
        for(String bit: parts) {
            if(bit.contains("TLink=") || bit.contains("FLink=")) {
                String bit2 = bit.substring(6);
                String[] bit3 = bit2.split(":");
                int lIndex = Integer.parseInt(bit3[0]);
                int rIndex = Integer.parseInt(bit3[1]);
                lHS.add(new Integer(lIndex));
                rHS.add(new Integer(rIndex));                
            }
        }
        // print indexes for diagnostic
        // System.out.println("\nLHS = " + lHS + "\nRHS = " + rHS + "\n");
        // find the index that does not appear in the RHS
        int topIndex = 1;
        for(Integer test: lHS) {
            if(!rHS.contains(test)) {
                topIndex = test.intValue();
                break;
            }
        }
        // System.out.println("TOP INDEX IS: " + topIndex + "\n");
        // ---------------------------------------------------------------------
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 4 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // construct boolean program
        cProg.add(new Integer(topIndex).toString());
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 5 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // call a recursive solution from here
        t_rec(topIndex);
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 6 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // cycle through and translate indexes into variable names
        // build HasMap of indexes to variable names
        HashMap<String, String> indexToVariable = new HashMap<String, String>();
        for(String bit: parts) {
            if(bit.contains("Variable")) {
                String[] t = bit.split(" ");
                String ind = t[0].substring(6);
                String var = t[1].substring(9);
                indexToVariable.put(ind, var);
            }
        }
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 7 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // build hashmap of variables to terminals
        HashMap<String, String> variableToTerminal = new HashMap<String, String>();
        variableToTerminal.put("1", "A0");
        variableToTerminal.put("2", "A1");
        variableToTerminal.put("3", "A2");
        variableToTerminal.put("4", "A3");
        variableToTerminal.put("5", "D0");
        variableToTerminal.put("6", "D1");
        variableToTerminal.put("7", "D2");
        variableToTerminal.put("8", "D3");
        variableToTerminal.put("9", "D4");
        variableToTerminal.put("10", "D5");
        variableToTerminal.put("11", "D6");
        variableToTerminal.put("12", "D7");
        variableToTerminal.put("13", "D8");
        variableToTerminal.put("14", "D9");
        variableToTerminal.put("15", "D10");
        variableToTerminal.put("16", "D11");
        variableToTerminal.put("17", "D12");
        variableToTerminal.put("18", "D13");
        variableToTerminal.put("19", "D14");
        variableToTerminal.put("20", "D15");
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 8 =\t" + (cTime-pTime));
        //pTime = cTime;
        
        // cycle through arraylist and change index to terminals + add to final arraylist
        for(int i = 0; i<cProg.size(); i++) {
            try {
                int index = Integer.parseInt(cProg.get(i));
                String var = indexToVariable.get(cProg.get(i));
                String term = variableToTerminal.get(var);
                cProg.set(i, term);
            } catch(NumberFormatException e) {
                // do nothing
            }
        }
        
        //cTime = System.currentTimeMillis();
        //System.out.println("Timer Point 9 =\t" + (cTime-pTime));
        
        //System.out.println(cProg);
        
        return cProg;
    }
    
    private void t_rec(int index) {
    
    // set parent node
        int parent = index;
        int tChild = 1;
        int fChild = 0;
        
        XNode node = baseInfo.get(new Integer(index));
        tChild = node.getTCID();
        fChild = node.getFCID();
        //System.out.println("Parent = " + parent + "\tTrue Child = " + tChild + "\tFalse Child = " + fChild + "\n");
        
        String sParent = new Integer(parent).toString();
        String sTChild = new Integer(tChild).toString();
        String sFChild = new Integer(fChild).toString();
        
        // decide what happens
        if(tChild>1 && fChild>1) {
            // if both variables then IF STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, ")");
                    cProg.add(i, sFChild);
                    cProg.add(i, sTChild);
                    cProg.add(i, sParent);
                    cProg.add(i, "IF");
                    cProg.add(i, "(");
                    break;
                }
            } 
        } else if(tChild>1 && fChild==0) {
            // if TLINK goes to a variable and FLINK to FALSE then AND STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, ")");
                    cProg.add(i, sTChild);
                    cProg.add(i, sParent);
                    cProg.add(i, "AND");
                    cProg.add(i, "(");
                    break;
                }
            }         
        } else if(tChild>1 && fChild==1) {
            // if TLINK goes to a variable and FLINK to TRUE then AND STATEMENT WITH COMPOUNDED NOT ON PARENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, ")");
                    cProg.add(i, sTChild);
                    cProg.add(i, ")");
                    cProg.add(i, sParent);
                    cProg.add(i, "NOT");
                    cProg.add(i, "(");
                    cProg.add(i, "AND");
                    cProg.add(i, "(");
                    break;
                }
            }            
        } else if(tChild==1 && fChild>1) {
            // if TLINK goes to TRUE and FLINK to a variable then OR STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, ")");
                    cProg.add(i, sFChild);
                    cProg.add(i, sParent);
                    cProg.add(i, "OR");
                    cProg.add(i, "(");
                    break;
                }
            } 
        } else if(tChild==0 && fChild>1) {
            // if TLINK goes to TRUE and FLINK to a variable then OR STATEMENT WITH COMPOUNDED NOT ON PARENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, ")");
                    cProg.add(i, sFChild);
                    cProg.add(i, ")");
                    cProg.add(i, sParent);
                    cProg.add(i, "NOT");
                    cProg.add(i, "(");
                    cProg.add(i, "OR");
                    cProg.add(i, "(");
                    break;
                }
            } 
        } else if(tChild==0 && fChild==1) {
            // if FLINK goes to TRUE and TLINK to FALSE then NOT STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    if(i<1) {
                        cProg.remove(i);
                        cProg.add(i, ")");
                        cProg.add(i, sParent);
                        cProg.add(i, "NOT");
                        cProg.add(i, "(");
                        break;
                    } else {
                        if(!cProg.get(i-1).equalsIgnoreCase("NOT")) {
                            cProg.remove(i);
                            cProg.add(i, ")");
                            cProg.add(i, sParent);
                            cProg.add(i, "NOT");
                            cProg.add(i, "(");
                            break;
                        }
                    }
                }
            }
        } else if(tChild==1 && fChild==0) {
            // if FLINK goes to FALSE and TLINK to TRUE then SINGLE VARIABLE
            for(int i = 0; i<cProg.size(); i++) {
                if(sParent.equals(cProg.get(i))) {
                    cProg.remove(i);
                    cProg.add(i, sParent);
                    break;
                }
            }
        }
        
        // recurse for both children
        if(tChild>1) {
            t_rec(tChild);
        }
        if(fChild>1) {
            t_rec(fChild);
        }
    }
}
