package gui;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import types.*;

public class CustomerController {
	
	private MainMenu mainMenu;
	private Customer cust;
	private boolean [] emptyFields;
	private Customer tmpCustomer;
	private Stage currentStage;
	//TextFields
	@FXML private TextField txt_Id;
	@FXML private TextField txt_Name;
	@FXML private ComboBox<String> cbxRanking;
	@FXML private TextArea tar_Comment;
	//Address Table
	@FXML private TableView<Address> addressTable;
	@FXML private TableColumn<Address, String> street;
    @FXML private TableColumn<Address, String> houseNo;
    @FXML private TableColumn<Address, String> zipCode;
    @FXML private TableColumn<Address, String> city;
    @FXML private TableColumn<Address, String> country;
    @FXML private TableColumn<Address, Boolean> billingAddress;
    //Contact Table
	@FXML private TableView<Contact> contactTable;
	@FXML private TableColumn<Contact, String> salutation;
	@FXML private TableColumn<Contact, String> name;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String> position;
    @FXML private TableColumn<Contact, String> phoneNo;
    @FXML private TableColumn<Contact, String> email;
    @FXML private TableColumn<Contact, Boolean> defaultContact;
    //Bank Account Table
	@FXML private TableView<BankAccount> bankAccountTable;
	@FXML private TableColumn<BankAccount, String> iban;
    @FXML private TableColumn<BankAccount, String> bic;
    @FXML private TableColumn<BankAccount, String> bankname;
    //Error Label
    @FXML private Label txt_errorMessage;
    @FXML private Button btn_ShowCurrentOrders;
    @FXML private Button btn_ShowOrderHistory;
    private String errorText="Some of your input values are not valid or empty. Please try again.";
    private boolean b_streetProperty, b_houseNoProperty, b_zipCodeProperty,	b_cityProperty,	b_countryProperty;
    private boolean b_nameProperty, b_firstNameProperty, b_positionProperty, b_phoneNoProperty, b_emailProperty;
    private boolean b_ibanProperty, b_bicProperty, b_bankNameProperty;

    
	/**
	 * Creates a new instance of Customer
	 * 
	 * @version 1.0
	 * @param mainMenu, Stage 
	 * @return Nothing
	 */
	public void init(MainMenu mainMenu, Stage stage) {
        this.mainMenu = mainMenu;
        this.cust = new Customer();
        this.currentStage = stage;
        setTextFields();
        btn_ShowCurrentOrders.setDisable(true);
        btn_ShowOrderHistory.setDisable(true);
   }
	
	
	/**
	 * Load an existing Customer
	 * 
	 * @version 1.0
	 * @param MainMenu, Customer, Stage 
	 */
	public void init(MainMenu mainMenu, Customer cust, Stage stage) {
        this.mainMenu = mainMenu;
        this.cust = cust;
        this.currentStage = stage;
        setTextFields();
   }
	
	
	/**
	 * Create bindings for TextFields, ComboBox, Address Table, Contact Table, Bank Table for the customer and set values.
	 * 
	 * @version 1.0
	 */	
	private void setTextFields(){
		tmpCustomer = new Customer(cust);
		
		ObservableList<String> listRanking = FXCollections.observableArrayList("A","B","C");
		cbxRanking.setItems(listRanking);
		
		createEventHandler();
		
		Bindings.bindBidirectional(txt_Id.textProperty(), this.cust.idProperty());
		Bindings.bindBidirectional(txt_Name.textProperty(),this.cust.nameProperty());
		Bindings.bindBidirectional(cbxRanking.valueProperty(),this.cust.rankingProperty());
		Bindings.bindBidirectional(tar_Comment.textProperty(),this.cust.commentProperty());

		//Address Table
		street.setCellValueFactory(cellData -> cellData.getValue().streetProperty());
		street.setCellFactory(column -> EditingCell.createStringEditCell());
		houseNo.setCellValueFactory(cellData -> cellData.getValue().houseNoProperty());
		houseNo.setCellFactory(column -> EditingCell.createStringEditCell());
		zipCode.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
		zipCode.setCellFactory(column -> EditingCell.createStringEditCell());
		city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
		city.setCellFactory(column -> EditingCell.createStringEditCell());
		country.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
		country.setCellFactory(column -> EditingCell.createStringEditCell());
		billingAddress.setCellValueFactory(cellData -> cellData.getValue().billingAddressProperty());
		//billingAddress.setCellFactory(CheckBoxTableCell.forTableColumn(billingAddress));
		billingAddress.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
		    @Override
		    public ObservableValue<Boolean> call(Integer param) {
		    	String addrId = billingAddress.getTableView().getItems().get(param).idProperty().get();
		    	boolean isChecked = billingAddress.getCellObservableValue(param).getValue();
		    	for (Address addr : billingAddress.getTableView().getItems()) {
					if(isChecked && !addr.idProperty().get().equals(addrId)){
						addr.billingAddressProperty().set(false);
					}
				}
		        return billingAddress.getCellObservableValue(param);
		    }
		}));
		if(this.cust.getAddressList().isEmpty()){
			this.cust.setAddressList(mainMenu.getAddressList(this.cust.idProperty().get()));
		}
	
		try{
			addressTable.setItems(this.cust.getAddressList());
		}catch(Exception e){
			ErrorLog.write(e);
		}
		//Contact Table
		salutation.setCellValueFactory(cellData -> cellData.getValue().salutationProperty());
		salutation.setCellFactory(column -> EditingCell.createStringEditCell());
		name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		name.setCellFactory(column -> EditingCell.createStringEditCell());
		firstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		firstName.setCellFactory(column -> EditingCell.createStringEditCell());
		position.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
		position.setCellFactory(column -> EditingCell.createStringEditCell());
		phoneNo.setCellValueFactory(cellData -> cellData.getValue().phoneNoProperty());
		phoneNo.setCellFactory(column -> EditingCell.createStringEditCell());
		email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
		email.setCellFactory(column -> EditingCell.createStringEditCell());
		defaultContact.setCellValueFactory(cellData -> cellData.getValue().defaultContactProperty());
		//defaultContact.setCellFactory(CheckBoxTableCell.forTableColumn(defaultContact));
		defaultContact.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
		    @Override
		    public ObservableValue<Boolean> call(Integer param) {
		    	String addrId = defaultContact.getTableView().getItems().get(param).idProperty().get();
		    	boolean isChecked = defaultContact.getCellObservableValue(param).getValue();
		    	for (Contact contact : defaultContact.getTableView().getItems()) {
					if(isChecked && !contact.idProperty().get().equals(addrId)){
						contact.defaultContactProperty().set(false);
					}
				}
		        return defaultContact.getCellObservableValue(param);
		    }
		}));
		if(this.cust.getContactList().isEmpty()){
			this.cust.setContactList(mainMenu.getContactList(this.cust.idProperty().get()));
		}
	
		try{
			contactTable.setItems(this.cust.getContactList());
		}catch(Exception e){
			ErrorLog.write(e);
		}
		
		//BankAccount Table
		iban.setCellValueFactory(cellData -> cellData.getValue().ibanProperty());
		iban.setCellFactory(column -> EditingCell.createStringEditCell());
		bic.setCellValueFactory(cellData -> cellData.getValue().bicProperty());
		bic.setCellFactory(column -> EditingCell.createStringEditCell());
		bankname.setCellValueFactory(cellData -> cellData.getValue().bankNameProperty());
		//bankname.setCellFactory(column -> EditingCell.createStringEditCell());
		bankname.setCellFactory(column -> EditingCell.createStringEditCell());
		if(this.cust.getBankAccountList().isEmpty()){
			this.cust.setBankAccountList(mainMenu.getBankAccountList(this.cust.idProperty().get()));
		}
	
		try{
			bankAccountTable.setItems(this.cust.getBankAccountList());
		}catch(Exception e){
			ErrorLog.write(e);
		}
	}
	
	/**
	 * Check filled fields for TextFields, ComboBox, Address Table, Contact Table, Bank Table for the customer.
	 * 
	 * @version 1.0
	 * @param String - which check
	 * @return false - if not all fields are filled, true - if all fields are filled
	 */	
	private boolean fillFields(String action){
				
		for (Address address : this.cust.getAddressList()) {
			b_streetProperty = address.streetProperty().get().isEmpty();
			b_houseNoProperty = address.houseNoProperty().get().isEmpty();
			b_zipCodeProperty = address.houseNoProperty().get().isEmpty();
			b_cityProperty = address.cityProperty().get().isEmpty();
			b_countryProperty = address.countryProperty().get().isEmpty();
		}
	
		for (Contact contacts : this.cust.getContactList()) {
			b_nameProperty = contacts.nameProperty().get().isEmpty();
			b_firstNameProperty = contacts.firstNameProperty().get().isEmpty();
			b_positionProperty = contacts.positionProperty().get().isEmpty();
			b_phoneNoProperty = contacts.phoneNoProperty().get().isEmpty();
			b_emailProperty = contacts.emailProperty().get().isEmpty();
		}


		for (BankAccount bankAccount : this.cust.getBankAccountList()) {
			b_ibanProperty =  bankAccount.ibanProperty().get().isEmpty();
			b_bicProperty = bankAccount.bicProperty().get().isEmpty();
			b_bankNameProperty = bankAccount.bankNameProperty().get().isEmpty();
		}
		
		boolean[] emptyFields = {txt_Name.getText().isEmpty(),
				cbxRanking.getValue() == "",
			    b_streetProperty, b_houseNoProperty, b_zipCodeProperty,	b_cityProperty,	b_countryProperty,
	    		b_nameProperty, b_firstNameProperty, b_positionProperty, b_phoneNoProperty, b_emailProperty,
	    		b_ibanProperty, b_bicProperty, b_bankNameProperty
				};
		
		
		this.emptyFields = emptyFields;
		action = "save";
		switch(action){
		case "save":
			if(emptyFields[0] || emptyFields[1] 
					|| emptyFields[2] || emptyFields[3] || emptyFields[4] || emptyFields[5] || emptyFields[6]
					|| emptyFields[7] || emptyFields[8] || emptyFields[9] || emptyFields[10] || emptyFields[11]
					|| emptyFields[12] || emptyFields[13] || emptyFields[14])
				return writeErrorMessage();
			else {return true;}
			
			default: return true;
		}
	}
	
	/**
	 * Create error message if no all fields are set right
	 * 
	 * @version 1.0
	 *
	 * @return false - if error is set, true - if no error is set
	 */
	public boolean writeErrorMessage(){
		String error= "";
		int z = 0;
		if(emptyFields[0] || emptyFields[1] || emptyFields[2] || emptyFields[3] || emptyFields[4] || emptyFields[5] || emptyFields[6]
				|| emptyFields[7] || emptyFields[8] || emptyFields[9] || emptyFields[10] || emptyFields[11]
				|| emptyFields[12] || emptyFields[13] || emptyFields[14]){
			for(int i=0; i<15; i++){
				boolean bool = emptyFields[i];
				if(bool){
					switch(i){
					case 0: error += "Name, ";	txt_Name.getStyleClass().add("label_error"); z++; break;
					case 1: error += "Ranking, "; 	cbxRanking.getStyleClass().add("label_error"); z++; break;
					case 2: error += "Street, "; z++; break;
					case 3: error += "House No, "; z++; break;
					case 4: error += "ZIP Code, "; z++; break;
					case 5: error += "City, "; z++; break;
					case 6: error += "Country, "; z++; break;
					case 7: error += "Billing Address, "; z++; break;
					case 8: error += "First Name, "; z++; break;
					case 9: error += "Position, "; z++; break;
					case 10: error += "Phone No., "; z++; break;
					case 11: error += "E-Mail, "; z++; break;
					case 12: error += "IBAN, "; z++; break;
					case 13: error += "BIC, "; z++; break;
					case 14: error += "Bank. "; z++; break;
					}
				}
			}
			if(z==1){
				error="Field: "+error + "must be set.";
				txt_errorMessage.setText(error);
			}else if(z > 1){
				error="Fields: "+error +"must be set.";
				txt_errorMessage.setText(error);
			}txt_errorMessage.setVisible(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Handle the event if "Show Current Order" button is clicked in customer card
	 * Open "Current Order" Card
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	
	@FXML private void handleShowCurrentOrder(ActionEvent event) {       
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
    
	/**
	 * Handle the event if "Show Order History" button is clicked in customer card
	 * Open "Order History" Card
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	    
	@FXML private void handleShowOrderHistory(ActionEvent event) {
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
	
	/**
	 * Handle the event if "New Address" button is clicked in customer card
	 * Add a new empty address line
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	    
	@FXML private void handleNewAddress(ActionEvent event) {
    	this.cust.getAddressList().add(new Address());
    }
	
	/**
	 * Handle the event if "New Contact" button is clicked in customer card
	 * Add a new empty contact line
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	  
	@FXML private void handleNewContact(ActionEvent event) {
    	this.cust.getContactList().add(new Contact());
    }
	
	/**
	 * Handle the event if "New Bank" button is clicked in customer card
	 * Add a new empty bank line
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleNewBank(ActionEvent event) {
    	this.cust.getBankAccountList().add(new BankAccount());
  
    }
	
	/**
	 * Handle the event if "Delete Address" button is clicked in customer card
	 * Try to delete selected address if address is not used
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleDelAddress(ActionEvent event) {
		
		if(mainMenu.isReferenced(addressTable.getSelectionModel().getSelectedItem()) == true){
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Cannot delete. Address is already used!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
		} else {
		
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getAddressList().remove(addressTable.getSelectionModel().getSelectedItem());
		}
		}
    }
	
	/**
	 * Handle the event if "Delete Contact" button is clicked in customer card
	 * Try to delete selected contact if contact is not used
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleDelContact(ActionEvent event) {
		
		if(mainMenu.isReferenced(contactTable.getSelectionModel().getSelectedItem()) == true){
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notification");
        	alert.setHeaderText("Cannot delete. Contact is already used!");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
        	alert.show();
		} else {
		
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getContactList().remove(contactTable.getSelectionModel().getSelectedItem());
		}
		}
    }
	
	
	/**
	 * Handle the event if "Delete Bank" button is clicked in customer card
	 * Delete selected bank
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleDelBank(ActionEvent event) {

		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getBankAccountList().remove(bankAccountTable.getSelectionModel().getSelectedItem());
		}
		
    }
	
	/**
	 * Handle the event if "Save" button is clicked in customer card
	 * Try to save customer object in POM DB
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleSave(ActionEvent event) {
		if(fillFields("save")==false){
			Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Warning");
	    	alert.setHeaderText("Please correctly fill all the necessary fields.");
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        	stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
	    	alert.show();
		}else{
    	System.out.println("Save");
    	mainMenu.saveCustomer(this.cust);
    	closeWindow(event);
		}
    }
	
	/**
	 * Handle the event if "Cancel" button is clicked in customer card
	 * Cancel customer object, does not save object DB
	 * 
	 * @version 1.0
	 * @param Clickevent
	 */	 
	@FXML private void handleCancel(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to cancel?") == true){ 
	    	this.cust.copy(tmpCustomer);
	    	closeWindow(event);
		}
    }
	

	private void closeWindow(ActionEvent e){
    	currentStage.close(); 
	}

	/**
	 * Set listeners to the Textfields, Combobox, Tables in Customer Card
	 * 
	 * @version 1.0
	 */
	private void createEventHandler(){
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	public void handle(WindowEvent we) {
	    		if(ConfirmBox.display("Confirmation Dialog", "Changes will be lost! Do you really want to close?") == false){
	    			we.consume();
	    		}else{
		    		cust.copy(tmpCustomer);
	    		}
	        }
	    });
		tar_Comment.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = tar_Comment.getText();
		if(!newValue){
			if(tar_Comment.getText().length() > 250){
				tar_Comment.getStyleClass().add("label_error");
				tar_Comment.setText("");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.setText(errorText);
				}
			else{
				tar_Comment.setText(localString);
				tar_Comment.getStyleClass().add("reset_label_error");
				txt_errorMessage.setVisible(false);
				txt_errorMessage.setText("");
				}
			}
		});

		txt_Name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		txt_errorMessage.setVisible(false);
		String localString;
		localString = txt_Name.getText();
		if(!newValue){
			if(txt_Name.getText().length() == 0){
				txt_Name.getStyleClass().add("label_error");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.setText("Field: Name must be filled.");
			}
			else if(txt_Name.getText().length() > 32 ) {
				txt_Name.getStyleClass().add("label_error");
				txt_Name.setText("");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.setText("Field: Name can only be 32 characters long.");
			}
			else if(txt_Name.getText().length() > 0 && txt_Name.getText().length() <= 32){ 
				txt_Name.setText(localString);
				txt_Name.getStyleClass().add("reset_label_error");
				txt_Name.setText(localString);
				txt_errorMessage.setVisible(false);
				txt_errorMessage.setText("");
				} 
			else {}
			}
		});
	}
	
}
