package psql_mes_db;

import java.sql.Connection;
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
	public List<Lot> getLotList(String orderNo) {
		List<Lot> lotList = new ArrayList<Lot>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("SELECT * FROM public.lot WHERE \"ORDER\" = ?");
			stmt.setString(1, orderNo);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
			   lotList.add(new Lot(rs.getString("lotid"), rs.getInt("priority"), rs.getInt("pieces"),rs.getString("state"),rs.getString("product"),rs.getString("customer"),rs.getString("ORDER"),rs.getDate("dueDate").toLocalDate(),rs.getDate("startDate").toLocalDate(),rs.getString("route"), rs.getString("oper")));
			}
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lotList;
	}
	@Override
	public boolean updateLots(Order order) {
		String sql = "";
		PreparedStatement stmt = null;
		try {
			sql= "UPDATE lot SET priority = ?, duedate = ? WHERE \"ORDER\"= ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setInt(1,order.priorityProperty().get());
			stmt.setDate(2, java.sql.Date.valueOf(order.getDueDate()));
			stmt.setString(3,order.ordernoProperty().get());
			stmt.executeUpdate();
		    stmt.close();
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public int getLotCount(String orderNo){
		String sql = "";
		PreparedStatement stmt = null;
		int count = 0;
		ResultSet rs;
		try {
			sql= "SELECT COUNT(*) FROM lot WHERE \"ORDER\"= ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
		    if(rs.next())
		    	count = rs.getInt(1);
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public int getDayWorkload(java.util.Date date) {
		PreparedStatement stmt = null;
		int workload = 0;
		try {
			stmt = con.prepareStatement("SELECT count(*) FROM lot WHERE startdate = ?");
			stmt.setDate(1,new java.sql.Date(date.getTime()));
			ResultSet rs = stmt.executeQuery();
			rs.next();
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
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			sql= "INSERT INTO lot(lotid, priority, pieces, state, product, route, oper, customer, \"ORDER\", duedate, startdate) VALUES (?, ?, ?, ?, ?,?,?, ?, ?, ?, ?)";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,lot.idProperty().get());
			stmt.setInt(2,lot.priorityProperty().get());
			stmt.setInt(3,lot.piecesProperty().get());
			stmt.setString(4,lot.stateProperty().get());
			stmt.setString(5,lot.productProperty().get());
			stmt.setString(6, getRoute(lot.productProperty().get()));
			stmt.setString(7, getOper(getRoute(lot.productProperty().get())));
			stmt.setString(8, lot.customerIdProperty().get());
			stmt.setString(9, lot.orderNoProperty().get());
			stmt.setDate(10, java.sql.Date.valueOf(lot.dueDateProperty().get()));
			stmt.setDate(11, java.sql.Date.valueOf(lot.startDateProperty().get()));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			rs.close();
		    stmt.close();
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getRoute(String product){
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "SELECT * FROM public.prodflow WHERE product = ? AND seq= '1'";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,product);
			rs = stmt.executeQuery();
			if(rs.next())
				return rs.getString("route");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getOper(String route){
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "SELECT * FROM public.workflow WHERE route = ? ORDER BY oper ASC";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,route);
			rs = stmt.executeQuery();
			if(rs.next())
				return rs.getString("oper");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public List<String> getProductList() {
		String sql = "";
		List<String> productList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "SELECT DISTINCT product FROM public.prodflow";
			stmt = this.con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				productList.add(rs.getString("product"));
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}
	public List<Route> getRouteList(String orderno,String product){
		String sql = "";
		List<Route> routeList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "SELECT seq, product, route "
					+ "FROM prodflow "
					+ "WHERE product = ? "
					+ "ORDER BY seq ASC";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,product);
			rs = stmt.executeQuery();
			while(rs.next()){
				Route route = new Route(rs.getString("seq"), rs.getString("product"), rs.getString("route"), "", "", "","","","","");
				List<Operation> operList = getOperationList(orderno, route.routeProperty().get());
				if( operList != null){
					route.setOperList(operList);
				}
				routeList.add(route);
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return routeList;
		/*Join
		 * SELECT p.seq, p.product, p.route, w.oper, w."DESC",(Select COUNT(*) FROM lot WHERE route = p.route AND oper = w.oper AND "ORDER" = '43   ' ) AS count 
			FROM prodflow p JOIN workflow w ON (p.route = w.route) 
			WHERE p.product = '2009MF' 
			ORDER BY p.seq ASC, w.oper ASC;  
		 
		 */
	}
	
	public List<Operation> getOperationList(String orderno,String route){
		String sql = "";
		List<Operation> operList = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "SELECT oper, \"DESC\", (Select COUNT(*) FROM lot WHERE route = ? AND oper = w.oper AND \"ORDER\" = ?) AS count "
					+ "FROM workflow w "
					+ "WHERE route = ? "
					+ "ORDER BY oper ASC";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,route);
			stmt.setString(2,orderno);
			stmt.setString(3,route);
			rs = stmt.executeQuery();
			while(rs.next()){
				operList.add(new Operation(rs.getString("oper"), rs.getString("DESC"), rs.getString("count")));
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return operList;
	}
}
