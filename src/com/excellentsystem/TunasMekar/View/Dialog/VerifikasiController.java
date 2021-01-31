/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class VerifikasiController  {

    @FXML public TextArea textArea;
    @FXML public PasswordField passwordField;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main main, Stage stage){
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
