import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;

public class Race{
	private Scanner sc = new Scanner(System.in);
	private int numberOfHorses = 0;
	private int numberOfHealthyHorses = 0;
	private int distanceBarnToGate = 10;
	private int distanceGateToFinish = 0;
	private Gate gate = new Gate();
	public static List<Horse> horses = new ArrayList<Horse>();

	public void setupRace(){
		distanceGateToFinish = validateInput(
								distanceGateToFinish, 
								20, 
								-1, 
								"Specify the distance to finish (min: 20): ", 
								"Please input a positive number: ");
		int tryCount = 0;
		while(numberOfHealthyHorses < 2){
			if(tryCount > 0){
				System.out.println("Number of healthy horse is " + numberOfHealthyHorses + ", please try again");
			}
			horses.clear();
			numberOfHorses = 0;
			numberOfHorses = validateInput(
										numberOfHorses, 
										2, 
										-1, 
										"Specify the number of horses (min: 2): ", 
										"Please input a positive number: ");
			for(int i = 1; i <= numberOfHorses; i++){
				horses.add(new Horse("horse" + i, distanceBarnToGate, distanceGateToFinish, gate));
			}
			numberOfHealthyHorses = (int)horses.stream().filter(h -> h.isHealthy()).count();
			tryCount++;
		}
		gate.setNumberOfHorses(numberOfHealthyHorses);
	}
	
	public void startRace(){
		System.out.println("\nQualified Horses (Healthy): ");

		horses.stream()
			  .filter(horse -> horse.isHealthy())
			  .forEach(horse -> {
					System.out.println(horse.getName());
					horse.setName(horse.getName().toUpperCase());
			   });

		System.out.println();

		horses = horses.stream()
					   .filter(horse -> horse.isHealthy())
					   .collect(Collectors.toCollection(ArrayList::new));

		horses.stream()
			  .forEach(horse -> {
				  horse.start();
			  });

		horses.stream()
			  .forEach(horse -> {
				   try{
					   horse.join();
				   }
				   catch(InterruptedException ie){
				   	   ie.printStackTrace();
			   	   }
			   });
	}

	public void finishRace(){
		System.out.println("\nThe race is finished!\nResults:");
		horses = horses.stream()
					   .sorted((horse1, horse2) -> Long.compare(horse1.getFinishTime(), horse2.getFinishTime()))
					   .collect(Collectors.toCollection(ArrayList::new));

		int rank = 1;
		for(Horse h:horses){
			System.out.println(rank + ". " + h.getName() + " (" + h.getFinishTime() + " ms)");
			rank++;
		}
	}
	
	public static List<Horse> getHorses(){
		return Collections.synchronizedList(horses);
	}

	private int validateInput(int input, int min, int max, String desc, String err){
		while (input < min || (input > max && max != -1)){
			System.out.print(desc);
			while(!sc.hasNextInt()){
				System.out.print(err);
				sc.nextLine();
			}
			input = sc.nextInt();
			sc.nextLine();
		}
		return input;
	}
}
