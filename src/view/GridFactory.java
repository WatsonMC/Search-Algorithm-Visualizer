package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import controller.SAVController;

public abstract class GridFactory {
	/**
	 * Creates a grid of GridPosition objects and adds them to the specified JPanel in a grid layout 
	 * @param width - total width of the grid
	 * @param height -  total height of the grid
	 * @param gridSize - dimension of GridPosition sizes
	 * @param posnState - State of the grids (See Gridpanel class definition)
	 * @param panel - JPanel to add the GridPositions to
	 * @return
	 * The array of gridPosition objects 
	 */
	public static GridPosition[][] constructGrid(int width, int height, Dimension gridSize, int posnState, JPanel panel, SAVController controller){
		GridPosition[][] grid = new GridPosition[width][height];
		GridLayout layout = new GridLayout(width,height);
		panel.setLayout(layout);
		for(int i = 0; i<width; i++) {
			for(int j = 0; j<height;j++) {
				grid[i][j] = new GridPosition(i,j);
				grid[i][j].setPreferredSize(gridSize);
				grid[i][j].setState(posnState);
				grid[i][j].setVisible(true);
				grid[i][j].addMouseListener(controller);
				grid[i][j].addKeyListener(controller);
				panel.add(grid[i][j]);
			}
		}
		return grid;
	}
}
