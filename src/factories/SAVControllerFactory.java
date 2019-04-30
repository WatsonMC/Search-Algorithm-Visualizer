package factories;

import controller.SAVController;
import model.SAVModel;
import view.SAVView;

public class SAVControllerFactory {
	private static SAVController savController;
	
	private  static SAVView view;
	private  static SAVModel model;
	
	public SAVControllerFactory(SAVView view, SAVModel model) {
		SAVControllerFactory.model = model;
		SAVControllerFactory.view = view;
	}
	
	public static SAVController getInstance() {
		if(savController == null) {
			savController = new SAVController(view, model);
		}
		return savController;
	}
	
}
