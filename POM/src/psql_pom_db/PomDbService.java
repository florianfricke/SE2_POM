package psql_pom_db;
import pom_db_interface.*;
import types.*;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;



public class PomDbService implements IPomDbService {
	private Connection con = null;
	private static final LocalDate emptyDate = null;
	public PomDbService(){
		openConnection();
	}

	@Override
	public boolean addCustomer(Customer cust) {
		String sql = "";
		ResultSet rs;
		PreparedStatement stmt = null;
		try {
			
			sql = "INSERT INTO Customer VALUES (DEFAULT, ?,?,?)";
			stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1,cust.nameProperty().get());
				stmt.setString(2,cust.rankingProperty().get());
				stmt.setString(3,cust.commentProperty().get());

			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			cust.idProperty().set(rs.getString("id"));
			rs.close();
		    stmt.close();
		    System.out.println("Inserted Customer "+ cust.idProperty());
		    if(!(cust.getAddressList().isEmpty())){ //if adressList was Updated
		    	for (Address addrr : cust.getAddressList()) {
					sql = "INSERT INTO pom.address VALUES (DEFAULT,?,?,?,?,?,?,?)";
					stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);		 	
						stmt.setString(1,cust.idProperty().get());
					 	stmt.setString(2,addrr.streetProperty().get());
						stmt.setString(3,addrr.houseNoProperty().get());
						stmt.setString(4,addrr.cityProperty().get());
						stmt.setString(5,addrr.zipCodeProperty().get());
						stmt.setString(6,addrr.countryProperty().get());
						stmt.setBoolean(7,addrr.billingAddressProperty().get());
					
					stmt.executeUpdate();
					rs = stmt.getGeneratedKeys();
					rs.next();
					addrr.idProperty().set(rs.getString("id"));
					rs.close();
				    stmt.close();
		    	}
		    }
			if(!(cust.getContactList().isEmpty())){
				for (Contact contact : cust.getContactList()) {
			     
				   
					sql = "INSERT INTO pom.contact VALUES (DEFAULT, ?,?,?,?,?,?)";
					stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				    stmt.setString(1,cust.idProperty().get());
					stmt.setString(2,contact.phoneNoProperty().get());
				    stmt.setString(3,contact.nameProperty().get());
					stmt.setString(4,contact.firstNameProperty().get());
					stmt.setString(5,contact.emailProperty().get());
					stmt.setString(6,contact.positionProperty().get());
				
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				rs.next();
				contact.idProperty().set(rs.getString("id"));
				rs.close();
			    stmt.close();
		    	}	    	
			}
			if(!(cust.getBankAccountList().isEmpty())){
				for (BankAccount ba  : cust.getBankAccountList()) {
			    	sql = "INSERT INTO pom.bankaccount(customerid, iban, bic, bankname) VALUES"+ 
			    			"('" + cust.idProperty().get() +"','"+ ba.ibanProperty().get() +"','"+ 
			    			ba.bicProperty().get() +"','"+ 
			    			ba.bankNameProperty().get()+"')";
			    	stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // ist da seine java methode? wo ist die implemtiert?
					stmt.executeUpdate();
					rs = stmt.getGeneratedKeys();
					rs.next();
					ba.idProperty().set(rs.getString("id"));
					rs.close();
				    stmt.close();
				   
				    
				    sql = "INSERT INTO pom.contact VALUES (DEFAULT, ?,?,?,?)";
					stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				    stmt.setString(1,cust.idProperty().get());
					stmt.setString(2,ba.ibanProperty().get());
				    stmt.setString(3,ba.bicProperty().get());
					stmt.setString(4,ba.bankNameProperty().get());
				
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				rs.next();
				ba.idProperty().set(rs.getString("id"));
				rs.close();
			    stmt.close();
		    	}	    	
			}
		    return true;
		} catch (SQLException e) {
			ErrorLog.write(e);
			//ErrorLog.write(e);
		}
		return false;
	}
	
	@Override
	public boolean updateCustomer(Customer cust) { 
		String sql = "";
		ResultSet rs;
		List<Address> dbCustAddressList = getAddressList(cust.idProperty().get());
		List<Contact> dbCustContactList = getContactList(cust.idProperty().get());
		List<BankAccount> dbCustBankAccountList = getBankAccountList(cust.idProperty().get());
		PreparedStatement stmt = null;
		try {
			
			sql = "UPDATE pom.customer SET companyname=?, ranking=?, comment = ?" + 
			"WHERE id = ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, cust.nameProperty().get()); // wenn ein Attribut nicht gesetzt wird wird automatisch NULL eingetragen?
			stmt.setString(2, cust.rankingProperty().get());
			stmt.setString(3, cust.commentProperty().get());
			stmt.setString(4, cust.idProperty().get());
			stmt.executeUpdate();
		    stmt.close();
		    System.out.println("Updated Customer "+ cust.idProperty());
		    if(!(cust.getAddressList().equals(dbCustAddressList))){ 
		    	for (Address addrr : cust.getAddressList()) {
		    		if(addrr.idProperty().get().isEmpty()){
		    			sql = "INSERT INTO pom.address(customerid, street, houseno, city, zipcode, country, billingaddress) VALUES"+ 
				    			"('" + cust.idProperty().get() +"','"+ addrr.streetProperty().get() +"','"+ 
				    			addrr.houseNoProperty().get() +"','"+ 
				    			addrr.cityProperty().get() +"','"+ 
				    			addrr.zipCodeProperty().get() +"','"+ 
				    			addrr.countryProperty().get() +"','"+ 
				    			addrr.billingAddressProperty().get() +"')";
				    	stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						stmt.executeUpdate();
						rs = stmt.getGeneratedKeys();
						rs.next();
						addrr.idProperty().set(rs.getString("id"));
						rs.close();
					    stmt.close();
		    		}else{ 
				    	sql = "UPDATE pom.address SET street = ?, houseno = ?, city = ?, zipcode = ?, country = ?, billingaddress = ?"+ 
				    			"WHERE id = ? AND customerid = ?";
				    	stmt = this.con.prepareStatement(sql);
		    			stmt.setString(1,addrr.streetProperty().get());
		    			stmt.setString(2,addrr.houseNoProperty().get());
		    			stmt.setString(3,addrr.cityProperty().get()); 
		    			stmt.setString(4,addrr.zipCodeProperty().get());
		    			stmt.setString(5,addrr.countryProperty().get());
		    			stmt.setBoolean(6,addrr.billingAddressProperty().get());
		    			stmt.setString(7, addrr.idProperty().get());
		    			stmt.setString(8, cust.idProperty().get());
				    	stmt.executeUpdate();
					    stmt.close();
		    		}
		    	}
	    		//Zu loeschende Elemente heraussuchen
	    		for (Address delAddrr : getAddressList(cust.idProperty().get())) { 
	    			if(!(cust.getAddressList().contains(delAddrr))){
		    			sql = "DELETE FROM pom.address WHERE id = ?"; // add WHERE customerID, if custID later gets PK
				    	stmt = this.con.prepareStatement(sql);
		    			stmt.setString(1, delAddrr.idProperty().get());
				    	stmt.executeUpdate();
					    stmt.close();
	    			}
	    		}
		    }
			if(!(cust.getContactList().equals(dbCustContactList))){
				for (Contact contact : cust.getContactList()) {
					if(contact.idProperty().get().isEmpty()){
						sql = "INSERT INTO pom.contact(customerid, phoneno, name, firstname, mailaddress, \"position\") VALUES"+ 
				    			"('" + cust.idProperty().get() +"','"+ contact.phoneNoProperty().get() +"','"+ 
				    			contact.nameProperty().get() +"','"+ 
				    			contact.firstNameProperty().get() +"','"+ 
				    			contact.emailProperty().get() +"','"+ 
				    			contact.positionProperty().get() +"')";
				    	stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						stmt.executeUpdate();
						rs = stmt.getGeneratedKeys();
						rs.next();
						contact.idProperty().set(rs.getString("id"));
						rs.close();
					    stmt.close();
					}else{ // das else ist wenn?
				    	sql = "UPDATE pom.contact SET phoneno = ?, name = ?, firstname = ?, mailaddress = ?, position = ?"+ 
				    			"WHERE id = ? AND customerid = ?";
				    	stmt = this.con.prepareStatement(sql);
						stmt.setString(1,contact.phoneNoProperty().get());
						stmt.setString(2,contact.nameProperty().get());
						stmt.setString(3,contact.firstNameProperty().get());
						stmt.setString(4,contact.emailProperty().get());
						stmt.setString(5,contact.positionProperty().get());
						stmt.setString(6, contact.idProperty().get());
						stmt.setString(7, cust.idProperty().get());
						stmt.executeUpdate();
					    stmt.close();
					}
				}
				for (Contact delContact : getContactList(cust.idProperty().get())) {
	    			if(!(cust.getContactList().contains(delContact))){
		    			sql = "DELETE FROM pom.contact WHERE id = ?"; // add WHERE customerID, if custID later gets PK
				    	stmt = this.con.prepareStatement(sql);
		    			stmt.setString(1, delContact.idProperty().get());
				    	stmt.executeUpdate();
					    stmt.close();
	    			}
		    	}	    	
			}
			if(!(cust.getBankAccountList().equals(dbCustBankAccountList))){ 
				for (BankAccount ba  : cust.getBankAccountList()) {
					if(ba.idProperty().get().isEmpty()){
				    	sql = "INSERT INTO pom.bankaccount(customerid, iban, bic, bankname) VALUES"+ 
				    			"('" + cust.idProperty().get() +"','"+ ba.ibanProperty().get() +"','"+ 
				    			ba.bicProperty().get() +"','"+ 
				    			ba.bankNameProperty().get()+"')";
				    	stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						stmt.executeUpdate();
						rs = stmt.getGeneratedKeys();
						rs.next();
						ba.idProperty().set(rs.getString("id"));
						rs.close();
					    stmt.close();
					}else{
				    	sql = "UPDATE pom.bankaccount SET iban = ?, bic = ?, bankname = ?"+ 
				    			"WHERE id = ? AND customerid = ?";
				    	stmt = this.con.prepareStatement(sql);
						stmt.setString(1,ba.ibanProperty().get());
						stmt.setString(2,ba.bicProperty().get());
						stmt.setString(3,ba.bankNameProperty().get());
						stmt.setString(4, ba.idProperty().get());
						stmt.setString(5, cust.idProperty().get());
						stmt.executeUpdate();
					    stmt.close();
					}
				}
				for (BankAccount delBankAccount : getBankAccountList(cust.idProperty().get())) {
	    			if(!(cust.getBankAccountList().contains(delBankAccount))){
		    			sql = "DELETE FROM pom.bankaccount WHERE id = ?"; // add WHERE customerID, if custID later gets PK
				    	stmt = this.con.prepareStatement(sql);
		    			stmt.setString(1, delBankAccount.idProperty().get());
				    	stmt.executeUpdate();
					    stmt.close();
	    			}
		    	}	    	
			}
		    return true;
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}
	
	@Override
	public List<Customer> getCustomerList(){
		List<Customer> custList = new ArrayList<Customer>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
			   custList.add(new Customer(rs.getString("id"), rs.getString("companyname"), rs.getString("ranking"), rs.getString("comment")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return custList;
	}

	
	private boolean openConnection(){
		try{
			 Class.forName("org.postgresql.Driver");
			 //current schema is set as 'pom'
			 ConnectionParameter cp = OpenConnectionFile.readFile();
	         con = DriverManager.getConnection("jdbc:postgresql://"+cp.getServerAddress()+":"+cp.getPort()+"/"+cp.getDataBase()+"?currentSchema=pom",cp.getUser(),cp.getPassword()); // useres File test.txt
	         return true;
		}catch(Exception e){
			Platform.exit();
			System.exit(0);
			//ErrorLog.write(e);
			//System.err.println(e.getClass().getName()+": "+e.getMessage()); // Konsolenausgabe

		}
		return false;
	}
	public void closeDbConn(){
		try{
			this.con.close();
			System.out.println("Datenbankverbindung geschlossen.");
		}catch(Exception e){
			ErrorLog.write(e);
			System.err.println(e.getClass().getName()+": "+e.getMessage()); // Ausgabe auf Konstole?!
		}
	}
	protected void finalize() 
	  {
		closeDbConn();
	  }

	@Override
	public boolean deleteCustomer(String id) { // wird die funktion hier quasi als oberfunktion genommen in der adresse, contact... gelöscht werden kann was in der geprüft wird?
		PreparedStatement stmt = null;
		String sql = "";
		for (Order order : getOrderList()) {
			if (order.customeridProperty().get().equals(id)){
				System.out.println("There already existing Orders for Customer " +id);
				//TODO show Dialog, in MainController
				return false;
			}
		}
		List<Address> delAddressList = getAddressList(id);
		List<Contact> delContactList = getContactList(id);
		List<BankAccount> delBankAccountList = getBankAccountList(id);
		try {
			if(!delAddressList.isEmpty()){
				for (@SuppressWarnings("unused") Address delAddrr : delAddressList) {
					sql = "DELETE FROM pom.address WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
			    	stmt = this.con.prepareStatement(sql);
					stmt.setString(1, id);
			    	stmt.executeUpdate();
				    stmt.close();
				}
			}
			
			if(!delContactList.isEmpty()){
				for (@SuppressWarnings("unused") Contact delContact : delContactList) {
					sql = "DELETE FROM pom.contact WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
			    	stmt = this.con.prepareStatement(sql);
					stmt.setString(1, id);
			    	stmt.executeUpdate();
				    stmt.close();
				}
			}
			
			if(!delBankAccountList.isEmpty()){
				for (@SuppressWarnings("unused") BankAccount delBa : delBankAccountList) {
					sql = "DELETE FROM pom.bankaccount WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
			    	stmt = this.con.prepareStatement(sql);
					stmt.setString(1, id);
			    	stmt.executeUpdate();
				    stmt.close();
				}
			}
	
				stmt = con.prepareStatement("DELETE FROM Customer WHERE id = ?");
				stmt.setString(1, id);
				stmt.executeUpdate();
			    stmt.close();
			    return true;
			    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}
	

	/**
	 * @returns List of all addresses of a Customer 
	 * 
	 */
	public List<Address> getAddressList(String custId){
		List<Address> addressList = new ArrayList<Address>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM address Where customerid = ? ");
			stmt.setString(1, custId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			   addressList.add(new Address(rs.getString("id"), rs.getString("street"), rs.getString("houseno"), rs.getString("zipcode"),rs.getString("city"),rs.getString("country"),Boolean.parseBoolean(rs.getString("billingaddress"))));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return addressList;
	}
	/**
	 * @returns List of all contacts of a Customer 
	 * 
	 */
	public List<Contact> getContactList(String custId){
		List<Contact> contactList = new ArrayList<Contact>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM contact Where customerid = ? ");
			stmt.setString(1, custId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			   contactList.add(new Contact(rs.getString("id"), rs.getString("name"), rs.getString("firstname"),rs.getString("position"),rs.getString("phoneno"),rs.getString("mailaddress")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return contactList;
	}
	/**
	 * @returns List of all bank accounts of a Customer 
	 * 
	 */
	public List<BankAccount> getBankAccountList(String custId){
		List<BankAccount> bankAccountList = new ArrayList<BankAccount>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM bankaccount Where customerid = ? ");
			stmt.setString(1, custId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				bankAccountList.add(new BankAccount(rs.getString("id"),rs.getString("iban"), rs.getString("bic"), rs.getString("bankname")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return bankAccountList;
	}
	/**
	 * @returns List of all orders of a Customer 
	 * 
	 */
	
	public List<Order> getOrderList(){
		List<Order> orderList = new ArrayList<Order>();
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM pom.order");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				orderList.add(new Order(rs.getString("orderno"), 
						rs.getString("customerid"), 
						rs.getString("addressid"), 
						rs.getString("contactid"),
						rs.getString("product"), 
						Double.parseDouble(rs.getString("price")),
						Integer.parseInt(rs.getString("volume")),
						rs.getString("state"),rs.getString("baselotid"),
						getNullDate(rs, "orderdate"),
						getNullDate(rs, "startdate"),
						getNullDate(rs, "releasedate"),
						getNullDate(rs, "completiondate"),
						getNullDate(rs, "duedate"),
						getNullDate(rs, "actualdeliverydate"),
						Integer.parseInt(rs.getString("lotsize")),
						Integer.parseInt(rs.getString("priority")),
						rs.getString("comment")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return orderList;
	}	
	
	private LocalDate getNullDate(ResultSet rs, String columnName){
		try {
			if(rs.getDate(columnName) == null){
				return emptyDate;
			}else{
				return rs.getDate(columnName).toLocalDate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ErrorLog.write(e);
			return emptyDate;
		}
	}

	private void setNullDate(PreparedStatement stmt, int index, LocalDate d){
		try{
			if(d == null){
				stmt.setNull(index, java.sql.Types.DATE);
			}else{
				stmt.setDate(index, java.sql.Date.valueOf(d));
			}
		}catch(SQLException e){
			ErrorLog.write(e);
		}
	}
	
	public List<Order> getCustomerOrder(String customerID){
		List<Order> orderList = new ArrayList<Order>();
			
			PreparedStatement stmt = null;
			try {
				stmt = con.prepareStatement("SELECT * FROM pom.order WHERE Customerid = ?");
				stmt.setString(1,customerID);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next())
				{ 
					orderList.add(new Order(rs.getString("orderno"), 
							rs.getString("customerid"), 
							rs.getString("addressid"), 
							rs.getString("contactid"),
							rs.getString("product"),
							Double.parseDouble(rs.getString("price")),
							Integer.parseInt(rs.getString("volume")),
							rs.getString("state"),rs.getString("baselotid"),
							getNullDate(rs,"orderdate"),
							getNullDate(rs,"startdate"),
							getNullDate(rs,"releasedate"),
							getNullDate(rs,"completiondate"),
							getNullDate(rs,"duedate"),
							getNullDate(rs,"actualdeliverydate"),
							Integer.parseInt(rs.getString("lotsize")),
							Integer.parseInt(rs.getString("priority")),
							rs.getString("comment")));
				}
				rs.close();
			    stmt.close();
			    
			} catch (SQLException e) {
				ErrorLog.write(e);
			}
			return orderList;
	}
	
	public List<Order> getCustomerOrderHistory(String customerID){
		List<Order> orderList = new ArrayList<Order>();
			
			PreparedStatement stmt = null;
			try {
				stmt = con.prepareStatement("SELECT * FROM pom.order WHERE Customerid = ?");
				stmt.setString(1,customerID);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next())
				{ 
					orderList.add(new Order(rs.getString("orderno"), 
							rs.getString("customerid"), 
							rs.getString("addressid"), 
							rs.getString("contactid"),
							rs.getString("product"),
							Double.parseDouble(rs.getString("price")),
							Integer.parseInt(rs.getString("volume")),
							rs.getString("state"),rs.getString("baselotid"),
							getNullDate(rs,"orderdate"),
							getNullDate(rs,"startdate"),
							getNullDate(rs,"releasedate"),
							getNullDate(rs,"completiondate"),
							getNullDate(rs,"duedate"),
							getNullDate(rs,"actualdeliverydate"),
							Integer.parseInt(rs.getString("lotsize")),
							Integer.parseInt(rs.getString("priority")),
							rs.getString("comment")));
				}
				rs.close();
			    stmt.close();
			    
			} catch (SQLException e) {
				ErrorLog.write(e);
			}
			return orderList;
	}
	/**
	 * Stores an Order Object on Database
	 * @returns true on success
	 */
	@Override
	public boolean addOrder(Order order) {
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			sql= "INSERT INTO pom.order VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1,order.customeridProperty().get());
			stmt.setString(2,order.addressidProperty().get());
			stmt.setString(3,order.contactidProperty().get());
			stmt.setString(4,order.productProperty().get());
			stmt.setDouble(5,order.priceProperty().get());
			stmt.setInt(6, order.volumeProperty().get());
			stmt.setString(7, order.stateProperty().get().toString());
			stmt.setString(8, order.baseLotIdProperty().get());
			setNullDate(stmt, 9, order.getOrderDate());//OrderDate
			setNullDate(stmt, 10, order.getStartDate());//starDate
			setNullDate(stmt, 11, order.getReleaseDate());//releaseDate
			setNullDate(stmt, 12, order.getCompletionDate());//completionDate
			setNullDate(stmt, 13, order.getDueDate());//DueDate
			setNullDate(stmt, 14, order.getActualDeliveryDate());//ActualDeliveryDate
			stmt.setInt(15, order.lotSizeProperty().get());
			stmt.setInt(16, order.priorityProperty().get());
			stmt.setString(17, order.commentProperty().get());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			order.ordernoProperty().set(rs.getString("orderno"));
			rs.close();
		    stmt.close();
		    return true;
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}
	

	public boolean updateOrder(Order order) {
		PreparedStatement stmt = null;
		try {	
			stmt = con.prepareStatement("Update pom.order set customerid = ?,addressid=?,contactid=?,product=?,price=?,volume=?,state=?,baselotid=?,orderdate=?, startdate=?, releasedate=?, completiondate=?, duedate=?, actualdeliverydate=?, lotsize=?, priority=?, comment=? where orderno = ? ");
			stmt.setString(1,order.customeridProperty().get());
			stmt.setString(2,order.addressidProperty().get());
			stmt.setString(3,order.contactidProperty().get());
			stmt.setString(4,order.productProperty().get());
			stmt.setDouble(5,order.priceProperty().get());
			stmt.setInt(6, order.volumeProperty().get());
			stmt.setString(7, order.stateProperty().get());
			stmt.setString(8, order.baseLotIdProperty().get());
			setNullDate(stmt, 9, order.getOrderDate());//OrderDate
			setNullDate(stmt, 10, order.getStartDate());//starDate
			setNullDate(stmt, 11, order.getReleaseDate());//releaseDate
			setNullDate(stmt, 12, order.getCompletionDate());//completionDate
			setNullDate(stmt, 13, order.getDueDate());//DueDate
			setNullDate(stmt, 14, order.getActualDeliveryDate());//ActualDeliveryDate
			stmt.setInt(15, order.lotSizeProperty().get());
			stmt.setInt(16, order.priorityProperty().get());
			stmt.setString(17, order.commentProperty().get());
			stmt.setString(18, order.ordernoProperty().get());
			
			stmt.executeUpdate();	
			return true;
	} catch (SQLException e) {
		ErrorLog.write(e);
	}
		return false;
	}
	
	/**
	 * Deletes an Order Object from database by orderno
	 * @param id of order tuple
	 * @returns true on success
	 */
	@Override 
	public boolean deleteOrder(String orderno) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("DELETE FROM pom.order WHERE orderno = ?");
			stmt.setString(1, orderno);
			stmt.executeUpdate();
		    stmt.close();
		    return true;
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}

	public Customer getCustomer(String customerId){
		Customer customerToReturn = new Customer();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Customer WHERE id = ?");
			stmt.setString(1, customerId);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			customerToReturn = new Customer( rs.getString("id"),  rs.getString("companyname"), rs.getString("ranking"),rs.getString("comment")); // customer erstellen
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		customerToReturn.setAddressList(FXCollections.observableList(this.getAddressList(customerId)));
		customerToReturn.setBankAccountList(FXCollections.observableList(this.getBankAccountList(customerId)));
		customerToReturn.setContactList(FXCollections.observableList(this.getContactList(customerId)));
		
		return customerToReturn;
	}
	public boolean hasSetup(){
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM pom.setup ORDER BY id ASC LIMIT 1");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				return true;
			}
			 
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}
	public Setup getSetup()
	{
		PreparedStatement stmt = null;
		Setup setup = new Setup();
		try {
			stmt = con.prepareStatement("SELECT * FROM pom.setup ORDER BY id ASC LIMIT 1");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
			setup.dayCapacityProperty().set(rs.getInt("daycapacity"));
			setup.defaultLotSizeProperty().set(rs.getInt("defaultlotsize"));
			}
			 
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return setup;
	}
	public boolean upsertSetup(Setup setup)
	{
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("INSERT INTO pom.setup (id, daycapacity, defaultlotsize) "
					+ "VALUES (1,?,?)"
					+ " ON CONFLICT (id) DO UPDATE SET daycapacity = ?, defaultlotsize = ?;");
			stmt.setInt(1,setup.dayCapacityProperty().get());
			stmt.setInt(2,setup.defaultLotSizeProperty().get());
			stmt.setInt(3,setup.dayCapacityProperty().get());
			stmt.setInt(4,setup.defaultLotSizeProperty().get());
			stmt.executeUpdate();
		    stmt.close();
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
			ErrorLog.write(e);
		}
		return false;
	}
	
	/**
	 * TODO: Markus? ist das sauber genug, wenn ich die Spalte auf der Serviceschicht hierher �bergebe? MH: Ja passt schon
	 * @param id to check
	 * @param column to check
	 * @return checks, if a specific foreign key is inserted in order table
	 */
	@Override
	public boolean isReferenced(String id, String column){
		String sql = "";
		PreparedStatement stmt = null;
		boolean result = true;
		
		ResultSet rs;
		try {
			sql= "select case when count(*) > 0 then true else false end from pom.order where "+column+" = ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, id);
		
			rs = stmt.executeQuery();
		    if(rs.next())
		    	result = rs.getBoolean(1);
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
