package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML private TextField txt_priority;
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
		Bindings.bindBidirectional(txt_priority.textProperty(),this.order.priorityProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_address.textProperty(),this.order.addressidProperty());
		Bindings.bindBidirectional(txt_contact.textProperty(),this.order.contactidProperty());
		Bindings.bindBidirectional(txt_customer.textProperty(),this.order.customeridProperty());
		Bindings.bindBidirectional(txt_orderDate.textProperty(),this.order.orderDateProperty());
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_deliveryDate.textProperty(),this.order.actualDeliveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
		
	}
	
	
	
	
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    	mainMenu.addOrder(this.order);
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
