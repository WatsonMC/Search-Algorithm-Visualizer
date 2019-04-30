package view;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

import controller.SAVController;
import factories.SAVControllerFactory;
import model.LabelHelper;

public class GridPanel extends JPanel{
	
	//Panel constants
	private final int WIDTH;
	private final int HEIGHT;
	private final int POSITION_SIZE = 10; //TODO update to have variable size grid
	public static final Dimension DEFAULT_SIZE = new Dimension(10, 10); 
	
	// Grid position states
	public static final int UNBLOCKED = 0;
	public static final int BLOCKED = 1;
	public static final int SOURCE = 2;
	public static final int TARGET = 3;
	
	//Grid of position objects
	protected GridPosition[][] grid;
	
	//controllers
	protected ColourController colourCtl;
	private SAVView view;
	
	//supporting classes
	private PathOverlayInterface pathOverlayInterface;
	
	//Vertex labels for grid positions 
	private String currTarget;
	private String currSource;
	

	//flag to colour grid posn's based on distance or not
	private boolean showDistance = false;
	
	/**
	 * Constructor for grid panel object
	 * @param width - number of columns of grid positions for the grid
	 * @param height - number of rows of grid positions for the grid
	 */
	public GridPanel(Integer width, Integer height) {
		this.WIDTH= width;
		this.HEIGHT = height;
		try {
			this.colourCtl = new ColourController(this);
		}
		catch(IOException ioe){
			System.out.println("IO exception on read of image icon files");
			ioe.printStackTrace();
		}
		constructGrid();
		this.setVisible(true);
	}
	
	//Constructs the grid of GridPositon objects and returns it, then updates the colours
	//of the GridPositions
	public void constructGrid() {
		this.grid = GridFactory.constructGrid(WIDTH, HEIGHT, DEFAULT_SIZE, UNBLOCKED, this, SAVControllerFactory.getInstance()); //this.controller = getInstnce()
		this.addMouseListener(SAVControllerFactory.getInstance());
		this.addKeyListener(SAVControllerFactory.getInstance());
		updateGridColours();
	}

	// Method to update all grid positions to their correct colour and type
	// Method will take into account the showDistances variable
	public boolean updateGridColours() {
			for(GridPosition[] array:grid) {
				for(GridPosition posn: array) {
					colourCtl.colourGridPosition(posn, showDistance);
				}
			}	
			repaint();
			return true;
	}

	/**
	 * Method to update distance parameters of the grid 
	 * Method assumes that supplied vertex labels exist on the grid. Method will
	 * exit early and incorrectly if this is not true.
	 * @param distances, map of string vertex values and integers to those vertices
	 * vertex labels are of the form "x,y" where x and y are the coordinates from top left 
	 * going down right of the GridPosition 
	 * @return
	 * true if update, false if exits early due to fault
	 */
	public boolean updateGrid(Map<String, Integer> distances) {
		if(distances!= null) {
			for(String vertex:distances.keySet()) {
				if(LabelHelper.getXFromLabel(vertex)== -1) {
					return false;
				}
				int x = LabelHelper.getXFromLabel(vertex);
				int y = LabelHelper.getYFromLabel(vertex);
				grid[x][y].setDistance(distances.get(vertex));	
				colourCtl.colourGridPosition(grid[x][y], showDistance);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Method to update the max distance used by the colour controller, and redraw colours to match
	 * @param maxDistance - new integer distance to be updated
	 */
	public void updateMaxDistance(int maxDistance) {
		colourCtl.setMaxDistanceWithUpdate(maxDistance);
		view.maxDistanceUpdateComplete();
	}
	
	/**
	 * Sets the state value of the given position reference to BLOCKED
	 * @param posn - "x,y" position reference, assumed to be of correct format
	 * and in the grid. Errors will occur if not
	 */
	protected void block(String posn) {
		setState(LabelHelper.getXFromLabel(posn), 
				LabelHelper.getYFromLabel(posn), 
				BLOCKED);
	}
	
	/**
	 * Sets the state value of the given position reference to UNBLOCKED
	 * @param posn - "x,y" position reference, assumed to be of correct format
	 * and in the grid. Errors will occur if not
	 */
	public void unblock(String posn) {
		setState(LabelHelper.getXFromLabel(posn), 
				LabelHelper.getYFromLabel(posn), 
				UNBLOCKED);
	}
	
	/**
	 * Method to update showing distances on the panel 
	 * @param flag - boolean value to set showDistances to.
	 * @return- previous state of the showDistances flag
	 * If showDistances is set true, the colourController will draw
	 * unblocked positions in their distance-based colour 
	 */
	public boolean showDistances(boolean flag) {
		boolean currFlag = showDistance;
		showDistance =flag;
		return currFlag;
	}

	public void setRunState(boolean flagRunning) {
		showDistances(flagRunning);
	}
 
	/**
	 * Set's source GridPosition, resets previous source.
	 * @param source - String reference
	 * - if source == null, resets assigned source to null
	 * - if source is not null, will set referenced position to source
	 * Current source is set to unblocked if not null
	 * Assumes that source is of correct form for vertex label ("x,y")
	 */
	public void setSource(String source) {
		if(currSource != null) {
			setState(LabelHelper.getXFromLabel(currSource), 
					LabelHelper.getYFromLabel(currSource), 
					UNBLOCKED);
		}
		if(source ==  null) {
			currSource = null;
			return;
		}
		setState(LabelHelper.getXFromLabel(source), 
				LabelHelper.getYFromLabel(source), 
				SOURCE);
		currSource = source;
		return;
	}
	
	/**
	 * Set's target GridPosition, resets previous target.
	 * @param target - String reference
	 * - if target == null, resets assigned target to null
	 * - if target is not null, will set referenced position to target
	 * Current target is set to unblocked if not null
	 * Assumes that target is of correct form for vertex label ("x,y")
	 */
	public void setTarget(String target) {
		if(currTarget != null) {
			setState(LabelHelper.getXFromLabel(currTarget), 
					LabelHelper.getYFromLabel(currTarget), 
					UNBLOCKED);
		}
		if(target == null) {
			currTarget = null;
			return;
		}
		setState(LabelHelper.getXFromLabel(target), 
				LabelHelper.getYFromLabel(target), 
				TARGET);
		currTarget = target;
		return;
	}
	
	//Set's the state of the given x,y co-ord to be the state and draws it
	// reutrns false if setState fails
	/**
	 * Sets the state of the position at 
	 * @param x - x co-ord of the position to be set
	 * @param y - y co-ord of the position to be set
	 * @param state - state to be set on position
	 * @return true if set state was accomplished, false if passed state is not valid
	 * This method will check state input and throw an out of bounds exception if state =/= 
	 * one of the four predefined states in GridPanel
	 * This method does not check the x,y  positions, and assumes they are present 
	 * in the grid.
	 */
	public boolean setState(int x, int y, int state) {
		if(!checkState(state)) {
			return false;
		}
		try {
			grid[x][y].setState(state);
		}
		catch(IndexOutOfBoundsException iobe) {
			iobe.printStackTrace();
			return false;
		}
		colourCtl.colourGridPosition(grid[x][y], showDistance);
		return true;
	}
	
	//DI for SAVview
	public void loadView(SAVView view) {
		this.view =view;
	}
	
	//DI for PathOverlayInterface
	public void loadPathOverlayInterface(PathOverlayInterface poi) {
		this.pathOverlayInterface = poi;
	}
	
	/**
	 * Getter method for specific positions in the grid
	 * @param xy - String reference in form "x,y"
	 * @return -  GridPosition object referenced
	 * This method will print a message to the console and return null if the referenced position does not exist
	 */
	protected GridPosition getGridPosition(String xy) {
		if(LabelHelper.getXFromLabel(xy) == -1) {
			System.out.printf("Grid position %s does not exist, error", xy);
			return null; //coords supplied do not match
		}
		return grid[LabelHelper.getXFromLabel(xy)][LabelHelper.getYFromLabel(xy)];
	}
	
	/**
	 * Getter method for the source GridPosition object
	 * @return -  GridPosition source object if source has been set, else null
	 */
	protected GridPosition getSource() {
		if(currSource!= null) {//if curr source is null, return null
			return grid[LabelHelper.getXFromLabel(currSource)][LabelHelper.getYFromLabel(currSource)];
		}
		return null;
	}
	
	/**
	 * Getter method for the target GridPosition object
	 * @return -  GridPosition target object if target has been set, else null
	 */
	protected GridPosition getTarget() {
		if(currTarget != null) {
			return grid[LabelHelper.getXFromLabel(currTarget)][LabelHelper.getYFromLabel(currTarget)];
		}
		return null;
	}

	//Resets grid coulours (removing colouring), does not change position states
	//disposes the path overlay interface pane
	//Used for maintaining previous search board state during refreshing 
	public void resetForNextSearch() {
		pathOverlayInterface.disposePathPanel();
		GridResetter.resetGrid(this);
	}

	//reset grids including changing all positions to be unblocked
	//Used for clear functionality. Will reset all position states and
	// distances
	public void resetForFreshSearch() {
		showDistances(false);
		pathOverlayInterface.disposePathPanel();
		GridResetter.resetGridFresh(this);
	}

	// checks if passed state variable is acceptable, else returns false
	private boolean checkState(int state) {
		if(state != BLOCKED &&
			state !=UNBLOCKED &&
			state != SOURCE &&
			state != TARGET) {
			return false;
		}
		return true;
	}
	
	

	

////////TESTS////////////////////////
	/**
	 * Test method to display the different icons for blocked/unblocked etc for visual checking
	 */
	public void addListenerToGrid(MouseListener e) {
		for(GridPosition[] posns: grid) {
			for(GridPosition posn: posns) {
				posn.addMouseListener(e);
			}
		}
	}
	
/////////////// Path Drawing->tsting//////////
	
public void testPathDraw() {
pathOverlayInterface.drawPath(new ArrayList<String>());
}

public void removePathPanel() {
pathOverlayInterface.disposePathPanel();
}
	
public void testBlockIcons() {
		//test blocked
		int x = 0;
		for(int i =0; i <100; i++) {
			grid[x][i].setState(BLOCKED);
			colourCtl.colourGridPosition(grid[x][i], false);
		}
		x++;
		for(int i =0; i <100; i++) {
			grid[x][i].setState(SOURCE);
			colourCtl.colourGridPosition(grid[x][i], false);
		}
		x++;
		for(int i =0; i <100; i++) {
			grid[x][i].setState(TARGET);
			colourCtl.colourGridPosition(grid[x][i], false);
		}
	}
	
	/**
	 * colours the entire board from 0-max colour scale
	 */
	public void testColourPallete() {
		for(int i =0; i <10000; i++) {
			grid[i/100][i%100].setDistance(i);
			colourCtl.colourGridPosition(grid[i/100][i%100], true);
		}
	}
	
	//Test method to return the distance of a current gridPosn
	// returns -1 if grid posn doesnt exist in grid
	public double getDistance(int x, int y) {
		try {
			return grid[x][y].getDistance();
		}
		catch(IndexOutOfBoundsException iobe) {
			iobe.printStackTrace();
			return -1;
		}
	}
	
	//Test method to return state of a current gridPosn
	// returns -1 if grid posn doesnt exist in grid
	public int getState(int x, int y) {
		try {
			return grid[x][y].getState();
		}
		catch(IndexOutOfBoundsException iobe) {
			iobe.printStackTrace();
			return -1;
		}
	}
	
	//state getter for string label input 
	public int getState(String posn) {
		return getState(LabelHelper.getXFromLabel(posn), LabelHelper.getYFromLabel(posn));
	}
	
	public String getSourceLabel() {
		return currSource;
	}
	public String getTargetLabel() {
		return currTarget;
	}

	
}
