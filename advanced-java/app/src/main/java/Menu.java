import java.io.File;
import java.util.Scanner;

public class Menu{
	private Scanner inputScanner = new Scanner(System.in);
	private TableService tableService;
	private File tableFile;

	public Menu(TableService tableService, File tableFile){
		this.tableService = tableService;
		this.tableFile = tableFile;
	}

	public void display(boolean hasLoad){
		if(!hasLoad){		
			tableService.loadFile(tableFile);
			hasLoad = false;
		}
		System.out.println("\nMenu:\n" + "1-Search " + "2-Edit " + "3-Print " + "4-Add Row " + "5-Sort " + "6-Reset " + "7-Exit\n");
		int option = 0;
		option = validateNumberInput(option, 1, 7, "Please input a number ranging from 1 to 7: ", "Please input a positive number: ");

		switch(option){
			case 1:
				String searchText = "";
				searchText = validateTextInput(searchText, 1, 3, false, "Please input 1 to 3 characters: ", "");
				tableService.search(searchText);
				break;

			case 2:
				int rowToEdit = -1;
				String description = "Specify the row to be edited (min:0, max:" + (tableService.getModel().getTable().size() - 1) + "): ";
				String error = "Please input a positive number for the row to be edited (min:0, max:" + (tableService.getModel().getTable().size() - 1) + "): ";
				rowToEdit = validateNumberInput(rowToEdit, 0, tableService.getModel().getTable().size() -1, description, error);

				int columnToEdit = -1;
				description = "Specify the column to be edited (min:0, max:" + (tableService.getModel().getTable().get(rowToEdit).size() - 1) + "): ";
				error = "Please input a positive number for the column to be edited (min:0, max:" + (tableService.getModel().getTable().get(rowToEdit).size() - 1) + "): ";
				columnToEdit = validateNumberInput(columnToEdit, 0, tableService.getModel().getTable().get(rowToEdit).size() - 1, description, error);

				int editKey = -1;
				description = "Please input 0 to edit the value, or input 1 to edit the key: ";
				error = "Please input 0 or 1 only: ";
				editKey = validateNumberInput(editKey, 0, 1, description, error);
				boolean toEditKey = (editKey == 1);

				String value = "";
				description = "Please input 3 characters with no tabs: ";
				error = "The inputted key already exists, please change it: ";
				value = validateTextInput(value, 3, 3, toEditKey, description, error);
				tableService.edit(rowToEdit, columnToEdit, toEditKey, value);
				break;

			case 3:
				tableService.printTable();
				break;

			case 4:
				tableService.addRow();
				int columnSize = tableService.getModel().getTable().get(0).size();
				String inputKey = "";
				String inputValue = "";
				error = "The inputted key already exists, please change it: ";
				for(int i = 0; i < columnSize; i++){
					inputKey = "";
					inputKey = validateTextInput(inputKey, 3, 3, true, "Please input 3 characters for the key: ", error);

					inputValue = "";
					inputValue = validateTextInput(inputValue, 3, 3, false, "Please input 3 characters for the value: ", "");
					
					tableService.addEntry(inputKey, inputValue, i == columnSize - 1);
				}
				break;

			case 5:
				tableService.sortTable();
				break;

			case 6:
				int rowCount = 0;
				description = "Specify the number of rows of the table: ";
				error = "Please input a positive integer for the number of rows: ";
				rowCount = validateNumberInput(rowCount, 1, -1, description, error);

				int columnCount = 0;
				description = "Specify the number of columns of the table: ";
				error = "Please input a positive integer for the number of columns: ";
				columnCount = validateNumberInput(columnCount, 1, -1, description, error);
				tableService.resetTable(rowCount, columnCount);
				break;

			case 7:
				System.exit(0);
				break;

			default:
				break;

		}
		display(true);
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

	private String validateTextInput(String input, int min, int max, boolean isKey, String description, String error){
		while((input.length() < min || input.length() > max) || input.contains("\t") || (tableService.getKeySet().contains(input) && isKey)){
			if(isKey){			
				if(tableService.getKeySet().contains(input)){
					System.out.print(error);
				}
				else{
					System.out.print(description);
				}
			}
			else{
					System.out.print(description);
			}
			input = inputScanner.nextLine();
		}
		return input;
	}
}
