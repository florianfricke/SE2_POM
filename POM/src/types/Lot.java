package types;

import javafx.beans.property.*;

public class Lot {
	private StringProperty id;
	private IntegerProperty priority;
	private IntegerProperty lotSize;
	private StringProperty state;
	private StringProperty product;
	private StringProperty customerId;
	private StringProperty orderNo;
	private StringProperty dueDate;
	
	public Lot(String id, int priority, int lotSize, String state, String product, String customerId, String orderNo, String dueDate){
		this.id = new SimpleStringProperty(id);
		this.priority = new SimpleIntegerProperty(priority);
		this.lotSize = new SimpleIntegerProperty(lotSize);
		this.state = new SimpleStringProperty(state);
		this.product = new SimpleStringProperty(product);
		this.customerId = new SimpleStringProperty(customerId);
		this.orderNo = new SimpleStringProperty(orderNo);
		this.dueDate = new SimpleStringProperty(dueDate);
	}
	
	public StringProperty idProperty() {
		return id;
	}
	public IntegerProperty priorityProperty() {
		return priority;
	}
	public IntegerProperty lotSizeProperty() {
		return lotSize;
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
	
}
