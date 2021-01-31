/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.PembelianDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.Satuan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class UbahBarangController  {

    @FXML private Label title;
    @FXML public TextField qtyField;
    @FXML public ComboBox<String> satuanCombo;
    @FXML public TextField nilaiField;
    
    private Main mainApp;
    private Stage stage;
    private ObservableList<String> allSatuan = FXCollections.observableArrayList();
    public void initialize() {
        Function.setNumberField(qtyField, qty);
        Function.setNumberField(nilaiField, rp);
        qtyField.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                qtyField.requestFocus();
                Platform.runLater(() -> {
                    if (qtyField.isFocused() && !qtyField.getText().isEmpty()) {
                        qtyField.selectAll();
                    }
                });
            }
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                nilaiField.requestFocus();
                Platform.runLater(() -> {
                    if (nilaiField.isFocused() && !nilaiField.getText().isEmpty()) {
                        nilaiField.selectAll();
                    }
                });
            }
        });
        nilaiField.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.UP) {
                event.consume();
                qtyField.requestFocus();
                Platform.runLater(() -> {
                    if (qtyField.isFocused() && !qtyField.getText().isEmpty()) {
                        qtyField.selectAll();
                    }
                });
            }
            if (event.getCode() == KeyCode.DOWN) {
                event.consume();
                nilaiField.requestFocus();
                Platform.runLater(() -> {
                    if (nilaiField.isFocused() && !nilaiField.getText().isEmpty()) {
                        nilaiField.selectAll();
                    }
                });
            }
        });
    }    
    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        satuanCombo.setItems(allSatuan);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) 
                close();
            if (event.getCode() == KeyCode.HOME) 
                satuanCombo.getSelectionModel().selectPrevious();
            if (event.getCode() == KeyCode.END) 
                satuanCombo.getSelectionModel().selectNext();
        });
    }
    public void setPenjualan(PenjualanDetail p, String kategoriPenjualan) {
        for (Satuan sat : p.getBarang().getAllSatuan()) {
            allSatuan.add(sat.getKodeSatuan());
        }
        title.setText(p.getBarang().getNamaBarang());
        satuanCombo.getSelectionModel().select(p.getSatuan());
        qtyField.setText(qty.format(p.getQty()));
        nilaiField.setText(rp.format(p.getHarga()));
        
        satuanCombo.setOnAction((event) -> {
            Satuan s = null;
            for (Satuan sat : p.getBarang().getAllSatuan()) {
                if(sat.getKodeSatuan().equals(satuanCombo.getSelectionModel().getSelectedItem()))
                    s = sat;
            }
            if(s!=null){
                if(kategoriPenjualan.equalsIgnoreCase("Grosir Besar"))
                    nilaiField.setText(rp.format(s.getHargaGrosirBesar()));
                else if(kategoriPenjualan.equalsIgnoreCase("Grosir"))
                    nilaiField.setText(rp.format(s.getHargaGrosir()));
                else if(kategoriPenjualan.equalsIgnoreCase("Retail"))
                    nilaiField.setText(rp.format(s.getHargaRetail()));
            }
        });
        qtyField.requestFocus();
        qtyField.selectAll();
    }
    public void setPembelian(PembelianDetail p) {
        for (Satuan sat : p.getBarang().getAllSatuan()) {
            allSatuan.add(sat.getKodeSatuan());
        }
        title.setText(p.getBarang().getNamaBarang());
        satuanCombo.getSelectionModel().select(p.getSatuan());
        qtyField.setText(qty.format(p.getQty()));
        nilaiField.setText(rp.format(p.getHargaBeli()));
        
        satuanCombo.setOnAction((event) -> {
            Satuan s = null;
            for (Satuan sat : p.getBarang().getAllSatuan()) {
                if(sat.getKodeSatuan().equals(satuanCombo.getSelectionModel().getSelectedItem()))
                    s = sat;
            }
        });
        qtyField.requestFocus();
        qtyField.selectAll();
    }
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
