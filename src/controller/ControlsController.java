package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.ControlsPanel;

public class ControlsController implements ChangeListener, ActionListener {
	
	private AlgorithmSelectionController algoCtrl;  
	private ControlsPanel controlsPanel;
	private SAVController controller;
	private boolean controlsPanelSet = false;
	
	
	private PauseController pauseController;
	
	public ControlsController() {
		//this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton action = (JButton)e.getSource();
		
		if(action.getText().equals(ControlsPanel.BTN_TXT_START)){
			//controller.start();
			//start functionality
			//call start on the main controller
			// set the running states
			// View:
			//	- update buttons to be not clickable
			//  - start showing distances
			//Controller:
			// - update running flags
			// - tell model to start
			//Model:
			// - create algorithms
			
		}
		else if(action.getText().equals("Clear")) {
			
		}
		else if(action.getText().equals("Display Instructions")) {
			
		}
		//Supeseded by individual controllers for each button
		//else if(action.getText().equals("Stop")) {
			//if(controller.isRunning()) {
				//if(!controller.getModel().pauseSearch()) {
					//controller.getModel().resumeSearch();
				//}
		//	}
	//	}
		
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}
	
	//Loads the controls panel to the controller. Can only be callsed once
	public boolean loadControlsPanel(ControlsPanel panel) {
		if(!controlsPanelSet) {
			//this.controlsPanel  = panel;
			//this.algoCtrl = new AlgorithmSelectionController(panel);
			//this.pauseController = new PauseController(controller, panel);
			return true;
		}
		return false;
	}
	
	public ActionListener getPauseController() {
		return this.pauseController;
	}
	
	//public ActionListener getStartController() {
	//	return this.startController;
	//}
	
	
	
	//getter for the algorithm selection controller
	public AlgorithmSelectionController getAlgorithmSelectionController() {
		return this.algoCtrl;
	}
}
