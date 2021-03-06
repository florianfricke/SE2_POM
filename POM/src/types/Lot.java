package types;

import java.time.LocalDate;
import javafx.beans.property.*;

public class Lot {
	private StringProperty id;
	private IntegerProperty priority;
	private IntegerProperty pieces;
	private StringProperty state;
	private StringProperty product;
	private StringProperty customerId;
	private StringProperty orderNo;
	private LocalDate dueDate;
	private LocalDate startDate;
	private static final LocalDate emptyDate = null;
	private StringProperty route;
	private StringProperty operation;
	
	public Lot(String id, int priority, int pieces, String state, String product, String customerId, String orderNo, LocalDate dueDate, LocalDate startDate, String route, String oper){
		this.id = new SimpleStringProperty(id);
		this.priority = new SimpleIntegerProperty(priority);
		this.pieces = new SimpleIntegerProperty(pieces);
		this.state = new SimpleStringProperty(state);
		this.product = new SimpleStringProperty(product);
		this.customerId = new SimpleStringProperty(customerId);
		this.orderNo = new SimpleStringProperty(orderNo);
		this.dueDate = dueDate;
		this.startDate = startDate;
		this.route = new SimpleStringProperty(route);
		this.operation = new SimpleStringProperty(oper);
		
	}
	public Lot() {
		this("",0 ,0 ,"","","","",emptyDate,emptyDate,"","");
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
		return new SimpleStringProperty(dueDate.toString());
	}	
	public StringProperty startDateProperty() {
		return new SimpleStringProperty(startDate.toString());
	}	
	public void setStartDate(LocalDate date){
		this.startDate = date;
	}
	public LocalDate getStartDate(){
		return startDate;
	}
	public StringProperty routeProperty() {return route;}
	public StringProperty operationProperty() {return operation;}
	
}
