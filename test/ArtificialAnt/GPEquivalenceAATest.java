/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArtificialAnt;

import Core.BehaviourRepresentation;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author Lawrence
 */
public class GPEquivalenceAATest extends TestCase {
    
    public GPEquivalenceAATest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of createRep method, of class GPEquivalenceAA.
     */
    public void testCreateRep1() {
        System.out.println("createRep1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN3");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("2:0");
        expResult.add("3:0");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep2() {
        System.out.println("createRep2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN3");
        prog.add("MOVE");
        prog.add("TURN-RIGHT");
        prog.add("MOVE");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("1:1");
        expResult.add("S");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep3() {
        System.out.println("createRep3");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep4() {
        System.out.println("createRep4");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("TURN-LEFT");
        prog.add("MOVE");
        prog.add(")");
        prog.add("MOVE");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("{");
        expResult.add("0:31");
        expResult.add("N");
        expResult.add("}");
        expResult.add("{");
        expResult.add("1:0");
        expResult.add("E");
        expResult.add("}");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep5() {
        System.out.println("createRep5");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN2");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("TURN-LEFT");
        prog.add("MOVE");
        prog.add(")");
        prog.add("MOVE");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("0:31");
        expResult.add("0:30");
        expResult.add("N");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep6() {
        System.out.println("createRep6");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN2");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("2:0");
        expResult.add("3:0");
        expResult.add("4:0");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep7() {
        System.out.println("createRep7");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN2");
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("2:0");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep8() {
        System.out.println("createRep8");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("TURN-LEFT");
        prog.add("MOVE");
        prog.add(")");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("TURN-RIGHT");
        prog.add("MOVE");
        prog.add(")");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("{");
        expResult.add("0:31");
        expResult.add("N");
        expResult.add("}");
        expResult.add("{");
        expResult.add("0:1");
        expResult.add("S");
        expResult.add("}");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }
    
    public void testCreateRep9() {
        System.out.println("createRep9");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("PROGN2");
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("(");
        prog.add("IF-FOOD-AHEAD");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add("MOVE");
        prog.add(")");
        prog.add("(");
        prog.add("PROGN2");
        prog.add("MOVE");
        prog.add("MOVE");
        prog.add(")");
        prog.add(")");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("1:0");
        expResult.add("2:0");
        expResult.add("E");
        BehaviourRepresentation result = instance.createRep(prog);
        assertEquals(expResult, result.getArrayList());
    }

    /**
     * Test of repToCode method, of class GPEquivalenceAA.
     */
    public void testRepToCode1() {
        System.out.println("repToCode1");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("1:0");
        rep.add("1:1");
        rep.add("2:1");
        rep.add("S");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("MOVE");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-LEFT");
        expResult.add("MOVE");
        expResult.add("TURN-RIGHT");
        expResult.add(")");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode2() {
        System.out.println("repToCode2");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("1:0");
        rep.add("2:0");
        rep.add("E");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("MOVE");
        expResult.add("MOVE");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode3() {
        System.out.println("repToCode3");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("0:1");
        rep.add("0:2");
        rep.add("S");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN3");        
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add("MOVE");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode4() {
        System.out.println("repToCode4");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("0:31");
        rep.add("1:31");
        rep.add("2:31");
        rep.add("2:30");
        rep.add("N");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("(");
        expResult.add("PROGN3");       
        expResult.add("TURN-LEFT");
        expResult.add("MOVE");
        expResult.add("TURN-RIGHT");
        expResult.add(")");
        expResult.add("(");
        expResult.add("PROGN3"); 
        expResult.add("MOVE");
        expResult.add("MOVE");        
        expResult.add("TURN-LEFT");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode5() {
        System.out.println("repToCode5");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("{");
        rep.add("0:31");
        rep.add("N");
        rep.add("}");
        rep.add("{");
        rep.add("1:0");
        rep.add("2:0");
        rep.add("E");
        rep.add("}");
        rep.add("E");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("TURN-LEFT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("MOVE");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode6() {
        System.out.println("repToCode6");
        ArrayList<String> rep = new ArrayList<String>();
        rep.add("{");
        rep.add("31:0");
        rep.add("W");
        rep.add("}");
        rep.add("{");
        rep.add("1:0");
        rep.add("E");
        rep.add("}");
        rep.add("E");
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-RIGHT");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode7() {
        System.out.println("repToCode7");
        ArrayList<String> rep = new ArrayList<String>();
        
        rep.add("{");
        rep.add("31:0");
        rep.add("W");
        rep.add("}");
        
        rep.add("{");
        rep.add("1:0");
        rep.add("E");
        rep.add("}");
        
        rep.add("{");
        rep.add("31:0");
        rep.add("W");
        rep.add("}");
        
        rep.add("{");
        rep.add("1:0");
        rep.add("E");
        rep.add("}");
        
        rep.add("E");
        
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-RIGHT");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-RIGHT");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }
    
    public void testRepToCode8() {
        System.out.println("repToCode8");
        ArrayList<String> rep = new ArrayList<String>();
        
        rep.add("0:31");
        rep.add("0:0");
        rep.add("31:0");
        rep.add("31:31");
        rep.add("31:0");
        rep.add("30:0");        
        rep.add("W");
        
        GPEquivalenceAA instance = new GPEquivalenceAA();
        BehaviourRepresentation thisRep = new BehaviourRepresentation(rep);        
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("(");
        expResult.add("PROGN2");
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-RIGHT");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("(");
        expResult.add("IF-FOOD-AHEAD");
        expResult.add("(");
        expResult.add("PROGN3");
        expResult.add("TURN-RIGHT");
        expResult.add("TURN-RIGHT");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add("MOVE");
        expResult.add(")");
        expResult.add(")");
        ArrayList<String> result = instance.repToCode(thisRep);
        assertEquals(expResult, result);
    }

}
