/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class TotalPenjualanController  {

    @FXML private Label kategoriLabel;
    @FXML private Label namaPelangganLabel;
    @FXML private Label totalPenjualanlabel;
    @FXML private Label totalDiskonlabel;
    @FXML private Label grandtotallabel;
    @FXML public Label totalPembayaranLabel;
    @FXML public Button saveButton;
    public Pelanggan pelanggan;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) 
                closeDialog();
            if (event.getCode() == KeyCode.F8) 
                cariPelanggan();
            if (event.getCode() == KeyCode.F12) 
                ubahPembayaran();
        });
    }
    public void setText(String kategori, String totalPenjualan, String totalDiskon, String grandtotal){
        kategoriLabel.setText(kategori);
        totalPenjualanlabel.setText(totalPenjualan);
        totalDiskonlabel.setText(totalDiskon);
        grandtotallabel.setText(grandtotal);
        totalPembayaranLabel.setText(grandtotal);
    }
    @FXML
    private void cariPelanggan(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/CariPelanggan.fxml");
        CariPelangganController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.pelangganTable.setRowFactory(table -> {
            TableRow<Pelanggan> row = new TableRow<Pelanggan>() {};
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        mainApp.closeDialog(child);
                        pelanggan = row.getItem();
                        namaPelangganLabel.setText(pelanggan.getNama());
                    }
                }
            });
            return row;
        });
        x.pelangganTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                if(x.pelangganTable.getSelectionModel().getSelectedItem()!=null){
                    mainApp.closeDialog(child);
                    pelanggan = x.pelangganTable.getSelectionModel().getSelectedItem();
                    namaPelangganLabel.setText(pelanggan.getNama());
                }
            }
        });
    }
    @FXML
    private void ubahPembayaran(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/UbahPembayaran.fxml");
        UbahPembayaranController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setPembayaran(Double.parseDouble(totalPembayaranLabel.getText().replaceAll(",", "")));
        child.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) 
                x.close();
        });
        x.pembayaranField.setOnAction((event) -> {
            if(Double.parseDouble(grandtotallabel.getText().replaceAll(",", ""))>=Double.parseDouble(x.pembayaranField.getText().replaceAll(",", ""))){
                totalPembayaranLabel.setText(x.pembayaranField.getText());
                x.close();
            }else{
                Stage warning = new Stage();
                FXMLLoader warningLoader = mainApp.showDialog(child, warning ,"View/Dialog/Warning.fxml");
                WarningController warningController = warningLoader.getController();
                warningController.setMainApp(mainApp, warning, "Warning", "Pembayaran yang dibayar melebihi total penjualan !!");
            }
        });
    }
    public void closeDialog(){
        mainApp.closeDialog(stage);
    }   

}
