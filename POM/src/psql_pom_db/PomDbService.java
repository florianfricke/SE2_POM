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
		PreparedStatement stmt = null;
		try {
			
			sql = "INSERT INTO Customer VALUES" + "('" + cust.idProperty().get() +"','"+ cust.nameProperty().get() +"','"+ 
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
		    System.out.println("Upserted Customer "+ cust.idProperty());
		    /* TODO sobald Tabellen gefüllt sind entweder einfügen oder Updaten
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
		try {
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
	 * @returns List of all Orders which are stored in data source
	 * 
	 */
	public List<Order> getOrderList(String custId){
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
			   addressList.add(new Address(rs.getInt("id"), rs.getString("street"), rs.getString("houseno"), rs.getString("zipcode"),rs.getString("city"),rs.getString("country"),Boolean.parseBoolean(rs.getString("billingaddress"))));
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
			   contactList.add(new Contact(rs.getInt("id"), rs.getString("name"), rs.getString("firstname"),rs.getString("position"),rs.getString("phoneno"),rs.getString("mailadress")));
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
				bankAccountList.add(new BankAccount(rs.getString("iban"), rs.getString("bic"), rs.getString("bankname")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bankAccountList;
	}
	
	
}
