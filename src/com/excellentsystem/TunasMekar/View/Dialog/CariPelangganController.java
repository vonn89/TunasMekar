/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.PelangganDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import com.excellentsystem.TunasMekar.Service.Service;
import java.sql.Connection;
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
public class CariPelangganController  {

    @FXML public TableView<Pelanggan> pelangganTable;
    @FXML private TableColumn<Pelanggan, String> kodePelangganColumn;
    @FXML private TableColumn<Pelanggan, String> namaColumn;
    @FXML private TableColumn<Pelanggan, String> alamatColumn;
    @FXML private TableColumn<Pelanggan, String> noTelpColumn;
    @FXML private TableColumn<Pelanggan, Number> hutangColumn;
    
    @FXML public TextField searchField;
    
    private Main mainApp;   
    private Stage stage;
    private final ObservableList<Pelanggan> allPelanggan = FXCollections.observableArrayList();
    private final ObservableList<Pelanggan> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodePelangganColumn.setCellValueFactory(cellData -> cellData.getValue().kodePelangganProperty());
        namaColumn.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        alamatColumn.setCellValueFactory(cellData -> cellData.getValue().alamatProperty());
        noTelpColumn.setCellValueFactory(cellData -> cellData.getValue().noTelpProperty());
        hutangColumn.setCellValueFactory(cellData -> cellData.getValue().hutangProperty());
        hutangColumn.setCellFactory((param) -> Function.getTableCell(rp));
        
        ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getPelanggan();
        });
        rowMenu.getItems().addAll(refresh);
        pelangganTable.setContextMenu(rowMenu);
        pelangganTable.setRowFactory(ttv -> {
            TableRow<Pelanggan> row = new TableRow<Pelanggan>() {
                @Override
                public void updateItem(Pelanggan item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detailBarang = new MenuItem("Detail Pelanggan");
                        detailBarang.setOnAction((ActionEvent e)->{
                            detailPelanggan(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getPelanggan();
                        });
                        rowMenu.getItems().addAll(detailBarang, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        allPelanggan.addListener((ListChangeListener.Change<? extends Pelanggan> change) -> {
            searchPelanggan();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchPelanggan();
        });
        pelangganTable.setItems(filterData);
        filterData.addAll(allPelanggan);
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
            if (event.getCode() == KeyCode.F12) {
                newPelanggan();
            }
            if (event.getCode() == KeyCode.UP) {
                if(pelangganTable.isFocused() && pelangganTable.getFocusModel().isFocused(0)){
                    Platform.runLater(() -> {
                        pelangganTable.getSelectionModel().clearSelection();
                        searchField.requestFocus();
                        searchField.selectAll();
                    });
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if(!pelangganTable.isFocused()){
                    Platform.runLater(() -> {
                        pelangganTable.requestFocus();
                        pelangganTable.getSelectionModel().select(0);
                        pelangganTable.getFocusModel().focus(0);
                    });
                }
            }
        });
        getPelanggan();
        searchField.requestFocus();
    } 
    @FXML
    private void getPelanggan(){
        Task<List<Pelanggan>> task = new Task<List<Pelanggan>>() {
            @Override 
            public List<Pelanggan> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    return PelangganDAO.getAllByStatus(con, "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allPelanggan.clear();
            allPelanggan.addAll(task.getValue());
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
    private void searchPelanggan() {
        try{
            filterData.clear();
            for (Pelanggan temp : allPelanggan) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodePelanggan())||
                        checkColumn(temp.getNama())||
                        checkColumn(temp.getAlamat())||
                        checkColumn(temp.getNoTelp())||
                        checkColumn(rp.format(temp.getHutang()))
                    )
                    filterData.add(temp);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void newPelanggan() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailPelanggan.fxml");
        DetailPelangganController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setNewPelanggan();
        x.saveButton.setOnAction((event) -> {
            if(x.kodePelangganField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Kode pelanggan masih kosong");
            else if(x.namaField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama masih kosong");
            else if(x.alamatField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Alamat masih kosong");
            else if(x.noTelpField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "No telp masih kosong");
            else {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            Pelanggan b = new Pelanggan();
                            b.setKodePelanggan(x.kodePelangganField.getText());
                            b.setNama(x.namaField.getText());
                            b.setAlamat(x.alamatField.getText());
                            b.setNoTelp(x.noTelpField.getText());
                            b.setHutang(0);
                            b.setStatus("true");
                            return Service.saveNewPelanggan(con, b);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getPelanggan();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(child);
                        mainApp.showMessage(Modality.NONE, "Success", "Data pelanggan berhasil disimpan");
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
    private void detailPelanggan(Pelanggan p) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailPelanggan.fxml");
        DetailPelangganController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setPelanggan(p.getKodePelanggan());
        x.setViewOnly();
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
    
}
