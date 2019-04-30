package main;

import controller.SAVController;
import controller.StateController;
import factories.ClearControllerFactory;
import factories.PauseControllerFactory;
import factories.SAVControllerFactory;
import factories.SpeedSliderControllerFactory;
import factories.StartControllerFactory;
import model.SAVModel;
import view.SAVView;

public class SAVMain {
	public static void main(String[] args) {
		//create view
		SAVView view = new SAVView();
		SAVModel model = new SAVModel();
		//SAVController controller = new SAVController(view,model);
		StateController stateController = new StateController();
		
		//Load factories
		PauseControllerFactory pfact = new PauseControllerFactory(stateController, model);
		StartControllerFactory startFact = new StartControllerFactory(model, stateController);
		SAVControllerFactory savFact = new SAVControllerFactory(view, model);
		ClearControllerFactory clearFact = new ClearControllerFactory(view, model, stateController);
		SpeedSliderControllerFactory speedFact = new SpeedSliderControllerFactory(model, stateController);
		
		
		view.setModel(model);
		view.setController(SAVControllerFactory.getInstance());
		view.loadStateController(stateController);
		model.setView(view);
		model.setController(SAVControllerFactory.getInstance());
		model.loadStateController(stateController);
		stateController.loadModel(model);
		stateController.loadView(view);
		
		
		view.init();
		model.init();
		stateController.init();
	
	}
	
}
