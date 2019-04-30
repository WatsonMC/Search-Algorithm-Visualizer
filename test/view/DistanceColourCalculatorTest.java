package view;

import java.awt.Color;

import org.junit.Test;

import junit.framework.TestCase;

public class DistanceColourCalculatorTest extends TestCase{
	
	private static GridPosition posn1;
	private static GridPosition posn2;
	private static GridPosition posn3;
	private static GridPosition posn4;
	
	@Override
	public void setUp() throws Exception{
		posn1 = new GridPosition(0, 0);
		posn1.setDistance(0);
		posn2 = new GridPosition(10, 0);
		posn2.setDistance(1);
		posn3 = new GridPosition(0, 10);
		posn3.setDistance(1000);
		posn4 = new GridPosition(10, 10);
		posn4.setDistance(500);
	}
	
	@Test
	public void testMaxDistanceWeighting() {
		/**
		 * getMaxDistanceWeighting: GridPosition posn, int maxDistance-> int weighting
		 * Inputs:
		 * - min posn distance (0) ->T1
		 * - max position distance (maxDistance value)-> T2
		 * - random value -> T3
		 * - maxDistance = 1
		 * - maxDistance = 1000
		 * - out of range
		 */
		int md1 = 0;
		int md2 = 1;
		int md3 = 1000;
		
		//Test1
		assertTrue("Test 1,testMaxDistanceWeighting", DistanceColourCalculator.getMaxDistanceWeighting(posn1, md3) == 0);
		//Test2
		assertTrue("Test 2,testMaxDistanceWeighting", DistanceColourCalculator.getMaxDistanceWeighting(posn3, md3) == 50);
		//Test3
		assertTrue("Test 3,testMaxDistanceWeighting", DistanceColourCalculator.getMaxDistanceWeighting(posn4, md3) == 25);
		//assertTrue
		assertTrue("Test 4,testMaxDistanceWeighting", DistanceColourCalculator.getMaxDistanceWeighting(posn2, md2) == 50);
		
		boolean exceptionThrown = false;
		try {
			DistanceColourCalculator.getMaxDistanceWeighting(posn3, md1);
		}
		catch(IllegalArgumentException iae) {
			exceptionThrown = true;
		}
		finally {
			assertTrue(exceptionThrown);
		}
	}
	
	@Test
	public void testStraightDistanceWeighting() {
		GridPosition target = new GridPosition(100, 0);
		GridPosition source = new GridPosition(50,0);
		GridPosition posn5 = new GridPosition(25,0);
		GridPosition posn6 = new GridPosition(75,0);
 		//Test set 1
		/*source = 00
		 * target = 100
		 * 
		 * posn is at 00 -> weighhting should be 0
		 * posn is at 50,0 -> weighting should be 25
		 * posn is at 100,0 -> weighting be 50
		 * posn is at 25,0 = 12
		 */
			
		assertEquals(DistanceColourCalculator.getStraightDistanceWeighting(source, target, source), 25);
		assertEquals(DistanceColourCalculator.getStraightDistanceWeighting(source, target, target), 50);
		assertEquals(DistanceColourCalculator.getStraightDistanceWeighting(source, target, posn1), 0);
		assertEquals(DistanceColourCalculator.getStraightDistanceWeighting(source, target, posn5), 12);
		assertEquals(DistanceColourCalculator.getStraightDistanceWeighting(source, target, posn6), 37);
		
	}
	
	@Test
	public void testGetColour() {
		//TEST GET FULL RED, FULL GREEN, MIDDLE
		
		//at source:
		posn1.setDistance(0);
		assertTrue(DistanceColourCalculator.getDistanceColour(posn1, posn4, posn1, 10)
				.equals(new Color((float)(1.0), (float)(0.5),(float)(0.0))));
		
		GridPosition source = new GridPosition(0,50);
		GridPosition target = new GridPosition(0, 100);
		assertEquals(DistanceColourCalculator.getDistanceColour(source, target, posn1, 100)
				,(new Color((float)(1.0), (float)(0),(float)(0.0))));
		
		
	}
	
	@Test
	public void testGetStraightDistanced() {
		assertTrue("Test 1, testGetStraightDistance", DistanceColourCalculator.getStraightDistance(posn1, posn2) == 10);
		assertTrue("Test 2, testGetStraightDistance", DistanceColourCalculator.getStraightDistance(posn1, posn3) == 10);
		assertEquals(DistanceColourCalculator.getStraightDistance(posn1, posn4), (int)Math.sqrt((double)10*10 + 10*10));
	}
	
}
