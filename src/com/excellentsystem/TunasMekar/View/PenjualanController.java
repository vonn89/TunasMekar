/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.CartHeadDAO;
import com.excellentsystem.TunasMekar.DAO.SatuanDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.pembulatan;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.timeout;
import static com.excellentsystem.TunasMekar.Main.user;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.CartDetail;
import com.excellentsystem.TunasMekar.Model.CartHead;
import com.excellentsystem.TunasMekar.Model.Pelanggan;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanDiskon;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import com.excellentsystem.TunasMekar.Model.Pembayaran;
import com.excellentsystem.TunasMekar.Model.Satuan;
import com.excellentsystem.TunasMekar.PrintOut.PrintOut;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.CariBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.CartController;
import com.excellentsystem.TunasMekar.View.Dialog.DiskonController;
import com.excellentsystem.TunasMekar.View.Dialog.NamaCartController;
import com.excellentsystem.TunasMekar.View.Dialog.PilihSatuanController;
import com.excellentsystem.TunasMekar.View.Dialog.TotalPenjualanController;
import com.excellentsystem.TunasMekar.View.Dialog.UbahBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.VerifikasiController;
import com.excellentsystem.TunasMekar.View.Dialog.WarningController;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class PenjualanController  {

    @FXML public TableView<PenjualanDetail> penjualanDetailTable;
    @FXML private TableColumn numberColumn = new TableColumn();
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> satuanColumn;
    @FXML private TableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML private TableColumn<PenjualanDetail, Number> totalColumn;
    @FXML private TableColumn removeColumn;
    
    @FXML public CheckBox printCheck;
    @FXML public TextField searchField;
    @FXML private TextField totalPenjualanField;
    @FXML private TextField totalDiskonField;
    @FXML private TextField grandtotalField;
    private CartHead cartHead;
    @FXML private Label cartLabel;
    
    @FXML private Label kategoriLabel;
    
    private Main mainApp;   
    private ObservableList<PenjualanDiskon> listPenjualanDiskon = FXCollections.observableArrayList();
    public ObservableList<PenjualanDetail> allBarang = FXCollections.observableArrayList();
    private Callback<TableColumn, TableCell> getDeleteButtonCell(){
        return (final TableColumn p) -> new TableCell<PenjualanDetail, Boolean>(){
            final Button btn = new Button("");
            @Override
            public void updateItem( Boolean item, boolean empty ){
                super.updateItem( item, empty );
                if ( empty ){
                    setGraphic( null );
                }else{
                    btn.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("Resource/remove.png"),15,15,false,false)));
                    btn.setContentDisplay(ContentDisplay.CENTER);
                    btn.setAlignment(Pos.CENTER);
                    btn.setPrefSize(15, 15);
                    btn.getStyleClass().clear();
                    btn.getStyleClass().add("transparent-button");
                    btn.setOnAction((ActionEvent e) ->{
                        PenjualanDetail detail = (PenjualanDetail) getTableView().getItems().get(getIndex());
                        hapusBarang(detail);
                    });
                    setGraphic(btn);
                }
            }
        };
    }
//    private Callback<TableColumn<PenjualanDetail, Number>, TableCell<PenjualanDetail, Number>> getQtyTableCell(){
//        return (final TableColumn<PenjualanDetail, Number> p) -> new TableCell<PenjualanDetail, Number>(){
//            final TextField textField = new TextField();
//            @Override
//            public void updateItem( Number item, boolean empty ){
//                super.updateItem( item, empty );
//                if ( empty ){
//                    setGraphic( null );
//                }else{
//                    textField.setText(qty.format(item));
//                    textField.setMaxWidth(Double.MAX_VALUE);
//                    textField.setPrefHeight(30);
//                    textField.getStyleClass().clear();
//                    textField.getStyleClass().add("transparent-text-field");
//                    textField.setOnKeyReleased((event) -> {
//                        try{
//                            PenjualanDetail detail = (PenjualanDetail) getTableView().getItems().get(getIndex());
//                            detail.setQty(Double.parseDouble(textField.getText().replaceAll(",", "")));
//                            detail.setTotalHarga(Function.pembulatan(detail.getQty()*detail.getHarga()));
//                            hitungTotal();
//                        }catch(NumberFormatException e){
//                            textField.undo();
//                        }
//                    });
//                    Platform.runLater(() -> {
//                        textField.end();
//                    });
//                    setGraphic(textField);
//                }
//            }
//        };
//    }
//    private Callback<TableColumn<PenjualanDetail, Number>, TableCell<PenjualanDetail, Number>> getHargaTableCell(){
//        return (final TableColumn<PenjualanDetail, Number> p) -> new TableCell<PenjualanDetail, Number>(){
//            final TextField textField = new TextField();
//            @Override
//            public void updateItem( Number item, boolean empty ){
//                super.updateItem( item, empty );
//                if ( empty ){
//                    setGraphic( null );
//                }else{
//                    textField.setText(rp.format(item));
//                    textField.setMaxWidth(Double.MAX_VALUE);
//                    textField.setPrefHeight(30);
//                    textField.getStyleClass().clear();
//                    textField.getStyleClass().add("transparent-text-field");
//                    textField.setOnKeyReleased((event) -> {
//                        try{
//                            PenjualanDetail detail = (PenjualanDetail) getTableView().getItems().get(getIndex());
//                            detail.setHarga(Double.parseDouble(textField.getText().replaceAll(",", "")));
//                            detail.setTotalHarga(Function.pembulatan(detail.getQty()*detail.getHarga()));
//                            hitungTotal();
//                        }catch(NumberFormatException e){
//                            textField.undo();
//                        }
//                    });
//                    Platform.runLater(() -> {
//                        textField.end();
//                    });
//                    setGraphic(textField);
//                }
//            }
//        };
//    }
    public void initialize() {
        namaBarangColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.6));
        satuanColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        qtyColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        hargaColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        totalColumn.prefWidthProperty().bind(penjualanDetailTable.widthProperty().subtract(100).multiply(0.1));
        
        penjualanDetailTable.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(ListChangeListener.Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    penjualanDetailTable.getColumns().setAll(namaBarangColumn, satuanColumn, qtyColumn, hargaColumn, 
                            totalColumn, removeColumn);
                    this.suspended = false;
                }
            }
        });
        
        numberColumn.setCellFactory((p) -> new TableCell(){
            @Override
            public void updateItem( Object item, boolean empty ){
                super.updateItem( item, empty );
                setGraphic( null );
                setText( empty ? null : getIndex() + 1 + "" );
            }
        });
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().satuanProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory((param) -> Function.getTableCell(qty));
//        qtyColumn.setCellFactory(getQtyTableCell());
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
//        hargaColumn.setCellFactory(getHargaTableCell());
        hargaColumn.setCellFactory((param) -> Function.getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalHargaProperty());
        totalColumn.setCellFactory((param) -> Function.getTableCell(rp));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        removeColumn.setCellFactory(getDeleteButtonCell());
        
        final ContextMenu rm = new ContextMenu();
        MenuItem cari = new MenuItem("Cari Barang");
        cari.setOnAction((ActionEvent event) -> {
            cariBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            penjualanDetailTable.refresh();
        });
        rm.getItems().addAll(cari, refresh);
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
                        MenuItem cari = new MenuItem("Cari Barang");
                        cari.setOnAction((ActionEvent event) -> {
                            cariBarang();
                        });
                        MenuItem ubah = new MenuItem("Ubah Barang");
                        ubah.setOnAction((ActionEvent event) -> {
                            ubahBarang(item);
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            penjualanDetailTable.refresh();
                        });
                        rm.getItems().add(cari);
                        rm.getItems().add(ubah);
                        rm.getItems().add(hapus);
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    ubahBarang(row.getItem());
            });
            return row;
        });
        penjualanDetailTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.DELETE) {
                if(penjualanDetailTable.getSelectionModel().getSelectedItem()!=null)
                    hapusBarang(penjualanDetailTable.getSelectionModel().getSelectedItem());
            }
            if (event.getCode() == KeyCode.SPACE) {
                if(penjualanDetailTable.getSelectionModel().getSelectedItem()!=null)
                    ubahBarang(penjualanDetailTable.getSelectionModel().getSelectedItem());
            }
        });
        searchField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                getBarang();
        });
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        penjualanDetailTable.setItems(allBarang);
        searchField.requestFocus();
    } 
    public void setKategoriPenjualan(String kategori){
        kategoriLabel.setText(kategori);
    }
    @FXML
    public void cariBarang(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/CariBarang.fxml");
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
        child.setOnHiding( event -> {
            searchField.requestFocus();
        });
    } 
    @FXML
    private void getBarang(){
        if(!"".equals(searchField.getText())){
            Task<List<Barang>> task = new Task<List<Barang>>() {
                @Override 
                public List<Barang> call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        List<Satuan> listSatuan = SatuanDAO.getAllByKodeBarcode(con, searchField.getText());
                        List<Barang> listBarang = new ArrayList<>();
                        for(Satuan s : listSatuan){
                            Barang b = BarangDAO.get(con, s.getKodeBarang());
                            b.setAllSatuan(SatuanDAO.getAllByKodeBarang(con, b.getKodeBarang()));
                            b.setSatuan(s);
                            listBarang.add(b);
                        }
                        return listBarang;
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                mainApp.closeLoading();
                List<Barang> listBarang = task.getValue();
                if(listBarang.isEmpty()){
                    Stage child = new Stage();
                    FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/Warning.fxml");
                    WarningController controller = loader.getController();
                    controller.setMainApp(mainApp, child, "Warning", "Kode Barcode "+searchField.getText()+" Tidak Ditemukan");
                    child.setOnHiding( event -> {
                        searchField.requestFocus();
                    });
                }else{
                    if(listBarang.size()==1){
                        setBarang(listBarang.get(0));
                    }else{
                        Stage child = new Stage();
                        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/PilihSatuan.fxml");
                        PilihSatuanController controller = loader.getController();
                        controller.setMainApp(mainApp, child);
                        controller.setBarang(listBarang, kategoriLabel.getText());
                        for(Node n : controller.flowPane.getChildren()){
                            if(n instanceof VBox){
                                VBox v = (VBox) n;
                                v.setOnMouseClicked((event) -> {
                                    for(Barang brg : listBarang){
                                        if(brg.getKodeBarang().equals(v.getId().substring(0,7)) &&
                                                brg.getSatuan().getKodeSatuan().equals(v.getId().substring(8))){
                                            mainApp.closeDialog(child);
                                            setBarang(brg);
                                        }
                                    }
                                });
                            }
                        }
                        child.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                            if (event.getCode() == KeyCode.ENTER) {
                                for(Node n : controller.flowPane.getChildren()){
                                    if(n instanceof VBox){
                                        VBox v = (VBox) n;
                                        if(v.isFocused()){
                                            for(Barang brg : listBarang){
                                                if(brg.getKodeBarang().equals(v.getId().substring(0,7)) &&
                                                        brg.getSatuan().getKodeSatuan().equals(v.getId().substring(8))){
                                                    mainApp.closeDialog(child);
                                                    setBarang(brg);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        child.setOnHiding( event -> {
                            searchField.requestFocus();
                        });
                    }
                }
                searchField.setText("");
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        }else{
            searchField.requestFocus();
        }
    }
    private void setBarang(Barang b){
        boolean status = true;
        for(PenjualanDetail p : allBarang){
            if(p.getKodeBarang().equals(b.getKodeBarang()) && p.getSatuan().equals(b.getSatuan().getKodeSatuan())){
                p.setQty(pembulatan(p.getQty() + 1));
                p.setTotalHarga(pembulatan(p.getHarga()*p.getQty()));
                status = false;
                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
                searchField.requestFocus();
            }
        }
        if(status){
            PenjualanDetail p = new PenjualanDetail();
            p.setBarang(b);
            p.setNoPenjualan("");
            p.setNoUrut(0);
            p.setKodeBarang(b.getKodeBarang());
            p.setNamaBarang(b.getNamaBarang());
            p.setSatuan(b.getSatuan().getKodeSatuan());
            p.setQty(1);
            p.setNilai(0);
            p.setTotalNilai(0);
            if(kategoriLabel.getText().equalsIgnoreCase("Grosir"))
                p.setHarga(b.getSatuan().getHargaGrosir());
            else if(kategoriLabel.getText().equalsIgnoreCase("Retail"))
                p.setHarga(b.getSatuan().getHargaRetail());
            else if(kategoriLabel.getText().equalsIgnoreCase("Grosir Besar"))
                p.setHarga(b.getSatuan().getHargaGrosirBesar());
            p.setTotalNilai(0);
            p.setTotalHarga(pembulatan(p.getHarga()*p.getQty()));
            allBarang.add(0, p);
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            searchField.requestFocus();
        }
    }
    public void ubahBarang(PenjualanDetail p){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/UbahBarang.fxml");
        UbahBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setPenjualan(p, kategoriLabel.getText());
        child.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                p.setSatuan(controller.satuanCombo.getSelectionModel().getSelectedItem());
                Satuan satuan = p.getBarang().getSatuan();
                for(Satuan s : p.getBarang().getAllSatuan()){
                    if(s.getKodeSatuan().equals(controller.satuanCombo.getSelectionModel().getSelectedItem()))
                        satuan = s;
                }
                p.getBarang().setSatuan(satuan);
                p.setQty(Double.parseDouble(controller.qtyField.getText().replaceAll(",", "")));
                p.setHarga(Double.parseDouble(controller.nilaiField.getText().replaceAll(",", "")));
                p.setTotalHarga(pembulatan(p.getHarga()*p.getQty()));
                             
                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
                mainApp.closeDialog(child);
            }
        });
        child.setOnHiding( event -> {
            searchField.requestFocus();
        });
    }
    private void hitungTotal(){
        double total = 0;
        double totalDiskon = 0 ;
        for(PenjualanDetail p : allBarang){
            total = total + p.getTotalHarga();
        }
        for(PenjualanDiskon d : listPenjualanDiskon){
            totalDiskon = totalDiskon + d.getTotalDiskon();
        }
        totalPenjualanField.setText(rp.format(total));
        totalDiskonField.setText(rp.format(totalDiskon));
        grandtotalField.setText(rp.format(total - totalDiskon));
    }
    private void hapusBarang(PenjualanDetail p){
        Stage warning = new Stage();
        FXMLLoader warningLoader = mainApp.showDialog(mainApp.MainStage, warning ,"View/Dialog/Warning.fxml");
        WarningController warningController = warningLoader.getController();
        warningController.setMainApp(mainApp, warning, "Confirmation", 
                "Hapus barang "+p.getNamaBarang()+" - "+qty.format(p.getQty())+" "+p.getSatuan()+" ?");
        warning.setOnHiding( event -> {
            searchField.requestFocus();
        });
        warningController.OK.setOnAction((event) -> {
            allBarang.remove(p);
            if(allBarang.isEmpty()){
                cartLabel.setText("");
                cartHead = null;
            }
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            mainApp.closeDialog(warning);
            searchField.requestFocus();
        });
    }
    @FXML
    public void diskon(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/Diskon.fxml");
        DiskonController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setDiskon(listPenjualanDiskon);
        child.setOnHiding( event -> {
            listPenjualanDiskon = controller.allDiskon;
            hitungTotal();
            searchField.requestFocus();
        });
    }
    @FXML
    public void loadCart(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/Cart.fxml");
        CartController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setKategoriPenjualan(kategoriLabel.getText());
        child.setOnHiding( event -> {
            searchField.requestFocus();
        });
        controller.cartTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
                if(controller.cartTable.getSelectionModel().getSelectedItem()!=null){
                    if(controller.allBarang.isEmpty()){
                        mainApp.showMessage(Modality.NONE, "Warning", "Keranjang belanja belum dipilih");
                    }else{
                        Stage warning = new Stage();
                        FXMLLoader warningLoader = mainApp.showDialog(child, warning ,"View/Dialog/Warning.fxml");
                        WarningController warningController = warningLoader.getController();
                        warningController.setMainApp(mainApp, warning, "Confirmation", "Lanjutkan penjualan pada keranjang "+
                                controller.cartTable.getSelectionModel().getSelectedItem().getNoCart());
                        warning.setOnHiding(evt -> {
                            searchField.requestFocus();
                        });
                        warningController.OK.setOnAction((ev) -> {
                            Task<String> task = new Task<String>() {
                                @Override 
                                public String call() throws Exception{
                                    Thread.sleep(timeout);
                                    try(Connection con = Koneksi.getConnection()){
                                        String status = "true";
                                        String noCart = "";
                                        List<PenjualanDetail> listPenjualan = new ArrayList<>();
                                        for(CartDetail c : controller.allBarang){
                                            noCart = c.getNoCart();
                                            PenjualanDetail d = new PenjualanDetail();
                                            d.setNoPenjualan("");
                                            d.setNoUrut(0);
                                            d.setKodeBarang(c.getKodeBarang());
                                            d.setSatuan(c.getSatuan());
                                            d.setQty(c.getQty());
                                            d.setHarga(c.getHarga());
                                            d.setTotalHarga(c.getTotal());

                                            d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                                            if(d.getBarang()==null)
                                                status = d.getKodeBarang()+" - "+c.getNamaBarang()+" tidak ditemukan";
                                            else{
                                                d.setNamaBarang(d.getBarang().getNamaBarang());
                                            }

                                            d.getBarang().setSatuan(SatuanDAO.get(con, d.getKodeBarang(), d.getSatuan()));
                                            if(d.getBarang().getSatuan()==null){
                                                status = "satuan "+d.getNamaBarang()+" -"+d.getSatuan()+" tidak ditemukan";
                                            }

                                            d.getBarang().setAllSatuan(SatuanDAO.getAllByKodeBarang(con, d.getKodeBarang()));
                                            listPenjualan.add(d);
                                        }
                                        cartHead = new CartHead();
                                        cartHead.setNoCart(noCart);
                                        cartHead.setKategoriPenjualan(kategoriLabel.getText());
                                        cartHead.setListCartDetail(controller.allBarang);
                                        allBarang.clear();
                                        allBarang.addAll(listPenjualan);
                                        listPenjualanDiskon.clear();
                                        return status;
                                    }
                                }
                            };
                            task.setOnRunning((e) -> {
                                mainApp.showLoadingScreen();
                            });
                            task.setOnSucceeded((e) -> {
                                mainApp.closeLoading();
                                if(task.getValue().equals("true")){
                                    cartLabel.setText("Cart : "+cartHead.getNoCart());
                                    penjualanDetailTable.refresh();
                                    penjualanDetailTable.getSelectionModel().clearSelection();
                                    hitungTotal();
                                    mainApp.closeDialog(warning);
                                    mainApp.closeDialog(child);
                                }else{
                                    cartHead = null;
                                    allBarang.clear();
                                    listPenjualanDiskon.clear();
                                    penjualanDetailTable.refresh();
                                    penjualanDetailTable.getSelectionModel().clearSelection();
                                    hitungTotal();
                                    mainApp.showMessage(Modality.NONE, "Failed", task.getValue());
                                }
                                searchField.requestFocus();
                            });
                            task.setOnFailed((e) -> {
                                mainApp.closeLoading();
                                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                            });
                            new Thread(task).start();
                        });
                    }
                }
            }
        });
    }
    @FXML
    public void saveCart(){
        if(allBarang.isEmpty()){
            mainApp.showMessage(Modality.NONE, "Warning", "Barang masih kosong");
            searchField.requestFocus();
        }else if(cartHead==null){
            try(Connection con = Koneksi.getConnection()){
                String noCart = CartHeadDAO.getId(con);
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child ,"View/Dialog/NamaCart.fxml");
                NamaCartController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.setNamaCart(noCart);
                controller.namaCartField.selectAll();
                child.setOnHiding( event -> {
                    searchField.requestFocus();
                });
                controller.saveButton.setOnAction((event) -> {
                    Task<String> task = new Task<String>() {
                        @Override 
                        public String call() throws Exception{
                            try(Connection con = Koneksi.getConnection()){
                                return Service.saveNewCart(con, controller.namaCartField.getText(), 
                                        kategoriLabel.getText(), allBarang, null);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((WorkerStateEvent e) -> {
                        mainApp.closeLoading();
                        String status = task.getValue();
                        if(status.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Keranjang belanja berhasil disimpan di cart- "+controller.namaCartField.getText());
                            allBarang.clear();
                            listPenjualanDiskon.clear();
                            cartLabel.setText("");
                            cartHead = null;
                            penjualanDetailTable.refresh();
                            penjualanDetailTable.getSelectionModel().clearSelection();
                            hitungTotal();
                            mainApp.closeDialog(child);
                        }else
                            mainApp.showMessage(Modality.NONE, "Failed", status);
                        searchField.requestFocus();
                    });
                    task.setOnFailed((e) -> {
                        mainApp.closeLoading();
                        mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                    });
                    new Thread(task).start();
                });
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }else{
            Stage warning = new Stage();
            FXMLLoader warningLoader = mainApp.showDialog(mainApp.MainStage, warning ,"View/Dialog/Warning.fxml");
            WarningController warningController = warningLoader.getController();
            warningController.setMainApp(mainApp, warning, "Confirmation", "Simpan penjualan pada keranjang "+
                    cartHead.getNoCart());
            warning.setOnHiding( event -> {
                searchField.requestFocus();
            });
            warningController.OK.setOnAction((event) -> {
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        try(Connection con = Koneksi.getConnection()){
                            return Service.saveNewCart(con, cartHead.getNoCart(), 
                                    kategoriLabel.getText(), allBarang, cartHead);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((WorkerStateEvent e) -> {
                    mainApp.closeLoading();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Keranjang belanja berhasil disimpan di cart- "+cartHead.getNoCart());
                        allBarang.clear();
                        listPenjualanDiskon.clear();
                        cartLabel.setText("");
                        cartHead = null;
                        penjualanDetailTable.refresh();
                        penjualanDetailTable.getSelectionModel().clearSelection();
                        hitungTotal();
                        mainApp.closeDialog(warning);
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                    searchField.requestFocus();
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            });
        }
    }
    @FXML
    public void reset(){
        Stage warning = new Stage();
        FXMLLoader warningLoader = mainApp.showDialog(mainApp.MainStage, warning ,"View/Dialog/Warning.fxml");
        WarningController warningController = warningLoader.getController();
        warningController.setMainApp(mainApp, warning, "Confirmation", "Hapus semua barang penjualan ?");
        warning.setOnHiding( event -> {
            searchField.requestFocus();
        });
        warningController.OK.setOnAction((event) -> {
            allBarang.clear();
            listPenjualanDiskon.clear();
            cartLabel.setText("");
            cartHead = null;
            penjualanDetailTable.refresh();
            penjualanDetailTable.getSelectionModel().clearSelection();
            hitungTotal();
            mainApp.closeDialog(warning);
            searchField.requestFocus();
        });
    }
    @FXML
    public void checkOut(){
        if(allBarang.isEmpty()){
            mainApp.showMessage(Modality.NONE, "Warning", "Barang masih kosong");
            searchField.requestFocus();
        }else{
            Stage child2 = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child2, "View/Dialog/TotalPenjualan.fxml");
            TotalPenjualanController controller = loader.getController();
            controller.setMainApp(mainApp, child2);
            child2.setOnHiding( event -> {
                searchField.requestFocus();
            });
            controller.setText(kategoriLabel.getText(), totalPenjualanField.getText(), totalDiskonField.getText(), grandtotalField.getText());
            controller.saveButton.setOnAction((event) -> {
                mainApp.closeDialog(child2);
                String status = "";
                for(PenjualanDetail d : allBarang){
                    double hargaKomp = 0;
                    if(kategoriLabel.getText().equalsIgnoreCase("Grosir"))
                         hargaKomp = d.getBarang().getSatuan().getHargaGrosir();
                    else if(kategoriLabel.getText().equalsIgnoreCase("Retail"))
                         hargaKomp = d.getBarang().getSatuan().getHargaRetail();
                    else if(kategoriLabel.getText().equalsIgnoreCase("Grosir Besar"))
                         hargaKomp = d.getBarang().getSatuan().getHargaGrosirBesar();
                    
                    if(hargaKomp>d.getHarga())
                        status = status + " - "+rp.format(d.getQty())+" "+d.getSatuan()+"   "+d.getNamaBarang()+
                                "\n  Harga Komp : Rp "+rp.format(hargaKomp)+"  Harga Jual : Rp "+rp.format(d.getHarga())+"\n";
                }
                if(!status.equals("")){
                    Stage child = new Stage();
                    FXMLLoader loader2 = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                    VerifikasiController controller2 = loader2.getController();
                    controller2.setMainApp(mainApp, child);
                    child.setOnHiding( evt -> {
                        searchField.requestFocus();
                    });
                    controller2.textArea.setText(status);
                    controller2.passwordField.setOnKeyPressed((KeyEvent keyEvent) -> {
                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                            mainApp.closeDialog(child);
                            if(sistem.getMasterPassword().equals(controller2.passwordField.getText())){
                                savePenjualan(controller.pelanggan, Double.parseDouble(controller.totalPembayaranLabel.getText().replaceAll(",", "")));
                            }else{
                                mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi masih salah");
                                searchField.requestFocus();
                            }
                        }
                    });
                }else{
                    savePenjualan(controller.pelanggan, Double.parseDouble(controller.totalPembayaranLabel.getText().replaceAll(",", "")));
                }
            });
        }
    }
    private void savePenjualan(Pelanggan pelanggan, double pembayaran){
        PenjualanHead p = new PenjualanHead();
        Task<String> task = new Task<String>() {
            @Override 
            public String call() throws Exception{
                try(Connection con = Koneksi.getConnection()){
                    p.setNoPenjualan("");
                    p.setTglPenjualan(Function.getSystemDate());
                    p.setKategoriPenjualan(kategoriLabel.getText());
                    if(pelanggan!=null){
                        p.setPelanggan(pelanggan.getKodePelanggan());
                        p.setCustomer(pelanggan);
                    }else
                        p.setPelanggan("");
                    
                    p.setTotalPenjualan(Double.parseDouble(totalPenjualanField.getText().replaceAll(",", "")));
                    p.setTotalDiskon(Double.parseDouble(totalDiskonField.getText().replaceAll(",", "")));
                    p.setGrandtotal(Double.parseDouble(grandtotalField.getText().replaceAll(",", "")));
                    
                    List<Pembayaran> listPembayaran = new ArrayList<>();
                    Pembayaran pp = new Pembayaran();
                    pp.setJenisPembayaran("Cash");
                    pp.setKeterangan("");
                    pp.setJumlahPembayaran(pembayaran);
                    listPembayaran.add(pp);
                    p.setListPembayaran(listPembayaran);
                    
                    p.setListPenjualanDiskon(listPenjualanDiskon);
                    
                    p.setPembayaran(pembayaran);
                    p.setSisaPembayaran(Double.parseDouble(grandtotalField.getText().replaceAll(",", ""))-pembayaran);
                    p.setKodeUser(user.getKodeUser());
                    p.setStatus("true");
                    p.setTglBatal("2000-01-01 00:00:00");
                    p.setUserBatal("");
                    List<PenjualanDetail> allDetail = new ArrayList<>();
                    int i = 1;
                    for(PenjualanDetail d : allBarang){
                        PenjualanDetail detail = new PenjualanDetail();
                        detail.setBarang(d.getBarang());
                        detail.setNoPenjualan("");
                        detail.setNoUrut(i);
                        detail.setKodeBarang(d.getKodeBarang());
                        detail.setNamaBarang(d.getNamaBarang());
                        detail.setSatuan(d.getSatuan());
                        detail.setQty(d.getQty());
                        detail.setQtyKeluar(pembulatan(d.getQty()*d.getBarang().getSatuan().getQty()));
                        detail.setNilai(0);
                        detail.setHarga(d.getHarga());
                        detail.setTotalNilai(0);
                        detail.setTotalHarga(detail.getHarga()*detail.getQty());
                        i++;
                        allDetail.add(detail);
                    }
                    p.setListPenjualanDetail(allDetail);
                    return Service.savePenjualan(con, p, cartHead);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((evt) -> {
            mainApp.closeLoading();
            String status = task.getValue();
            if(status.equals("true")){
                mainApp.showMessage(Modality.NONE, "Success", "Penjualan berhasil disimpan");
                if(printCheck.isSelected()){
                    try{
                        PrintOut po = new PrintOut();
                        po.printStrukPenjualanDirect(p);
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error Printing", e.toString());
                    }
                }
                allBarang.clear();
                listPenjualanDiskon.clear();
                cartLabel.setText("");
                cartHead = null;
                penjualanDetailTable.refresh();
                penjualanDetailTable.getSelectionModel().clearSelection();
                hitungTotal();
            }else
                mainApp.showMessage(Modality.NONE, "Failed", status);
            searchField.requestFocus();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    
}
