package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.SAVModel;
import view.ControlsPanel;

public class PauseController implements ActionListener{
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Class to handle pause/resume button presses from controls panel
	 */
	
	/**
	 * Dependencies:
	 * - State controller to change state
	 * - Model to pause/resume search
	 * 
	 */
	
	private boolean enabledFlag =  true;
	
	private SAVController controller; // this should be removed
	private ControlsPanel controls;	//this should be removed 
	private SAVModel model;
	
	private Component pauseComponent;
	
	private StateController stateController;
	
	public PauseController(SAVController controller, ControlsPanel controls) {
		this.controller = controller;
		this.controls = controls;
	}
	
	public PauseController() {
		
	}
	
	
	// calls pause or resume depending of the button text is pause or resume
	//Superseded by action event called using stateController
	public void actionPerformed1(ActionEvent e) {
		JButton btnPause = ((JButton)e.getSource());
		if(btnPause.getText().equals(ControlsPanel.BTN_TXT_PAUSE)) {
			controller.getModel().pauseSearch();getClass();
		}
		else {
			controller.getModel().resumeSearch();
		}
		controls.togglePauseButton();
	}
	
	//Injection of stateController 
	public boolean loadStateController(StateController stateController) {
		if(this.stateController == null) {
			this.stateController = stateController;
			return true;
		}
		return false;
	}
	
	//Injection of model object 
	public boolean loadModel(SAVModel model) {
		if(this.model == null) {
			this.model = model;
			return true;
		}
		return false;
	}
	
	public void loadComponent(Component component) {
		this.pauseComponent = component;
	}
	
	//Enable method for functionality of button
	public void enable() {
		this.enabledFlag = true;
		if(pauseComponent!= null) {
			pauseComponent.setEnabled(true);
		}
			
	}
	
	//disable method for functionality of button
	public void disable() {
		this.enabledFlag = false;
		if(pauseComponent!= null) {
			pauseComponent.setEnabled(false);
		}
	}
	/**
	 * toggles the state of the button by calling the stateController pausePressed() or resumePressed(), if not enabeld does nothing
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!enabledFlag) {return;}
		JButton btnPause = ((JButton)e.getSource());
		if(btnPause.getText().equals(ControlsPanel.BTN_TXT_PAUSE)) {
			model.pauseSearch();
			stateController.setState(StateController.PAUSED);
		}
		else {
			model.resumeSearch();
			stateController.setState(StateController.RUNNING);
		}
	}

}
