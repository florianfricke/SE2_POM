package types;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Route {
	private StringProperty seq;
	private StringProperty product;
	private StringProperty route;
	private StringProperty operation;
	private StringProperty description;
	private StringProperty lotCount;
	private List<Operation> operList;
	
	
	public Route(String seq, String product, String route,String op, String desc, String lotCount){
		this.seq = new SimpleStringProperty(seq);
		this.product = new SimpleStringProperty(product);
		this.route = new SimpleStringProperty(route);
		this.operation = new SimpleStringProperty(op);
		this.description = new SimpleStringProperty(desc);
		this.lotCount = new SimpleStringProperty(lotCount);

	}
	public StringProperty seqProperty() {return seq;}
	public StringProperty productProperty() {return product;}
	public StringProperty routeProperty() {return route;}
	public StringProperty operationProperty() {return operation;}
	public StringProperty descriptionProperty() {return description;}
	public StringProperty lotCountProperty() {return lotCount;}
	public void setOperList(List<Operation> operList){
		this.operList = operList;
	}
	
	public List<Operation> getOperList(){
		return this.operList;
	}

}
