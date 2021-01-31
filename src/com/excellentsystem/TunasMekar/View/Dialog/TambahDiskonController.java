/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Diskon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class TambahDiskonController {

    @FXML public Label namaDiskonField;
    @FXML public TextField diskonRpField;
    @FXML public TextField qtyField;
    @FXML public TextField totalDiskonField;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        qtyField.setOnKeyReleased((event) -> {
            try{
                String string = qtyField.getText();
                if(string.contains("-"))
                    qtyField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            qtyField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            qtyField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        qtyField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                qtyField.end();
            }catch(Exception e){
                qtyField.undo();
            }
            hitungTotal();
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setDiskon(Diskon diskon){
        if(diskon != null){
            namaDiskonField.setText(diskon.getNamaDiskon());
            diskonRpField.setText(rp.format(diskon.getDiskonRp()));

            hitungTotal();
            qtyField.requestFocus();
        }else{
            close();
        }
    }
    @FXML
    private void hitungTotal(){
        double qty = Double.parseDouble(qtyField.getText().replaceAll(",", ""));
        double diskon = Double.parseDouble(diskonRpField.getText().replaceAll(",", ""));
        totalDiskonField.setText(rp.format(qty*diskon));
    }
    @FXML
    public void close() {
        mainApp.closeDialog(stage);
    }
    
}
