package psql_mes_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import mes_db_interface.IMesDBService;
import types.*;

public class MesDbService implements IMesDBService {
	private Connection con = null;
	public MesDbService(){
		openConnection();
	}
	private boolean openConnection(){
		try{
			 Class.forName("org.postgresql.Driver");
	         con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mes","postgres", "0815");
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
	public boolean addLots(Lot lotTemplate, int n) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<Lot> getLots(String OrderNo) {
		List<Lot> lotList = new ArrayList<Lot>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM public.lot");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			   lotList.add(new Lot(rs.getString("id"), rs.getInt("priority"), rs.getInt("lotSize"),rs.getString("state"),rs.getString("product"),rs.getString("customerId"),rs.getString("orderNo"),rs.getString("dueDate")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lotList;
	}
	@Override	
	public Lot getLot(String orderNo){
		Lot lotToReturn = new Lot();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM lot WHERE orderno = ?");
			stmt.setString(1, orderNo);
			ResultSet rs = stmt.executeQuery();
			lotToReturn =(new Lot(rs.getString("id"), rs.getInt("priority"), rs.getInt("lotSize"),rs.getString("state"),rs.getString("product"),rs.getString("customerId"),rs.getString("orderNo"),rs.getString("dueDate")));
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lotToReturn;
	}
	@Override
	public boolean updateLots(String baseLotId, int newPrio) {
		// TODO Auto-generated method stub
		return false;
	}
}
