package factories;

import controller.InstructionsController;
import view.SAVView;

public class InstructionsControllerFactory {
	private static InstructionsController instructionController;
	
	private static SAVView view;
	
	public InstructionsControllerFactory(SAVView view) {
		this.view = view;
	}
	
	public static InstructionsController getInstance() {
		if(instructionController == null){
			instructionController = new InstructionsController();
			instructionController.loadView(view);
		}
		return instructionController;
	}
}
