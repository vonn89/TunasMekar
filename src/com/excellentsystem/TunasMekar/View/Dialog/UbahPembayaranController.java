/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class UbahPembayaranController  {

    @FXML public TextField pembayaranField;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        Function.setNumberField(pembayaranField, rp);
        pembayaranField.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                pembayaranField.requestFocus();
                Platform.runLater(() -> {
                    if (pembayaranField.isFocused() && !pembayaranField.getText().isEmpty()) {
                        pembayaranField.selectAll();
                    }
                });
            }
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                pembayaranField.requestFocus();
                Platform.runLater(() -> {
                    if (pembayaranField.isFocused() && !pembayaranField.getText().isEmpty()) {
                        pembayaranField.selectAll();
                    }
                });
            }
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setPembayaran(double pembayaran){
        pembayaranField.setText(rp.format(pembayaran));
    }
    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
}
