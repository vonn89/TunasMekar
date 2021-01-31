/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.PelangganDAO;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.DetailPelangganController;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import com.excellentsystem.TunasMekar.View.Dialog.UbahHargaController;
import java.sql.Connection;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
 * @author Excellent
 */
public class DataPelangganController  {

    @FXML private TableView<Pelanggan> pelangganTable;
    @FXML private TableColumn<Pelanggan, String> kodePelangganColumn;
    @FXML private TableColumn<Pelanggan, String> namaColumn;
    @FXML private TableColumn<Pelanggan, String> alamatColumn;
    @FXML private TableColumn<Pelanggan, String> noTelpColumn;
    @FXML private TableColumn<Pelanggan, Number> hutangColumn;
    
    @FXML private Label totalHutangLabel;
    @FXML private TextField searchField;
    private Main mainApp;
    private ObservableList<Pelanggan> allPelanggan = FXCollections.observableArrayList();
    private ObservableList<Pelanggan> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodePelangganColumn.setCellValueFactory(cellData -> cellData.getValue().kodePelangganProperty());
        namaColumn.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        alamatColumn.setCellValueFactory(cellData -> cellData.getValue().alamatProperty());
        noTelpColumn.setCellValueFactory(cellData -> cellData.getValue().noTelpProperty());
        hutangColumn.setCellValueFactory(cellData -> cellData.getValue().hutangProperty());
        hutangColumn.setCellFactory((c) -> getTableCell(rp));
        
        allPelanggan.addListener((ListChangeListener.Change<? extends Pelanggan> change) -> {
            searchPelanggan();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchPelanggan();
        });
        filterData.addAll(allPelanggan);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Pelanggan Baru");
        addNew.setOnAction((ActionEvent e)->{
            newPelanggan();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getPelanggan();
        });
        rowMenu.getItems().addAll(addNew, refresh);
        pelangganTable.setContextMenu(rowMenu);
        pelangganTable.setRowFactory(table -> {
            TableRow<Pelanggan> row = new TableRow<Pelanggan>() {
                @Override
                public void updateItem(Pelanggan item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Pelanggan Baru");
                        addNew.setOnAction((ActionEvent e)->{
                            newPelanggan();
                        });
                        MenuItem edit = new MenuItem("Ubah Pelanggan");
                        edit.setOnAction((ActionEvent e)->{
                            updatePelanggan(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Pelanggan");
                        delete.setOnAction((ActionEvent e) -> {
                            deletePelanggan(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPelanggan();
                        });
                        rowMenu.getItems().addAll(addNew, edit, delete, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    updatePelanggan(row.getItem());
            });
            return row;
        });
        pelangganTable.setItems(filterData);
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                searchField.requestFocus();
                searchField.selectAll();
            }
        });
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getPelanggan();
    }
    private void getPelanggan() {
        Task<List<Pelanggan>> task = new Task<List<Pelanggan>>() {
            @Override 
            public List<Pelanggan> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    return PelangganDAO.getAllByStatus(con,  "true");
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
    private Boolean checkColumn(String column) {
        return column != null && column.toLowerCase().contains(searchField.getText().toLowerCase());
    }
    private void searchPelanggan() {
        filterData.clear();
        allPelanggan.forEach((temp) -> {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                filterData.add(temp);
            } else {
                if (checkColumn(temp.getKodePelanggan()) ||
                    checkColumn(temp.getNama()) ||
                    checkColumn(temp.getAlamat()) ||
                    checkColumn(temp.getNoTelp()) ||
                    checkColumn(rp.format(temp.getHutang()))  ) 
                    filterData.add(temp);
            }
        });
        hitungTotal();
    }
    private void hitungTotal(){
        double total = 0;
        for(Pelanggan p : filterData){
            total = total + p.getHutang();
        }
        totalHutangLabel.setText(rp.format(total));
    }
    private void newPelanggan() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPelanggan.fxml");
        DetailPelangganController x = loader.getController();
        x.setMainApp(mainApp, stage);
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
                        mainApp.closeDialog(stage);
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
    private void updatePelanggan(Pelanggan b) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPelanggan.fxml");
        DetailPelangganController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setPelanggan(b.getKodePelanggan());
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
                            b.setNama(x.namaField.getText());
                            b.setAlamat(x.alamatField.getText());
                            b.setNoTelp(x.noTelpField.getText());
                            b.setHutang(0);
                            b.setStatus("true");
                            return Service.saveUpdatePelanggan(con, b);
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
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Data pelanggan berhasil disimpan");
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
    private void deletePelanggan(Pelanggan pelanggan) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus pelanggan " + pelanggan.getKodePelanggan() + "-" + pelanggan.getNama()+ " ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deletePelanggan(con, pelanggan);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                getPelanggan();
                String status = task.getValue();
                if (status.equals("true")) {
                    mainApp.showMessage(Modality.NONE, "Success", "Data pelanggan berhasil dihapus");
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
}
