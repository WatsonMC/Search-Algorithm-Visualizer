package model;

public abstract class LabelHelper {
	private static final String VERTEX_REGEX = "[0-9]+,{1}[0-9]+";
	
	/**
	 * Returns the x coordinate of a position based on it's label
	 * @param vertex - string label in from of "x,y", where x and y are 0-9
	 * Any string not of this form will be rejected. See vertexRegex for details
	 * @return
	 * int of the X co-ord
	 */
	public static int getXFromLabel(String vertex) {
		if(!vertex.matches(VERTEX_REGEX)){
			throw new IllegalArgumentException("Illegal argument to label helper: " + vertex);
		}
		return Integer.parseInt(vertex.split(",")[0]);
	}
	
	/**
	 * Returns the y coordinate of a position based on it's label
	 * @param vertex - string label in from of "x,y", where x and y are 0-9
	 * Any string not of this form will be rejected. See vertexRegex for details
	 * @return
	 * int of the Y co-ord
	 */
	public static int getYFromLabel(String vertex) {
		if(!vertex.matches(VERTEX_REGEX)){
			throw new IllegalArgumentException("Illegal argument to label helper: " +  vertex);
		}
		return Integer.parseInt(vertex.split(",")[1]);
	}
	
	/**
	 * Makes a string vertex label out of the given x,y coords
	 * @param x - >= 0
	 * @param y - >=0 
	 * @return new String("x,y");
	 */
	public static String makeLabel(int x, int y) {
		if(x<0 || y<0) {
			throw new IllegalArgumentException("less-than 0 coord value passed to makeLabel(): " + x  + " " + y);
		}
		return new String(Integer.toString(x)+ ","+ Integer.toString(y));
	}
}
