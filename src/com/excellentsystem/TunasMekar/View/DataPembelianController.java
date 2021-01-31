/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.PembayaranDAO;
import com.excellentsystem.TunasMekar.DAO.PembelianDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PembelianHeadDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import static com.excellentsystem.TunasMekar.Function.pembulatan;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglNormal;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import static com.excellentsystem.TunasMekar.Main.user;
import com.excellentsystem.TunasMekar.Model.Pembayaran;
import com.excellentsystem.TunasMekar.Model.PembelianDetail;
import com.excellentsystem.TunasMekar.Model.PembelianHead;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.DetailPembayaranController;
import com.excellentsystem.TunasMekar.View.Dialog.DetailPembelianController;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import com.excellentsystem.TunasMekar.View.Dialog.PembayaranController;
import com.excellentsystem.TunasMekar.View.Dialog.VerifikasiController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.DatePicker;
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
public class DataPembelianController  {

    @FXML private TableView<PembelianHead> pembelianHeadTable;
    @FXML private TableColumn<PembelianHead, String> noPembelianColumn;
    @FXML private TableColumn<PembelianHead, String> tglPembelianColumn;
    @FXML private TableColumn<PembelianHead, String> supplierColumn;
    @FXML private TableColumn<PembelianHead, String> paymentTermColumn;
    @FXML private TableColumn<PembelianHead, String> jatuhTempoColumn;
    @FXML private TableColumn<PembelianHead, Number> totalPembelianColumn;
    @FXML private TableColumn<PembelianHead, Number> pembayaranColumn;
    @FXML private TableColumn<PembelianHead, Number> sisaPembayaranColumn;
    @FXML private TableColumn<PembelianHead, String> kodeUserColumn;
    
    @FXML private Label totalPembelianLabel;
    @FXML private Label totalPembayaranLabel;
    @FXML private Label totalSisaPembayaranLabel;
    
    @FXML private TextField searchField;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    
    private Main mainApp;   
    private final ObservableList<PembelianHead> allPembelian = FXCollections.observableArrayList();
    private final ObservableList<PembelianHead> filterData = FXCollections.observableArrayList();
    public void initialize() { 
        noPembelianColumn.setCellValueFactory(cellData -> cellData.getValue().noPembelianProperty());
        noPembelianColumn.setCellFactory(col -> Function.getWrapTableCell(noPembelianColumn));
        
        tglPembelianColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglPembelian())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglPembelianColumn.setComparator(Function.sortDate(tglLengkap));
        tglPembelianColumn.setCellFactory(col -> Function.getWrapTableCell(tglPembelianColumn));
        
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        supplierColumn.setCellFactory(col -> Function.getWrapTableCell(supplierColumn));
        
        paymentTermColumn.setCellValueFactory(cellData -> cellData.getValue().paymentTermProperty());
        paymentTermColumn.setCellFactory(col -> Function.getWrapTableCell(paymentTermColumn));
        
        jatuhTempoColumn.setCellValueFactory(cellData -> cellData.getValue().jatuhTempoProperty());
        jatuhTempoColumn.setCellFactory(col -> Function.getWrapTableCell(jatuhTempoColumn));
        
        totalPembelianColumn.setCellValueFactory(cellData -> cellData.getValue().totalPembelianProperty());
        totalPembelianColumn.setCellFactory(col -> getTableCell(rp));
        
        pembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().pembayaranProperty());
        pembayaranColumn.setCellFactory(col -> getTableCell(rp));
        
        sisaPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().sisaPembayaranProperty());
        sisaPembayaranColumn.setCellFactory(col -> getTableCell(rp));
        
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());
        kodeUserColumn.setCellFactory(col -> Function.getWrapTableCell(kodeUserColumn));
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("New Pembelian");
        addNew.setOnAction((ActionEvent e) -> {
            newPembelian();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getPembelian();
        });
        rowMenu.getItems().add(addNew);
        rowMenu.getItems().addAll(refresh);
        pembelianHeadTable.setContextMenu(rowMenu);
        pembelianHeadTable.setRowFactory(table -> {
            TableRow<PembelianHead> row = new TableRow<PembelianHead>() {
                @Override
                public void updateItem(PembelianHead item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("New Pembelian");
                        addNew.setOnAction((ActionEvent e) -> {
                            newPembelian();
                        });
                        MenuItem detail = new MenuItem("Detail Pembelian");
                        detail.setOnAction((ActionEvent e) -> {
                            detailPembelian(item);
                        });
                        MenuItem edit = new MenuItem("Edit Pembelian");
                        edit.setOnAction((ActionEvent e) -> {
                            editPembelian(item);
                        });
                        MenuItem batal = new MenuItem("Batal Pembelian");
                        batal.setOnAction((ActionEvent e) -> {
                            batalPembelian(item);
                        });
                        MenuItem detailBayar = new MenuItem("Detail Pembayaran");
                        detailBayar.setOnAction((ActionEvent e)->{
                            detailPembayaran(item);
                        });
                        MenuItem bayar = new MenuItem("Pembayaran Pembelian");
                        bayar.setOnAction((ActionEvent e) -> {
                            pembayaran(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPembelian();
                        });
                        rowMenu.getItems().add(addNew);
                        rowMenu.getItems().add(detail);
                        rowMenu.getItems().add(edit);
                        rowMenu.getItems().add(batal);
                        rowMenu.getItems().add(detailBayar);
                        rowMenu.getItems().add(bayar);
                        rowMenu.getItems().add(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        detailPembelian(row.getItem());
                    }
                }
            });
            return row;
        });
        allPembelian.addListener((ListChangeListener.Change<? extends PembelianHead> change) -> {
            searchPembelian();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchPembelian();
        });
        filterData.addAll(allPembelian);
        pembelianHeadTable.setItems(filterData);
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        getPembelian();
    } 
    @FXML
    private void getPembelian(){
        Task<List<PembelianHead>> task = new Task<List<PembelianHead>>() {
            @Override 
            public List<PembelianHead> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    return PembelianHeadDAO.getAllByDateAndStatus(
                        con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allPembelian.clear();
            allPembelian.addAll(task.getValue());
            pembelianHeadTable.refresh();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private Boolean checkColumn(String column){
        if(column!=null){
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        }
        return false;
    }
    private void searchPembelian() {
        try{
            filterData.clear();
            for (PembelianHead temp : allPembelian) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getNoPembelian())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getTglPembelian())))||
                        checkColumn(temp.getSupplier())||
                        checkColumn(temp.getPaymentTerm())||
                        checkColumn(tglNormal.format(tglBarang.parse(temp.getJatuhTempo())))||
                        checkColumn(rp.format(temp.getTotalPembelian()))||
                        checkColumn(rp.format(temp.getPembayaran()))||
                        checkColumn(rp.format(temp.getSisaPembayaran()))||
                        checkColumn(temp.getKodeUser()))
                        filterData.add(temp);
                }
            }
            hitungTotal();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void hitungTotal(){
        double totalPembelian = 0;
        double totalPembayaran = 0;
        double totalSisaPembayaran = 0;
        for(PembelianHead d : filterData){
            totalPembelian = totalPembelian + d.getTotalPembelian();
            totalPembayaran = totalPembayaran + d.getPembayaran();
            totalSisaPembayaran = totalSisaPembayaran + d.getSisaPembayaran();
        }
        totalPembelianLabel.setText(rp.format(totalPembelian));
        totalPembayaranLabel.setText(rp.format(totalPembayaran));
        totalSisaPembayaranLabel.setText(rp.format(totalSisaPembayaran));
    }
    private void newPembelian(){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage ,stage, "View/Dialog/DetailPembelian.fxml");
        DetailPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setNewPembelian();
        controller.saveButton.setOnAction((event) -> {
            if(controller.allBarang.isEmpty()){
                mainApp.showMessage(Modality.NONE, "Warning", "Barang masih kosong");
                searchField.requestFocus();
            }else{
                PembelianHead p = new PembelianHead();
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        try(Connection con = Koneksi.getConnection()){
                            p.setNoPembelian("");
                            p.setTglPembelian(Function.getSystemDate());
                            p.setSupplier(controller.supplierField.getText());
                            p.setPaymentTerm(controller.paymentTermField.getText());
                            p.setJatuhTempo(controller.jatuhTempoPicker.getValue().toString());
;
                            p.setTotalPembelian(Double.parseDouble(controller.totalPembelianField.getText().replaceAll(",", "")));
                            p.setPembayaran(0);
                            p.setSisaPembayaran(Double.parseDouble(controller.totalPembelianField.getText().replaceAll(",", "")));
                            p.setKodeUser(user.getKodeUser());
                            p.setStatus("true");
                            p.setTglBatal("2000-01-01 00:00:00");
                            p.setUserBatal("");
                            List<PembelianDetail> allDetail = new ArrayList<>();
                            int i = 1;
                            for(PembelianDetail d : controller.allBarang){
                                PembelianDetail detail = new PembelianDetail();
                                detail.setBarang(d.getBarang());
                                detail.setNoPembelian("");
                                detail.setNoUrut(i);
                                detail.setKodeBarang(d.getKodeBarang());
                                detail.setNamaBarang(d.getNamaBarang());
                                detail.setSatuan(d.getSatuan());
                                detail.setQty(d.getQty());
                                detail.setQtyMasuk(pembulatan(d.getQty()*d.getBarang().getSatuan().getQty()));
                                detail.setHargaBeli(d.getHargaBeli());
                                detail.setHargaPpn(d.getHargaPpn());
                                detail.setTotal(detail.getHargaPpn()*detail.getQty());
                                i++;
                                allDetail.add(detail);
                            }
                            p.setListPembelianDetail(allDetail);
                            return Service.savePembelian(con, p);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((evt) -> {
                    mainApp.closeLoading();
                    getPembelian();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Pembelian berhasil disimpan");
                        mainApp.closeDialog(stage);
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
    private void detailPembelian(PembelianHead p){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage ,stage, "View/Dialog/DetailPembelian.fxml");
        DetailPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setDetailPembelian(p.getNoPembelian());
    }
    private void editPembelian(PembelianHead p){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage ,stage, "View/Dialog/DetailPembelian.fxml");
        DetailPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setEditPembelian(p.getNoPembelian());
        controller.saveButton.setOnAction((event) -> {
            if(p.getPembayaran()>Double.parseDouble(controller.totalPembelianField.getText().replaceAll(",", ""))){
                mainApp.showMessage(Modality.NONE, "Warning", "Pembayaran yang sudah diterima melebihi total pembelian yang di edit");
            }else{
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        try(Connection con = Koneksi.getConnection()){
                            String status = "true";
                            p.setStatus("false");
                            p.setTglBatal(Function.getSystemDate());
                            p.setUserBatal(user.getKodeUser());

                            PembelianHead pbNew = new PembelianHead();
                            pbNew.setNoPembelian("");
                            pbNew.setTglPembelian(Function.getSystemDate());
                            pbNew.setSupplier(controller.supplierField.getText());
                            pbNew.setTotalPembelian(Double.parseDouble(controller.totalPembelianField.getText().replaceAll(",", "")));
                            pbNew.setPaymentTerm(controller.paymentTermField.getText());
                            pbNew.setJatuhTempo(controller.jatuhTempoPicker.getValue().toString());

                            List<Pembayaran> listPembayaran = new ArrayList<>();
                            Pembayaran pp = new Pembayaran();
                            pp.setJenisPembayaran("Cash");
                            pp.setKeterangan("");
                            pp.setJumlahPembayaran(p.getPembayaran());
                            listPembayaran.add(pp);
                            pbNew.setListPembayaran(listPembayaran);

                            pbNew.setPembayaran(p.getPembayaran());
                            pbNew.setSisaPembayaran(Double.parseDouble(controller.totalPembelianField.getText().replaceAll(",", "")) - p.getPembayaran());
                            pbNew.setKodeUser(user.getKodeUser());
                            pbNew.setStatus("true");
                            pbNew.setTglBatal("2000-01-01 00:00:00");
                            pbNew.setUserBatal("");

                            List<PembelianDetail> allDetail = new ArrayList<>();
                            int i = 1;
                            for(PembelianDetail d : controller.allBarang){
                                if(d.getBarang()==null){
                                    status = d.getKodeBarang()+"-"+d.getNamaBarang()+" tidak ditemukan";
                                }else if(d.getBarang().getSatuan()==null){
                                    status = d.getKodeBarang()+"-"+d.getNamaBarang()+" satuan "+d.getSatuan()+" tidak ditemukan";
                                }else{
                                    PembelianDetail detail = new PembelianDetail();
                                    detail.setBarang(d.getBarang());
                                    detail.setNoPembelian("");
                                    detail.setNoUrut(i);
                                    detail.setKodeBarang(d.getKodeBarang());
                                    detail.setNamaBarang(d.getNamaBarang());
                                    detail.setSatuan(d.getSatuan());
                                    detail.setQty(d.getQty());
                                    detail.setQtyMasuk(pembulatan(d.getQty()*d.getBarang().getSatuan().getQty()));
                                    detail.setHargaBeli(d.getHargaBeli());
                                    detail.setHargaPpn(d.getHargaPpn());
                                    detail.setTotal(detail.getHargaPpn()*detail.getQty());
                                    i++;
                                    allDetail.add(detail);
                                }
                            }
                            pbNew.setListPembelianDetail(allDetail);
                            if(status.equals("true"))
                                return Service.saveEditPembelian(con, p, pbNew);
                            else 
                                return status;
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getPembelian();
                    String statusEdit = task.getValue();
                    if(statusEdit.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Pembelian berhasil disimpan");
                        mainApp.closeDialog(stage);
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", statusEdit);
                });
                task.setOnFailed((e) -> {
                    task.getException().printStackTrace();
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        });
        
    }
    private void batalPembelian(PembelianHead p){
        try(Connection con = Koneksi.getConnection()){
            p.setListPembelianDetail(PembelianDetailDAO.getAll(con, p.getNoPembelian()));
            p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPembelian(), "true"));
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
            VerifikasiController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            String status = "Batal Pembelian\n"
                    + "No Pembelian : "+p.getNoPembelian()+"\n"
                    + "Tgl Pembelian : "+tglLengkap.format(tglSql.parse(p.getTglPembelian()))+"\n";
            for(PembelianDetail d : p.getListPembelianDetail()){
                status = status + " - "+rp.format(d.getQty())+" "+d.getSatuan()+"   "+d.getNamaBarang()+"  : Rp "+rp.format(d.getHargaBeli())+"\n";
            }
            status = status + "Total Pembelian : "+rp.format(p.getTotalPembelian())+"\n";
            status = status + "Pembayaran : "+rp.format(p.getPembayaran())+"\n";
            status = status + "Sisa Pembayaran : "+rp.format(p.getSisaPembayaran())+"\n";
            controller.textArea.setText(status);
            controller.passwordField.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    mainApp.closeDialog(child);
                    if(controller.passwordField.getText().equals(sistem.getMasterPassword())){

                        Task<String> task = new Task<String>() {
                            @Override 
                            public String call() throws Exception{
                                try(Connection con = Koneksi.getConnection()){
                                    p.setStatus("false");
                                    p.setTglBatal(Function.getSystemDate());
                                    p.setUserBatal(user.getKodeUser());
                                    return Service.saveBatalPembelian(con, p);
                                }
                            }
                        };
                        task.setOnRunning((e) -> {
                            mainApp.showLoadingScreen();
                        });
                        task.setOnSucceeded((e) -> {
                            mainApp.closeLoading();
                            getPembelian();
                            String statusBatal = task.getValue();
                            if(statusBatal.equals("true")){
                                mainApp.showMessage(Modality.NONE, "Success", "Pembelian berhasil dibatal");
                            }else
                                mainApp.showMessage(Modality.NONE, "Failed", statusBatal);
                        });
                        task.setOnFailed((e) -> {
                            task.getException().printStackTrace();
                            mainApp.closeLoading();
                            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                        });
                        new Thread(task).start();

                    }else{
                        mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi masih salah");
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void detailPembayaran(PembelianHead p){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPembayaran.fxml");
        DetailPembayaranController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setDetailPembelian(p);
        x.pembayaranTable.setRowFactory((TableView<Pembayaran> tableView) -> {
            final TableRow<Pembayaran> row = new TableRow<Pembayaran>(){
                @Override
                public void updateItem(Pembayaran item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else{
                        final ContextMenu rm = new ContextMenu();
                        MenuItem batal = new MenuItem("Batal Pembayaran");
                        batal.setOnAction((ActionEvent e)->{
                            batalPembayaran(item, stage);
                        });
                        rm.getItems().add(batal);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
    }
    private void batalPembayaran(Pembayaran pembayaran, Stage stage){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
            "Batal pembayaran "+pembayaran.getNoPembayaran()+" ?");
        controller.OK.setOnAction((ActionEvent e) -> {
            mainApp.closeMessage();
            Task<String> task = new Task<String>() {
                @Override 
                public String call()throws Exception {
                    try (Connection con = Koneksi.getConnection()) {
                        pembayaran.setTglBatal(Function.getSystemDate());
                        pembayaran.setUserBatal(user.getKodeUser());
                        pembayaran.setStatus("false");
                        return Service.saveBatalPembayaranPembelian(con, pembayaran);
                    }
                }
            };
            task.setOnRunning((ex) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent ex) -> {
                mainApp.closeLoading();
                getPembelian();
                if(task.getValue().equals("true")){
                    mainApp.closeDialog(stage);
                    mainApp.showMessage(Modality.NONE, "Success", "Data pembayaran berhasil dibatal");
                }else{
                    mainApp.showMessage(Modality.NONE, "Error", task.getValue());
                }
            });
            task.setOnFailed((ex) -> {
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                mainApp.closeLoading();
            });
            new Thread(task).start();
        });
    }
    private void pembayaran(PembelianHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Pembayaran.fxml");
        PembayaranController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setPembayaranPembelian(p);
        controller.saveButton.setOnAction((event) -> {
            double jumlahBayar = Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", ""));
            if(jumlahBayar>p.getSisaPembayaran())
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah yang dibayar melebihi dari sisa pembayaran");
            else{
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        try (Connection con = Koneksi.getConnection()) {
                            p.setPembayaran(p.getPembayaran()+Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));
                            p.setSisaPembayaran(p.getSisaPembayaran()-Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));
                            
                            Pembayaran pembayaran = new Pembayaran();
                            pembayaran.setNoPembayaran(PembayaranDAO.getId(con));
                            pembayaran.setTglPembayaran(Function.getSystemDate());
                            pembayaran.setNoTransaksi(p.getNoPembelian());
                            pembayaran.setJenisPembayaran("Cash");
                            pembayaran.setKeterangan("");
                            pembayaran.setJumlahPembayaran(Double.parseDouble(controller.jumlahPembayaranField.getText().replaceAll(",", "")));
                            pembayaran.setKodeUser(user.getKodeUser());
                            pembayaran.setTglBatal("2000-01-01 00:00:00");
                            pembayaran.setUserBatal("");
                            pembayaran.setStatus("true");
                            return Service.savePembayaranPembelian(con, p, pembayaran);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getPembelian();
                    if(task.getValue().equals("true")){
                        mainApp.closeDialog(stage);
                        mainApp.showMessage(Modality.NONE, "Success", "Pembayaran pembelian berhasil disimpan");
                    }else
                        mainApp.showMessage(Modality.NONE, "Error", task.getValue());
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
