package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
	@FXML private TreeTableColumn<Route, String> lotId;
	@FXML private TreeTableColumn<Route, String> pieces;
	@FXML private TreeTableColumn<Route, String> state;
	@FXML private TreeTableColumn<Route, String> dueDate;
	
	public void initialize(URL location, ResourceBundle resources) {
	
    }
	
    public void setMainApp(MainMenu mainMenu, String orderNo, String product) {
        this.mainMenu = mainMenu;
        this.orderNo = orderNo;
        this.product = product; 
        setTable();
    }
    private void setTable(){
    	int routeLotCount=0;
    	List<Lot> routeOperLotList = mainMenu.getLotList(orderNo);
    	TreeItem<Route> root = new TreeItem<>(new Route("","","","", "", "","","","",""));
    	root.setExpanded(true);
    	for (Route r : mainMenu.getRouteList(orderNo, product)) {
    		routeLotCount = 0;
        	TreeItem<Route> route = new TreeItem<>(r);
        	route.setExpanded(false);
        	for (Operation oper : r.getOperList()) {
        		
        		if(!oper.lotCountProperty().get().equals("0")){
        			route.setExpanded(true);
        			routeLotCount += Integer.parseInt(oper.lotCountProperty().get());
        			//route.getValue().lotCountProperty().set(oper.lotCountProperty().get());
        			route.getValue().lotCountProperty().set(String.valueOf(routeLotCount));
        		}																													
        		TreeItem<Route> op = new TreeItem<>(new Route("","","",oper.operationProperty().get(),oper.descriptionProperty().get(),oper.lotCountProperty().get(),"","","",""));
        		TreeItem<Route> l = new TreeItem<>(op.getValue());
        		List<Lot> tmpLotList = routeOperLotList.stream().filter(p -> p.routeProperty().get().equals(r.routeProperty().get()) && p.operationProperty().get().equals(oper.operationProperty().get())).collect(Collectors.toList());
        		for (Lot lot : tmpLotList) {
					l.getChildren().add(new TreeItem<>(new Route("","","","","","",lot.idProperty().get(), lot.piecesProperty().getValue().toString(),lot.stateProperty().get(),lot.dueDateProperty().get())));
        		}
        		route.getChildren().add(l);
			}
        	root.getChildren().add(route);  
		}    
                
		seq.setCellValueFactory(cellData -> cellData.getValue().getValue().seqProperty());
        route.setCellValueFactory(cellData -> cellData.getValue().getValue().routeProperty());
        operation.setCellValueFactory(cellData -> cellData.getValue().getValue().operationProperty());
        description.setCellValueFactory(cellData -> cellData.getValue().getValue().descriptionProperty());
        lotCount.setCellValueFactory(cellData -> cellData.getValue().getValue().lotCountProperty());
        lotId.setCellValueFactory(cellData -> cellData.getValue().getValue().idProperty());
        pieces.setCellValueFactory(cellData -> cellData.getValue().getValue().piecesProperty());
        state.setCellValueFactory(cellData -> cellData.getValue().getValue().stateProperty());
        dueDate.setCellValueFactory(cellData -> cellData.getValue().getValue().dueDateProperty());
        
        prodTreeTable.setRoot(root);
        prodTreeTable.setShowRoot(false);

    }
}
