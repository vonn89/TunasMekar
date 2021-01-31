/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.KategoriBarangDAO;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.KategoriBarang;
import com.excellentsystem.TunasMekar.Service.Service;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
 * @author yunaz
 */
public class KategoriBarangController  {

    @FXML private TableView<KategoriBarang> kategoriBarangTable;
    @FXML private TableColumn<KategoriBarang, String> kodeKategoriBarangColumn;
    
    @FXML private TextField kodeKategoriBarangField;
    private ObservableList<KategoriBarang> allKategoriBarang = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        kodeKategoriBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        
        final ContextMenu menu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            kategoriBarangTable.refresh();
        });
        menu.getItems().addAll(refresh);
        kategoriBarangTable.setContextMenu(menu);
        kategoriBarangTable.setRowFactory(table -> {
            TableRow<KategoriBarang> row = new TableRow<KategoriBarang>(){
                @Override
                public void updateItem(KategoriBarang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(menu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem hapus = new MenuItem("Hapus Kategori Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteKategoriBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            kategoriBarangTable.refresh();
                        });
                        rowMenu.getItems().addAll(hapus,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        kodeKategoriBarangField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                saveKategoriBarang();
            }
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        kategoriBarangTable.setItems(allKategoriBarang);
        getKategoriBarang();
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    private void getKategoriBarang(){
        Task<List<KategoriBarang>> task = new Task<List<KategoriBarang>>() {
            @Override 
            public List<KategoriBarang> call()throws Exception {
                try(Connection con = Koneksi.getConnection()){
                    return KategoriBarangDAO.getAll(con);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            mainApp.closeLoading();
            allKategoriBarang.clear();
            allKategoriBarang.addAll(task.getValue());
            kategoriBarangTable.refresh();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
        });
        new Thread(task).start();
    }
    private void deleteKategoriBarang(KategoriBarang temp){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus Kategori Barang "+temp.getKodeKategori()+" ?");
        controller.OK.setOnAction((ActionEvent ex) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deleteKategoriBarang(con, temp);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((e) -> {
                mainApp.closeLoading();
                getKategoriBarang();
                String status = task.getValue();
                if(status.equals("true")){
                    mainApp.showMessage(Modality.NONE, "Success", "Kategori Barang berhasil dihapus");
                    kodeKategoriBarangField.setText("");
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
    @FXML
    private void saveKategoriBarang(){
        if(kodeKategoriBarangField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori masih kosong");
        else{
            Boolean s = true;
            for(KategoriBarang k : allKategoriBarang){
                if(k.getKodeKategori().toUpperCase().equals(kodeKategoriBarangField.getText().toUpperCase()))
                    s = false;
            }
            if(s){
                KategoriBarang k = new KategoriBarang();
                k.setKodeKategori(kodeKategoriBarangField.getText().toUpperCase());
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            return Service.saveNewKategoriBarang(con, k);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getKategoriBarang();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Kategori Barang berhasil disimpan");
                        kodeKategoriBarangField.setText("");
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }else{
                mainApp.showMessage(Modality.NONE, "Warning", "Kode Kategori sudah terdaftar");
            }
        }
    } 
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
    
}
