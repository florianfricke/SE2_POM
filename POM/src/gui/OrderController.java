package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import types.Customer;
import types.Order;

public class OrderController {
	private MainMenu mainMenu;
	private Order order;
	
    @FXML private TextField txt_Id;
    @FXML private TextField txt_product;
    @FXML private TextField txt_priority;
    @FXML private TextField txt_customer;
    @FXML private TextField txt_orderDate;
    @FXML private TextField txt_releaseDate;
    @FXML private TextField txt_state;
    @FXML private TextField txt_baseLotID;
    @FXML private TextField txt_volume;
    @FXML private TextField txt_dueDate;
    @FXML private TextField txt_price;
    @FXML private TextField txt_delveryDate;
    @FXML private TextArea tar_comment;


	@FXML private TextArea tar_Comment;
	@FXML private Button btnSave;
	private Customer cust;
/*	
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
		Bindings.bindBidirectional(txt_Id.textProperty(), this.cust.idProperty());
		Bindings.bindBidirectional(txt_Name.textProperty(),this.cust.nameProperty());
		Bindings.bindBidirectional(txt_Ranking.textProperty(),this.cust.rankingProperty());
		Bindings.bindBidirectional(tar_Comment.textProperty(),this.cust.commentProperty());
	}
	
	@FXML private void handleNewAddress(ActionEvent event) {
    	System.out.println("New Address");
    	//Code Einfügen
  
    }
	
	@FXML private void handleNewContact(ActionEvent event) {
    	System.out.println("New Contact");
    	//Code Einfügen
  
    }
	
	@FXML private void handleNewBank(ActionEvent event) {
    	System.out.println("New Bank");
    	//Code Einfügen
  
    }
	
	@FXML private void handleDelAddress(ActionEvent event) {
    	System.out.println("Del Address");
    	//Code Einfügen
  
    }
	
	@FXML private void handleDelContact(ActionEvent event) {
    	System.out.println("Del Contact");
    	//Code Einfügen
  
    }
	
	@FXML private void handleDelBank(ActionEvent event) {
    	System.out.println("Del Bank");
    	//Code Einfügen
  
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
	*/
}
