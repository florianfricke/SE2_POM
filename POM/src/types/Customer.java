package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer {

	private final StringProperty id;
	private final StringProperty name;
	private final StringProperty ranking;
	private final StringProperty comment;
	private ObservableList<Address> addressList;
	private ObservableList<Contact> contactList;
	private ObservableList<BankAccount> bankAccountList;

	public Customer(String id, String name, String ranking,String comment) {
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.ranking = new SimpleStringProperty(ranking);
		this.comment = new SimpleStringProperty(comment);
		this.addressList = FXCollections.observableArrayList();
		this.contactList = FXCollections.observableArrayList();
		this.bankAccountList = FXCollections.observableArrayList();
	}
	
	public Customer() {
		this("","","","");
		this.addressList = FXCollections.observableArrayList();
		this.contactList = FXCollections.observableArrayList();
		this.bankAccountList = FXCollections.observableArrayList();
		}

	public StringProperty idProperty() {
		return id;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public StringProperty rankingProperty() {
		return ranking;
	}

	public StringProperty commentProperty() {
		return comment;
	}
	
	public ObservableList<Address> getAddressList(){
		return addressList;
	}
	public void setAddressList(ObservableList<Address> addressList){
		this.addressList = addressList;
	}
	public ObservableList<Contact> getContactList(){
		return contactList;
	}
	public void setContactList(ObservableList<Contact> contactList){
		this.contactList = contactList;
	}
	public ObservableList<BankAccount> getBankAccountList(){
		return bankAccountList;
	}
	public void setBankAccountList(ObservableList<BankAccount> bankAccountList){
		this.bankAccountList = bankAccountList;
	}
	
	
	public String toString(){
		return nameProperty().get();
	}

}
