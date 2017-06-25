package gui; 

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.Label;


import types.*; 

public class OrderController {
	private MainMenu mainMenu;
	private Order order;
	private boolean [] emptyFields;
	private Callback<DatePicker, DateCell> startDateCellFactory;
	private Callback<DatePicker, DateCell> dueDateCellFactory;
	private OrderLotChanges changeValues;
	private Order tmpOrder;
	private Stage currentStage;
	
    @FXML private TextField txt_Id;
    @FXML private TextField txt_orderDate;
    @FXML private TextField txt_releaseDate;
    @FXML private TextField txt_state;
    @FXML private TextField txt_baseLotID;
    @FXML private TextField txt_volume;
    @FXML private TextField txt_dueDate;
    @FXML private TextField txt_price;
    @FXML private TextField txt_deliveryDate;
    @FXML private TextField txtLotSize;
    @FXML private TextArea tar_comment;
    @FXML private ComboBox<String> cbxProduct;
    @FXML private ComboBox<CbxItemObservable> cbxCustomer;
    @FXML private ComboBox<CbxItemObservable> cbxContact;
    @FXML private ComboBox<CbxItemObservable> cbxAddress;
    @FXML private ComboBox<String> cbxPriority;
    @FXML private Label txt_errorMessage;
    @FXML private Button btnRelease;
    @FXML private Button btnUpdate;
    @FXML private Button btnTree;
    
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
    @FXML private DatePicker dpkDeliveryDate;
    @FXML private DatePicker dpkReleaseDate;
    @FXML private DatePicker dpkOrderDate;
    @FXML private DatePicker dpkDueDate;
    @FXML private DatePicker dpkStartDate;
    private String errorText="Some of your input values are not valid or empty. Please try again.";
    
	//String Converter for DatePicker
    private StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
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
	
	public void init(MainMenu mainMenu, Stage stage) {
        this.mainMenu = mainMenu;
        this.order = new Order();
        this.currentStage = stage;
        order.lotSizeProperty().set(mainMenu.getSetup().defaultLotSizeProperty().get());
        setTextFields();
        btnUpdate.setDisable(true);
        btnTree.setDisable(true);
        
   }
	
	public void init(MainMenu mainMenu, Order order, Stage stage) {
        this.mainMenu = mainMenu;
        this.order = order;
        this.currentStage = stage;
        changeValues = new OrderLotChanges(order.getOrderLotChanges());
        disableFields();
        setTextFields();
   }
	private void disableFields(){
        if(order.stateProperty().get().equals(State.PLANNED.name())){
        	btnTree.setDisable(true);
        }else if(order.stateProperty().get().equals(State.IN_PROCESS.name()) || order.getCompletionDate() != null){
        	cbxProduct.setDisable(true);
        	cbxCustomer.setDisable(true);
        	dpkOrderDate.setEditable(false);
        	txt_baseLotID.setEditable(false);
        }
	}
	
	private void setTextFields(){
		tmpOrder = new Order(order);
		//ComboBoxen
		cbxProduct.setItems(mainMenu.getProductList());
		
		ObservableList<CbxItemObservable> custIdList = FXCollections.observableArrayList();
		ObservableList<CbxItemObservable> addrIdList = FXCollections.observableArrayList();
		ObservableList<CbxItemObservable> contactIdList = FXCollections.observableArrayList();
		
		ObservableList<String> listPriority = FXCollections.observableArrayList("1","2","3","5","6","7","8","9","10");
		cbxPriority.setItems(listPriority);
		
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
			cbxAddress.getSelectionModel().select(new CbxItemObservable("", "Choose Address"));
			cbxContact.getSelectionModel().select(new CbxItemObservable("", "Choose Contact"));
		}
	
		//DatePicker
		createDateCells();
		dpkDeliveryDate.setValue(this.order.getActualDeliveryDate());
		dpkDeliveryDate.setConverter(converter);
		dpkDeliveryDate.setDisable(true);
		dpkDeliveryDate.setStyle("-fx-opacity: 1");
		dpkDeliveryDate.getEditor().setStyle("-fx-opacity: 1");		
		
		dpkReleaseDate.setValue(this.order.getReleaseDate());
		dpkReleaseDate.setDisable(true);
		dpkReleaseDate.setConverter(converter);
		dpkReleaseDate.setStyle("-fx-opacity: 1");
		dpkReleaseDate.getEditor().setStyle("-fx-opacity: 1");
		
		dpkOrderDate.setValue(this.order.getOrderDate());
		dpkOrderDate.setConverter(converter);
		dpkOrderDate.setDisable(true);
		dpkOrderDate.setStyle("-fx-opacity: 1");
		dpkOrderDate.getEditor().setStyle("-fx-opacity: 1");
		
		dpkDueDate.setValue(this.order.getDueDate());
		dpkDueDate.setDayCellFactory(dueDateCellFactory);
		dpkDueDate.setConverter(converter);
		
		dpkStartDate.setValue(this.order.getStartDate());
		dpkStartDate.setDayCellFactory(startDateCellFactory);
		dpkStartDate.setConverter(converter);
		
		
		//eventHandler
		createEventHandler();
	
		//Bindings
		Bindings.bindBidirectional(txt_Id.textProperty(), this.order.ordernoProperty());
		Bindings.bindBidirectional(cbxPriority.valueProperty(), this.order.priorityProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(cbxProduct.valueProperty(), this.order.productProperty());
		Bindings.bindBidirectional(txt_state.textProperty(),this.order.stateProperty());
		Bindings.bindBidirectional(txt_baseLotID.textProperty(),this.order.baseLotIdProperty());
		Bindings.bindBidirectional(txt_volume.textProperty(),this.order.volumeProperty(),new NumberStringConverter());
		Bindings.bindBidirectional(txt_price.textProperty(),this.order.priceProperty(),new NumberStringConverter());
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
        lotTable.setItems(mainMenu.getLotList(order.ordernoProperty().get()));
	}
	private boolean checkFieldsFilled(String action){
		setDateFields();
		if (action.equals("save")){
			boolean [] emptyFields	= { cbxProduct.getValue() == "", 
					cbxPriority.getValue().equals("0"),
					cbxCustomer.getValue().getValue().getId().isEmpty(),
					cbxAddress.getValue().getValue().getId().isEmpty(),
					cbxContact.getValue().getValue().getId().isEmpty(),
					false,
					false,
					false,
					false,
					false,
					false
					};
			this.emptyFields = emptyFields;
		}else{
			boolean [] emptyFields	= { cbxProduct.getValue() == "", 
					cbxPriority.getValue().equals("0"),
					cbxCustomer.getValue().getValue().getId().isEmpty(),
					cbxAddress.getValue().getValue().getId().isEmpty(),
					cbxContact.getValue().getValue().getId().isEmpty(),
					dpkStartDate.getValue() == null,
					txt_baseLotID.getText().isEmpty(),
					txt_volume.getText().equals("0"),
					dpkDueDate.getValue() == null,
					txt_price.getText().equals("0"),
					txtLotSize.getText().isEmpty()
					};
			this.emptyFields = emptyFields;
		}
		
		switch (action) {
		case "save":
			if(cbxCustomer.getValue().getValue().getId().isEmpty() || cbxAddress.getValue().getValue().getId().isEmpty() || cbxContact.getValue().getValue().getId().isEmpty())
				return createErrorMessage();
			break;
		case "release":return createErrorMessage();
		case "update":return createErrorMessage();
		default:
			return true;	
		}
	return true;
	}
	//Button Handler
	@FXML private void handleSave(ActionEvent e) {
		//Set ComboBoxes
		if(checkFieldsFilled("save")){
			this.order.customeridProperty().set(cbxCustomer.getSelectionModel().getSelectedItem().get().idProperty().get());
	    	this.order.addressidProperty().set(cbxAddress.getSelectionModel().getSelectedItem().get().idProperty().get());
	    	this.order.contactidProperty().set(cbxContact.getSelectionModel().getSelectedItem().get().idProperty().get());
	    	//Save Date Fields in Order
	    	setDateFields();
	    	this.order.stateProperty().set(txt_state.getText());
	    	setDateFields();
	    	mainMenu.saveOrder(this.order);
	    	String s = ((Button) e.getSource()).getText();
	    	if(s.equals("Save")){
	        	closeWindow(e);
	    	}
    	}
    }
	private void setDateFields(){
    	this.order.setOrderDate(dpkOrderDate.getValue());
    	this.order.setDueDate(dpkDueDate.getValue());
    	this.order.setStartDate(dpkStartDate.getValue());
    	this.order.setReleaseDate(dpkReleaseDate.getValue());
	}
	
	@FXML private void handleCancel(ActionEvent event) {
		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to cancel?") == true){
			this.order.copy(tmpOrder);
	    	closeWindow(event);
		}
    }
	
	@FXML private void handleRelease(ActionEvent event) {
    	System.out.println("Release Order");
    	
   
    		
    	
    	
    	if(order.stateProperty().get() == State.PLANNED.name()){
    		
    /*	 	if(!mainMenu.isDueDateViable(order) == false){
        		Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Notificaion");
            	alert.setHeaderText("One or more lots can't be started before due date. Please update due date.");
            	alert.show();
            	} else  */ {
    		
    		if(checkFieldsFilled("release")){ 
    			handleSave(event);
    			mainMenu.releaseOrder(order);
    			lotTable.setItems(mainMenu.getLotList(order.ordernoProperty().get()));
    			getDateFields();
    		}
    		}
    	}else{
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("State have to be PLANNED!");
        	alert.show();
    	
    	}
    }
	@FXML private void handleUpdate(ActionEvent event) {
    	System.out.println("Update MES Lots");
    	if(order.stateProperty().get() == State.IN_PROCESS.name()){
    		
    		/*if(!mainMenu.isDueDateViable(order) == false){
        		Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Notificaion");
            	alert.setHeaderText("One or more lots can't be started before due date. Please update due date.");
            	alert.show();
            	} else */ {
    		
    		
    		
    		if(checkFieldsFilled("update")){
        		if(mainMenu.updateLots(order)){
	        		handleSave(event);
	    			lotTable.setItems(mainMenu.getLotList(order.ordernoProperty().get()));
	    			getDateFields();
        		}
        		}
    		}
    	}else{
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("State have to be IN PROCESS!");
        	alert.show();
    	}
    }
	
	@FXML private void handleCancelOrder(ActionEvent event){
		
    	if(order.stateProperty().get().equals(State.IN_PROCESS.toString())){
    		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to cancel: order " +order.ordernoProperty().get().toString()) == true){
        		System.out.println("Cancel");
    		if (mainMenu.cancelOrder(order)){
    			// delete if true
    			
    			getDateFields();
    		}
    		else {
    			Alert alert = new Alert(AlertType.ERROR);
            	alert.setTitle("Notificaion");
            	alert.setHeaderText("Some lots are already IN PROCESS");
            	alert.show();
            	return;
    		}
    		}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("Order is not IN PROCESS");
        	alert.show();
        	return;
    	}
	}
	
	@FXML private void handleFinishOrder(ActionEvent event){
    	if(order.stateProperty().get().equals(State.COMPLETED.toString())){
    		if(ConfirmBox.display("Confirmation Dialog", "Do you really want to finish: order " +order.ordernoProperty().get().toString()) == true){
        		System.out.println("Finish Order");
    		if (mainMenu.finishOrder(order)){
    			// delete if true
    			getDateFields();
    		}
    		}
    	
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Notificaion");
        	alert.setHeaderText("Order is not COMPLETED");
        	alert.show();
        	return;
    	}
		
	}
	
	@FXML private void handleTree(ActionEvent event) {
		 System.out.println("Production Flow");
	        try {
	        	FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("ProductionTreeView.fxml"));
	            Parent root = fxmlLoader.load();
	            ProductionTreeController prodTreeCtrl = (ProductionTreeController)fxmlLoader.getController();
	            prodTreeCtrl.setMainApp(this.mainMenu, this.order.ordernoProperty().get(), order.productProperty().get());
	            Scene scene = new Scene(root, 800, 500);
	            Stage stage = new Stage();
	            stage.setTitle("Production Flow");
	            stage.setScene(scene);
	            stage.getIcons().add(new Image("file:src/gui/Cinderella_Icon.png"));
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.showAndWait();
	        } catch (IOException e) {
	            Logger logger = Logger.getLogger(getClass().getName());
	            logger.log(Level.SEVERE, "Failed to create new Window.", e);
	        	} 
    }
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	
	public boolean createErrorMessage(){
		String error = "";
		int c = 0;
		if(emptyFields[0] ||
				emptyFields[1] ||
				emptyFields[2] ||
				emptyFields[3] ||
				emptyFields[4] ||
				emptyFields[5] ||
				emptyFields[6] ||
				emptyFields[7] ||
				emptyFields[8] ||
				emptyFields[9] ||
				emptyFields[10]){ 

			for(int i = 0; i< 11;i++){
				boolean b = emptyFields[i];

				if(b){
					switch (i) {
					case 0:	error += "Product, ";c++; break;
					case 1:	error += "Priority, "; c++; break;
					case 2:	error += "Customer, "; c++; break;
					case 3:	error += "Address, "; c++; break;
					case 4:	error += "Contact, "; c++; break;
					case 5:	error += "StartDate, "; c++; break;
					case 6:	error += "Base Lot Id, "; c++; break;
					case 7:	error += "Volume, "; c++; break;
					case 8:	error += "Due Date, "; c++; break;
					case 9:	error += "Price, "; c++; break;
					case 10:error += "LotSize, "; c++; break;
					}
				}
			}
			if(c == 1){
				error = "Field " + error + "must be set.";
				txt_errorMessage.setText(error);
			}else if(c > 1){
				error = "Fields " + error + "must be set.";
				txt_errorMessage.setText(error);
				
			}
			txt_errorMessage.setVisible(true);
			return false;
		}
		return true;
	}
	
	private void getDateFields(){
		dpkDeliveryDate.setValue(this.order.getActualDeliveryDate());
		dpkReleaseDate.setValue(this.order.getReleaseDate());
		txt_state.setText(this.order.stateProperty().get());
	}
	private void createDateCells(){
		//New DateCell to disable DateValues before Order Date
		this.startDateCellFactory = 
	            new Callback<DatePicker, DateCell>() {
	                @Override
	                public DateCell call(final DatePicker datePicker) {
	                    return new DateCell() {
	                        @Override
	                        public void updateItem(LocalDate item, boolean empty) {
	                            super.updateItem(item, empty);
	                           try {
		                            if (item.isBefore(dpkOrderDate.getValue().plusDays(1))) {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }   
		               
		                            if (item.isAfter(dpkDueDate.getValue().minusDays(1))) {
		                                    setDisable(true);
		                                    setStyle("-fx-background-color: #ffc0cb;");
		                            }  
							} catch (Exception e) {
								// TODO: handle exception
							}
	                    }
	                };
	            }
	        };
	    //New DateCell to disable DateValues before Order Date
        this.dueDateCellFactory = 
	            new Callback<DatePicker, DateCell>() {
	                @Override
	                public DateCell call(final DatePicker datePicker) {
	                    return new DateCell() {
	                    	
	                        @Override
	                        public void updateItem(LocalDate item, boolean empty) {
	                            super.updateItem(item, empty);
	                            try {
	                            	if (item.isBefore(dpkOrderDate.getValue().plusDays(1)) || item.isBefore(dpkStartDate.getValue().plusDays(1))) {
	                                    setDisable(true);
	                                    setStyle("-fx-background-color: #ffc0cb;");
	                            	}  
								} catch (Exception e) {
									// TODO: handle exception
								}
	                    }
	                };
	            }
	        };
	}
	
	private void createEventHandler(){
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	public void handle(WindowEvent we) {
	    		if(ConfirmBox.display("Confirmation Dialog", "Changes will be lost! Do you really want to close?") == false){
	    			we.consume();
	    		}else{
		    		order.copy(tmpOrder);
	    		}
	        }
	    });
		txt_volume.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(order.stateProperty().get().equals(State.IN_PROCESS.name())){
					 changeValues.volumeProperty().set(Integer.parseInt(newValue));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}				
			}
			
		});
		
		txt_volume.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
					if(order.stateProperty().get().equals(State.IN_PROCESS.name())){
						int i = changeValues.volumeProperty().get() - order.getOrderLotChanges().volumeProperty().get();
						if(unfocus && i < 0){
							txt_errorMessage.setVisible(true);
							txt_errorMessage.setText("Volume can not be reduced.");
							txt_volume.setStyle("-fx-background-color: #ffc0cb;");
							txt_volume.setText(String.valueOf(order.getOrderLotChanges().volumeProperty().get()));
						}else if(!txt_volume.getText().matches("[0-9]*") && unfocus){
							txt_errorMessage.setText("");
							txt_errorMessage.setVisible(true);
							txt_errorMessage.setText(errorText);
							txt_volume.setStyle("-fx-background-color: #ffc0cb;");
							txt_volume.setText(String.valueOf(order.getOrderLotChanges().volumeProperty().get()));
						}else if(unfocus && i > 0 ) {
							Alert alert = new Alert(AlertType.INFORMATION);
					    	alert.setTitle("Notification");
					    	alert.setHeaderText("You updated the Volume. Click update to transfer new Lots to the MES.");
					    	alert.show();
						}else{
							txt_errorMessage.textProperty().set("");
							txt_errorMessage.setVisible(false);
							txt_volume.setStyle(null);
						}
					}	
					//For all states	
					else if(!focus){
							String localString;
							localString = txt_volume.getText();
							if(txt_volume.getText().length() == 0) {
								txt_volume.getStyleClass().add("label_error");
								txt_errorMessage.setVisible(true);
								txt_errorMessage.setText(errorText);
							}
							//Only numbers, letters and spaces are allowed.
							else if(txt_volume.getText().matches("[0-9]*")) { 
									txt_volume.setText(localString); 
									txt_volume.getStyleClass().add("reset_label_error");
									txt_errorMessage.textProperty().set("");
									txt_errorMessage.setVisible(false);
							}
							else{
								txt_volume.getStyleClass().add("label_error");
								txt_volume.setText("");
								txt_errorMessage.setVisible(true);
								txt_errorMessage.setText(errorText);
							} 
						}
					else{}
			}
		});  
		

		txt_price.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = txt_price.getText();
		if(!newValue){
				if(txt_price.getText().length() == 0 || txt_price.getText() == "0"){
					txt_price.getStyleClass().add("label_error");
					txt_errorMessage.setVisible(true);
					txt_errorMessage.setText(errorText);
				}
				//Allows double numbers separated by a comma or dot. The length is not limited
				else if(txt_price.getText().matches("(\\d+(?:[\\.\\,]\\d*)?)$")) {
						txt_price.setText(localString);
						txt_price.getStyleClass().add("reset_label_error");
						txt_errorMessage.setVisible(false);
						txt_errorMessage.setText("");
				}
				else{ 
					txt_price.getStyleClass().add("label_error");
					txt_price.setText(""); 
					txt_errorMessage.setVisible(true);
					txt_errorMessage.setText(errorText);
				} 
			}
		});  

		tar_comment.focusedProperty().addListener((arg0, oldValue, newValue) -> {
		String localString;
		localString = tar_comment.getText();
		if(!newValue){
			if(tar_comment.getText().length() > 250){
				tar_comment.getStyleClass().add("label_error");
				tar_comment.setText("");
				txt_errorMessage.setVisible(true);
				txt_errorMessage.setText(errorText);
				}
			else{
				tar_comment.setText(localString);
				tar_comment.getStyleClass().add("reset_label_error");
				txt_errorMessage.setVisible(false);
				txt_errorMessage.setText("");
				}
			}
		});
		
		txtLotSize.focusedProperty().addListener((arg0, oldValue, newValue) ->{
			String localString;
			localString = txtLotSize.getText();
			if(!newValue){
				if(txtLotSize.getText().length() == 0 || txtLotSize.getText().equals("")) {
					txtLotSize.getStyleClass().add("label_error");
					txt_errorMessage.setVisible(true);
					txt_errorMessage.setText(errorText);
				}
				//Only numbers, letters and spaces are allowed.
				else if(txtLotSize.getText().matches("[0-9]*")) { 
						txtLotSize.setText(localString); 
						txtLotSize.getStyleClass().add("reset_label_error");
						txt_errorMessage.textProperty().set("");
						txt_errorMessage.setVisible(false);
				}
				else{
					txtLotSize.getStyleClass().add("label_error");
					txtLotSize.setText("");
					txt_errorMessage.setVisible(true);
					txt_errorMessage.setText(errorText);
				} 
			}
		else{}
		});
	}
}
