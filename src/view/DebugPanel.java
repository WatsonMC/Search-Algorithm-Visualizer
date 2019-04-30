package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;


public class DebugPanel {
	private JPanel panel;
	private JLabel currentPosn;
	private JButton btnTest1;
	private JButton btnTest2;
	private JButton btnTest3;
	private SAVView sav;
	private DebugListener listener;
	
	public DebugPanel(SAVView view) {
		panel = new JPanel();
		this.sav = view;
		panel.setLayout(new GridLayout(10, 1));
		constructViewPosnValue();
		constructButton1();
		constructButton2();
		constructButton3();
		constructMaxDistanceColour();
		constructMinDistanceColour();
		panel.setVisible(true);
		panel.setPreferredSize(new Dimension(300,100));
		listener = new DebugListener(this);
		sav.registerNewMouseListenerOnGrid(listener);
		
		
	}
	
	public void constructMaxDistanceColour() {
		Color max = new Color(0,255,0);
		JPanel rect = new DrawRect(max);
		rect.setVisible(true);
		panel.add(rect);
		
	}
	
	public void constructMinDistanceColour() {
		Color max = new Color(255,0,0);
		JPanel rect = new DrawRect(max);
		rect.setVisible(true);
		panel.add(rect);
		
	}
	
	class DrawRect extends JPanel {
		Color colour;
		public DrawRect(Color colour) {
			this.colour = colour;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(this.colour);
			g.drawRect(0, 0, 40, 40);
			g.fillRect(0, 0, 40, 40);
		}
	}
	
	public void constructViewPosnValue() {
		currentPosn = new JLabel("test");
		currentPosn.setVisible(true);
		panel.add(currentPosn);
	}
	
	public void setPosnText(String text) {
		currentPosn.setText(text);
	}
	
	public void constructButton1() {
		btnTest1 = new JButton("add ovelay");
		btnTest1.setVisible(true);
		panel.add(btnTest1);
		
		btnTest1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sav.getGrid().testPathDraw();
			}
		});
	}
	
	public void constructButton2() {
		btnTest2 = new JButton("removeOverlay");
		btnTest2.setVisible(true);
		panel.add(btnTest2);
		
		btnTest2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sav.getGrid().removePathPanel();
			}
		});
	}
	
	public void constructButton3() {
		btnTest3 = new JButton("instructionsTest");
		btnTest3.setVisible(true);
		panel.add(btnTest3);
		
		btnTest3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				InstructionsPanel.displayInstructions();
				
			}
		});
		
	}
	
	public JPanel getDebugPanel() {
		return panel;
	}
	
	
}

class DebugListener implements MouseListener{
	private DebugPanel debug;
	
	public DebugListener(DebugPanel debug) {
		this.debug =debug;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() instanceof GridPosition) {
			GridPosition posn = (GridPosition)e.getSource();
			debug.setPosnText("Current posn: " + posn.getXCoord() + "," + posn.getYCoord());
		}
		
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
