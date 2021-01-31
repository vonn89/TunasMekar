/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TunasMekar.View.Dialog;

import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.PembelianHead;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class PembayaranController  {

    @FXML private TextField noTransaksiField;
    @FXML private TextField totalTransaksiField;
    @FXML private TextField sudahTerbayarField;
    @FXML private TextField sisaPembayaranField;
    @FXML public TextField jumlahPembayaranField;
    @FXML public Button saveButton;
    private Main mainApp;   
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        jumlahPembayaranField.setOnKeyReleased((event) -> {
            try{
                String string = jumlahPembayaranField.getText();
                if(string.indexOf(".")>0){
                    String string2 = string.substring(string.indexOf(".")+1, string.length());
                    if(string2.contains("."))
                        jumlahPembayaranField.undo();
                    else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                        jumlahPembayaranField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                }else
                    jumlahPembayaranField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                jumlahPembayaranField.end();
            }catch(Exception e){
                jumlahPembayaranField.undo();
            }
        });
    }   
    public void setPembayaranPenjualan(PenjualanHead p){
        noTransaksiField.setText(p.getNoPenjualan());
        totalTransaksiField.setText(rp.format(p.getTotalPenjualan()));
        sudahTerbayarField.setText(rp.format(p.getPembayaran()));
        sisaPembayaranField.setText(rp.format(p.getSisaPembayaran()));
    }
    public void setPembayaranPembelian(PembelianHead p){
        noTransaksiField.setText(p.getNoPembelian());
        totalTransaksiField.setText(rp.format(p.getTotalPembelian()));
        sudahTerbayarField.setText(rp.format(p.getPembayaran()));
        sisaPembayaranField.setText(rp.format(p.getSisaPembayaran()));
    }
    public void close(){
        mainApp.closeDialog(stage);
    }    
    
}
