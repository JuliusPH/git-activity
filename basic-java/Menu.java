import java.util.Scanner;

public class Menu{
	private TableService tableService;
	private Scanner inputScanner = new Scanner(System.in);
	private int rowSize = 0;
	private int columnSize = 0;

	public Menu(TableService tableService){
		this.tableService = tableService;
	}

	public void display(boolean isTableCreated){
		if(!isTableCreated){
			initTable();
		}
		tableService.print();

		System.out.println("\nMenu:\n" + "1-Search " + "2-Edit " + "3-Print " + "4-Reset " + "5-Exit\n");
		int option = 0;
		option = validateNumberInput(option, 1, 5, 
									"Please input a number ranging from 1 to 5: ", 
									"Please input a positive number: ");
		switch(option){
			case 1:
				String searchText = "";
				searchText = validateTextInput(searchText, 1, 3, "Please input 1 to 3 characters: ", "");
				tableService.search(searchText);
				break;

			case 2:
				int rowSize = tableService.getModel().getTable().length - 1;
				int columnSize = tableService.getModel().getTable()[0].length - 1;

				int rowToEdit = -1;
				rowToEdit = validateNumberInput(rowToEdit, 0, 
												tableService.getModel().getTable().length - 1, 
												"Specify the row to be edited (min:0, max:" + rowSize + "): ", 
												"Please input a positive number for the row to be edited (min:0, max:" + rowSize + "): ");

				int columnToEdit = -1;
				columnToEdit = validateNumberInput(columnToEdit, 0, 
												tableService.getModel().getTable()[0].length - 1, 
												"Specify the row to be edited (min:0, max:" + columnSize + "): ", 
												"Please input a positive number for the row to be edited (min:0, max:" + columnSize + "): ");


				String replacementValue = "";
				replacementValue = validateTextInput(replacementValue, 3, 3,
										 "Please input 3 characters with no tabs: ", "");
				tableService.edit(rowToEdit, columnToEdit, replacementValue);
				break;

			case 3:
				tableService.print();
				break;

			case 4:
				initTable();
				break;

			case 5:
				System.exit(0);
				break;

			default:
				break;
		}
		display(true);
	}

	private void initTable(){
		rowSize = 0;
		columnSize = 0;
		rowSize = validateNumberInput(rowSize, 1, -1, 
									"Specify the number of rows of the table: ", 
									"Please input a positive integer for the number of rows: ");
		columnSize = validateNumberInput(columnSize, 1, -1, 
									"Specify the number of columns of the table: ", 
									"Please input a positive integer for the number of rows: ");
		tableService.createTable(rowSize, columnSize);
	}

	private int validateNumberInput(int input, int min, int max, String description, String error){
		while (input < min || (input > max && max != -1)){
			System.out.print(description);
			while(!inputScanner.hasNextInt()){
				System.out.print(error);
				inputScanner.nextLine();
			}
			input = inputScanner.nextInt();
			inputScanner.nextLine();
		}
		return input;
	}

	private String validateTextInput(String input, int min, int max, String description, String error){
		while((input.length() < min || input.length() > max) || input.contains("\t")){
			System.out.print(description);
			input = inputScanner.nextLine();
		}
		return input;
	}
}
