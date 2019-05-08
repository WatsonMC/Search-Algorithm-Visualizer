package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * @author      Malcolm Watson <weatsonmc123@gmail.com>
 * Class controls graphical alteration of GridPosition objects.
 * 4 different images are used:
 * - Blocked image
 * - Target image 
 * - Source image
 * - Default image
 * 
 * ColourController determines which image should be used, and colours (using the DistanceColourCalculator) default images to
 * display distances of positions during the search.
 */


public class ColourController {

	/**
	 * Class to control colouring of the GridPosition objects in the gridpanel
	 * Main purpose of the object is to recieve gridPosition objects, work out what 
	 * colour they should be and colour them.
	 * 
	 * COLOURING RULES:
	 * If position is source, target or blocked, then it is always shown in same way
	 * If position is unblocked and showDistances is true, then the controller
	 * should colour the posn based on it's distance from the source
	 * 
	 * if posn is source, posn should be loaded with the source image from resoucres
	 * if posn  is target, posn  should be loaded with the target image from resources
	 */
	
	/**
	 * Colour selection: 
	 * Color scales from R255 G0, to R255 G255, to R0 G255 for a total of 510 possible colours
	 * 
	 */
	
	// Image icons
	private  Image SOURCE_ICON;
	private  Image TARGET_ICON;
	private  Image BLOCKED_ICON;
	
	// GridPanel dependency
	private GridPanel grid;
	
	//Max distance values used in colour calculations
	private int maxDistance;
	public static final int DEFAULT_MD = 15000;
	
	//Default colour for grid position
	public static final Color DEFAULT_COLOUR = Color.LIGHT_GRAY;
	public static Image DEFAULT_IMAGE = null;
	
	/**
	 * COnstructor for colour controller from grid dependency injection
	 * @param grid -> the grid which this colour controller is controlling. Dependency is used only for 
	 * @throws IOException
	 */
	public ColourController(GridPanel grid) throws IOException{
		initImageIcons();
		this.grid =grid;
	}
	
	// test
	public ColourController() throws IOException{
		initImageIcons();
	}
	
	public void initImageIcons() throws IOException{
		SOURCE_ICON = getScaledImage(ImageIO.read(getClass().getResource("/source.png")),GridPanel.DEFAULT_SIZE.width, GridPanel.DEFAULT_SIZE.height);
		TARGET_ICON = getScaledImage(ImageIO.read(getClass().getResource("/target.png")),GridPanel.DEFAULT_SIZE.width, GridPanel.DEFAULT_SIZE.height);
		BLOCKED_ICON = getScaledImage(ImageIO.read(getClass().getResource("/blocked.png")),GridPanel.DEFAULT_SIZE.width, GridPanel.DEFAULT_SIZE.height);
		maxDistance =DEFAULT_MD;
	}
	
	/**
	 * Method to take in a GridPosition object and colour it based on it's state
	 * @param posn
	 * @param showDistance indicates whether the controller should colour based on distance, or just state
	 * @return true of succesfully coloured
	 */
	public boolean colourGridPosition(GridPosition posn, boolean showDistance) {
		if(isUnblocked(posn)) {
			//handleunblocked position;
			drawUnblocked(posn, showDistance);
		}
		else if(posn.getState() == GridPanel.BLOCKED) {
			//Draw blocked
			drawBlocked(posn);
		}
		else if(posn.getState() == GridPanel.TARGET) {
			//draw target/
			drawTarget(posn);
		}
		else {
			//draw source
			drawSource(posn);
		}
		return true;
	}
	
	// Method to set the posn with the target image icon
	public void drawTarget(GridPosition posn) {
		posn.setImage(TARGET_ICON);
	}
	
	//method to set the posn with the source imageicon
	public void drawSource(GridPosition posn) {
		posn.setImage(SOURCE_ICON);
	}
	
	//method to set the posn with the blocked colour
	public void drawBlocked(GridPosition posn) {
		posn.setImage(BLOCKED_ICON);
	}
	
	//method to determine colour and set for an unblocked posn
	public void drawUnblocked(GridPosition posn, boolean showDistance) {
		if(!showDistance) {posn.setColour(DEFAULT_COLOUR);}
		else if (posn.getDistance() != Integer.MAX_VALUE){	// max value is the indicator of not yet visited
			posn.setColour(DistanceColourCalculator.getDistanceColour(
							grid.getSource(),
							grid.getTarget(),
							posn,this.maxDistance));
				
		}
		//TODO if leaving unvisited notes light grey doesn't look good, set to black or something
	}
	
	/**
	 * Method for setting maxDistance for colourscale
	 * Should only be used when the SA has finished
	 * @param maxDistance
	 */
	public void setMaxDistanceWithUpdate(int maxDistance) {
		this.maxDistance = maxDistance;
		grid.updateGridColours();
		//grid.maxDistanceUpdateComplete(); not necessary, updateMaxDistance method in GridPanel already calls
		
		//this.maxDistance = getStraightDistance(grid.getSource(), grid.getTarget());
	}
	
	//Update the max distance in background, not when task is running (eg. on reset)
	public void setMaxDistanceWithoutUpdate(int maxDistance) {
		this.maxDistance = maxDistance;
		grid.updateGridColours();
		//this.maxDistance = getStraightDistance(grid.getSource(), grid.getTarget());
	}
	
	
	/**
	 * Helper method to check if a gridposition is in the unblocked state or not
	 */
	private boolean isUnblocked(GridPosition posn) {
		if(posn.getState() == GridPanel.UNBLOCKED) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Returns the required colour for a given grid position based on its distacne
	 * Colour is decided by the logic documented at the start of this method
	 * @param posn
	 * @maxDistance maximum distance to target from source. nominally 5000, but ,if algorithm is quick enough
	 * Can be set to actual distance to target when this is passed through.
	 * @return
	 */
	private Color getDistanceColor1(GridPosition posn, int maxDistance) {
		int distance = posn.getDistance();
		float red;
		float green;
		if(distance >= maxDistance/2) {
			red = 1- (float)(distance-maxDistance/2)/(maxDistance/2); //when distance = max distnce == 0
			return new Color(red, 1,0);
		}
		green = (float)distance/(maxDistance/2);
		return new Color(1,green,0);
	}
	
	//Gets colour based on:
	//50% on delta between straight distance from posn to target vs from 
	// source to target. Scales fomr 0-50, 0 = dist = 2x source_targ dist
	// 50 = 0
	//50% based on difference between travelled distance and max distance
	// 0 = 0 distance, 50 = max distance
	//100 colour scale (full green) reached when:
	//- dist = maxDistance
	//- straight line dist = 0
	public Color getDistanceColor3(GridPosition posn, int maxDistance) {
		int weightStraight = getStraightDistanceWeighting(posn);
		int weightMaxDist = getMaxDistanceWeight(posn, maxDistance);
		//change to only max distance weighting if we are just searching whole graph
		if(grid.getTarget() == null) {
			weightMaxDist *=2;
		}
		
		int distance = weightStraight + weightMaxDist;
		if(weightMaxDist>50 || weightStraight>50) {
			System.out.println("Out of colour range on colour setting");
		}
		

		
		float red;
		float green;
		if(distance >= 100/2) {
			red = 1- (float)(distance-100/2)/(100/2); //when distance = max distnce == 0
			return new Color(red, 1,0);
		}
		green = (float)distance/(100/2);
		return new Color(1,green,0);
	}
	
	//returns straight distance weighting for getDistanceColour 3 methodology
	public int getStraightDistanceWeighting(GridPosition posn) {
		if(grid.getTarget() == null) {return 0;}	// since if we have no target, discount
		int sourceTargetStraightDistance = getStraightDistance(grid.getSource(), grid.getTarget());
		int posnTargetStraightDistance =  getStraightDistance(posn, grid.getTarget());
		double weighting =   1- (double)(posnTargetStraightDistance)/(2*sourceTargetStraightDistance);
		if(weighting>1) {weighting = 1;}
		if(weighting<0) {weighting = 0;}
		return (int)Math.floor((weighting*50));
	}
	
	//returns weighting for distance based on max distannce
	//Assumes that given posn should never have a distance greater than max
	public int getMaxDistanceWeight(GridPosition posn, int maxDistance) {
		double distance = posn.getDistance();
		double weighting = (double)distance/maxDistance;
		if(distance>maxDistance) {	
			return 50;	//TODO get rid of the Tolkein wizadry of magic numbers here
		}
		return (int)Math.floor((weighting*50));
	}
	
	//Trial routine to use the absolute distance to target as the 
	private Color getDistanceColor(GridPosition posn, int maxDistance) {
		int distance = getStraightDistance(posn, grid.getTarget());
		float red;
		float green;
		if(distance >= maxDistance/2) {
			red = 1- (float)(distance-maxDistance/2)/(maxDistance/2); //when distance = max distnce == 0
			return new Color(red, 1,0);
		}
		green = (float)distance/(maxDistance/2);
		return new Color(1,green,0);
	}
	
	
	// from: https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	//TODO workout how the fuck this works
	private ImageIcon getScaledImageIcon(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return new ImageIcon(resizedImg);
	}
	
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	/**
	 * Method to create an image of a rectangle of the set colour and return the icon for use in JBUtton 
	 * @param colour
	 * @param w
	 * @param h
	 * @return ImageIcon to assign to the gridposition/JButton
	 */
	private ImageIcon getDrawnRectangle(Color colour, int w, int h) {
		BufferedImage rectImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2  = rectImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //???
		g2.setColor(colour);
		g2.fillRect(0,0, w, h);
		g2.dispose();
		
		return(new ImageIcon(rectImage));
	}
	
	
	public int getStraightDistance(GridPosition posnOne, GridPosition posnTwo) {
		double deltaXSquared = Math.pow(Math.abs(posnOne.x - posnTwo.x),2);
		double deltaYSquared = Math.pow(Math.abs(posnOne.y - posnTwo.y),2);
		return (int)Math.pow(deltaXSquared+deltaYSquared,0.5 );
	}
	
	//TEST GETTERS:
	public Image getIcon(int state) {
		if(state == GridPanel.BLOCKED) {
			return this.BLOCKED_ICON;
		}
		if(state == GridPanel.SOURCE) {
			return this.SOURCE_ICON;
		}
		if(state == GridPanel.TARGET) {
			return this.TARGET_ICON;
		}
		return null;
	}
	
	public int getMaxDistance() {
		return this.maxDistance;
	}
	
}
