import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class Horse extends Thread{
	private boolean isHealthy = false;
	private int distanceBarnToGate = 0;
	private int distanceGateToFinish = 0;
	private long finishTime = 0;
	private Gate gate;
	private boolean hasGallopMoreThanOnce = false;

	public Horse(String name, int distanceBarnToGate, int distanceGateToFinish, Gate gate){
		super(name);
		this.distanceBarnToGate = distanceBarnToGate;
		this.distanceGateToFinish = distanceGateToFinish;
		this.gate = gate;
		isHealthy = new Random().nextBoolean();
	}
		
	public void run(){
		try{
			barnToGate();
			gate.waitInGate();
			gateToFinish();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	private void barnToGate() throws InterruptedException{
		while(distanceBarnToGate > 0){
			int gallopDistance = new Random().nextInt(10) + 1;
			distanceBarnToGate -= gallopDistance;
			String extraMessage = distanceBarnToGate <= 0 ? "and arrived at the gate" : "- Remaining: " + distanceBarnToGate;
			threadMessage("gallops " + gallopDistance + " " + extraMessage);
			Thread.sleep(10);
		}
	}

	private void gateToFinish() throws InterruptedException{
		while(distanceGateToFinish > 0){
			int trailingHorseDistance = Race.getHorses()
											.stream()
											.sorted((horse1, horse2) -> 
												Integer.compare(horse2.getDistanceGateToFinish(), horse1.getDistanceGateToFinish()))
											.collect(Collectors.toCollection(ArrayList::new))
											.get(0).getDistanceGateToFinish();

			boolean isTrailing = hasGallopMoreThanOnce && distanceGateToFinish == trailingHorseDistance;
			
			int pace = isTrailing ? new Random().nextInt(20) + 1 : new Random().nextInt(10) + 1;
			distanceGateToFinish -= pace;
			String extraMessage = (distanceGateToFinish <= 0 ? "and finish the race!" : "- Remaining: " + distanceGateToFinish)
									+ (isTrailing ? " - TRAILING" : "");
			finishTime = threadMessage("gallops " + pace + " " + extraMessage);
			hasGallopMoreThanOnce = true;
			Thread.sleep(10);
		}
	}

	private long threadMessage(String message){
		long time = System.currentTimeMillis();
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + " (" + time + " ms)" + " " + message);
		return time;
	}

	public boolean isHealthy(){
		return isHealthy;
	}

	public long getFinishTime(){
		return finishTime;
	}

	public int getDistanceGateToFinish(){
		return distanceGateToFinish;
	}
}
