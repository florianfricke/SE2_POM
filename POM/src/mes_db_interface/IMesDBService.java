package mes_db_interface;

import java.util.Date;

import types.*;

public interface IMesDBService {
	public java.util.List < Lot > getLots(String OrderNo);
	public boolean updateLots(String baseLotId, int newPrio);
	public Lot getLot(String orderNo);
	public int getDayWorkload(Date date);
	public boolean addLot(Lot lot);
}
