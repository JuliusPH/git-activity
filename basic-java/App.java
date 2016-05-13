public class App{
	
	public static void main(String[] args){
		new App().start();
	}
	
	public void start(){
		TableService tableService = new TableService();
		Menu menu = new Menu(tableService);
		menu.display(false);
	}	
}
