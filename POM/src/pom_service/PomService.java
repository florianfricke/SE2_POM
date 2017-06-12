package pom_service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mes_db_interface.*;
import pom_db_interface.*;
import psql_mes_db.MesDbService;
import psql_pom_db.*;
import types.*;

/**
 * @author Konstantin
 *
 */
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

	public boolean deleteOrder(String orderno){
		return pomPersistance.deleteOrder(orderno);
	}

	public List<Customer> getCustomerList() {
		return pomPersistance.getCustomerList();
	}
	
	public boolean deleteCustomer(String id){
		return pomPersistance.deleteCustomer(id);
	}
	
	public List<Order> getOrderList(){
		return pomPersistance.getOrderList();
	}
	
	//MES Methods
	public boolean addLot(Lot lot) {
		return mesPersistance.addLot(lot);
	}
	
	public List<Lot> getLots(String OrderNo) {
		return mesPersistance.getLots(OrderNo);
	}
	
	public boolean updateLots(String baseLotId, int newPrio) {
		return mesPersistance.updateLots(baseLotId, newPrio);
	}
	
	/*
	 * @returns Remaining Capacity of a date
	 */
	public int getDayCapacity(Date date)
	{
		return pomPersistance.getDayCapacity() - mesPersistance.getDayWorkload(date);
	}
	
	public boolean releaseOrder(Order order) 
	{
		int remainingVolume = order.volumeProperty().get();
		int n;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		boolean success = false;
		int i = 1;
		
		Lot lotTemplate = new Lot(null, order.priorityProperty().get(), order.lotSizeProperty().get(), order.stateProperty().get(), order.productProperty().get(), order.customeridProperty().get(), order.ordernoProperty().get(), order.dueDateProperty().get(),c.getTime().toString());
		
		while(remainingVolume > 0)
		{		
			n = getDayCapacity(c.getTime());//*order.lotSizeProperty().get();
			
			
			while(n>0 && remainingVolume > 0)
			{
				n--;
				if(remainingVolume < order.lotSizeProperty().get()) //remaining Volume is smaller than lotSize
				{	
					lotTemplate.lotSizeProperty().set(remainingVolume);
					n=0;
				}
				
				lotTemplate.idProperty().set(order.baseLotIdProperty().get()+Integer.toString(i++));
				success = addLot(lotTemplate);
				remainingVolume -= lotTemplate.lotSizeProperty().get();
				
			}
			c.add(Calendar.DATE, 1);
			lotTemplate.startDateProperty().set(c.getTime().toString());
		}
		
		return success;
	}
}
