package model;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import controller.SAVController;
import controller.StateController;
import graph.Graph;
import undirectedWeightedGraph.UndirectedWeightedGraph;
import updatingSearchAlgorithms.DjirkstaSearchUpdating;
import updatingSearchAlgorithms.GraphUpdate;
import updatingSearchAlgorithms.UpdatingSearchAlgorithm;
import view.SAVView;

public class SAVModel {
	/**
	 * AF:
	 * -Model of a set of points on a 2D plane, which may or may not be visited by searcher
	 * 
	 * RI:
	 * - points are modelled by Graph with String vertices
	 * - vertices are named by their grid co-ordinates staring at origin (0,0), at the top left
	 * - a blocked path on the 2D plane is represented by a vertex which is unreachable
	 * - adjacent points all have a distance of 1 to each other
	 * - Graph does not contain any vertices which are are outside of the range (0,0) - (99,99)
	 * - Vertex naming conventions are "x,y" where x and y are the integer co-ordinates of the position
	 * - target is a string representing a co-ord in the graph range, or is null representing no target
	 * - source is a string representinga  co-ord in the graph range, or is null representing no source
	 * 
	 * Thread Safety:
	 * - a mess
	 * - Graph is mutable, accessed from the algorithm runner thread, from the inital thread and from the EDT
	 *- Need to make sure that the runner thread is not startable until after the initial thread finished
	 * - after initial thread completes, access to the graph from the EDT is prevented by the controller
	 * 	->>>>>>> need a lock on the graph that the runner can grab for the duration of its run
	 * ->>>this lock is only released from the EDT when the algorith is started
	 * 
	 *  - Distances is only accessed from the EDT either via the updateProcessor object created during startSearch()
	 *  - or from get methods called from the view directly
	 */
	
	
	//RI components
	protected Graph<String> graph;
	private Map<String, Integer> distances;
	private List<String> pathToTarget;
	private Set<String> vertices;
	
	public Object graphLock;
	
	//constants
	protected static final Integer WIDTH = 100;
	protected static final Integer HEIGHT = 100;
	public static final int DEFAULT_DISTANCE_STRAIGHT = 100;
	public static final int DEFAULT_DISTANCE_DIAGONAL = 141;
	
	//variable RI
	private String source;
	private String target;
	
	//Dependencies
	protected SAVController controller; 
	private SAVView view;
	private StateController stateController; //
	private UpdateProcessor updater;
	private RunSearchAlgorithm runner;
	
	private boolean flagRunning = false;
	private int speedSetting = 10; //speed in ms of algorithm execution
	
	
	public SAVModel() {
		//do nothing lol
	}
	
	// depenedency injection for controller and view
	//OBSOLETE = replaced by getInstance
	public void setController(SAVController controller) {
		this.controller = controller;
	}
	
	//calls init method with default distances
	public void init() {
		init(WIDTH,HEIGHT);
	}
	/**
	 * Creates the graph object, assigning vertex names and distances, then tells view to update
	 */
	public void init(Integer width, Integer height) {
		graph = UndirectedWeightedGraph.empty();
		//create each string vertex
		//set the edge weights
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height;y++) {
				String temp = LabelHelper.makeLabel(x,y);
				graph.add(temp);
				if(AdjacencyHelper.checkCoords(x, y+1, width, height)) {graph.set(temp, LabelHelper.makeLabel(x,y+1), DEFAULT_DISTANCE_STRAIGHT);}
				if(AdjacencyHelper.checkCoords(x+1, y+1, width, height)) {graph.set(temp, LabelHelper.makeLabel(x+1,y+1), DEFAULT_DISTANCE_DIAGONAL);}
				if(AdjacencyHelper.checkCoords(x+1, y, width, height)) {graph.set(temp, LabelHelper.makeLabel(x+1, y), DEFAULT_DISTANCE_STRAIGHT);}
				if(AdjacencyHelper.checkCoords(x-1, y+1, width, height)) {graph.set(temp, LabelHelper.makeLabel(x-1,y+1), DEFAULT_DISTANCE_DIAGONAL);}
			}
		}
		//setAllWeights();
		vertices = graph.vertices();
		pathToTarget = null;
		distances = null;
		source = null;
		target = null;
		updateDistances(distances);
		System.out.println("Graph finished initilization");
	}

	public void setView(SAVView view) {
		this.view = view;
	}
	
	public void loadStateController(StateController stateController) {
		this.stateController = stateController;
	}
	
	/**
	 *Update the distances in the model and tell the view to update
	 * @param update
	 */
	public void updateDistances(Map<String,Integer> update) {
		this.distances = update;
		view.updateView(distances);
	}
	
	// Update source in view and triger redraw
	public void updateSource() {
		view.updateSource(this.source);
	}
	
	// Update target in view and trigger redraw
	public void updateTarget() {
		view.updateTarget(this.target);
	}
	
	//Set the model state to running
	public void setRunningState(boolean flagRunning) {
		this.flagRunning = flagRunning;
	}

	/**
	 * Sets all the weights between all vertices and their adjacent vertices to be the default distance
	 * Not optimized at current 
	 * 
	 * Superseded -> weights now set in itialization
	 */
	//public void setAllWeights() {
		//int count = 0;
		//for(String vertex: graph.vertices()) {
		//	for(String adjacentVertex: AdjacencyHelper.getAdjacentVertices(vertex, WIDTH, HEIGHT)) {
			//	if(graph.vertices().contains(adjacentVertex)){
				//	graph.set(vertex, adjacentVertex, DEFAULT_DISTANCE_STRAIGHT);
			//		System.out.println(count);
			//		count++;
		//			
			//	}
		//	}
	//	}
	//	System.out.println("Graph finished initilization");
//	}

	/*
	 * Checks if given x,y co-ord for source are in the graph, and then updates the source if so
	 * If null is passed to x or y, source will be reset to null
	 * If current source values are given to x and y, source will be reset to null
	 * @param x - x position of coord
	 * @param y	- y position of coords
	 * @return True if source is set, false if source was not changed
	 */
	public boolean setSource(Integer x, Integer y) {
		if(x == null) {
			this.source = null;
			System.out.println("Source updated to null");
			return true;
		}
		String newSource =  LabelHelper.makeLabel(x,y);
		if(graph.vertices().contains(newSource)){
			if(this.source != null && this.source.equals(newSource)) {
				this.source = null;
				System.out.println("Source updated to null");
			}
			else {
				this.source = newSource;
				System.out.println("Source updated to: "+ x + y);
	
			}
			updateSource();
			return true;
		}
		updateSource();
		return false;
	}

	/*
	 * Checks if given x,y co-ord for target are in the graph, and then updates the target if so
	 * If null is passed to x or y, target will be reset to null
	 * If current target values are given to x and y, target will be reset to null
	 * @param x - x position of coord
	 * @param y	- y position of coords
	 * @return true if target is set, false if target was not changed
	 */
	public boolean setTarget(Integer x, Integer y) {
		if(x == null) {
			this.target = null;
			System.out.println("Target updated to null");
			return true;
		}
		String newTarget =  LabelHelper.makeLabel(x,y);
		if(graph.vertices().contains(newTarget)){
			if(this.target!=null && this.target.equals(newTarget)) {
				this.target = null;
				System.out.println("Target updated to null");
			}
			else {
				this.target = newTarget;
				System.out.println("Target updated to: "+ x + y);
			}
			updateTarget();
			return true;
		}
		updateTarget();
		return false;
	}

	//sets the speed to the value specified in the argument. throw error if speed is negative
	public void setSpeed(int newSpeed) {
		if(newSpeed<=0) {
			throw new IllegalArgumentException("Speed smaller than or equal to 0 set");
		}
		else {
			this.speedSetting = newSpeed;
			if(updater!=null) {
				updater.updateTimeDelay(newSpeed);
			}
		}
	}

	/**
	 * Sets the specified co-ordinate as blocked, so that the search algorithm does not try to access it
	 * this is achieved by directly removing the vertex from the graph, hence removing all connections.
	 * This method assumes that the vertex is in the graph, and that the x/y coords are viable 
	 * @param x, integer x in the 
	 * @param y
	 * @return true if vertex is successfully removed, else false
	 */
	public boolean setBlock(Integer x, Integer y) {
		updateSourceTargetIfNeeded(x, y);
		String vertex = LabelHelper.makeLabel(x, y);
		Set<String> adjacents = AdjacencyHelper.getAdjacentVertices(vertex, WIDTH, HEIGHT);
		for(String neighbor:adjacents) {
			graph.set(vertex, neighbor, 0);
		}
		view.block(vertex);
		return true;
		//return(graph.remove(LabelHelper.makeLabel(x, y)));
	}
	
	public void updateSourceTargetIfNeeded(Integer x, Integer y) {
		String vertex = LabelHelper.makeLabel(x, y);
		if(vertex.equals(source)) {
			setSource(null,null);
			updateSource();
		}
		else if(vertex.equals( target)) {
			setTarget(null,null);
			updateTarget();
		}
		return;
	}
	/**
	 * Unsets the block at a specified co-ord, so that the search algorithm tries to access it
	 * This method assumes that the specified co-ordiate is not in the graph, ie. that it has been blocked
	 * previously.
	 * This is achieved by re-adding the co-ordinate to the graph, and resetting the connections to 
	 * adjacent vertices
	 * @param x
	 * @param y
	 * @return
	 * True if the un-set is successful else false
	 */
	public boolean unsetBlock(Integer x, Integer y) {
		updateSourceTargetIfNeeded(x, y);
		String vertex = LabelHelper.makeLabel(x, y);
		Set<String> horizontalAdjacents = AdjacencyHelper.getHorizontalAdjacentVertices(vertex, WIDTH, HEIGHT);
		for(String adjacentVertex: AdjacencyHelper.getAdjacentVertices(vertex, WIDTH, HEIGHT)) {
			if(horizontalAdjacents.contains(adjacentVertex)){
				graph.set(vertex, adjacentVertex, DEFAULT_DISTANCE_STRAIGHT);
			}
			else {
				graph.set(vertex, adjacentVertex, DEFAULT_DISTANCE_DIAGONAL);
			}
		}
		view.unblock(vertex);
		return true;
	}

	//Clears the model for complete refresh
	public void clearModel() {
		init(WIDTH,HEIGHT);
	}
	
	/**
	 * Method to begin the search of the graph
	 * Checks whether the search should be source to target or source to whole graph
	 * creates the algorithm and updater objects
	 * creates the blocking queue through which updates will be pushed
	 * starts the updater on the EDT
	 */
	//TODO abstract this into seperate class -> possibly RunSearchAlgorithm
	public void startSearch() {
		//set un-editable -> this should be done in the controller though right?
		// create the search algorithm
		//create the graph update processor
		//start the algorithm and process the updates
		if(source == null) {
			controller.stopRunning();//remove when stateController set
			stateController.setState(StateController.PRE_SEARCH);
			return;
		}
		//TODO make this method choose the algorithm based on the user selection
		//TODO make this method set the time delay based on the view settings (eg. sliding scale)
		UpdatingSearchAlgorithm algorithm = new DjirkstaSearchUpdating(graph); 
		updater = new UpdateProcessor(this);
		ArrayBlockingQueue<GraphUpdate> updateQueue = new ArrayBlockingQueue<>(1000);
		algorithm.setUpdateQueue(updateQueue);
		updater.setUpdateQueue(updateQueue, speedSetting);
		runner = new RunSearchAlgorithm(updater,source, target, algorithm, graphLock,this);
		
		//begin the update processor thread on the EDT
		updater.execute();
		//begin the algorithm thread
		runner.execute();
		
	}
	
	//calls resume on updater, returning success or failure of resume event
	//TODO abstract to RunSearchAlgorithm
	public boolean pauseSearch() {
		return updater.pauseButtonPressed();
	}
	
	//calls pause on updater, returning success or failure of pause event
	public boolean resumeSearch() {
		return updater.resumeFromPause();
	}
	//tell the controller that the search is completed
	public void searchEnded() {
		//controller.stopRunning();
		//StateController.setState(DrawPath) -> called in view already (correct!)
		runner.cancel(false);
		updater.cancel(false);
	}
	
	//tell the controller that the search is completed, and update the path to target field
	//with the provided path
	public void searchEnded(List<String> pathToTarget) {
		//TODO tell controller search ended
		this.pathToTarget=pathToTarget;
		view.pushEndPath(pathToTarget);
		searchEnded();
		//Tell Controller to stop running
		//Tell view to show path to target
	}
	//Resets the model by recreating complete graph
	//IF called from pause, will also cancel all updaters and algorthims
	public void resetModel() {
		if(stateController.getState() == StateController.PAUSED) {
			this.updater.cancel(true);
			this.runner.cancel(false);
		}
		init(WIDTH,HEIGHT);
	}
	
	/**
	 * Method to return a set of the adjacent vertices on the 2D place.
	 * Adjacent vertices are those that are within the horizontal, diagonal and vertical single step distance
	 * vertices are adjacent regardless of whether connected by an edge
	 * @param vertex
	 * @return
	 * Set of the adjacent vertices
	 */
	//SUPERSEDED by AdjacencyHelper
	//public Set<String> getAdjacentVertices(String vertex){
		//if(!vertex.matches("[0-9]+,{1}[0-9]+")) {
			//somehow passed a vertex string that is not of the form "x,y"
		//	throw new IllegalArgumentException("somehow passed a vertex string that is not of the form \"x,y\"");
	//	}
	//	int x = Integer.parseInt(vertex.split(",")[0]);
	//	int y = Integer.parseInt(vertex.split(",")[1]);
		
	//	List<Integer> possibleX = Arrays.asList(new Integer[] {x-1,x,x+1});
	//	List<Integer> possibleY = Arrays.asList(new Integer[] {y-1,y,y+1});
		
	//	Set<String> adjacents = new HashSet<>();
		
	//	for(Integer newX:possibleX) {
		//	if(checkCoords(newX,0)) {
	//			for(Integer newY:possibleY) {
			//		if(checkCoords(newX,newY)) {
				//		adjacents.add(newX + "," + newY);
				//	}
			//	}
	//		}
	//	}//
	//	adjacents.remove(vertex);
	//	Set<String> adjacentVertices = adjacents.stream().
	//			filter(n-> vertices.contains(n)).
	//			collect(Collectors.toSet());
	//	return adjacentVertices;
//	}
	
	//getters for view
	public String getSource() {
		return this.source;
	}

	public String getTarget() {
		return this.target;
	}

	public Map<String,Integer> getDistances(){
		return new HashMap<>(this.distances);
	}

	//Test methods ///////////////////////////////////
	public void setGraph(Graph<String> graph) {
		this.graph = graph;
	}

	public Graph<String> getGraph() {
		return this.graph;
	}
	
	public int getSpeed() {
		return this.speedSetting;
	}

	/**
	 * Method to update the view colour controller with the calculated max distance.
	 * max distance is either the final distance from source to array (so that the last
	 * update of the view shows red->green) or 10000 reporesenting any poss
	 * @param i
	 */
	public void pushNewMaxDistance(int newDistance) {
		updater.pauseDistance();
		view.pushNewMaxDistance(newDistance);
	}

	public void maxDistanceUpdateComplete() {
		updater.resumeDistance();
	}
}
