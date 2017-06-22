package pom_db_interface;
import java.util.List;


import types.*;

public interface IPomDbService {
	public boolean addCustomer(Customer cust);
	public boolean updateCustomer(Customer cust);
	public List<Customer> getCustomerList();
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
	public Setup getSetup();
	public Customer getCustomer(String customerId);
	public boolean upsertSetup(Setup setup);
	public boolean hasSetup();
}
