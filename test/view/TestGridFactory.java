package view;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.junit.Test;

import controller.SAVController;

public class TestGridFactory {
	//Grid factory class is responsible only for instantiating a grid
	//sets preffered sizes
	// adds mouse and keyboard listener
	// adds gridpositions to the panel
	
	@Test
	public void testConstructGrid() {
		int width = 10;
		int height = 15;
		
		JPanel testPanel = new JPanel();
		SAVController mockController = new SAVController();
		Dimension gridSize = new Dimension(10,10);
		
		GridPosition[][] grid = GridFactory.constructGrid(width, height, gridSize, GridPanel.BLOCKED,testPanel, mockController);
		
		assertTrue(grid.length == width);
		assertTrue(grid[0].length == height);
		
		// test posn states
		List<Component> testPanelComps = new LinkedList<>(Arrays.asList(testPanel.getComponents()));
		for(GridPosition[] gridLine: grid) {
			for(GridPosition posn: gridLine) {
				assertTrue(posn.getState() == GridPanel.BLOCKED);
				assertEquals(posn.getMouseListeners()[1],mockController );
				assertEquals(posn.getKeyListeners()[0],mockController );
				assertEquals(gridSize, posn.getPreferredSize());
				assertTrue(testPanelComps.contains(posn));
			}
		}
		
		
	}
	
}
