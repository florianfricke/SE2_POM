package gui;
import types.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController{
	private MainMenu mainMenu;
	@FXML private TableView<Customer> customerTable;
	@FXML private TableView<Customer> orderTable;
	//Customer Table
	@FXML private TableColumn<Customer, String> customerId;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerRanking;
    @FXML private TableColumn<Customer, String> customerComment;
    
    //Order Table
	@FXML private TableColumn<Order, String> orderId;
    @FXML private TableColumn<Order, String> product;
    @FXML private TableColumn<Order, Integer> priority;
    @FXML private TableColumn<Order, String> customer; 
    @FXML private TableColumn<Order, String> orderDate;
    @FXML private TableColumn<Order, String> releaseDate;
    @FXML private TableColumn<Order, String> state;
    @FXML private Button btnDashboard;
    @FXML private Button btnCustomers;
    @FXML private Button btnOrders;
    @FXML private Button btnSetUp;
    @FXML private Pane content;

    
	public void initialize(URL location, ResourceBundle resources) {

    }
    
    
    public void setMainApp(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    
  //MouseClick on Row and open Customer Card
    @FXML private void handleRowClickCust(MouseEvent click) {
    	if(click.getClickCount() != 2) return; //just Double Click
        System.out.println("clicked on Customer: " + (customerTable.getSelectionModel().getSelectedItem()).idProperty().get());
        Customer cust = customerTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerCard.fxml"));
            Parent root = fxmlLoader.load();
            CustomerController custCtrl = (CustomerController)fxmlLoader.getController();
            custCtrl.init(this.mainMenu,cust);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
    @FXML private void handleNewCust(ActionEvent event) {
    	System.out.println("New");
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerCard.fxml"));
            Parent root = fxmlLoader.load();
            CustomerController custCtrl = (CustomerController)fxmlLoader.getController();
            custCtrl.init(this.mainMenu);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    
 
    
    @FXML private void handleDelCust(ActionEvent event) {
    	System.out.println("Delete");
    	mainMenu.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
    }
    
    @FXML private void handleNewOrder(ActionEvent event) {
    	System.out.println("New Order");
       	try {
  
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
            Parent root = fxmlLoader.load();
            CustomerController custCtrl = (CustomerController)fxmlLoader.getController();
            custCtrl.init(this.mainMenu);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
    @FXML private void handleDelOrder(ActionEvent event) {
    	System.out.println("Delete");
    	//Code Einf�gen
    }
    
    @FXML private void handleRowClickOrder(ActionEvent event) {
    	System.out.println("RowClick");
    	//Code Einf�gen
    }
    
    @FXML private void handleDash(ActionEvent event) {
    	System.out.println("Menu Dashboard");
    	mainMenu.changeScene("DashboardSubPage.fxml", event); 
    }
    @FXML private void handleCust(ActionEvent event) {
    	System.out.println("Menu Customers");
    	mainMenu.changeScene("CustomerSubPage.fxml", event);
    }
    @FXML private void handleOrder(ActionEvent event) {
    	System.out.println("Menu Orders");
    	mainMenu.changeScene("OrderSubPage.fxml", event);
    }
    
    
    public void loadCustomerTable(){
        customerId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        customerName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customerRanking.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        customerComment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        customerTable.setItems(mainMenu.getCustomerList());
    }
    public void loadOrderTable(){
    	/*
        orderId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        product.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priority.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        orderDate.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        releaseDate.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        state.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        customerTable.setItems(mainMenu.getCustomerList());
        */
    }
    
    
}
