package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import controller.AlgorithmSelectionController;
import controller.ControlsController;
import factories.AlgorithmSelectionControllerFactory;
import factories.ClearControllerFactory;
import factories.InstructionsControllerFactory;
import factories.PauseControllerFactory;
import factories.SpeedSliderControllerFactory;
import factories.StartControllerFactory;


public class ControlsPanel  {

	private static Border INTRO_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), 
			BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	private static Border PANEL_BORDER = BorderFactory.createLineBorder(Color.WHITE,5,false);
	private static Font INTRO_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 15);
	private JPanel controlPanel;
	private JPanel startStopPanel;	// top panel, start stop and control speed
	private JPanel algorithmSelectionPanel;	//middle panel, select algorithm and show description
	private JPanel clearPanel;	// bottom panel, has clear button
		
	private JTextArea txtAlgo;
	private int VGAP = 15;	//Gap beteween components on each panel
	private int PANEL_GAP = 10; //gap between three panels  
	private int BUTTON_HEIGHT = 50; // standard button height
	

	private boolean flagRunning;
	
	private ControlsController controlsController;
	private BorderLayout layout;
	private Map<String, Component> componentMap =  new HashMap<>();
	//Button text IDs
	public static final String BTN_TXT_START = "Start";
	public static final String BTN_TXT_PAUSE = "Pause";
	public static final String BTN_TXT_RESUME = "Resume";
	public static final String BTN_TXT_CLEAR = "Clear";
	public static final String BTN_TXT_INST = "Instructions";
	//Constructor class for the controls panel
	
	
	
	public ControlsPanel(ControlsController controlsController) {
		this.controlPanel = new JPanel();
		this.startStopPanel = new JPanel();
		this.clearPanel = new JPanel();
		this.algorithmSelectionPanel = new JPanel();
		this.controlsController = controlsController;
		this.layout = new BorderLayout();
		controlsController.loadControlsPanel(this);

		constructPanel();

	}
	
	/**
	 * Method to toggle text on the pause button from resume to pause or vice versa
	 */
	public void togglePauseButton() {
		JButton btnStop = ((JButton)componentMap.get("btnStop"));
		if(btnStop.getText().equals(BTN_TXT_PAUSE)) {
			btnStop.setText(BTN_TXT_RESUME);
			return;
		}
		btnStop.setText(BTN_TXT_PAUSE);
	}
	
	//method for toggling text of pause/stop button to pause
	public void setPauseButton() {
		JButton btnStop = ((JButton)componentMap.get("btnStop"));
		btnStop.setText(BTN_TXT_PAUSE);
	}
	
	//method for toggling text of pause/stop button to resume
	public void setResumeButton() {
		JButton btnStop = ((JButton)componentMap.get("btnStop"));
		btnStop.setText(BTN_TXT_RESUME);
		return;
	}
	
	public Component getComponent(String text) {
		if(componentMap.get(text) == null) {
			return null;
		}
		else return componentMap.get(text);
	}
	
	public ControlsPanel() {
		this.controlPanel = new JPanel();
		this.startStopPanel = new JPanel();
		this.clearPanel = new JPanel();
		this.algorithmSelectionPanel = new JPanel();
		//this.controlsController = controlsController;
		this.layout = new BorderLayout();
	//	controlsController.loadControlsPanel(this);

		constructPanel();
	}
	
	public void setRunState(boolean flagRunning) {
		
	}
	
	private void toggleButtons(boolean flagRunning) {
		
	}
	
	/**
	 * Buttons:
	 * Clear
	 * Start
	 * Stop?? maybe (second tier)
	 * 
	 * Select Algorithm dropdown
	 * Speed slider
	 * 
	 * Algorithm description text
	 */
	
	private void constructPanel() {
		controlPanel.setLayout(new BorderLayout(0,PANEL_GAP));
		constructStartStopPanel();
		constructAlogrithmSelectionPanel();
		constructClearPanel();
		
		controlPanel.add(startStopPanel, BorderLayout.PAGE_START);
		controlPanel.add(algorithmSelectionPanel, BorderLayout.CENTER);
		controlPanel.add(clearPanel, BorderLayout.PAGE_END);
		controlPanel.setVisible(true);
	}

	//Constructor for the start stop panel (top third) of the controls panel
	public void constructStartStopPanel() {
		startStopPanel.setLayout(new BorderLayout(0,VGAP));
		JPanel startContentPanel = new JPanel();
		startContentPanel.setLayout(new BorderLayout(0,VGAP));
		constructStopButton(startContentPanel);
		constructStartButton(startContentPanel);
		constructSpeedSlider(startContentPanel);
		constructStartStopIntro(startStopPanel);
		
		startStopPanel.setBorder(PANEL_BORDER);
		startStopPanel.add(startContentPanel,BorderLayout.CENTER);
	}
	
	//construct the algorithm selector and details panel
	public void constructAlogrithmSelectionPanel() {
		algorithmSelectionPanel.setLayout(new BorderLayout(0,VGAP));
		JPanel algoContentPanel = new JPanel();
		algoContentPanel.setLayout(new BorderLayout());
		constructAlgorithmText(algoContentPanel);
		constructSelectAlgorithm(algoContentPanel);
		constructAlgoPanelIntroText(algorithmSelectionPanel);
		algorithmSelectionPanel.setBorder(PANEL_BORDER);
		algorithmSelectionPanel.add(algoContentPanel,BorderLayout.CENTER);
	}
	
	public void constructClearPanel() {
		clearPanel.setLayout(new BorderLayout(0,VGAP));
		
		constructClearButton(clearPanel);
		constructClearPanelIntro(clearPanel);
		constructInstructionsButton(clearPanel);
		clearPanel.setBorder(PANEL_BORDER);
	}
	
	
	//construct the algoirthm control text
	private void constructStartStopIntro(JPanel panel) {
		JLabel lblAlgoControls = new JLabel();
		lblAlgoControls.setText("Start and stop execution");
		lblAlgoControls.setFont(INTRO_FONT);
		lblAlgoControls.setBorder(INTRO_BORDER);
		lblAlgoControls.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel.add(lblAlgoControls,BorderLayout.PAGE_START);
		addComponentToMap(lblAlgoControls);
	}
	
	//Constructor method for the start button	//->start stop panel
	private void constructStartButton(JPanel panel) {
		JButton btnStart = new JButton(BTN_TXT_START);
		btnStart.setPreferredSize(new Dimension(btnStart.getWidth(), BUTTON_HEIGHT));
		btnStart.addActionListener(StartControllerFactory.getInstance());
		StartControllerFactory.getInstance().loadComponent(btnStart);
		panel.add(btnStart,BorderLayout.PAGE_START);
		addComponentToMap(btnStart);
	}
	
	//->start stop panel
	private void constructStopButton(JPanel panel) {
		JButton btnStop = new JButton(BTN_TXT_PAUSE);
		btnStop.setName("btnStop");
		btnStop.setPreferredSize(new Dimension(btnStop.getWidth(), BUTTON_HEIGHT));
		btnStop.addActionListener(PauseControllerFactory.getInstance());
		PauseControllerFactory.getInstance().loadComponent(btnStop);
		panel.add(btnStop,BorderLayout.CENTER);
		addComponentToMap(btnStop);
	}
	
	//Speed slider constructor ->start stop panel
	private void constructSpeedSlider(JPanel panel) {
		JSlider speedSlider = new JSlider();
		speedSlider.setMajorTickSpacing(5);
		speedSlider.setMaximum(40);
		speedSlider.setMinimum(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setInverted(true);
		speedSlider.setValue(20);
		speedSlider.addChangeListener(SpeedSliderControllerFactory.getInstance()); //Change to SppedController
		SpeedSliderControllerFactory.getInstance().loadComponent(speedSlider);
		speedSlider.setBorder(BorderFactory.createTitledBorder("Algorithm Speed"));
		panel.add(speedSlider,BorderLayout.PAGE_END);
		addComponentToMap(speedSlider);
	}
	
	//Constructor for the intro text for the algorithm panel section
	private void constructAlgoPanelIntroText(JPanel panel) {
		JLabel lblAlgoIntro = new JLabel();
		lblAlgoIntro.setText("Select the algorithm");
		lblAlgoIntro.setFont(INTRO_FONT);
		lblAlgoIntro.setBorder(INTRO_BORDER);
		lblAlgoIntro.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblAlgoIntro,BorderLayout.PAGE_START);
		addComponentToMap(lblAlgoIntro);
	}
	
	//constructor for the selection box
	private void constructSelectAlgorithm(JPanel panel) {
		//TODO add this
		AlgorithmSelectionController algCtrl = AlgorithmSelectionControllerFactory.getInstance();
		JComboBox<String> cmbAlgorithms =  new JComboBox<>(algCtrl.getAlogirthmArray());
		cmbAlgorithms.addActionListener(algCtrl);
		panel.add(cmbAlgorithms, BorderLayout.PAGE_START);
		addComponentToMap(cmbAlgorithms);
		updateAlgorithmText(algCtrl.getAlgorithmText((String)cmbAlgorithms.getSelectedItem()));
		cmbAlgorithms.setEditable(false);
		cmbAlgorithms.setSelectedItem(0);
	}
	
	private void constructAlgorithmText(JPanel panel) {
		txtAlgo = new JTextArea();
		txtAlgo.setEditable(false);
		panel.add(txtAlgo,BorderLayout.CENTER);
		txtAlgo.setBorder(BorderFactory.createTitledBorder("Description"));
		txtAlgo.setText("test"); //TODO remove
		txtAlgo.setLineWrap(true);
		txtAlgo.setWrapStyleWord(true);
		addComponentToMap(txtAlgo);
	}
	
	//constructor method for clearpanel intro
	private void constructClearPanelIntro(JPanel panel) {
		JLabel lblClearPanel = new JLabel();
		lblClearPanel.setText("Clear board");
		lblClearPanel.setFont(INTRO_FONT);
		lblClearPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lblClearPanel.setBorder(INTRO_BORDER);
		panel.add(lblClearPanel,BorderLayout.PAGE_START);
		addComponentToMap(lblClearPanel);
	}
	
	//Constructor method for the clear button
	private void constructClearButton(JPanel panel) {
		JButton btnClear = new JButton(BTN_TXT_CLEAR);
		btnClear.setPreferredSize(new Dimension(btnClear.getWidth(), BUTTON_HEIGHT));
		btnClear.addActionListener(ClearControllerFactory.getInstance());
		ClearControllerFactory.getInstance().loadComponent(btnClear);
		panel.add(btnClear,BorderLayout.CENTER);
		addComponentToMap(btnClear);
	}
	
	//constructor method for instructionsbutton
	private void constructInstructionsButton(JPanel panel) {
		JButton btnInstructions = new JButton(BTN_TXT_INST);
		btnInstructions.setPreferredSize(new Dimension(btnInstructions.getWidth(), BUTTON_HEIGHT));
		btnInstructions.addActionListener(InstructionsControllerFactory.getInstance());
		panel.add(btnInstructions,BorderLayout.SOUTH);
		addComponentToMap(btnInstructions);
	}
	
	

	private void addComponentToMap(Component comp) {
		this.componentMap.put(comp.getName(),comp);
	}
	
	//getter for the display panel
	public JPanel getControlPanel() {
		return this.controlPanel;
	}

	//Used to update the text of the selected algorithm
	public void updateAlgorithmText(String algorithmText) {
		txtAlgo.setText(algorithmText);
	}
}
