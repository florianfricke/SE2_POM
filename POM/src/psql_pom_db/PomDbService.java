package psql_pom_db;
import pom_db_interface.*;
import types.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PomDbService implements IPomDbService {
	private Connection con = null;
	public PomDbService(){
		openConnection();
	}

	@Override
	public boolean addCustomer(Customer cust) {
		String sql = "";
		ResultSet rs;
		PreparedStatement stmt = null;
		try {
			
			sql = "INSERT INTO Customer (companyname, ranking, comment) VALUES" + 
					"('"+ cust.nameProperty().get() +"','"+ 
		            cust.rankingProperty().get() +"','"+ 
					cust.commentProperty().get() +"') ";
			stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			cust.idProperty().set(rs.getString("id"));
			rs.close();
		    stmt.close();
		    System.out.println("Upserted Customer "+ cust.idProperty());
		    if(!(cust.getAddressList().isEmpty())){
		    	for (Address addrr : cust.getAddressList()) {
			    	sql = "INSERT INTO public.address(customerid, street, houseno, city, zipcode, country, billingaddress) VALUES"+ 
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
		    	}
		    }
			if(!(cust.getContactList().isEmpty())){
				for (Contact contact : cust.getContactList()) {
			    	sql = "INSERT INTO public.contact(customerid, phoneno, name, firstname, mailadress, \"position\") VALUES"+ 
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
		    	}	    	
			}
			if(!(cust.getBankAccountList().isEmpty())){
				for (BankAccount ba  : cust.getBankAccountList()) {
			    	sql = "INSERT INTO public.bankaccount(customerid, iban, bic, bankname) VALUES"+ 
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
		    	}	    	
			}
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean updateCustomer(Customer cust) { //TODO Funktion noch in private Untermethoden aufteilen
		String sql = "";
		ResultSet rs;
		List<Address> dbCustAddressList = getAddressList(cust.idProperty().get());
		List<Contact> dbCustContactList = getContactList(cust.idProperty().get());
		List<BankAccount> dbCustBankAccountList = getBankAccountList(cust.idProperty().get());
		PreparedStatement stmt = null;
		try {
			
			sql = "UPDATE public.customer SET companyname=?, ranking=?, comment = ?" + 
			"WHERE id = ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, cust.nameProperty().get());
			stmt.setString(2, cust.rankingProperty().get());
			stmt.setString(3, cust.commentProperty().get());
			stmt.setString(4, cust.idProperty().get());
			stmt.executeUpdate();
		    stmt.close();
		    System.out.println("Updated Customer "+ cust.idProperty());
		    if(!(cust.getAddressList().equals(dbCustAddressList))){
		    	for (Address addrr : cust.getAddressList()) {
		    		if(addrr.idProperty().get().isEmpty()){
		    			sql = "INSERT INTO public.address(customerid, street, houseno, city, zipcode, country, billingaddress) VALUES"+ 
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
				    	sql = "UPDATE public.address SET street = ?, houseno = ?, city = ?, zipcode = ?, country = ?, billingaddress = ?"+ 
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
	    		//Zu löschende Elemente heraussuchen
	    		for (Address delAddrr : getAddressList(cust.idProperty().get())) {
	    			if(!(cust.getAddressList().contains(delAddrr))){
		    			sql = "DELETE FROM public.address WHERE id = ?"; // add WHERE customerID, if custID later gets PK
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
						sql = "INSERT INTO public.contact(customerid, phoneno, name, firstname, mailadress, \"position\") VALUES"+ 
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
					}else{
				    	sql = "UPDATE public.contact SET phoneno = ?, name = ?, firstname = ?, mailadress = ?, position = ?"+ 
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
		    			sql = "DELETE FROM public.contact WHERE id = ?"; // add WHERE customerID, if custID later gets PK
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
				    	sql = "INSERT INTO public.bankaccount(customerid, iban, bic, bankname) VALUES"+ 
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
				    	sql = "UPDATE public.bankaccount SET iban = ?, bic = ?, bankname = ?"+ 
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
		    			sql = "DELETE FROM public.bankaccount WHERE id = ?"; // add WHERE customerID, if custID later gets PK
				    	stmt = this.con.prepareStatement(sql);
		    			stmt.setString(1, delBankAccount.idProperty().get());
				    	stmt.executeUpdate();
					    stmt.close();
	    			}
		    	}	    	
			}
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return custList;
	}
	
	private boolean openConnection(){
		try{
			 Class.forName("org.postgresql.Driver");
	         con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pom","postgres", "0815");
	         return true;
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
		System.out.println("Erfolgreich verbunden!");
		return false;
	}
	public void closeDbConn(){
		try{
			this.con.close();
			System.out.println("Datenbankverbindung geschlossen.");
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}
	}
	protected void finalize() 
	  {
		closeDbConn();
	  }

	@Override
	public boolean deleteCustomer(String id) {
		PreparedStatement stmt = null;
		String sql = "";
		List<Address> delAddressList = getAddressList(id);
		List<Contact> delContactList = getContactList(id);
		List<BankAccount> delBankAccountList = getBankAccountList(id);
		try {
			if(!delAddressList.isEmpty()){
				for (@SuppressWarnings("unused") Address delAddrr : delAddressList) {
					sql = "DELETE FROM public.address WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
			    	stmt = this.con.prepareStatement(sql);
					stmt.setString(1, id);
			    	stmt.executeUpdate();
				    stmt.close();
				}
			}
			
			if(!delContactList.isEmpty()){
				for (@SuppressWarnings("unused") Contact delContact : delContactList) {
					sql = "DELETE FROM public.contact WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
			    	stmt = this.con.prepareStatement(sql);
					stmt.setString(1, id);
			    	stmt.executeUpdate();
				    stmt.close();
				}
			}
			
			if(!delBankAccountList.isEmpty()){
				for (@SuppressWarnings("unused") BankAccount delBa : delBankAccountList) {
					sql = "DELETE FROM public.bankaccount WHERE customerid = ?"; // add WHERE customerID, if custID later gets PK
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			   contactList.add(new Contact(rs.getString("id"), rs.getString("name"), rs.getString("firstname"),rs.getString("position"),rs.getString("phoneno"),rs.getString("mailadress")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return bankAccountList;
	}
	
	public List<Order> getOrderList(){
		List<Order> orderList = new ArrayList<Order>();
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM public.order");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				orderList.add(new Order(rs.getString("orderno"), rs.getString("customerid"), rs.getString("adressid"), rs.getString("contactid"),rs.getString("product"),Double.parseDouble(rs.getString("price")),Integer.parseInt(rs.getString("volume")),rs.getString("state"),rs.getString("baselotid"),(rs.getString("orderdate")),(rs.getString("releasedate")),(rs.getString("completitiondate")),(rs.getString("duedate")),(rs.getString ("actualdeliverydate")),Integer.parseInt(rs.getString("lotsize")),Integer.parseInt(rs.getString("priority")),rs.getString("comment")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
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
		try {
			
			sql = "INSERT INTO Order VALUES" + "('" + 
					order.ordernoProperty().get() +"','"+ 
					order.customeridProperty().get() +"','"+ 
					order.addressidProperty().get() +"','"+
					order.contactidProperty().get() +"','"+
					order.productProperty().get() +"',"+
					order.priceProperty().get() +","+
					order.volumeProperty().get() +",'"+
					order.stateProperty() +"','"+
					order.baseLotIdProperty().get() +"','"+
					order.orderDateProperty() +"',"+
					"NULL, NULL, NULL,"+ //Additional dates are NULL 
					order.lotSizeProperty().get() +","+
					order.priorityProperty().get() +",'"+
					order.commentProperty().get() +"') ";
				    /*+ "ON CONFLICT (customerid) DO UPDATE SET customerid = ?,"
				    + "companyname = ?, customerranking = ?, comment = ?";*/
			stmt = this.con.prepareStatement(sql);
			/*stmt.setString(1, cust.idProperty().get());
			stmt.setString(2, cust.nameProperty().get());
			stmt.setString(3, cust.rankingProperty().get());
			stmt.setString(4, cust.commentProperty().get());*/
			stmt.executeUpdate();
		    stmt.close();
		    System.out.println("Upserted Order "+ order.ordernoProperty());
		    /* TODO sobald Tabellen gefï¿½llt sind entweder einfï¿½gen oder Updaten
		    if(!(cust.getAddressList().isEmpty())){
		    	for (Address addrr : cust.getAddressList()) {
			    	sql = "INSERT INTO address VALUES" + "('" + addr.idProperty().get() +"','"+ cust.nameProperty().get() +"','"+ 
				            cust.rankingProperty().get() +"','"+ 
							cust.commentProperty().get() +"') "
						    + "ON CONFLICT (customerid) DO UPDATE SET customerid = ?,"
						    + "companyname = ?, customerranking = ?, comment = ?";
					stmt = this.con.prepareStatement(sql);
					stmt.setString(1, cust.idProperty().get());
					stmt.setString(2, cust.nameProperty().get());
					stmt.setString(3, cust.rankingProperty().get());
					stmt.setString(4, cust.commentProperty().get());
					stmt.executeUpdate();
				    stmt.close();
		    	}
		    }
			if(!(cust.getContactList().isEmpty())){
					    	
			}
			if(!(cust.getBankAccountList().isEmpty())){
				
			}*/
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Deletes an Order Object from database by orderno
	 * @param id of order tupel
	 * @returns true on success
	 */
	@Override
	public boolean deleteOrder(String orderno) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("DELETE FROM Order WHERE orderno = ?");
			stmt.setString(1, orderno);
			stmt.executeUpdate();
		    stmt.close();
		    return true;
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * @returns List of all Orders which are stored in data source
	 * 
	 */
	/*public List<Order> getOrderList(String custId){
		List<Order> orderList = new ArrayList<Order>();
		PreparedStatement stmt = null;
		if(custId.isEmpty()){
			try {
				stmt = con.prepareStatement("SELECT * FROM Order");
				ResultSet rs = stmt.executeQuery();
				while (rs.next())
				{
				   //orderList.add(new Order(rs.getString("orderno"), rs.getString("customerid"), rs.getString("adressid"), rs.getString("contactid"),rs.getString("product"),rs.getString("price"),rs.getString("volume"),rs.getString("state"),rs.getString("baselotid"),rs.getString("orderdate"),rs.getString("releasedate"),rs.getString("completitiondate"),rs.getString("duedate"),rs.getString("actualdeliverydate"),rs.getString("lotsize"),rs.getString("priority"),rs.getString("comment")));
				}
				rs.close();
			    stmt.close();
			    
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				stmt = con.prepareStatement("SELECT * FROM Order");
				ResultSet rs = stmt.executeQuery();
				while (rs.next())
				{
				   //orderList.add(new Customer(rs.getString("customerid"), rs.getString("companyname"), rs.getString("customerranking"), rs.getString("comment")));
				}
				rs.close();
			    stmt.close();
			    
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orderList;
		
	}*/

	
	
}
