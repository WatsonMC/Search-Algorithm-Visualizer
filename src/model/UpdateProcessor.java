package model;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import updatingSearchAlgorithms.GraphUpdate;

public class UpdateProcessor extends SwingWorker<Void,GraphUpdate>{
	
	/**Thread safety
	 * Concurrent access only for timeDelay variable
	 * - Time delay set and get locked behind synchronized operations
	 * - updatingDistance boolean is volatile and accessed only via property change listeners
	 * 
	 */
	
	private ArrayBlockingQueue<GraphUpdate> updateQueue;
	
	//pausing flags
	private volatile boolean updatingDistace;
	private volatile boolean pausePressed;
	
	//Max and min update time settings in ms, provided update time
	public static final long MAX_UPDATE_TIME = 10000;//10 seconds
	public static final long MIN_UPDATE_TIME = 1;	//1 tenth of a second
	private long timeDelay;
	
	//model object
	private SAVModel model;
	
	//iteration count integer
	private int iterationCount;

	
	public UpdateProcessor(SAVModel model) {
		this.model = model;
	}
	
	// pause updating to push max distance change, return true if changed to paused state. 
	public synchronized final  boolean pauseDistance() {
		System.out.println("pause for distance update called on updater");

		if(!updatingDistace && !isDone()) {
			updatingDistace = true;
			return true;
		}
		return false;
	}
	//Pause updating due to button press from user
	public synchronized final  boolean pauseButtonPressed() {
		System.out.println("pause button pressed, pause update called on updater");

		if(!pausePressed && !isDone()) {
			pausePressed = true;
			return true;
		}
		return false;
	}
	
	//resume from paused state due to max distancechange, return false if already resumed
	public synchronized final boolean resumeDistance() {
		System.out.println("Resume from distance update called on updater");
		if(updatingDistace && !isDone()) {
			updatingDistace = false;
			return true;
		}
		return false;
	}
	
	//Resume method from a pause button press
	public synchronized final boolean resumeFromPause() {
		System.out.println("Resume from pause called on updater");
		if(pausePressed && !isDone()) {
			pausePressed = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Method to set the update queue from which this object should process
	 * @param updateQueue
	 * @param timeDelay, the time delay between when updates are pushed to the model in ms
	 */
	public void setUpdateQueue(ArrayBlockingQueue<GraphUpdate> updateQueue, int timeDelay) {
		this.updateQueue = updateQueue;
		if(timeDelay<MIN_UPDATE_TIME) {this.timeDelay = MIN_UPDATE_TIME;}
		else if(timeDelay>MAX_UPDATE_TIME) {this.timeDelay = MAX_UPDATE_TIME;}
		else {this.timeDelay = timeDelay;}
	}
	
	// Method to update time delay of 
	public synchronized void updateTimeDelay(int newTimeDelay) {
		this.timeDelay = newTimeDelay;
	}
	
	public synchronized long getTimeDelay() {
		return this.timeDelay;
	}
	
	/**
	 * Method to send extracted distance updates to the model object
	 * @param updatedDistances
	 */
	public void updateModel(GraphUpdate update) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.updateDistances(update.getDistances());				
			}
		});
	}
	
	public void sendEnd(List<String> pathToTarget) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.searchEnded(pathToTarget);				
			}
		});
	}
	
	public void sendEnd() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.searchEnded();				
			}
		});
	}
	
	public void initialise() {
		this.iterationCount = 0;
	}
	
	/**
	 * Method to check the order of incoming messages
	 * @param update
	 * @return true if current message order is expected, else false
	 */
	public boolean checkOrder(GraphUpdate update) {
		if(update.iterationNumber != iterationCount) {
			return false;
		}
		else {
			iterationCount++;
			return true;
		}
	}
	
	public void processUpdate(GraphUpdate update) throws InterruptedException {
		 if(update.type == GraphUpdate.END) {
			 if(update.getPathToTarget() == null) {
				 System.out.println("End mesage recieved with no route to target");
				 sendEnd();
			 }
			 else {
				 System.out.println("End mesage recieved with path");
				 sendEnd(update.getPathToTarget());
			 }
			 return;
		 }
		 if(!checkOrder(update)) {
			 throw new IllegalArgumentException("Updates out of order: Expected, recieved  = " + iterationCount + "," + update.iterationNumber);
		 }
		 else {
			 //update graph, pause for delayed amount of time before sending next update
			// publish(update);	05.04.2019 MW removed, think this was wrong way to try to solve
			 updateModel(update);
			 
			TimeUnit.MILLISECONDS.sleep(getTimeDelay());
		 }
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try {
				GraphUpdate update = updateQueue.take();
				if(update.type!= GraphUpdate.START) {
					throw new IllegalArgumentException("First update in queue is not start message");
				}
				else {
					initialise();
					int SILPM = 0;	//stop loop at 10000 iterations, Sophisticated Infinite Loop Prevention Mechanism
					while(SILPM<100000) {
					SILPM++;
						 update = updateQueue.take();
						 System.out.println("update: " + SILPM);
						 while(updatingDistace || pausePressed) {
							 Thread.sleep(200);//pause procssing while the max distance update is occuring
							 System.out.println("pause thread called");
						 }
						 if(update.type == GraphUpdate.END) {
							 if(update.getPathToTarget() == null) {
								 System.out.println("End mesage recieved with no path");
								 sendEnd();
							 }
							 else {
								 System.out.println("End mesage recieved with path");
								 sendEnd(update.getPathToTarget());
							 }
							 break;
						 }
						 if(!checkOrder(update)) {
							 throw new IllegalArgumentException("Updates out of order: Expected, recieved  = " + iterationCount + "," + update.iterationNumber);
						 }
						 else {
							 //update graph, pause for delayed amount of time before sending next update
							 updateModel(update);
							TimeUnit.MILLISECONDS.sleep(getTimeDelay());
						 }
					}
				}
		}
		catch(InterruptedException ie) {
			System.out.println("interuption in updateProcessor");
			ie.printStackTrace();
			return null;
		}
		return null;
		
		// TODO Auto-generated method stub
		//initialise to recieve updates
			//read first object
			//confirm is start
			//reset iteration counter
			//set loop up to stop when stop message recieved
		//run
			//get each graph object
			// check iteration/is end
			// send update to model
		//terminate
			//if is end
			//send end to model 
	}
	
}
