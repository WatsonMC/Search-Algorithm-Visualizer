package view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import controller.PathPanelListener;

public class PathOverlayInterface {
	/**
	 * What this class does
	 * - Holds the gridPanel and the pathPanel objects so that pathPanel can be overlayed on the grid
	 * - Disposes of the path panel (by referne = null) when reset
	 * - Controls calls to pathDrawer to overlay path on panel
	 * 
	 * Needed references:
	 * - gridPanel to ask for gridPositions and to hold panel
	 * - PathDrawer object
	 * - Maybe view too
	 * 
	 * Reference by:
	 * - GridPanel
	 * - SAVView
	 */
	
	
	private JPanel gridPanel;
	private PathPanel pathPanel;
	private JPanel holdingPanel;
	
	private SAVView view;
	
	public PathOverlayInterface(SAVView view) {
		this.view = view;
		this.holdingPanel = new JPanel();
		holdingPanel.setLayout(new OverlayLayout(holdingPanel));
		
	}
	
	//Load the gridpanel to the object during initilization
	public void loadGrid(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
		gridPanel.loadPathOverlayInterface(this);
		holdingPanel.add(gridPanel);
		
	}
	
	public void addPanel(JFrame frame) {
		frame.add(holdingPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Method to begin draw of path. Loads the path to the path panel
	 * @param pathToTarget
	 */
	public void drawPath(List<String> pathToTarget) {
		initPathPanel();
		List<String> testpath = new ArrayList<>();
		testpath.add("0,0");
		testpath.add("1,0");
		testpath.add("1,1");
		testpath.add("1,2");
		testpath.add("1,3");
		testpath.add("1,4");
		testpath.add("2,5");
		testpath.add("3,6");
		testpath.add("23,61");
		testpath.add("15,99");
		testpath.add("99,99");
		((PathPanel)pathPanel).loadPathAndDraw(pathToTarget);
	}
	
	public void pathDrawComplete() {
		view.pathDrawComplete();
	}
	
	//Initialization method for path panel. Called each time apath is to be drawn
	public void initPathPanel() {
		pathPanel = new PathPanel(gridPanel, this);
		pathPanel.addMouseListener(new PathPanelListener());
		pathPanel.setSize(gridPanel.getSize());
		pathPanel.setLocation(0,0);
		pathPanel.setVisible(true);
		holdingPanel.add(pathPanel);
		System.out.println(holdingPanel.getComponentZOrder(gridPanel));
		holdingPanel.setComponentZOrder(gridPanel,1);
		holdingPanel.setComponentZOrder(pathPanel,0);
		pathPanel.requestFocus();
		holdingPanel.revalidate();
		holdingPanel.repaint();
	}
	
	
	public void testDrawOnPanel() {
		((PathPanel)pathPanel).string = "test";
		pathPanel.repaint();
		pathPanel.revalidate();
	}
	
	
	public void disposePathPanel() {
		if(pathPanel!= null&&pathPanel.drawComplete()) {
			holdingPanel.remove(pathPanel);
			pathPanel = null;
			holdingPanel.revalidate();
			holdingPanel.repaint();
			gridPanel.requestFocus();
		}
	}
	
	/////////Test///////
	public void testDrawPanel() {
		pathPanel = new PathPanel(gridPanel,this);
		((PathPanel) pathPanel).setStartFinish(((GridPanel)gridPanel).getSourceLabel(),((GridPanel)gridPanel).getTargetLabel());
		pathPanel.addMouseListener(view.getController());
		pathPanel.setSize(gridPanel.getSize());
		pathPanel.setLocation(0,0);
		pathPanel.setVisible(true);
		//holdingPanel.remove(gridPanel);
		holdingPanel.add(pathPanel);
		System.out.println(holdingPanel.getComponentZOrder(gridPanel));
		holdingPanel.setComponentZOrder(gridPanel,1);
		holdingPanel.setComponentZOrder(pathPanel,0);

		//holdingPanel.add(gridPanel);
		testDrawOnPanel();
		pathPanel.requestFocus();
		holdingPanel.revalidate();
		holdingPanel.repaint();

	}
	
	public void handleReset() {
		((GridPanel)gridPanel).resetForNextSearch();
	}
			
	
}
