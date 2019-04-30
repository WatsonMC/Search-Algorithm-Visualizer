package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import graph.Graph;
import junit.framework.TestCase;
import view.SAVView;

class viewMock extends SAVView{
	public static boolean setBlockCalled = false;
	public static boolean unsetBlockCalled = false;
	public static String source;
	public static String target;
	@Override 
	public void updateView(Map<String,Integer> distances) {
		System.out.println("view update called successfully");
	}
	
	@Override 
	public void block(String vertex) {
		setBlockCalled = true;
	}
	
	@Override 
	public void unblock(String vertex) {
		unsetBlockCalled = true;
	}
	
	@Override
	public void updateSource(String src) {
		source = src;
	}
	
	@Override
	public void updateTarget(String tgt) {
		target = tgt;
	}
	
}

public class ModelTest  extends TestCase{
	private static viewMock view;
	private static boolean setBlockCalled;
	private static boolean unsetBlockCalled;
	
	@Override
	public void setUp() throws Exception{
		view = new viewMock();
	}
	
	@Test
	public void testUpdateSTasNeeded() {
		//Should update source and target to null if necessary
		//Only necessary if current vertex being changed (passed in call)
		// is either source or target
		
		SAVModel model = new SAVModel();
		model.setView(view);
		model.init();
		model.setSource(1, 1);
		model.updateSourceTargetIfNeeded(1, 0);
		assertTrue(model.getSource().equals(LabelHelper.makeLabel(1, 1)));
		model.updateSourceTargetIfNeeded(1, 1);
		assertTrue(model.getSource() == null);
		
		model.setTarget(2, 2);
		model.updateSourceTargetIfNeeded(1, 0);
		assertTrue(model.getTarget().equals(LabelHelper.makeLabel(2, 2)));
		model.updateSourceTargetIfNeeded(2, 2);
		assertTrue(model.getTarget() == null);
	}
	
	
	@Test
	public void testBlock() {
		SAVModel sav = new SAVModel(); 
		sav.setView(view);
		sav.init();//now have our graph object
		
		//test removing corner
		//test removing middle 
		//test removing edge
		//AFter remove - test that edges are all 0
		String corner = "0,0";	//(0,0)
		String edge = Integer.toString(SAVModel.WIDTH-1) + ",5"; //(99,5)
		String edgeTop = "50,"  + Integer.toString(SAVModel.HEIGHT -1); //(50,99)
		String middle = "5,4"; //(5,4);
		
		sav.setBlock(0,0);
		assertTrue(sav.getGraph().vertices().contains(corner));
		assertTrue(testPositionAdjacents(sav.getGraph(), corner, true));
		sav.setBlock(99,5);
		assertTrue(sav.getGraph().vertices().contains(edge));
		assertTrue(testPositionAdjacents(sav.getGraph(), edge, true));
		sav.setBlock(50,99);
		assertTrue(sav.getGraph().vertices().contains(edgeTop));
		assertTrue(testPositionAdjacents(sav.getGraph(), edgeTop, true));
		sav.setBlock(5,4);
		assertTrue(sav.getGraph().vertices().contains(middle));
		assertTrue(testPositionAdjacents(sav.getGraph(), middle, true));
		
		//Tests2 :
		//Set block called on source
		//Set block called on target
		String label = LabelHelper.makeLabel(1, 1);
		sav.setSource(1, 1);
		sav.setBlock(1, 1);
		assertTrue(sav.getGraph().vertices().contains(label));
		assertTrue(testPositionAdjacents(sav.getGraph(), label, true));
		assertTrue(sav.getSource() == null);
		assertTrue(viewMock.source== null);
		
		//test on target
		sav.setTarget(1, 1);
		sav.setBlock(1, 1);
		assertTrue(sav.getGraph().vertices().contains(label));
		assertTrue(testPositionAdjacents(sav.getGraph(), label, true));
		assertTrue(sav.getTarget() == null);
		assertTrue(viewMock.target== null);
	}
	
	
	private boolean testPositionAdjacents(Graph<String> graph, String position, Boolean blocked) {
		//tests wheter the acjacent positions of a given position have correct edge settings
		//If blocked is true, edges should be  non existant
		//if blocked is false, edges should exist
		
		Set<String> adjacents = AdjacencyHelper.getAdjacentVertices(position, 100, 100);
		if(blocked) {
			for(String vertex:adjacents) {
				if(graph.set(position, vertex, 2) != 0) {
					return false;
				}
			}
			return true;
		}
		else {
			for(String vertex:adjacents) {
				if(graph.set(position, vertex, 2) == 0) {
					return false;
				}
			}
			return true;
		}
	}
	
	@Test
	public void testUnblock() {
		SAVModel sav = new SAVModel(); 
		sav.setView(view);
		sav.init();//now have our graph object

		//test removing block on corner
		//test removing block on edge
		//test removing block in middle
		// test removing block on extremities of x and y (0, max)
		String corner = "0,0";	//(0,0)
		String edge = Integer.toString(SAVModel.WIDTH-1) + ",5"; //(99,5)
		String edgeTop = "50,"  + Integer.toString(SAVModel.HEIGHT -1); //(50,99)
		String middle = "5,4"; //(5,4);
		sav.setBlock(0,0);
		sav.setBlock(99,5);
		sav.setBlock(50,99);
		sav.setBlock(5,4);
		
		sav.unsetBlock(0,0);
		assertTrue(sav.getGraph().vertices().contains(corner));
 		assertEquals(sav.getGraph().targets(corner),getExpectedMap(corner));
		
		sav.unsetBlock(99,5);
		assertTrue(sav.getGraph().vertices().contains(edge));
 		assertEquals(sav.getGraph().targets(edge),getExpectedMap(edge));
		
		sav.unsetBlock(50,99);
		assertTrue(sav.getGraph().vertices().contains(edgeTop));
		assertEquals(sav.getGraph().targets(edgeTop),getExpectedMap(edgeTop));
		
		sav.unsetBlock(5,4);
		assertTrue(sav.getGraph().vertices().contains(middle));
		assertEquals(sav.getGraph().targets(middle),getExpectedMap(middle));
		
		//Test unblock on source target
		String label = LabelHelper.makeLabel(1, 1);
		sav.setSource(1, 1);
		sav.unsetBlock(1, 1);
		assertTrue(sav.getGraph().vertices().contains(label));
		assertTrue(testPositionAdjacents(sav.getGraph(), label, false));
		assertTrue(sav.getSource() == null);
		assertTrue(viewMock.source== null);
		
		//test on target
		sav.setTarget(1, 1);
		sav.unsetBlock(1, 1);
		assertTrue(sav.getGraph().vertices().contains(label));
		assertTrue(testPositionAdjacents(sav.getGraph(), label, false));
		assertTrue(sav.getTarget() == null);
		assertTrue(viewMock.target== null);
	}	
	
	private Map<String,Integer> getExpectedMap(String vertex){
		Map<String, Integer> resultMap = new HashMap<>();
		Set<String> horizontalAdjacents = AdjacencyHelper.getHorizontalAdjacentVertices(vertex, 100, 100);
		for(String adjVtx: AdjacencyHelper.getAdjacentVertices(vertex, 100, 100)) {
			if(horizontalAdjacents.contains(adjVtx)) {
				resultMap.put(adjVtx, SAVModel.DEFAULT_DISTANCE_STRAIGHT);
			}
			else {
				resultMap.put(adjVtx, SAVModel.DEFAULT_DISTANCE_DIAGONAL);
			}
		}
		return resultMap;	
	}
	
	@Test
	public void testInit() {
		//Init is responsible for setting all model RI's to null references,
		//creating the new empty graph
		//- Creating vertices
		//- Creating edges
		
		//Test with Width/height 10
		// test with width/height = 100
		
		SAVModel model1 = new SAVModel();
		model1.setView(view);
		model1.init(10, 10);
		assertTrue(model1.getSource() == null);
		assertTrue(model1.getTarget () == null);
		try {
				assertTrue(true);
			}
		catch(NullPointerException noe) {
			assertTrue(true);
		}
		
		Graph<String> model1Gr = model1.getGraph();
		//check that all required vertices are present
		assertEquals(100, model1Gr.vertices().size());
		assertTrue(model1Gr.vertices().contains("5,5"));
		assertTrue(model1Gr.vertices().contains("0,9"));
		assertTrue(model1Gr.vertices().contains("9,0"));
		assertTrue(model1Gr.vertices().contains("9,9"));
		assertTrue(model1Gr.vertices().contains("0,0"));
		
		//Cyeck that diagonal and horizontal weights are correct
		assertEquals(SAVModel.DEFAULT_DISTANCE_STRAIGHT, model1Gr.set("0,0", "1,0", 1));
		assertEquals(SAVModel.DEFAULT_DISTANCE_DIAGONAL, model1Gr.set("8,8", "9,9", 1));
		
	}
	
	@Test 
	public void testSetSource() {
		/**
		 * If x,y  = null, source  =  null
		 * If new source = old source, set as null
		 * else set as new source
		 */
		
		SAVModel model = new SAVModel();
		model.setView(view);
		model.init();
		
		assertTrue(model.getSource() == null);
		model.setSource(1, 1);
		assertTrue(model.getSource().equals(LabelHelper.makeLabel(1, 1)));
		model.setSource(1, 1);		
		assertTrue(model.getSource() == null);
		model.setSource(1, 1);	
		assertTrue(model.getSource().equals(LabelHelper.makeLabel(1, 1)));
		model.setSource(null, null);		
		assertTrue(model.getSource() == null);
	}	
	
	@Test
	public void testSetSpeed() {
		SAVModel model = new SAVModel();
		model.setView(view);
		model.init();
		
		assertTrue(model.getSpeed() == 10);
		model.setSpeed(15);
		assertTrue(model.getSpeed() == 15);
		boolean exceptionThrown = false;
		try {
			model.setSpeed(-1);
		}
		catch(IllegalArgumentException iae) {
			exceptionThrown = true;
		}
		finally{
			assertTrue(exceptionThrown);
		}
	}
	
	@Test 
	public void testSetTarget() {
		/**
		 * If x,y  = null, target  =  null
		 * If new target = old target, set as null
		 * else set as new source
		 */
		
		SAVModel model = new SAVModel();
		model.setView(view);
		model.init();
		
		assertTrue(model.getTarget() == null);
		model.setTarget(1, 1);
		assertTrue(model.getTarget().equals(LabelHelper.makeLabel(1, 1)));
		model.setTarget(1, 1);		
		assertTrue(model.getTarget() == null);
		model.setTarget(1, 1);	
		assertTrue(model.getTarget().equals(LabelHelper.makeLabel(1, 1)));
		model.setTarget(null, null);		
		assertTrue(model.getTarget() == null);
	}
}
