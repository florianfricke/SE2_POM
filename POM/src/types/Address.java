package types;

import javafx.beans.property.*;


public class Address {
	
	private IntegerProperty id;
	private StringProperty street ;
	private StringProperty houseNo;
	private IntegerProperty zipCode;
	private StringProperty city;
	private StringProperty country;
	private BooleanProperty billingAddress;
	
	public Address() {
		
	}
	
	public Address(int id, String street, String houseNo, int zipCode, String city, String country, boolean bill ){
		this.id = new SimpleIntegerProperty(id);
		this.street = new SimpleStringProperty(street);
		this.houseNo = new SimpleStringProperty(houseNo);
		this.zipCode = new SimpleIntegerProperty(zipCode);
		this.city = new SimpleStringProperty(city);
		this.country = new SimpleStringProperty(country);
		this.billingAddress = new SimpleBooleanProperty(bill);
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
	public IntegerProperty zipCodeProperty() {
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