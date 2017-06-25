package gui;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
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
	@FXML private TableColumn<Contact, String> name;
    @FXML private TableColumn<Contact, String> firstName;
    @FXML private TableColumn<Contact, String> position;
    @FXML private TableColumn<Contact, String> phoneNo;
    @FXML private TableColumn<Contact, String> email;
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
    
	public void init(MainMenu mainMenu, Stage stage) {
        this.mainMenu = mainMenu;
        this.cust = new Customer();
        this.currentStage = stage;
        setTextFields();
        btn_ShowCurrentOrders.setDisable(true);
        btn_ShowOrderHistory.setDisable(true);
   }
	
	public void init(MainMenu mainMenu, Customer cust, Stage stage) {
        this.mainMenu = mainMenu;
        this.cust = cust;
        this.currentStage = stage;
        setTextFields();
   }
	
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
		billingAddress.setCellFactory(CheckBoxTableCell.forTableColumn(billingAddress));
		if(this.cust.getAddressList().isEmpty()){
			this.cust.setAddressList(mainMenu.getAddressList(this.cust.idProperty().get()));
		}
	
		try{
			addressTable.setItems(this.cust.getAddressList());
		}catch(Exception e){
			ErrorLog.write(e);
		}
		//Contact Table
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
	
	
	
	
	
	private boolean fillFields(String action){
		boolean[] emptyFields = {txt_Name.getText().isEmpty(),
								cbxRanking.getValue() == "",
								};
		
		
		for (Address address : this.cust.getAddressList()) {
			if (address.streetProperty().get().isEmpty() == true || address.houseNoProperty().get().isEmpty() == true || 
					address.zipCodeProperty().get().isEmpty() == true || address.cityProperty().get().isEmpty() == true 
					|| address.countryProperty().get().isEmpty() == true){
				
						return false;
			}
		}
		
		for (Contact contacts : this.cust.getContactList()) {
			if(contacts.nameProperty().get().isEmpty() == true || contacts.firstNameProperty().get().isEmpty() == true || 
					contacts.positionProperty().get().isEmpty() == true || contacts.phoneNoProperty().get().isEmpty() == true ||
					contacts.emailProperty().get().isEmpty() == true){
				
						return false;
			}	
		}
		
		for (BankAccount bankAccount : this.cust.getBankAccountList()) {
			if (bankAccount.ibanProperty().get().isEmpty() == true || bankAccount.bicProperty().get().isEmpty() == true || 
					bankAccount.bankNameProperty().get().isEmpty() == true){
				return false;
			}
			
		}
		
		
		
		this.emptyFields = emptyFields;
		action = "save";
		switch(action){
		case "save":
			if(emptyFields[0] && emptyFields[1])
				return writeErrorMessage();
			else if(emptyFields[0] && !(emptyFields[1]))
				return writeErrorMessage();
			else if(emptyFields[1] && !(emptyFields[0]))
				return writeErrorMessage();
			else if(!(emptyFields[0] && emptyFields[1])){ return true;}
			else {return true;}
			
			default: return true;
		}
	}
	
	public boolean writeErrorMessage(){
		String error= "";
		int z = 0;
		if(emptyFields[0] || emptyFields[1]){
			for(int i=0; i<1; i++){
				boolean bool = emptyFields[i];
				if(bool){
					switch(i){
					case 0: error += "Name, "; z++; break;
					case 1: error += "Ranking "; z++; break;
					}
				}
			}
			if(z==1){
				error="Field: "+error + "must be set.";
				txt_errorMessage.setText(error);
			}else if(z == 2){
				error="Fields: "+error +"must be set.";
				txt_errorMessage.setText(error);
			}txt_errorMessage.setVisible(true);
			return false;
		}
		return true;
	}
	
	@FXML private void handleShowCurrentOrder(ActionEvent event) {       
    	System.out.println("CurrentOrder");
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
    
    
	@FXML private void handleShowOrderHistory(ActionEvent event) {
        System.out.println("OrderHistory");
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
	
	@FXML private void handleNewAddress(ActionEvent event) {
    	this.cust.getAddressList().add(new Address());
    }
	
	@FXML private void handleNewContact(ActionEvent event) {
    	this.cust.getContactList().add(new Contact());
    }
	
	@FXML private void handleNewBank(ActionEvent event) {
    	this.cust.getBankAccountList().add(new BankAccount());
  
    }
	
	@FXML private void handleDelAddress(ActionEvent event) {
		
		if(mainMenu.isReferenced(addressTable.getSelectionModel().getSelectedItem()) == true){
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("Cannot delete. Address is already used!");
        	alert.show();
		} else {
		
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getAddressList().remove(addressTable.getSelectionModel().getSelectedItem());
		}
		}
    }
	
	@FXML private void handleDelContact(ActionEvent event) {
		
		if(mainMenu.isReferenced(contactTable.getSelectionModel().getSelectedItem()) == true){
			Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("Cannot delete. Contact is already used!");
        	alert.show();
		} else {
		
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getContactList().remove(contactTable.getSelectionModel().getSelectedItem());
		}
		}
    }
	
	@FXML private void handleDelBank(ActionEvent event) {

		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	this.cust.getBankAccountList().remove(bankAccountTable.getSelectionModel().getSelectedItem());
		}
		
    }
		
	@FXML private void handleSave(ActionEvent event) {
		if(fillFields("save")==false){
			Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Warning");
	    	alert.setHeaderText("Please correctly fill all the necessary fields.");
	    	alert.show();
		}else{
    	System.out.println("Save");
    	mainMenu.saveCustomer(this.cust);
    	closeWindow(event);
		}
    }
	
	@FXML private void handleCancel(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to cancel?") == true){ 
	    	this.cust.copy(tmpCustomer);
	    	closeWindow(event);
		}
    }
	
	private void closeWindow(ActionEvent e){
    	currentStage.close(); 
	}

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
