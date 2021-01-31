/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.LogBarangDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.LogBarang;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class LogBarangController  {

    @FXML private TableView<LogBarang> barangTable;
    @FXML private TableColumn<LogBarang, String> tanggalColumn;
    @FXML private TableColumn<LogBarang, String> kategoriColumn;
    @FXML private TableColumn<LogBarang, String> keteranganColumn;
    @FXML private TableColumn<LogBarang, Number> stokAwalColumn;
    @FXML private TableColumn<LogBarang, Number> stokMasukColumn;
    @FXML private TableColumn<LogBarang, Number> stokKeluarColumn;
    @FXML private TableColumn<LogBarang, Number> stokAkhirColumn;
    @FXML private TableColumn<LogBarang, Number> nilaiAwalColumn;
    @FXML private TableColumn<LogBarang, Number> nilaiMasukColumn;
    @FXML private TableColumn<LogBarang, Number> nilaiKeluarColumn;
    @FXML private TableColumn<LogBarang, Number> nilaiAkhirColumn;
    
    @FXML private Label kodeBarangLabel;
    @FXML private Label namaBarangLabel;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    
    private Stage stage;
    private Main mainApp;   
    private final ObservableList<LogBarang> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        tanggalColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTanggal())));
            } catch (Exception ex) {
                return null;
            }
        });
        tanggalColumn.setComparator(Function.sortDate(tglLengkap));
        kategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kategoriProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        
        stokAwalColumn.setCellValueFactory(cellData -> cellData.getValue().stokAwalProperty());
        stokAwalColumn.setCellFactory(col -> getTableCell(qty));
        stokMasukColumn.setCellValueFactory(cellData -> cellData.getValue().stokMasukProperty());
        stokMasukColumn.setCellFactory(col -> getTableCell(qty));
        stokKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().stokKeluarProperty());
        stokKeluarColumn.setCellFactory(col -> getTableCell(qty));
        stokAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().stokAkhirProperty());
        stokAkhirColumn.setCellFactory(col -> getTableCell(qty));
        nilaiAwalColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiAwalProperty());
        nilaiAwalColumn.setCellFactory(col -> getTableCell(rp));
        nilaiMasukColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiMasukProperty());
        nilaiMasukColumn.setCellFactory(col -> getTableCell(rp));
        nilaiKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiKeluarProperty());
        nilaiKeluarColumn.setCellFactory(col -> getTableCell(rp));
        nilaiAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiAkhirProperty());
        nilaiAkhirColumn.setCellFactory(col -> getTableCell(rp));
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu rm = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            getLogBarang();
        });
        rm.getItems().addAll(refresh);
        barangTable.setContextMenu(rm);
        barangTable.setRowFactory(ttv -> {
            TableRow<LogBarang> row = new TableRow<LogBarang>() {
                @Override
                public void updateItem(LogBarang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Penjualan");
                        detail.setOnAction((ActionEvent e) -> {
                            detailPenjualan(item);
                        });
                        MenuItem detailPenyesuaian = new MenuItem("Detail Penyesuaian Stok Barang");
                        detailPenyesuaian.setOnAction((ActionEvent event) -> {
                            showDetailPenyesuaianStok(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getLogBarang();
                        });
                        if(item.getKategori().startsWith("Penjualan") || item.getKategori().startsWith("Batal Penjualan"))
                            rm.getItems().add(detail);
                        if(item.getKategori().equals("Penyesuaian Stok Barang"))
                            rm.getItems().add(detailPenyesuaian);
                        rm.getItems().addAll(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    if(row.getItem().getKategori().startsWith("Penjualan") || row.getItem().getKategori().startsWith("Batal Penjualan"))
                        detailPenjualan(row.getItem());
                    else if(row.getItem().getKategori().equals("Penyesuaian Stok Barang"))
                        showDetailPenyesuaianStok(row.getItem());
            });
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
        getLogBarang();
    }
    @FXML
    private void getLogBarang(){
        Task<List<LogBarang>> task = new Task<List<LogBarang>>() {
            @Override 
            public List<LogBarang> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    String barang = kodeBarangLabel.getText();
                    Barang b = BarangDAO.get(con, barang);
                    List<LogBarang> allStok = LogBarangDAO.getAllByDateAndBarang(con, 
                            tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), barang);
                    for(LogBarang l : allStok){
                        l.setBarang(b);
                    }
                    return allStok;
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
            allBarang.sort(Comparator.comparing(LogBarang::getTanggal));
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private void detailPenjualan(LogBarang log){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setDetailPenjualan(log.getKeterangan());
    }
    private void showDetailPenyesuaianStok(LogBarang log){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child ,"View/Dialog/DetailPenyesuaianStokBarang.fxml");
        DetailPenyesuaianStokBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setPenyesuaianStokBarang(log.getKeterangan());
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
