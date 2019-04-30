package view;


import javax.swing.BorderFactory;
import javax.swing.JButton;

public class GridPosition extends JButton{
//custom square component.
	/**
	 * RI:
	 * Represents a position on the grid that will be searched with the algorithm
	 * Reps:
	 * int x,y =  integer values greater than 0, the co-ordinates of the grid posn
	 * int state = indicator of state of the position -> whether it is blocked, unblocked,the source or the target
	 * int distance = distance from source to this grid posn, distnace = MAX_VALUE will be drawn as unblocked square
	 * 
	 * 
	 */
	
	
	public int x;
	public int y;
	private int distance;
	
	private int state = GridPanel.UNBLOCKED;
	
	
	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.setBorder(BorderFactory.createEmptyBorder()); //maybe not necessary since all buttons are images
		this.distance  =Integer.MAX_VALUE;
	}
	
	//Method to set the state of the grid position
	public boolean setState(int state) {
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
	
	//GETTERS
	public int getState() {
		return this.state;
	}
	public int getDistance() {
		return this.distance;
	}
	public void setDistance(int newDist) {
		this.distance = newDist;
		
	}
	public int getXCoord() {
		return this.x;
	}
	public int getYCoord() {
		return this.y;
	}
}
