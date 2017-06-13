package gui; 



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.scene.Node; 
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import types.*; 



public class OrderController {
	private MainMenu mainMenu;
	private Order order;
	
    @FXML private TextField txt_Id;
    @FXML private TextField txt_product;
    @FXML private ComboBox<CbxItemObservable> cbxCustomer;
    @FXML private TextField txt_orderDate;
    @FXML private TextField txt_releaseDate;
    @FXML private TextField txt_state;
    @FXML private TextField txt_baseLotID;
    @FXML private TextField txt_volume;
    @FXML private TextField txt_dueDate;
    @FXML private TextField txt_price;
    @FXML private TextField txt_deliveryDate;
    @FXML private TextField txtLotSize;
    @FXML private ComboBox<CbxItemObservable> cbxContact;
    @FXML private ComboBox<CbxItemObservable> cbxAddress;
    @FXML private TextArea tar_comment;
    @FXML private ComboBox<String> cbxPriority;
    
    //Lot Table
    @FXML private TableView<Lot> lotTable;
	@FXML private TableColumn<Lot, String> id;
    @FXML private TableColumn<Lot, String> customerId;
    @FXML private TableColumn<Lot, Number> pieces;
    @FXML private TableColumn<Lot, String> state;
    @FXML private TableColumn<Lot, String> product;
    @FXML private TableColumn<Lot, Number> priority;
    @FXML private TableColumn<Lot, String> orderNo;
    @FXML private TableColumn<Lot, String> startDate;
    @FXML private TableColumn<Lot, String> dueDate;
    @FXML private DatePicker dpkOrderDate;
    @FXML private DatePicker dpkDueDate;

	@FXML private Button btnSave;
	
	public void init(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.order = new Order();
        setTextFields();
        
   }
	
	public void init(MainMenu mainMenu, Order order) {
        this.mainMenu = mainMenu;
        this.order = order;
        setTextFields();
   }
	
	private void setTextFields(){
		ObservableList<CbxItemObservable> custIdList = FXCollections.observableArrayList();
		ObservableList<CbxItemObservable> addrIdList = FXCollections.observableArrayList();
		ObservableList<CbxItemObservable> contactIdList = FXCollections.observableArrayList();
		for (Customer cust : mainMenu.getCustomerList()) {
			 custIdList.add(cust.comboBoxProperty());	 
		}
		cbxCustomer.setItems(custIdList);
		cbxCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CbxItemObservable>() {
           	@Override
			public void changed(ObservableValue<? extends CbxItemObservable> observable, CbxItemObservable oldValue,CbxItemObservable newValue) {
            	System.out.println("Changed Customer to"+ newValue.get().getId());
            	addrIdList.clear();
            	contactIdList.clear();
            	Customer cust;
            	if(newValue.get().getId() != ""){
            		cust = mainMenu.getCustomer(newValue.get().getId());
            	}else{
            		cust = new Customer();
            	}
            	for (Address addr : cust.getAddressList()) {
    				addrIdList.add(addr.comboBoxProperty());
    			}
            	for (Contact cont : cust.getContactList()) {
    				contactIdList.add(cont.comboBoxProperty());
    			}
            	cbxAddress.setItems(addrIdList);
				cbxContact.setItems(contactIdList);

				try{
				//Find Address with addressId of Order
				Predicate<Address> addrPredicate = a-> a.idProperty().get().equals(order.addressidProperty().get());
				Address  address = cust.getAddressList().stream().filter(addrPredicate).findFirst().get();
				cbxAddress.getSelectionModel().select(address.comboBoxProperty());
				}catch(Exception e){
					
				}
				try{
				//Find Contact with contactId of Order
				Predicate<Contact> contPredicate = c-> c.idProperty().get().equals(order.contactidProperty().get());
				Contact  contact = cust.getContactList().stream().filter(contPredicate).findFirst().get();
				cbxContact.getSelectionModel().select(contact.comboBoxProperty());
				}catch(Exception e){
					
				}
			}
        });
		if(order.ordernoProperty().get() != ""){
		cbxCustomer.getSelectionModel().select(mainMenu.getCustomer(order.customeridProperty().get()).comboBoxProperty());
		}else{
			cbxCustomer.getSelectionModel().select(new CbxItemObservable("", "Choose Customer"));
			cbxAddress.getSelectionModel().select(new CbxItemObservable("", ""));
			cbxContact.getSelectionModel().select(new CbxItemObservable("", ""));
		}
		

		
	
		dpkOrderDate.setValue(this.order.getOrderDate());
		dpkOrderDate.setEditable(false);
		dpkOrderDate.setConverter(converter);
		dpkDueDate.setValue(this.order.getDueDate());
		dpkDueDate.setEditable(false);
		dpkDueDate.setConverter(converter);
		
		

		
		
		
		
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		cbxPriority.setItems(listPriority);
		
		
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.ordernoProperty());
		Bindings.bindBidirectional(txt_product.textProperty(),this.order.productProperty());
		Bindings.bindBidirectional(cbxPriority.valueProperty(), this.order.priorityProperty(), new NumberStringConverter());
		cbxCustomer.accessibleTextProperty().bindBidirectional(this.order.customeridProperty());
	
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		//Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_deliveryDate.textProperty(),this.order.actualDeliveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
		Bindings.bindBidirectional(txtLotSize.textProperty(),this.order.lotSizeProperty(), new NumberStringConverter());
		
		//LotTable
		id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		customerId.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        pieces.setCellValueFactory(cellData -> cellData.getValue().piecesProperty());
        state.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        product.setCellValueFactory(cellData -> cellData.getValue().productProperty());
        priority.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        orderNo.setCellValueFactory(cellData -> cellData.getValue().orderNoProperty());
        startDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        dueDate.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
	}
	
	//Wohin mit dem Converter?
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter =
                  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };
	
	
	
	@FXML private void handleSave(ActionEvent event) {
		this.order.customeridProperty().set(cbxCustomer.getSelectionModel().getSelectedItem().get().idProperty().get());
    	this.order.addressidProperty().set(cbxAddress.getSelectionModel().getSelectedItem().get().idProperty().get());
    	this.order.contactidProperty().set(cbxContact.getSelectionModel().getSelectedItem().get().idProperty().get());
    	this.order.setOrderDate(dpkOrderDate.getValue());
    	this.order.setDueDate(dpkDueDate.getValue());
    	mainMenu.saveOrder(this.order);
    	closeWindow(event);
    }
	
	@FXML private void handleCancel(ActionEvent event) {
    	System.out.println(this.order.priorityProperty());
    	closeWindow(event);
    }
	
	@FXML private void handleRelease(ActionEvent event) {
    	System.out.println("Release Order");
    	if(order.stateProperty().get() == State.PLANNED.name()){
    		mainMenu.releaseOrder(order);
    	}else{
    		//TODO open Dialog 
    	}
    	closeWindow(event);
    }

	
	@FXML private void handleUpdate(ActionEvent event) {
    	System.out.println("Update MES Lots");
    	if(order.stateProperty().get() == State.IN_PROCESS.name()){
    		mainMenu.updateLots(order);
    	}else{
    		//TODO open Dialog 
    	}
    	closeWindow(event);
    }

	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}
