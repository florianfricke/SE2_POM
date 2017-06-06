package types;

import javafx.beans.property.*;


public class Contact {
	
	private IntegerProperty id;
	private StringProperty name;
	private StringProperty firstName;
	private StringProperty position;
	private StringProperty phoneNo;
	private StringProperty email;
	
	public Contact() {
		
	}
	
	public Contact(int id, String name, String firstName, String position, String phoneNo, String email){
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.firstName = new SimpleStringProperty(firstName);
		this.position = new SimpleStringProperty(position);
		this.phoneNo = new SimpleStringProperty(phoneNo);
		this.email = new SimpleStringProperty(email);
	}
	
	

	public IntegerProperty idProperty() {
		return id;
	}
	public StringProperty nameProperty() {
		return name;
	}
	public StringProperty firstNameProperty() {
		return firstName;
	}
	public StringProperty positionProperty() {
		return position;
	}
	public StringProperty phoneNoProperty() {
		return phoneNo;
	}
	public StringProperty emailProperty() {
		return email;
	}
}