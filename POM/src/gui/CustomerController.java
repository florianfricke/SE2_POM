package gui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import types.*;

public class CustomerController {
	private MainMenu mainMenu;
	private Customer cust;
	private List<Address> delAddressList;
	private List<Contact> delContactList;
	private List<BankAccount> delBankAccountList;
	private boolean [] emptyFields;
	//TextFields
	@FXML private TextField txt_Id;
	@FXML private TextField txt_Name;
	@FXML private TextField txt_Ranking;
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
    
	public void init(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.cust = new Customer();
        setTextFields();
        btn_ShowCurrentOrders.setDisable(true);
        btn_ShowOrderHistory.setDisable(true);
   }
	
	public void init(MainMenu mainMenu, Customer cust) {
        this.mainMenu = mainMenu;
        this.cust = cust;

        setTextFields();
   }
	
	private void setTextFields(){
		Bindings.bindBidirectional(txt_Id.textProperty(), this.cust.idProperty());
		Bindings.bindBidirectional(txt_Name.textProperty(),this.cust.nameProperty());
		Bindings.bindBidirectional(txt_Ranking.textProperty(),this.cust.rankingProperty());
		Bindings.bindBidirectional(tar_Comment.textProperty(),this.cust.commentProperty());

/************************************************************************************/
		txt_Ranking.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = txt_Ranking.getText();
		if(!newValue){
			/*Only allows one letter(ABCabc) and spaces (before and after the letter)*/
			if(txt_Ranking.getText().matches("[abcABC]{1}[ ]$") || txt_Ranking.getText().matches("[ ]{1}[abcABC]$") || txt_Ranking.getText().matches("[abcABC]{1}")){
				txt_Ranking.setText(localString);
				txt_Ranking.setStyle("text-field");
				txt_errorMessage.setText("");
			}else {
				txt_Ranking.setText("");
				txt_Ranking.setStyle("-fx-border-color: #ff0707; -fx-border-radius: 5;");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.getStyleClass().add("label_error");
				txt_errorMessage.setText("Some of your input values are not valid or empty. Please try again.");
				}
			}
		});
/***************************************************************************************/
		tar_Comment.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = tar_Comment.getText();
		if(!newValue){
			if(tar_Comment.getText().length() > 250){
				tar_Comment.setStyle("-fx-border-color: #ff0707; -fx-border-radius: 5;");
				tar_Comment.setText("");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.getStyleClass().add("label_error");
				txt_errorMessage.setText("Some of your input values are not valid or empty. Please try again.");
				}
			else{
				tar_Comment.setText(localString);
				tar_Comment.setStyle("text-field");
				txt_errorMessage.setText("");
				}
			}
		});
/***************************************************************************************/
		txt_Name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = txt_Name.getText();
		if(!newValue){
			if(txt_Name.getText().length() == 0){
				txt_Name.setStyle("-fx-border-color: #ff0707; -fx-border-radius: 5;");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.getStyleClass().add("label_error");
				txt_errorMessage.setText("Some of your input values are not valid or empty. Please try again.");
			}
			else if(txt_Name.getText().length() > 32 ) {
				txt_Name.setStyle("-fx-border-color: #ff0707; -fx-border-radius: 5;");
				txt_Name.setText("");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.getStyleClass().add("label_error");
				txt_errorMessage.setText("Some of your input values are not valid or empty. Please try again.");
			}
			else{ 
				txt_Name.setText(localString);
				txt_Name.setStyle("text-field");
				txt_Name.setText(localString);
				txt_errorMessage.setText("");
				} 
			}
		});
/***************************************************************************************/
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	private boolean fillFields(String action){
		boolean[] emptyFields = {txt_Name.getText().isEmpty(),
								txt_Ranking.getText().isEmpty(),
								};
		this.emptyFields = emptyFields;
		action = "save";
		switch(action){
		case "save":
			if(emptyFields[0] && emptyFields[1])
				return writeErrorMessage();
			else if(emptyFields[0])
				return writeErrorMessage();
			else if(emptyFields[1])
				return writeErrorMessage();
			break;
			default: return true;
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
    	System.out.println("New Address");
    	this.cust.getAddressList().add(new Address());
    }
	
	@FXML private void handleNewContact(ActionEvent event) {
    	System.out.println("New Contact");
    	this.cust.getContactList().add(new Contact());
    }
	
	@FXML private void handleNewBank(ActionEvent event) {
    	System.out.println("New Bank");
    	this.cust.getBankAccountList().add(new BankAccount());
  
    }
	
	@FXML private void handleDelAddress(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
			System.out.println("Del Address");
			Address tmpAddress = addressTable.getSelectionModel().getSelectedItem();
	    	this.delAddressList = new ArrayList<Address>();
	    	this.delAddressList.add(tmpAddress);
	    	this.cust.getAddressList().remove(tmpAddress);
		}
    }
	
	@FXML private void handleDelContact(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	System.out.println("Del Contact");
	    	Contact tmpContact = contactTable.getSelectionModel().getSelectedItem();
	    	this.delContactList = new ArrayList<Contact>();
	    	this.delContactList.add(tmpContact);
	    	this.cust.getContactList().remove(tmpContact);
		}
    }
	
	@FXML private void handleDelBank(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to delete?") == true){
	    	System.out.println("Del Bank");
	    	BankAccount tmpBankAccount = bankAccountTable.getSelectionModel().getSelectedItem();
	    	this.delBankAccountList = new ArrayList<BankAccount>();
	    	this.delBankAccountList.add(tmpBankAccount);
	    	this.cust.getBankAccountList().remove(tmpBankAccount);
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
			System.out.println("Cancel");
	    	
	    	if(this.delAddressList != null){
		    	for (Address address : delAddressList) {
		    		this.cust.getAddressList().add(address);
				}
	    	}
	    	if(this.delContactList != null){
		    	for (Contact contact : delContactList) {
		    		this.cust.getContactList().add(contact);
				}
	    	}
	    	
	    	if(this.delBankAccountList != null){
		    	for (BankAccount bankAccount : delBankAccountList) {
		    		this.cust.getBankAccountList().add(bankAccount);
				}
	    	}
	    	closeWindow(event);
		}
    }
public boolean writeErrorMessage(){
		String error= "";
		int z = 0;
		if(emptyFields[0] || emptyFields[1]){
			for(int i=0; i<2; i++){
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
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}
