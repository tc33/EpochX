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

package Majority9;

import net.sf.javabdd.*;
import java.util.*;
import Core.*;

/**
 * A SemanticModule specifi to the 11 bit multiplexer problem
 * @author Lawrence Beadle
 */
public class GPEquivalence9maj implements Core.SemanticModule {
    
    private BDDFactory b;
    private BDD dZero, dOne, dTwo, dThree, dFour, dFive, dSix, dSeven, dEight ;    
    private int depth;
    private ArrayList<String> cProg, parts;
    private String oB = "(";
    private String cB = ")";
    private String ifF = "IF";
    private String andF = "AND";
    private String orF = "OR";
    private String notF = "NOT";
    
    /**
     * Starts the SemanticModule
     */
    public void start() {
        // set up BDD analyser
        b = BDDFactory.init("cudd", 5000, 5000);
        // create all possible base variables
        dZero = b.ithVar(1);
        dOne = b.ithVar(2);
        dTwo = b.ithVar(3);
        dThree = b.ithVar(4);
        dFour = b.ithVar(5);
        dFive = b.ithVar(6);
        dSix = b.ithVar(7);
        dSeven = b.ithVar(8);
        dEight = b.ithVar(9);
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
            if(prog.get(0).equalsIgnoreCase("D0")) {
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
                // return OR of two BDDs
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
        parts = new ArrayList<String>();
        for(int i = 0; i<p.length; i++) {
            parts.add(p[i]);
        }
        // print ArrayList for diagnostic
        //System.out.println("PROG PARTS: " + parts);
        
        // work out the top node - node that never features on RHS of links
        ArrayList<Integer> lHS = new ArrayList<Integer>();
        ArrayList<Integer> rHS = new ArrayList<Integer>();
        // load up arraylists
        for(String bit: parts) {
            if(bit.contains("TLink=") || bit.contains("FLink=")) {
                String bit2 = bit.substring(6);
                String[] bit3 = bit2.split(":");
                lHS.add(new Integer(Integer.parseInt(bit3[0])));
                rHS.add(new Integer(Integer.parseInt(bit3[1])));
            }
        }
        // print indexes for diagnostic
        //System.out.println("\nLHS = " + lHS + "\nRHS = " + rHS + "\n");
        // find the index that does not appear in the RHS
        int topIndex = 1;
        for(Integer test: lHS) {
            if(!rHS.contains(test)) {
                topIndex = test.intValue();
                break;
            }
        }
        //System.out.println("TOP INDEX IS: " + topIndex + "\n");        
        
        // construct boolean program
        cProg = new ArrayList<String>();
        cProg.add(new Integer(topIndex).toString());
        
        // call a recursive solution from here
        cProg = t_rec(topIndex, cProg);
        
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
        // build hashmap of variables to terminals
        HashMap<String, String> variableToTerminal = new HashMap<String, String>();
        variableToTerminal.put("1", "D0");
        variableToTerminal.put("2", "D1");
        variableToTerminal.put("3", "D2");
        variableToTerminal.put("4", "D3");
        variableToTerminal.put("5", "D4");
        variableToTerminal.put("6", "D5");
        variableToTerminal.put("7", "D6");
        variableToTerminal.put("8", "D7");
        variableToTerminal.put("9", "D8");
        
        // cycle through arraylist and change index to terminals
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
        
        return cProg;
    }
    
    private ArrayList<String> t_rec(int index, ArrayList<String> composedProg) {
              
        // set parent node
        int parent = index;
        int tChild = 1;
        int fChild = 0;
        
        // work out what is on the T and F links - set child 
        for(String linkHunt: parts) {
            String temp;
            if(linkHunt.contains("TLink=" + parent + ":")) {
                temp = linkHunt.replace("TLink="+parent+":", "");
                tChild = Integer.parseInt(temp);
            }
            if(linkHunt.contains("FLink=" + parent + ":")) {
                temp = linkHunt.replace("FLink="+parent+":", "");
                fChild = Integer.parseInt(temp);
            }
        }
        //System.out.println("Parent = " + parent + "\tTrue Child = " + tChild + "\tFalse Child = " + fChild + "\n");
        
        // decide what happens
        if(tChild>1 && fChild>1) {
            // if both variables then IF STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(fChild).toString());
                    cProg.add(i, new Integer(tChild).toString());
                    cProg.add(i, new Integer(parent).toString());
                    cProg.add(i, ifF);
                    cProg.add(i, oB);
                    break;
                }
            } 
        } else if(tChild>1 && fChild==0) {
            // if TLINK goes to a variable and FLINK to FALSE then AND STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(tChild).toString());
                    cProg.add(i, new Integer(parent).toString());
                    cProg.add(i, andF);
                    cProg.add(i, oB);
                    break;
                }
            }         
        } else if(tChild>1 && fChild==1) {
            // if TLINK goes to a variable and FLINK to TRUE then AND STATEMENT WITH COMPOUNDED NOT ON PARENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(tChild).toString());
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(parent).toString());
                    cProg.add(i, notF);
                    cProg.add(i, oB);
                    cProg.add(i, andF);
                    cProg.add(i, oB);
                    break;
                }
            }            
        } else if(tChild==1 && fChild>1) {
            // if TLINK goes to TRUE and FLINK to a variable then OR STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(fChild).toString());
                    cProg.add(i, new Integer(parent).toString());
                    cProg.add(i, orF);
                    cProg.add(i, oB);
                    break;
                }
            } 
        } else if(tChild==0 && fChild>1) {
            // if TLINK goes to TRUE and FLINK to a variable then OR STATEMENT WITH COMPOUNDED NOT ON PARENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(fChild).toString());
                    cProg.add(i, cB);
                    cProg.add(i, new Integer(parent).toString());
                    cProg.add(i, notF);
                    cProg.add(i, oB);
                    cProg.add(i, orF);
                    cProg.add(i, oB);
                    break;
                }
            } 
        } else if(tChild==0 && fChild==1) {
            // if FLINK goes to TRUE and TLINK to FALSE then NOT STATEMENT
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    if(i<1) {
                        cProg.remove(i);
                        cProg.add(i, cB);
                        cProg.add(i, new Integer(parent).toString());
                        cProg.add(i, notF);
                        cProg.add(i, oB);
                        break;
                    } else {
                        if(!cProg.get(i-1).equalsIgnoreCase("NOT")) {
                            cProg.remove(i);
                            cProg.add(i, cB);
                            cProg.add(i, new Integer(parent).toString());
                            cProg.add(i, notF);
                            cProg.add(i, oB);
                            break;
                        }
                    }
                }
            }
        } else if(tChild==1 && fChild==0) {
            // if FLINK goes to FALSE and TLINK to TRUE then SINGLE VARIABLE
            for(int i = 0; i<cProg.size(); i++) {
                if(cProg.get(i).equals(new Integer(parent).toString())) {
                    cProg.remove(i);
                    cProg.add(i, new Integer(parent).toString());
                    break;
                }
            }
        }
        
        // recurse for both children
        if(tChild>1) {
            cProg = t_rec(tChild, cProg);
        }
        if(fChild>1) {
            cProg = t_rec(fChild, cProg);
        }
        
        return cProg;
    }
}
