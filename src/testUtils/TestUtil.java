package testUtils;

import javax.swing.SwingUtilities;

import view.SAVView;

public class TestUtil {

	public static void main(String[] args) {
		//if(args.equals("view")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SAVView savView = new SAVView();
					savView.testColourPallete();
					savView.testBlocks();

				}
			});
		//}
	}
}
