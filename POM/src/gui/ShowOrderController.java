package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import types.Order;
import types.State;

public class ShowOrderController {
	private MainMenu mainMenu;
	private String customerId;
	private boolean showHistory;
	@FXML private TableView<Order> orderTable;
	@FXML private TableView<Order> delayOrderTable;
	@FXML private TableColumn<Order, String> orderno;
    @FXML private TableColumn<Order, String> state;
    //@FXML private TableColumn<Order, String> delay;
    
	public void initialize(URL location, ResourceBundle resources) {
		
    }
	
    public void init(MainMenu mainMenu, String customerId, boolean showHistory) {
        this.mainMenu = mainMenu;
        this.customerId = customerId;
        this.showHistory = showHistory;
        loadCustomerOrder();
    }
    
    public void loadCustomerOrder(){   	
       	orderno.setCellValueFactory(cellData -> cellData.getValue().ordernoProperty());
       	
        if(!showHistory){
        	orderTable.setItems(mainMenu.getCustomerOrder(customerId));
        }else{
        	List<Order> inTimeOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_IN_TIME.name()).collect(Collectors.toList());
        	List<Order> delayOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_DELAY.name()).collect(Collectors.toList());
        	orderTable.setItems(FXCollections.observableList(inTimeOrders));
        	delayOrderTable.setItems(FXCollections.observableList(delayOrders));
        }
    }
}
