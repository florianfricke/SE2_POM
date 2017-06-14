package gui;
import types.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Customer");
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
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("New Customer");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
    @FXML private void handleDelCust(ActionEvent event) {
    	if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete: " +customerTable.getSelectionModel().getSelectedItem().nameProperty().get().toString()) == true){
    		System.out.println("Delete");
    		mainMenu.deleteCustomer(customerTable.getSelectionModel().getSelectedItem());
    	}
    }
    
    @FXML private void handleNewOrder(ActionEvent event) {
    	System.out.println("New Order");
       	try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
            Parent root = fxmlLoader.load();
            OrderController orderCtrl = (OrderController)fxmlLoader.getController();
            orderCtrl.init(this.mainMenu);
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("New Order");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
    @FXML private void handleDelOrder(ActionEvent event) {
    	if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete: order " +orderTable.getSelectionModel().getSelectedItem().ordernoProperty().get().toString()) == true){
    		System.out.println("Delete");
    		mainMenu.deleteOrder(orderTable.getSelectionModel().getSelectedItem());
    	}
    }
    
    @FXML private void handleRowClickOrder(MouseEvent click) {
    	if(click.getClickCount() != 2) return; //just Double Click
        System.out.println("clicked on Order: " + (orderTable.getSelectionModel().getSelectedItem()).ordernoProperty().get());
        Order order = orderTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
            Parent root = fxmlLoader.load();
            OrderController orderCtrl = (OrderController)fxmlLoader.getController();
            orderCtrl.init(this.mainMenu,order);
            Scene scene = new Scene(root, 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Order");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
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
        comboBoxSearchListCustomer();
        FilteredList<Customer> filteredData = new FilteredList<>(mainMenu.getCustomerList(), p -> true);
        txt_searchFieldCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
        	filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                	// If filter text is empty, display all orders.
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
        customerTable.setItems(sortedData);
    }
    public void loadOrderTable(){
        orderId.setCellValueFactory(cellData -> cellData.getValue().ordernoProperty());
        product.setCellValueFactory(cellData -> cellData.getValue().productProperty());
        priority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        customer.setCellValueFactory(cellData -> cellData.getValue().customeridProperty());
        state.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        comboBoxSearchListOrder();
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Order> filteredData = new FilteredList<>(mainMenu.getOrderList(), p -> true);
        txt_searchFieldOrder.textProperty().addListener((observable, oldValue, newValue) -> {
        	filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                	// If filter text is empty, display all orders.
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
                	} else if (order.getReleaseDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;  
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
                	if (order.getReleaseDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
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
        orderTable.setItems(sortedData);
    }
	public void comboBoxSearchListOrder(){
		//Wo muss diese Methode hin?
		list_searchOrder = FXCollections.observableArrayList("All","ID","Product","Priority","Customer","Order Date","Release Date","State");
		comboBox_searchOrder.setItems(list_searchOrder);
		comboBox_searchOrder.getSelectionModel().select(0);
	}
	public void comboBoxSearchListCustomer(){
		//Wo muss diese Methode hin?
		list_searchCustomer = FXCollections.observableArrayList("All","ID","Name","Ranking","Comment");
		comboBox_searchCustomer.setItems(list_searchCustomer);
		comboBox_searchCustomer.getSelectionModel().select(0);
	}
}
