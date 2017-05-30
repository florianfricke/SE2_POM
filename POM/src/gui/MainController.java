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
	@FXML private TableView<Customer> table;
	@FXML private TableColumn<Customer, String> id;
    @FXML private TableColumn<Customer, String> name;
    @FXML private TableColumn<Customer, String> ranking;
    @FXML private TableColumn<Customer, String> comment;
    @FXML private Button btnDashboard;
    @FXML private Button btnCustomers;
    @FXML private Button btnOrders;
    @FXML private Button btnSetUp;
    @FXML private Pane content;

    
	public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ranking.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        comment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }
    
    
    public void setMainApp(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    
  //MouseClick on Row and open Customer Card
    @FXML private void handleRowClickCust(MouseEvent click) {
    	if(click.getClickCount() != 2) return; //just Double Click
        System.out.println("clicked on Customer: " + (table.getSelectionModel().getSelectedItem()).idProperty().get());
        Customer cust = table.getSelectionModel().getSelectedItem();
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
    	mainMenu.deleteCustomer(table.getSelectionModel().getSelectedItem());
    }
    
    @FXML private void handleNewOrder(ActionEvent event) {
    	System.out.println("New Order");
    	//Code Einfügen
    }
    
    @FXML private void handleDelOrder(ActionEvent event) {
    	System.out.println("Delete");
    	//Code Einfügen
    }
    
    @FXML private void handleRowClickOrder(ActionEvent event) {
    	System.out.println("RowClick");
    	//Code Einfügen
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
    
    
    public void loadTable(){
        table.setItems(mainMenu.getCustomerList());
    }
    
    
}
