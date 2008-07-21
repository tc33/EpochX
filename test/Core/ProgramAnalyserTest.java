/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Core;

import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author lb212
 */
public class ProgramAnalyserTest extends TestCase {
    
    public ProgramAnalyserTest(String testName) {
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
     * Test of getDepthOfTree method, of class ProgramAnalyser.
     */
    public void testGetDepthOfTree1() {
        System.out.println("getDepthOfTree1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        int expResult = 1;
        int result = ProgramAnalyser.getDepthOfTree(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetDepthOfTree2() {
        System.out.println("getDepthOfTree2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("A0");
        int expResult = 0;
        int result = ProgramAnalyser.getDepthOfTree(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetDepthOfTree3() {
        System.out.println("getDepthOfTree3");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        int expResult = 2;
        int result = ProgramAnalyser.getDepthOfTree(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetDepthOfTree4() {
        System.out.println("getDepthOfTree4");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("(");
        prog.add("OR");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        prog.add("D3");
        prog.add(")");
        int expResult = 3;
        int result = ProgramAnalyser.getDepthOfTree(prog);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNoOfFunctions method, of class ProgramAnalyser.
     */
    public void testGetNoOfFunctions1() {
        System.out.println("getNoOfFunctions1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add("IF");
        nodes.add("AND");
        nodes.add("OR");
        nodes.add("NOT");
        int expResult = 1;
        int result = ProgramAnalyser.getNoOfFunctions(prog, nodes);
        assertEquals(expResult, result);
    }
    
    public void testGetNoOfFunctions2() {
        System.out.println("getNoOfFunctions2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("A0");
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add("IF");
        nodes.add("AND");
        nodes.add("OR");
        nodes.add("NOT");
        int expResult = 0;
        int result = ProgramAnalyser.getNoOfFunctions(prog, nodes);
        assertEquals(expResult, result);
    }
    
    public void testGetNoOfFunctions3() {
        System.out.println("getNoOfFunctions3");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add("IF");
        nodes.add("AND");
        nodes.add("OR");
        nodes.add("NOT");
        int expResult = 2;
        int result = ProgramAnalyser.getNoOfFunctions(prog, nodes);
        assertEquals(expResult, result);
    }
    
    public void testGetNoOfFunctions4() {
        System.out.println("getNoOfFunctions4");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("(");
        prog.add("OR");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        prog.add("D3");
        prog.add(")");
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add("IF");
        nodes.add("AND");
        nodes.add("OR");
        nodes.add("NOT");
        int expResult = 3;
        int result = ProgramAnalyser.getNoOfFunctions(prog, nodes);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNoOfTerminals method, of class ProgramAnalyser.
     */
    public void testGetNoOfTerminals1() {
        System.out.println("getNoOfTerminals1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 3;
        int result = ProgramAnalyser.getNoOfTerminals(prog, terms);
        assertEquals(expResult, result);
    }
    
    public void testGetNoOfTerminals2() {
        System.out.println("getNoOfTerminals2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("A0");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 1;
        int result = ProgramAnalyser.getNoOfTerminals(prog, terms);
        assertEquals(expResult, result);
    }
    public void testGetNoOfTerminals3() {
        System.out.println("getNoOfTerminals3");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 4;
        int result = ProgramAnalyser.getNoOfTerminals(prog, terms);
        assertEquals(expResult, result);
    }
    public void testGetNoOfTerminals4() {
        System.out.println("getNoOfTerminals4");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("(");
        prog.add("OR");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        prog.add("D3");
        prog.add(")");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 5;
        int result = ProgramAnalyser.getNoOfTerminals(prog, terms);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDistinctTerminals method, of class ProgramAnalyser.
     */
    public void testGetDistinctTerminals1() {
        System.out.println("getDistinctTerminals1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("IF");
        prog.add("A1");
        prog.add("A1");
        prog.add("A1");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 1;
        int result = ProgramAnalyser.getDistinctTerminals(prog, terms);
        assertEquals(expResult, result);
    }
    
    public void testGetDistinctTerminals2() {
        System.out.println("getDistinctTerminals2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("IF");
        prog.add("A1");
        prog.add("A0");
        prog.add("A1");
        ArrayList<String> terms = new ArrayList<String>();
        terms.add("A0");
        terms.add("A1");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        int expResult = 2;
        int result = ProgramAnalyser.getDistinctTerminals(prog, terms);
        assertEquals(expResult, result);
    }

    /**
     * Test of getModificationDepth method, of class ProgramAnalyser.
     */
    public void testGetModificationDepth1() {
        System.out.println("getModificationDepth1");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        int expResult = 1;
        int result = ProgramAnalyser.getModificationDepth(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    public void testGetModificationDepth2() {
        System.out.println("getModificationDepth2");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("(");
        prog1.add("AND");
        prog1.add("(");
        prog1.add("OR");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        prog1.add("D2");
        prog1.add(")");
        prog1.add("D3");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        int expResult = 2;
        int result = ProgramAnalyser.getModificationDepth(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProgramLength method, of class ProgramAnalyser.
     */
    public void testGetProgramLength1() {
        System.out.println("getProgramLength1");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        int expResult = 4;
        int result = ProgramAnalyser.getProgramLength(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetProgramLength2() {
        System.out.println("getProgramLength2");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("A0");
        int expResult = 1;
        int result = ProgramAnalyser.getProgramLength(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetProgramLength3() {
        System.out.println("getProgramLength3");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        int expResult = 6;
        int result = ProgramAnalyser.getProgramLength(prog);
        assertEquals(expResult, result);
    }
    
    public void testGetProgramLength4() {
        System.out.println("getProgramLength4");
        ArrayList<String> prog = new ArrayList<String>();
        prog.add("(");
        prog.add("IF");
        prog.add("A0");
        prog.add("(");
        prog.add("AND");
        prog.add("(");
        prog.add("OR");
        prog.add("D0");
        prog.add("D1");
        prog.add(")");
        prog.add("D2");
        prog.add(")");
        prog.add("D3");
        prog.add(")");
        int expResult = 8;
        int result = ProgramAnalyser.getProgramLength(prog);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSyntaxPercentageSimilarity method, of class ProgramAnalyser.
     */
    public void testGetSyntaxPercentageSimilarity0() {
        System.out.println("getSyntaxPercentageSimilarity0");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("D0");
        prog1.add("D2");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        double expResult = 50.0;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog2, prog1);
        assertEquals(expResult, result);
    }
    
    public void testGetSyntaxPercentageSimilarity1() {
        System.out.println("getSyntaxPercentageSimilarity1");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("D0");
        prog1.add("D2");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        double expResult = 50.0;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    public void testGetSyntaxPercentageSimilarity2() {
        System.out.println("getSyntaxPercentageSimilarity2");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("(");
        prog1.add("AND");
        prog1.add("(");
        prog1.add("OR");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        prog1.add("D2");
        prog1.add(")");
        prog1.add("D3");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D3");
        prog2.add(")");
        double expResult = 50.0;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    public void testGetSyntaxPercentageSimilarity3() {
        System.out.println("getSyntaxPercentageSimilarity3");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("(");
        prog1.add("AND");
        prog1.add("(");
        prog1.add("OR");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        prog1.add("D2");
        prog1.add(")");
        prog1.add("D3");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("A0");
        double expResult = 0.0;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    public void testGetSyntaxPercentageSimilarity4() {
        System.out.println("getSyntaxPercentageSimilarity4");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("(");
        prog1.add("AND");
        prog1.add("(");
        prog1.add("OR");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        prog1.add("D2");
        prog1.add(")");
        prog1.add("D3");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("(");
        prog2.add("OR");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        prog2.add("D3");
        prog2.add(")");
        double expResult = 100.0;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog1, prog2);
        assertEquals(expResult, result);
    }
    
    public void testGetSyntaxPercentageSimilarity5() {
        System.out.println("getSyntaxPercentageSimilarity5");
        ArrayList<String> prog1 = new ArrayList<String>();
        prog1.add("(");
        prog1.add("IF");
        prog1.add("A0");
        prog1.add("D0");
        prog1.add("(");
        prog1.add("AND");
        prog1.add("D0");
        prog1.add("D1");
        prog1.add(")");
        prog1.add(")");
        ArrayList<String> prog2 = new ArrayList<String>();
        prog2.add("(");
        prog2.add("IF");
        prog2.add("A0");
        prog2.add("(");
        prog2.add("AND");
        prog2.add("D0");
        prog2.add("D1");
        prog2.add(")");
        prog2.add("D2");
        prog2.add(")");
        double expResult = 33.33333333333333;
        double result = ProgramAnalyser.getSyntaxPercentageSimilarity(prog2, prog1);
        assertEquals(expResult, result);
    }
}
