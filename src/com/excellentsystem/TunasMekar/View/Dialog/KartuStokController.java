/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.StokBarangDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglNormal;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.StokBarang;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class KartuStokController {

    @FXML private TableView<StokBarang> barangTable;
    @FXML private TableColumn<StokBarang, String> tanggalColumn;
    @FXML private TableColumn<StokBarang, Number> stokAwalColumn;
    @FXML private TableColumn<StokBarang, Number> stokMasukColumn;
    @FXML private TableColumn<StokBarang, Number> stokKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> stokAkhirColumn;
    
    @FXML private Label kodeBarangLabel;
    @FXML private Label namaBarangLabel;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    
    private Stage stage;
    private Main mainApp;   
    private final ObservableList<StokBarang> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        tanggalColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglNormal.format(tglBarang.parse(cellData.getValue().getTanggal())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tanggalColumn.setComparator(Function.sortDate(tglNormal));
        stokAwalColumn.setCellValueFactory(cellData -> cellData.getValue().stokAwalProperty());
        stokAwalColumn.setCellFactory(col -> getTableCell(qty));
        stokMasukColumn.setCellValueFactory(cellData -> cellData.getValue().stokMasukProperty());
        stokMasukColumn.setCellFactory(col -> getTableCell(qty));
        stokKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().stokKeluarProperty());
        stokKeluarColumn.setCellFactory(col -> getTableCell(qty));
        stokAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().stokAkhirProperty());
        stokAkhirColumn.setCellFactory(col -> getTableCell(qty));
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE).minusMonths(1));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getStokBarang();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(ttv -> {
            TableRow<StokBarang> row = new TableRow<StokBarang>() {
                @Override
                public void updateItem(StokBarang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getStokBarang();
                        });
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
    }    
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    } 
    public void setBarang(String kodeBarang, String namaBarang){
        kodeBarangLabel.setText(kodeBarang);
        namaBarangLabel.setText(namaBarang);
        getStokBarang();
    }
    @FXML
    private void getStokBarang(){
        Task<List<StokBarang>> task = new Task<List<StokBarang>>() {
            @Override 
            public List<StokBarang> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    String kodeBarang = kodeBarangLabel.getText();
                    Barang barang = BarangDAO.get(con, kodeBarang);
                    List<StokBarang> listStok = StokBarangDAO.getAllByDateAndBarang(con, 
                            tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), kodeBarang);
                    for(StokBarang stok : listStok){
                        stok.setBarang(barang);
                    }
                    return listStok;
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
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
