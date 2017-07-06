package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
	@FXML private Label txt_errorMessage;
	private Stage currentStage;
	
	public void init(Stage stage){
		this.currentStage = stage;
		createEventHandler();
	}
	
	@FXML private void handleSave(ActionEvent event){
		 try{
			 Connection con = DriverManager.getConnection("jdbc:postgresql://"+txtServerAddress.getText().trim()+":"+txtPort.getText().trim()+"/"+txtDatabaseName.getText().trim()+"?currentSchema=pom",txtUser.getText().trim(),txtPassword.getText().trim());
			 con.close();
		 }catch(SQLException e){
			txt_errorMessage.setVisible(true);
			txt_errorMessage.setText("Could not open Database Connection.");
			return;
		 }
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
	
	private void createEventHandler(){
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
	    	public void handle(WindowEvent we) {
	    		Platform.exit();
	    		System.exit(0);
	        }
	    });
		
		txtServerAddress.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
				if(focus){
					txt_errorMessage.setVisible(false);
				}
			}
		});
		txtPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
				if(focus){
					txt_errorMessage.setVisible(false);
				}
			}
		});
		txtDatabaseName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
				if(focus){
					txt_errorMessage.setVisible(false);
				}
			}
		});
		txtPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
				if(focus){
					txt_errorMessage.setVisible(false);
				}
			}
		});
		txtUser.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean unfocus, Boolean focus) {
				if(focus){
					txt_errorMessage.setVisible(false);
				}
			}
		});
	}
}


