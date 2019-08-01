package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.InstructionsPanel;
import view.SAVView;

public class InstructionsController implements ActionListener {
	
	//DI
	private SAVView view;
	
	private boolean enabled = true;
	private Component instructionsComponent;
	
	public void loadView(SAVView view) {
		if(this.view == null) {
			this.view = view;
		}
	}
	
	public void enable() {
		this.enabled =true;
		if(instructionsComponent !=null) {
			instructionsComponent.setEnabled(true);
		}
	}
	
	public void disable() {
		this.enabled = false;
		if(instructionsComponent!=null) {
			instructionsComponent.setEnabled(false);
		}
	}
	
	
	public void loadComponent(Component component){
		this.instructionsComponent = component;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//set state
		
		InstructionsPanel.displayInstructions(view.getFrame());
	}
}
