/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.PenyesuaianStokBarangDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PenyesuaianStokBarangHeadDAO;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangDetail;
import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangHead;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class DetailPenyesuaianStokBarangController {

    @FXML private TextField noPenyesuaianField;
    @FXML private TextField tglPenyesuaianField;
    @FXML private TextField jenisPenyesuaianField;
    @FXML private TextArea catatanField;
    @FXML private TextField kodeUserField;
    
    @FXML private TableView<PenyesuaianStokBarangDetail> penyesuaianDetailTable;
    @FXML private TableColumn numberColumn = new TableColumn( "Number" );
    @FXML private TableColumn<PenyesuaianStokBarangDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenyesuaianStokBarangDetail, String> satuanColumn;
    @FXML private TableColumn<PenyesuaianStokBarangDetail, Number> qtyColumn;
    @FXML private TableColumn<PenyesuaianStokBarangDetail, Number> qtyStokColumn;
    @FXML private TableColumn<PenyesuaianStokBarangDetail, Number> nilaiColumn;
    @FXML private TableColumn<PenyesuaianStokBarangDetail, Number> totalNilaiColumn;
    
    @FXML private Label totalQtyLabel;
    @FXML private Label totalNilaiLabel;
    
    private Stage stage;
    private Main mainApp;   
    private ObservableList<PenyesuaianStokBarangDetail> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        numberColumn.setCellFactory((p) -> new TableCell(){
            @Override
            public void updateItem( Object item, boolean empty ){
                super.updateItem( item, empty );
                setGraphic( null );
                setText( empty ? null : getIndex() + 1 + "" );
            }
        });
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        satuanColumn.setCellValueFactory(cellData -> cellData.getValue().satuanProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(qty));
        qtyStokColumn.setCellValueFactory(cellData -> cellData.getValue().qtyStokProperty());
        qtyStokColumn.setCellFactory(col -> getTableCell(qty));
        nilaiColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiProperty());
        nilaiColumn.setCellFactory(col -> getTableCell(rp));
        totalNilaiColumn.setCellValueFactory(cellData -> cellData.getValue().totalNilaiProperty());
        totalNilaiColumn.setCellFactory(col -> getTableCell(rp));
        
        final ContextMenu rm = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e)->{
            penyesuaianDetailTable.refresh();
        });
        rm.getItems().addAll(refresh);
        penyesuaianDetailTable.setContextMenu(rm);
        penyesuaianDetailTable.setRowFactory(ttv -> {
            TableRow<PenyesuaianStokBarangDetail> row = new TableRow<PenyesuaianStokBarangDetail>() {
                @Override
                public void updateItem(PenyesuaianStokBarangDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rm);
                    } else{
                        ContextMenu rm = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            penyesuaianDetailTable.refresh();
                        });
                        rm.getItems().add(refresh);
                        setContextMenu(rm);
                    }
                }
            };
            return row;
        });
        penyesuaianDetailTable.setItems(allBarang);
    }    
    public void setMainApp(Main main, Stage stage) {
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
    }
    public void setPenyesuaianStokBarang(String noPenyesuaian) {
        Task<PenyesuaianStokBarangHead> task = new Task<PenyesuaianStokBarangHead>() {
            @Override 
            public PenyesuaianStokBarangHead call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    PenyesuaianStokBarangHead p = PenyesuaianStokBarangHeadDAO.get(con, noPenyesuaian);
                    p.setListPenyesuaianStokBarangDetail(PenyesuaianStokBarangDetailDAO.getAll(con, noPenyesuaian));
                    for(PenyesuaianStokBarangDetail d : p.getListPenyesuaianStokBarangDetail()){
                        d.setBarang(BarangDAO.get(con, d.getKodeBarang()));
                    }
                    return p;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent ev) -> {
            try{
                mainApp.closeLoading();
                PenyesuaianStokBarangHead p = task.getValue();
                noPenyesuaianField.setText(p.getNoPenyesuaian());
                tglPenyesuaianField.setText(tglLengkap.format(tglSql.parse(p.getTglPenyesuaian())));
                jenisPenyesuaianField.setText(p.getJenisPenyesuaian());
                catatanField.setText(p.getCatatan());
                kodeUserField.setText(p.getKodeUser());
                allBarang.clear();
                allBarang.addAll(p.getListPenyesuaianStokBarangDetail());
                hitungTotal();
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
    private void hitungTotal(){
        double totalQty = 0;
        double totalNilai = 0;
        for(PenyesuaianStokBarangDetail p : allBarang){
            totalQty = totalQty + p.getQty();
            totalNilai = totalNilai + p.getTotalNilai();
        }
        totalQtyLabel.setText(qty.format(totalQty));
        totalNilaiLabel.setText(qty.format(totalNilai));
    }
    @FXML
    private void close() {
        mainApp.closeDialog(stage);
    }
}
