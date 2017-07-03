package tests;

import pom_db_interface.*;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
  public boolean updateCustomer(Customer cust); +
  public List<Customer> getCustomerList(); +
  public boolean deleteCustomer(String id); +
  public List<Order> getOrderList(); +
  public List<Address> getAddressList(String custId); +
  public List<Contact> getContactList(String custId); +
  public List<BankAccount> getBankAccountList(String custId); +
  public boolean addOrder(Order order);+
  public boolean updateOrder(Order order);
  boolean deleteOrder(String orderno);+
  */

  //function for creating a test customer
  private Customer addTestCustomer(){
    IPomDbService pomPersistance = new PomDbService();
    Customer testCustomer = new Customer("","Test customer","A","");
    pomPersistance.addCustomer(testCustomer);
    String testCustomerID = testCustomer.idProperty().get();
    //insert one testCustomer's address
    Address testCustomerAddress = new Address( "",
					    					  testCustomerID, 
					                          "Hochschulstr.", 
					                          "32", 
					                          "01069", 
					                          "Dresden", 
					                          "Gernamy", 
					                          false);
   
   // ObservableList<Address> addressList = FXCollections.observableArrayList();
    List<Address> addressList=new ArrayList<Address>();
    addressList.add(testCustomerAddress);
    testCustomer.setAddressList(FXCollections.observableList(pomPersistance.getAddressList(testCustomerID)));
    
    //insert one testCustomer's contact
    Contact testCustomerContact = new Contact("",
					    			          testCustomerID, 
					    					  "Herr",
					                          "Name", 
					                          "First name", 
					                          "personal", 
					                          "121344", 
					                          "email",
					                          false);
    //ObservableList<Contact> contactList = FXCollections.observableArrayList();
    List<Contact> contactList=new ArrayList<Contact>();
    contactList.add(testCustomerContact);
    testCustomer.setContactList(FXCollections.observableList(pomPersistance.getContactList(testCustomerID)));
    //insert one testCustomer's bank account
    BankAccount testCustomerBankAccount = new BankAccount ( "",
							                                "IBAN", 
							                                "BIC", 
							                                "BankName");
    //ObservableList<BankAccount> bankAccountList = FXCollections.observableArrayList();
    List<BankAccount> bankAccountList=new ArrayList<BankAccount>();
    bankAccountList.add(testCustomerBankAccount);
    testCustomer.setBankAccountList(FXCollections.observableList(pomPersistance.getBankAccountList(testCustomerID))); 
    pomPersistance.updateCustomer(testCustomer);
    return testCustomer;
  }
  


  /***************************************************************/
  /*                        addCustomer()                        */
  //Standard case
  @Test (timeout = 10000)
  public void testAddCustomer(){
    IPomDbService pomPersistance = new PomDbService();
    Customer testCustomer = new Customer("","Test customer","A","Here is test customer");
    boolean expecting = true;
    boolean result=pomPersistance.addCustomer(testCustomer);
    pomPersistance.deleteCustomer(testCustomer.idProperty().get());
    assertEquals(expecting,result);
  }
  
  //Check if customer data are inserted to DB   
  @Test (timeout=10000)
  public void dataTestAddCustomer(){
      IPomDbService pomPersistance = new PomDbService();
      Customer testCustomer = addTestCustomer();  
      String testCustomerID = testCustomer.idProperty().get();
      Customer customerFromDB=null;
      boolean result = false;
      try {
        stmt = con.prepareStatement("SELECT * FROM customer Where id ='"+testCustomerID+"'");
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
          customerFromDB = new Customer( rs.getString("id"),  rs.getString("companyname"), rs.getString("ranking"),rs.getString("comment"));
        
        }
        rs.close();
          stmt.close();   
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        ErrorLog.write(e);
      }
      
      if(testCustomer.idProperty().get().equals(customerFromDB.idProperty().get())&&
         testCustomer.nameProperty().get().equals(customerFromDB.nameProperty().get())&&
         testCustomer.rankingProperty().get().equals(customerFromDB.rankingProperty().get().trim())&&
         testCustomer.commentProperty().get().equals(customerFromDB.commentProperty().get().trim()))
    
      result=true;
      
      pomPersistance.deleteCustomer(testCustomerID);
    
      assertEquals(true,result);
  } 
    
  /***************************************************************/ 
  /*                        deleteCustomer()                     */
  //Standard
  //@Test(timeout = 10000)
  public void testDeleteCustomer(){
    IPomDbService pomPersistance = new PomDbService();
    Customer testCustomer = addTestCustomer();
    PreparedStatement stmt = null;
    String customerToDeleteID = testCustomer.idProperty().get();
    boolean result = pomPersistance.deleteCustomer(customerToDeleteID);
    assertEquals(true,result);
  }
  
  //Prove if there are still data in the DB
  @Test
  public void dataTestDeleteCustomer(){
    IPomDbService pomPersistance = new PomDbService();
    Customer testCustomer = addTestCustomer();
    PreparedStatement stmt = null;
    String customerToDeleteID = testCustomer.idProperty().get();
      
    pomPersistance.deleteCustomer(customerToDeleteID);
    
    //really deleted from the database?
    ResultSet realResult=null;
    String result="";
    try {
      stmt = con.prepareStatement("SELECT * FROM Customer WHERE id='"+customerToDeleteID+"'");
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
  @Test (timeout=10000)
  public void testDeleteCustomerWithOrders(){
	  IPomDbService pomPersistance = new PomDbService();
	    PreparedStatement stmt = null;
	    String customerID = "";
	    ResultSet rs=null;
	    //get customerID
	    try {
	      stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test Customer'");
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
    boolean result = pomPersistance.deleteCustomer(customerID);
    assertEquals(false,result);
    
  }
  
  /***************************************************************/
  /*                           addOrder()                        */
  //Standard
  //!Before running this test, create new customer with name "Test Customer" and insert all data(address,contact,bank)
  @Test  ( timeout = 10000 )
  public void testAddOrder(){
    IPomDbService pomPersistance = new PomDbService();
    PreparedStatement stmt = null;
    String customerID = "";
    String orderID = "";
    ResultSet rs=null;
    //get customerID
    try {
      stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test Customer'");
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
    LocalDate emptyDate = null; 
    LocalDate dueDate = LocalDate.of(2017, 12, 12);
    Order testOrder = new Order("",customerID,addressID,contactID,"B200",123,10,State.PLANNED.name(),"Abc",
    		LocalDate.now(),LocalDate.now(),emptyDate,emptyDate,dueDate,emptyDate,10,1,"");
    
    boolean result = pomPersistance.addOrder(testOrder);
    pomPersistance.deleteOrder(orderID);
    assertEquals(true,result);
  
  }
  /***************************************************************/
  /*                     updateOrder()                           */
  //Standard
  @Test  ( timeout = 10000 )
  public void testUpdateOrder(){
	  	IPomDbService pomPersistance = new PomDbService();
	    PreparedStatement stmt = null;
	    String customerID = "";
	    ResultSet rs=null;
	    //get customerID
	    try {
	      stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test Customer'");
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
	    LocalDate emptyDate = null; 
	    LocalDate dueDate = LocalDate.of(2017, 12, 12);
	    
	    Order testOrder = new Order("",customerID,addressID,contactID,"B200",123,10,State.PLANNED.name(),"Abc",
	    		LocalDate.now(),LocalDate.now(),emptyDate,emptyDate,dueDate,emptyDate,10,1,"");
	    
	    pomPersistance.addOrder(testOrder);
	    
	    LocalDate newDueDate = LocalDate.of(2017, 12, 20);
	    testOrder.setDueDate(newDueDate);
	    pomPersistance.updateOrder(testOrder);
	    String orderID=testOrder.ordernoProperty().get();
	    String realDueDate ="";
	      try {
	        stmt = con.prepareStatement("SELECT * FROM pom.order Where orderno ='"+orderID+"'");
	        rs = stmt.executeQuery();
	        while (rs.next())
	        {
	          realDueDate = rs.getString("duedate");
	        }
	        rs.close();
	          stmt.close();   
	      } catch (SQLException e) {
	        // TODO Auto-generated catch block
	        ErrorLog.write(e);
	      }
	      pomPersistance.deleteOrder(orderID);
	      assertEquals(testOrder.getDueDate().toString(),realDueDate.toString());
  }
    
  /***************************************************************/
  /*                        deleteOrder()                        */
  //Standard
  @Test ( timeout = 10000 )
  public void testDeleteOrder(){    
	  IPomDbService pomPersistance = new PomDbService();
	  PreparedStatement stmt = null;
	  String customerID = "";
	  ResultSet rs=null;
	  //get customerID
	  try {
	    stmt = con.prepareStatement("SELECT * FROM Customer WHERE companyname='Test Customer'");
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
	  LocalDate emptyDate = null; 
	  LocalDate dueDate = LocalDate.of(2017, 12, 12);
	  Order testOrder = new Order("",customerID,addressID,contactID,"B200",123,10,State.PLANNED.name(),"Abc",
	  		LocalDate.now(),LocalDate.now(),emptyDate,emptyDate,dueDate,emptyDate,10,1,"");
	  String orderID=testOrder.ordernoProperty().get();
	  pomPersistance.addOrder(testOrder);
	  boolean result = pomPersistance.deleteOrder(orderID);
	  assertEquals(true,result);
  }
  /***************************************************************/
  /*                     getCustomerList()                       */
  //es wird geprüft, ob die Anzahl der DS aus der DB 
  //mit der Anzahl der Listenelementen übereinstimmt
  @Test (timeout = 10000)
  public void testGetCustomerList(){
	List<Customer> custList = new ArrayList<Customer>();
	IPomDbService pomPersistance = new PomDbService();
	PreparedStatement stmt = null;
	int count = 0;
	try {
		stmt = con.prepareStatement("SELECT COUNT(*) FROM Customer");
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
		{
		   count=rs.getInt("count");
		}
		rs.close();
	    stmt.close();
	    
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
	custList=pomPersistance.getCustomerList();
			
	assertEquals(count,custList.size());
		
  }
  /***************************************************************/
  /*                     getOrderList()                          */
  //Standard
  @Test (timeout = 10000)
  public void testGetOrderList(){
	List<Order> orderList = new ArrayList<Order>();
	IPomDbService pomPersistance = new PomDbService();
	PreparedStatement stmt = null;
	int count = 0;
	try {
		stmt = con.prepareStatement("SELECT COUNT(*) FROM pom.order");
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
		{
		   count=rs.getInt("count");
		}
		rs.close();
	    stmt.close();
	    
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
	orderList=pomPersistance.getOrderList();
			
	assertEquals(count,orderList.size());
	
  }
  /***************************************************************/
  /*                     getAddressList()                        */
  //Standard
  @Test (timeout = 10000)
  public void testGetAddressList(){
	Customer testCustomer = addTestCustomer();
	String custId = testCustomer.idProperty().get();
	List<Address> addressList = new ArrayList<Address>();
	IPomDbService pomPersistance = new PomDbService();
	PreparedStatement stmt = null;
	int count = 0;
	try {
		stmt = con.prepareStatement("SELECT COUNT(*) FROM address WHERE customerid='"+custId+"'");
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
		{
		   count=rs.getInt("count");
		}
		rs.close();
	    stmt.close();
	    
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
	pomPersistance.deleteCustomer(custId);
	
	addressList=pomPersistance.getAddressList(custId);
			
	assertEquals(count,addressList.size());
  }
  /***************************************************************/
  /*                     getContactList()                        */
  //Standard
  /*                     getContactList()                        */
  //Standard
  @Test (timeout=10000)
  public void testGetContactList(){	
	Customer testCustomer = addTestCustomer();
	String custId = testCustomer.idProperty().get();
	List<Contact> contactList = new ArrayList<Contact>();
	IPomDbService pomPersistance = new PomDbService();
	PreparedStatement stmt = null;
	int count = 0;
	try {
		stmt = con.prepareStatement("SELECT COUNT(*) FROM contact WHERE customerid='"+custId+"'");
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
		{
		   count=rs.getInt("count");
		}
		rs.close();
	    stmt.close();
	    
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
	pomPersistance.deleteCustomer(custId);
	
	contactList=pomPersistance.getContactList(custId);
			
	assertEquals(count,contactList.size());
  }
  /***************************************************************/
  /*                     getBankAccountList()                    */
  //Standard
  /*                     getBankAccountList()                    */
  //Standard
  @Test (timeout=10000)
  public void testGetBankAccountList(){
	Customer testCustomer = addTestCustomer();
	String custId = testCustomer.idProperty().get();
	List<BankAccount> bankAccountList = new ArrayList<BankAccount>();
	IPomDbService pomPersistance = new PomDbService();
	PreparedStatement stmt = null;
	int count = 0;
	try {
		stmt = con.prepareStatement("SELECT COUNT(*) FROM bankaccount WHERE customerid='"+custId+"'");
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
		{
		   count=rs.getInt("count");
		}
		rs.close();
	    stmt.close();
	    
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
	pomPersistance.deleteCustomer(custId);
	
	bankAccountList=pomPersistance.getBankAccountList(custId);
			
	assertEquals(count,bankAccountList.size());

  }
  /***************************************************************/
  /*                     updateCustomer()                        */
  //Standard
  @Test ( timeout = 10000 )
  public void testUpdateCustomer(){
	  IPomDbService pomPersistance = new PomDbService();
      Customer testCustomer = addTestCustomer();
      String newName="newTest";
      testCustomer.nameProperty().set(newName);
      String testCustomerID = testCustomer.idProperty().get();
      pomPersistance.updateCustomer(testCustomer);
      String realName="";
      try {
        stmt = con.prepareStatement("SELECT * FROM customer Where id ='"+testCustomerID+"'");
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
          realName = rs.getString("companyname");
        
        }
        rs.close();
          stmt.close();   
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        ErrorLog.write(e);
      }
      pomPersistance.deleteCustomer(testCustomerID);
      assertEquals(newName,realName);
  }
  /***************************************************************/
  /*                     updateCustomer()                        */

    
  
  private boolean openConnection(){
    try{
       Class.forName("org.postgresql.Driver");
       //current schema is set as 'pom'
           con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mes?currentSchema=pom","postgres", "0815");
           return true;
    }catch(Exception e){
      ErrorLog.write(e);
      System.err.println(e.getClass().getName()+": "+e.getMessage());
    }
    return false;
  }
}
