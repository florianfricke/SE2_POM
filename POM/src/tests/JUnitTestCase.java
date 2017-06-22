package tests;

import pom_db_interface.*;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import psql_pom_db.*;
import types.*;

public class JUnitTestCase {
	
	private Connection con = null;
	PreparedStatement stmt = null;

	public JUnitTestCase(){
		openConnection();
	}
	
	/*functions to test:
	public boolean addCustomer(Customer cust); +
	public boolean updateCustomer(Customer cust);
	public List<Customer> getCustomerList();
	public boolean deleteCustomer(String id); +
	public List<Order> getOrderList();
	public List<Address> getAddressList(String custId);
	public List<Contact> getContactList(String custId);
	public List<BankAccount> getBankAccountList(String custId);
	public boolean addOrder(Order order); +!return false anstatt von true!
	public boolean updateOrder(Order order);
	boolean deleteOrder(String orderno);+
	public int getDayCapacity();
	public Customer getCustomer(String customerId);
	
	releaseOrder
	*/

	//function for creating a test customer
	private Customer addTestCustomer(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("","Test customer","A","Here is test customer");
		pomPersistance.addCustomer(testCustomer);
		String testCustomerID = "";
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test customer'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   testCustomerID =rs.getString("id");
			}
			rs.close();
		    stmt.close();    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//insert one testCustomer's address
		Address testCustomerAddress = new Address(	"13", 
													"Hochschulstr.", 
													"32", 
													"01069", 
													"Dresden", 
													"Gernamy", 
													false);
		
		ObservableList<Address> addressList = FXCollections.observableArrayList();
		//List<Address> addressList=new ArrayList<Address>();
		addressList.add(testCustomerAddress);
		testCustomer.setAddressList((ObservableList<Address>) addressList);
		//insert one testCustomer's contact
		Contact testCustomerContact = new Contact(	"13", 
													"Name", 
													"First name", 
													"personal", 
													"", 
													"");
		ObservableList<Contact> contactList = FXCollections.observableArrayList();
		//List<Contact> contactList=new ArrayList<Contact>();
		contactList.add(testCustomerContact);
		testCustomer.setContactList((ObservableList<Contact>) contactList);
		//insert one testCustomer's bank account
		BankAccount testCustomerBankAccount = new BankAccount (	"13",
																"IBAN", 
																"BIC", 
																"BankName");
		ObservableList<BankAccount> bankAccountList = FXCollections.observableArrayList();
		//List<BankAccount> bankAccountList=new ArrayList<BankAccount>();
		bankAccountList.add(testCustomerBankAccount);
		testCustomer.setBankAccountList((ObservableList<BankAccount>) bankAccountList);	
		
		
		return testCustomer;
	}
	
	

	/***************************************************************/
	/*                        addCustomer()                        */
	//Standard case
	//@Test
	public void testAddCustomer(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("","Test customer","A","Here is test customer");
		boolean expecting = true;
		boolean result=pomPersistance.addCustomer(testCustomer);
		assertEquals(expecting,result);
	}
	
	//Prove if customer data are inserted to DB 
	@Test
	public void dataTestAddCustomer(){
			Customer testCustomer = addTestCustomer();
			IPomDbService pomPersistance = new PomDbService();		
			String nameExpected = testCustomer.toString();
			String nameReal="";
			try {
				stmt = con.prepareStatement("SELECT * FROM customer Where companyname ='"+nameExpected+"'");
				ResultSet rs = stmt.executeQuery();
				while (rs.next())
				{
				nameReal = rs.getString("companyname");	
				}
				rs.close();
			    stmt.close();		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				ErrorLog.write(e);
			}
			assertEquals(nameExpected,nameReal);
	}	
		
	/***************************************************************/	
	/*                        deleteCustomer()                     */
	//Standard
	//@Test
	public void testDeleteCustomer(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("","CustromerToDelete","A","F�r deleteCustomer() test");
		pomPersistance.addCustomer(testCustomer);
		PreparedStatement stmt = null;
		String CustomerToDeleteID = "";
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='CustromerToDelete'");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
			   CustomerToDeleteID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		boolean result = pomPersistance.deleteCustomer(CustomerToDeleteID);
		assertEquals(true,result);
	}
	
	//Prove if there are still data in the DB
	//@Test
	public void dataTestDeleteCustomer(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = new Customer("","CustromerToDeleteData","A","F�r deleteCustomer() test");
		pomPersistance.addCustomer(testCustomer);
		PreparedStatement stmt = null;
		String CustomerToDeleteID = "";
		ResultSet rs=null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='CustromerToDeleteData'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   CustomerToDeleteID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		
		pomPersistance.deleteCustomer(CustomerToDeleteID);
		
		//really deleted from the database?
		ResultSet realResult=null;
		String result="";
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='CustromerToDeleteData'");
			realResult = stmt.executeQuery();
			
			while (realResult.next())
			{
			   result=realResult.getString("id");
			}
			realResult.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//We are expecting no data in the DB
		String expresult ="";
		assertEquals(result,expresult);
	}
	
	//Trying to delete customer with existing orders 
	//!Before running this test, create new customer with name "Test" and insert all data(address,contact,bank)
	//@Test
	public void testDeleteCustomerWithOrders(){
		IPomDbService pomPersistance = new PomDbService();
		PreparedStatement stmt = null;
		String customerToDeleteID = "";
		ResultSet rs=null;
		//get customerID
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   customerToDeleteID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//get addressID
		String addressID="";
		try {
			stmt = con.prepareStatement("SELECT * FROM Address WHERE customerid='"+customerToDeleteID+"'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   addressID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//get contactID
		String contactID="";
		try {
			stmt = con.prepareStatement("SELECT * FROM Contact WHERE customerid='"+customerToDeleteID+"'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   contactID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
				
		Order testOrder = new Order("",customerToDeleteID,addressID,contactID,"",0,0,State.PLANNED.toString(),"",LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),10,0,"");
		
		pomPersistance.addOrder(testOrder);
		
		boolean result = pomPersistance.deleteCustomer(customerToDeleteID);
		assertEquals(false,result);
	}
	
	/***************************************************************/
	/*                           addOrder()                        */
	//Standard
	//@Test
	public void testAddOrder(){
		IPomDbService pomPersistance = new PomDbService();
		PreparedStatement stmt = null;
		String customerID = "";
		ResultSet rs=null;
		//get customerID
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   customerID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//get addressID
		String addressID="";
		try {
			stmt = con.prepareStatement("SELECT * FROM Address WHERE customerid='"+customerID+"'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   addressID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		//get contactID
		String contactID="";
		try {
			stmt = con.prepareStatement("SELECT * FROM Contact WHERE customerid='"+customerID+"'");
			rs = stmt.executeQuery();
			while (rs.next())
			{
			   contactID =rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
				
		Order testOrder = new Order("",customerID,addressID,contactID,"",0,0,State.PLANNED.toString(),"",LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),LocalDate.now(),10,0,"");
		
		boolean result = pomPersistance.addOrder(testOrder);
		
		assertEquals(true,result);
	
	}
		
	/***************************************************************/
	/*                        deleteOrder()                        */
	//Standard
	//@Test
	public void testDeleteOrder(){
		IPomDbService pomPersistance = new PomDbService();
		Customer testCustomer = addTestCustomer();
		String custID=testCustomer.idProperty().toString();
		String orderID = "";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Order WHERE customerid='"+custID+"'");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
			   orderID=rs.getString("id");
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		boolean result = pomPersistance.deleteOrder(orderID);
		assertEquals(true,result);
	}
	/***************************************************************/
	
	
	
	private boolean openConnection(){
		try{
			 Class.forName("org.postgresql.Driver");
	         con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pom","postgres", "0815");
	         return true;
		}catch(Exception e){
			ErrorLog.write(e);
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
		System.out.println("Erfolgreich verbunden!");
		return false;
	}
}
