/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Barang;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class PilihSatuanController {

    @FXML public FlowPane flowPane;
    
    public ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
    }     
    private void selectPrev(){
        int selected = 1;
        for(Node n : flowPane.getChildren()){
            if(n instanceof VBox){
                VBox v = (VBox) n;
                if(v.isFocused())
                    selected = flowPane.getChildren().indexOf(v);
            }
        }
        if(selected-1>=0){
            flowPane.getChildren().get(selected-1).requestFocus();
        }
    }
    private void selectNext(){
        int selected = -1;
        for(Node n : flowPane.getChildren()){
            if(n instanceof VBox){
                VBox v = (VBox) n;
                if(v.isFocused())
                    selected = flowPane.getChildren().indexOf(v);
            }
        }
        if(selected+1<flowPane.getChildren().size()){
            flowPane.getChildren().get(selected+1).requestFocus();
        }
    }
    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ESCAPE) 
                close();
            if (event.getCode() == KeyCode.UP) 
                selectPrev();
            if (event.getCode() == KeyCode.DOWN) 
                selectNext();
            if (event.getCode() == KeyCode.LEFT) 
                selectPrev();
            if (event.getCode() == KeyCode.RIGHT) 
                selectNext();
        });
    } 
    public void setBarang(List<Barang> listBarang, String kategori){
        allBarang.addAll(listBarang);
        flowPane.getChildren().clear();
        for(Barang b : allBarang){
            VBox vbox = new VBox();
            vbox.setId(b.getKodeBarang()+"-"+b.getSatuan().getKodeSatuan());
            vbox.setPrefSize(180, 180);
            vbox.setAlignment(Pos.CENTER);
            vbox.getStyleClass().clear();
            vbox.getStyleClass().add("barang");
            
            Label nama = new Label(b.getNamaBarang());
            nama.setStyle("-fx-font-family: serif;" +
                            "-fx-font-size: 18;");
            nama.setTextAlignment(TextAlignment.CENTER);
            nama.setWrapText(true);

            Label satuan = new Label("( "+b.getSatuan().getKodeSatuan()+" )");
            satuan.setStyle("-fx-font-size: 14;"
//                    + "-fx-text-fill: seccolor1;"
            );
            satuan.setTextAlignment(TextAlignment.CENTER);
            satuan.setWrapText(true);

            vbox.getChildren().add(nama);
            vbox.getChildren().add(satuan);
            
            Label harga = new Label("Rp 0");
            
            if(kategori.equalsIgnoreCase("Grosir Besar"))
                harga.setText("Rp "+rp.format(b.getSatuan().getHargaGrosirBesar()));
            else if(kategori.equalsIgnoreCase("Grosir"))
                harga.setText("Rp "+rp.format(b.getSatuan().getHargaGrosir()));
            else if(kategori.equalsIgnoreCase("Retail"))
                harga.setText("Rp "+rp.format(b.getSatuan().getHargaRetail()));
            else if(kategori.equalsIgnoreCase("Pembelian"))
                harga.setText("");
            
            harga.setStyle("-fx-font-size: 14;"
//                    + "-fx-text-fill: red;"
                    + "-fx-font-weight: bold;");
            harga.setTextAlignment(TextAlignment.CENTER);
            harga.setWrapText(true);
            vbox.getChildren().add(harga);

            flowPane.getChildren().add(vbox);
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
}
