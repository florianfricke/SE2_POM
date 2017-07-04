/**
 * 
 */
package types;
import java.time.LocalDate;
import java.util.*;

import javafx.beans.property.*;

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
	private LocalDate releaseDate;
	private LocalDate completionDate;
	private LocalDate dueDate;
	private LocalDate startDate;
	private LocalDate actualDeliveryDate;
	private IntegerProperty lotSize;
	private IntegerProperty priority;
	private StringProperty comment;
	private OrderLotChanges orderLotChanges;
	private IntegerProperty delay;
	
	/**
	 * Empty Constructor
	 */
	
	public Order() {
		this("","","","","",0,0,State.PLANNED.toString(),"",LocalDate.now(),emptyDate,emptyDate,emptyDate,emptyDate,emptyDate,0,0,"");
	}

	
	
	public Order(String orderno,String customerid, String addressid, String contactid, String product, 
			double price, int volume, String state, String baseLotId, LocalDate orderdate, LocalDate startDate, LocalDate releaseDate, LocalDate completionDate,
			LocalDate dueDate, LocalDate actualDeliveryDate, int lotSize, int priority, String comment) {
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
		this.startDate = startDate;
		this.releaseDate = releaseDate;
		this.completionDate = completionDate;
		this.dueDate = dueDate;
		this.actualDeliveryDate = actualDeliveryDate;
		this.lotSize = new SimpleIntegerProperty(lotSize);
		this.priority = new SimpleIntegerProperty(priority);
		this.comment = new SimpleStringProperty(comment);
		this.delay = new SimpleIntegerProperty();
		setOrderLotChange();
	}
	
	public Order(Order order){
		this(order.ordernoProperty().get(),
		order.customeridProperty().get(),
		order.addressidProperty().get(),
		order.contactidProperty().get(),
		order.productProperty().get(),
		order.priceProperty().get(),
		order.volumeProperty().get(),
		order.stateProperty().get(),
		order.baseLotIdProperty().get(),
		order.getOrderDate(),
		order.getStartDate(),
		order.getReleaseDate(),
		order.getCompletionDate(),
		order.getDueDate(),
		order.getActualDeliveryDate(),
		order.lotSizeProperty().get(),
		order.priorityProperty().get(),
		order.commentProperty().get());	
	}
	
	public StringProperty ordernoProperty() {return orderno;}
	public StringProperty customeridProperty() {return customerid;}
	public StringProperty addressidProperty() {return addressid;}
	public StringProperty contactidProperty() {return contactid;}
	public StringProperty productProperty() {return product;}
	public DoubleProperty priceProperty() {return price;}
	public IntegerProperty volumeProperty() {return volume;}
	public StringProperty stateProperty() {return new SimpleStringProperty(state.name());}
	public void setState(State s){this.state = s;}
	public StringProperty baseLotIdProperty() {return baseLotId;}
	public LocalDate getOrderDate() {return orderDate;}
	public void setOrderDate(LocalDate date) {this.orderDate = date;}
	public LocalDate getStartDate() {return startDate;}
	public void setStartDate(LocalDate date) {this.startDate = date;}
	public LocalDate getReleaseDate() {return releaseDate;}
	public void setReleaseDate(LocalDate date) {this.releaseDate = date;}
	public LocalDate getCompletionDate() {return completionDate;}
	public void setCompletionDate(LocalDate date) {this.completionDate = date;}
	public LocalDate getDueDate() {return dueDate;}
	public void setDueDate(LocalDate date) {this.dueDate = date;}
	public LocalDate getActualDeliveryDate() {return actualDeliveryDate;}
	public void setActualDeliveryDate(LocalDate date) {this.actualDeliveryDate = date;}
	public IntegerProperty lotSizeProperty() {return lotSize;}
	public IntegerProperty priorityProperty() {return priority;}
	public StringProperty commentProperty() {return comment;}
	public OrderLotChanges getOrderLotChanges(){return this.orderLotChanges;}
	public List < Lot > getLots() {return null;}
	public IntegerProperty delayProperty(){return delay;}
	
	public void setOrderLotChange(){
		this.orderLotChanges = new OrderLotChanges(this.volume.get(), this.priority.get(), this.dueDate);
	}
	
	public void copy(Order order){
		this.orderno.set(order.ordernoProperty().get());
		this.customerid.set(order.customeridProperty().get());
		this.addressid.set(order.addressidProperty().get());
		this.contactid.set(order.contactidProperty().get());
		this.product.set(order.productProperty().get());
		this.price.set(order.priceProperty().get());
		this.volume.set(order.volumeProperty().get());
		this.state = State.valueOf(order.stateProperty().get());
		this.baseLotId.set(order.baseLotIdProperty().get());
		this.setOrderDate(order.getOrderDate());
		this.setStartDate(order.getStartDate());
		this.setReleaseDate(order.getReleaseDate());
		this.setCompletionDate(order.getCompletionDate());
		this.setDueDate(order.getDueDate());
		this.setActualDeliveryDate(order.getActualDeliveryDate());
		this.lotSize.set(order.lotSizeProperty().get());
		this.priority.set(order.priorityProperty().get());
		this.comment.set(order.commentProperty().get());	
	}
	

}
