/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.DiskonDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Diskon;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class PengaturanDiskonController  {

    @FXML private TextField kodeDiskonField;
    @FXML private TextField namaDiskonField;
    @FXML private TextField diskonRpField;
    
    @FXML private TableView<Diskon> diskonTable;
    @FXML private TableColumn<Diskon, String> kodeDiskonColumn;
    @FXML private TableColumn<Diskon, String> namaDiskonColumn;
    @FXML private TableColumn<Diskon, Number> diskonRpColumn;
    
    private ObservableList<Diskon> allDiskon = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize(){
        kodeDiskonColumn.setCellValueFactory(cellData -> cellData.getValue().kodeDiskonProperty());
        namaDiskonColumn.setCellValueFactory(cellData -> cellData.getValue().namaDiskonProperty());
        diskonRpColumn.setCellValueFactory(cellData -> cellData.getValue().diskonRpProperty());
        diskonRpColumn.setCellFactory(col -> getTableCell(rp));
        
        final ContextMenu menu = new ContextMenu();
        MenuItem tambah = new MenuItem("Tambah Diskon");
        tambah.setOnAction((ActionEvent event) -> {
            newDiskon();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getDiskon();
        });
        menu.getItems().addAll(tambah, refresh);
        diskonTable.setContextMenu(menu);
        diskonTable.setRowFactory(table -> {
            TableRow<Diskon> row = new TableRow<Diskon>(){
                @Override
                public void updateItem(Diskon item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem tambah = new MenuItem("Tambah Diskon");
                        tambah.setOnAction((ActionEvent event) -> {
                            newDiskon();
                        });
                        MenuItem hapus = new MenuItem("Hapus Diskon");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteDiskon(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getDiskon();
                        });
                        rowMenu.getItems().addAll(tambah, hapus, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        diskonTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectDiskon(newValue));
        
        Function.setNumberField(diskonRpField, rp);
    }
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        diskonTable.setItems(allDiskon);
        getDiskon();
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    private void getDiskon(){
        Task<List<Diskon>> task = new Task<List<Diskon>>() {
            @Override 
            public List<Diskon> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    return DiskonDAO.getAllByStatus(con, "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            mainApp.closeLoading();
            allDiskon.clear();
            allDiskon.addAll(task.getValue());
            kodeDiskonField.setText("");
            namaDiskonField.setText("");
            diskonRpField.setText("0");
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private Diskon diskon = null;
    private String status = "";
    private void selectDiskon(Diskon value){
        try{
            diskon=null;
            status="";
            kodeDiskonField.setText("");
            namaDiskonField.setText("");
            diskonRpField.setText("0");
            kodeDiskonField.setDisable(false);
            if(value!=null){
                status="update";
                diskon = value;
                kodeDiskonField.setDisable(true);
                kodeDiskonField.setText(diskon.getKodeDiskon());
                namaDiskonField.setText(diskon.getNamaDiskon());
                diskonRpField.setText(rp.format(diskon.getDiskonRp()));
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }    
    private void newDiskon(){
        status = "new";
        diskon = new Diskon();
        kodeDiskonField.setDisable(false);
        kodeDiskonField.setText("");
        namaDiskonField.setText("");
        diskonRpField.setText("0");
    }    
    @FXML
    private void cancel(){
        selectDiskon(null);
    }
    private void deleteDiskon(Diskon temp){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus Diskon "+temp.getKodeDiskon()+" ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deleteDiskon(con, temp);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((e) -> {
                mainApp.closeLoading();
                getDiskon();
                String s = task.getValue();
                if(s.equals("true")){
                    mainApp.showMessage(Modality.NONE, "Success", "Diskon berhasil dihapus");
                    selectDiskon(null);
                }else{
                    mainApp.showMessage(Modality.NONE, "Failed", s);
                }
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        });
    }
    @FXML
    private void saveDiskon(){
        if(status.equals("update")){
            if(diskon==null || kodeDiskonField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "Diskon belum dipilih");
            }else{
                MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                        "Update diskon "+diskon.getKodeDiskon()+" ?");
                controller.OK.setOnAction((ActionEvent evt) -> {
                    Task<String> task = new Task<String>() {
                        @Override 
                        public String call() throws Exception{
                            Thread.sleep(timeout);
                            try(Connection con = Koneksi.getConnection()){
                                diskon.setNamaDiskon(namaDiskonField.getText());
                                diskon.setDiskonPersen(0);
                                diskon.setDiskonRp(Double.parseDouble(diskonRpField.getText().replaceAll(",", "")));
                                diskon.setStatus("true");
                                return Service.saveUpdateDiskon(con, diskon);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((e) -> {
                        mainApp.closeLoading();
                        getDiskon();
                        String message = task.getValue();
                        if(message.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Data diskon berhasil disimpan");
                        }else{
                            mainApp.showMessage(Modality.NONE, "Failed", message);
                        }
                    });
                    task.setOnFailed((e) -> {
                        mainApp.closeLoading();
                        mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                    });
                    new Thread(task).start();
                });
            }
        }else if(status.equals("new")){
            if(diskon==null || kodeDiskonField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "Kode diskon masih kosong");
            }else{
                Boolean s = true;
                for(Diskon k : allDiskon){
                    if(k.getKodeDiskon().toUpperCase().equals(kodeDiskonField.getText().toUpperCase()))
                        s = false;
                }
                if(s){
                    MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                            "Save new diskon "+kodeDiskonField.getText()+" ?");
                    controller.OK.setOnAction((ActionEvent ev) -> {
                        Task<String> task = new Task<String>() {
                            @Override 
                            public String call() throws Exception{
                                Thread.sleep(timeout);
                                try(Connection con = Koneksi.getConnection()){
                                    Diskon d = new Diskon();
                                    d.setKodeDiskon(kodeDiskonField.getText().toUpperCase());
                                    d.setNamaDiskon(namaDiskonField.getText());
                                    d.setDiskonPersen(0);
                                    d.setDiskonRp(Double.parseDouble(diskonRpField.getText().replaceAll(",", "")));
                                    d.setStatus("true");
                                    return Service.saveNewDiskon(con, d);
                                }
                            }
                        };
                        task.setOnRunning((e) -> {
                            mainApp.showLoadingScreen();
                        });
                        task.setOnSucceeded((e) -> {
                            mainApp.closeLoading();
                            getDiskon();
                            String message = task.getValue();
                            if(message.equals("true")){
                                mainApp.showMessage(Modality.NONE, "Success", "Data diskon berhasil disimpan");
                            }else
                                mainApp.showMessage(Modality.NONE, "Failed", message);
                        });
                        task.setOnFailed((e) -> {
                            mainApp.closeLoading();
                            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                        });
                        new Thread(task).start();
                    });
                }else{
                    mainApp.showMessage(Modality.NONE, "Warning", "Kode diskon sudah terdaftar");
                }
            }
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
