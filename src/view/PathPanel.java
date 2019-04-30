package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class PathPanel extends JPanel {
	public String string;	//TODO remove, testing
	public String start;	//TODO remove, testing
	public String finish; //TODO remove, testing
	public JPanel grid;
	private PathOverlayInterface poi;
	
	
	private List<String> pathToTarget;	//path to be drawn
	private List<String> drawnPathToTarget; //parts of the path already drawn
	private boolean pathLoaded =false;
	private boolean pathDrawn = false;
	private final int posnWidth = GridPanel.DEFAULT_SIZE.width;
	private final int nodeDiam = posnWidth/2;
	
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (string!=null) {
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 150, 150);
		}
		
		//drawLinkGraphics(start,finish,g);
		
		if(pathLoaded) {
			drawPath(g);
			//drawLinkGraphics(start, finish, g);
		}
	}
	
	//Constructor for the panel
	public PathPanel(JPanel grid, PathOverlayInterface poi) {
		this.grid = grid;
		this.setBackground(new Color(0,0,0,0));
		this.poi = poi;
		
	}
	
	public void setStartFinish(String start, String finish) {
		this.start = start;
		this.finish = finish;
	}
	
	/**
	 * Loads the path for the panel to draw
	 * When called, will automatically begin drawing process for path
	 * @param path
	 * List of string labels for grid positions
	 */
	public void loadPathAndDraw(List<String> path) {
		this.pathToTarget = path;
		this.drawnPathToTarget = new ArrayList<>();
		//this.start = "0,0";
		//this.finish = "0,0";
		pathLoaded = true;
		drawPath();
	}
	
	/**
	 * Method to draw the path piecemeal, sleeping the EDT between updates
	 * THIS METHOD requies that button activaiton is disabled prior to call,
	 * or else events generated during its runtime(= pathLength*20ms) will cause errors 
	 */
	public void drawPath() {
		for(int i =pathToTarget.size() -1; i>0;i--) {
			drawnPathToTarget.add(pathToTarget.get(i));
			drawnPathToTarget.add(pathToTarget.get(i-1));
			paintComponent(this.getGraphics());
	
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		revalidate();
		repaint();
		pathDrawn = true;
		poi.pathDrawComplete();
	}
	
	
	/**
	 * Draws the path as specified in pathToTarget field
	 * @param g
	 */
	public void drawPath(Graphics g) {
			for( int i = 0; i< drawnPathToTarget.size()-1; i++) {
				start = drawnPathToTarget.get(i);
				finish = drawnPathToTarget.get(i+1);
				drawLinkGraphics(drawnPathToTarget.get(i), drawnPathToTarget.get(i+1), g);
			}
	}
	
	/**
	 * Method to drawn the link graphic between two nodes.
	 * Link graphic is a white circle for the node, and a black line between nodes
	 * @param start
	 * @param finish
	 * @param g
	 */
	public void drawLinkGraphics(String start, String finish, Graphics g) {
		GridPosition startPos = ((GridPanel)grid).getGridPosition(start);
		GridPosition finPos = ((GridPanel)grid).getGridPosition(finish);
		
		drawLine(startPos, finPos, g);
		drawNode(startPos,g);
		drawNode(finPos, g);
		
	}
	
	/**
	 * Method to draw node points at each travelled position
	 * Node point is ablack circle with white fill
	 * @param posn
	 * @param g
	 */
	public void drawNode(GridPosition posn, Graphics g) {
		Color outline = Color.BLACK;
		Color fill = Color.WHITE;
		
		double xPos = posn.getX() +(double)posnWidth/2 - (double)nodeDiam/2;
		double yPos = posn.getY() +(double)posnWidth/2 - (double)nodeDiam/2;
		g.setColor(fill);
		g.fillOval((int)xPos, (int)yPos, nodeDiam, nodeDiam);
	}
	
	public boolean drawComplete() {
		return this.pathDrawn;
	}
	
	/**
	 * Method to draw a line between the two grid positions
	 * Line is black in colour, and drawn between centers of the positions
	 * @param startPos
	 * @param finPos
	 * @param g
	 */
	public void drawLine(GridPosition startPos, GridPosition finPos,Graphics g) {
		int startX = startPos.getX()+posnWidth/2;
		int startY = startPos.getY()+ posnWidth/2;
		int finX = finPos.getX() + posnWidth/2;
		int finY = finPos.getY() + posnWidth/2;
		
		g.setColor(Color.BLACK);
		g.drawLine(startX, startY, finX, finY);
	}
	
	// Method to handle resetting panel after draw is complete
	//If path is completed drawing, will call full reset, clearing grid and deleting path panel
	public void reset() {
		if(pathDrawn) {
			poi.handleReset();
		}
	}

}
