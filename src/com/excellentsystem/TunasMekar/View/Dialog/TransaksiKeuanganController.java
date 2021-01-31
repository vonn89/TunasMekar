/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.KategoriTransaksi;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class TransaksiKeuanganController  {

    @FXML public ComboBox<String> tipeKeuanganCombo;
    @FXML public ComboBox<String> kategoriCombo;
    @FXML public TextField keteranganField;
    @FXML public TextField jumlahRpField;
    @FXML public Button saveButton;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        Function.setNumberField(jumlahRpField, rp);
    }    
    public void setMainApp(Main main,Stage stage){
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setKategori(List<KategoriTransaksi> listKategoriTransaksi){
        ObservableList<String> allTipeKasir = FXCollections.observableArrayList();
        allTipeKasir.addAll("Kas");
        tipeKeuanganCombo.setItems(allTipeKasir);
        tipeKeuanganCombo.getSelectionModel().select("Kas");
        ObservableList<String> allKategori = FXCollections.observableArrayList();
        for(KategoriTransaksi k : listKategoriTransaksi){
            allKategori.add(k.getKodeKategori());
        }
        kategoriCombo.setItems(allKategori);
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
