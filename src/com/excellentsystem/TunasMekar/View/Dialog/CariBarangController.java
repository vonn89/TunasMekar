/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.Satuan;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class CariBarangController  {

    @FXML public TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML private TableColumn<Barang, String> kodeBarangColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, String> satuanColumn;
    @FXML private TableColumn<Barang, Number> hargaGrosirColumn;
    @FXML private TableColumn<Barang, Number> hargaRetailColumn;
    
    @FXML public TextField searchField;
    
    private Main mainApp;   
    private Stage stage;
    private final ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private final ObservableList<Barang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().kodeSatuanProperty());
        hargaGrosirColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().hargaGrosirProperty());
        hargaRetailColumn.setCellValueFactory(cellData -> cellData.getValue().getSatuan().hargaRetailProperty());
        hargaGrosirColumn.setCellFactory((param) -> Function.getTableCell(rp));
        hargaRetailColumn.setCellFactory((param) -> Function.getTableCell(rp));
        
        ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getBarang();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(ttv -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detailBarang = new MenuItem("Detail Barang");
                        detailBarang.setOnAction((ActionEvent e)->{
                            detailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getBarang();
                        });
                        rowMenu.getItems().addAll(detailBarang, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        barangTable.setItems(filterData);
        filterData.addAll(allBarang);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
            if (event.getCode() == KeyCode.UP) {
                if(barangTable.isFocused() && barangTable.getFocusModel().isFocused(0)){
                    Platform.runLater(() -> {
                        barangTable.getSelectionModel().clearSelection();
                        searchField.requestFocus();
                        searchField.selectAll();
                    });
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if(!barangTable.isFocused()){
                    Platform.runLater(() -> {
                        barangTable.requestFocus();
                        barangTable.getSelectionModel().select(0);
                        barangTable.getFocusModel().focus(0);
                    });
                }
            }
        });
    } 
    public void setCari(){
        getBarang();
        searchField.requestFocus();
    }
    @FXML
    private void getBarang(){
        Task<List<Barang>> task = new Task<List<Barang>>() {
            @Override 
            public List<Barang> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    List<Barang> listBarang = BarangDAO.getAllByKategoriAndStatus(con, "%", "true");
                    List<Satuan> allSatuan = SatuanDAO.getAll(con);
                    List<Barang> allBarang = new ArrayList<>();
                    for(Barang b : listBarang){
                        List<Satuan> listsatuan = new ArrayList<>();
                        for(Satuan s : allSatuan){
                            if(s.getKodeBarang().equals(b.getKodeBarang()))
                                listsatuan.add(s);
                        }
                        for(Satuan s : listsatuan){
                            Barang barang = new Barang();
                            barang.setKodeBarang(b.getKodeBarang());
                            barang.setKodeKategori(b.getKodeKategori());
                            barang.setNamaBarang(b.getNamaBarang());
                            barang.setSupplier(b.getSupplier());
                            barang.setStokMinimal(b.getStokMinimal());
                            barang.setSatuan(s);
                            barang.setAllSatuan(listsatuan);
                            allBarang.add(barang);
                        }
                    }
                    return allBarang;
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
    private Boolean checkColumn(String column){
        if(column!=null)
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        return false;
    }
    private void searchBarang() {
        try{
            filterData.clear();
            for (Barang temp : allBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeBarang())||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getSatuan().getKodeBarcode())||
                        checkColumn(temp.getSatuan().getKodeSatuan())
                    )
                    filterData.add(temp);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void detailBarang(Barang b) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(b.getKodeBarang());
        x.setViewOnly();
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
    
}
