/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class WarningController  {

    @FXML private Label label;
    @FXML private Label labelSpasi;
    @FXML public Button OK;
    @FXML public Button cancel;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage, String type, String warningText){
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) 
                OK.fire();
            if (event.getCode() == KeyCode.ESCAPE) 
                closeDialog();
            if (event.getCode() == KeyCode.SPACE) 
                closeDialog();
        });
        label.setText(warningText);
        if(type.equals("Warning")){
            OK.setVisible(false);
            cancel.setVisible(false);
            labelSpasi.setVisible(true);
        }else if(type.equals("Confirmation")){
            OK.setVisible(true);
            cancel.setVisible(true);
            labelSpasi.setVisible(false);
        }
    }
    public void closeDialog(){
        mainApp.closeDialog(stage);
    }   
    
}
