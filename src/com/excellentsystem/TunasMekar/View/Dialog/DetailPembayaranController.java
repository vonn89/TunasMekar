/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.PembayaranDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.Pembayaran;
import com.excellentsystem.TunasMekar.Model.PembelianHead;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import static com.excellentsystem.TunasMekar.PrintOut.PrintOut.df;
import java.sql.Connection;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yunaz
 */
public class DetailPembayaranController {

    @FXML public TableView<Pembayaran> pembayaranTable;
    @FXML private TableColumn<Pembayaran, String> noPembayaranColumn;
    @FXML private TableColumn<Pembayaran, String> tglPembayaranColumn;
    @FXML private TableColumn<Pembayaran, Number> jumlahPembayaranColumn;
    
    @FXML private TextField noTransaksiField;
    @FXML private TextField tglTransaksiField;
    @FXML private TextField totalTransaksiField;
    @FXML private Label pembayaranLabel;
    @FXML private Label sisaPembayaranLabel;
    
    private ObservableList<Pembayaran> allPembayaran = FXCollections.observableArrayList();
    private Main mainApp;   
    private Stage stage;
    public void initialize() {
        noPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().noPembayaranProperty());
        tglPembayaranColumn.setCellValueFactory(cellData -> { 
            try{
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglPembayaran())));
            } catch (Exception ex) {
                return null;
            }
        });
        jumlahPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().jumlahPembayaranProperty());
        jumlahPembayaranColumn.setCellFactory(col -> Function.getTableCell(rp));
        
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        pembayaranTable.setItems(allPembayaran);
    }   
    public void setDetailPembelian(PembelianHead p){
        Task<List<Pembayaran>> task = new Task<List<Pembayaran>>() {
            @Override 
            public List<Pembayaran> call() throws Exception{
                try (Connection con = Koneksi.getConnection()) {
                    return PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPembelian(), "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            try{
                mainApp.closeLoading();
                noTransaksiField.setText(p.getNoPembelian());
                tglTransaksiField.setText(tglLengkap.format(tglSql.parse(p.getTglPembelian())));
                totalTransaksiField.setText(df.format(p.getTotalPembelian()));
                pembayaranLabel.setText(df.format(p.getPembayaran()));
                sisaPembayaranLabel.setText(df.format(p.getSisaPembayaran()));
                allPembayaran.clear();
                allPembayaran.addAll(task.getValue());
            }catch(Exception ex) {
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void setDetailPenjualan(PenjualanHead p){
        Task<List<Pembayaran>> task = new Task<List<Pembayaran>>() {
            @Override 
            public List<Pembayaran> call() throws Exception{
                try (Connection con = Koneksi.getConnection()) {
                    return PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPenjualan(), "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            try{
                mainApp.closeLoading();
                noTransaksiField.setText(p.getNoPenjualan());
                tglTransaksiField.setText(tglLengkap.format(tglSql.parse(p.getTglPenjualan())));
                totalTransaksiField.setText(df.format(p.getTotalPenjualan()));
                pembayaranLabel.setText(df.format(p.getPembayaran()));
                sisaPembayaranLabel.setText(df.format(p.getSisaPembayaran()));
                allPembayaran.clear();
                allPembayaran.addAll(task.getValue());
            }catch(Exception ex) {
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void close() {
        mainApp.closeDialog(stage);
    }
    
}
