package mes_db_interface;

import java.sql.Array;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import types.*;

public interface IMesDBService {
	public List < Lot > getLotList(String OrderNo);
	public boolean addLot(Lot lot);
	public boolean updateLots(Order order);
	public int getLotCount(String orderNo);
	public int getDayWorkload(Date date);
	public List<String> getProductList();
	public List<Route> getRouteList(String orderno,String product);
	public boolean cancelLots(String orderno);
	public int getLotInProcessCount(String orderNo);
	public LocalDate getLatestStartDate(String orderno);
	public boolean checkBaseLotIDExists(String baseLotId);
}
