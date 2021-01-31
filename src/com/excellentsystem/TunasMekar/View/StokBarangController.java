/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.KategoriBarangDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import com.excellentsystem.TunasMekar.DAO.StokBarangDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getDateCellDisableAfter;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.timeout;
import static com.excellentsystem.TunasMekar.Main.user;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.KategoriBarang;
import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangDetail;
import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangHead;
import com.excellentsystem.TunasMekar.Model.Satuan;
import com.excellentsystem.TunasMekar.Model.StokBarang;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.DetailBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.KartuStokController;
import com.excellentsystem.TunasMekar.View.Dialog.LogBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.PenyesuaianBarangController;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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
public class StokBarangController {
    
    @FXML private TableView<StokBarang> stokBarangTable;
    @FXML private TableColumn<StokBarang, String> kodeBarangColumn;
    @FXML private TableColumn<StokBarang, String> namaBarangColumn;
    
    @FXML private TableColumn<StokBarang, Number> stokAwalColumn;
    @FXML private TableColumn<StokBarang, Number> stokMasukColumn;
    @FXML private TableColumn<StokBarang, Number> stokKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> stokAkhirColumn;
    
    @FXML private TableView<KategoriBarang> groupTable;
    @FXML private TableColumn<KategoriBarang, String> kodeGroupColumn;
    
    @FXML private TextField searchField;
    @FXML private DatePicker tglPicker;
    @FXML private Label totalStokAwalLabel;
    @FXML private Label totalStokMasukLabel;
    @FXML private Label totalStokKeluarLabel;
    @FXML private Label totalStokAkhirLabel;
    
    private Main mainApp;   
    private final ObservableList<KategoriBarang> listGroup = FXCollections.observableArrayList();
    private final ObservableList<StokBarang> allStokBarang = FXCollections.observableArrayList();
    private final ObservableList<StokBarang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeGroupColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        stokAwalColumn.setCellValueFactory(cellData -> cellData.getValue().stokAwalProperty());
        stokAwalColumn.setCellFactory(col -> getTableCell(qty));
        stokMasukColumn.setCellValueFactory(cellData -> cellData.getValue().stokMasukProperty());
        stokMasukColumn.setCellFactory(col -> getTableCell(qty));
        stokKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().stokKeluarProperty());
        stokKeluarColumn.setCellFactory(col -> getTableCell(qty));
        stokAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().stokAkhirProperty());
        stokAkhirColumn.setCellFactory(col -> getTableCell(qty));
        
        tglPicker.setConverter(Function.getTglConverter());
        tglPicker.setValue(LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE));
        tglPicker.setDayCellFactory((final DatePicker datePicker) -> getDateCellDisableAfter(LocalDate.parse(sistem.getTglSystem())));
        
        ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getStokBarang();
        });
        rowMenu.getItems().addAll(refresh);
        stokBarangTable.setContextMenu(rowMenu);
        stokBarangTable.setRowFactory(ttv -> {
            TableRow<StokBarang> row = new TableRow<StokBarang>() {
                @Override
                public void updateItem(StokBarang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detailBarang = new MenuItem("Detail Barang");
                        detailBarang.setOnAction((ActionEvent e)->{
                            detailBarang(item.getBarang());
                        });
                        MenuItem cekKartuStok = new MenuItem("Lihat Kartu Stok Barang");
                        cekKartuStok.setOnAction((ActionEvent e)->{
                            showKartuStok(item);
                        });
                        MenuItem logBarang = new MenuItem("Lihat Log Barang");
                        logBarang.setOnAction((ActionEvent e)->{
                            showLogBarang(item);
                        });
                        MenuItem penyesuaianBarang = new MenuItem("Penyesuaian Stok Barang");
                        penyesuaianBarang.setOnAction((ActionEvent e)->{
                            showPenyesuaianStok(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getStokBarang();
                        });
                        rowMenu.getItems().addAll(detailBarang, cekKartuStok, logBarang, 
                                new SeparatorMenuItem(), penyesuaianBarang, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    showPenyesuaianStok(row.getItem());
            });
            return row;
        });
        ContextMenu grouprowMenu = new ContextMenu();
        MenuItem refresh2 = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getStokBarang();
        });
        grouprowMenu.getItems().addAll(refresh2);
        groupTable.setContextMenu(grouprowMenu);
        
        groupTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> getStokBarang());
        allStokBarang.addListener((ListChangeListener.Change<? extends StokBarang> change) -> {
            searchStokBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchStokBarang();
        });
        stokBarangTable.setItems(filterData);
        filterData.addAll(allStokBarang);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        groupTable.setItems(listGroup);
        setListView();
    } 
    @FXML
    private void setListView(){
        Task<List<KategoriBarang>> task = new Task<List<KategoriBarang>>() {
            @Override 
            public List<KategoriBarang> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    List<KategoriBarang> temp = new ArrayList<>();
                    KategoriBarang k = new KategoriBarang();
                    k.setKodeKategori("Semua");
                    temp.add(k);
                    
                    List<KategoriBarang> listKategori = KategoriBarangDAO.getAll(con);
                    for(KategoriBarang g : listKategori){
                        KategoriBarang x = new KategoriBarang();
                        x.setKodeKategori(g.getKodeKategori());
                        temp.add(x);
                    }
                    return temp;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            listGroup.clear();
            listGroup.addAll(task.getValue());
            groupTable.getSelectionModel().selectFirst();
        });
        task.setOnFailed((e) -> {
            task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    @FXML
    private void getStokBarang(){
        Task<List<StokBarang>> task = new Task<List<StokBarang>>() {
            @Override 
            public List<StokBarang> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, "%", "%");
                    List<StokBarang> stokAkhir = StokBarangDAO.getAllByTanggalAndBarang(con, tglPicker.getValue().toString(), "%");
                    for(StokBarang stok : stokAkhir){
                        for(Barang b :listBarang){
                            if(stok.getKodeBarang().equals(b.getKodeBarang()))
                                stok.setBarang(b);
                        }
                        if(stok.getBarang()==null)
                            System.out.println(stok.getKodeBarang());
                    }
                    return stokAkhir;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allStokBarang.clear();
            allStokBarang.addAll(task.getValue());
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            task.getException().printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private Boolean checkColumn(String column){
        if(column!=null)
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        return false;
    }
    private void search(StokBarang temp){
        if (searchField.getText() == null || searchField.getText().equals(""))
            filterData.add(temp);
        else{
            if(checkColumn(temp.getKodeBarang())||
                checkColumn(temp.getBarang().getKodeKategori())||
                checkColumn(temp.getBarang().getNamaBarang())||
                checkColumn(temp.getBarang().getSupplier())
            )
            filterData.add(temp);
        }
    }
    private void searchStokBarang() {
        try{
            filterData.clear();
            for (StokBarang temp : allStokBarang) {
                String kategori = groupTable.getSelectionModel().getSelectedItem().getKodeKategori();
                if(kategori.equals("Semua") || temp.getBarang().getKodeKategori().equals(kategori))
                    search(temp);
            }
            hitungTotal();
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void hitungTotal(){
        double stokAwal = 0;
        double stokMasuk = 0;
        double stokKeluar = 0;
        double stokAkhir = 0;
        for(StokBarang b : filterData){
            stokAwal = stokAwal + b.getStokAwal();
            stokMasuk = stokMasuk + b.getStokMasuk();
            stokKeluar = stokKeluar + b.getStokKeluar();
            stokAkhir = stokAkhir + b.getStokAkhir();
        }
        totalStokAwalLabel.setText(qty.format(stokAwal));
        totalStokMasukLabel.setText(qty.format(stokMasuk));
        totalStokKeluarLabel.setText(qty.format(stokKeluar));
        totalStokAkhirLabel.setText(qty.format(stokAkhir));
    }  
    private void detailBarang(Barang b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(b.getKodeBarang());
        x.setViewOnly();
    }
    private void showKartuStok(StokBarang s){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/KartuStok.fxml");
        KartuStokController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(s.getKodeBarang(), s.getBarang().getNamaBarang());
    }
    private void showLogBarang(StokBarang s){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/LogBarang.fxml");
        LogBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(s.getKodeBarang(), s.getBarang().getNamaBarang());
    }
    private void showPenyesuaianStok(StokBarang s){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/PenyesuaianBarang.fxml");
        PenyesuaianBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(s.getBarang());
        x.saveButton.setOnAction((event) -> {
            if(x.kodeBarangField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Barang belum dipilih");
            else if (Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Qty tidak boleh kosong");
            else if (x.jenisPenyesuaianCombo.getSelectionModel().getSelectedItem() == null) 
                mainApp.showMessage(Modality.NONE, "Warning", "Jenis penyesuaian stok belum dipilih");
            else if (x.satuanCombo.getSelectionModel().getSelectedItem() == null) 
                mainApp.showMessage(Modality.NONE, "Warning", "Satuan belum dipilih");
            else {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            Satuan satuan = SatuanDAO.get(con, x.kodeBarangField.getText(), x.satuanCombo.getSelectionModel().getSelectedItem());
                            
                            PenyesuaianStokBarangHead p = new PenyesuaianStokBarangHead();
                            p.setTglPenyesuaian(Function.getSystemDate());
                            p.setJenisPenyesuaian(x.jenisPenyesuaianCombo.getSelectionModel().getSelectedItem());
                            p.setCatatan(x.catatanField.getText());
                            p.setKodeUser(user.getKodeUser());
                            p.setStatus("true");
                            p.setTglBatal("2000-01-01 00:00:00");
                            p.setUserBatal("");
                            
                            List<PenyesuaianStokBarangDetail> allDetail = new ArrayList<>();
                            PenyesuaianStokBarangDetail detail = new PenyesuaianStokBarangDetail();
                            detail.setNoPenyesuaian("");
                            detail.setNoUrut(1);
                            detail.setKodeBarang(x.kodeBarangField.getText());
                            detail.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                            detail.setQtyStok(Function.pembulatan(detail.getQty()*satuan.getQty()));
                            detail.setSatuan(x.satuanCombo.getSelectionModel().getSelectedItem());
                            detail.setNilai(Double.parseDouble(x.nilaiField.getText().replaceAll(",", "")));
                            detail.setTotalNilai(Function.pembulatan(detail.getQty()*detail.getNilai()));
                            allDetail.add(detail);
                            
                            p.setListPenyesuaianStokBarangDetail(allDetail);
                            return Service.savePenyesuaianStokBarang(con, p);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getStokBarang();
                    String status = task.getValue();
                    if (status.equals("true")) {
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Penyesuaian stok barang berhasil disimpan");
                    } else 
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
}
