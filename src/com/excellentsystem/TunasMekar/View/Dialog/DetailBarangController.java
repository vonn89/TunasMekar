/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.KategoriBarangDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.KategoriBarang;
import com.excellentsystem.TunasMekar.Model.Satuan;
import java.sql.Connection;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class DetailBarangController {

    @FXML private GridPane gridPane;
    @FXML public TextField kodeBarangField;
    @FXML public ComboBox<String> kodeKategoriCombo;
    @FXML public TextField namaBarangField;
    @FXML public TextField supplierField;
    @FXML public TextField stokMinimalField;

    @FXML public Button saveButton;
    @FXML private Button cancelButton;
    
    @FXML private TableView<Satuan> satuanTable;
    @FXML private TableColumn<Satuan, String> kodeSatuanColumn;
    @FXML private TableColumn<Satuan, String> kodeBarcodeColumn;
    @FXML private TableColumn<Satuan, Number> qtyColumn;
    @FXML private TableColumn<Satuan, Number> hargaGrosirBesarColumn;
    @FXML private TableColumn<Satuan, Number> hargaGrosirColumn;
    @FXML private TableColumn<Satuan, Number> hargaRetailColumn;
    
    private Main mainApp;
    private Stage stage;
    private ObservableList<String> allKategori = FXCollections.observableArrayList();
    public ObservableList<Satuan> allSatuan = FXCollections.observableArrayList();
    public void initialize() {
        kodeSatuanColumn.setCellValueFactory(cellData -> cellData.getValue().kodeSatuanProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> {
            String kodeBarcode = cellData.getValue().getKodeBarcode();
            if(cellData.getValue().getKodeBarang()!=null && 
                    cellData.getValue().getKodeBarcode() !=null && 
                    !cellData.getValue().getKodeBarcode().startsWith(cellData.getValue().getKodeBarang()))
                return new SimpleStringProperty(kodeBarcode);
            else
                return new SimpleStringProperty("");
        });
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        hargaRetailColumn.setCellValueFactory(cellData -> cellData.getValue().hargaRetailProperty());
        hargaRetailColumn.setCellFactory(col -> getTableCell(rp));
        hargaGrosirColumn.setCellValueFactory(cellData -> cellData.getValue().hargaGrosirProperty());
        hargaGrosirColumn.setCellFactory(col -> getTableCell(rp));
        hargaGrosirBesarColumn.setCellValueFactory(cellData -> cellData.getValue().hargaGrosirBesarProperty());
        hargaGrosirBesarColumn.setCellFactory(col -> getTableCell(rp));
        
        Function.setNumberField(stokMinimalField, rp);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Satuan Baru");
        addNew.setOnAction((ActionEvent e)->{
            addNewSatuan();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            satuanTable.refresh();
        });
        rowMenu.getItems().addAll(addNew, refresh);
        satuanTable.setContextMenu(rowMenu);
        satuanTable.setRowFactory(t -> {
            TableRow<Satuan> row = new TableRow<Satuan>() {
                @Override
                public void updateItem(Satuan item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Satuan Baru");
                        addNew.setOnAction((ActionEvent e)->{
                            addNewSatuan();
                        });
                        MenuItem edit = new MenuItem("Ubah Satuan");
                        edit.setOnAction((ActionEvent e) -> {
                            editSatuan(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Satuan");
                        delete.setOnAction((ActionEvent e) -> {
                            allSatuan.remove(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            satuanTable.refresh();
                        });
                        if(saveButton.isVisible())
                            rowMenu.getItems().addAll(addNew, edit, delete);
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    if(saveButton.isVisible()){
                        editSatuan(row.getItem());
                    }
            });
            return row;
        });
        kodeKategoriCombo.setItems(allKategori);
        satuanTable.setItems(allSatuan);
    }
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        getKategori();
    }
    private void getKategori(){
        Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    List<KategoriBarang> kategori = KategoriBarangDAO.getAll(con);
                    allKategori.clear();
                    for (KategoriBarang temp : kategori) {
                        allKategori.add(temp.getKodeKategori());
                    }
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void setNewBarang(){
        kodeBarangField.setText("New Barang");
        kodeKategoriCombo.getSelectionModel().clearSelection();
        namaBarangField.setText("");
        supplierField.setText("");
        stokMinimalField.setText("0");
        allSatuan.clear();
    }
    public void setBarang(Barang b) {
        allSatuan.addAll(b.getAllSatuan());
        kodeBarangField.setText(b.getKodeBarang());
        kodeKategoriCombo.getSelectionModel().select(b.getKodeKategori());
        namaBarangField.setText(b.getNamaBarang());
        supplierField.setText(b.getSupplier());
        stokMinimalField.setText(rp.format(b.getStokMinimal()));
    }
    public void setBarang(String kodeBarang) {
        Task<Barang> task = new Task<Barang>() {
            @Override 
            public Barang call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    Barang b = BarangDAO.get(con, kodeBarang);
                    b.setAllSatuan(SatuanDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                    return b;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            Barang b = task.getValue();
            if(b!=null){
                allSatuan.addAll(b.getAllSatuan());
                kodeBarangField.setText(b.getKodeBarang());
                kodeKategoriCombo.getSelectionModel().select(b.getKodeKategori());
                namaBarangField.setText(b.getNamaBarang());
                supplierField.setText(b.getSupplier());
                stokMinimalField.setText(rp.format(b.getStokMinimal()));
            }else{
                mainApp.showMessage(Modality.NONE, "Warning", "Barang tidak ditemukan");
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
        kodeBarangField.setDisable(true);
        kodeKategoriCombo.setDisable(true);
        namaBarangField.setDisable(true);
        supplierField.setDisable(true);
        stokMinimalField.setDisable(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        
        MenuItem deleteMenu = null;
        for(MenuItem m : satuanTable.getContextMenu().getItems()){
            if(m.getText().equals("Tambah Satuan Baru"))
                deleteMenu = m;
        }
        satuanTable.getContextMenu().getItems().remove(deleteMenu);
        
        gridPane.getRowConstraints().get(9).setMinHeight(0);
        gridPane.getRowConstraints().get(9).setPrefHeight(0);
        gridPane.getRowConstraints().get(9).setMaxHeight(0);
    }
    private void addNewSatuan(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/SatuanBarang.fxml");
        SatuanBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.saveButton.setOnAction((event) -> {
            if("".equals(x.kodeSatuanField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode satuan masih kosong");
            else if("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
            else if("".equals(x.hargaRetailField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
            else if("".equals(x.hargaGrosirField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
            else if("".equals(x.hargaGrosirBesarField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir besar masih kosong");
            else{
                Satuan satuan = null;
                for(Satuan temp : allSatuan){
                    if(temp.getKodeSatuan().equals(x.kodeSatuanField.getText()))
                        satuan = temp;
                }
                if(satuan==null){
                    satuan = new Satuan();
                    satuan.setKodeBarang(kodeBarangField.getText());
                    satuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    satuan.setKodeSatuan(x.kodeSatuanField.getText());
                    satuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    satuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
                    allSatuan.add(satuan);
                }else{
                    Satuan newSatuan = new Satuan();
                    newSatuan.setKodeBarang(kodeBarangField.getText());
                    newSatuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    newSatuan.setKodeSatuan(x.kodeSatuanField.getText());
                    newSatuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    newSatuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
                    allSatuan.remove(satuan);
                    allSatuan.add(newSatuan);
                }
                satuanTable.refresh();
                x.close();
            }
        });
    }
    private void editSatuan(Satuan s){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/SatuanBarang.fxml");
        SatuanBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.kodeSatuanField.setText(s.getKodeSatuan());
        if(s.getKodeBarang()!=null && s.getKodeBarcode() !=null && !s.getKodeBarcode().startsWith(s.getKodeBarang()))
            x.kodeBarcodeField.setText(s.getKodeBarcode());
        x.qtyField.setText(qty.format(s.getQty()));
        x.hargaRetailField.setText(rp.format(s.getHargaRetail()));
        x.hargaGrosirField.setText(rp.format(s.getHargaGrosir()));
        x.hargaGrosirBesarField.setText(rp.format(s.getHargaGrosirBesar()));
        x.kodeSatuanField.setDisable(true);
        x.saveButton.setOnAction((event) -> {
            if("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
            else if("".equals(x.hargaRetailField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
            else if("".equals(x.hargaGrosirField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
            else if("".equals(x.hargaGrosirBesarField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir besar masih kosong");
            else{
                Satuan satuan = null;
                for(Satuan temp : allSatuan){
                    if(temp.getKodeSatuan().equals(x.kodeSatuanField.getText()))
                        satuan = temp;
                }
                if(satuan==null){
                    satuan = new Satuan();
                    satuan.setKodeBarang(kodeBarangField.getText());
                    satuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    satuan.setKodeSatuan(x.kodeSatuanField.getText());
                    satuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    satuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
                    allSatuan.add(satuan);
                }else{
                    Satuan newSatuan = new Satuan();
                    newSatuan.setKodeBarang(kodeBarangField.getText());
                    newSatuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    newSatuan.setKodeSatuan(x.kodeSatuanField.getText());
                    newSatuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    newSatuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
                    allSatuan.remove(satuan);
                    allSatuan.add(newSatuan);
                }
                satuanTable.refresh();
                x.close();
            }
        });
    }
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }

}
