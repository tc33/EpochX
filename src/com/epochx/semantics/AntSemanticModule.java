/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
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
package com.epochx.semantics;

import java.util.ArrayList;
import java.util.List;

import com.epochx.ant.Ant;
import com.epochx.core.GPModel;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.TerminalNode;

/**
 * @author lb212
 *
 */
public class AntSemanticModule implements SemanticModule {
	
	private List<TerminalNode<?>> terminals;
	private GPModel model;
	
	/**
	 * Constructor for Ant Semantic Module
	 * @param list List of terminal nodes
	 * @param model The GPModel object
	 */
	public AntSemanticModule(List<TerminalNode<?>> list, GPModel model) {
		this.terminals = list;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#start()
	 */
	@Override
	public void start() {
		// Not required as we do not need to activate external software to study behaviour
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#stop()
	 */
	@Override
	public void stop() {
		// Not required as we do not need to activate external software to study behaviour
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#behaviourToCode(com.epochx.semantics.Representation)
	 */
	@Override
	public CandidateProgram behaviourToCode(Representation representation) {
		// TODO Auto-generated method stub		
		return this.repToCode1(representation, "E");
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public Representation codeToBehaviour(CandidateProgram program) {
		// TODO Auto-generated method stub
		
		// develop ant monitoring model
        ArrayList<String>antModel = new ArrayList<String>();

        // initialise a new ant
        Ant myAnt = new Ant(600, null);
        this.runAnt(program);
        
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
                antModel = this.condenseAntRep(antModel);
            }
        }

        return new AntRepresentation(antModel);
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
	
	public ArrayList<String> condenseAntRep(ArrayList<String> result) {
        
        // cycle through removing duplicate subsets
        // work out total depth                   
        int maxDepth = 0;
        int depth = 0;      
        
        // ---------------------------------------------------------------------
        for (String s : result) {
            if (s.equals("{")) {
                depth++;
            }
            if (s.equals("}")) {
                depth--;
            }
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        //condense brackets only if there are brackets i.e. maxDepth>0
        if (maxDepth > 0) {
            boolean reduce = true;
            while (reduce) {
                reduce = false;
                // cycle through and condense
                int masterDepth = 0;
                depth = 0;
                int[] tracker = new int[maxDepth + 1];
                // set all to zero
                for (int i = 0; i < tracker.length; i++) {
                    tracker[i] = 0;
                }
                ArrayList<String> subset1, subset2;
                for (int i = 0; i < result.size() - 1; i++) {
                    if (result.get(i).equalsIgnoreCase("{")) {
                        tracker[masterDepth]++;
                    }
                    if (tracker[masterDepth] % 2 == 1 && result.get(i).equalsIgnoreCase("{")) {
                        subset1 = new ArrayList<String>();
                        subset2 = new ArrayList<String>();
                        depth = 0;
                        int endPoint1 = 0;
                        int endPoint2 = 0;
                        for (int y = i; y < result.size(); y++) {
                            if (result.get(y).equalsIgnoreCase("{")) {
                                depth++;
                            }
                            if (result.get(y).equalsIgnoreCase("}")) {
                                depth--;
                            }
                            subset1.add(result.get(y));
                            if (depth == 0) {
                                endPoint1 = y;
                                break;
                            }
                        }
                        for (int y = endPoint1 + 1; y < result.size(); y++) {
                            if (result.get(y).equalsIgnoreCase("{")) {
                                depth++;
                            }
                            if (result.get(y).equalsIgnoreCase("}")) {
                                depth--;
                            }
                            subset2.add(result.get(y));
                            if (depth == 0) {
                                endPoint2 = y;
                                break;
                            }
                        }
                        // check if subsets equivalent
                        if (subset1.equals(subset2)) {                                                
                            
                            // pull up pre if code
                            ArrayList<String> preif = new ArrayList<String>();
                            // work out expected orientation before IF
                            String expectedO = "E";
                            if (i > 0) {
                                for (int k = 0; k < i; k++) {
                                    preif.add(result.get(k));
                                    if (result.get(k).equalsIgnoreCase("N") || result.get(k).equalsIgnoreCase("S") || result.get(k).equalsIgnoreCase("E") || result.get(k).equalsIgnoreCase("W")) {
                                        expectedO = result.get(k);
                                    }
                                }
                            }
                            
                            // add subset1 to pre if
                            subset1.remove(0);
                            subset1.remove(subset1.size()-1);
                            for(String s: subset1) {
                                preif.add(s);
                            }
                            
                            // get post if code if necessary
                            if(result.size()>endPoint2) {
                                // get post if code
                                ArrayList<String> postif = new ArrayList<String>();
                                for(int k = (endPoint2+1); k<result.size(); k++) {
                                    postif.add(result.get(k));
                                }
                                // add post if code to preif+subset1 - care with orientation
                                result = joinPaths(preif, postif, expectedO);                                
                            } else {
                                result = preif;
                            }
                            
                            reduce = true;
                            break;
                        }
                    }
                    // fix depth afterwards
                    if (result.get(i).equalsIgnoreCase("{")) {
                        masterDepth++;
                    }
                    if (result.get(i).equalsIgnoreCase("}")) {
                        masterDepth--;
                    }
                }
            }
        }
        // ---------------------------------------------------------------------
        
        // pull out orientation letters in sequence
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).equalsIgnoreCase("N") || result.get(i).equalsIgnoreCase("W") || result.get(i).equalsIgnoreCase("S") || result.get(i).equalsIgnoreCase("E")) {
                if (result.get(i + 1).equalsIgnoreCase("N") || result.get(i + 1).equalsIgnoreCase("W") || result.get(i + 1).equalsIgnoreCase("S") || result.get(i + 1).equalsIgnoreCase("E")) {
                    result.remove(i);
                    i--;
                }
            }
        }
        
        ArrayList<String> controlStack = new ArrayList<String>();
        controlStack.add("E");
        depth = 0;
        for(int i = 0; i<result.size(); i++) {
            if(result.get(i).equalsIgnoreCase("{")) {
                // amend depth
                depth++;
                // move up previous depths orientation
                controlStack.add(controlStack.get(depth-1));
            } else if(result.get(i).equalsIgnoreCase("}")){
                // remove orientation from top of control stack
                controlStack.remove(depth);
                // amend depth
                depth--;
            } else if(result.get(i).equalsIgnoreCase("M")){
                // do nothing
            } else {
                if(result.get(i).equalsIgnoreCase(controlStack.get(depth))) {
                    // remove duplicate orientation
                    result.remove(i);
                    i--;
                } else {
                    controlStack.set(depth, result.get(i));
                }
            }            
        }
        
        return result;
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
}
