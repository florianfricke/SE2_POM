package gui;

import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import types.ErrorLog;

public class DatabaseConnectionDialogController{
	
	@FXML private TextField txtServerAddress;
	@FXML private TextField txtPort;
	@FXML private TextField txtDatabaseName;
	@FXML private TextField txtUser;
	@FXML private TextField txtPassword;
	private Stage currentStage;
	
	public void init(Stage stage){
		this.currentStage = stage;
		
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	public void handle(WindowEvent we) {
	    		Platform.exit();
	        }
	    });
	}
	
	@FXML private void handleSave(ActionEvent event){
		
		try{
		    PrintWriter writer = new PrintWriter("dbConnectionFile.txt", "UTF-8");
		    writer.println(txtServerAddress.getText());
		    writer.println(txtPort.getText());
		 	writer.println(txtDatabaseName.getText());
			writer.println(txtUser.getText());
			writer.println(txtPassword.getText());
		    writer.close();
		} catch (IOException e) {
		   ErrorLog.write(e);
		}
		
		closeWindow(event);
	}
	
	
	
	private void closeWindow(ActionEvent e){
    	final Node currStage = (Node)e.getSource();
    	Stage stage = (Stage) currStage.getScene().getWindow();
    	stage.close(); 
	}
	

	
}


