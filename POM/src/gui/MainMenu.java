package gui;
import types.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pom_service.PomService;

import java.io.IOException;

public class MainMenu extends Application {
	private PomService pomService;
	private ObservableList<Customer> customerList;
	private ObservableList<Order> orderList;
	private ObservableList<Order> getCustomerOrder;
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	@Override
	public void start(Stage stage) {
		pomService = new PomService(SaveType.postgres, SaveType.postgres);
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			root = loader.load();
			MainController mc = (MainController)loader.getController(); 
			System.out.println("FXML wurde geladen.");
			mc.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Das war wohl nix.");
		}
		
		Scene scene = new Scene(root, 900, 600);
		stage.setTitle("POM");
		stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        stage.setScene(scene);
        stage.show();
	}
	
	public ObservableList<Customer> getCustomerList() {
		this.customerList = FXCollections.observableList(pomService.getCustomerList());
		return this.customerList;
	}
	public ObservableList<Order> getOrderList() {
		return FXCollections.observableList(pomService.getOrderList());
	}
	
	public ObservableList<Order> getCustomerOrder(String customerID) {
		return FXCollections.observableList(pomService.getCustomerOrder(customerID));
	}
	
	public void saveCustomer(Customer cust){
		if(cust.idProperty().get().isEmpty()){
			if(pomService.addCustomer(cust)){
				this.customerList.add(cust);
			}
		}else{
			pomService.updateCustomer(cust);
		}
	}
	
	public void deleteCustomer(Customer cust){
		if (cust.idProperty().get().isEmpty())
			return;
		if(pomService.deleteCustomer(cust.idProperty().get())){
			customerList.remove(cust);
		}
		
	}
	
	public void saveOrder(Order order){
		if(order.ordernoProperty().get().isEmpty()){
			if(pomService.addOrder(order)){
				this.orderList.add(order);
			}
		}else {
			pomService.updateOrder(order);

		}
	}
	
	public void addOrder(Order order){
		if(pomService.addOrder(order)){
			this.orderList.add(order);
		}
		    
	}
	public void updateOrder(Order order){
		pomService.updateOrder(order);
	}
	
	public void deleteOrder(Order order){
		if (order.ordernoProperty().get().isEmpty())
			return;
		if(pomService.deleteOrder(order.ordernoProperty().get())){
			orderList.remove(order);
		}
		
	}
	
	public ObservableList<Address> getAddressList(String custId){
		return FXCollections.observableList(pomService.getAddressList(custId));
	}
	public ObservableList<Contact> getContactList(String custId){
		return FXCollections.observableList(pomService.getContactList(custId));
	}
	public ObservableList<BankAccount> getBankAccountList(String custId){
		return FXCollections.observableList(pomService.getBankAccountList(custId));
	}
	
	public void changeScene(String fxml, ActionEvent event){
    	final Node currStage = (Node)event.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			stage.setScene(new Scene(loader.load(),(stage.getWidth()-13), (stage.getHeight()-35)));
			MainController mc = (MainController)loader.getController();
			mc.setMainApp(this);
			System.out.println(((Button)event.getSource()).getId());
			if (((Button)event.getSource()).getId().equals("btnCustomers")){
				mc.loadCustomerTable();
			}
			if (((Button)event.getSource()).getId().equals("btn_ShowCurrentOrders")){
				mc.loadCustomerOrder();
			}
			if (((Button)event.getSource()).getId().equals("btnOrders")){
				mc.loadOrderTable();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public Customer getCustomer(String customerId){
		return pomService.getCustomer(customerId);
	}
	public boolean releaseOrder(Order order) {
		return pomService.releaseOrder(order);
	}
	public boolean updateLots(Order order) {
		return pomService.updateLots(order);
	}
	public ObservableList<String> getProductList(){
		return FXCollections.observableList(pomService.getProductList());
	}
	
	public ObservableList<Lot> getLotList(String orderNo){
		return FXCollections.observableList(pomService.getLotList(orderNo));
	}
}
