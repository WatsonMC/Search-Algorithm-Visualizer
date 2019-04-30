package factories;

import controller.SpeedSliderController;
import controller.StateController;
import model.SAVModel;

public class SpeedSliderControllerFactory {
	private static SpeedSliderController speedControler;
	
	private static SAVModel model;
	private static StateController stateController;
	
	public SpeedSliderControllerFactory(SAVModel model,StateController stateController) {
		SpeedSliderControllerFactory.model = model;
		SpeedSliderControllerFactory.stateController= stateController;
	}
	
	public static SpeedSliderController getInstance() {
		if(speedControler == null) {
			speedControler = new SpeedSliderController(model, stateController);
		}
		return speedControler;
	}
	
}
