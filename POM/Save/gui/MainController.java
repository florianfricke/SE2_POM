package gui;
import types.*;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController{
	@FXML private TableView<Customer> tbv;
	@FXML private TableColumn<Customer, String> id;
    @FXML private TableColumn<Customer, String> name;
    @FXML private TableColumn<Customer, String> ranking;
    @FXML private TableColumn<Customer, String> comment;
    private MainMenu mainMenu;
    
    
    public MainController(){
    	
    }
    
	public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ranking.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        comment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
    }
    
    
    public void setMainApp(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        //tbv.setItems(mainMenu.getCustomerList());

    }

}
