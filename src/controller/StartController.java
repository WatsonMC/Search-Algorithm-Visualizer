package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.SAVModel;

public class StartController implements ActionListener {
	/**
	 * Class to handle press of the start button and begin running of the algorithm
	 * Dependencies:
	 * - Model 
	 * 		-> changes run state
	 * StateController


	 */
	
	private SAVModel model;
	private StateController stateController;
	private Component startComponent;
	
	private boolean enabledFlag = true;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		/**
		 * If enabled,
		 * 	Call state controller.setRunning();
		 * 	Call Model.startSearch();
		 */
		if(!enabledFlag||model.getSource().equals(null)) {return;}
		stateController.setState(StateController.RUNNING);
		model.startSearch();
	}
	
	//Injection method for stateControllerDependency
	public boolean loadStateController(StateController stateController) {
		if(this.stateController == null) {
				this.stateController = stateController;
				return true;
		}
		return false;
	}
	
	//Injeciton method for model dependency
	public boolean loadModel(SAVModel model) {
		if(this.model == null) {
			this.model = model;
			return true;
		}
		return false;
	}
	//injection for component to change state
	public void loadComponent(Component component) {
		this.startComponent = component;
	}
	

	// 	Set enable flag to all calls to speed change functionality
	public void enable() {
		this.enabledFlag = true;
		startComponent.setEnabled(true);
	}
	
	// Reset enable flag to prevent unwanted calls to speed change functionality 
	public void disable() {
		this.enabledFlag = false;
		startComponent.setEnabled(false);
	}
}
