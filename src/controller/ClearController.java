package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.SAVModel;
import view.SAVView;

public class ClearController implements ActionListener{
	
	/**
	 * This class is responsible for handling clear button press events
	 * on clear button press:
	 * - if enabled, 
	 * - clear graphics on grid
	 * - reinitialize graph object
	 * Dependencies:
	 * - View -> to call clear and reset view methods
	 * - Model -> to call clear and reset methods on model 
	 * - StateController -> to call set State
	 */
	
	private SAVView view;
	private SAVModel model;
	private StateController stateController;
	
	private Component clearComponent;
	
	private boolean enabledFlag = true; 
	
	public ClearController(SAVView view, SAVModel model, StateController stateController) {
		this.view = view;
		this.model = model;
		this.stateController = stateController;
	}
	
	public void enable() {
		this.enabledFlag =true;
		if(clearComponent !=null) {
			clearComponent.setEnabled(true);
		}
	}
	
	public void disable() {
		this.enabledFlag = false;
		if(clearComponent !=null) {
			clearComponent.setEnabled(false);
		}
	}
	
	public void loadComponent(Component component) {
		this.clearComponent = component;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(enabledFlag) {
			System.out.println("Clear button pressed while enabled"); //workls
			model.resetModel();
			view.clearBoard();
			stateController.setState(StateController.PRE_SEARCH);
			return;
		}
		System.out.println("Clear button pressed while disabled");
	}
	
}
