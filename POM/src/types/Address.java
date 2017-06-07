package types;

import javafx.beans.property.*;


public class Address {
	
	private final IntegerProperty id;
	private final StringProperty street ;
	private final StringProperty houseNo;
	private final StringProperty zipCode;
	private final StringProperty city;
	private final StringProperty country;
	private final BooleanProperty billingAddress;
	
	public Address(int id, String street, String houseNo, String zipCode, String city, String country, boolean bill ){
		this.id = new SimpleIntegerProperty(id);
		this.street = new SimpleStringProperty(street);
		this.houseNo = new SimpleStringProperty(houseNo);
		this.zipCode = new SimpleStringProperty(zipCode);
		this.city = new SimpleStringProperty(city);
		this.country = new SimpleStringProperty(country);
		this.billingAddress = new SimpleBooleanProperty(bill);
	}
	
	public Address() {
		this(0,"","","","","",false);
	}	


	public IntegerProperty idProperty() {
		return id;
	}
	public StringProperty streetProperty() {
		return street;
	}
	public StringProperty houseNoProperty() {
		return houseNo;
	}
	public StringProperty zipCodeProperty() {
		return zipCode;
	}
	public StringProperty cityProperty() {
		return city;
	}
	public StringProperty countryProperty() {
		return country;
	}
	
	public BooleanProperty billingAddressProperty() {
		return billingAddress;
	}
}