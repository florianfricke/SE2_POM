package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import types.Order;
import types.State;

import java.time.temporal.ChronoUnit;

public class ShowOrderController {
	private MainMenu mainMenu;
	private String customerId;
	private boolean showHistory;
	@FXML private TableView<Order> orderTable;
	@FXML private TableView<Order> delayOrderTable;
	@FXML private TableView<Order> canceledTable;
	@FXML private Label lbl_statisticFinishedInTime;
	@FXML private Label lbl_statisticFinishedOnDelay;
	@FXML private Label lbl_statisticCanceled;
	@FXML private TableColumn<Order, String> orderno;
    @FXML private TableColumn<Order, LocalDate> dueDate;
    @FXML private TableColumn<Order, LocalDate> actualDeliveryDate;
    @FXML private TableColumn<Order, String> state;
    @FXML private TableColumn<Order, Number> delay;
    
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
       	
        if(!showHistory)	//ShowCurrentOrder
        {
        	//Table with PLANNED, IN_PROCESS, COMPLETED Order
        	List<Order> currentOrders = mainMenu.getCustomerOrder(customerId).stream().filter(p -> p.stateProperty().get() == State.PLANNED.name() || p.stateProperty().get() == State.IN_PROCESS.name() || p.stateProperty().get() == State.COMPLETED.name()).collect(Collectors.toList());
        	orderTable.setItems(FXCollections.observableList(currentOrders));
        }
        else				//ShowOrderHistory
        {		
        	//Table with FINISHED_IN_TIME Order
        	List<Order> inTimeOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_IN_TIME.name()).collect(Collectors.toList());
        	orderTable.setItems(FXCollections.observableList(inTimeOrders));
        	//Table with FINISHED_DELAY Order
        	List<Order> delayOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_DELAY.name()).collect(Collectors.toList());
        	//Delay in Days
        	for (Order order : delayOrders) {
        		int daysBewteen = (int)ChronoUnit.DAYS.between(order.getDueDate(), order.getActualDeliveryDate());
        		order.delayProperty().set(daysBewteen);
				
		}
        	delayOrderTable.setItems(FXCollections.observableList(delayOrders));
        	//Table with CANCELED Order
        	List<Order> canceledOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.CANCELED.name()).collect(Collectors.toList());
        	canceledTable.setItems(FXCollections.observableList(canceledOrders));
        	
        	int total = orderTable.getItems().size() + delayOrderTable.getItems().size() + canceledTable.getItems().size();
        	
        	//Finished in Time Row Count 
        	lbl_statisticFinishedInTime.setText(orderTable.getItems().size() + "/" + total +" ");
        	//Finished on Delay Row Count
        	lbl_statisticFinishedOnDelay.setText(delayOrderTable.getItems().size() + "/" + total +" ");
        	//Canceled Row Count
        	lbl_statisticCanceled.setText(canceledTable.getItems().size() + "/" + total);
        }
    }
}
