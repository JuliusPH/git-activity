public class Gate{
	private int numberOfHorsesInGate = 0;
	private int numberOfHorses;
	
	public void setNumberOfHorses(int numberOfHorses){
		this.numberOfHorses = numberOfHorses;
	}

	public synchronized void waitInGate() throws InterruptedException{
		numberOfHorsesInGate++;
		if(numberOfHorsesInGate < numberOfHorses){
			wait();
		}
		else{
			notifyAll();
			(new Runnable(){
				@Override
				public void run(){
					System.out.println("\nAll horses are in the gate now!\n");
				}
			}).run();
		}
	}
}
