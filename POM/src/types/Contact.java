package types;

import javafx.beans.property.*;


public class Contact {
	
	private final StringProperty id;
	private final StringProperty customerId;
	private final StringProperty salutation;
	private final StringProperty name;
	private final StringProperty firstName;
	private final StringProperty position;
	private final StringProperty phoneNo;
	private final StringProperty email;
	private final BooleanProperty defaultContact;
	private CbxItemObservable comboBoxItem;
	
	public Contact() {
		this("","","","","","","","",false);
		this.comboBoxItem = new CbxItemObservable("", "");
	}
	
	public Contact(String id,String custId,String salutation,String name, String firstName, String position, String phoneNo, String email, Boolean defaultContact){
		this.id = new SimpleStringProperty(id);
		this.customerId = new SimpleStringProperty(custId);
		this.salutation = new SimpleStringProperty(salutation);
		this.name = new SimpleStringProperty(name);
		this.firstName = new SimpleStringProperty(firstName);
		this.position = new SimpleStringProperty(position);
		this.phoneNo = new SimpleStringProperty(phoneNo);
		this.email = new SimpleStringProperty(email);
		this.defaultContact = new SimpleBooleanProperty(defaultContact);
		this.comboBoxItem = new CbxItemObservable(id, name+" "+ firstName);
	}
	
	

	public StringProperty idProperty() {
		return id;
	}
	public StringProperty customerIdProperty() {
		return customerId;
	}
	public StringProperty salutationProperty() {
		return salutation;
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
	public BooleanProperty defaultContactProperty() {
		return defaultContact;
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
	    if(!this.id.get().equals(otherContact.id.get())||
	       !this.salutation.get().equals(otherContact.salutationProperty().get()) ||
	 	   !this.name.get().equals(otherContact.nameProperty().get()) ||
		   !this.firstName.get().equals(otherContact.firstNameProperty().get())||
		   !this.position.get().equals(otherContact.positionProperty().get()) ||
		   !this.phoneNo.get().equals(otherContact.phoneNoProperty().get()) ||
		   !this.email.get().equals(otherContact.emailProperty().get()) ||
		   !this.defaultContact.get() == otherContact.defaultContactProperty().get()){
		    	return false;
		    }else return true;		
	}
}