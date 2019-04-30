package factories;

import controller.StartController;
import controller.StateController;
import model.SAVModel;

public class StartControllerFactory {
	private static StartController startController;
	
	private static SAVModel model;
	private static StateController stateController;
	public StartControllerFactory(SAVModel model, StateController stateController) {
		this.model = model;
		this.stateController = stateController;
	}
	
	public static StartController getInstance() {
		if (startController == null){
			startController = new StartController();
			startController.loadModel(model);
			startController.loadStateController(stateController);
		}
		return startController;
	}
	
}
