package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import types.Customer;
import types.Order;
import types.State;

import java.time.temporal.ChronoUnit;

public class ShowOrderController {
	private MainMenu mainMenu;
	private String customerId;
	private boolean showHistory;
	@FXML private TableView<Order> orderTable;
	@FXML private TableView<Order> delayOrderTable;
	@FXML private Label lbl_statistic2;
	@FXML private Label lbl_statistic4;
	@FXML private Label lbl_delay2;
	@FXML private TableColumn<Order, String> orderno;
    @FXML private TableColumn<Order, LocalDate> dueDate;
    @FXML private TableColumn<Order, LocalDate> actualDeliveryDate;
    @FXML private TableColumn<Order, String> state;
    @FXML private TableColumn<Order, String> delay;
    
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
        	
        	//Finished in Time Row Count 
        	lbl_statistic2.setText(orderTable.getItems().size() + "/" + (delayOrderTable.getItems().size()+orderTable.getItems().size()) +" ");
        	//Finished on Delay Row Count
        	lbl_statistic4.setText(delayOrderTable.getItems().size() + "/" + (delayOrderTable.getItems().size()+orderTable.getItems().size()));
        	
        	LocalDate dueDate = LocalDate.of(2014, 6, 28);
        	LocalDate actualDeliveryDate = LocalDate.of(2014, 7, 1);

        	//LocalDate test = dueDate.getCellObservableValue(delayOrderTable.getItems().get()).getValue();
        	//delayOrderTable.getSelectionModel().clearAndSelect(2,delayOrderTable.getVisibleLeafColumn(0));
        	orderTable.getSelectionModel().clearAndSelect(1);
        	orderTable.getSelectionModel().getSelectedItem();
        
        	//delayOrderTable.getSelectionModel().selectedItemProperty();
        	//order.getReleaseDate().toString();
        	//System.out.println(TableColumn dueDate.getValue());
        	
           	//dueDate und acutalDeliveryDate hierher übergeben bzw woanders berechnen und delay ï¿½bergeben
        	long daysBewteen = ChronoUnit.DAYS.between(dueDate, actualDeliveryDate);       
        	//delayOrderTable.getItems().dueDate();
        	
        	//Delay Text
        	if (daysBewteen >= 0)
        		lbl_delay2.setText(daysBewteen + " Days");
        	else
        		lbl_delay2.setText("0 Days");
        }
    }
}
