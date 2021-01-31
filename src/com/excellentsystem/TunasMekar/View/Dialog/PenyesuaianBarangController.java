/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.Satuan;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class PenyesuaianBarangController {

    @FXML public ComboBox<String> jenisPenyesuaianCombo;
    @FXML public TextField kodeBarangField;
    @FXML private TextField namaBarangField;
    @FXML public TextField qtyField;
    @FXML public TextField nilaiField;
    @FXML public ComboBox<String> satuanCombo;
    
    @FXML public TextArea catatanField;
    @FXML public Button saveButton;
    
    private Stage stage;
    private Main mainApp;   
    private ObservableList<String> listJenisPenyesuaian = FXCollections.observableArrayList();
    private ObservableList<String> allSatuan = FXCollections.observableArrayList();
    public void initialize() {
        Function.setNumberField(qtyField, qty);
        Function.setNumberField(nilaiField, rp);
        satuanCombo.setItems(allSatuan);
        jenisPenyesuaianCombo.setItems(listJenisPenyesuaian);
    }    
    public void setMainApp(Main main,Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        listJenisPenyesuaian.clear();
        listJenisPenyesuaian.add("Penambahan Stok Barang");
        listJenisPenyesuaian.add("Pengurangan Stok Barang");
        jenisPenyesuaianCombo.getSelectionModel().selectFirst();
    } 
    public void setBarang(Barang b){
        try(Connection con = Koneksi.getConnection()){
            allSatuan.clear();
            List<Satuan> listSatuan = SatuanDAO.getAllByKodeBarang(con, b.getKodeBarang());
            for(Satuan s : listSatuan){
                allSatuan.add(s.getKodeSatuan());
            }
            kodeBarangField.setText(b.getKodeBarang());
            namaBarangField.setText(b.getNamaBarang());
            qtyField.setText("1");
            nilaiField.setText("0");
            if(b.getSatuan()!=null)
                satuanCombo.getSelectionModel().select(b.getSatuan().getKodeSatuan());
            else
                satuanCombo.getSelectionModel().selectFirst();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    public void cariBarang(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariBarang.fxml");
        CariBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setCari();
        x.barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {};
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        mainApp.closeDialog(child);
                        Barang b = row.getItem();
                        setBarang(b);
                    }
                }
            });
            return row;
        });
        x.barangTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                if(x.barangTable.getSelectionModel().getSelectedItem()!=null){
                    mainApp.closeDialog(child);
                    setBarang(x.barangTable.getSelectionModel().getSelectedItem());
                }
            }
        });
    } 
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
