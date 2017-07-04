package pom_db_interface;
import java.util.List;


import types.*;

public interface IPomDbService {
	/**
	 * Inserts a Customer into the Database and all related Address-, Contact- or Bank account data

	 * @param cust - Instance of type Customer
	 * @return boolean for success or failure
	 * @author Markus Höfgen
	 * @version 1.0
	 */
	public boolean addCustomer(Customer cust);
	/**
	 * Updates a Customer and all Address-, Contact-, or Bancaccount data in the Database if data was chaged.
	 * The function also handles added or deleted Addresses, Contacts or Bankaccounts.
	 * @param cust - Instance of type Customer
	 * @return boolean for success or failure
	 * @author Markus Höfgen
	 * @version 1.0
	 */
	public boolean updateCustomer(Customer cust);
	/**
	 * Returns a List of all Customers in Database without the related Addresses, Contacts, Bankaccounts
	 * @return List of Customers
	 * @version 1.0
	 */
	public List<Customer> getCustomerList();
	/**
	 * Deletes a Customer from the Database and all related Address-, Contact- or Bank account data
	 * @version 1.0
	 * @param id - (String) Id of the Customer
	 * @return boolean for success or failure
	 * @author Markus Höfgen
	 */
	public boolean deleteCustomer(String id);
	public List<Order> getOrderList();
	public List<Order> getCustomerOrder(String customerID);
	public List<Order> getCustomerOrderHistory(String customerID);
	public List<Address> getAddressList(String custId);
	public List<Contact> getContactList(String custId);
	public List<BankAccount> getBankAccountList(String custId);
	public boolean addOrder(Order order);
	public boolean updateOrder(Order order);
	boolean deleteOrder(String orderno);
	/**
	 * Returns a Setup Object 
	 * @version 1.0
	 * @return Setup - Instance of type Setup
	 * @author Markus Höfgen
	 */
	public Setup getSetup();
	/**
	 * Returns a Customer from the Database with all related Addresses, Contacts, Bankaccounts
	 * @return List of Customers
	 * @author Markus Höfgen
	 * @version 1.0
	 */
	public Customer getCustomer(String customerId);
	/**
	 * Inserts or Updates a new or existing Setup Dataset
	 * @version 1.0
	 * @param setup - Instance of type Setup
	 * @return boolean for success or failure 
	 * @author Markus Höfgen
	 */
	public boolean upsertSetup(Setup setup);
	/**
	 * Checks if a Setup Dataset has already been created   
	 * @version 1.0
	 * @return boolean - true if Setup existing, false if not
	 * @author Markus Höfgen
	 */
	public boolean hasSetup();
	/**
	 * Checks whether an Address ID or a Contact ID is referenced from any Order
	 * @param id to check
	 * @param column - Name of the Order Column in Database Table order
	 * @return checks, if a specific foreign key is inserted in order table
	 */
	public boolean isReferenced(String id, String column);
}
