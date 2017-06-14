package mes_db_interface;

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
}
