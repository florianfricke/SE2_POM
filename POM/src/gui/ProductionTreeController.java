package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.*;

public class ProductionTreeController {
	private MainMenu mainMenu;
	private String orderNo;
	private String product;
	@FXML private TreeTableView<Route> prodTreeTable;
	@FXML private TreeTableColumn<Route, String> seq;
	@FXML private TreeTableColumn<Route, String> route;
	@FXML private TreeTableColumn<Route, String> operation;
	@FXML private TreeTableColumn<Route, String> description;
	@FXML private TreeTableColumn<Route, String> lotCount;
	
	public void initialize(URL location, ResourceBundle resources) {

		
    }
	
    public void setMainApp(MainMenu mainMenu, String orderNo, String product) {
        this.mainMenu = mainMenu;
        this.orderNo = orderNo;
        this.product = product; 
        setTable();
    }
    private void setTable(){
    	TreeItem<Route> root = new TreeItem<>(new Route("","","","", "", ""));
    	root.setExpanded(true);
    	for (Route r : mainMenu.getRouteList(orderNo, product)) {
        	TreeItem<Route> t = new TreeItem<>(r);
        	t.setExpanded(false);
        	for (Operation oper : r.getOperList()) {
        		if(!oper.lotCountProperty().get().equals("0")){
        			t.setExpanded(true);
        			t.getValue().lotCountProperty().set(oper.lotCountProperty().get());
        		}
        		t.getChildren().add(new TreeItem<>(new Route("","","",oper.operationProperty().get(),oper.descriptionProperty().get(),oper.lotCountProperty().get())));
			}
        	root.getChildren().add(t);  
		}    
                
		seq.setCellValueFactory(cellData -> cellData.getValue().getValue().seqProperty());
        route.setCellValueFactory(cellData -> cellData.getValue().getValue().routeProperty());
        operation.setCellValueFactory(cellData -> cellData.getValue().getValue().operationProperty());
        description.setCellValueFactory(cellData -> cellData.getValue().getValue().descriptionProperty());
        lotCount.setCellValueFactory(cellData -> cellData.getValue().getValue().lotCountProperty());
        
        prodTreeTable.setRoot(root);
        prodTreeTable.setShowRoot(false);

    }
}
