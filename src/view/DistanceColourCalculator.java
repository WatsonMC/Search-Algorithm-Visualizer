package view;

import java.awt.Color;

public abstract class DistanceColourCalculator {
	/** this
	 *Class handles production of the colours required for the colour controller to 
	 *draw grid positions.
	 *
	 *This is realised through static methods which produce colours from given 
	 *positions and maxdistance values
	 */
	
	/**
	 * This method produces a colour based on the grid position source and target, and the max distance to be travvelled
	 * The colour is determined by:
	 * - The straight distance weighting 
     * 	-> A measure of how diaognally close to the target the current position is, relative to how close the source was
     *  -> percent of overall weighting is : w%sd =50 - 50*(p->t)/2*(s->t)  || w%sd = 0  iff t ==null
	 * - The max distance weighting 
	 * 	-> Maxdistance represents the known maximum travel distance of the algorithm
	 *  -> max distance overall weighting percentage is: w%md = (s->p)/maxDistance
	 * @param source - source position
	 * @param target - target position
	 * @param maxDistance -  maxDistance position
	 * @return
	 * Return a colour from max red(1,0,0)  to max green  (0,1,0)
	 */
	public static Color getDistanceColour(GridPosition source, GridPosition target, GridPosition posn, int maxDistance) {
		int totalDistance;
		if(target == null) {
			//dealing with source only
			totalDistance = 2*getMaxDistanceWeighting(posn, maxDistance);
		}
		else {
			totalDistance = getMaxDistanceWeighting(posn, maxDistance) + getStraightDistanceWeighting(source, target, posn);
		}
		if(totalDistance >100 || totalDistance <0) {
			System.out.println("Out of colour range on colour setting:");
		}
		float red;
		float green;
		if(totalDistance >= 100/2) {
			red = 1- (float)(totalDistance-100/2)/(100/2); //when distance = max distnce == 0
			return new Color(red, 1,0);
		}
		green = (float)totalDistance/(100/2);
		return new Color(1,green,0);
	}
	
	
	
	/**
	 * Method to calculate the max distance weighting given the position and the max distance
	 * If the position distance is greater than the max distance, then an illegal argument exception will be thrown
	 * @param posn
	 * @param maxDistance
	 * @return
	 * weighting in range (0-50) 
	 */
	public static int getMaxDistanceWeighting(GridPosition posn, int maxDistance) throws IllegalArgumentException {
		double posnDistance = posn.getDistance();
		//if(posnDistance>maxDistance) {throw new IllegalArgumentException("Position distance is greater than max distance");}
		//TODO better handling of maxDistance for long searches
		if(posnDistance>maxDistance) {posnDistance= maxDistance;}
		double weighting = (double)posnDistance/maxDistance;
		return (int)Math.floor(weighting*50);
	}
	
	/**
	 * Method to calc straight wait distance (diagnoal) from source to target
	 * -> A measure of how diaognally close to the target the current position is, relative to how close the source was
     *  -> percent of overall weighting is : w%sd =50 - 50*(p->t)/2*(s->t)  || w%sd = 0  iff t ==null
	 * @param source
	 * @param target
	 * @param posn 
	 * @return
	 * w%sd =50 - 50*(p->t)/2*(s->t)  
	 */
	public static int getStraightDistanceWeighting(GridPosition source, GridPosition target, GridPosition posn) {
		if(target == null) {
			throw new IllegalArgumentException("straight distance calculation attmpted on non-existant target");
		}
		int distanceToTarget = getStraightDistance(posn, target);
		int baseDistance = getStraightDistance(source, target);
		
		double weighting =   1- (double)(distanceToTarget)/(2*baseDistance);
		if(weighting >1) {weighting =1;}
		if(weighting <0) {weighting =0;}
		
		return (int)Math.floor((weighting*50));	
	}
	
	
	/**
	 * Retruns a rounded int equal to the straight distancebetween two grid positions
	 * @param posn1
	 * @param posn2
	 * @return rounded down (floor) int closest to distance between grid positions
	 */
	public static int getStraightDistance(GridPosition posn1, GridPosition posn2) {
		double deltaXSquared = Math.pow(Math.abs(posn1.x - posn2.x),2);
		double deltaYSquared = Math.pow(Math.abs(posn1.y - posn2.y),2);
		return (int)Math.pow(deltaXSquared+deltaYSquared,0.5 );
	}
	
}
