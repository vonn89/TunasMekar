/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.PelangganDAO;
import com.excellentsystem.TunasMekar.DAO.PembayaranDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanDiskonDAO;
import com.excellentsystem.TunasMekar.DAO.PenjualanHeadDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import com.excellentsystem.TunasMekar.PrintOut.PrintOut;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class GroupPenjualanController  {

    @FXML private TableView<PenjualanHead> penjualanHeadTable;
    @FXML private TableColumn<PenjualanHead, Boolean> checkColumn;
    @FXML private TableColumn<PenjualanHead, String> noPenjualanColumn;
    @FXML private TableColumn<PenjualanHead, String> tglPenjualanColumn;
    @FXML private TableColumn<PenjualanHead, String> kategoriColumn;
    @FXML private TableColumn<PenjualanHead, String> pelangganColumn;
    @FXML private TableColumn<PenjualanHead, Number> grandtotalColumn;
    @FXML private TableColumn<PenjualanHead, Number> pembayaranColumn;
    @FXML private TableColumn<PenjualanHead, Number> sisaPembayaranColumn;
    @FXML private TableColumn<PenjualanHead, String> kodeUserColumn;
    
    @FXML private TableView<PenjualanDetail> penjualanDetailTable;
    @FXML private TableColumn numberColumn = new TableColumn( "Number" );
    @FXML private TableColumn<PenjualanDetail, String> kodeBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> satuanColumn;
    @FXML private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML private TableColumn<PenjualanDetail, Number> totalColumn;
    
    @FXML private CheckBox checkAll;
    @FXML private TextField searchField;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    
    @FXML private Label totalPenjualanLabel;
    private Main mainApp;
    private Stage stage;
    private final ObservableList<PenjualanDetail> allDetail = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> allPenjualan = FXCollections.observableArrayList();
    private final ObservableList<PenjualanHead> filterData = FXCollections.observableArrayList();
    public void initialize() {
        checkColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(
                (Integer param) -> penjualanHeadTable.getItems().get(param).selectedProperty()));
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
            } catch (Exception ex) {
                return null;
            }
        });
        tglPenjualanColumn.setComparator(Function.sortDate(tglLengkap));
        tglPenjualanColumn.setCellFactory(col -> Function.getWrapTableCell(tglPenjualanColumn));
        
        kategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kategoriPenjualanProperty());
        kategoriColumn.setCellFactory(col -> Function.getWrapTableCell(kategoriColumn));
        
        pelangganColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer().namaProperty());
        pelangganColumn.setCellFactory(col -> Function.getWrapTableCell(pelangganColumn));
        
        grandtotalColumn.setCellValueFactory(cellData -> cellData.getValue().grandtotalProperty());
        grandtotalColumn.setCellFactory(col -> getTableCell(rp));
        
        pembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().pembayaranProperty());
        pembayaranColumn.setCellFactory(col -> getTableCell(rp));
        
        sisaPembayaranColumn.setCellValueFactory(cellData -> cellData.getValue().sisaPembayaranProperty());
        sisaPembayaranColumn.setCellFactory(col -> getTableCell(rp));
        
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
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
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
                    if(row.getItem()!=null){
                        if(row.getItem().isSelected())
                            row.getItem().setSelected(false);
                        else
                            row.getItem().setSelected(true);
                        hitungTotal();
                    }
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
        getPenjualan();
    }
    @FXML
    private void checkAll(){
        for(PenjualanHead s : allPenjualan){
            s.setSelected(checkAll.isSelected());
        }
        penjualanHeadTable.refresh();
    }
    @FXML
    private void getPenjualan(){
        Task<List<PenjualanHead>> task = new Task<List<PenjualanHead>>() {
            @Override 
            public List<PenjualanHead> call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByDateAndKategoriAndStatus(
                        con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "%", "true");
                    List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByDateAndKategoriAndStatus(
                        con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "%", "true");
                    for(PenjualanHead p : listPenjualanHead){
                        if(p.getPelanggan()!=null && !p.getPelanggan().equals("") ){
                            p.setCustomer(PelangganDAO.get(con, p.getPelanggan()));
                        }
                        if(p.getCustomer()==null){
                            Pelanggan plg = new Pelanggan();
                            plg.setKodePelanggan("");
                            plg.setNama("");
                            plg.setAlamat("");
                            plg.setNoTelp("");
                            plg.setHutang(0);
                            plg.setStatus("");
                            
                            p.setCustomer(plg);
                        }
                        List<PenjualanDetail> detail = new ArrayList<>();
                        for(PenjualanDetail d : listPenjualanDetail){
                            if(p.getNoPenjualan().equals(d.getNoPenjualan()))
                                detail.add(d);
                        }
                        p.setListPenjualanDetail(detail);
                        p.setSelected(checkAll.isSelected());
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
                        checkColumn(p.getPelanggan())||
                        checkColumn(p.getCustomer().getNama())||
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
            if(d.isSelected())
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
    private void printStruk(){
        List<PenjualanHead> listPrint = new ArrayList();
        for(PenjualanHead d : filterData){
            if(d.isSelected()){
                listPrint.add(d);
            }
        }
        if(listPrint.isEmpty()){
            mainApp.showMessage(Modality.NONE, "Warning", "Penjualan belum dipilih");
        }else{
            try(Connection con = Koneksi.getConnection()){
                for(PenjualanHead p : listPrint){
                    p.setListPenjualanDiskon(PenjualanDiskonDAO.getAll(con, p.getNoPenjualan()));
                    p.setListPembayaran(PembayaranDAO.getAllByNoTransaksiAndStatus(con, p.getNoPenjualan(), "true"));
                }
                PrintOut po = new PrintOut();
                po.printStrukGroupPenjualanDirect(listPrint);
            }catch(Exception e){
                e.printStackTrace();
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
