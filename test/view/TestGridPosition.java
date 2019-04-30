package view;

import org.junit.Assert;
import org.junit.Test;

public class TestGridPosition {
	
	@Test
	public void testInit() {
		GridPosition posn = new GridPosition(1,1);
		Assert.assertTrue(posn.getDistance() == Integer.MAX_VALUE);
	}
	
	@Test 
	public void testSetState() {
		GridPosition posn = new GridPosition(1,1);
		Assert.assertTrue(posn.setState(GridPanel.BLOCKED));
		Assert.assertEquals(posn.getState(), GridPanel.BLOCKED);
		
		Assert.assertTrue(posn.setState(GridPanel.TARGET));
		Assert.assertEquals(posn.getState(), GridPanel.TARGET);
		
		Assert.assertTrue(posn.setState(GridPanel.SOURCE));
		Assert.assertEquals(posn.getState(), GridPanel.SOURCE);
		
		Assert.assertTrue(posn.setState(GridPanel.UNBLOCKED));
		Assert.assertEquals(posn.getState(), GridPanel.UNBLOCKED);
		
		Assert.assertFalse(posn.setState(-2));
		Assert.assertEquals(posn.getState(), GridPanel.UNBLOCKED);
	}
	
	@Test
	public void testSetDistance() {
		//Tests setting distance methods
		GridPosition posn = new GridPosition(1,1);
		
		posn.setDistance(100);
		Assert.assertTrue(posn.getDistance() == 100);
		posn.setDistance(200);
		Assert.assertTrue(posn.getDistance() == 200);
	}
	
	@Test 
	public void testCoords() {
		GridPosition posn1 = new GridPosition(0,0);
		GridPosition posn2 = new GridPosition(0,100);
		GridPosition posn3 = new GridPosition(0,555);
		GridPosition posn4 = new GridPosition(555,999);
		
		Assert.assertTrue(posn1.getXCoord() == 0 && posn1.getYCoord() == 0);
		Assert.assertTrue(posn2.getXCoord() == 0 && posn2.getYCoord() == 100);
		Assert.assertTrue(posn3.getXCoord() == 0 && posn3.getYCoord() == 555);
		Assert.assertTrue(posn4.getXCoord() == 555 && posn4.getYCoord() == 999);
	}
}
