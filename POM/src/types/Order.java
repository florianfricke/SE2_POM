/**
 * 
 */
package types;
import java.util.*;

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
	private int baseLotId;
	private Date orderDate;
	private Date releaseDate;
	private Date completionDate;
	private Date dueDate;
	private int priority;
	private String comment;
	private int lotSize;
	private Date actualDeliveryDate;
	//private List < Lot > lotList;
	
	public Order(String iD, String product, int volume, String state, Date orderDate,
				 Date releaseDate, Date completionDate, Date dueDate, int priority)
	{
		
	}
	public List < Lot > getLots() {
		return null;
	}
	
	public boolean release() {
		return false;
	}
	
}
