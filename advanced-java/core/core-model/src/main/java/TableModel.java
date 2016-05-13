import java.util.LinkedHashMap;
import java.util.List;

public class TableModel{

	private List<LinkedHashMap<String, String>> table;
	
	public List<LinkedHashMap<String, String>> getTable(){
		return table;
	}

	public void setTable(List<LinkedHashMap<String, String>> table){
		this.table = table;
	}

}
