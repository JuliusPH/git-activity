import java.io.File;
import java.net.URISyntaxException;

public class App{
	private File tableFile;
	private String fileName = "file.txt";
	private String defaultFilePath = "";
	private TableService tableService = new TableService();
	private Menu menu;

	public static void main(String[] args){
		new App().start(args);
	}

	public void start(String[] args){
		try{
			defaultFilePath = new File(App.class
											.getProtectionDomain()
											.getCodeSource()
											.getLocation()
											.toURI()
											.getPath())
											.getParent() + File.separator + fileName;
		}
		catch(URISyntaxException e){
			e.printStackTrace();
		}
		String filePath = "";
		if(args.length > 0){
			filePath = args[0];
			tableFile = new File(filePath);
			System.out.println("\nOpening " + filePath);
			if(!tableFile.exists() || tableFile.isDirectory()){
				System.out.println("\nSpecified file is not found. Using the default file instead.");
				tableFile = new File(defaultFilePath);
			}
		}
		else{
			System.out.println("\nNo file specified, using the default file.");
			tableFile = new File(defaultFilePath);
		}

		menu = new Menu(tableService, tableFile);
		menu.display(false);
	}
}
