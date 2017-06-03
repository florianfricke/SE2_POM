/**
 * 
 */
package types;
import java.util.Date;
import java.util.List;

/**
 * @author Konstantin
 *
 */
public class Order {

	private String iD;
	//private Address address;
	//private Contact contact;
	private String product;
	private double price;
	private int volume;
	private String state;
	private String baseLotId;
	private Date orderDate;
	private Date releaseDate;
	private Date completionDate;
	private Date dueDate;
	private int priority;
	private String comment;
	private int lotSize;
	private Date actualDeliveryDate;

	/**
	 * Constructor for Order Type
	 * @param iD
	 * @param product
	 * @param price
	 * @param volume
	 * @param state
	 * @param baseLotId
	 * @param orderDate
	 * @param releaseDate
	 * @param completionDate
	 * @param dueDate
	 * @param priority
	 * @param comment
	 * @param lotSize
	 * @param actualDeliveryDate
	 */
	
	
	
	public Order(String iD, String product, double price, int volume, String state, String baseLotId, Date orderDate,
			Date releaseDate, Date completionDate, Date dueDate, int priority, String comment, int lotSize,
			Date actualDeliveryDate) {
		
		this.iD = iD;
		this.product = product;
		this.price = price;
		this.volume = volume;
		this.state = state;
		this.baseLotId = baseLotId;
		this.orderDate = orderDate;
		this.releaseDate = releaseDate;
		this.completionDate = completionDate;
		this.dueDate = dueDate;
		this.priority = priority;
		this.comment = comment;
		this.lotSize = lotSize;
		this.actualDeliveryDate = actualDeliveryDate;
	}
	

	public Order() {
		super();
	}



	//private List < Lot > lotList;
	public List < Lot > getLots() {
		return null;
	}
	
	public boolean release() {
		return false;
	}

	public String getiD() {
		return iD;
	}

	public String getProduct() {
		return product;
	}

	public double getPrice() {
		return price;
	}

	public int getVolume() {
		return volume;
	}

	public String getState() {
		return state;
	}

	public String getBaseLotId() {
		return baseLotId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public int getPriority() {
		return priority;
	}

	public String getComment() {
		return comment;
	}

	public int getLotSize() {
		return lotSize;
	}

	public Date getActualDeliveryDate() {
		return actualDeliveryDate;
	}
	
}
