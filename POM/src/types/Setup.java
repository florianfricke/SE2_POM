package types;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Setup {
	private IntegerProperty dayCapacity;
	private IntegerProperty defaultLotSize;
	
	public Setup(){
		this.dayCapacity = new SimpleIntegerProperty(10);
		this.defaultLotSize = new SimpleIntegerProperty(10);
	}
	
	public Setup(int dayCpcty, int defLotSize){
		this.dayCapacity = new SimpleIntegerProperty(dayCpcty);
		this.defaultLotSize = new SimpleIntegerProperty(defLotSize);
	}
	
	public IntegerProperty dayCapacityProperty(){return dayCapacity;}
	public IntegerProperty defaultLotSizeProperty(){return defaultLotSize;}
}
