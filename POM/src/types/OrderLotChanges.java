package types;

import java.util.Date;
import javafx.beans.property.*;

public class OrderLotChanges {
	private IntegerProperty volume;
	private IntegerProperty priority;
	private Date dueDate;
	public OrderLotChanges(int volume,int priority, Date date){
		this.volume = new SimpleIntegerProperty(volume);
		this.priority = new SimpleIntegerProperty(priority);
		this.dueDate = date;
	}
	
	public IntegerProperty volumeProperty(){
		return volume;
	}
	public IntegerProperty priorityProperty(){
		return priority;
	}
	public Date getdueDate(){
		return dueDate;
	}
}
