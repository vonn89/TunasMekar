/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.KategoriTransaksiDAO;
import com.excellentsystem.TunasMekar.DAO.KeuanganDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.KategoriTransaksi;
import com.excellentsystem.TunasMekar.Model.Keuangan;
import com.excellentsystem.TunasMekar.Service.Service;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class KeuanganController {

    @FXML private DatePicker tglPicker;
    @FXML private ComboBox<String> tipeKasirCombo;
    @FXML private TextField saldoAwalField;
    @FXML private TextField saldoAkhirField;
    
    @FXML private StackPane pane; 
    private GridPane gridPane; 
    
    private double saldoAwal = 0;
    private double saldoAkhir = 0;
    private List<KategoriTransaksi> listKategoriTransaksi = new ArrayList<>();
    private ObservableList<Keuangan> allKeuangan = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        tglPicker.setConverter(Function.getTglConverter());
        tglPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellDisableAfter(LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu menu = new ContextMenu();
        MenuItem newTransaksi = new MenuItem("New Transaksi Keuangan");
        newTransaksi.setOnAction((ActionEvent event) -> {
            newTransaksi();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getKeuangan();
        });
        menu.getItems().addAll(newTransaksi, refresh);
        pane.setOnContextMenuRequested((e) -> {
            menu.show(pane, e.getScreenX(), e.getScreenY());
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        ObservableList<String> allTipeKasir = FXCollections.observableArrayList();
        allTipeKasir.addAll("Kas");
        tipeKasirCombo.setItems(allTipeKasir);
        tipeKasirCombo.getSelectionModel().select("Kas");
    }
    @FXML
    private void getKeuangan(){
        Task<List<Keuangan>> task = new Task<List<Keuangan>>() {
            @Override 
            public List<Keuangan> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    listKategoriTransaksi.clear();
                    listKategoriTransaksi.addAll(KategoriTransaksiDAO.getAll(con));
                    saldoAwal = KeuanganDAO.getSaldoAwal(con, tglPicker.getValue().toString(), tipeKasirCombo.getSelectionModel().getSelectedItem());
                    saldoAkhir = KeuanganDAO.getSaldoAkhir(con, tglPicker.getValue().toString(), tipeKasirCombo.getSelectionModel().getSelectedItem());
                    return KeuanganDAO.getAllByTanggalAndTipeKeuangan(con, 
                        tglPicker.getValue().toString(), tglPicker.getValue().toString(), tipeKasirCombo.getSelectionModel().getSelectedItem());
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            saldoAwalField.setText(rp.format(saldoAwal));
            saldoAkhirField.setText(rp.format(saldoAkhir));
            allKeuangan.clear();
            allKeuangan.addAll(task.getValue());
            setGridPane();
        });
        task.setOnFailed((e) -> {task.getException().printStackTrace();
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void setGridPane(){
        try {
            pane.getChildren().clear();
            gridPane = new GridPane();
            
            gridPane.getColumnConstraints().add(new ColumnConstraints(10, 100, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
            gridPane.getColumnConstraints().add(new ColumnConstraints(100, 100, 200, Priority.ALWAYS, HPos.RIGHT, true));
            
            List<Keuangan> group = new ArrayList<>();
            for(Keuangan k : allKeuangan){
                boolean status = true;
                for(Keuangan g : group){
                    if(k.getKategori().equals(g.getKategori())){
                        g.getListKeuangan().add(k);
                        g.setJumlahRp(g.getJumlahRp()+k.getJumlahRp());
                        status = false;
                    }
                }
                if(status){
                    Keuangan head = new Keuangan();
                    head.setNoKeuangan("");
                    head.setTglKeuangan("");
                    head.setTipeKeuangan(k.getTipeKeuangan());
                    head.setKategori(k.getKategori());
                    head.setKeterangan("");
                    head.setJumlahRp(k.getJumlahRp());
                    head.setKodeUser("");
                    head.setListKeuangan(new ArrayList<>());
                    head.getListKeuangan().add(k);
                    group.add(head);
                }
            }
            int totalRows =  group.size() ;
            for(int i = 0 ; i<totalRows ; i++){
                gridPane.getRowConstraints().add(new RowConstraints(40,40,40));
                if(i%2==0)
                    addBackground(i);
            }
            int row = 0;
            row = addRow(group, "Penjualan Grosir", row);
            row = addRow(group, "Penjualan Retail", row);
            for(Keuangan k : group){
                if(!k.getKategori().equals("Penjualan Grosir") && !k.getKategori().equals("Penjualan Retail")){
                    addHyperLinkText(k.getKategori(), 0, row, k);
                    addText(rp.format(k.getJumlahRp()), 1,row);
                    row = row + 1;
                }
            }
            
            gridPane.setPadding(new Insets(5));
            pane.getChildren().add(gridPane);
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
    private int addRow(List<Keuangan> group, String kategori, int row){
        for(Keuangan k : group){
            if(k.getKategori().equals(kategori)){
                addHyperLinkText(k.getKategori(), 0, row, k);
                addText(rp.format(k.getJumlahRp()), 1,row);
                row = row + 1;
            }
        }
        return row;
    }
    private void addBackground(int row){
        AnchorPane x = new AnchorPane();
        x.setStyle("-fx-background-color:derive(seccolor6,-10%);");
        gridPane.add(x, 0, row, GridPane.REMAINING, 1);
    }
    private void addText(String text,int column, int row){
        if(text.startsWith("-")){
            Label label = new Label(text);
            label.setStyle("-fx-font-size:14;"
                    + "-fx-font-weight: bold;"
                    + "-fx-text-fill:red;");
            gridPane.add(label, column, row);
        }else{
            Label label = new Label(text);
            label.setStyle("-fx-font-size:14;"
                    + "-fx-font-weight: bold;");
            gridPane.add(label, column, row);
        }
    }
    private void addHyperLinkText(String text, int column, int row, Keuangan k){
        Hyperlink hyperlink = new Hyperlink(text);
        hyperlink.setOnAction((e) -> {
            detailKeuangan(text, k);
        });
        gridPane.add(hyperlink, column, row);
    }
    private void newTransaksi(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/TransaksiKeuangan.fxml");
        TransaksiKeuanganController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setKategori(listKategoriTransaksi);
        controller.saveButton.setOnAction((event) -> {
            if(controller.tipeKeuanganCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Tipe keuangan belum dipilih");
            else if(controller.kategoriCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Kategori belum dipilih");
            else if(controller.keteranganField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Keterangan masih kosong");
            else if(controller.jumlahRpField.getText().equals("0") || controller.jumlahRpField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah rp masih kosong");
            else{
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        try(Connection con = Koneksi.getConnection()){
                            return Service.saveTransaksiKeuangan(con, controller.tipeKeuanganCombo.getSelectionModel().getSelectedItem(), 
                                    controller.kategoriCombo.getSelectionModel().getSelectedItem(), controller.keteranganField.getText(), 
                                    Double.parseDouble(controller.jumlahRpField.getText().replaceAll(",", "")));
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    getKeuangan();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.closeDialog(child);
                        mainApp.showMessage(Modality.NONE, "Success", "Transaksi keuangan berhasil disimpan");
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
    private void detailKeuangan(String kategori, Keuangan k){
        if(kategori.startsWith("Penjualan")){
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/DetailKeuanganPenjualan.fxml");
            DetailKeuanganPenjualanController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setPenjualan(tglPicker.getValue().toString(), kategori);
        }else if(kategori.startsWith("Batal Penjualan") ){
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/DetailKeuanganPenjualan.fxml");
            DetailKeuanganPenjualanController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setBatalPenjualan(tglPicker.getValue().toString(), kategori);
        }else{
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/DetailKeuangan.fxml");
            DetailKeuanganController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setKeuangan(k);
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem refresh = new MenuItem("Refresh");
            refresh.setOnAction((ActionEvent event) -> {
                controller.keuanganTable.refresh();
            });
            rowMenu.getItems().addAll(refresh);
            controller.keuanganTable.setContextMenu(rowMenu);
            controller.keuanganTable.setRowFactory(table -> {
                TableRow<Keuangan> row = new TableRow<Keuangan>() {
                    @Override
                    public void updateItem(Keuangan item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setContextMenu(rowMenu);
                        }else{
                            final ContextMenu rowMenu = new ContextMenu();
                            MenuItem batal = new MenuItem("Batal Transaksi");
                            batal.setOnAction((ActionEvent e) -> {
                                batalTransaksi(item, child);
                            });
                            MenuItem refresh = new MenuItem("Refresh");
                            refresh.setOnAction((ActionEvent e) -> {
                                controller.keuanganTable.refresh();
                            });
                            boolean status = false;
                            for(KategoriTransaksi k : listKategoriTransaksi){
                                if(item.getKategori().equals(k.getKodeKategori()))
                                    status = true;
                            }
                            if(status)
                                rowMenu.getItems().add(batal);
                            rowMenu.getItems().add(refresh);
                            setContextMenu(rowMenu);
                        }
                    }
                };
                return row;
            });
        }
    }
    private void batalTransaksi(Keuangan k, Stage c){
        try{
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(c, child, "View/Dialog/Verifikasi.fxml");
            VerifikasiController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.textArea.setText("Batal Transaksi Keuangan\n"
                    + tglLengkap.format(tglSql.parse(k.getTglKeuangan()))+"\n"
                    + k.getNoKeuangan()+" - "+k.getKategori()+"\n"
                    +"  "+ k.getKeterangan()+"   :  Rp "+rp.format(k.getJumlahRp()));
            controller.passwordField.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    mainApp.closeDialog(child);
                    if(controller.passwordField.getText().equals(sistem.getMasterPassword())){

                        Task<String> task = new Task<String>() {
                            @Override 
                            public String call() throws Exception{
                                try(Connection con = Koneksi.getConnection()){
                                    return Service.saveBatalTransaksiKas(con, k);
                                }
                            }
                        };
                        task.setOnRunning((e) -> {
                            mainApp.showLoadingScreen();
                        });
                        task.setOnSucceeded((e) -> {
                            mainApp.closeLoading();
                            getKeuangan();
                            String status = task.getValue();
                            if(status.equals("true")){
                                mainApp.closeDialog(c);
                                mainApp.showMessage(Modality.NONE, "Success", "Transaksi berhasil dibatal");
                            }else
                                mainApp.showMessage(Modality.NONE, "Failed", status);
                        });
                        task.setOnFailed((e) -> {
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
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
