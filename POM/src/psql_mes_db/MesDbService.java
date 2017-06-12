package psql_mes_db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	public List<Lot> getLots(String OrderNo) {
		List<Lot> lotList = new ArrayList<Lot>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM public.lot");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			   lotList.add(new Lot(rs.getString("id"), rs.getInt("priority"), rs.getInt("lotSize"),rs.getString("state"),rs.getString("product"),rs.getString("customerId"),rs.getString("orderNo"),rs.getString("dueDate"),rs.getString("startDate")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lotList;
	}
	@Override	
	public Lot getLot(String lotId){
		Lot lotToReturn = new Lot();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM lot WHERE lotid = ?");
			stmt.setString(1, lotId);
			ResultSet rs = stmt.executeQuery();
			lotToReturn =(new Lot(rs.getString("id"), rs.getInt("priority"), rs.getInt("lotSize"),rs.getString("state"),rs.getString("product"),rs.getString("customerId"),rs.getString("orderNo"),rs.getString("dueDate"),rs.getString("startDate")));
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
	
	@Override
	public int getDayWorkload(java.util.Date date) {
		PreparedStatement stmt = null;
		int workload = 0;
		try {
			stmt = con.prepareStatement("SELECT count(*) FROM lot WHERE startday = ?");
			stmt.setDate(1,(Date) date);
			ResultSet rs = stmt.executeQuery();
			workload = rs.getInt(1);
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workload;
	}
	@Override
	public boolean addLot(Lot lot) {
		
		return false;
	}
}
