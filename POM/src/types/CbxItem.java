package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CbxItem{
	private StringProperty id = new SimpleStringProperty();
	private StringProperty name = new SimpleStringProperty();
	
	public CbxItem(String id, String name){
		setId(id);
		setName(name);
	}
	public final StringProperty idProperty() {
		return id;
	}
	 public final String getId() {
	        return id.get();
	    }

	    public final void setId(String id) {
	        this.id.set(id);
	    }

	public final StringProperty nameProperty() {
		return name;
	}
	
	 public final String getName() {
	        return name.get();
	    }

	    public final void setName(String name) {
	        this.name.set(name);
	    }
	

}
