package model;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import model.SAVModel;
import model.SAVModelMock;
import updatingSearchAlgorithms.GraphUpdate;

class TestUpdateProcessor {
	/**
	 * Need:
	 * fake queue to send updates from
	 * - test recieving correct updates
	 * - test incorrect order
	 * - test start and end messaging 
	 * 
	 * fake model 
	 * - to recieve end messages 
	 * - to recieve updates
	 * 
	 * some way of checking that timing is correct
	 * - fake model can poll system time every time it recieved an update
	 * 
	 */
	@Test
	void testTime3s() {
		
		//graph objects
		GraphUpdate gu1 = new GraphUpdate(GraphUpdate.START);
		GraphUpdate gu2 = new GraphUpdate(new HashMap<String,Integer>(), 0);
		GraphUpdate gu3 = new GraphUpdate(new HashMap<String,Integer>(), 1);
		GraphUpdate gu4 = new GraphUpdate(new HashMap<String,Integer>(), 2);
		GraphUpdate gu5 = new GraphUpdate(new HashMap<String,Integer>(), 3);
		GraphUpdate gu6 = new GraphUpdate(new HashMap<String,Integer>(), 4);
		GraphUpdate gu7 = new GraphUpdate(new HashMap<String,Integer>(), 5);
		GraphUpdate gu8 = new GraphUpdate(GraphUpdate.END);
		ArrayBlockingQueue<GraphUpdate> testQueue = new ArrayBlockingQueue<>(10);
		
		try {
			testQueue.put(gu1);
			testQueue.put(gu2);
			testQueue.put(gu3);
			testQueue.put(gu4);
			testQueue.put(gu5);
			testQueue.put(gu6);
			testQueue.put(gu7);
			testQueue.put(gu8);
			
			
			SAVModel testModel = new SAVModelMock();
			int t1 = 3000;
			
			UpdateProcessor testPro = new UpdateProcessor(testModel);
			testPro.setUpdateQueue(testQueue, t1);
			
			Thread testThread = new Thread(testPro);
			testThread.start();
			
			//model should now be changed
			TimeUnit.MILLISECONDS.sleep(30000);	//wait 30 s
			
			List<Long> timeDelays = ((SAVModelMock) testModel).getUpdateDelays();
			for(Long time: timeDelays) {
				System.out.println(time);
			}
			
			int time1 = ((SAVModelMock)testModel).getAverageTimeDelayInDeciSeconds();
			System.out.println("time from run is " + time1 );
			System.out.println("time expectedis " + t1/10);
			assertTrue(time1 == t1/10);
		}
		catch(InterruptedException ie) {
			
		}
	}
	
	@Test
	void testMinTime() {
		
		//graph objects
		GraphUpdate gu1 = new GraphUpdate(GraphUpdate.START);
		GraphUpdate gu2 = new GraphUpdate(new HashMap<String,Integer>(), 0);
		GraphUpdate gu3 = new GraphUpdate(new HashMap<String,Integer>(), 1);
		GraphUpdate gu4 = new GraphUpdate(new HashMap<String,Integer>(), 2);
		GraphUpdate gu5 = new GraphUpdate(new HashMap<String,Integer>(), 3);
		GraphUpdate gu6 = new GraphUpdate(new HashMap<String,Integer>(), 4);
		GraphUpdate gu7 = new GraphUpdate(new HashMap<String,Integer>(), 5);
		GraphUpdate gu8 = new GraphUpdate(GraphUpdate.END);
		ArrayBlockingQueue<GraphUpdate> testQueue = new ArrayBlockingQueue<>(10);
		
		try {
			testQueue.put(gu1);
			testQueue.put(gu2);
			testQueue.put(gu3);
			testQueue.put(gu4);
			testQueue.put(gu5);
			testQueue.put(gu6);
			testQueue.put(gu7);
			testQueue.put(gu8);
			
			
			SAVModel testModel = new SAVModelMock();
			int t1 = (int)UpdateProcessor.MIN_UPDATE_TIME;
			
			UpdateProcessor testPro = new UpdateProcessor(testModel);
			testPro.setUpdateQueue(testQueue, t1);
			
			Thread testThread = new Thread(testPro);
			testThread.start();
			
			//model should now be changed
			TimeUnit.MILLISECONDS.sleep(5000);	//wait 5 s
			
			List<Long> timeDelays = ((SAVModelMock) testModel).getUpdateDelays();
			for(Long time: timeDelays) {
				System.out.println(time);
			}
			
			int time1 = ((SAVModelMock)testModel).getAverageTimeDelayInDeciSeconds();
			System.out.println("time from run is " + time1 );
			System.out.println("time expectedis " + t1/10);
			assertTrue(time1 == t1/10);
		}
		catch(InterruptedException ie) {
			
		}
	}
	
	@Test
	void testMaxTime() {
		
		//graph objects
		GraphUpdate gu1 = new GraphUpdate(GraphUpdate.START);
		GraphUpdate gu2 = new GraphUpdate(new HashMap<String,Integer>(), 0);
		GraphUpdate gu3 = new GraphUpdate(new HashMap<String,Integer>(), 1);
		GraphUpdate gu4 = new GraphUpdate(new HashMap<String,Integer>(), 2);
		GraphUpdate gu5 = new GraphUpdate(new HashMap<String,Integer>(), 3);
		GraphUpdate gu6 = new GraphUpdate(new HashMap<String,Integer>(), 4);
		GraphUpdate gu7 = new GraphUpdate(new HashMap<String,Integer>(), 5);
		GraphUpdate gu8 = new GraphUpdate(GraphUpdate.END);
		ArrayBlockingQueue<GraphUpdate> testQueue = new ArrayBlockingQueue<>(10);
		
		try {
			testQueue.put(gu1);
			testQueue.put(gu2);
			testQueue.put(gu3);
			testQueue.put(gu4);
			testQueue.put(gu5);
			testQueue.put(gu6);
			testQueue.put(gu7);
			testQueue.put(gu8);
			
			
			SAVModel testModel = new SAVModelMock();
			int t1 = (int)UpdateProcessor.MAX_UPDATE_TIME;
			
			UpdateProcessor testPro = new UpdateProcessor(testModel);
			testPro.setUpdateQueue(testQueue, t1);
			
			Thread testThread = new Thread(testPro);
			testThread.start();
			
			//model should now be changed
			TimeUnit.MILLISECONDS.sleep(100000);	//wait 100 s
			
			List<Long> timeDelays = ((SAVModelMock) testModel).getUpdateDelays();
			for(Long time: timeDelays) {
				System.out.println(time);
			}
			
			int time1 = ((SAVModelMock)testModel).getAverageTimeDelayInDeciSeconds();
			System.out.println("time from run is " + time1 );
			System.out.println("time expectedis " + t1/10);
			assertTrue(time1 == t1/10);
		}
		catch(InterruptedException ie) {
			
		}
	}
}
