package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Customer {

	private StringProperty id;
	private StringProperty name;
	private StringProperty ranking;
	private StringProperty comment;
	private ObservableList<Address> addressList;
	private ObservableList<Contact> contactList;
	private ObservableList<BankAccount> bankAccountList;

	public Customer(String id, String name, String ranking,String comment) {
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.ranking = new SimpleStringProperty(ranking);
		this.comment = new SimpleStringProperty(comment);
	}
	
	public Customer() {}

	public StringProperty idProperty() {
		return id;
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty rankingProperty() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking.set(ranking);
	}

	public StringProperty commentProperty() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment.set(comment);
	}
	
	public ObservableList<Address> getAddressList(){
		return addressList;
	}
	public ObservableList<Contact> getContactList(){
		return contactList;
	}
	public ObservableList<BankAccount> getBankAccountList(){
		return bankAccountList;
	}

}
