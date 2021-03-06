package gui;
import types.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
public class MainController{
	private MainMenu mainMenu;
	@FXML private TableView<Customer> customerTable;
	@FXML private TableView<Order> orderTable;
	//Customer Table
	@FXML private TableColumn<Customer, String> customerId;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerRanking;
    @FXML private TableColumn<Customer, String> customerComment;
    //Order Table
	@FXML private TableColumn<Order, String> orderId;
    @FXML private TableColumn<Order, String> product;
    @FXML private TableColumn<Order, Number> priority;
    @FXML private TableColumn<Order, String> customer; 
    @FXML private TableColumn<Order, Date> orderDate;
    @FXML private TableColumn<Order, String> releaseDate;
    @FXML private TableColumn<Order, String> state;
    @FXML private Button btnDashboard;
    @FXML private Button btnCustomers;
    @FXML private Button btnOrders;
    @FXML private Button btnSetUp;
    @FXML private Pane content;
    //Setup Page
    @FXML private TextField txt_dayCapacity;
    @FXML private TextField txt_defaultLotSize;
    //Filter Table
    @FXML private TextField txt_searchFieldOrder;
    @FXML private TextField txt_searchFieldCustomer;
    @FXML private ComboBox<String> comboBox_searchOrder;
    @FXML private ComboBox<String> comboBox_searchCustomer;
    private ObservableList<String> list_searchOrder;
    private ObservableList<String> list_searchCustomer;
	public void initialize(URL location, ResourceBundle resources) {
    }
    public void setMainApp(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
	/**
	 * Open Customer Card with selected Customer
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */
    @FXML private void handleRowClickCust(MouseEvent click) {
    	if(click.getClickCount() != 2) return; //just Double Click
    	if(customerTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Customer!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
        Customer cust = customerTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerCard.fxml"));
            Parent root = fxmlLoader.load();
            CustomerController custCtrl = (CustomerController)fxmlLoader.getController();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Customer");
            stage.setScene(scene);
            custCtrl.init(this.mainMenu,cust, stage);
			stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
	/**
	 * Handle the event if "New Customer" button is clicked
	 * Opens empty Customer Card
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleNewCust(ActionEvent event) {
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerCard.fxml"));
            Parent root = fxmlLoader.load();
            CustomerController custCtrl = (CustomerController)fxmlLoader.getController();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("New Customer");
            stage.setScene(scene);
            custCtrl.init(this.mainMenu, stage);
			stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
	/**
	 * Handle the event if "Delete Customer" button is clicked
	 * Try to delete customer if customer object is not used
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleDelCust(ActionEvent event) {
    	if(customerTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Customer!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete: " +customerTable.getSelectionModel().getSelectedItem().nameProperty().get().toString()) == true){
	    	if(mainMenu.deleteCustomer(customerTable.getSelectionModel().getSelectedItem()) == false) {
	    		Alert alert = new Alert(AlertType.ERROR);
	        	alert.setTitle("Notification");
	        	alert.setHeaderText("There already existing Orders for this Customer");
	        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
	        	alert.show();
	        	return;
	    	}
	    }
    }
	/**
	 * Handle the event if "Show Current Order" button is clicked in customer card
	 * Open "Current Order" Card with selected customer
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleShowCurrentOrder(ActionEvent event) {
    	if(customerTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Customer!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	else
	    	{
        	Customer cust = customerTable.getSelectionModel().getSelectedItem();
    		List<Order> currentOrders = mainMenu.getCustomerOrder(cust.idProperty().get()).stream().filter(p -> p.stateProperty().get() == State.PLANNED.name() || p.stateProperty().get() == State.IN_PROCESS.name() || p.stateProperty().get() == State.COMPLETED.name()).collect(Collectors.toList());
    		if(currentOrders.isEmpty() == true ){
		    	Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Notification");
		    	alert.setHeaderText("The customer has no PLANNED, IN_PROCESS or COMPLETED order!");
	        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
		    	alert.show();
	    	}
	    	else
	    	{
		    	try {
		            FXMLLoader fxmlLoader = new FXMLLoader();
		            fxmlLoader.setLocation(getClass().getResource("ShowCurrentOrder.fxml"));
		            Parent root = fxmlLoader.load();
		            ShowOrderController showOrderCtrl = (ShowOrderController)fxmlLoader.getController();
		            showOrderCtrl.init(this.mainMenu, cust.idProperty().get(), false);
		            Scene scene = new Scene(root, 800, 500);
		            Stage stage = new Stage();
		            stage.setTitle("Show Current Order");
		            stage.setScene(scene);
		            stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
		            stage.initModality(Modality.APPLICATION_MODAL);
		            stage.showAndWait();
		        } catch (IOException e) {
		            Logger logger = Logger.getLogger(getClass().getName());
		            logger.log(Level.SEVERE, "Failed to create new Window.", e);
		        } 
	    	}
	    }
	}
	/**
	 * Handle the event if "Show open History" button is clicked in customer card
	 * Open "Order History" Card for selected customer
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
	@FXML private void handleShowOrderHistory(ActionEvent event) {
    	if(customerTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Customer!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	else
    	{
    		Customer cust = customerTable.getSelectionModel().getSelectedItem();
    		List<Order> historyOrders = mainMenu.getCustomerOrderHistory(cust.idProperty().get()).stream().filter(p -> p.stateProperty().get() == State.FINISHED_IN_TIME.name() || p.stateProperty().get() == State.FINISHED_DELAY.name() || p.stateProperty().get() == State.CANCELED.name()).collect(Collectors.toList());
    		if(historyOrders.isEmpty() == true ){
		    	Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("Notification");
		    	alert.setHeaderText("The customer has no FINISHED or CANCELED order!");
	        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
		    	alert.show();
	    	}
	    	else
	    	{
            try {
            	FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ShowOrderHistory.fxml"));
                Parent root = fxmlLoader.load();
                ShowOrderController showOrderCtrl = (ShowOrderController)fxmlLoader.getController();
                showOrderCtrl.init(this.mainMenu, cust.idProperty().get(), true);
                Scene scene = new Scene(root, 800, 500);
                Stage stage = new Stage();
                stage.setTitle("Show Order History");
                stage.setScene(scene);
                stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            	}
	    	}
    	}
    }
	/**
	 * Handle the event if "New Order" button is clicked
	 * Opens empty Order Card
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleNewOrder(ActionEvent event) {
       	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
            Parent root = fxmlLoader.load();
            OrderController orderCtrl = (OrderController)fxmlLoader.getController();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("New Order");
            stage.setScene(scene);
            orderCtrl.init(this.mainMenu, stage);
			stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
       	orderTable.refresh();
    }
	/**
	 * Handle the event if "Delete Order" button is clicked
	 * Try to delete order if State is PLANNED
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleDelOrder(ActionEvent event) {
    	if(orderTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Order!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	if(orderTable.getSelectionModel().getSelectedItem().stateProperty().get().equals(State.PLANNED.toString())){
        	if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete: order " +orderTable.getSelectionModel().getSelectedItem().ordernoProperty().get().toString()) == true){
        		mainMenu.deleteOrder(orderTable.getSelectionModel().getSelectedItem());
        	}
    	} else {
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Cannot delete order");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    }
	/**
	 * Handle the event if "Cancel Order" button is clicked
	 * Try to cancel order if order is already IN_PROCESS
	 * Just possible if all lots have state RDY
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleCancelOrder(ActionEvent event){
    	if(orderTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Order!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	if(orderTable.getSelectionModel().getSelectedItem().stateProperty().get().equals(State.IN_PROCESS.toString())){
    		if (orderTable.getSelectionModel().getSelectedItem() != null){
    			if(ConfirmBox.display("Confirmation Dialog", "Do you really want to cancel: order " +orderTable.getSelectionModel().getSelectedItem().ordernoProperty().get().toString()) == true){
        			if(mainMenu.cancelOrder(orderTable.getSelectionModel().getSelectedItem())){
        				// delete if true
        			}	else {
            			Alert alert = new Alert(AlertType.ERROR);
                    	alert.setTitle("Notificaion");
                    	alert.setHeaderText("Some lots are already IN PROCESS");
                    	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
                    	alert.show();
                    	return;	
    			}
    		}
    			orderTable.refresh();
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Order is not IN PROCESS");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    }
	/**
	 * Handle the event if "Finish" button is clicked
	 * Try to change state to FINISH
	 * Just possible if order is COMPLETED
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
    @FXML private void handleFinishOrder(ActionEvent event){
    	if(orderTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Order!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    	if(orderTable.getSelectionModel().getSelectedItem().stateProperty().get().equals(State.COMPLETED.toString())){
    		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to finish: order " +orderTable.getSelectionModel().getSelectedItem().ordernoProperty().get().toString()) == true){
	    		if (orderTable.getSelectionModel().getSelectedItem() != null){
	    			mainMenu.finishOrder(orderTable.getSelectionModel().getSelectedItem());
	    			orderTable.refresh();
	    		}
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Order is not COMPLETED");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
    }
	/**
	 * Open Order Card with selected Customer
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */
    @FXML private void handleRowClickOrder(MouseEvent click) {
    	if(click.getClickCount() != 2) return; //just Double Click
    	if(orderTable.getSelectionModel().getSelectedItem() == null){
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Select Order!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
        	return;
    	}
        Order order = orderTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
            Parent root = fxmlLoader.load();
            OrderController orderCtrl = (OrderController)fxmlLoader.getController();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Order");
            stage.setScene(scene);
            orderCtrl.init(this.mainMenu,order, stage);
			stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
        orderTable.refresh();
    }
    @FXML private void handleDash(ActionEvent event) {
    	mainMenu.changeScene("DashboardSubPage.fxml", event);
    	mainMenu.upsertSetup();
    }
    @FXML private void handleCust(ActionEvent event) {
    	mainMenu.changeScene("CustomerSubPage.fxml", event);
    	mainMenu.upsertSetup();
    }
    @FXML private void handleOrder(ActionEvent event) {
    	mainMenu.changeScene("OrderSubPage.fxml", event);
    	mainMenu.upsertSetup();
    }
    @FXML private void handleSetup(ActionEvent event) {
    	mainMenu.changeScene("SetupSubPage.fxml", event);
    }
	/**
	 * Open User Manual with external PDF Client
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */
   @FXML private void handleHelp(ActionEvent event) {
	    try {
	        Desktop desktop = Desktop.getDesktop();
            File file = new File("Handbuch/user_manual.pdf");
	        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN) && file.exists()) {
	        	desktop.open(file);
	        } else {
	    		Alert alert = new Alert(AlertType.ERROR);
	        	alert.setTitle("Notification");
	        	alert.setHeaderText("The user manual pdf-file does not exist!");
	        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
	        	alert.show();
	        	return;
	        }
	    } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to open pdf-file.", e);
	    }
    }
    @FXML private void handleAbout(ActionEvent event){
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("About.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }
	/**
	 * If no DB connection is set, open DB Connection Dialog and save settings to txt file
	 * 
	 * @version 1.0
	 * 
	 */
    public void openDatabaseConnectionDialog(){
    	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("DatabaseConnectionDialog.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Database Connection");
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        } 
    }
	/**
	 * Load all customer objects into a table view and set listeners for filter
	 * 
	 * @version 1.0
	 * @return true, if comboBox filter is set
	 */
    @SuppressWarnings("unchecked")
	public void loadCustomerTable(){
        customerId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        customerName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        customerRanking.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        customerComment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        comboBoxSearchListCustomer();
        FilteredList<Customer> filteredData = new FilteredList<>(mainMenu.getCustomerList(), p -> true);
        txt_searchFieldCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
        	filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(comboBox_searchCustomer.getSelectionModel().getSelectedItem() == "All"){
                	if (customer.idProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (customer.nameProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (customer.rankingProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (customer.commentProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                		return true;
                	} 	
                }
                if(comboBox_searchCustomer.getSelectionModel().getSelectedItem() == "ID"){
                	if (customer.idProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchCustomer.getSelectionModel().getSelectedItem() == "Name"){
                	if (customer.nameProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchCustomer.getSelectionModel().getSelectedItem() == "Ranking"){
                	if (customer.rankingProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchCustomer.getSelectionModel().getSelectedItem() == "Comment"){
                	if (customer.commentProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                return false;
        });
        });
        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
        customerId.setSortType(TableColumn.SortType.ASCENDING);
        customerTable.getSortOrder().setAll(customerId);
        customerTable.sort();
        customerTable.setItems(sortedData);
    }
	/**
	 * Load all order objects into a table view and set listeners for filter
	 * 
	 * @version 1.0
	 * @return true, if comboBox filter is set
	 */
    @SuppressWarnings("unchecked")
	public void loadOrderTable(){
        orderId.setCellValueFactory(cellData -> cellData.getValue().ordernoProperty());
        product.setCellValueFactory(cellData -> cellData.getValue().productProperty());
        priority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        customer.setCellValueFactory(cellData -> cellData.getValue().customeridProperty());
        state.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        comboBoxSearchListOrder();
        FilteredList<Order> filteredData = new FilteredList<>(mainMenu.getOrderList(), p -> true);
        txt_searchFieldOrder.textProperty().addListener((observable, oldValue, newValue) -> {
        	filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "All"){
                	if (order.ordernoProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (order.productProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (order.priorityProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                	} else if (order.customeridProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                		return true;
                	} else if (order.getOrderDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;  
                	} else 
                		if(order.getReleaseDate() != null){
                		if (order.getReleaseDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;  
                		}
	                } else if (order.stateProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;  
	            	}           	
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "ID"){
                	if (order.ordernoProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "Product"){
                	if (order.productProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "Priority"){
                	if (order.priorityProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "Customer"){
                	if (order.customeridProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "Order Date"){
                	if (order.getOrderDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "Release Date"){
                	if(order.getReleaseDate() != null){
                		   if (order.getReleaseDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                             }
                	}              	
                }
                if(comboBox_searchOrder.getSelectionModel().getSelectedItem() == "State"){
                	if (order.stateProperty().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                        }
                }
                return false;
        });
        });
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(orderTable.comparatorProperty());
        orderId.setSortType(TableColumn.SortType.ASCENDING);
        orderTable.getSortOrder().setAll(orderId);
        orderTable.sort();
        orderTable.setItems(sortedData);
        orderTable.comparatorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
            	orderTable.getSortOrder().setAll(orderId);
            	  orderId.setSortType(TableColumn.SortType.ASCENDING);
            }
          });
    }
    public void loadSetupPage(){
    	Bindings.bindBidirectional(txt_dayCapacity.textProperty(), this.mainMenu.getSetup().dayCapacityProperty(),new NumberStringConverter());
    	Bindings.bindBidirectional(txt_defaultLotSize.textProperty(), this.mainMenu.getSetup().defaultLotSizeProperty(),new NumberStringConverter());
    }
	public void comboBoxSearchListOrder(){
		list_searchOrder = FXCollections.observableArrayList("All","ID","Product","Priority","Customer","Order Date","Release Date","State");
		comboBox_searchOrder.setItems(list_searchOrder);
		comboBox_searchOrder.getSelectionModel().select(0);
	}
	public void comboBoxSearchListCustomer(){
		list_searchCustomer = FXCollections.observableArrayList("All","ID","Name","Ranking","Comment");
		comboBox_searchCustomer.setItems(list_searchCustomer);
		comboBox_searchCustomer.getSelectionModel().select(0);
	}
}