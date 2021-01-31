/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.PelangganDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import java.sql.Connection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class DetailPelangganController {
    @FXML private GridPane gridPane;
    @FXML public TextField kodePelangganField;
    @FXML public TextField namaField;
    @FXML public TextField alamatField;
    @FXML public TextField noTelpField;

    @FXML public Button saveButton;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
    }
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setNewPelanggan(){
        kodePelangganField.setText("");
        namaField.setText("");
        alamatField.setText("");
        noTelpField.setText("");
    }
    public void setPelanggan(Pelanggan b) {
        kodePelangganField.setDisable(true);
        kodePelangganField.setText(b.getKodePelanggan());
        namaField.setText(b.getNama());
        alamatField.setText(b.getAlamat());
        noTelpField.setText(b.getNoTelp());
    }
    public void setPelanggan(String kodePelanggan) {
        Task<Pelanggan> task = new Task<Pelanggan>() {
            @Override 
            public Pelanggan call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    return PelangganDAO.get(con, kodePelanggan);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            Pelanggan b = task.getValue();
            if(b!=null){
                setPelanggan(b);
            }else{
                mainApp.showMessage(Modality.NONE, "Warning", "Pelanggan tidak ditemukan");
            }
        });
        task.setOnFailed((e) -> {
            task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void setViewOnly(){
        kodePelangganField.setDisable(true);
        namaField.setDisable(true);
        alamatField.setDisable(true);
        noTelpField.setDisable(true);
        saveButton.setVisible(false);
        
        gridPane.getRowConstraints().get(7).setMinHeight(0);
        gridPane.getRowConstraints().get(7).setPrefHeight(0);
        gridPane.getRowConstraints().get(7).setMaxHeight(0);
    }
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
