package pom_service;
import java.util.List;

import javafx.collections.ObservableList;
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
	
	public List<Customer> getCustomerList() {
		return pomPersistance.getCustomerList();
	}
	
	public boolean deleteCustomer(String id){
		return pomPersistance.deleteCustomer(id);
	}
}
