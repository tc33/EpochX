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
 * The SemanticModule specific to the Artificial Ant Package
 * @author Lawrence Beadle
 */
public class GPEquivalenceAA implements core.SemanticModule {
    
    private Ant myAnt;
    private ArrayList<String> antModel;
    private String oB = "(";
    private String cB = ")";
    private String iFA = "IF-FOOD-AHEAD";
    private String p2 = "PROGN2";
    private String p3 = "PROGN3";
    private String m = "MOVE";
    private String tl = "TURN-LEFT";
    private String tr = "TURN-RIGHT";
    private String skip = "SKIP";        
    
    /**
     * Starts the Semantic Module
     */
    public void start() {
        // do nothing
        // not used in ant model
    }
   
    /**
     * Compares two programs semantically
     * @param program1 1st program to compare
     * @param program2 2nd program to compare
     * @return TRUE if the programs are semantically equivalent
     */
    public boolean comparePrograms(ArrayList<String> program1, ArrayList<String> program2) {

        ArrayList<String> mRep1 = createRep(program1).getArrayList();
        ArrayList<String> mRep2 = createRep(program2).getArrayList();

        return mRep1.equals(mRep2);
    }

    /**
     * Stops the SemanticModule
     */
    public void finish() {
    // do nothing
    // not used in ant model
    }

    /**
     * Creates a semantic representation of a program
     * @param prog The program to be represented semantically
     * @return A semantic representation of the program
     */
    public BehaviourRepresentation createRep(ArrayList<String> prog) {

        // develop ant monitoring model
        antModel = new ArrayList<String>();

        // initialise a new ant
        myAnt = new Ant(600);
        this.runAnt(prog);
        
        // work out depth of if statements
        int depth = 0;
        int maxDepth = 0;
        for(String s: antModel) {
            if(s.equalsIgnoreCase("{")) {
                depth++;
            }
            if(s.equalsIgnoreCase("}")) {
                depth--;
            }
            if(depth>maxDepth) {
                maxDepth = depth;
            }
        }
        
        for (int i = 0; i < (maxDepth + 1); i++) {
            if (antModel.size() > 0) {
                antModel = GenPop.condenseAntRep(antModel);
            }
        }
//        if(antModel.size()>0) {
//            antModel = GenPop.condenseAntRep(antModel);
//        }
//        if(antModel.size()>0) {
//            antModel = GenPop.condenseAntRep(antModel);
//        }
//        if(antModel.size()>0) {
//            antModel = GenPop.condenseAntRep(antModel);
//        }
//        if(antModel.size()>0) {
//            antModel = GenPop.condenseAntRep(antModel);
//        }

        return new BehaviourRepresentation(antModel);
    }

    /**
     * Trasnlates a BehaviourRepresentation representation into syntax
     * @param thisRep The representation to be translated
     * @return The code of the program
     */
    public ArrayList<String> repToCode(BehaviourRepresentation thisRep) {
        return this.repToCode1(thisRep, "E");
    }

    private ArrayList<String> repToCode1(BehaviourRepresentation thisRep, String lastO) {
        ArrayList<String> representation = thisRep.getArrayList();
        ArrayList<String> sequence = new ArrayList<String>();

        // create a linear move list
        String oBeforeIf = "E";
        String lastOrientation = lastO;
        String instruction;
        for (int i = 0; i < representation.size(); i++) {
            instruction = representation.get(i);
            // SCENARIOS
            // interpret instruction
            if (instruction.equals("M")) {
                sequence.add(m);
            } else if (instruction.equals("E")) {
                if (lastOrientation.equalsIgnoreCase("N")) {
                    sequence.add(tr);
                }
                if (lastOrientation.equalsIgnoreCase("W")) {
                    if (Math.random() < 0.5) {
                        sequence.add(tr);
                        sequence.add(tr);
                    } else {
                        sequence.add(tl);
                        sequence.add(tl);
                    }
                }
                if (lastOrientation.equalsIgnoreCase("S")) {
                    sequence.add(tl);
                }
                lastOrientation = new String(instruction);
            } else if (instruction.equals("S")) {
                if (lastOrientation.equalsIgnoreCase("E")) {
                    sequence.add(tr);
                }
                if (lastOrientation.equalsIgnoreCase("N")) {
                    if (Math.random() < 0.5) {
                        sequence.add(tr);
                        sequence.add(tr);
                    } else {
                        sequence.add(tl);
                        sequence.add(tl);
                    }
                }
                if (lastOrientation.equalsIgnoreCase("W")) {
                    sequence.add(tl);
                }
                lastOrientation = new String(instruction);
            } else if (instruction.equals("W")) {
                if (lastOrientation.equalsIgnoreCase("S")) {
                    sequence.add(tr);
                }
                if (lastOrientation.equalsIgnoreCase("E")) {
                    if (Math.random() < 0.5) {
                        sequence.add(tr);
                        sequence.add(tr);
                    } else {
                        sequence.add(tl);
                        sequence.add(tl);
                    }
                }
                if (lastOrientation.equalsIgnoreCase("N")) {
                    sequence.add(tl);
                }
                lastOrientation = new String(instruction);
            } else if (instruction.equals("N")) {
                if (lastOrientation.equalsIgnoreCase("W")) {
                    sequence.add(tr);
                }
                if (lastOrientation.equalsIgnoreCase("S")) {
                    if (Math.random() < 0.5) {
                        sequence.add(tr);
                        sequence.add(tr);
                    } else {
                        sequence.add(tl);
                        sequence.add(tl);
                    }
                }
                if (lastOrientation.equalsIgnoreCase("E")) {
                    sequence.add(tl);
                }
                lastOrientation = new String(instruction);
            } else if (instruction.equalsIgnoreCase("{")) {
                // save entry position
                oBeforeIf = lastOrientation;
                // IF-FOOD-AHEAD recursive call
                int depth = 1;
                ArrayList<String> part = new ArrayList<String>();
                sequence.add(oB);
                sequence.add(iFA);
                // pull out first section of if and submit to recursive call
                while (i < representation.size()) {
                    i++;
                    if (representation.get(i).equalsIgnoreCase("{")) {
                        depth++;
                    }
                    if (representation.get(i).equalsIgnoreCase("}")) {
                        depth--;
                    }
                    if (depth == 0) {
                        break;
                    }
                    part.add(representation.get(i));
                }
                // pull part back from recursive call
                part = this.repToCode1(new BehaviourRepresentation(part), oBeforeIf);
                // add part to sequence if no part then add skip
                if (part.size() == 0) {
                    sequence.add(skip);
                } else {
                    for (String s : part) {
                        sequence.add(s);
                    }
                }
                // reset lastX and last Y to before if branch
                lastOrientation = oBeforeIf;
                // do second part of if
                i = i + 2;
                depth = 1;
                part = new ArrayList<String>();
                while (i < representation.size()) {
                    if (representation.get(i).equalsIgnoreCase("{")) {
                        depth++;
                    }
                    if (representation.get(i).equalsIgnoreCase("}")) {
                        depth--;
                    }
                    if (depth == 0) {
                        break;
                    }
                    part.add(representation.get(i));
                    i++;
                }
                // pull part back from recursive call
                part = this.repToCode1(new BehaviourRepresentation(part), oBeforeIf);
                // add part to sequence if no part then add skip
                if (part.size() == 0) {
                    sequence.add(skip);
                } else {
                    for (String s : part) {
                        sequence.add(s);
                    }
                }
                // end close brakct
                sequence.add(cB);
                // move i along one to get out of final if bracket
                lastOrientation = oBeforeIf;
            } else if (instruction.equalsIgnoreCase("}")) {
                // do nothing                
            } else {
                System.out.println("REP TO CODE ERROR - GPEQUIVALENCE AA");
            }
        }

        // combine using PROGN2 and PROGN3
        // count expressions
        int count = 2;        
        while (count > 1) {
            //System.out.println(sequence);
            // count number of loose function in sequence
            count = 0;
            int depth = 0;
            for (String s : sequence) {
                if (depth == 0) {
                    count++;
                }
                if (s.equalsIgnoreCase("(")) {
                    depth++;
                }
                if (s.equalsIgnoreCase(")")) {
                    depth--;
                }
            }

            // sort out composing function tree
            if (count == 1) {
                // do nothing is resolved ready to return
            } else if (count == 2) {
                // function up PROGN2
                sequence.add(0, oB);
                sequence.add(1, p2);
                sequence.add(cB);
            } else if (count == 3) {
                // function up PROGN3
                sequence.add(0, oB);
                sequence.add(1, p3);
                sequence.add(cB);
            } else if (count > 3) {
                int p3s = count / 3;
                int p2s = 0;
                if(count%3==2) {
                    p2s = 1;
                }
                // process p3s
                int counter;
                int pSDone = 0;
                int endIndex = 0;
                for (int i = 0; i<sequence.size(); i++) {                    
                    // process PROGN3s
                    if (pSDone < p3s) {
                        counter = 0;
                        sequence.add(i, oB);
                        // move i along one to add function at next point
                        i++;
                        sequence.add(i, p3);
                        // move i along one so PROGN3 does not get included in count through
                        i++;
                        depth = 0;
                        while (counter < 3) {
                            if (sequence.get(i).equalsIgnoreCase("(")) {
                                depth++;
                            }
                            if (sequence.get(i).equalsIgnoreCase(")")) {
                                depth--;
                            }
                            if (depth == 0) {
                                counter++;
                            }
                            // manage index through
                            i++;
                        }
                        sequence.add(i, cB);                        
                        pSDone++;
                    } else {
                        endIndex = i;
                        break;
                    }
                }
                // do final for modulus and process PROGN2s if necessary
                if (p2s == 1) {
                    //System.out.println(sequence);                    
                    sequence.add(endIndex, oB);
                    // move i along one
                    endIndex++;
                    sequence.add(endIndex, p2);
                    // add bracket at end
                    //System.out.println(sequence);
                    sequence.add(cB);
                    //System.out.println(sequence);
                }
            } else if (count == 0) {
                // do nothing
                //System.out.println("COUNT IS ZERO ERROR");
            } else {
                System.out.println("REP TO CODE RESOLUTION ERROR IN GPEQUIVALENCEAA");
                System.out.println("EXPR COUNT = " + count);
                System.out.println(representation);
                System.out.println(sequence);
            }
        }
        //System.out.println(sequence);
        return sequence;
    }
 
    private void runAnt(ArrayList<String> expr) {
        
        int eSize = expr.size();
        
        if(eSize<1) {
            System.out.println("Resolution ERROR - invalid expression - " + expr);
        } else if(eSize==1) {
            if(expr.get(0).equalsIgnoreCase("MOVE")) {
                myAnt.move();
                antModel.add("M");
            } else if(expr.get(0).equalsIgnoreCase("TURN-LEFT")) {
                myAnt.turnLeft();
                antModel.add(myAnt.getOrientation());
            } else if(expr.get(0).equalsIgnoreCase("TURN-RIGHT")) {
                myAnt.turnRight();
                antModel.add(myAnt.getOrientation());
            } else if(expr.get(0).equalsIgnoreCase("SKIP")) {
                // do nothing - skip is escape charachter for if statement branches
            } else {
                System.out.println("Resolution ERROR - INVALID TERMINAL DETECTED - " + expr);
            }
        } else {
            if(expr.get(0).equalsIgnoreCase("(")) {
                // bracket scenario - remove each end and resolve expr on inside of bracket
                ArrayList<String> test = new ArrayList<String>();
                int top = expr.size() - 1;
                for (int i = 1; i < top; i++) {
                    test.add(expr.get(i));
                }
                this.runAnt(test);
            } else if (expr.get(0).equalsIgnoreCase("IF-FOOD-AHEAD")) {
                antModel.add("{");
                // execute both and plot positions
                String oldOrientation = myAnt.getOrientation();
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
                //reset position in ant for call if food ahead is false
                antModel.add(myAnt.getOrientation());
                antModel.add("}");
                antModel.add("{");
                myAnt.setOrientation(oldOrientation);
                // second expr
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
                //reset position in ant for next set of calls
                antModel.add(myAnt.getOrientation());
                antModel.add("}");
                myAnt.setOrientation(oldOrientation);                
            } else if (expr.get(0).equalsIgnoreCase("PROGN2")) {
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
}

    
    
    
    