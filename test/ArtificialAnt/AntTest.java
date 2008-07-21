/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArtificialAnt;

import junit.framework.TestCase;

/**
 *
 * @author lb212
 */
public class AntTest extends TestCase {
    
    public AntTest(String testName) {
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
     * Test of turnLeft method, of class Ant.
     */
    public void testTurnLeft1() {
        System.out.println("turnLeft1");
        Ant instance = new Ant(400);
        String expected = "N";
        instance.turnLeft();
        String result = instance.getOrientation();
        assertEquals(expected, result);        
    }
    
    public void testTurnLeft2() {
        System.out.println("turnLeft2");
        Ant instance = new Ant(400);
        instance.turnLeft();
        String expected = "W";
        instance.turnLeft();
        String result = instance.getOrientation();
        assertEquals(expected, result);        
    }
    
    public void testTurnLeft3() {
        System.out.println("turnLeft3");
        Ant instance = new Ant(400);
        instance.turnLeft();
        instance.turnLeft();
        String expected = "S";
        instance.turnLeft();
        String result = instance.getOrientation();
        assertEquals(expected, result);        
    }
    
    public void testTurnLeft4() {
        System.out.println("turnLeft4");
        Ant instance = new Ant(400);
        instance.turnLeft();
        instance.turnLeft();
        instance.turnLeft();
        String expected = "E";
        instance.turnLeft();
        String result = instance.getOrientation();
        assertEquals(expected, result);        
    }

    /**
     * Test of turnRight method, of class Ant.
     */
    public void testTurnRight1() {
        System.out.println("turnRight1");
        Ant instance = new Ant(400);
        String expected = "S";
        instance.turnRight();
        String result = instance.getOrientation();
        assertEquals(expected, result);
    }
    
    public void testTurnRight2() {
        System.out.println("turnRight2");
        Ant instance = new Ant(400);
        instance.turnRight();
        String expected = "W";
        instance.turnRight();
        String result = instance.getOrientation();
        assertEquals(expected, result);
    }
    
    public void testTurnRight3() {
        System.out.println("turnRight3");
        Ant instance = new Ant(400);
        instance.turnRight();
        instance.turnRight();
        String expected = "N";
        instance.turnRight();
        String result = instance.getOrientation();
        assertEquals(expected, result);
    }
    
    public void testTurnRight4() {
        System.out.println("turnRight4");
        Ant instance = new Ant(400);
        instance.turnRight();
        instance.turnRight();
        instance.turnRight();
        String expected = "E";
        instance.turnRight();
        String result = instance.getOrientation();
        assertEquals(expected, result);
    }

    /**
     * Test of move method, of class Ant.
     */
    public void testMove1() {
        System.out.println("move1");
        Ant instance = new Ant(400);
        instance.move();
        instance.turnRight();
        instance.move();
        // now at 1:1 facing south
        String expected = "1:2";
        instance.move();
        String result = instance.getXLocation() + ":" + instance.getYLocation();
        assertEquals(expected, result);        
    }
    
    public void testMove2() {
        System.out.println("move2");
        Ant instance = new Ant(400);
        instance.move();
        instance.turnRight();
        instance.move();
        // now at 1:1 facing south
        instance.turnLeft();
        String expected = "2:1";
        instance.move();
        String result = instance.getXLocation() + ":" + instance.getYLocation();
        assertEquals(expected, result);        
    }
    
    public void testMove3() {
        System.out.println("move3");
        Ant instance = new Ant(400);
        instance.move();
        instance.turnRight();
        instance.move();
        // now at 1:1 facing south
        instance.turnRight();
        String expected = "0:1";
        instance.move();
        String result = instance.getXLocation() + ":" + instance.getYLocation();
        assertEquals(expected, result);        
    }
    
    public void testMove4() {
        System.out.println("move4");
        Ant instance = new Ant(400);
        instance.move();
        instance.turnRight();
        instance.move();
        // now at 1:1 facing south
        instance.turnRight();
        instance.turnRight();
        String expected = "1:0";
        instance.move();
        String result = instance.getXLocation() + ":" + instance.getYLocation();
        assertEquals(expected, result);        
    }

    /**
     * Test of eatFood method, of class Ant.
     */
    public void testEatFood() {
        System.out.println("eatFood");
        Ant instance = new Ant(400);
        instance.eatFood();
        instance.eatFood();
        instance.eatFood();
        int expected = 3;
        int result = instance.getFoodEaten();
        assertEquals(expected, result);
    }

    /**
     * Test of getXLocation method, of class Ant.
     */
    public void testGetXLocation() {
        System.out.println("getXLocation");
        Ant instance = new Ant(400);
        int expResult = 0;
        int result = instance.getXLocation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getYLocation method, of class Ant.
     */
    public void testGetYLocation() {
        System.out.println("getYLocation");
        Ant instance = new Ant(400);
        int expResult = 0;
        int result = instance.getYLocation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMoves method, of class Ant.
     */
    public void testGetMoves() {
        System.out.println("getMoves");
        Ant instance = new Ant(400);
        instance.move();
        instance.turnLeft();
        instance.turnRight();
        instance.eatFood();
        instance.move();
        int expResult = 4;
        int result = instance.getMoves();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOrientation method, of class Ant.
     */
    public void testGetOrientation1() {
        System.out.println("getOrientation1");
        Ant instance = new Ant(400);
        String expResult = "E";
        String result = instance.getOrientation();
        assertEquals(expResult, result);
    }
    
    public void testGetOrientation2() {
        System.out.println("getOrientation2");
        Ant instance = new Ant(400);
        instance.turnLeft();
        String expResult = "N";
        String result = instance.getOrientation();
        assertEquals(expResult, result);
    }
    
    public void testGetOrientation3() {
        System.out.println("getOrientation3");
        Ant instance = new Ant(400);
        instance.turnRight();
        String expResult = "S";
        String result = instance.getOrientation();
        assertEquals(expResult, result);
    }
    
    public void testGetOrientation4() {
        System.out.println("getOrientation4");
        Ant instance = new Ant(400);
        instance.turnRight();
        instance.turnRight();
        String expResult = "W";
        String result = instance.getOrientation();
        assertEquals(expResult, result);
    }

}
