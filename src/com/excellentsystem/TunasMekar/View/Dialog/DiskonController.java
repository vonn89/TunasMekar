/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.DiskonDAO;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Diskon;
import com.excellentsystem.TunasMekar.Model.PenjualanDiskon;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import com.excellentsystem.TunasMekar.View.Dialog.TambahDiskonController;
import java.sql.Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
 * @author ASUS
 */
public class DiskonController  {

    @FXML private TableView<PenjualanDiskon> diskonTable;
    @FXML private TableColumn<PenjualanDiskon, String> namaDiskonColumn;
    @FXML private TableColumn<PenjualanDiskon, Number> qtyColumn;
    @FXML private TableColumn<PenjualanDiskon, Number> totalDiskonColumn;
    @FXML private TextField totalDiskonField;
    
    @FXML private TextField searchField;
    
    private Stage stage;
    private Main mainApp;   
    public ObservableList<PenjualanDiskon> allDiskon = FXCollections.observableArrayList();
    public void initialize() {
        namaDiskonColumn.setCellValueFactory(cellData -> cellData.getValue().namaDiskonProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        totalDiskonColumn.setCellValueFactory(cellData -> cellData.getValue().totalDiskonProperty());
        totalDiskonColumn.setCellFactory(col -> getTableCell(rp));
        
        final ContextMenu rmCart = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            diskonTable.refresh();
        });
        rmCart.getItems().addAll(refresh);
        diskonTable.setContextMenu(rmCart);
        diskonTable.setRowFactory(ttv -> {
            TableRow<PenjualanDiskon> row = new TableRow<PenjualanDiskon>() {
                @Override
                public void updateItem(PenjualanDiskon item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rmCart);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem hapus = new MenuItem("Hapus Diskon");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusDiskon(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            diskonTable.refresh();
                        });
                        rm.getItems().addAll(hapus, refresh);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
        diskonTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.DELETE) {
                if(diskonTable.getSelectionModel().getSelectedItem()!=null)
                    hapusDiskon(diskonTable.getSelectionModel().getSelectedItem());
            }
        });
        searchField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                tambahDiskon(searchField.getText());
            }
        });
    }    
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        diskonTable.setItems(allDiskon);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                if(!searchField.isFocused()){
                    searchField.requestFocus();
                    searchField.selectAll();
                }
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
            if (event.getCode() == KeyCode.UP) {
                if(diskonTable.isFocused() ){
                    if(diskonTable.getFocusModel().isFocused(0)||diskonTable.getSelectionModel().isEmpty()){
                        Platform.runLater(() -> {
                            diskonTable.getSelectionModel().clearSelection();
                            searchField.requestFocus();
                            searchField.selectAll();
                        });
                    }
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                if(!diskonTable.isFocused()){
                    Platform.runLater(() -> {
                        diskonTable.requestFocus();
                        diskonTable.getSelectionModel().select(0);
                        diskonTable.getFocusModel().focus(0);
                    });
                }
            }
        });
    } 
    public void setDiskon(ObservableList<PenjualanDiskon> listDiskonPenjualan){
        allDiskon.clear();
        allDiskon.addAll(listDiskonPenjualan);
        hitungtotal();
    }
    private void tambahDiskon(String kodeDiskon){
        Task<Diskon> task = new Task<Diskon>() {
            @Override 
            public Diskon call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    return DiskonDAO.get(con, kodeDiskon);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            Diskon diskon = task.getValue();
            if(diskon==null){
                mainApp.showMessage(Modality.NONE, "Warning", "Kode diskon tidak ditemukan");
            }else{
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/TambahDiskon.fxml");
                TambahDiskonController x = loader.getController();
                x.setMainApp(mainApp, child);
                x.setDiskon(diskon);
                x.qtyField.setOnKeyPressed((event) -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        boolean status = true;
                        for(PenjualanDiskon s : allDiskon){
                            if(s.getKodeDiskon().equals(diskon.getKodeDiskon())){
                                s.setQty(s.getQty()+Integer.parseInt(x.qtyField.getText().replaceAll(",", "")));
                                s.setTotalDiskon(s.getDiskonRp()*s.getQty());
                                status = false;
                            }
                        }
                        if(status){
                            PenjualanDiskon d = new PenjualanDiskon();
                            d.setNoPenjualan("");
                            d.setKodeDiskon(diskon.getKodeDiskon());
                            d.setNamaDiskon(x.namaDiskonField.getText());
                            d.setDiskonPersen(0);
                            d.setDiskonRp(Double.parseDouble(x.diskonRpField.getText().replaceAll(",", "")));
                            d.setQty(Integer.parseInt(x.qtyField.getText().replaceAll(",", "")));
                            d.setTotalDiskon(Double.parseDouble(x.totalDiskonField.getText().replaceAll(",", "")));
                            allDiskon.add(d);
                        }
                        hitungtotal();
                        mainApp.closeDialog(child);
                        searchField.requestFocus();
                        searchField.selectAll();
                    }
                });
            }
        });
        task.setOnFailed((e) -> {
            task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
        
    }
    private void hapusDiskon(PenjualanDiskon d){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus diskon "+d.getNamaDiskon()+" ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            allDiskon.remove(d);
            hitungtotal();
            mainApp.closeMessage();
        });
    }
    private void hitungtotal(){
        double total = 0;
        for(PenjualanDiskon d : allDiskon){
            total = total + d.getTotalDiskon();
        }
        totalDiskonField.setText(rp.format(total));
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
