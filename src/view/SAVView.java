package view;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ControlsController;
import controller.SAVController;
import controller.StateController;
import model.SAVModel;

public class SAVView{
	//Gui components
	private JFrame frame;
	private GridPanel grid;
	private JPanel controls;
	private ControlsPanel cp;
	private PathOverlayInterface pathOverlayInterface;
	
	private SAVModel model;
	private SAVController controller;
	private boolean debug = true;
	
	private StateController stateController;

	private boolean flagRunning = false;
	// flag to trigger a redraw of entire grid, usually after finish of one search, begin of another
	private boolean resetOnNextUpdate = false;
	
	private final int SIZE = 100; //static size of the grid 
	//TODO update view to update have variable size grid
	
	public SAVView() {
		//init();
	}
	
	public void loadStateController(StateController stateController) {
		this.stateController = stateController;
	}
	
	public void init() {
		//TODO seperate init of gui's from other comps
		this.frame = new JFrame("Search Algorithm Visualizer");
		this.grid = new GridPanel(SIZE,SIZE);
		this.grid.loadView(this);
		
		this.pathOverlayInterface = new PathOverlayInterface(this);
		pathOverlayInterface.loadGrid(grid);
		pathOverlayInterface.addPanel(frame);
		
		frame.addKeyListener(controller);
		
		//create controls panel and controller
		ControlsController cpCtrl = new ControlsController();	//controls contoller to be removed
		cp = new ControlsPanel(cpCtrl);	//can take cpCtrl contstructor injection out after finished all individual conrollers
		this.controls = cp.getControlPanel();
		
		stateController.loadControlsPanel(cp);
		
		frame.add(controls, BorderLayout.LINE_START);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		grid.setFocusable(true);
		grid.requestFocus();
		if(debug) {
			DebugPanel debug = new DebugPanel(this);
			frame.add(debug.getDebugPanel(),BorderLayout.LINE_END);
		}
		frame.pack();
		
	}
	/**
	 * Method to update distances as showed in the grid panel
	 */
	public void updateView(Map<String,Integer> distances) {
		grid.updateGrid(distances);
		//grid.updateGridColours();
	}
	
	public void updateSource(String source) {
		grid.setSource(source);
		if(resetOnNextUpdate) {resetGrid();}
	}
	
	public void updateTarget(String target) {
		grid.setTarget(target);
		if(resetOnNextUpdate) {resetGrid();}
	}
	
	public void block(String posn) {
		grid.block(posn);
		if(resetOnNextUpdate) {resetGrid();}
	}
	
	public void unblock(String posn) {
		grid.unblock(posn);
		if(resetOnNextUpdate) {resetGrid();}
	}
	
	public void resetGrid() {
		grid.resetForNextSearch();
		resetOnNextUpdate =false;
	}
	
	
	//model injection
	public void setModel(SAVModel model) {
		this.model = model;
	}
	//controller injection
	public void setController(SAVController controller) {
		this.controller = controller;
	}
	
	//setter for run state of 
	public void setRunState(boolean flagRunning) {
		if(this.flagRunning  == true && flagRunning ==false) {
			resetOnNextUpdate = true;	//turned off
		}
		this.flagRunning = flagRunning;
		((GridPanel)grid).setRunState(flagRunning);
		((ControlsPanel)cp).setRunState(flagRunning);//defunct, handled by State controller
	}
	
	//pushes new max distance to the grid
	public void pushNewMaxDistance(int distance) {
		grid.updateMaxDistance(distance);
	}
	//set the state,push the end path to the interface
	public void pushEndPath(List<String> path) {
		System.out.println("end success: " + path.toString());
		//TODO here is where we need to call the disable buttons method
		stateController.setState(StateController.DRAW_PATH);
		pathOverlayInterface.drawPath(path);
	}
	public void pathDrawComplete() {
		stateController.setState(StateController.PRE_SEARCH);
	}

	public void maxDistanceUpdateComplete() {
		model.maxDistanceUpdateComplete();
	}
	
	//clears the board ready for a new algorithm
	public void clearBoard() {
		grid.resetForFreshSearch();
	}
	
	public boolean registerNewMouseListenerOnGrid(MouseListener e){
		this.grid.addMouseListener(e);
		grid.addListenerToGrid(e);
		return true;
	}
	
	public GridPanel getGrid() {
		return this.grid;
	}
	////TEST///////
	public void testColourPallete() {
		((GridPanel)grid).testColourPallete();
		frame.pack();
	}
	public void testBlocks() {
		((GridPanel)grid).testBlockIcons();
	}
	
	//test method only
	public MouseListener getController() {
		return this.controller;
	}
	
	public void resetIfNeeded() {
		if(resetOnNextUpdate) {
			grid.resetForNextSearch();
		}
	}

	public void requestGridFocus() {
		grid.requestFocus();
	}
}
