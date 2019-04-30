package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import graph.Graph;

public class SAVModelMock extends SAVModel{
	//Mock up class to test update processor
	public List<Long> updateDelays = new LinkedList<>();
	public List<Long> updateOffsets = new LinkedList<>();
	Long startTime;
	Long lastTime;
	
	public SAVModelMock() {
		super();
		this.startTime= System.currentTimeMillis();
	}
	
	
	@Override
	public void updateDistances(Map<String, Integer> updates) {
		//recieve update
		//check time
		System.out.println("current time is" + System.currentTimeMillis());
		if(updateDelays.isEmpty()) {
				//Do nothing since this is the first time, so update is instant
		}
		else {
			updateOffsets.add(System.currentTimeMillis() - lastTime);
		}
		lastTime = System.currentTimeMillis();
		updateDelays.add(lastTime-this.startTime);
	}
	
	public List<Long> getUpdateDelays(){
		return this.updateDelays;
	}
	
	public Integer getAverageTimeDelayInDeciSeconds(){
		int average =  0;
		int sum = 0;
		for(int i =0; i<updateOffsets.size(); i++) {
			sum += (updateOffsets.get(i))/10;
		}
		average  = sum / (updateOffsets.size());
		return average;
	}
}
