package mes_db_interface;

import types.*;

public interface IMesDBService {
	public boolean addLots(Lot lotTemplate, int n);
	public java.util.List < Lot > getLots(String OrderNo);
	public boolean updateLots(String baseLotId, int newPrio);
}
