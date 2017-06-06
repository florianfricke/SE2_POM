package gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import types.Address;
import types.Customer;

public class CustomerController {
	private MainMenu mainMenu;
	private Customer cust;
	@FXML private TableView<Address> addressTable;
	@FXML private TableColumn<Address, String> street;
    @FXML private TableColumn<Address, String> houseNo;
    @FXML private TableColumn<Address, String> zipCode;
    @FXML private TableColumn<Address, String> city;
    @FXML private TableColumn<Address, String> country;
    @FXML private TableColumn<Address, Boolean> billingAddress;
	@FXML private TextField txt_Id;
	@FXML private TextField txt_Name;
	@FXML private TextField txt_Ranking;
	@FXML private TextArea tar_Comment;
	
	public void init(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.cust = new Customer();
        setTextFields();
   }
	
	public void init(MainMenu mainMenu, Customer cust) {
        this.mainMenu = mainMenu;
        this.cust = cust;

        setTextFields();
   }
	
	private void setTextFields(){
        //this.cust.getAddressList().add(new Address(1, "Bergblick", "11", "01896", "Pulsnitz", "Deutschland", false));
        //this.cust.getAddressList().add(new Address(2, "Steinaer Str.", "2a", "01896", "Pulsnitz", "Deutschland", true));
        
        
        
		Bindings.bindBidirectional(txt_Id.textProperty(), this.cust.idProperty());
		Bindings.bindBidirectional(txt_Name.textProperty(),this.cust.nameProperty());
		Bindings.bindBidirectional(txt_Ranking.textProperty(),this.cust.rankingProperty());
		Bindings.bindBidirectional(tar_Comment.textProperty(),this.cust.commentProperty());
		
		street.setCellValueFactory(cellData -> cellData.getValue().streetProperty());
		houseNo.setCellValueFactory(cellData -> cellData.getValue().houseNoProperty());
		zipCode.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
		city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
		country.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
		billingAddress.setCellValueFactory(cellData -> cellData.getValue().billingAddressProperty());
		try{
			addressTable.setItems(mainMenu.getAddressList(this.cust.idProperty().get()));	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML private void handleNewAddress(ActionEvent event) {
    	System.out.println("New Address");
    	//Code Einf�gen
  
    }
	
	@FXML private void handleNewContact(ActionEvent event) {
    	System.out.println("New Contact");
    	//Code Einf�gen
  
    }
	
	@FXML private void handleNewBank(ActionEvent event) {
    	System.out.println("New Bank");
    	//Code Einf�gen
  
    }
	
	@FXML private void handleDelAddress(ActionEvent event) {
    	System.out.println("Del Address");
    	//Code Einf�gen
  
    }
	
	@FXML private void handleDelContact(ActionEvent event) {
    	System.out.println("Del Contact");
    	//Code Einf�gen
  
    }
	
	@FXML private void handleDelBank(ActionEvent event) {
    	System.out.println("Del Bank");
    	//Code Einf�gen
  
    }
	
	
	
	
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    	mainMenu.addCustomer(this.cust);
    	closeWindow(event);
    }
	
	@FXML private void handleCancel(ActionEvent event) {
    	System.out.println("Cancel");
    	closeWindow(event);
    }
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
}
