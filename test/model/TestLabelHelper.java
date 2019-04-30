package model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestLabelHelper extends TestCase{
	public static String v1;
	public static String v2;
	public static String v3;
	public static String v4;
	public static String v5;
	public static String v6;
	@Override
	public void setUp() throws Exception{
		v1 = "0,0";
		v2 = "1,1";
		v3 = "-1,2"; 	//errors
		v4 = "this is not correct";
		v5 = "0,1231234324";
		v6 = "123451235,3";
	}
	
	@Test
	public void testExceptions() {
		//Test failure calls
		boolean exThrown = false;
		try {
			runXTest(v3,0);
		}
		catch(IllegalArgumentException iae) {
			exThrown = true;
		}
		finally {
			assertTrue(exThrown);
		}
		
		exThrown = false;
		try {
			runXTest(v4,0);
		}
		catch(IllegalArgumentException iae) {
			exThrown = true;
		}
		finally {
			assertTrue(exThrown);
		}
		
		//test label maker exception
		exThrown =false;
		try {
			LabelHelper.makeLabel(-1, 1);
		}
		catch(IllegalArgumentException iae) {
			exThrown = true;
		}
		finally {
			assertTrue(exThrown);
		}
		
		exThrown =false;
		try {
			LabelHelper.makeLabel(0, -1);
		}
		catch(IllegalArgumentException iae) {
			exThrown = true;
		}
		finally {
			assertTrue(exThrown);
		}
		
	}
	
	@Test
	public void testGetX() {
		assertTrue(runXTest(v1,0));
		assertTrue(runXTest(v2,1));
		assertTrue(runXTest(v5,0));
		assertTrue(runXTest(v6,123451235));
	}
	
	public boolean runXTest(String vertex, int expRes) {
		return(LabelHelper.getXFromLabel(vertex) == expRes);
	}
	
	@Test
	public void testGetY() {
		assertTrue(runYTest(v1,0));
		assertTrue(runYTest(v2,1));
		assertTrue(runYTest(v5,1231234324));
		assertTrue(runYTest(v6,3));
	}
	
	public boolean runYTest(String vertex, int expRes) {
		return(LabelHelper.getYFromLabel(vertex) == expRes);
	}
	
	public void testLabelMaker() {
		assertTrue(testLabel(0,0, "0,0"));
		assertTrue(testLabel(1,5, "1,5"));
		assertTrue(testLabel(12341,1450879236, "12341,1450879236"));
	}
	
	public boolean testLabel(int x, int y, String expRes) {
		return(expRes.equals(LabelHelper.makeLabel(x, y)));
	}
	
}
