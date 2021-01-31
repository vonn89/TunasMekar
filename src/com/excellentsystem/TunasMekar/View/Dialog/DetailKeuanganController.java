/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.Keuangan;
import java.text.ParseException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class DetailKeuanganController  {

    @FXML public TableView<Keuangan> keuanganTable;
    @FXML private TableColumn<Keuangan, String> noKeuanganColumn;
    @FXML private TableColumn<Keuangan, String> tglKeuanganColumn;
    @FXML private TableColumn<Keuangan, String> keteranganColumn;
    @FXML private TableColumn<Keuangan, Number> jumlahRpColumn;
    @FXML private TableColumn<Keuangan, String> kodeUserColumn;
    
    @FXML private Label tipeKasirLabel;
    @FXML private Label kategoriLabel;
    @FXML private Label totalLabel;
    private ObservableList<Keuangan> allKeuangan = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        noKeuanganColumn.setCellValueFactory(cellData -> cellData.getValue().noKeuanganProperty());
        tglKeuanganColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglKeuangan())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tglKeuanganColumn.setComparator(Function.sortDate(tglLengkap));
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        jumlahRpColumn.setCellValueFactory(cellData -> cellData.getValue().jumlahRpProperty());
        jumlahRpColumn.setCellFactory(col -> getTableCell(rp));
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());
    }    
    public void setMainApp(Main main, Stage stage){
        this.mainApp = main;
        this.stage = stage;
        keuanganTable.setItems(allKeuangan);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setKeuangan(Keuangan k){
        tipeKasirLabel.setText(k.getTipeKeuangan());
        kategoriLabel.setText(k.getKategori());
        allKeuangan.clear();
        allKeuangan.addAll(k.getListKeuangan());
        keuanganTable.refresh();
        hitungTotal();
    }
    private void hitungTotal(){
        double total = 0;
        for(Keuangan k : allKeuangan){
            total = total + k.getJumlahRp();
        }
        totalLabel.setText(rp.format(total));
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
