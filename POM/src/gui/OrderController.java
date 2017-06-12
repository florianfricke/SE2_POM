package gui; 
 

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.scene.Node; 
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
import javafx.util.converter.NumberStringConverter;
import pom_service.PomService;
import types.Customer;
import types.Order;
import types.SaveType; 

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class OrderController {
	private MainMenu mainMenu;
	private Order order;
	private PomService pomService;
	private ObservableList<Customer> custList;
	
    @FXML private TextField txt_Id;
    @FXML private TextField txt_product;
    @FXML private TextField txt_customer;
    @FXML private TextField txt_orderDate;
    @FXML private TextField txt_releaseDate;
    @FXML private TextField txt_state;
    @FXML private TextField txt_baseLotID;
    @FXML private TextField txt_volume;
    @FXML private TextField txt_dueDate;
    @FXML private TextField txt_price;
    @FXML private TextField txt_deliveryDate;
    @FXML private TextField txt_contact;
    @FXML private TextField txt_address;
    @FXML private TextArea tar_comment;
    @FXML private ComboBox<String> comboBox_priority;
    @FXML private ComboBox<Customer> combobox_customer;

	@FXML private Button btnSave;
	
	public void comboBoxPriority(){
		//Wo muss diese Methode hin?
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		comboBox_priority.setItems(listPriority);
	}
	

	public void comboBoxCustomer(){
			
		this.custList = FXCollections.observableList(this.pomService.getCustomerList());
		System.out.println(custList);
		combobox_customer.setItems(this.custList);
		combobox_customer.getSelectionModel().select(2);
	}
	
	
	public void init(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.order = new Order();
        this.pomService = new PomService(SaveType.postgres);
        comboBoxCustomer();
        comboBoxPriority();
        setTextFields();
        
   }
	
	public void init(MainMenu mainMenu, Order order) {
        this.mainMenu = mainMenu;
        this.order = order;
        comboBoxCustomer();
        comboBoxPriority();
        setTextFields();
   }
	
	private void setTextFields(){
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.ordernoProperty());
		Bindings.bindBidirectional(txt_product.textProperty(),this.order.productProperty());
		Bindings.bindBidirectional(comboBox_priority.valueProperty(), this.order.priorityProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(txt_address.textProperty(),this.order.addressidProperty());
		Bindings.bindBidirectional(txt_contact.textProperty(),this.order.contactidProperty());
		
		Bindings.bindBidirectional(txt_customer.textProperty(),this.order.customeridProperty());
		//Bindings.bindBidirectional(combobox_customer.getSelectionModel().getSelectedItem().idProperty(),this.order.customeridProperty());
		//combobox_customer.getSelectionModel().getSelectedItem().idProperty().bindBidirectional(this.order.customeridProperty());
		// Bindings.bindBidirectional(combobox_customer.getSelectionModel().selectedItemProperty().get().idProperty(),this.order.customeridProperty());
		//System.out.println(combobox_customer.getSelectionModel().getSelectedItem().rankingProperty());
		//System.out.println(combobox_customer.getSelectionModel().getSelectedItem().idProperty());
		
		Bindings.bindBidirectional(txt_orderDate.textProperty(),this.order.orderDateProperty());
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_deliveryDate.textProperty(),this.order.actualDeliveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
	
		//ComboBoxen
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		comboBox_priority.setItems(listPriority);
		
		
		
		
	}
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    	mainMenu.addOrder(this.order);
    	closeWindow(event);
    }
	
	@FXML private void handleCancel(ActionEvent event) {
    	System.out.println(this.order.priorityProperty());
    	closeWindow(event);
    }
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.scene.Node; 
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
import javafx.util.converter.NumberStringConverter; 
import types.Order; 



public class OrderController {
	private MainMenu mainMenu;
	private Order order;
	
    @FXML private TextField txt_Id;
    @FXML private TextField txt_product;
    @FXML private TextField txt_customer;
    @FXML private TextField txt_orderDate;
    @FXML private TextField txt_releaseDate;
    @FXML private TextField txt_state;
    @FXML private TextField txt_baseLotID;
    @FXML private TextField txt_volume;
    @FXML private TextField txt_dueDate;
    @FXML private TextField txt_price;
    @FXML private TextField txt_deliveryDate;
    @FXML private TextField txt_contact;
    @FXML private TextField txt_address;
    @FXML private TextArea tar_comment;
    @FXML private ComboBox<String> comboBox_priority;

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
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.ordernoProperty());
		Bindings.bindBidirectional(txt_product.textProperty(),this.order.productProperty());
		Bindings.bindBidirectional(comboBox_priority.valueProperty(), this.order.priorityProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(txt_address.textProperty(),this.order.addressidProperty());
		Bindings.bindBidirectional(txt_contact.textProperty(),this.order.contactidProperty());
		Bindings.bindBidirectional(txt_customer.textProperty(),this.order.customeridProperty());
		//Bindings.bindBidirectional(txt_orderDate.textProperty(),this.order.orderDateProperty());
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_deliveryDate.textProperty(),this.order.actualDeliveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
	
		//ComboBoxen
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		comboBox_priority.setItems(listPriority);
		
	}
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    	mainMenu.addOrder(this.order);
    	closeWindow(event);
    }
	
	@FXML private void handleCancel(ActionEvent event) {
    	System.out.println(this.order.priorityProperty());
    	closeWindow(event);
    }
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}

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
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
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
    @FXML private ComboBox<CbxItemObservable> cbxContact;
    @FXML private ComboBox<CbxItemObservable> cbxAddress;
    @FXML private TextArea tar_comment;
    @FXML private ComboBox<String> cbxPriority;

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
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		cbxPriority.setItems(listPriority);
		
		
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.ordernoProperty());
		Bindings.bindBidirectional(txt_product.textProperty(),this.order.productProperty());
		Bindings.bindBidirectional(cbxPriority.valueProperty(), this.order.priorityProperty(), new NumberStringConverter());
		//Bindings.bindBidirectional(cbxAddress.textProperty(),this.order.addressidProperty());
		//Bindings.bindBidirectional(cbxContact.textProperty(),this.order.contactidProperty());
		//Bindings.bindBidirectional(cbxCustomer.getSelectionModel().getSelectedItem().get().idProperty(),this.order.customeridProperty());
		cbxCustomer.accessibleTextProperty().bindBidirectional(this.order.customeridProperty());
		Bindings.bindBidirectional(txt_orderDate.textProperty(),this.order.orderDateProperty());
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_deliveryDate.textProperty(),this.order.actualDeliveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
		//ComboBoxen

		
	}
	
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    	System.out.println(cbxCustomer.getSelectionModel().getSelectedItem().get().idProperty());
    	this.order.customeridProperty().set(cbxCustomer.getSelectionModel().getSelectedItem().get().idProperty().get());
    	this.order.addressidProperty().set(cbxAddress.getSelectionModel().getSelectedItem().get().idProperty().get());
    	this.order.contactidProperty().set(cbxContact.getSelectionModel().getSelectedItem().get().idProperty().get());
    	mainMenu.addOrder(this.order);
    	closeWindow(event);
    }
	
	@FXML private void handleCancel(ActionEvent event) {
    	System.out.println(this.order.priorityProperty());
    	closeWindow(event);
    }

	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}
