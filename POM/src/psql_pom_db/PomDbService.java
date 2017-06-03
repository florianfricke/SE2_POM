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
		PreparedStatement stmt = null;
		try {
			
			String sql = "INSERT INTO Customer VALUES" + "('" + cust.idProperty().get() +"','"+ cust.nameProperty().get() +"','"+ 
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
		    System.out.println("Inserted Customer "+ cust.idProperty());
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
			   custList.add(new Customer(rs.getString("customerid"), rs.getString("companyname"), rs.getString("customerranking"), rs.getString("comment")));
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
			stmt = con.prepareStatement("DELETE FROM Customer WHERE customerid = ?");
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
	 * TODO (Nils Nolte): please alter column name produkt to product 
	 * and actualdeliverysize to actualdeliverydate 
	 * and we also need column bankaccountid as fk to bank account
	 */
	public List<Order> getOrderList(){
		List<Order> orderList = new ArrayList<Order>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM Order");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
			{
			   orderList.add(new Order(rs.getString("orderno"), rs.getString("produkt"), rs.getDouble("price"),
					   rs.getInt("volume"), rs.getString("state"), rs.getString("baselotid"), rs.getDate("orderdate"),
					   rs.getDate("releasedate"), rs.getDate("completiondate"), rs.getDate("duedate"), rs.getInt("priority"), 
					   rs.getString("comment"), rs.getInt("lotsize"), rs.getDate("actualdeliverysize")
					   ));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}
}
