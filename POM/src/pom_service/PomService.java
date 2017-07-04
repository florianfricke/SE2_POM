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
	private Setup setup;

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
		if(!pomPersistance.hasSetup()){
			pomPersistance.upsertSetup(new Setup());
		}
		setup = pomPersistance.getSetup();
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
	/**
	 * Returns a Setup Object 
	 * @version 1.0
	 * @return Setup - Instance of type Setup
	 */
	public Setup getSetup(){
		return setup;
	}
	/**
	 * Inserts or Updates a new or existing Setup Dataset
	 * @version 1.0
	 * @param setup - Instance of type Setup
	 * @return boolean for success or failure 
	 */
	public boolean upsertSetup(){
		return pomPersistance.upsertSetup(this.setup);
	}

	// Customer Methods
	/**
	 * Inserts a Customer into the Database and all related Address-, Contact- or Bank account data
	 * @version 1.0
	 * @param cust - Instance of type Customer
	 * @return boolean for success or failure
	 */
	public boolean addCustomer(Customer cust) {
		return pomPersistance.addCustomer(cust);
	}
	
	/**
	 * Updates a Customer and all Address-, Contact-, or Bancaccount data in the Database if data was chaged.
	 * The function also handles added or deleted Addresses, Contacts or Bankaccounts.
	 * @version 1.0
	 * @param cust - Instance of type Customer
	 * @return boolean for success or failure
	 */
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
	
	/**
	 * Checks, if address is referenced to an Order
	 * 
	 * @param adress
	 * @returns true, if address is referenced
	 */
	public boolean isReferenced(Address address)
	{
		return pomPersistance.isReferenced(address.idProperty().get(),"addressid");
		
	}
	
	/**
	 * Checks, if contact is referenced to an Order
	 * 
	 * @param contact
	 * @returns true, if contact is referenced
	 */
	public boolean isReferenced(Contact contact)
	{
		return pomPersistance.isReferenced(contact.idProperty().get(),"contactid");
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
	
	public boolean cancelOrder(Order order)
	{
		if(mesPersistance.getLotInProcessCount(order.ordernoProperty().get()) == 0)
		{
			if(mesPersistance.cancelLots(order.ordernoProperty().get()))
			{
				order.setState(State.CANCELED);
				return pomPersistance.updateOrder(order);
			}
		}
		
		return false;
	}
	
	public boolean finishOrder(Order order)
	{
		order.setActualDeliveryDate(LocalDate.now()); //set CurrentDate as ActualDeliveryDate
		
		if(order.getActualDeliveryDate().isAfter(order.getDueDate()))
		{
			order.setState(State.FINISHED_DELAY);
		}
		else
		{
			order.setState(State.FINISHED_IN_TIME);
		}
		
		return updateOrder(order);
	}
	/**
	 * Returns a Customer from the Database with all related Addresses, Contacts, Bankaccounts
	 * @return List of Customers
	 * @version 1.0
	 */
	public Customer getCustomer(String customerId) {
		return pomPersistance.getCustomer(customerId);
	}
	/**
	 * Returns a List of all Customers in Database without the related Addresses, Contacts, Bankaccounts
	 * @return List of Customers
	 * @version 1.0
	 */
	public List<Customer> getCustomerList() {
		return pomPersistance.getCustomerList();
	}

	public List<Customer> getCustomerNameList() {
		return pomPersistance.getCustomerList();
	}
	
	public List<Order> getCustomerOrder(String customerID) {
		return pomPersistance.getCustomerOrder(customerID);
	}
	
	public List<Order> getCustomerOrderHistory(String customerID) {
		return pomPersistance.getCustomerOrderHistory(customerID);
	}
	/**
	 * Deletes a Customer from the Database and all related Address-, Contact- or Bank account data
	 * @version 1.0
	 * @param id - (String) Id of the Customer
	 * @return boolean for success or failure
	 */
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
	/**
	 * Returns a List of all Lots of an Order 
	 * @return List<Lot> List of Lots
	 * @param OrderNo Order No. of the Order the Lots are related to
	 * @version 1.0
	 */
	public List<Lot> getLotList(String OrderNo) {
		return mesPersistance.getLotList(OrderNo);
	}
	/**
	 * Updates Fields Priority, Volume, DueDate of all lots of a certain order.
	 * If Volume has been increased, new Lots will be inserted by consideration of Day Capacity. 
	 * @see #insertLotDayBalanced
	 * @return boolean for success or failure
	 * @version 1.0
	 */
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

	/**
	 * 
	 * @returns Remaining Capacity of a date
	 */
	public int getDayCapacity(Date date) {
		return setup.dayCapacityProperty().get() - mesPersistance.getDayWorkload(date);
	}

	public boolean releaseOrder(Order order) {
		int remainingVolume = order.volumeProperty().get();
		if (insertLotDayBalanced(order, remainingVolume)) {
			// set Status and ReleaseDate
			order.setState(State.IN_PROCESS);
			order.setReleaseDate(LocalDate.now());
			pomPersistance.updateOrder(order);
			order.setOrderLotChange();
			return true;
		}
		return false;
	}

	private boolean insertLotDayBalanced(Order order, int remainingVolume) {
		int n;
		if(order.getStartDate().isBefore(LocalDate.now())) return false;
		Calendar c = Calendar.getInstance();
		c.setTime(Date.from(order.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		boolean success = false;
		int i = mesPersistance.getLotCount(order.ordernoProperty().get());
		i+=1;

		Lot lotTemplate = new Lot(null, order.priorityProperty().get(), order.lotSizeProperty().get(), "RDY",
				order.productProperty().get(), order.customeridProperty().get(), order.ordernoProperty().get(),
				order.getDueDate(), order.getStartDate(),"","");

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
			lotTemplate.setStartDate(d);
		}
		return success;
	}
	
	/**
	 * Returns a List of all Routes the given Product has to run through
	 * @return List of Route
	 * @param orderno Order No. of the Order the Lots are related to
	 * @param product which is added to Order
	 * @version 1.0
	 */
	public List<Route> getRouteList(String orderno,String product){
		return mesPersistance.getRouteList(orderno, product);
	}
	
	/**
	 * checks, if the order's dueDate is still viable, or if a lot starts even after the duedate
	 * 
	 * @param order Object to check
	 * @return true for viable
	 */
	public boolean isDueDateViable(Order order)
	{
		return !mesPersistance.getLatestStartDate(order.ordernoProperty().get()).isAfter(order.getDueDate());
	}
	
	/**
	 *Checks whether the preferred BaselotID already exists
	 * @param baseLotId - String of BaselotId
	 * @return boolean true if exists or false if not exists
	 * @version 1.0
	 */
	public boolean checkBaseLotIDExists(String baseLotId){
		return mesPersistance.checkBaseLotIDExists(baseLotId);
	}


}