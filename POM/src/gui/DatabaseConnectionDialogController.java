package gui;

import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import types.ErrorLog;

public class DatabaseConnectionDialogController{
	
	@FXML private TextField txtDatabase;
	@FXML private TextField txtUser;
	@FXML private TextField txtPassword;
	
	
	@FXML private void handleSave(ActionEvent event){
		
		try{
		    PrintWriter writer = new PrintWriter("dbConnectionFile.txt", "UTF-8");
		    writer.println(txtDatabase.getText()+","+ txtUser.getText()+","+txtPassword.getText());
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


