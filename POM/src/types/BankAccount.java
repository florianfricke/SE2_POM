package types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BankAccount {
	private final StringProperty id;
	private final StringProperty iban;
	private final StringProperty bic;
	private final StringProperty bankName;
	
	public BankAccount(String id,String iban, String bic, String bankName){
		this.id = new SimpleStringProperty(id);
		this.iban = new SimpleStringProperty(iban);
		this.bic = new SimpleStringProperty(bic);
		this.bankName = new SimpleStringProperty(bankName);
	}
	
	public BankAccount() {this("","","","");}
	
	public StringProperty idProperty() {
		return id;
	}
	public StringProperty ibanProperty() {
		return iban;
	}
	public StringProperty bicProperty() {
		return bic;
	}
	public StringProperty bankNameProperty() {
		return bankName;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof BankAccount)) return false;
	    BankAccount otherBankAccount = (BankAccount)o;
	    if(!this.id.get().equals(otherBankAccount.idProperty().get())||
	       !this.iban.get().equals(otherBankAccount.ibanProperty().get())||
	       !this.bic.get().equals(otherBankAccount.bicProperty().get())){
	    	return false;
	    }else return true;
		
	}
}
