package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;

import view.ControlsPanel;

public class AlgorithmSelectionController implements ActionListener{
	/**
	 * This class acts as a store and controller for the algorithm selection
	 * Algorithms are identified by their string names
	 * Algorithm selection is done by combobox on the controls panel. 
	 * Instance is injected into the model to
	 * 	a. let the model create the correct algorithm when running
	 */
	//Map of algorithms and their description text
	private static Map<String,String> algorithmDescriptions;
	
	//set of algorithm names
	private static Set<String> algorithms;
	
	//current algorithm selected. This is controlled through the view control panel
	private String currentAlgorithm; 
	
	private static final String NO_ALGORITHM_LOADED_TEXT = "ERROR - no algorithm text loaded";
	
	private ControlsPanel cpanel;
	
	public AlgorithmSelectionController(ControlsPanel cpanel) {
		this.cpanel =cpanel;
		algorithmDescriptions = new HashMap<>();
		algorithms =  new HashSet<>();
		intialize();
	}
	
	
	//Getter for an array of the algorithm names for the combobox selector 
	public String[] getAlogirthmArray() {
		return algorithms.toArray(new String[algorithms.size()]);
	}
	
	//gets the algorithm text for the supplied stringname of the algorithm
	public String getAlgorithmText(String algorithm) {
		if(!algorithms.contains(algorithm)) {
			return NO_ALGORITHM_LOADED_TEXT;
		}
		else{ return algorithmDescriptions.get(algorithm);} 
	}
	
	//geter for the current algorithm
	public String getAlgorithm() {
		return new String(currentAlgorithm);
	}
	
	// Sets the current algorithm to the supplied algorithm, if it is found in
	//the algorithms, then returns true. If not found, returns false.
	public boolean setAlgorithm(String algorithm) {
		if(!algorithms.contains(algorithm)) {
			return false;
		}
		this.currentAlgorithm = algorithm;
		return true;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		//only the selected combobox should be registered on this listener
		JComboBox<String> selectionBox = (JComboBox<String>)e.getSource();		
		String selectedAlgo = (String)selectionBox.getSelectedItem();
		cpanel.updateAlgorithmText(this.getAlgorithmText(selectedAlgo));
		
	}
	
	
	private void intialize() {
		//add each algorithm
		String dijkstra = "Dijkstra's";
		String dijkstraDesc = "Dijkstra's algorithm is an algorithm for finding the shortest paths "
				+ "between nodes in a graph. "
				+ "\n\nThe Dijkstra algorithm uses labels that are positive integer or real numbers, which have the strict weak ordering defined, to find the distance"
				+ "to all nodes, or between two points.";
		algorithmDescriptions.put(dijkstra, dijkstraDesc);
		
		String test = "test";
		String testDesc = "this is a test."
				+ "\n it is your birthday";
		algorithmDescriptions.put(test, testDesc);
		
		algorithms = algorithmDescriptions.keySet();
		currentAlgorithm = dijkstra; //TODO make this fucking better...
	}
	
}
