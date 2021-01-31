/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.PelangganDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanHeadDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import static com.excellentsystem.TunasMekar.Function.pembulatan;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import com.excellentsystem.TunasMekar.Model.Satuan;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class DetailPenjualanController  {

    @FXML private GridPane gridPane;
    @FXML private TableView<PenjualanDetail> penjualanDetailTable;
    @FXML private TableColumn numberColumn = new TableColumn( "Number" );
    @FXML private TableColumn<PenjualanDetail, String> kodeBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> satuanColumn;
    @FXML private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML private TableColumn<PenjualanDetail, Number> qtyStokColumn;
    @FXML private TableColumn<PenjualanDetail, Number> nilaiColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML private TableColumn<PenjualanDetail, Number> totalColumn;
    
    @FXML private TextField noPenjualanField;
    @FXML private TextField tglPenjualanField;
    @FXML private TextField kategoriPenjualanField;
    @FXML public TextField pelangganField;
    @FXML private TextField kodeUserField;
    @FXML public Label totalPenjualanLabel;
    @FXML public Label totalDiskonLabel;
    @FXML public Label grandtotalLabel;
    @FXML public Button saveButton;
    @FXML public CheckBox printSuratPenjualanCheck;
    
    private PenjualanHead p;
    private Stage stage;
    private Main mainApp;   
    public ObservableList<PenjualanDetail> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        numberColumn.setCellFactory((p) -> new TableCell(){
            @Override
            public void updateItem( Object item, boolean empty ){
                super.updateItem( item, empty );
                setGraphic( null );
                setText( empty ? null : getIndex() + 1 + "" );
            }
        });
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().satuanProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        qtyStokColumn.setCellValueFactory(cellData -> cellData.getValue().qtyKeluarProperty());
        qtyStokColumn.setCellFactory(col -> getTableCell(qty));
        nilaiColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiProperty());
        nilaiColumn.setCellFactory(col -> getTableCell(rp));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalHargaProperty());
        totalColumn.setCellFactory(col -> getTableCell(rp));
        
        final ContextMenu rm = new ContextMenu();
        MenuItem tambah = new MenuItem("Tambah Barang");
        tambah.setOnAction((ActionEvent e) -> {
            tambahBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            penjualanDetailTable.refresh();
        });
        if(saveButton.isVisible())
            rm.getItems().add(tambah);
        rm.getItems().addAll(refresh);
        penjualanDetailTable.setContextMenu(rm);
        penjualanDetailTable.setRowFactory(ttv -> {
            TableRow<PenjualanDetail> row = new TableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem tambah = new MenuItem("Tambah Barang");
                        tambah.setOnAction((ActionEvent e) -> {
                            tambahBarang();
                        });
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem ubah = new MenuItem("Ubah Barang");
                        ubah.setOnAction((ActionEvent e) -> {
                            ubahBarang(item);
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent e) -> {
                            hapusBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            penjualanDetailTable.refresh();
                        });
                        if(saveButton.isVisible()){
                            rm.getItems().add(tambah);
                            rm.getItems().add(ubah);
                            rm.getItems().add(hapus);
                        }else{
                            rm.getItems().add(detail);
                        }
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        if(saveButton.isVisible())
                            ubahBarang(row.getItem());
                        else
                            detailBarang(row.getItem());
                    }
                }
            });
            return row;
        });
        penjualanDetailTable.setItems(allBarang);
    }    
    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setDetailPenjualan(String noPenjualan) {
        Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    p = PenjualanHeadDAO.get(con, noPenjualan);
                    if(p.getPelanggan()!=null && !p.getPelanggan().equals("")){
                        p.setCustomer(PelangganDAO.get(con, p.getPelanggan()));
                    }
                    p.setListPenjualanDetail(PenjualanDetailDAO.getAll(con, noPenjualan));
                    for(PenjualanDetail d : p.getListPenjualanDetail()){
                        d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                    }
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try{
                mainApp.closeLoading();
                noPenjualanField.setText(p.getNoPenjualan());
                tglPenjualanField.setText(tglLengkap.format(tglSql.parse(p.getTglPenjualan())));
                kategoriPenjualanField.setText(p.getKategoriPenjualan());
                if(p.getPelanggan()!=null && !p.getPelanggan().equals("")){
                    pelangganField.setText(p.getCustomer().getNama());
                }
                kodeUserField.setText(p.getKodeUser());
                totalPenjualanLabel.setText(rp.format(p.getTotalPenjualan()));
                totalDiskonLabel.setText(rp.format(p.getTotalDiskon()));
                grandtotalLabel.setText(rp.format(p.getGrandtotal()));
                allBarang.clear();
                allBarang.addAll(p.getListPenjualanDetail());
                saveButton.setVisible(false);
                printSuratPenjualanCheck.setVisible(false);
                gridPane.getRowConstraints().get(9).setMinHeight(0);
                gridPane.getRowConstraints().get(9).setPrefHeight(0);
                gridPane.getRowConstraints().get(9).setMaxHeight(0);
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    public void setEditPenjualan(String noPenjualan) {
        Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    p = PenjualanHeadDAO.get(con, noPenjualan);
                    if(p.getPelanggan()!=null && !p.getPelanggan().equals("")){
                        p.setCustomer(PelangganDAO.get(con, p.getPelanggan()));
                    }
                    p.setListPenjualanDetail(PenjualanDetailDAO.getAll(con, noPenjualan));
                    for(PenjualanDetail d : p.getListPenjualanDetail()){
                        d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                        d.getBarang().setSatuan(SatuanDAO.get(con, d.getKodeBarang(), d.getSatuan()));
                        d.getBarang().setAllSatuan(SatuanDAO.getAllByKodeBarang(con, d.getKodeBarang()));
                    }
                    return null;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try{
                mainApp.closeLoading();
                noPenjualanField.setText(p.getNoPenjualan());
                tglPenjualanField.setText(tglLengkap.format(tglSql.parse(p.getTglPenjualan())));
                kategoriPenjualanField.setText(p.getKategoriPenjualan());
                if(p.getPelanggan()!=null && !p.getPelanggan().equals("")){
                    pelangganField.setText(p.getCustomer().getNama());
                }
                kodeUserField.setText(p.getKodeUser());
                totalPenjualanLabel.setText(rp.format(p.getTotalPenjualan()));
                totalDiskonLabel.setText(rp.format(p.getTotalDiskon()));
                grandtotalLabel.setText(rp.format(p.getGrandtotal()));
                allBarang.clear();
                allBarang.addAll(p.getListPenjualanDetail());
                qtyStokColumn.setVisible(false);
                nilaiColumn.setVisible(false);
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private void detailBarang(PenjualanDetail d){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(d.getKodeBarang());
        x.setViewOnly();
    }
    private void tambahBarang(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/CariBarang.fxml");
        CariBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setCari();
        x.barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {};
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        mainApp.closeDialog(child);
                        Barang b = row.getItem();
                        setBarang(b);
                    }
                }
            });
            return row;
        });
        x.barangTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                if(x.barangTable.getSelectionModel().getSelectedItem()!=null){
                    mainApp.closeDialog(child);
                    setBarang(x.barangTable.getSelectionModel().getSelectedItem());
                }
            }
        });
    }
    private void setBarang(Barang b){
        boolean statusBarang = true;
        for(PenjualanDetail d : allBarang){
            if(d.getKodeBarang().equals(b.getKodeBarang()) && d.getSatuan().equals(b.getSatuan().getKodeSatuan())){
                d.setQty(pembulatan(d.getQty() + 1));
                d.setTotalHarga(pembulatan(d.getHarga()*d.getQty()));
                statusBarang = false;
                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
            }
        }
        if(statusBarang){
            PenjualanDetail d = new PenjualanDetail();
            d.setBarang(b);
            d.setNoPenjualan("");
            d.setNoUrut(0);
            d.setKodeBarang(b.getKodeBarang());
            d.setNamaBarang(b.getNamaBarang());
            d.setSatuan(b.getSatuan().getKodeSatuan());
            d.setQty(1);
            d.setNilai(0);
            d.setTotalNilai(0);
            if(p.getKategoriPenjualan().equals("Grosir Besar"))
                d.setHarga(b.getSatuan().getHargaGrosirBesar());
            if(p.getKategoriPenjualan().equals("Grosir"))
                d.setHarga(b.getSatuan().getHargaGrosir());
            else if(p.getKategoriPenjualan().equals("Retail"))
                d.setHarga(b.getSatuan().getHargaRetail());
            d.setTotalNilai(0);
            d.setTotalHarga(pembulatan(d.getHarga()*d.getQty()));
            allBarang.add(0, d);
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
        }
    }
    private void ubahBarang(PenjualanDetail d){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child ,"View/Dialog/UbahBarang.fxml");
        UbahBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setPenjualan(d, p.getKategoriPenjualan());
        controller.qtyField.requestFocus();
        controller.qtyField.selectAll();
        child.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                d.setSatuan(controller.satuanCombo.getSelectionModel().getSelectedItem());
                Satuan satuan = d.getBarang().getSatuan();
                for(Satuan s : d.getBarang().getAllSatuan()){
                    if(s.getKodeSatuan().equals(controller.satuanCombo.getSelectionModel().getSelectedItem()))
                        satuan = s;
                }
                d.getBarang().setSatuan(satuan);
                d.setQty(Double.parseDouble(controller.qtyField.getText().replaceAll(",", "")));
                d.setHarga(Double.parseDouble(controller.nilaiField.getText().replaceAll(",", "")));
                d.setTotalHarga(pembulatan(d.getHarga()*d.getQty()));
                             
                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
                mainApp.closeDialog(child);
            }
        });
    }
    private void hapusBarang(PenjualanDetail d){
        Stage warning = new Stage();
        FXMLLoader warningLoader = mainApp.showDialog(stage, warning ,"View/Dialog/Warning.fxml");
        WarningController warningController = warningLoader.getController();
        warningController.setMainApp(mainApp, warning, "Confirmation", 
                "Hapus barang "+d.getNamaBarang()+" - "+qty.format(d.getQty())+" "+d.getSatuan()+" ?");
        warningController.OK.setOnAction((event) -> {
            allBarang.remove(d);
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            mainApp.closeDialog(warning);
        });
    }
    private void hitungTotal(){
        double total = 0;
        for(PenjualanDetail d : allBarang){
            total = total + d.getTotalHarga();
        }
        totalPenjualanLabel.setText(rp.format(total));
        grandtotalLabel.setText(rp.format(total-Double.parseDouble(totalDiskonLabel.getText().replaceAll(",", ""))));
    }
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
    
}
