/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.PenjualanDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanHeadDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class DetailKeuanganPenjualanController  {

    @FXML private Label title;
    @FXML private TableView<PenjualanHead> penjualanHeadTable;
    @FXML private TableColumn<PenjualanHead, String> noPenjualanColumn;
    @FXML private TableColumn<PenjualanHead, String> tglPenjualanColumn;
    @FXML private TableColumn<PenjualanHead, Number> totalPenjualanColumn;
    @FXML private TableColumn<PenjualanHead, String> kodeUserColumn;
    
    @FXML private TableView<PenjualanDetail> penjualanDetailTable;
    @FXML private TableColumn numberColumn = new TableColumn( "Number" );
    @FXML private TableColumn<PenjualanDetail, String> kodeBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> satuanColumn;
    @FXML private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML private TableColumn<PenjualanDetail, Number> totalColumn;
    
    @FXML private TextField searchField;
    @FXML private Label totalPenjualanLabel;
    private Main mainApp;
    private Stage stage;
    private final ObservableList<PenjualanDetail> allDetail = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> allPenjualan = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> filterData = FXCollections.observableArrayList();
    public void initialize() {
        numberColumn.setCellFactory((p) -> new TableCell(){
            @Override
            public void updateItem( Object item, boolean empty ){
                super.updateItem( item, empty );
                setGraphic( null );
                setText( empty ? null : getIndex() + 1 + "" );
            }
        });
        noPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().noPenjualanProperty());
        tglPenjualanColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getTglPenjualan())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tglPenjualanColumn.setComparator(Function.sortDate(tglLengkap));
        totalPenjualanColumn.setCellValueFactory(cellData -> cellData.getValue().totalPenjualanProperty());
        totalPenjualanColumn.setCellFactory(col -> getTableCell(rp));
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());
        
        kodeBarangColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarangProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().satuanProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalHargaProperty());
        totalColumn.setCellFactory(col -> getTableCell(rp));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getPenjualan();
        });
        rowMenu.getItems().addAll(refresh);
        penjualanHeadTable.setContextMenu(rowMenu);
        penjualanHeadTable.setRowFactory(table -> {
            TableRow<PenjualanHead> row = new TableRow<PenjualanHead>() {
                @Override
                public void updateItem(PenjualanHead item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Penjualan");
                        detail.setOnAction((ActionEvent e) -> {
                            detailPenjualan(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPenjualan();
                        });
                        rowMenu.getItems().add(detail);
                        rowMenu.getItems().add(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null)
                        detailPenjualan(row.getItem());
                }
            });
            return row;
        });
        
        final ContextMenu rowMenuDetail = new ContextMenu();
        MenuItem refreshDetail = new MenuItem("Refresh");
        refreshDetail.setOnAction((ActionEvent event) -> {
            getPenjualan();
        });
        rowMenuDetail.getItems().addAll(refreshDetail);
        penjualanDetailTable.setContextMenu(rowMenuDetail);
        penjualanDetailTable.setRowFactory(table -> {
            TableRow<PenjualanDetail> row = new TableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenuDetail);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPenjualan();
                        });
                        rowMenu.getItems().add(detail);
                        rowMenu.getItems().add(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null)
                        detailBarang(row.getItem());
                }
            });
            return row;
        });
        
        penjualanHeadTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectPenjualan(newValue));
        
        allPenjualan.addListener((ListChangeListener.Change<? extends PenjualanHead> change) -> {
            searchPenjualan();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchPenjualan();
        });
        filterData.addAll(allPenjualan);
        penjualanHeadTable.setItems(filterData);
        penjualanDetailTable.setItems(allDetail);
    }    
    public void setMainApp(Main main, Stage stage){
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    private String tanggal = "2000-01-01";
    public void setPenjualan(String tanggal, String kategori){
        this.tanggal = tanggal;
        title.setText(kategori);
        getPenjualan();
    }
    public void setBatalPenjualan(String tanggal, String kategori){
        this.tanggal = tanggal;
        title.setText(kategori);
        getBatalPenjualan();
    }
    private void getPenjualan(){
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override 
            public List<PenjualanHead> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    String kategoriPenjualan = title.getText().replaceAll("Batal ", "").replaceAll("Penjualan ", "");
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByDateAndKategoriAndStatus(
                        con, tanggal, tanggal, kategoriPenjualan, "%");
                    List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByDateAndKategoriAndStatus(
                        con, tanggal, tanggal, kategoriPenjualan, "%");
                    for(PenjualanHead p : listPenjualanHead){
                        List<PenjualanDetail> detail = new ArrayList<>();
                        for(PenjualanDetail d : listPenjualanDetail){
                            if(p.getNoPenjualan().equals(d.getNoPenjualan()))
                                detail.add(d);
                        }
                        p.setListPenjualanDetail(detail);
                    }
                    return listPenjualanHead;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allPenjualan.clear();
            allPenjualan.addAll(task.getValue());
            penjualanHeadTable.refresh();
            hitungTotal();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private void getBatalPenjualan(){
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override 
            public List<PenjualanHead> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    String kategoriPenjualan = title.getText().replaceAll("Batal ", "").replaceAll("Penjualan ", "");
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByDateAndKategoriAndStatus(
                        con, tanggal, tanggal, kategoriPenjualan, "false");
                    List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByDateAndKategoriAndStatus(
                        con, tanggal, tanggal, kategoriPenjualan, "false");
                    for(PenjualanHead p : listPenjualanHead){
                        List<PenjualanDetail> detail = new ArrayList<>();
                        for(PenjualanDetail d : listPenjualanDetail){
                            if(p.getNoPenjualan().equals(d.getNoPenjualan()))
                                detail.add(d);
                        }
                        p.setListPenjualanDetail(detail);
                    }
                    return listPenjualanHead;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allPenjualan.clear();
            allPenjualan.addAll(task.getValue());
            penjualanHeadTable.refresh();
            hitungTotal();
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
    private void searchPenjualan() {
        try{
            filterData.clear();
            for (PenjualanHead p : allPenjualan) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(p);
                else{
                    if(checkColumn(p.getNoPenjualan())||
                        checkColumn(tglLengkap.format(tglSql.parse(p.getTglPenjualan())))||
                        checkColumn(p.getKategoriPenjualan())||
                        checkColumn(p.getKodeUser())||
                        checkColumn(rp.format(p.getTotalPenjualan())))
                        filterData.add(p);
                    else{
                        boolean status = false;
                        for(PenjualanDetail d : p.getListPenjualanDetail()){
                            if(checkColumn(d.getNoPenjualan())||
                                checkColumn(d.getKodeBarang())||
                                checkColumn(d.getNamaBarang())||
                                checkColumn(d.getSatuan())||
                                checkColumn(rp.format(d.getQty()))||
                                checkColumn(rp.format(d.getHarga())))
                                status = true;
                        }
                        if(status)
                            filterData.add(p);
                    }
                }
            }
            hitungTotal();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectPenjualan(PenjualanHead p){
        allDetail.clear();
        if(p!=null)
            allDetail.addAll(p.getListPenjualanDetail());
    }
    private void hitungTotal(){
        double totalPenjualan = 0;
        for(PenjualanHead d : filterData){
            totalPenjualan = totalPenjualan + d.getTotalPenjualan();
        }
        totalPenjualanLabel.setText(rp.format(totalPenjualan));
    }
    private void detailPenjualan(PenjualanHead p){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage ,child, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setDetailPenjualan(p.getNoPenjualan());
    }
    private void detailBarang(PenjualanDetail d){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(d.getKodeBarang());
        x.setViewOnly();
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
