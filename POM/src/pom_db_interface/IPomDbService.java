package pom_db_interface;
import java.util.List;

import types.*;

public interface IPomDbService {
	public boolean addCustomer(Customer cust);
	public List<Customer> getCustomerList();
	public boolean deleteCustomer(String id);
	public List<Order> getOrderList();
}
