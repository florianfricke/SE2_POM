package mes_db_interface;

import java.util.Date;
import java.util.List;

import types.*;

public interface IMesDBService {
	public List < Lot > getLots(String OrderNo);
	public boolean updateLots(String baseLotId, int newPrio);
	public Lot getLot(String orderNo);
	public int getDayWorkload(Date date);
	public boolean addLot(Lot lot);
	public List<String> getProductList();
}
