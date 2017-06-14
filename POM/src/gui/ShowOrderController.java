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
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableColumn<Order, Integer> delay;
    
	public void initialize(URL location, ResourceBundle resources) {
		//delay.setCellValueFactory(new PropertyValueFactory<Order, Double>("25"));
    }
	
    public void init(MainMenu mainMenu, String customerId, boolean showHistory) {
        this.mainMenu = mainMenu;
        this.customerId = customerId;
        this.showHistory = showHistory;
        loadCustomerOrder();
        //statistic();
        //showDelay();        
    }
    public void loadCustomerOrder(){   	
       	orderno.setCellValueFactory(cellData -> cellData.getValue().ordernoProperty());
       	
        if(!showHistory){
        	orderTable.setItems(mainMenu.getCustomerOrder(customerId));
        }else{
        	int fintime = 1;	//Anzahl finished in time order
        	int findelay = 1;	//Anzahl finished on delay order
        	int finsum = 2;		//Anzahl order
        	LocalDate dueDate = LocalDate.of(2014, 6, 28);
        	LocalDate actualDeliveryDate = LocalDate.of(2014, 7, 1);
        	
        	//dueDate und acutalDeliveryDate hierher übergeben bzw woanders berechnen und delay übergeben
        	long daysBewteen = ChronoUnit.DAYS.between(dueDate, actualDeliveryDate);
        	       	
        	List<Order> inTimeOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_IN_TIME.name()).collect(Collectors.toList());
        	List<Order> delayOrders = mainMenu.getCustomerOrderHistory(customerId).stream().filter(p -> p.stateProperty().get() == State.FINISHED_DELAY.name()).collect(Collectors.toList());
        	orderTable.setItems(FXCollections.observableList(inTimeOrders));
        	//delay.setCellValueFactory(cellData -> cellData.getValue().delayProperty());
        	delayOrderTable.setItems(FXCollections.observableList(delayOrders));
        	        	
        	//Finished in Time Text
        	lbl_statistic2.setText(fintime + "/" + finsum +" ");
        	//Finished on Delay Text
        	lbl_statistic4.setText(findelay + "/" + finsum);
        	        	
        	//Delay Text
        	if (daysBewteen >= 0)
        		lbl_delay2.setText(daysBewteen + " Days");
        	else
        		lbl_delay2.setText("0 Days");
        }
    } 	
}
