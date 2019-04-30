package factories;

import controller.PauseController;
import controller.StateController;
import model.SAVModel;

public class PauseControllerFactory {
	private static PauseController pauseController;
	private static SAVModel model;
	private static StateController stateController;
	
	//contrustor requires pause controller dependencies
	public PauseControllerFactory(StateController stateController, SAVModel model) {
		PauseControllerFactory.stateController = stateController;
		PauseControllerFactory.model = model;
	}
	
	//get instance method for the controller
	public static PauseController getInstance() {
		if(pauseController   == null) {
			pauseController = new PauseController();
			pauseController.loadModel(model);
			pauseController.loadStateController(stateController);
		}
		return pauseController;
	}
}
