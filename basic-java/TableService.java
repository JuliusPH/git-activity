import java.util.Random;

public class TableService{

	private TableModel tableModel = new TableModel();
	private int minAsciiValue = 32;
	private int maxAsciiValue = 127;

	public void createTable(int rowSize, int columnSize){
		String[][] table = new String[rowSize][columnSize];
		for(int row = 0; row < rowSize; row++) {
			for(int column = 0; column < columnSize; column++) {
				table[row][column] = randomAsciiCharacters();
			}
		}
		tableModel.setTable(table);
	}

	public void search(String searchText){
		int count = 0;
		for(int row = 0; row < tableModel.getTable().length; row++){
			outerloop: for(int column = 0; column < tableModel.getTable()[0].length; column++){
				int occurence = 0;
				for(int index = 0; index <= 3 - searchText.length(); index++){
					if(tableModel.getTable()[row][column].substring(index, index + searchText.length()).equals(searchText)){
						occurence++;
						count++;
					}
				}
				if(occurence > 0){
					System.out.println("\n'" + searchText 
											 + "' can be found at row:" + row 
											 + " column:" + column 
											 + " with " + occurence + " occurence/s");
					continue outerloop;
				}
			}
		}
		if(count <= 0) {
			System.out.println("\n'" + searchText + "' is not found");		
		}
	}

	public void edit(int rowToEdit, int columnToEdit, String replacementValue){
		String previousValue = tableModel.getTable()[rowToEdit][columnToEdit];
		tableModel.getTable()[rowToEdit][columnToEdit] = replacementValue;
		System.out.println("\nValue at row:" + rowToEdit + " column:" + columnToEdit 
											 + " has changed " + previousValue + " -> " + replacementValue);
	}

	public void print(){
		System.out.println();
		for(int row = 0; row < tableModel.getTable().length; row++) {
			for(int column = 0; column < tableModel.getTable()[0].length; column++) {
				System.out.print(tableModel.getTable()[row][column] + "\t");
			}
			System.out.println();
		}
	}

	private String randomAsciiCharacters(){
		return (char)(minAsciiValue + new Random().nextInt(maxAsciiValue - minAsciiValue)) + "" + 
			   (char)(minAsciiValue + new Random().nextInt(maxAsciiValue - minAsciiValue)) + "" + 
			   (char)(minAsciiValue + new Random().nextInt(maxAsciiValue - minAsciiValue));
	}

	public TableModel getModel(){
		return tableModel;
	}
}
