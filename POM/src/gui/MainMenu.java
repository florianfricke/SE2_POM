package gui;

import types.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pom_service.PomService;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenu extends Application {
	private PomService pomService;
	private ObservableList<Customer> customerList;
	private ObservableList<Order> orderList;
	private MainController mc;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) {
		
		File f = new File("dbConnectionFile.txt");
		if(f.exists() && !f.isDirectory()) { 

		} else {		
	    	try {
	            FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("DatabaseConnectionDialog.fxml"));
	            Parent root = fxmlLoader.load();
	            DatabaseConnectionDialogController dc = (DatabaseConnectionDialogController) fxmlLoader.getController();

	            Scene scene = new Scene(root, 800, 500);
	            Stage stage1 = new Stage();
	            stage1.setTitle("Database Connection");
	            stage1.setScene(scene);
	            dc.init(stage);
	            stage1.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
	            stage1.initModality(Modality.APPLICATION_MODAL);
	            stage1.showAndWait();
	        } catch (IOException e) {
	            Logger logger = Logger.getLogger(getClass().getName());
	            logger.log(Level.SEVERE, "Failed to create new Window.", e);
	        } 
		} 
		
		
		
		pomService = new PomService(SaveType.postgres, SaveType.postgres);
		Parent root = null;
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardSubPage.fxml"));
			root = loader.load();
			mc = (MainController) loader.getController();
			mc.setMainApp(this);
		} catch (IOException e) {
			ErrorLog.write(e);
		}

		Scene scene = new Scene(root);
		stage.setTitle("POM");
		stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();

	}

	public ObservableList<Customer> getCustomerList() {
		this.customerList = FXCollections.observableList(pomService.getCustomerList());
		return this.customerList;
	}

	public ObservableList<Order> getOrderList() {
		this.orderList = FXCollections.observableList(pomService.getOrderList());
		return this.orderList;
	}

	public ObservableList<Order> getCustomerOrder(String customerID) {
		this.orderList = FXCollections.observableList(pomService.getCustomerOrder(customerID));
		return this.orderList;
	}

	public ObservableList<Order> getCustomerOrderHistory(String customerID) {
		this.orderList = FXCollections.observableList(pomService.getCustomerOrderHistory(customerID));
		return this.orderList;
	}

	public void saveCustomer(Customer cust) {
		if (cust.idProperty().get().isEmpty()) {
			if (pomService.addCustomer(cust)) {
				this.customerList.add(cust);
			}
		} else {
			pomService.updateCustomer(cust);
		}
	}

	public boolean deleteCustomer(Customer cust) {
		if (cust.idProperty().get().isEmpty())
			return false;
		if (pomService.deleteCustomer(cust.idProperty().get())) {
			customerList.remove(cust);
			return true;
		
		} else {
			return false;
		}
	}

	public void saveOrder(Order order) {
		if (order.ordernoProperty().get().isEmpty()) {
			if (pomService.addOrder(order)) {
				this.orderList.add(order);
			}
		} else {
			pomService.updateOrder(order);

		}
	}

	public void addOrder(Order order) {
		if (pomService.addOrder(order)) {
			this.orderList.add(order);
		}

	}

	public void updateOrder(Order order) {
		pomService.updateOrder(order);
	}

	public void deleteOrder(Order order) {
		if (order.ordernoProperty().get().isEmpty())
			return;
		if (pomService.deleteOrder(order.ordernoProperty().get())) {
			this.orderList.remove(order);
		}

	}
	
	public boolean cancelOrder(Order order) {
		if (order.ordernoProperty().get().isEmpty())
			return false;
		return (pomService.cancelOrder(order));

	}
	
	public boolean finishOrder(Order order) {
		if (order.ordernoProperty().get().isEmpty())
			return false;
		return (pomService.finishOrder(order));

	}


	public ObservableList<Address> getAddressList(String custId) {
		return FXCollections.observableList(pomService.getAddressList(custId));
	}

	public ObservableList<Contact> getContactList(String custId) {
		return FXCollections.observableList(pomService.getContactList(custId));
	}

	public ObservableList<BankAccount> getBankAccountList(String custId) {
		return FXCollections.observableList(pomService.getBankAccountList(custId));
	}

	public void changeScene(String fxml, ActionEvent event) {
		final Node currStage = (Node) event.getSource();
		Stage stage = (Stage) currStage.getScene().getWindow();
		
		ObservableList<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()));
		Rectangle2D bounds = screens.get(0).getVisualBounds();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			stage.setScene(new Scene(loader.load()));
  			
			MainController mc = (MainController) loader.getController();
			mc.setMainApp(this);
			if (((Button) event.getSource()).getId().equals("btnCustomers")) {
				mc.loadCustomerTable();
			}
			if (((Button) event.getSource()).getId().equals("btnOrders")) {
				mc.loadOrderTable();
			}
			if (((Button) event.getSource()).getId().equals("btnSetUp")) {
				mc.loadSetupPage();
			}
		} catch (IOException e) {
			ErrorLog.write(e);
		}
	}

	public Customer getCustomer(String customerId) {
		return pomService.getCustomer(customerId);
	}

	public boolean releaseOrder(Order order) {
		return pomService.releaseOrder(order);
	}

	public boolean updateLots(Order order) {
		return pomService.updateLots(order);
	}

	public ObservableList<String> getProductList() {
		return FXCollections.observableList(pomService.getProductList());
	}

	public ObservableList<Lot> getLotList(String orderNo) {
		return FXCollections.observableList(pomService.getLotList(orderNo));
	}

	public MainController getMainController() {
		return this.mc;
	}
	public ObservableList<Route> getRouteList(String orderno,String product){
		return FXCollections.observableList(pomService.getRouteList(orderno, product));
	}
	public Setup getSetup(){
		return pomService.getSetup();
	}
	public boolean upsertSetup(){
		return pomService.upsertSetup();
	}

	public boolean isReferenced(Address address) {
		return pomService.isReferenced(address);
	}

	public boolean isReferenced(Contact contact) {
		return pomService.isReferenced(contact);
	}

	public boolean isDueDateViable(Order order) {
		return pomService.isDueDateViable(order);
	}
	
	public boolean checkBaseLotIDExists(String baseLotId){
		return pomService.checkBaseLotIDExists(baseLotId);
	}

}
