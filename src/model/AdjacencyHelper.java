package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AdjacencyHelper {
	/**
	 * Returns a set of strings representing vertex labels of adjacent positions for the given vertex
	 * viable adjacent vertices are all those within 1 x/y traversal distance (including diagonal)
	 * in the x range (0-maxWidth] yrange (0-maxHeight]
	 * @param vertex - vertex about which to find adjacent vertices (in vertex label form)
	 * @param maxWidth - maximum width of the grid - x>= this value is out of range
	 * @param maxHeight - maximum height of the grid - y>= this value is out of range
	 * @return
	 * Set of string labels rep vertices adjacent	
	 * 
	 */
	public static Set<String> getAdjacentVertices(String vertex, int maxWidth, int maxHeight){
		int x = LabelHelper.getXFromLabel(vertex);
		int y = LabelHelper.getYFromLabel(vertex);
		
		List<Integer> possibleX = Arrays.asList(new Integer[] {x-1,x,x+1});
		List<Integer> possibleY = Arrays.asList(new Integer[] {y-1,y,y+1});
		
		Set<String> adjacents = new HashSet<>();
		
		for(Integer newX:possibleX) {
			for(Integer newY:possibleY) {
				if(checkCoords(newX,newY,maxWidth, maxHeight)) {
					adjacents.add(LabelHelper.makeLabel(newX, newY));
				}
			}
		}
		adjacents.remove(vertex);
		return adjacents;
	}
	/**
	 * Returns a set of strings representing vertex labels of horizontal adjacent positions for the given vertex
	 * in the x range (0-maxWidth] yrange (0-maxHeight]
	 * @param vertex - vertex about which to find adjacent vertices (in vertex label form)
	 * @param maxWidth - maximum width of the grid - x>= this value is out of range
	 * @param maxHeight - maximum height of the grid - y>= this value is out of range
	 * @return
	 * Set of string labels rep vertices adjacent	
	 * 
	 */
	public static Set<String> getHorizontalAdjacentVertices(String vertex, int maxWidth, int maxHeight){
		int x = LabelHelper.getXFromLabel(vertex);
		int y = LabelHelper.getYFromLabel(vertex);
		//(x,y-1), (x-1,y) (x+1,y) ,(x,y+1);
		Set<String> adjacents = new HashSet<>();
		if(checkCoords(x,y-1,maxWidth, maxHeight)) {
			adjacents.add(LabelHelper.makeLabel(x, y-1));}
		if(checkCoords(x-1,y,maxWidth, maxHeight)) {
			adjacents.add(LabelHelper.makeLabel(x-1, y));}
		if(checkCoords(x+1,y,maxWidth, maxHeight)) {
			adjacents.add(LabelHelper.makeLabel(x+1, y));}
		if(checkCoords(x,y+1,maxWidth, maxHeight)) {
			adjacents.add(LabelHelper.makeLabel(x, y+1));}
		return adjacents;
	}
	
	
	/**
	 * Cheks if given xy is in range and valid as a vertex coord set
	 * @param x
	 * @param y
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static boolean checkCoords(int x, int y, int maxWidth, int maxHeight) {
		if((x<0 || x>=maxWidth) || (y<0||y>=maxHeight)) {
			return false;
		}
		return true;
	}
}
