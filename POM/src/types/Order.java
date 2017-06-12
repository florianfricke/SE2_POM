/**
 * 
 */
package types;
import java.time.LocalDate;
import java.util.*;

import javafx.beans.property.*;
import javafx.util.converter.LocalDateStringConverter;

/**
 * @author Konstantin
 *
 */
public class Order {



	private static final LocalDate emptyDate = null;
	private StringProperty orderno;
	private StringProperty customerid;
	private StringProperty addressid;
	private StringProperty contactid;
	private StringProperty product;
	private DoubleProperty price;
	private IntegerProperty volume;
	private State state;
	private StringProperty baseLotId;
	private LocalDate orderDate;
	private StringProperty releaseDate;
	private StringProperty completionDate;
	private LocalDate dueDate;
	private StringProperty actualDeliveryDate;
	private IntegerProperty lotSize;
	private IntegerProperty priority;
	private StringProperty comment;
	private OrderLotChanges orderLotChanges;
	
	/**
	 * Empty Constructor
	 */
	
	public Order() {
		this("","","","","",0,0,State.PLANNED.toString(),"",LocalDate.now(),"","",emptyDate,"",10,0,"");
	}

	
	
	public Order(String orderno,String customerid, String addressid, String contactid, String product, 
			double price, int volume, String state, String baseLotId, LocalDate orderdate, String releaseDate, String completionDate,
			LocalDate dueDate, String actualDeliveryDate, int lotSize, int priority, String comment) {
		this.orderno = new SimpleStringProperty(orderno);
		this.customerid = new SimpleStringProperty(customerid);
		this.addressid = new SimpleStringProperty(addressid);
		this.contactid = new SimpleStringProperty(contactid);
		this.product = new SimpleStringProperty(product);
		this.price = new SimpleDoubleProperty(price);
		this.volume = new SimpleIntegerProperty(volume);
		this.state = State.valueOf(state);
		this.baseLotId = new SimpleStringProperty(baseLotId);
		this.orderDate = orderdate;
		this.releaseDate = new SimpleStringProperty(releaseDate);
		this.completionDate = new SimpleStringProperty(completionDate);
		this.dueDate = dueDate;
		this.actualDeliveryDate = new SimpleStringProperty(actualDeliveryDate);
		this.lotSize = new SimpleIntegerProperty(lotSize);
		this.priority = new SimpleIntegerProperty(priority);
		this.comment = new SimpleStringProperty(comment);
		setOrderLotChange();
	}
	
	
	/*public static String GetCurrentDate(){
		//wo geh�rt diese methode hin?
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date date = new Date();
		
		return date;
		
	}*/
	
	
	public StringProperty ordernoProperty() {
		return orderno;
	}
	public StringProperty customeridProperty() {
		return customerid;
	}
	public StringProperty addressidProperty() {
		return addressid;
	}
	public StringProperty contactidProperty() {
		return contactid;
	}
	public StringProperty productProperty() {
		return product;
	}
	public DoubleProperty priceProperty() {
		return price;
	}
	
	public IntegerProperty volumeProperty() {
		return volume;
	}
	public StringProperty stateProperty() {
		return new SimpleStringProperty(state.name());
	}
	public StringProperty baseLotIdProperty() {
		return baseLotId;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate date) {
		this.orderDate = date;
	}
	public StringProperty releaseDateProperty() {
		return releaseDate;
	}
	public StringProperty completionDateProperty() {
		return completionDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate date) {
		this.dueDate = date;
	}
	public StringProperty actualDeliveryDateProperty() {
		return actualDeliveryDate;
	}
	public IntegerProperty lotSizeProperty() {
		return lotSize;
	}
	public IntegerProperty priorityProperty() {
		return priority;
	}
	public StringProperty commentProperty() {
		return comment;
	}
	public OrderLotChanges getOrderLotChanges(){
		return this.orderLotChanges;
	}
	
	public List < Lot > getLots() {
		return null;
	}
	
	public void setOrderLotChange(){
		//this.orderLotChanges = new OrderLotChanges(this.volume.get(), this.priority.get(), new Date(this.dueDate.get()));
	}




}
