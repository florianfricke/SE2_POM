package types;

import javafx.beans.property.*;


public class Address {
	
	private final StringProperty id;
	private final StringProperty street ;
	private final StringProperty houseNo;
	private final StringProperty zipCode;
	private final StringProperty city;
	private final StringProperty country;
	private final BooleanProperty billingAddress;
	private CbxItemObservable comboBoxItem;
	
	public Address(String id, String street, String houseNo, String zipCode, String city, String country, boolean bill ){
		this.id = new SimpleStringProperty(id);
		this.street = new SimpleStringProperty(street);
		this.houseNo = new SimpleStringProperty(houseNo);
		this.zipCode = new SimpleStringProperty(zipCode);
		this.city = new SimpleStringProperty(city);
		this.country = new SimpleStringProperty(country);
		this.billingAddress = new SimpleBooleanProperty(bill);
		this.comboBoxItem = new CbxItemObservable(id, street+" "+houseNo);
	}
	
	public Address() {
		this("","","","","","",false);
		this.comboBoxItem = new CbxItemObservable("", "");
	}	


	public StringProperty idProperty() {
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
	public CbxItemObservable comboBoxProperty(){
		return comboBoxItem;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Address)) return false;
	    Address otherAddress = (Address)o;
	    if(this.id.get().equals(otherAddress.id.get())){
	    	return true;
	    }
		return false;
		
	}
}