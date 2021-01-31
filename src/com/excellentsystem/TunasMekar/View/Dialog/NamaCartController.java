/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class NamaCartController {

    @FXML public TextField namaCartField;
    @FXML public Button saveButton;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
    }    
    public void setMainApp(Main mainApp,Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setNamaCart(String namaCart){
        namaCartField.setText(namaCart);
    }
    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
    
}
