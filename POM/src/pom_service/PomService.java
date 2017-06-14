package pom_service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mes_db_interface.*;
import pom_db_interface.*;
import psql_mes_db.MesDbService;
import psql_pom_db.*;
import types.*;

public class PomService {
	private IPomDbService pomPersistance;
	private IMesDBService mesPersistance;

	public PomService(SaveType pomSaveType, SaveType mesSaveType) {
		switch (pomSaveType.name()) {
		case "postgres":
			pomPersistance = new PomDbService();
			break;
		case "text":
			;
			break; // prepared for further Persistence Implementation
		case "mssql":
			;
			break; // prepared for further Persistence Implementation
		}

		switch (mesSaveType.name()) {
		case "postgres":
			mesPersistance = new MesDbService();
			break;
		case "text":
			;
			break; // prepared for further Persistence Implementation
		case "mssql":
			;
			break; // prepared for further Persistence Implementation
		}
	}

	// Customer Methods
	public boolean addCustomer(Customer cust) {
		return pomPersistance.addCustomer(cust);
	}

	public boolean updateCustomer(Customer cust) {
		if (pomPersistance.updateCustomer(cust)) {
			return true;
		}
		return false;
	}

	public List<Address> getAddressList(String custId) {
		return pomPersistance.getAddressList(custId);
	}

	public List<Contact> getContactList(String custId) {
		return pomPersistance.getContactList(custId);
	}

	public List<BankAccount> getBankAccountList(String custId) {
		return pomPersistance.getBankAccountList(custId);
	}

	// Order Methods
	public boolean addOrder(Order order) {
		return pomPersistance.addOrder(order);
	}

	public boolean updateOrder(Order order) {
		return pomPersistance.updateOrder(order);
	}

	public boolean deleteOrder(String orderno) {
		return pomPersistance.deleteOrder(orderno);
	}

	public Customer getCustomer(String customerId) {
		return pomPersistance.getCustomer(customerId);
	}

	public List<Customer> getCustomerList() {
		return pomPersistance.getCustomerList();
	}

	public List<Customer> getCustomerNameList() {
		return pomPersistance.getCustomerList();
	}

	public boolean deleteCustomer(String id) {
		return pomPersistance.deleteCustomer(id);
	}

	public List<Order> getOrderList() {
		return pomPersistance.getOrderList();
	}

	public List<String> getProductList() {
		return mesPersistance.getProductList();
	}

	// MES Methods

	public List<Lot> getLotList(String OrderNo) {
		return mesPersistance.getLotList(OrderNo);
	}

	public boolean updateLots(Order order) {
		boolean succes = false;
		// new higher Volume - old Volume from initialize => Volume to insert in Lot Table
		int remainingVolume = order.volumeProperty().get() - order.getOrderLotChanges().volumeProperty().get(); 
		if (remainingVolume > 0){
			succes = insertLotDayBalanced(order, remainingVolume);
		}
		if ((order.priorityProperty().get() != order.getOrderLotChanges().priorityProperty().get())||(!order.getDueDate().toString().equals(order.getOrderLotChanges().getdueDate().toString()))){
			succes = mesPersistance.updateLots(order);
		}
		if(succes)
			order.setOrderLotChange();
		return succes;
	}

	/*
	 * @returns Remaining Capacity of a date
	 */
	public int getDayCapacity(Date date) {
		return pomPersistance.getDayCapacity() - mesPersistance.getDayWorkload(date);
	}

	public boolean releaseOrder(Order order) {
		int remainingVolume = order.volumeProperty().get();
		if (insertLotDayBalanced(order, remainingVolume)) {
			// set Status and ReleaseDate
			order.setState(State.IN_PROCESS);
			order.setReleaseDate(LocalDate.now());
			pomPersistance.updateOrder(order);
			return true;
		}
		return false;
	}

	private boolean insertLotDayBalanced(Order order, int remainingVolume) {
		int n;
		Calendar c = Calendar.getInstance();
		c.setTime(Date.from(order.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		boolean success = false;
		int i = mesPersistance.getLotCount(order.ordernoProperty().get());
		i+=1;

		Lot lotTemplate = new Lot(null, order.priorityProperty().get(), order.lotSizeProperty().get(), "RDY",
				order.productProperty().get(), order.customeridProperty().get(), order.ordernoProperty().get(),
				order.getDueDate(), order.getStartDate());

		while (remainingVolume > 0) {
			n = getDayCapacity(c.getTime());

			while (n > 0 && remainingVolume > 0) {
				n--;
				if (remainingVolume < order.lotSizeProperty().get())
				{
					lotTemplate.piecesProperty().set(remainingVolume);
					n = 0;
				}

				lotTemplate.idProperty().set(order.baseLotIdProperty().get() + Integer.toString(i++));
				success = mesPersistance.addLot(lotTemplate);
				remainingVolume -= lotTemplate.piecesProperty().get();

			}
			c.add(Calendar.DATE, 1);
			LocalDate d = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
			System.out.println(d.toString());
			lotTemplate.setStartDate(d);
		}
		return success;
	}
}