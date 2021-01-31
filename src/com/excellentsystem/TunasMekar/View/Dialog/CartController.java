/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.CartDetailDAO;
import com.excellentsystem.TunasMekar.DAO.CartHeadDAO;
import com.excellentsystem.TunasMekar.Function;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.CartDetail;
import com.excellentsystem.TunasMekar.Model.CartHead;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import java.sql.Connection;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class CartController {

    @FXML public TableView<CartHead> cartTable;
    @FXML private TableColumn<CartHead, String> kodeCartColumn;
    @FXML private TableColumn removeColumn;
    
    @FXML private TableView<CartDetail> cartDetailTable;
    @FXML private TableColumn<CartDetail, String> namaBarangColumn;
    @FXML private TableColumn<CartDetail, String> satuanColumn;
    @FXML private TableColumn<CartDetail, Number> qtyColumn;
    @FXML private TableColumn<CartDetail, Number> hargaColumn;
    @FXML private TableColumn<CartDetail, Number> totalColumn;
    @FXML private TextField totalPenjualanField;
    
    private String kategoriPenjualan = "%";
    private Stage stage;
    private Main mainApp;   
    private ObservableList<CartHead> allCart = FXCollections.observableArrayList();
    public ObservableList<CartDetail> allBarang = FXCollections.observableArrayList();
    private Callback<TableColumn, TableCell> getDeleteButtonCell(){
        return (final TableColumn p) -> new TableCell<CartHead, Boolean>(){
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
                        CartHead detail = (CartHead) getTableView().getItems().get(getIndex());
                        hapusCart(detail);
                    });
                    setGraphic(btn);
                }
            }
        };
    }
    public void initialize() {
        kodeCartColumn.setCellValueFactory(cellData -> cellData.getValue().noCartProperty());
        removeColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        removeColumn.setCellFactory(getDeleteButtonCell());
        
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().satuanProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        totalColumn.setCellFactory((param) -> Function.getTableCell(rp));
        
        final ContextMenu rmCart = new ContextMenu();
        MenuItem refreshCart = new MenuItem("Refresh");
        refreshCart.setOnAction((ActionEvent e)->{
            getCart();
        });
        rmCart.getItems().addAll(refreshCart);
        cartTable.setContextMenu(rmCart);
        cartTable.setRowFactory(ttv -> {
            TableRow<CartHead> row = new TableRow<CartHead>() {
                @Override
                public void updateItem(CartHead item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rmCart);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem hapus = new MenuItem("Hapus Cart");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusCart(item);
                        });
                        MenuItem refreshCart = new MenuItem("Refresh");
                        refreshCart.setOnAction((ActionEvent event) -> {
                            getCart();
                        });
                        rm.getItems().addAll(hapus, refreshCart);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
        cartTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> getBarang());
        cartTable.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.DELETE) {
                if(cartTable.getSelectionModel().getSelectedItem()!=null)
                    hapusCart(cartTable.getSelectionModel().getSelectedItem());
            }
        });
        
        final ContextMenu rm = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            cartDetailTable.refresh();
        });
        rm.getItems().addAll(refresh);
        cartDetailTable.setContextMenu(rm);
        cartDetailTable.setRowFactory(ttv -> {
            TableRow<CartDetail> row = new TableRow<CartDetail>() {
                @Override
                public void updateItem(CartDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            cartDetailTable.refresh();
                        });
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
    }    
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        cartDetailTable.setItems(allBarang);
        cartTable.setItems(allCart);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) {
                close();
            }
            if (event.getCode() == KeyCode.DOWN) {
                if(!cartTable.isFocused()){
                    Platform.runLater(() -> {
                        cartTable.requestFocus();
                        cartTable.getSelectionModel().select(0);
                        cartTable.getFocusModel().focus(0);
                    });
                }
            }
        });
    } 
    public void setKategoriPenjualan(String kategoriPenjualan){
        this.kategoriPenjualan = kategoriPenjualan;
        getCart();
    }
    private void getCart(){
        Task<List<CartHead>> task = new Task<List<CartHead>>() {
            @Override 
            public List<CartHead> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    return CartHeadDAO.getAllByKategoriPenjualan(con, kategoriPenjualan);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            mainApp.closeLoading();
            allCart.clear();
            allCart.addAll(task.getValue());
            cartTable.getSelectionModel().selectFirst();
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    private void hapusCart(CartHead c){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Hapus keranjang "+c.getNoCart()+" ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deleteCart(con, c);
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
                    allBarang.clear();
                    double total = 0;
                    for(CartDetail d : allBarang){
                        total = total + d.getQty() * d.getHarga();
                    }
                    totalPenjualanField.setText(rp.format(total));
                    getCart();
                    mainApp.showMessage(Modality.NONE, "Success", "Keranjang belanja berhasil dihapus");
                }else
                    mainApp.showMessage(Modality.NONE, "Failed", status);
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        });
    }
    private void getBarang(){
        allBarang.clear();
        totalPenjualanField.setText(rp.format(0));
        if(cartTable.getSelectionModel().getSelectedItem()!=null){
            Task<List<CartDetail>> task = new Task<List<CartDetail>>() {
                @Override 
                public List<CartDetail> call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return CartDetailDAO.getAll(con, cartTable.getSelectionModel().getSelectedItem().getNoCart());
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((e) -> {
                mainApp.closeLoading();
                allBarang.addAll(task.getValue());
                double total = 0;
                for(CartDetail d : allBarang){
                    total = total + d.getQty() * d.getHarga();
                }
                totalPenjualanField.setText(rp.format(total));
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
