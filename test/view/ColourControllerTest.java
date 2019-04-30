package view;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;
import testUtils.TestUtil;
public class ColourControllerTest extends TestCase{
	public GridPanel testGrid;
	
	@Test
	public void testColourController()  {
		//TestUtil tester = new TestUtil();
		//tester.main(new String[] {"view"});
		 
	}
	
	@Override
	public void setUp() throws Exception{
		testGrid = new GridPanel(5,5);
	}
	
	
	
	@Test
	public void testDraws() throws IOException{
		GridPosition source = new GridPosition(0, 0);
		GridPosition target = new GridPosition(0, 1);
		GridPosition blocked = new GridPosition(1, 0);
		GridPosition unblocked = new GridPosition(1, 1);
		
		ColourController ctrl = new ColourController(); //  to get icons
		
		
		//tests:
		// draw blocked gets rigght icon
		// draw source draws correct icon
		// draw target draws correct icon
		// draw unblocked draws correct shades
		
		//test source
		source.setState(GridPanel.SOURCE);
		ctrl.colourGridPosition(source, false);
		assertEquals(ctrl.getIcon(GridPanel.SOURCE), source.getIcon());
		ctrl.colourGridPosition(source, true);
		assertEquals(ctrl.getIcon(GridPanel.SOURCE), source.getIcon());
		
		//test target
		target.setState(GridPanel.TARGET);
		ctrl.colourGridPosition(target, false);
		assertEquals(ctrl.getIcon(GridPanel.TARGET), target.getIcon());
		ctrl.colourGridPosition(target, true);
		assertEquals(ctrl.getIcon(GridPanel.TARGET), target.getIcon());
		
		//test blocked
		blocked.setState(GridPanel.BLOCKED);
		ctrl.colourGridPosition(blocked, false);
		assertEquals(ctrl.getIcon(GridPanel.BLOCKED), blocked.getIcon());
		ctrl.colourGridPosition(blocked, true);
		assertEquals(ctrl.getIcon(GridPanel.BLOCKED), blocked.getIcon());
		
		//Test unblocked
		unblocked.setState(GridPanel.UNBLOCKED);
		ctrl.colourGridPosition(unblocked, false);
		assertEquals(ctrl.getIcon(GridPanel.UNBLOCKED), unblocked.getIcon());
		
	}
	
	@Test
	public void testSetMaxDistance() throws IOException {
		ColourController ctrl = new ColourController(testGrid);
		ctrl.setMaxDistanceWithoutUpdate(10);
		assertTrue(ctrl.getMaxDistance() == 10);
	}
	
	/*-----------------------------------------------------
	//SUPERSEDED by seperation of colour generation into DistanceColourCalculator
	*/
	//@Test
	//public void testColourWeightings() throws IOException{
		
		//
		///S
		//check:
		// - distance = 0 
		// - distance = maxDistance
		// - distance =25% maxDistance
//		GridPanel gr1  =  new GridPanel(5, 5);
		//Max Distance testing
	//	ColourController colour = new ColourController(gr1);
		
		//int maxD1 = 200;
		//int maxD2 = 12000;
	
		//int t1 = 100;
		//int t2 = 200;
		//int t3 = 0;
		//int t4 = 12000;
		//int t5 = 652;
		//GridPosition posn1 = new GridPosition(1, 1);
		//posn1.setDistance(t1);
		//assertTrue(colour.getMaxDistanceWeight(posn1, maxD1) == 25);
		
		//posn1.setDistance(t2);
		//assertTrue("actual value is: " +colour.getMaxDistanceWeight(posn1, maxD1), colour.getMaxDistanceWeight(posn1, maxD1) == 50);

		//posn1.setDistance(t3);
		//assertTrue(colour.getMaxDistanceWeight(posn1, maxD1) == 0);

		
		//colour.setMaxDistanceWithUpdate(maxD1);
		//posn1.setDistance(t1);
		
//		GridPosition posn2 = new GridPosition(1, 1);
//		posn2.setDistance(t2);
//		
//		GridPosition posn3 = new GridPosition(1, 1);/
//		posn3.setDistance(t3);
//		
		//Straight distance weighting test
	//	String pos1 = "0,0";
//		String pos2 = "4,4";
//		String pos3 = "1,2";
	//	String pos4 = "3,3";
		//String pos5 = "0,2";
//		gr1.setTarget(pos2);
//		gr1.setSource(pos1);

	//}
	
}
