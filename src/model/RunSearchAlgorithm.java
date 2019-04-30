package model;

import java.util.Comparator;

import javax.swing.SwingWorker;

import updatingSearchAlgorithms.UpdatingSearchAlgorithm;

public class RunSearchAlgorithm extends SwingWorker<Void,Integer>{
	
	private String source;
	private  String target;
	private final UpdatingSearchAlgorithm algorithm;
	private final boolean findingTarget;
	public Object lock;
	private SAVModel model;
	
	
	public RunSearchAlgorithm(UpdateProcessor updateProcessor, String source, String target, UpdatingSearchAlgorithm algorithm, Object lock, SAVModel model) {
		this.lock = lock;
		this.model = model;
		if(target == null) {
			findingTarget = false;
		}
		else { 
			this.target = target;
			findingTarget = true;
		}
		this.source = source;
		this.algorithm = algorithm;
	}
	/**
	 * Push the newly determined max distance to the controller for update
	 */
	@Override
	public void done() {
		//return the highest found distance value, not equal to the unvisited placeholder
		//which will either be the distance to the target, or the distance to the farthest visited
		//node
		//pauseUpdateProcessor.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
		
		System.out.println("Search finished, pushing update, updater paused");
		int newMaxDistance = algorithm.getDistances()
				.values().stream()
				.filter(line -> Integer.MAX_VALUE!= line)
				.max(Comparator.comparing(Integer::valueOf))
				.get();
		model.pushNewMaxDistance(newMaxDistance);
							
		System.out.println("Search finished, pushing update, updater resumed");
		//resumeUpdateProcessor.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
	}
	
	//@Override
	//public void run() {
	//	synchronized (this.lock) {
			//if(findingTarget) {
				//algorithm.searchForTarget(source, target);
		//	}
		//	else {
		//		algorithm.searchWholeGraph(source);
			//}
	//	//}
//	}

	/**
	 * Runs algorithm in the background, and pushes the final distance out when completed. This is then used
	 * to update the 
	 */
	@Override
	protected Void doInBackground() {
	//	synchronized (this.lock) {
		//try {
			if(findingTarget) {
				algorithm.searchForTarget(new String(source), new String(target));
			}
			else {
				algorithm.searchWholeGraph(new String(source));
				//a
			}
			return null;
	//	}
		}
		//catch(InterruptedException ie) {
			//System.out.println("Runner exited");
			//return null;
	//	}
}
