package psql_mes_db;

import java.sql.Connection;
import java.sql.DriverManager;
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
	public boolean addLots(Lot lotTemplate, int n) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<Lot> getLots(String OrderNo) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean updateLots(String baseLotId, int newPrio) {
		// TODO Auto-generated method stub
		return false;
	}
}
