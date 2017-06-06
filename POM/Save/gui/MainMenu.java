package gui;
import types.Customer;
import types.SaveType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pom_service.PomService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainMenu extends Application {
	private PomService pomService;
	private TableView<Customer> table = new TableView<Customer>();
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	@Override
	public void start(Stage stage) {
		pomService = new PomService(SaveType.postgres);
		
		stage.setTitle("POM");
		BorderPane bp = new BorderPane();
		ToolBar toolbar = new ToolBar();
		HBox statusbar = new HBox();
		bp.setTop(toolbar);
		bp.setBottom(statusbar);
		
		//SideMenu
		VBox vbox = new VBox();
		vbox.setMinWidth(100);
		vbox.setAlignment(Pos.BOTTOM_CENTER);
		Button btnDash = new Button("Dashboard");
		btnDash.prefWidthProperty().bind(vbox.widthProperty());
		Button btnCustomer = new Button("Customer");
		btnCustomer.prefWidthProperty().bind(vbox.widthProperty());
		
		btnCustomer.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent event) {
		    	try {
		            FXMLLoader fxmlLoader = new FXMLLoader();
		            fxmlLoader.setLocation(getClass().getResource("CustomerCard.fxml"));
		            /* 
		             * if "fx:controller" is not set in fxml
		             * fxmlLoader.setController(NewWindowController);
		             */
		            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
		            Stage stage = new Stage();
		            stage.setTitle("New Window");
		            stage.setScene(scene);
		            stage.show();
		        } catch (IOException e) {
		            Logger logger = Logger.getLogger(getClass().getName());
		            logger.log(Level.SEVERE, "Failed to create new Window.", e);
		        }
		    }
		});
		
		Button btnOrder = new Button("Order");
		btnOrder.prefWidthProperty().bind(vbox.widthProperty());
		vbox.getChildren().addAll(btnDash, btnCustomer, btnOrder);
		bp.setLeft(vbox);
		
		//TableView
		table.setEditable(false);
		TableColumn<Customer, String> idCol = new TableColumn<Customer, String>("ID");
        TableColumn<Customer, String> nameCol = new TableColumn<Customer, String>("Name");
        TableColumn<Customer, String> rankingCol = new TableColumn<Customer, String>("Ranking");
        TableColumn<Customer, String> commentCol = new TableColumn<Customer, String>("Comment");


        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        rankingCol.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());
        commentCol.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        
        table.setItems(FXCollections.observableList(pomService.getCustomerList()));
        table.getColumns().addAll(idCol, nameCol, rankingCol, commentCol);
        bp.setCenter(table);
		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.show();
	}
}
