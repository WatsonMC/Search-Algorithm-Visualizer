package model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TestAdjacencyHelper {
	@Test 
	public void testGetAdjacentVertices(){
		/**
		 * Test cases
		 * Width:
		 * - 0
		 * - Max Width(-1)
		 * - normal ->T
		 * 
		 * Height:
		 * - 0
		 * - max Height(-1)
		 * - normal ->T
		 * 
		 * general
		 * - corner
		 * - edge
		 * - middle ->T1
		 */
		String corner = "0,0";	//(0,0)
		String edge = Integer.toString(SAVModel.WIDTH-1) + ",5"; //(99,5)
		String edgeTop = "50,"  + Integer.toString(SAVModel.HEIGHT -1); //(99,5)
		String middle = "5,4"; //(5,4);
		
		//T1 test middle, (5,4)
		// (4,3)(5,3)(6,3)(4,4)(6,4)
		Set<String> result1 = new HashSet<>();
		result1.add(LabelHelper.makeLabel(4, 3));
		result1.add(LabelHelper.makeLabel(5, 3));
		result1.add(LabelHelper.makeLabel(6, 3));
		result1.add(LabelHelper.makeLabel(4, 4));
		result1.add(LabelHelper.makeLabel(6, 4));
		result1.add(LabelHelper.makeLabel(4, 5));
		result1.add(LabelHelper.makeLabel(5, 5));
		result1.add(LabelHelper.makeLabel(6, 5));
		assertTrue(result1.equals(AdjacencyHelper.getAdjacentVertices(middle, SAVModel.WIDTH, SAVModel.HEIGHT)));
		
		//T2 test corner. (0,0)
		Set<String> result2 = new HashSet<>();
		result2.add(LabelHelper.makeLabel(0, 1));
		result2.add(LabelHelper.makeLabel(1, 0));
		result2.add(LabelHelper.makeLabel(1, 1));
		assertTrue(result2.toString() + " =/= " + AdjacencyHelper.getAdjacentVertices(corner, SAVModel.WIDTH, SAVModel.HEIGHT).toString(),
				result2.equals(AdjacencyHelper.getAdjacentVertices(corner, SAVModel.WIDTH, SAVModel.HEIGHT)));

		//Test Edge (99,5)
		Set<String> result3 = new HashSet<>();
		result3.add(LabelHelper.makeLabel(SAVModel.WIDTH-1, 4));
		result3.add(LabelHelper.makeLabel(SAVModel.WIDTH-1, 6));
		result3.add(LabelHelper.makeLabel(SAVModel.WIDTH-2, 4));
		result3.add(LabelHelper.makeLabel(SAVModel.WIDTH-2, 5));
		result3.add(LabelHelper.makeLabel(SAVModel.WIDTH-2, 6));
		assertTrue(result3.toString() + " =/= " + AdjacencyHelper.getAdjacentVertices(edge, SAVModel.WIDTH, SAVModel.HEIGHT).toString(),
				result3.equals(AdjacencyHelper.getAdjacentVertices(edge, SAVModel.WIDTH, SAVModel.HEIGHT)));

		//Test edge top (50,99)
		Set<String> result4 = new HashSet<>();
		result4.add(LabelHelper.makeLabel(49, SAVModel.HEIGHT-1));
		result4.add(LabelHelper.makeLabel(51, SAVModel.HEIGHT-1));
		result4.add(LabelHelper.makeLabel(51, SAVModel.HEIGHT-2));
		result4.add(LabelHelper.makeLabel(50, SAVModel.HEIGHT-2));
		result4.add(LabelHelper.makeLabel(49, SAVModel.HEIGHT-2));
		assertTrue(result4.toString() + " =/= " + AdjacencyHelper.getAdjacentVertices(edgeTop, SAVModel.WIDTH, SAVModel.HEIGHT).toString(),
				result4.equals(AdjacencyHelper.getAdjacentVertices(edgeTop, SAVModel.WIDTH, SAVModel.HEIGHT)));
	}
	
	@Test
	public void testGetHoriziontalAdjacencies(){
		//should only get Horizontal adjacencies eg.
			// 1,1 => 0,1 2,1 1,0, 1,2
		// touching grid positions
		
		//Test general case
		String vertex = "3,3";
		Set<String> generalResult = new HashSet<>();
		generalResult.add("3,4");
		generalResult.add("3,2");
		generalResult.add("4,3");
		generalResult.add("2,3");
		
		assertEquals(generalResult, AdjacencyHelper.getHorizontalAdjacentVertices(vertex, 10, 10));
		
		//test edge case on corner
		Set<String>  edgeResult = new HashSet<>();
		edgeResult.add("2,3");
		edgeResult.add("3,2");
		assertEquals(edgeResult, AdjacencyHelper.getHorizontalAdjacentVertices(vertex, 4, 4));

		
	}

}


