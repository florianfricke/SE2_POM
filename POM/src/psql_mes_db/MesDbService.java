package psql_mes_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mes_db_interface.IMesDBService;
import types.*;

public class MesDbService implements IMesDBService {
	private Connection con = null;
	public MesDbService(){
		openConnection();
	}
	/**
	 * 
	 * @param con builds the connection 
	 * @return true if the connection is opened successfully
	 */
	private boolean openConnection(){
		try{
			 Class.forName("org.postgresql.Driver");
			 ConnectionParameter cp = OpenConnectionFile.readFile();
			 con = DriverManager.getConnection("jdbc:postgresql://"+cp.getServerAddress()+":"+cp.getPort()+"/"+cp.getDataBase()+"?currentSchema=public",cp.getUser(),cp.getPassword()); // useres File test
	         return true;
		}catch(Exception e){
			ErrorLog.write(e);
		}
		return false;
	}
	/**
	 * @return true if connection is closed successfully
	 */
	public void closeDbConn(){
		try{
			this.con.close();
		}catch(Exception e){
			ErrorLog.write(e);
		}
	}
	protected void finalize() 
	  {
		closeDbConn();
	  }
	/**
	 * @param orderNo the customer you want to get the lots from
	 * @return a list of lots for a customer
	 */
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
			ErrorLog.write(e);
		}
		return lotList;
	}
	/**
	 * @return true if the lots were updated correctly
	 * @param stmt the SQL statement 
	 * @param order the order you want to update the lots on
	 */
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
			ErrorLog.write(e);
		}
		return false;
	}
	/**
	 * @return true if Lots were canceled correctly
	 * @param orderno the order number you want to cancel the lots from 
	 */
	@Override
	public boolean cancelLots(String orderno) {
		String sql = "";
		PreparedStatement stmt = null;
		try {
			sql= "UPDATE lot SET state = 'CANCELED' WHERE \"ORDER\"= ?";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1,orderno);
			stmt.executeUpdate();
		    stmt.close();
		    return true;
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return false;
	}
	/**
	 * @param orderNo the order you want to know the number of lots from
	 * @return the number of lots for a order 
	 */
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
			ErrorLog.write(e);
		}
		return count;
	}
	
	/**
	 *
	 * @param orderNo the order you want to get the information from
	 * @return count of lots, which are not in state 'RDY'
	 */
	@Override
	public int getLotInProcessCount(String orderNo){
		String sql = "";
		PreparedStatement stmt = null;
		int count = 0;
		ResultSet rs;
		try {
			sql= "SELECT COUNT(*) FROM lot WHERE \"ORDER\"= ? and state != 'RDY'";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, orderNo);
		
			rs = stmt.executeQuery();
		    if(rs.next())
		    	count = rs.getInt(1);
		    stmt.close();
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return count;
	}

	/**
	 * 
	 * @param orderNo the order you want to get the information from
	 * @return the latest start date of a lot from a order
	 */
	@Override
	public LocalDate getLatestStartDate(String orderno)
	{
		PreparedStatement stmt = null;
		LocalDate result = null;
		try {
			stmt = con.prepareStatement("select max(startdate) from lot where \"ORDER\" = ?");
			stmt.setString(1, orderno);;
			ResultSet rs = stmt.executeQuery();
			rs.next();
			result = rs.getDate(1).toLocalDate();
			rs.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			ErrorLog.write(e);
		}
		return result;
	}
	/**
	 * 
	 */
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
			ErrorLog.write(e);
		}
		return workload;
	}
	/**
	 * @param lot the lot you want to add
	 * @return true if the lot was added successfully
	 */
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
			ErrorLog.write(e);
		}
		return false;
	}
	/**
	 * @return 
	 * @param product
	 */
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
			ErrorLog.write(e);
		}
		return "";
	}
	/**
	 * 
	 * @param route
	 * @return
	 */
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
			ErrorLog.write(e);
		}
		return "";
	}
	/**
	 * @return all products from the table prodflow
	 * 
	 */
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
			ErrorLog.write(e);
		}
		return productList;
	}
	/**
	 * @return 
	 * @param orderno
	 * @param product
	 */
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
			ErrorLog.write(e);
		}
		return routeList;
	}
	/**
	 * 
	 * @param orderno
	 * @param route
	 * @return
	 */
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
			ErrorLog.write(e);
		}
		return operList;
	}
	/**
	 *Checks whether the preferred BaselotID already exists
	 * @param baseLotId - String of BaselotId
	 * @return boolean true if exists or false if not exists
	 * @version 1.0
	 */
	public boolean checkBaseLotIDExists(String baseLotId){
		String sql = "";
		PreparedStatement stmt = null;
		ResultSet rs;
		try{
			sql= "Select exists(SELECT * from lot WHERE LEFT(lotid, position( '1' in lotid)) = ?)";
			stmt = this.con.prepareStatement(sql);
			stmt.setString(1, baseLotId+"1");
			rs = stmt.executeQuery();
			if(rs.next()){
				return rs.getBoolean("exists");
			}	
		}
		catch(Exception e){
			ErrorLog.write(e);
		}
		return true;
	}
}
