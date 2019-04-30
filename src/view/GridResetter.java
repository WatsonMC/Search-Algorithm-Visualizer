package view;

public abstract class GridResetter {
	/**
	 * Class to reset grid state at start of new search
	 * Resetting a grid state includes:
	 * - setting every distance to zero on positions
	 * - updating maxdistance to standard value
	 * - redrawing 
	 */
	
	//resets all grid positions to max distance, and changes max distance to default
	public static boolean resetGrid(GridPanel gridPanel) {
		for(GridPosition[] gridRows: gridPanel.grid) {
			for(GridPosition posn: gridRows) {
				posn.setDistance(Integer.MAX_VALUE);
			}
		}
		gridPanel.colourCtl.setMaxDistanceWithoutUpdate(ColourController.DEFAULT_MD);
		gridPanel.updateGridColours();
		gridPanel.repaint();
		return true;
	}
	
	//resets grid panel to state after construction, all positions unblocked
	//resets max distances
	//updates colours
	public static boolean resetGridFresh(GridPanel gridPanel) {
		for(GridPosition[] gridRows: gridPanel.grid) {
			for(GridPosition posn: gridRows) {
				posn.setState(GridPanel.UNBLOCKED);
			}
		}
		return(resetGrid(gridPanel));
	}
}
