package controller;

import java.awt.Component;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SAVModel;

public class SpeedSliderController implements ChangeListener{
	/**
	 * Class to handle change events on the speed slider on the control panel
	 * and update speed accordingly
	 * References needed:
	 * - Model -> to update speed
	 */
	
	private SAVModel model;
	private StateController stateController;
	
	private Component speedComponent;
	private boolean enabledFlag;
	
	public SpeedSliderController(SAVModel model, StateController stateController) {
		this.model = model;
		this.stateController = stateController;
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if(enabledFlag) {
			int newSpeed = ((JSlider)arg0.getSource()).getValue();
			model.setSpeed(newSpeed);
		}
	}
	
	// singleton pattern load method for the model dependency
	public boolean loadModel(SAVModel model) {
		if (model == null) {
			this.model = model;
			return true;
		}
		else return false;
	}
	
	
	//DI for component
	public void loadComponent(Component component) {
		this.speedComponent = component;
	}
	
	
	// Set enable flag to all calls to speed change functionality
	public void enable() {
		this.enabledFlag = true;
		speedComponent.setEnabled(true);
	}
	
	// Reset enable flag to prevent unwanted calls to speed change functionality 
	public void disable() {
		this.enabledFlag = false;
		speedComponent.setEnabled(false);
	}
	
}
