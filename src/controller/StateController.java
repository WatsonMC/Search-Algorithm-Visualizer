package controller;

import factories.AlgorithmSelectionControllerFactory;
import factories.ClearControllerFactory;
import factories.InstructionsControllerFactory;
import factories.PauseControllerFactory;
import factories.SAVControllerFactory;
import factories.SpeedSliderControllerFactory;
import factories.StartControllerFactory;
import model.SAVModel;
import view.ControlsPanel;
import view.SAVView;

public class StateController {
	
	/**
	 * Class is responsible for changing states of the contollers, model and view for different
	 *as specified below
	 *
	 * States:
	 * Pre search:
	 * State when user is drawing on the grid and everything execpt the pause button should be available:
	 * 
	 * Drawing path
	 * State when the algo has finished and the final path is being drawn. No User input is available during this state
	 * 
	 * Algorithm running:
	 * Only pause is available - this is when the algorithm is actually running
	 * 
	 * Clear and resume available:
	 * State when pause has been pressed during a run. Here clear and resume should be available
	 * 
	 */
	
	/**
	 * Dependencies:
	 * - Model -> to start, pause and resume searches THIS COULD BE IN THE START CONTROLLER?PAUSE CONTRLLLERS
	 * - controlsPanel -> to alter states of buttons
	 * 
	 */
	/**
	 * Controllers handled by the stateController
	 * - PauseController
	 * - StartController
	 * - ClearController
	 * - SpeedSliderController
	 */
	private PauseController pauseContoller;
	private StartController startController;
	private SpeedSliderController speedController;
	private ClearController clearController;
	private AlgorithmSelectionController algorithmSelectionController;
	private SAVController SAVcontroller;
	private InstructionsController instructionsController;
	
	
	public static final Integer PRE_SEARCH = 0;
	public static final Integer PAUSED = 2;
	public static final Integer RUNNING = 1;
	public static final Integer DRAW_PATH = 3;
	
	private int state;
	
	// dependencies:
	private ControlsPanel controls;
	private SAVModel model;
	private SAVView view;
	
	// injection method for controls panel
	public void loadControlsPanel(ControlsPanel controls) {
		this.controls = controls;
	}
	
	//injection method for model
	public void loadModel(SAVModel model) {
		this.model = model;
	}
	
	//injection method for view
	public void loadView(SAVView view) {
		this.view = view;
	}

	
	//call pause search on the model, toggle button state
	public void pausePressed() {
		model.pauseSearch();
		controls.togglePauseButton();
	}
	//call resume pressed on the model,  toggle button state
	public void resumePressed() {
		model.resumeSearch();
		controls.togglePauseButton();
	}
	
	//Initialise the controller by loading the controllers from factories
	public void init() {
		this.pauseContoller = PauseControllerFactory.getInstance();
		this.startController = StartControllerFactory.getInstance();
		this.clearController = ClearControllerFactory.getInstance();
		this.SAVcontroller = SAVControllerFactory.getInstance();
		this.speedController = SpeedSliderControllerFactory.getInstance();
		this.algorithmSelectionController = AlgorithmSelectionControllerFactory.getInstance();
		this.instructionsController = InstructionsControllerFactory.getInstance();
		setState(PRE_SEARCH);
	}
	// Method to set the state of the program to one of the 4 predefined states.
	// Throws illegal argument exception if invalid state is called
	public void setState(Integer state) {
		if(state == PAUSED) {
			setPauseState();
		}
		if(state == RUNNING) {
			setRunningState();
		}
		if(state == DRAW_PATH) {
			setDrawState();
		}
		if(state == PRE_SEARCH) {
			setPreSearchState();
		}
	}
	
	public int getState() {
		return this.state;
	}
	
	private void setPauseState() {
		 //TODO change controls panel to have toggle and et methods -> will need to change when clear is pressed
		controls.setResumeButton();
		clearController.enable();
		//logic to change enable flags on other components
		SAVcontroller.disable();
		speedController.enable();
		
		controls.setResumeButton();
		this.state = PAUSED;
	}
	
	private void setPreSearchState() {
		
		//logic to change enable flags on other components
		startController.enable();
		pauseContoller.disable();
		speedController.enable();
		SAVcontroller.enable();	//allows altering of grid positions
		//algorithmSelectionController.enable();
		clearController.enable();
		
		//set controls
		this.state = PRE_SEARCH;
		
		//set aesthetics not handled by individual controllers
		controls.setPauseButton();
	}
	
	private void setRunningState() {
		//controller enables
		view.resetIfNeeded();
		pauseContoller.enable();
		startController.disable();
		SAVcontroller.disable(); //prevent alterations to grid positions
		//algorithmSelectionController.disable();
		speedController.disable();
		clearController.disable();
		
		//other enables
		view.setRunState(true);	//sets grid running within this call
		model.setRunningState(true);
		
		//set controls
		controls.setPauseButton();
		//logic to change enable flags on other components
		this.state = RUNNING;
	}
	
	private void setDrawState() {
		pauseContoller.disable();
		startController.disable();
		SAVcontroller.disable(); //prevent alterations to grid positions
		//algorithmSelectionController.disable();
		speedController.disable();
		clearController.disable();
		//logic to change enable flags on other components
		
		view.setRunState(false);
		this.state = DRAW_PATH;
	}
	
	public Integer getAlogrithmKey() {
		return algorithmSelectionController.getAlgorithmKey();
	}
}
