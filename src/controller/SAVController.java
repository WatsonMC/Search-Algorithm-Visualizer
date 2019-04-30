package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.SAVModel;
import view.GridPanel;
import view.GridPosition;
import view.PathPanel;
import view.SAVView;

public class SAVController implements MouseListener, KeyListener{
	
	/**
	 * RI:
	 * - boolean flags control pressed and mousepressed represent
	 * the ctontrol key being held down and the left mouse button being held respectively
	 * Only one should be true at a given time, since it is not possible to set source/target
	 * while also setting blocks
	 * 
	 */
	//Dependencies
	private SAVView view;
	private SAVModel model;
	
	//fields
	private boolean flagControlKey = false;
	private boolean flagLeftClick = false;
	private boolean flagRightClick = false;
	private boolean runState = false;
	
	private boolean gridEnabled = true;
	
	//controllers
	private ControlsController controlsController;
	
	
	public SAVController(SAVView view, SAVModel model) {
		this.view = view;
		this.model = model;
		
	}
	
	//test controller
	public SAVController() {
		// TODO Auto-generated constructor stub
	}
	
	public void start() {
		runState = true;
		view.setRunState(true);
		model.setRunningState(true);
		model.startSearch();
	}
	
	public void stopRunning() {
		this.runState = false;
		view.setRunState(false);
		model.setRunningState(false);
	}
	//replaces start();
	public void enable() {
		this.runState = false;
	}
	
	public void disable() {
		this.runState = true;
	}
	

	@Override
	public void mouseClicked(MouseEvent click) {
  		if(runState == true || !gridEnabled) {
			return;
		}
		if(flagControlKey) {
			GridPosition clickedPosition = (GridPosition) click.getSource();
			if(click.getButton() == MouseEvent.BUTTON1) {
				//set source
				model.setSource(clickedPosition.x, clickedPosition.y);
				
			}
			else if(click.getButton() == MouseEvent.BUTTON3) {
				//set target
				model.setTarget(clickedPosition.x, clickedPosition.y);
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(!flagControlKey) {
			if(flagLeftClick) {
				block((GridPosition)e.getSource());
			}
			if(flagRightClick) {
				unblock((GridPosition)e.getSource());

			}
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(!flagControlKey && !flagRightClick &&e.getButton() == MouseEvent.BUTTON1) {
			flagLeftClick = true;		
			block((GridPosition)e.getSource());
		}
		if(!flagControlKey && !flagLeftClick &&e.getButton() == MouseEvent.BUTTON3) {
			flagRightClick = true;		
			unblock((GridPosition)e.getSource());
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(e.getSource() instanceof PathPanel) {
			return;
		}

		if(!flagControlKey && !flagRightClick &&e.getButton() == MouseEvent.BUTTON1) {
			flagLeftClick = false;		
		}
		if(!flagControlKey && !flagLeftClick &&e.getButton() == MouseEvent.BUTTON3) {
			flagRightClick = false;		
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL&!flagLeftClick & !flagRightClick) {
			flagControlKey =true;
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL&!flagLeftClick) {
			flagControlKey =false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(runState == true|| !gridEnabled) {
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL&!flagLeftClick) {
			flagControlKey =true;
		}			
	}
	
	public String makeLabel(int x, int y) {
		return Integer.toString(x) + "," + Integer.toString(y);
	}
	
	public void toggleBlock(GridPosition posn) {
		if(posn.getState() == GridPanel.BLOCKED) {
			model.unsetBlock(posn.x, posn.y);
		}
		else {
			model.setBlock(posn.x, posn.y);
		}
	}
	
	public void block(GridPosition posn) {
		if(posn.getState() != GridPanel.BLOCKED) {
			model.setBlock(posn.x, posn.y);
		}
	}
	
	public void unblock(GridPosition posn) {
		if(posn.getState() != GridPanel.UNBLOCKED) {
			model.unsetBlock(posn.x, posn.y);
		}
	}
	
	//getter method for the model
	public SAVModel getModel() {
		return this.model;
	}
	
	public boolean isRunning() {
		return new Boolean(runState);
	}
	
	public void disableGrid() {
		this.gridEnabled =false;
	}
	
	public void enableGrid() {
		this.gridEnabled = true;
	}

}
