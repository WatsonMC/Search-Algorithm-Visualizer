package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.PathPanel;

public class PathPanelListener implements MouseListener{
	/**
	 * Listener to handle the path panel interaction. 
	 * This class will call reset on the panel. The reset method of the panel handles whether or not
	 * to actually reset
	 */
	@Override //resets the path panel if clicked on
	public void mouseClicked(MouseEvent e) {
		PathPanel panel = (PathPanel)e.getSource();
		panel.reset();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
