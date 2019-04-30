package factories;

import controller.ClearController;
import controller.StateController;
import model.SAVModel;
import view.SAVView;

public class ClearControllerFactory {
	private static ClearController clearController;
	
	private static SAVView view;
	private static SAVModel model;
	private static StateController stateController;
	
	public ClearControllerFactory(SAVView view, SAVModel model,StateController stateController) {
		ClearControllerFactory.view = view;
		ClearControllerFactory.model = model;
		ClearControllerFactory.stateController= stateController;
	}
	
	public static ClearController getInstance() {
		if(clearController == null) {
			clearController = new ClearController(view, model, stateController);
		}
		return clearController;
	}
}
