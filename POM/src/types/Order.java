/**
 * 
 */
package types;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

/**
 * @author Konstantin
 *
 */
public class Order {



	private StringProperty orderno;
	//private Address address;
	//private Contact contact;
	private StringProperty customerid;
	private StringProperty adressid;
	private StringProperty contactid;
	private StringProperty product;
	private DoubleProperty price;
	private IntegerProperty volume;
	private StringProperty state;
	private StringProperty baseLotId;
	private StringProperty orderDate;
	private StringProperty releaseDate;
	private StringProperty completionDate;
	private StringProperty dueDate;
	private StringProperty actualDeliveryDate;
	private IntegerProperty lotSize;
	private IntegerProperty priority;
	private StringProperty comment;
	
	/**
	 * Empty Constructor
	 */
	
	public Order() {
		
	}

	
	public Order(String orderno,String customerid, String adressid, String contactid, String product, 
			double price, int volume, String state, String baseLotId, String orderDate, String releaseDate,	 String completionDate,
			String dueDate, String actualDeliveryDate, int lotSize, int priority, String comment) {
		this.orderno = new SimpleStringProperty(orderno);
		this.customerid = new SimpleStringProperty(customerid);
		this.adressid = new SimpleStringProperty(adressid);
		this.contactid = new SimpleStringProperty(contactid);
		this.product = new SimpleStringProperty(product);
		this.price = new SimpleDoubleProperty(price);
		this.volume = new SimpleIntegerProperty(volume);
		this.state = new SimpleStringProperty(state);
		this.baseLotId = new SimpleStringProperty(baseLotId);
		this.orderDate = new SimpleStringProperty(orderDate);
		this.releaseDate = new SimpleStringProperty(releaseDate);
		this.completionDate = new SimpleStringProperty(completionDate);
		this.dueDate = new SimpleStringProperty(dueDate);
		this.actualDeliveryDate = new SimpleStringProperty(actualDeliveryDate);
		this.lotSize = new SimpleIntegerProperty(lotSize);
		this.priority = new SimpleIntegerProperty(priority);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public StringProperty ordernoProperty() {
		return orderno;
	}
	public StringProperty customeridProperty() {
		return customerid;
	}
	public StringProperty adressidProperty() {
		return adressid;
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
		return state;
	}
	public StringProperty baseLotIdProperty() {
		return baseLotId;
	}
	public StringProperty orderDateProperty() {
		return orderDate;
	}
	public StringProperty releaseDateProperty() {
		return releaseDate;
	}
	public StringProperty completionDateProperty() {
		return completionDate;
	}
	public StringProperty dueDateProperty() {
		return dueDate;
	}
	public StringProperty c() {
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
	
	public List < Lot > getLots() {
		return null;
	}
	
	public boolean release() {
		return false;
	}
}
