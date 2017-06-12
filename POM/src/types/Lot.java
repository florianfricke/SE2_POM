package types;

import javafx.beans.property.*;

public class Lot {
	private StringProperty id;
	private IntegerProperty priority;
	private IntegerProperty pieces;
	private StringProperty state;
	private StringProperty product;
	private StringProperty customerId;
	private StringProperty orderNo;
	private StringProperty dueDate;
	private StringProperty startDate;
	
	public Lot(String id, int priority, int pieces, String state, String product, String customerId, String orderNo, String dueDate){
		this.id = new SimpleStringProperty(id);
		this.priority = new SimpleIntegerProperty(priority);
		this.pieces = new SimpleIntegerProperty(pieces);
		this.state = new SimpleStringProperty(state);
		this.product = new SimpleStringProperty(product);
		this.customerId = new SimpleStringProperty(customerId);
		this.orderNo = new SimpleStringProperty(orderNo);
		this.dueDate = new SimpleStringProperty(dueDate);
		this.startDate = new SimpleStringProperty(startDate);
	}
	//eingefügt um Lot am anfang erstellen zu können
	public Lot() {
		this("",0 ,0 ,"","","","","","");
	}
	public StringProperty idProperty() {
		return id;
	}
	public IntegerProperty priorityProperty() {
		return priority;
	}
	public IntegerProperty piecesProperty() {
		return pieces;
	}
	public StringProperty stateProperty() {
		return state;
	}
	public StringProperty productProperty() {
		return product;
	}
	public StringProperty customerIdProperty() {
		return customerId;
	}
	public StringProperty orderNoProperty() {
		return orderNo;
	}
	public StringProperty dueDateProperty() {
		return dueDate;
	}	
	public StringProperty startDateProperty() {
		return startDate;
	}	
	
}
