package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class GridPositionPanel extends JPanel{
			
	/**
	 * Existing functions of the gridPosition
	 * Reps:
	 * - X, Y -> Co-ords of the grid position.
	 * - Distance -> distance of gridPosition from the source
	 * - State -> state of the gridPosition, whether it is blocked, unblocked,the source or the target
	 * NEW
	 * - Image	-> image to paint if not unblocked
	 * - Colour -> colour do paint
	 * 
	 * 
	 * Functions:
	 * - Set get state
	 * - set get distance
	 * - get X,Y Coords
	 * - setColour
	 * - setImage, get Image
	 *
	 */
	
	public int x;
	public int y;
	private int width= 0;
	private int height  = 0;
	private int distance;
	
	private int state = GridPanel.UNBLOCKED;
	
	private Color colour = ColourController.DEFAULT_COLOUR;
	
	private Image img = ColourController.DEFAULT_IMAGE; //blocked image
	
	//Constructor for the gridposition,
	public GridPositionPanel(int x, int y) {
		
	}
	
	//Paints the gridposition with the colour if it is unblocked,
	//else paints as an image
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.state == GridPanel.UNBLOCKED) {
			g.setColor(colour);
			g.fillRect(0,0, width,height);
			g.dispose();
		}
		else {
			g.drawImage(img,0,0,null);
		}
		
	}
	
	//Method to set the state of the grid position
	public boolean setState(int state) {
		if(this.width == 0) {
			this.width = this.getPreferredSize().width;
			this.height = this.getPreferredSize().height;
		}
		int prevState = this.state;
		this.state = state;
		if(checkRep()) {
			return true;
		}
		else {
			this.state=prevState;
			return false;
		}	
	}
	
	//getter for state
	public int getState() {
		return this.state;
	}
	
	//setter for distance
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	//getter for distance
	public int getDistance(int distance) {
		return this.distance;
	}
	
	//Setter for colour
	public void setColour(Color colour) {
		this.colour = colour;
		repaint();
	}
	
	//setter for image
	public void setImage(Image img){
		this.img = img;
		repaint();
	}
	
	//getter for image
	public Image getImage() {
		return img;
	}
	
	
	private boolean checkRep() {
		if(x <0 ||y<0) {
			return false;
		}
		if(state!=GridPanel.BLOCKED
				&&state != GridPanel.SOURCE
					&&state != GridPanel.TARGET
						&&state != GridPanel.UNBLOCKED) {
			return false;
		}
		return true;
	}
	/**
	 * ColourController function:
	 * Rep:
	 * Image Icons for source,target,default,blocked
	 * gridPanel reference
	 * maxDistance value + default
	 * 
	 * Relvant functions:
	 * -------
	 * colourGridPosition:
	 * 	-Takes in boolean and gridPOsition
	 *  -Calls drawblocked/unblocked/target etc on the posn
	 *  NEW FUNCTION:
	 *  - takes in boolean and GridPosition
	 *  - Sets colour of grid Posn if showDistances and unblocked
	 *  - else sets image of panel to relevant image
	 *  - relies on gridPanel itself to redraw
	 *  
	 *  DrawTarget, DrawSource, etc. superseded
	 *  -----
	 *  drawUnblocked(posn, showDistance):
	 *  - if showing distances, and distance is not maxValue
	 *  - Colour grid acording to distacne
	 *  
	 *  Function does not change, but will call setColour instead of setIcon
	 *  
	 *  
	 *  ------------
	 *  
	 *  
	 *  
	 * 	
	 * 
	 * 
	 */
}
