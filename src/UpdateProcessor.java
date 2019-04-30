import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import model.SAVModel;
import updatingSearchAlgorithms.GraphUpdate;

public class UpdateProcessor implements Runnable{
	private ArrayBlockingQueue<GraphUpdate> updateQueue;
	
	//Max and min update time settings in ms, provided update time
	public static final long MAX_UPDATE_TIME = 10000;//10 seconds
	public static final long MIN_UPDATE_TIME = 100;	//1 tenth of a second
	private long timeDelay;
	
	//model object
	private SAVModel model;
	
	//iteration count integer
	private int iterationCount;

	
	public UpdateProcessor(SAVModel model) {
		this.model = model;
	}

	
	/**
	 * Method to set the update queue from which this object should process
	 * @param updateQueue
	 */
	public void setUpdateQueue(ArrayBlockingQueue<GraphUpdate> updateQueue, int timeDelay) {
		this.updateQueue = updateQueue;
		if(timeDelay<MIN_UPDATE_TIME) {this.timeDelay = MIN_UPDATE_TIME;}
		else if(timeDelay>MAX_UPDATE_TIME) {this.timeDelay = MAX_UPDATE_TIME;}
		else {this.timeDelay = timeDelay;}
	}
	
	/**
	 * Method to send extracted distance updates to the model object
	 * @param updatedDistances
	 */
	public void updateModel(GraphUpdate update) {
		model.updateDistances(update.getDistances());
	}
	
	public void sendEnd() {
		
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
	
	
	@Override
	public void run() throws IllegalArgumentException{
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
						 if(update.type == GraphUpdate.END) {
							 sendEnd();
							 break;
						 }
						 if(!checkOrder(update)) {
							 throw new IllegalArgumentException("Updates out of order: Expected, recieved  = " + iterationCount + "," + update.iterationNumber);
						 }
						 else {
							 //update graph, pause for delayed amount of time before sending next update
							 updateModel(update);
							TimeUnit.MILLISECONDS.sleep(timeDelay);
						 }
					}
				}
		}
		catch(InterruptedException ie) {
			System.out.println("interuption in updateProcessor");
			ie.printStackTrace();
		}
		
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
