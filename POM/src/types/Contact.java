package types;

import javafx.beans.property.*;


public class Contact {
	
	private final StringProperty id;
	private final StringProperty name;
	private final StringProperty firstName;
	private final StringProperty position;
	private final StringProperty phoneNo;
	private final StringProperty email;
	private CbxItemObservable comboBoxItem;
	
	public Contact() {
		this("","","","","","");
		this.comboBoxItem = new CbxItemObservable("", "");
	}
	
	public Contact(String id, String name, String firstName, String position, String phoneNo, String email){
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.firstName = new SimpleStringProperty(firstName);
		this.position = new SimpleStringProperty(position);
		this.phoneNo = new SimpleStringProperty(phoneNo);
		this.email = new SimpleStringProperty(email);
		this.comboBoxItem = new CbxItemObservable(id, name+" "+ firstName);
	}
	
	

	public StringProperty idProperty() {
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
	public CbxItemObservable comboBoxProperty(){
		return comboBoxItem;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Contact)) return false;
	    Contact otherContact = (Contact)o;
	    if(this.id.get().equals(otherContact.id.get())){
	    	return true;
	    }
		return false;
		
	}
}