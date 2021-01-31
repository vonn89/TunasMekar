/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class UbahHargaController  {

    @FXML public TextField hargaRetailField;
    @FXML public TextField hargaGrosirField;
    @FXML public TextField hargaGrosirBesarField;
    @FXML public Button saveButton;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        Function.setNumberField(hargaRetailField, rp);
        Function.setNumberField(hargaGrosirField, rp);
        Function.setNumberField(hargaGrosirBesarField, rp);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setHarga(double hargaRetail, double hargaGrosir, double hargaGrosirBesar){
        hargaRetailField.setText(rp.format(hargaRetail));
        hargaGrosirField.setText(rp.format(hargaGrosir));
        hargaGrosirBesarField.setText(rp.format(hargaGrosirBesar));
    }
    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
    
}
