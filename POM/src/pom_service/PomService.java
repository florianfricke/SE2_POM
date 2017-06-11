package pom_service;
import java.util.List;

import pom_db_interface.*;
import psql_pom_db.*;
import types.*;

public class PomService {
	private IPomDbService pomPersistance;
	
	
	public PomService(SaveType s){
		switch(s.name()){
		case "postgres": pomPersistance = new PomDbService();break;
		case "text":;break; // prepared for further Persistence Implementation
		case "mssql":;break; // prepared for further Persistence Implementation
		}
	}
		
	public boolean addCustomer(Customer cust){
		return pomPersistance.addCustomer(cust);
	}
	public boolean updateCustomer(Customer cust){
		if(pomPersistance.updateCustomer(cust)){
			return true;
		}
		return false;
	}
	
	public boolean addOrder(Order order){
		return pomPersistance.addOrder(order);
	}
	
	public boolean deleteOrder(String orderno){
		return pomPersistance.deleteOrder(orderno);
	}

	
	public List<Customer> getCustomerList() {
		return pomPersistance.getCustomerList();
	}
	
	public List<Customer> getCustomerNameList() {
		return pomPersistance.getCustomerList();
	}
	
	public boolean deleteCustomer(String id){
		return pomPersistance.deleteCustomer(id);
	}
	
	public List<Order> getOrderList(){
		return pomPersistance.getOrderList();
	}
	public List<Address> getAddressList(String custId){
		return pomPersistance.getAddressList(custId);
	}
	public List<Contact> getContactList(String custId){
		return pomPersistance.getContactList(custId);
	}
	public List<BankAccount> getBankAccountList(String custId){
		return pomPersistance.getBankAccountList(custId);
	}
}
