package pom_service;
import java.util.List;

import mes_db_interface.*;
import pom_db_interface.*;
import psql_mes_db.MesDbService;
import psql_pom_db.*;
import types.*;

public class PomService {
	private IPomDbService pomPersistance;
	private IMesDBService mesPersistance;
	
	
	public PomService(SaveType pomSaveType, SaveType mesSaveType){
		switch(pomSaveType.name()){
		case "postgres": pomPersistance = new PomDbService();break;
		case "text":;break; // prepared for further Persistence Implementation
		case "mssql":;break; // prepared for further Persistence Implementation
		}
		
		switch(mesSaveType.name()){
		case "postgres": mesPersistance = new MesDbService();break;
		case "text":;break; // prepared for further Persistence Implementation
		case "mssql":;break; // prepared for further Persistence Implementation
		}
	}
	
	//Customer Methods	
	public boolean addCustomer(Customer cust){
		return pomPersistance.addCustomer(cust);
	}
	public boolean updateCustomer(Customer cust){
		if(pomPersistance.updateCustomer(cust)){
			return true;
		}
		return false;
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
	
	//Order Methods
	public boolean addOrder(Order order){
		return pomPersistance.addOrder(order);
	}
	public boolean updateOrder(Order order){
		return pomPersistance.updateOrder(order);
	}

	public boolean updateOrder(Order order){
		if(pomPersistance.updateOrder(order)){
			return true;
		}
		return false;
	}
	
	public boolean deleteOrder(String orderno){
		return pomPersistance.deleteOrder(orderno);
	}
	public Customer getCustomer(String customerId){
		return pomPersistance.getCustomer(customerId);
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
	
	//MES Methods
	public boolean addLots(Lot lotTemplate, int n) {
		return mesPersistance.addLots(lotTemplate, n);
	}
	
	public List<Lot> getLots(String OrderNo) {
		return mesPersistance.getLots(OrderNo);
	}
	
	public boolean updateLots(Order order) {
		/*int newVolumne = order.volumeProperty().get();
		int oldVolume = order.getOrderLotChanges().volumeProperty().get();
		Lot lot = new Lot("DEFAULT", order.priorityProperty().get(),order.lotSizeProperty().get(),order.stateProperty().get(),order.productProperty().get(),order.customeridProperty().get(), order.ordernoProperty().get(),order.dueDateProperty().get(), order.stateProperty().get());
		if (newVolumne > oldVolume){
			int LotCapacity = 10;
			int lotsToinsert;
			//Calculate No. of new Lots for insertion
			lotsToinsert = (newVolumne - oldVolume)/order.lotSizeProperty().get();//TODO ROUND to int
		}
		if (order.priorityProperty().get() != order.getOrderLotChanges().priorityProperty().get()){
					
		}
		if (order.dueDateProperty().get() != order.getOrderLotChanges().getdueDate().toString()){
			
		}
		order.setOrderLotChange();*/
		return false;
	}
}
