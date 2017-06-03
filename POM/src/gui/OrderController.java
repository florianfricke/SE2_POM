package gui;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
	@FXML private Button btn_save;
	@FXML private Button btn_cancel;
	
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
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.iD());
		Bindings.bindBidirectional(txt_product.textProperty(),this.order.product());
		Bindings.bindBidirectional(txt_priority.textProperty(),this.order.priorityProperty());
		Bindings.bindBidirectional(txt_customer.textProperty(),this.order.customerProperty());
		Bindings.bindBidirectional(txt_orderDate.textProperty(),this.order.orderDateProperty());
		Bindings.bindBidirectional(txt_releaseDate.textProperty(),this.order.releaseDateProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIDProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty());
		Bindings.bindBidirectional(txt_dueDate.textProperty(),this.order.dueDateProperty());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty());
		Bindings.bindBidirectional(txt_delveryDate.textProperty(),this.order.delveryDateProperty());
		Bindings.bindBidirectional(tar_comment.textProperty(),this.order.commentProperty());
		
	}
	
	
	
	@FXML private void handleSave(ActionEvent event) {
    	System.out.println("Save");
    //	mainMenu.addCustomer(this.cust);
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
