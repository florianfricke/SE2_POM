package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BankAccount {
	
	private final StringProperty iban;
	private final StringProperty bic;
	private final StringProperty bankName;
	
	public BankAccount(String iban, String bic, String bankName){
		this.iban = new SimpleStringProperty(iban);
		this.bic = new SimpleStringProperty(bic);
		this.bankName = new SimpleStringProperty(bankName);
	}
	
	public BankAccount() {this(null,null,null);}
	
	public StringProperty ibanProperty() {
		return iban;
	}
	public StringProperty bicProperty() {
		return bic;
	}
	public StringProperty bankNameProperty() {
		return bankName;
	}
}
