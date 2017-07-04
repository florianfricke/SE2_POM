package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Operation {
	private StringProperty operation;
	private StringProperty description;
	private StringProperty lotCount;
	
	
	public Operation(String op, String desc, String lotCount){
		this.operation = new SimpleStringProperty(op);
		this.description = new SimpleStringProperty(desc);
		this.lotCount = new SimpleStringProperty(lotCount);
	}
	public StringProperty operationProperty() {return operation;}
	public StringProperty descriptionProperty() {return description;}
	public StringProperty lotCountProperty() {return lotCount;}
}
