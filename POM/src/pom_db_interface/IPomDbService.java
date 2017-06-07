package pom_db_interface;
import java.util.ArrayList;
import java.util.List;

import types.*;

public interface IPomDbService {
	public boolean addCustomer(Customer cust);
	public List<Customer> getCustomerList();
	public boolean deleteCustomer(String id);
	public List<Order> getOrderList();
	public List<Address> getAddressList(String custId);
	public List<Contact> getContactList(String custId);
	public List<BankAccount> getBankAccountList(String custId);
	
}
