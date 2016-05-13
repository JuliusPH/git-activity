import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import org.apache.commons.lang3.RandomStringUtils;

public class TableService{

	private TableModel tableModel = new TableModel();
	private File tableFile;
	private HashSet<String> keySet = new HashSet<String>();
	private Random r = new Random();
	private List<LinkedHashMap<String, String>> tempTable = new ArrayList<LinkedHashMap<String, String>>();

	public void loadFile(File tableFile){
		this.tableFile = tableFile;

		boolean hasReset = false;

		try {
			Scanner fileScanner = new Scanner(tableFile);
			int row = 0;
		    mainloop:while (fileScanner.hasNextLine()){
				String[] line = fileScanner.nextLine().split("\t\t");
				tempTable.add(new LinkedHashMap<>());
				for(String entry : line){
					String[] keyValue = entry.split("\t");
					try{
						tempTable.get(row).put(keyValue[0], keyValue[1]);
					}
					catch(ArrayIndexOutOfBoundsException e){
						System.out.println("\nNot a valid text file, converting it...");
						resetTable(2, 2);
						hasReset = true;
						break mainloop;
					}
				}
				row++;
		    }
			tableModel.setTable(tempTable);
		} 
		catch(FileNotFoundException e){
		    e.printStackTrace();
		}
		updateKeySet();
		if(!hasReset){
			printTable();
		}
	}

	public void search(String searchText){
		int occurencesInKey = 0;
		int occurencesInValue = 0;
		int count = 0;
		for(int row = 0; row < tableModel.getTable().size(); row++){
			int column = 0;
			for (Map.Entry<String, String> entry : tableModel.getTable().get(row).entrySet()){
				occurencesInKey = 0;
				occurencesInValue = 0;
				for(int index = 0; index <= 3 - searchText.length(); index++){
					if(entry.getKey().substring(index, index + searchText.length()).equals(searchText)){
						occurencesInKey++;
						count++;
					}
					if(entry.getValue().substring(index, index + searchText.length()).equals(searchText)){
						occurencesInValue++;
						count++;
					}
				}

				if(occurencesInKey > 0){
					System.out.println("\n'" + searchText + "' can be found at row:" + row + " column:" + column + " in key with " + occurencesInKey + " occurence/s");
				}

				if(occurencesInValue > 0){
					System.out.println("\n'" + searchText + "' can be found at row:" + row + " column:" + column + " in value with " + occurencesInValue + " occurence/s");
				}
				column++;
			}
      	}
		if(count <= 0){
			System.out.println("\n'" + searchText + "' is not found");
		}
		printTable();
	}

	public void edit(int rowToEdit, int columnToEdit, boolean toEditKey, String replaceText){
		tempTable = new ArrayList<LinkedHashMap<String, String>>();
		String previousText = "";
		if(toEditKey){
			previousText = tableModel.getTable().get(rowToEdit).keySet().toArray()[columnToEdit].toString();
		}
		else{
			previousText = tableModel.getTable().get(rowToEdit).values().toArray()[columnToEdit].toString();
		}

		for(int row = 0; row < tableModel.getTable().size(); row++){
			tempTable.add(new LinkedHashMap<>());
			int column = 0;
			for (Map.Entry<String, String> entry : tableModel.getTable().get(row).entrySet()){
				if(row == rowToEdit && column == columnToEdit){
					if(toEditKey){
						tempTable.get(row).put(replaceText, entry.getValue().toString());
					}
					else{
						tempTable.get(row).put(entry.getKey().toString(), replaceText);
					}
				}
				else{
					tempTable.get(row).put(entry.getKey().toString(), entry.getValue().toString());
				}
				column++;
			}
      	}
		tableModel.setTable(tempTable);
		updateKeySet();
		updateFile();
		
		System.out.println("\n" + (toEditKey ? "Key" : "Value") + " at row:" + rowToEdit + " column:" + columnToEdit + " has changed " + previousText + " -> " + replaceText);
		printTable();
	}

	public void printTable(){
		System.out.println();
		for(int row = 0; row < tableModel.getTable().size(); row++){
			for (Map.Entry<String, String> entry : tableModel.getTable().get(row).entrySet()){
				 System.out.print(entry.getKey() + "\t" + entry.getValue() + "\t\t");
			}
          	System.out.println();
      	}
	}

	public void addRow(){
		tempTable = new ArrayList<LinkedHashMap<String, String>>();
		tempTable.addAll(tableModel.getTable());
		tempTable.add(new LinkedHashMap<String, String>());
	}

	public void addEntry(String key, String value, boolean isLast){
		tempTable.get(tempTable.size() - 1).put(key, value);
		updateKeySet();
		if(isLast){
			tableModel.setTable(tempTable);
			updateFile();
			printTable();
		}
	}

	public void sortTable(){
		tempTable = new ArrayList<LinkedHashMap<String, String>>();
		for(int row = 0; row < tableModel.getTable().size(); row++){
			Map<String, String> sortedMap = new TreeMap<String, String>(tableModel.getTable().get(row));
			tempTable.add(new LinkedHashMap<String, String>(sortedMap));
	  	}
		tableModel.setTable(tempTable);
		Collections.sort(tableModel.getTable(), 
							(firstMap, secondMap) -> 
							firstMap.entrySet().iterator().next().getKey()
							.compareTo(secondMap.entrySet().iterator().next().getKey()));
		updateFile();
		printTable();
	}

	public void resetTable(int rowCount, int columnCount){
		tempTable = new ArrayList<LinkedHashMap<String, String>>();
		keySet.clear();
		for(int row = 0; row < rowCount; row++) {
			tempTable.add(new LinkedHashMap<>());
			for(int column = 0; column < columnCount; column++) {
				String randomKey = RandomStringUtils.randomAscii(3);
				while(keySet.contains(randomKey)){
					randomKey = RandomStringUtils.randomAscii(3);
				}
				keySet.add(randomKey);
				String randomValue = RandomStringUtils.randomAscii(3);
				tempTable.get(row).put(randomKey, randomValue);
			}
		}
		tableModel.setTable(tempTable);
		updateFile();
		printTable();
	}

	private void updateKeySet(){
		keySet.clear();
		for(int row = 0; row < tempTable.size(); row++){
			for (Map.Entry<String, String> entry : tempTable.get(row).entrySet()) {
				 keySet.add(entry.getKey());
			}
      	}
	}

	public HashSet getKeySet(){
		return keySet;
	}

	public TableModel getModel(){
		return tableModel;
	}

	private void updateFile(){
		String text = "";
		for(int row = 0; row < tableModel.getTable().size(); row++){
			for (Map.Entry<String, String> entry : tableModel.getTable().get(row).entrySet()) {
				text += entry.getKey() + "\t" + entry.getValue() + "\t\t";
			}
          	text += "\n";
      	}
		FileWriter fileWriter;
		try{
 			fileWriter = new FileWriter(tableFile, false);
			fileWriter.write(text);
			fileWriter.close();
		} 
		catch(IOException e){
		    e.printStackTrace();
		}
	}
}
