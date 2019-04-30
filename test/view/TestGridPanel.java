package view;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;
import model.LabelHelper;

public class TestGridPanel  extends TestCase{
	private GridPanel testPanel;
	private String v0;
	private String v1;
	private String v2;
	private String v3;
	private String v4;
	
	@Override
	public void setUp() throws Exception{
		testPanel =  new GridPanel(5,5);
		v0 = "0,0";
		v1 = "1,0";
		v2 = "2,5";
		v3 = "9,9";
		v4 = "2,9";
	}
	
	@Test
	public void testUpdateDistances(){
		//test panel of 10 x 10
		GridPanel testPanel = new GridPanel(10,10);
		
		/**
		 * Tests:
		 * posn cases
		 * 1. update all distances ->T3
		 * 2. Update partial distances ->T1
		 * 3. update one distance -T2
		 * 4. update non-existant distance (out of bounds) -> T4
		 * 
		 * value cases
		 * 1. 0 ->T1
		 * 2. MAX VALUE ->T1
		 * 3. normal ->T1
		 */

		Map<String, Integer> t1 = new HashMap<>();
		
		t1.put(v0,0);
		t1.put(v1,50);
		t1.put(v3,Integer.MAX_VALUE);
		t1.put(v4,7);
		t1.put(v2,12);
		
		//T1
		assertTrue(testPanel.updateGrid(t1));
		assertTrue(testPanel.getDistance(0, 0) == 0);
		assertTrue(testPanel.getDistance(1, 0) == 50);
		assertTrue(testPanel.getDistance(9, 9) == Integer.MAX_VALUE);
		assertTrue(testPanel.getDistance(2, 5) == 12);
		assertTrue(testPanel.getDistance(2, 9) == 7);
		
		//T2
		Map<String, Integer> t2 = new HashMap<>();
		t2.put(v0, 20);
		assertTrue(testPanel.updateGrid(t2));
		assertTrue(testPanel.getDistance(0, 0) == 20);
		
		Map<String, Integer> t3 = new HashMap<>();
		
		//T3
		for(int x = 0; x<10; x++) {
			for (int y = 0; y<10;y++) {
				t3.put(LabelHelper.makeLabel(x,y),5);
			}
		}
		assertTrue(testPanel.updateGrid(t3));
		for(int x = 0; x<10; x++) {
			for (int y = 0; y<10;y++) {
				assertTrue(testPanel.getDistance(x, y) == 5);
			}
		}
		
		Map<String,Integer> t4 = new HashMap<>();
		t4.put("test",-1);
		try {
			assertTrue(!testPanel.updateGrid(t4));
		}
		catch(IllegalArgumentException iae) {
			assertTrue(true);
		}
		
	}

	public void testBlock() {
		/**
		 * Test cases:
		 * 1. Grid position values
		 * - edge (0,0) => T1
		 * - edge (4,4) => T1
		 * - normal (1,2) => T1
		 * 
		 * 2. position state
		 * - blocking unblocked positions => t2
		 * - blocking already blocked position => t2
		 * - blocking source => t2
		 * - blocking target => t2
		 */
		GridPanel gr1 = new GridPanel(5, 5);
		
		String posn1 = "0,0";
		String posn2 = "1,2";
		String posn3 = "4,4";
		
		//T1 : test blocking normally
		gr1.block(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.BLOCKED);
		gr1.block(posn2);
		assertTrue(gr1.getState(posn2) == GridPanel.BLOCKED);
		gr1.block(posn3);
		assertTrue(gr1.getState(posn3) == GridPanel.BLOCKED);
		//all posns now blocked
		
		//T2: Test blocking types
		// BLOCKING WHEN BLOCKED
		gr1.block(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.BLOCKED);
		// BLOCKING WHEN TARGET
		gr1.setTarget(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.TARGET);
		gr1.block(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.BLOCKED);
		//BLOCKING WHEN SOURCE
		gr1.setSource(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.SOURCE);
		gr1.block(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.BLOCKED);		
	}
	
	public void testUnblock() {
		/**
		 * Test cases:
		 * 1. Grid position values
		 * - edge (0,0) => T1
		 * - edge (4,4) => T1
		 * - normal (1,2) => T1
		 * 
		 * 2. position state
		 * - unblocking unblocked positions => t2
		 * - Unblocking blocked already blocked position => t2
		 * - Unblocking source => t2
		 * - Unblocking target => t2
		
		 */
		GridPanel gr1 = new GridPanel(5, 5);
		
		String posn1 = "0,0";
		String posn2 = "1,2";
		String posn3 = "4,4";
		
		//T1 : test blocking normally
		gr1.unblock(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.UNBLOCKED);
		gr1.unblock(posn2);
		assertTrue(gr1.getState(posn2) == GridPanel.UNBLOCKED);
		gr1.unblock(posn3);
		assertTrue(gr1.getState(posn3) == GridPanel.UNBLOCKED);
		//all posns now unblocked
		
		//T2: Test position types
		// Unblocking when Unblocked
		gr1.unblock(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.UNBLOCKED);
		//UNBLOCKING WHEN TARGET
		gr1.setTarget(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.TARGET);
		gr1.unblock(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.UNBLOCKED);
		//UNBLOCKING WHEN SOURCE
		gr1.setSource(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.SOURCE);
		gr1.unblock(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.UNBLOCKED);
		//UNBLOCKING WHEN BLOCKED
		gr1.block(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.BLOCKED);
		gr1.unblock(posn1);
		assertTrue(gr1.getState(posn1) == GridPanel.UNBLOCKED);
		
	}
	
	public void testSetState() {
		GridPanel gr1 = new GridPanel(5, 5);
		String posn1 = "0,0";
		String posn2 = "1,2";
		String posn3 = "4,4";
		
		gr1.setState(0,0,GridPanel.SOURCE);
		assertTrue(gr1.getState(posn1) == GridPanel.SOURCE);
		
		gr1.setState(1,2,GridPanel.TARGET);
		assertTrue(gr1.getState(posn2) == GridPanel.TARGET);
		
		gr1.setState(4,4,GridPanel.UNBLOCKED);
		assertTrue(gr1.getState(posn3) == GridPanel.UNBLOCKED);
		
		//Test false state set
		assertTrue(!gr1.setState(0,0,-1));
	}
	
	public void testSetTarget() {
		// need to test:
		/**
		 * Curr target = null
		 * curr target != null
		 * curr target =  target
		 * target = null
		 */
		testPanel.setTarget(null);
		assertTrue(testPanel.getTargetLabel() == null);
		assertTrue(testPanel.getTarget() == null);

		//curr = null, new != null
		
		testPanel.setTarget(v0);
		//now expect currSource = v0, state = source
		assertTrue(testPanel.getTargetLabel().equals(v0));
		assertTrue(testPanel.getTarget().getState() == GridPanel.TARGET);
		assertTrue(testPanel.getGridPosition(v0).equals(testPanel.getTarget()));
		
		
		//currTarget !=null, expect set to set v0 to unblocked
		testPanel.setTarget(v1);
		assertTrue(testPanel.getTargetLabel().equals(v1));
		//test that the grid position at v1 is the target position
		assertTrue(testPanel.getGridPosition(v1).equals(testPanel.getTarget()));
		assertTrue(testPanel.getTarget().getState() == GridPanel.TARGET);
		//test previous source reset to unblocked
		assertTrue(testPanel.getGridPosition(v0).getState() == GridPanel.UNBLOCKED);

		// curr target = new target
		testPanel.setTarget(v1);
		//check that grid position now null
		assertTrue(testPanel.getTargetLabel() == v1);
		assertTrue(testPanel.getTarget().equals(testPanel.getGridPosition(v1)));
		//check that v1 is normal unblocked
		assertTrue(testPanel.getGridPosition(v1).getState() == GridPanel.TARGET);
		
		//set target = null
		testPanel.setTarget(null);
		assertTrue(testPanel.getTargetLabel() == null);
		assertTrue(testPanel.getTarget() == null );
		//test that it resets previous source
		assertTrue(testPanel.getGridPosition(v1).getState() == GridPanel.UNBLOCKED);
		
	}
	
	public void testSetSource() {
		/**
		 * States at time of set:
		 * - currSource = null
		 * - currSource != null
		 * - setSource = null
		 * - setSource !=null
		 * - setSource = currSource
		 */
		
		testPanel.setSource(null);
		assertTrue(testPanel.getSourceLabel() == null);
		assertTrue(testPanel.getSource() == null);

		//curr = null, new != null
		
		testPanel.setSource(v0);
		//now expect currSource = v0, state = source
		assertTrue(testPanel.getSourceLabel().equals(v0));
		assertTrue(testPanel.getSource().getState() == GridPanel.SOURCE);
		assertTrue(testPanel.getGridPosition(v0).equals(testPanel.getSource()));
		
		
		//currSource !=null, expect set to set v0 to unblocked
		testPanel.setSource(v1);
		assertTrue(testPanel.getSourceLabel().equals(v1));
		//test that the grid position at v1 is the source position
		assertTrue(testPanel.getGridPosition(v1).equals(testPanel.getSource()));
		assertTrue(testPanel.getSource().getState() == GridPanel.SOURCE);
		//test previous source reset to unblocked
		assertTrue(testPanel.getGridPosition(v0).getState() == GridPanel.UNBLOCKED);

		// curr source = new source
		testPanel.setSource(v1);
		//check that grid position now null
		assertTrue(testPanel.getSourceLabel() == v1);
		assertTrue(testPanel.getSource().equals(testPanel.getGridPosition(v1)));
		//check that v1 is normal unblocked
		assertTrue(testPanel.getGridPosition(v1).getState() == GridPanel.SOURCE);
		
		//set source = null
		testPanel.setSource(null);
		assertTrue(testPanel.getSourceLabel() == null);
		assertTrue(testPanel.getSource() == null );
		//test that it resets previous source
		assertTrue(testPanel.getGridPosition(v1).getState() == GridPanel.UNBLOCKED);

		
	}
	
	public void testGetGridPosition() {
		
	}
	
	public void testGetSource() {
		/**
		 * Test when source is null
		 * test when source not null
		 */
		testPanel.setSource(null);
		assertTrue(testPanel.getSourceLabel() ==null);
		assertTrue(testPanel.getSource() ==null);
		
		testPanel.setSource(v1);
		assertTrue(testPanel.getSourceLabel().equals(v1));
		assertTrue(testPanel.getGridPosition(v1).equals(testPanel.getSource()));
	}
	
	public void testGetTarget() {
		/**
		 * Test when target is null
		 * test when tgt not null
		 */
		testPanel.setTarget(null);
		assertTrue(testPanel.getTargetLabel() ==null);
		assertTrue(testPanel.getTarget() ==null);
		
		testPanel.setTarget(v1);
		assertTrue(testPanel.getTargetLabel().equals(v1));
		assertTrue(testPanel.getGridPosition(v1).equals(testPanel.getTarget()));
	}
}
