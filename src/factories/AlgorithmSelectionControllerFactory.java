package factories;

import controller.AlgorithmSelectionController;
import view.SAVView;

public class AlgorithmSelectionControllerFactory {
	private static AlgorithmSelectionController algorithmSelectionController;
	
	private static SAVView view;
	
	public AlgorithmSelectionControllerFactory(SAVView view) {
		AlgorithmSelectionControllerFactory.view = view;
	}
	
	public static AlgorithmSelectionController getInstance() {
		if(algorithmSelectionController == null) {
			algorithmSelectionController = new AlgorithmSelectionController(view);
		}
		return algorithmSelectionController;
	}
	
	
}
