package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import mes_db_interface.*;
import pom_db_interface.*;
import psql_mes_db.MesDbService;
import psql_pom_db.*;
import types.*;

public class JUnitTestCase {
	
	/*Functions to test:
	public boolean addCustomer(Customer cust);
	public boolean updateCustomer(Customer cust);
	public List<Customer> getCustomerList();
	public boolean deleteCustomer(String id);
	public List<Order> getOrderList();
	public List<Address> getAddressList(String custId);
	public List<Contact> getContactList(String custId);
	public List<BankAccount> getBankAccountList(String custId);
	public boolean addOrder(Order order);
	boolean deleteOrder(String orderno);
	public Customer getCustomer(String customerId);
	*/
	

	/***************************************************************/
	/*                        addCustomer()                        */
	//Customer nichts eingegeben
	@Test
	public void testAddCustomerNoParameter(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer();
		boolean result = pomPersistance.addCustomer(testCustomer);
		assertEquals(false,result);
	}
	//Customer Name nicht eingegeben
	@Test
	public void testAddCustomerNoName(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("12345","","A","comment");
		boolean result = pomPersistance.addCustomer(testCustomer);
		assertEquals(false,result);
	}
	//Customer Ranking nicht eingegeben
	@Test
	public void testAddCustomerNoRanking(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("12345","Name","","comment");
		boolean result = pomPersistance.addCustomer(testCustomer);
		assertEquals(false,result);
	}
	//Customer Ranking nicht im Wertebereich
	@Test
	public void testAddCustomerFalseRanking(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("12345","Name","abc","comment");
		boolean result = pomPersistance.addCustomer(testCustomer);
		assertEquals(false,result);
	}
	/***************************************************************/
	
	
	/***************************************************************/
	/*                        deleteCustomer()                     */
	//Beim Löschen des Kundes kein ID eingegeben
	@Test
	public void testDeleteCustomer(){
		IPomDbService pomPersistance = new PomDbService();
		boolean result = pomPersistance.deleteCustomer(null);
		assertEquals(false,result);
	}
	
	//Kunde hat noch offene Fertigungsaufträge, state=...
	//@Test DEAKTIVIERT
	public void testDeleteCustomerWithOrders(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("00013","Test Löschen", "A","");
		pomPersistance.addCustomer(testCustomer);
		Order testOrder = new Order("10000","00013","","","",0,0,"PLANNED","","2017-06-11","","","","",10,0,"");
		pomPersistance.addOrder(testOrder);
		boolean result = pomPersistance.deleteCustomer("00013");
		assertEquals(false,result);
	}
	/***************************************************************/
	
	/***************************************************************/
	/*                        deleteOrder()                     */
	@Test
	//Beim Löschen der Bestellung kein ID eingegeben
	public void testDeleteOrder(){
		IPomDbService pomPersistance = new PomDbService();
		boolean result = pomPersistance.deleteOrder(null);
		assertEquals(false,result);
	}
	/***************************************************************/

	

}
