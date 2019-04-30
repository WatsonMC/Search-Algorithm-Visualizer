package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Assert;
import org.junit.Test;

import updatingSearchAlgorithms.GraphUpdate;
import updatingSearchAlgorithms.UpdatingSearchAlgorithm;

class mockAlgorithm implements UpdatingSearchAlgorithm{
	public boolean searchWholeGraphCalled = false;
	public boolean searchForTargetCalled = false;
	
	@Override
	public Map<String, Integer> searchWholeGraph(String source) {
		// TODO Auto-generated method stub
		searchWholeGraphCalled = true;
		return null;
	}

	@Override
	public boolean searchForTarget(String source, String target) {
		// TODO Auto-generated method stub
		searchForTargetCalled = true;
		return false;
	}

	@Override
	public boolean setUpdateQueue(ArrayBlockingQueue<GraphUpdate> updateQueue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayBlockingQueue<GraphUpdate> getUpdateQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<String> findPathToTarget(String source, String target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findDistanceToTarget(String source, String target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPredecessors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getDistances() {
		// TODO Auto-generated method stub
		Map<String, Integer> fakeDist = new HashMap<>();
		fakeDist.put("v1",100);
		fakeDist.put("v2",1000);
		fakeDist.put("v23",1);
		
		return fakeDist;
	}
	
}

class mockModel extends SAVModel{
	public boolean newDistanceRecieved = false;
	@Override 
	public void pushNewMaxDistance(int i) {
		System.out.println("MD update recieved for mock model");
		newDistanceRecieved = true;
	}
}

public class TestRunSearchAlgorithm {
	/**
	 * TO test:
	 * - that correct search algorithm is run
	 * 	- Source and target assigned
	 * 	- source only assigned
	 * 
	 * - that done correctly pushes the max distances
	 *	 - that max distance gets pushed
	 *	 - that distance is correct
	 * @throws InterruptedException 
	 */
	@Test
	public void testCorrectSearchAlgCall() throws InterruptedException {
		mockModel mockModel = new mockModel();
		UpdateProcessor update = new UpdateProcessor(mockModel)	;	
		mockAlgorithm mockAlgo = new mockAlgorithm();
		
		String source = "source"; 
		String target = "target";
		
		Object lock =  new Object();
		RunSearchAlgorithm runner = new RunSearchAlgorithm(update, source, target, mockAlgo, lock, mockModel);
		runner.execute();
		
		//test that search for target was called and that a new distance was pushed to the view 
		long initialTime = System.currentTimeMillis();
		while(!runner.isDone()) {
			long timeout =  System.currentTimeMillis() - initialTime;
			if(timeout>10000) {
				System.out.println("didnt finish");
				break;
			}
		}
		Thread.sleep(1000); //give done() method time to push update
		Assert.assertTrue(mockAlgo.searchForTargetCalled);
		Assert.assertTrue(mockModel.newDistanceRecieved);
		
		mockModel.newDistanceRecieved =false;
		
		
		//test that search whole graph was called and that new max distance was pushed
		runner = new RunSearchAlgorithm(update, source, null, mockAlgo, lock, mockModel);
		runner.execute();
		initialTime = System.currentTimeMillis();
		while(!runner.isDone()) {
			long timeout =  System.currentTimeMillis() - initialTime;
			if(timeout>10000) {
				System.out.println("didnt finish");
				break;
			}
		}
		Thread.sleep(1000); //give done() method time to push update
		Assert.assertTrue(mockAlgo.searchWholeGraphCalled);
		Assert.assertTrue(mockModel.newDistanceRecieved);
		
	}
}
