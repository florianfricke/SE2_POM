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
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	@Override
	public void start(Stage stage) {
		pomService = new PomService(SaveType.postgres);
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
		
		Scene scene = new Scene(root);
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
		if(pomService.deleteCustomer(cust.idProperty().get())){
			customerList.remove(cust);
		}
		
	}
	
	public void addOrder(Order order){
		if(pomService.addOrder(order)){
			if(!orderList.contains(order)){
				this.orderList.add(order);
			}
		}
	}
	
	public void deleteOrder(Order order){
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
			stage.setScene(new Scene(loader.load()));
			MainController mc = (MainController)loader.getController();
			mc.setMainApp(this);
			System.out.println(((Button)event.getSource()).getId());
			if (((Button)event.getSource()).getId().equals("btnCustomers")){
				mc.loadCustomerTable();
			}
			if (((Button)event.getSource()).getId().equals("btnOrders")){
				mc.loadOrderTable();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
