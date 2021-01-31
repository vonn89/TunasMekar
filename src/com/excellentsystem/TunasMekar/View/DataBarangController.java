/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.KategoriBarangDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.KategoriBarang;
import com.excellentsystem.TunasMekar.Model.Satuan;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.DetailBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.ImportBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import com.excellentsystem.TunasMekar.View.Dialog.UbahHargaController;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
 * @author Yunaz
 */
public class DataBarangController  {

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML private TableColumn<Barang, String> kodeBarangColumn;
    @FXML private TableColumn<Barang, String> kodeSatuanColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, String> supplierColumn;
    @FXML private TableColumn<Barang, Number> stokMinimalColumn;
    @FXML private TableColumn<Barang, Number> qtyColumn;
    @FXML private TableColumn<Barang, Number> hargaBeliColumn;
    @FXML private TableColumn<Barang, Number> hargaRetailColumn;
    @FXML private TableColumn<Barang, Number> hargaGrosirColumn;
    @FXML private TableColumn<Barang, Number> hargaGrosirBesarColumn;
    @FXML private TableColumn<Barang, Number> labaGrosirColumn;
    
    @FXML private Label totalQtyLabel;
    @FXML private ComboBox<String> kategoriCombo;
    @FXML private TextField searchField;
    private List<Barang> uploadData;
    private Main mainApp;
    private ObservableList<String> allKategori = FXCollections.observableArrayList();
    private ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Barang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        kodeSatuanColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().kodeSatuanProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> {
            String kodeBarcode = cellData.getValue().getSatuan().getKodeBarcode();
            if(cellData.getValue().getKodeBarang()!=null && 
                    cellData.getValue().getSatuan().getKodeBarcode() !=null && 
                    !cellData.getValue().getSatuan().getKodeBarcode().startsWith(cellData.getValue().getKodeBarang()))
                return new SimpleStringProperty(kodeBarcode);
            else
                return new SimpleStringProperty("");
        });
        stokMinimalColumn.setCellValueFactory(cellData -> cellData.getValue().stokMinimalProperty());
        stokMinimalColumn.setCellFactory((c) -> getTableCell(qty));
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().qtyProperty());
        qtyColumn.setCellFactory((c) -> getTableCell(qty));
        hargaBeliColumn.setCellValueFactory(cellData -> {
            double hargaBeli = cellData.getValue().getHargaBeli()*cellData.getValue().getSatuan().getQty();
            return new SimpleDoubleProperty(hargaBeli);
        });
        hargaBeliColumn.setCellFactory((c) -> getTableCell(rp));
        hargaRetailColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().hargaRetailProperty());
        hargaRetailColumn.setCellFactory((c) -> getTableCell(rp));
        hargaGrosirColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().hargaGrosirProperty());
        hargaGrosirColumn.setCellFactory((c) -> getTableCell(rp));
        hargaGrosirBesarColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().hargaGrosirBesarProperty());
        hargaGrosirBesarColumn.setCellFactory((c) -> getTableCell(rp));
        labaGrosirColumn.setCellValueFactory(cellData -> {
            double hargaBeli = cellData.getValue().getHargaBeli()*cellData.getValue().getSatuan().getQty();
            double hargaGrosir = cellData.getValue().getSatuan().getHargaGrosir();
            return new SimpleDoubleProperty(hargaGrosir-hargaBeli);
        });
        labaGrosirColumn.setCellFactory((c) -> getTableCell(rp));
        
        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allBarang);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Barang Baru");
        addNew.setOnAction((ActionEvent e)->{
            newBarang();
        });
        MenuItem importBarang = new MenuItem("Import Data Barang");
        importBarang.setOnAction((ActionEvent e)->{
            importBarang();
        });
        MenuItem uploadDataBarang = new MenuItem("Upload Data Barang");
        uploadDataBarang.setOnAction((ActionEvent e)->{
            uploadDataBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getBarang();
        });
        rowMenu.getItems().addAll(addNew, importBarang, uploadDataBarang, refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Barang Baru");
                        addNew.setOnAction((ActionEvent e)->{
                            newBarang();
                        });
                        MenuItem edit = new MenuItem("Ubah Barang");
                        edit.setOnAction((ActionEvent e)->{
                            updateBarang(item);
                        });
                        MenuItem ubahHarga = new MenuItem("Ubah Harga");
                        ubahHarga.setOnAction((ActionEvent e)->{
                            ubahHarga(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Barang");
                        delete.setOnAction((ActionEvent e) -> {
                            deleteBarang(item);
                        });
                        MenuItem importBarang = new MenuItem("Import Data Barang");
                        importBarang.setOnAction((ActionEvent e)->{
                            importBarang();
                        });
                        MenuItem uploadDataBarang = new MenuItem("Upload Data Barang");
                        uploadDataBarang.setOnAction((ActionEvent e)->{
                            uploadDataBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getBarang();
                        });
                        rowMenu.getItems().addAll(addNew, edit, ubahHarga, delete, new SeparatorMenuItem(), importBarang, uploadDataBarang, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    ubahHarga(row.getItem());
            });
            return row;
        });
        kategoriCombo.setItems(allKategori);
        barangTable.setItems(filterData);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getKategori();
    }
    private void getKategori() {
        Task<List<KategoriBarang>> task = new Task<List<KategoriBarang>>() {
            @Override 
            public List<KategoriBarang> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    return KategoriBarangDAO.getAll(con);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            List<KategoriBarang> listKategori = task.getValue();
            allKategori.clear();
            allKategori.add("Semua");
            for(KategoriBarang k : listKategori){
                allKategori.add(k.getKodeKategori());
            }
            kategoriCombo.getSelectionModel().select("Semua");
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    @FXML
    private void getBarang() {
        Task<List<Barang>> task = new Task<List<Barang>>() {
            @Override 
            public List<Barang> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    String kategori = "%";
                    if(!kategoriCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                        kategori = kategoriCombo.getSelectionModel().getSelectedItem();
                    List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, kategori, "true");
                    List<Barang> dataBarang = new ArrayList<>();
                    for(Barang b : listBarang){
                        b.setAllSatuan(SatuanDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                        for(Satuan s : b.getAllSatuan()){
                            Barang barang = new Barang();
                            barang.setKodeKategori(b.getKodeKategori());
                            barang.setKodeBarang(b.getKodeBarang());
                            barang.setNamaBarang(b.getNamaBarang());
                            barang.setSupplier(b.getSupplier());
                            barang.setStokMinimal(b.getStokMinimal());
                            barang.setHargaBeli(b.getHargaBeli());
                            barang.setStatus(b.getStatus());
                            barang.setSatuan(s);
                            barang.setAllSatuan(b.getAllSatuan());
                            dataBarang.add(barang);
                        }
                    }
                    uploadData = listBarang;
                    return dataBarang;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allBarang.clear();
            allBarang.addAll(task.getValue());
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private Boolean checkColumn(String column) {
        return column != null && column.toLowerCase().contains(searchField.getText().toLowerCase());
    }
    private void searchBarang() {
        filterData.clear();
        allBarang.forEach((temp) -> {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                filterData.add(temp);
            } else {
                if (checkColumn(temp.getKodeBarang()) ||
                    checkColumn(temp.getKodeKategori()) ||
                    checkColumn(temp.getNamaBarang()) ||
                    checkColumn(temp.getSupplier()) ||
                    checkColumn(qty.format(temp.getStokMinimal())) ||
                    checkColumn(temp.getSatuan().getKodeBarcode()) ||
                    checkColumn(temp.getSatuan().getKodeSatuan()) ||
                    checkColumn(qty.format(temp.getSatuan().getQty())) ||
                    checkColumn(qty.format(temp.getSatuan().getHargaGrosir())) ||
                    checkColumn(qty.format(temp.getSatuan().getHargaGrosirBesar())) ||
                    checkColumn(qty.format(temp.getSatuan().getHargaRetail())) ) 
                    filterData.add(temp);
            }
        });
        totalQtyLabel.setText(rp.format(filterData.size()));
    }
    private void newBarang() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setNewBarang();
        x.saveButton.setOnAction((event) -> {
            if(x.namaBarangField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            else if(x.allSatuan.isEmpty())
                mainApp.showMessage(Modality.NONE, "Warning", "Satuan barang masih kosong");
            else {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            Barang b = new Barang();
                            b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                            b.setNamaBarang(x.namaBarangField.getText());
                            b.setSupplier(x.supplierField.getText());
                            b.setStokMinimal(Integer.parseInt(x.stokMinimalField.getText()));
                            b.setStatus("true");
                            b.setAllSatuan(x.allSatuan);
                            return Service.saveNewBarang(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        });
    }
    private void updateBarang(Barang b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(b.getKodeBarang());
        x.saveButton.setOnAction((event) -> {
            if(x.namaBarangField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            else if(x.allSatuan.isEmpty())
                mainApp.showMessage(Modality.NONE, "Warning", "Satuan barang masih kosong");
            else {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                            b.setNamaBarang(x.namaBarangField.getText());
                            b.setSupplier(x.supplierField.getText());
                            b.setStokMinimal(Integer.parseInt(x.stokMinimalField.getText().replaceAll(",", "")));
                            b.setStatus("true");
                            b.setAllSatuan(x.allSatuan);
                            return Service.saveUpdateBarang(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
                    }else{
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                    }
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        });
    }
    private void ubahHarga(Barang b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/UbahHarga.fxml");
        UbahHargaController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setHarga(b.getSatuan().getHargaRetail(), b.getSatuan().getHargaGrosir(), b.getSatuan().getHargaGrosirBesar());
        x.saveButton.setOnAction((event) -> {
            if(x.hargaRetailField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
            else if(x.hargaGrosirField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
            else if(x.hargaGrosirBesarField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir besar masih kosong");
            else {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            b.getSatuan().setHargaGrosirBesar(Double.parseDouble(x.hargaGrosirBesarField.getText().replaceAll(",", "")));
                            b.getSatuan().setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                            b.getSatuan().setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                            return Service.saveUpdateHarga(con, b.getSatuan());
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Ubah harga berhasil disimpan");
                    }else{
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                    }
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        });
    }
    private void deleteBarang(Barang barang) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus barang " + barang.getKodeBarang() + "-" + barang.getNamaBarang() + " ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deleteBarang(con, barang);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                getBarang();
                String status = task.getValue();
                if (status.equals("true")) {
                    mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil dihapus");
                } else {
                    mainApp.showMessage(Modality.NONE, "Failed", status);
                }
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        });
    }
    private void importBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/ImportBarang.fxml");
        ImportBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.saveButton.setOnAction((event) -> {
            MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                    "Import " +  x.allBarang.size() + " barang ?");
            controller.OK.setOnAction((ActionEvent ev) -> {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            return Service.saveImportBarang(con, x.allBarang);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil disimpan");
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            });
        });
    }
    private void uploadDataBarang(){
        if(uploadData!=null){
            mainApp.showMessage(Modality.NONE, "Warning", "Data barang yang akan di upload masih kosong");
        }else{
            MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                    "Upload data barang ?");
            controller.OK.setOnAction((ActionEvent ev) -> {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            return Service.saveUploadBarang(con, uploadData);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Data barang berhasil diupload");
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            });
        }
    }
}
