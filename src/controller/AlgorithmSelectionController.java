package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;

import updatingSearchAlgorithms.UpdatingSearchAlgorithm;
import view.ControlsPanel;
import view.SAVView;

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
	private static Map<String,Integer> algorithmSelectors;
	
	//set of algorithm names
	private static Set<String> algorithms;
	
	//current algorithm selected. This is controlled through the view control panel
	private String currentAlgorithm; 
	
	private static final String NO_ALGORITHM_LOADED_TEXT = "ERROR - no algorithm text loaded";
	
	//Dependencies
	private ControlsPanel cpanel;
	private SAVView view;
	
	public AlgorithmSelectionController(SAVView view) {
		this.view =view;
		algorithmDescriptions = new HashMap<>();
		algorithmSelectors = new HashMap<>();
		algorithms =  new HashSet<>();
		intialize();
	}
	
	
	//Getter for an array of the algorithm names for the combobox selector 
	public String[] getAlogirthmArray() {
		String[] algoArray =  algorithms.toArray(new String[algorithms.size()]);
		Arrays.sort(algoArray);
		return algoArray;
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
	
	public Integer getAlgorithmKey() {
		return algorithmSelectors.get(currentAlgorithm);
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
		currentAlgorithm = selectedAlgo;
		view.setAlgorithmDescription(this.getAlgorithmText(selectedAlgo));
		
	}
	
	
	private void intialize() {
		//add each algorithm
		String dijkstra = "Dijkstra's";
		String dijkstraDesc = "Dijkstra's algorithm is an algorithm for finding the shortest paths "
				+ "between nodes in a graph. "
				+ "\n\nThe Dijkstra algorithm uses labels that are positive integer or real numbers, which have the strict weak ordering defined, to find the distance"
				+ "to all nodes, or between two points.";
		algorithmDescriptions.put(dijkstra, dijkstraDesc);
		algorithmSelectors.put(dijkstra, UpdatingSearchAlgorithm.DJIRKSTA);
		
//		String test = "test";
//		String testDesc = "this is a test."
//				+ "\n it is your birthday";
//		algorithmDescriptions.put(test, testDesc);
		
		String AStarSimple = "A* diagonal";
		String AStarSimpleDesc = "A Star Search uses a heurstic function to estimate the"
				+ " expected cost for each neighbour node of a given vertex. \n"
				+ "For the diagonal heuristic, the h-score is the distance travelled between a given node "
				+ "and the target node if it were to take the best route using diagonal and horizontal/vertical steps. \n"
				+ "The h-score and the g-score (distance from start node) is summed \n"
				+ "in the f-score - which is used to select the next target vertex. \n"
				+ "In this way, A* can find a target more quickly that Dijkrstras, but is the same"
				+ " for non-target searches (since there is no target to calculate the h-score). \n"
				+ "A* is also not garaunteed to find the shortest path, especially with other implementations using"
				+ " different H-functions.";
		algorithmDescriptions.put(AStarSimple, AStarSimpleDesc);
		algorithmSelectors.put(AStarSimple, UpdatingSearchAlgorithm.A_STAR_DIAGONAL);
		
		String AStarSimpleManhattan = "A* Manhattan";
		String AStarSimpleManhattanDesc = "A Star Search uses a heurstic function to estimate the"
				+ " expected cost for each neighbour node of a given vertex. \n"
				+ "For the Manhattan heuristic, the h-score is the combination of dx and dy, the "
				+ "respective distances between the x and y positions of the currently epanding node"
				+ " and the target node \n"
				+ "The h-score and the g-score (distance from start node) is summed "
				+ "in the f-score - which is used to select the next target vertex. \n"
				+ "In this way, A* can find a target more quickly that Dijkrstras, but is the same"
				+ " for non-target searches (since there is no target to calculate the h-score). \n"
				+ "A* is also not garaunteed to find the shortest path, especially with other implementations using"
				+ " different H-functions.";
		algorithmDescriptions.put(AStarSimpleManhattan, AStarSimpleManhattanDesc);
		algorithmSelectors.put(AStarSimpleManhattan, UpdatingSearchAlgorithm.A_STAR_MANHATTAN);
		
		String AStarSimpleEuclidian = "A* Euclidan";
		String AStarSimpleEuclidianDesc = "A Star Search uses a heurstic function to estimate the"
				+ " expected cost for each neighbour node of a given vertex. \n"
				+ "For the Euclidian heuristic, the h-score is the straight-line distance (as the crow flies) between a given node "
				+ "and the target node. This is distinct from the diagonal heurstic in that it does not "
				+ "calculate the actual distance travelled in steps for the final path, just the raw difference. \n"
				+ "The h-score and the g-score (distance from start node) is summed "
				+ "in the f-score - which is used to select the next target vertex. \n"
				+ "In this way, A* can find a target more quickly that Dijkrstras, but is the same"
				+ " for non-target searches (since there is no target to calculate the h-score). \n"
				+ "A* is also not garaunteed to find the shortest path, especially with other implementations using"
				+ " different H-functions.";
		algorithmDescriptions.put(AStarSimpleEuclidian, AStarSimpleEuclidianDesc);
		algorithmSelectors.put(AStarSimpleEuclidian, UpdatingSearchAlgorithm.A_STAR_EUCLIDIAN);

		algorithms = algorithmDescriptions.keySet();
		currentAlgorithm = getAlogirthmArray()[0]; //TODO make this fucking better...
	}
	
}
