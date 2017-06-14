package types;

import java.time.LocalDate;
import javafx.beans.property.*;

public class OrderLotChanges {
	private IntegerProperty volume;
	private IntegerProperty priority;
	private LocalDate dueDate;
	public OrderLotChanges(int volume,int priority, LocalDate date){
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
	public LocalDate getdueDate(){
		return dueDate;
	}
}
