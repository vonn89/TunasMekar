package com.excellentsystem.TunasMekar.View;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglNormal;
import static com.excellentsystem.TunasMekar.Main.user;
import com.excellentsystem.TunasMekar.Model.Otoritas;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class MainController  {

    @FXML private Label title;
    @FXML private Label tglSystemLabel;
    
    @FXML private StackPane stackPane;
    @FXML public VBox menuPane;
    
    @FXML private Accordion accordion;
    @FXML private VBox userVbox;
    @FXML private TitledPane loginButton;
    @FXML private MenuButton logoutButton;
    @FXML private MenuButton ubahPasswordButton;
    
    @FXML private VBox penjualanVbox;
    @FXML private StackPane penjualanGrosirBesarPane;
    @FXML private StackPane penjualanGrosirPane;
    @FXML private StackPane penjualanRetailPane;
    
    @FXML private TitledPane dataPelangganPane;
    
    @FXML private TitledPane dataBarangPane;
    
    @FXML private TitledPane stokBarangPane;
    
    @FXML private TitledPane dataPenjualanPane;
    
    @FXML private TitledPane dataPembelianPane;
    
    @FXML private TitledPane keuanganPane;
    
    @FXML private TitledPane laporanPane;
    @FXML private VBox laporanVbox;
    @FXML private MenuButton menuLaporanBarang;
    @FXML private MenuButton menuLaporanPenjualan;
    @FXML private MenuButton menuLaporanPembelian;
    @FXML private MenuButton menuLaporanKeuangan;
    @FXML private MenuButton menuLaporanManagerial;
    
    @FXML private TitledPane pengaturanPane;
    @FXML private VBox pengaturanVbox;
    @FXML private MenuButton menuPengaturanUmum;
    @FXML private MenuButton menuPengaturanDiskon;
    @FXML private MenuButton menuDataKategoriBarang;
    @FXML private MenuButton menuDataUser;
    
    private Main mainApp;
    public void setMainApp(Main mainApp) {
        try{
            this.mainApp = mainApp;
            menuPane.setPrefWidth(0);
            for(Node n : penjualanVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : laporanVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : pengaturanVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : userVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            title.setText(sistem.getNama());
            tglSystemLabel.setText(tglNormal.format(tglBarang.parse(sistem.getTglSystem())));
            setUser();
            if(user.getKodeUser().equals("grosir"))
                showPenjualanGrosir();
            else if(user.getKodeUser().equals("retail"))
                showPenjualanRetail();
            else
                showPenjualanGrosir();
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }    
    public void setUser() {
        if(user==null){
            loginButton.setText("Login");
            logoutButton.setVisible(false);
            ubahPasswordButton.setVisible(false);
            mainApp.showLoginScene();
        }else{
            logoutButton.setVisible(true);
            ubahPasswordButton.setVisible(true);
            loginButton.setText(user.getKodeUser());
            for(Otoritas o : user.getOtoritas()){
                if(o.getJenis().equals("Penjualan Grosir Besar") && o.isStatus()==false){
                    penjualanGrosirBesarPane.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Penjualan Grosir") && o.isStatus()==false){
                    penjualanGrosirPane.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Penjualan Retail") && o.isStatus()==false){
                    penjualanRetailPane.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Data Pelanggan") && o.isStatus()==false){
                    accordion.getPanes().remove(dataPelangganPane);
                }else if(o.getJenis().equals("Data Barang") && o.isStatus()==false){
                    accordion.getPanes().remove(dataBarangPane);
                }else if(o.getJenis().equals("Stok Barang") && o.isStatus()==false){
                    accordion.getPanes().remove(stokBarangPane);
                }else if(o.getJenis().equals("Data Penjualan") && o.isStatus()==false){
                    accordion.getPanes().remove(dataPenjualanPane);
                }else if(o.getJenis().equals("Data Pembelian") && o.isStatus()==false){
                    accordion.getPanes().remove(dataPembelianPane);
                }else if(o.getJenis().equals("Keuangan") && o.isStatus()==false){
                    accordion.getPanes().remove(keuanganPane);
                }else if(o.getJenis().equals("Laporan") && o.isStatus()==false){
                    accordion.getPanes().remove(laporanPane);
                }else if(o.getJenis().equals("Pengaturan") && o.isStatus()==false){
                    accordion.getPanes().remove(pengaturanPane);
                }
            }
        }
    }
    @FXML
    public void showHideMenu(){
        final Animation hideSidebar = new Transition() { 
            { setCycleDuration(Duration.millis(250)); }
            @Override
            protected void interpolate(double frac) {
                final double curWidth = 220 * (1.0 - frac) ;
                menuPane.setPrefWidth(curWidth);
                laporanPane.setExpanded(false);
                pengaturanPane.setExpanded(false);
                loginButton.setExpanded(false);
            }
        };
        final Animation showSidebar = new Transition() {
            { setCycleDuration(Duration.millis(250)); }
            @Override
            protected void interpolate(double frac) {
              final double curWidth = 220 * frac ;
              menuPane.setPrefWidth(curWidth);
            }
        };
        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (menuPane.getPrefWidth()!=0) 
                hideSidebar.play();
            else 
                showSidebar.play();
        }
    }
    @FXML
    private void showQuickButton(){
        final Animation hideSidebar = new Transition() {
            {setCycleDuration(Duration.millis(250));}
            @Override
            protected void interpolate(double frac) {
                final double curWidth = 50 * (1.0 - frac);
                stackPane.setPrefWidth(curWidth);
            }
        };
        hideSidebar.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent) -> {
            stackPane.setVisible(false);
        });
        final Animation showSidebar = new Transition() {
            {setCycleDuration(Duration.millis(250));}
            @Override
            protected void interpolate(double frac) {
                final double curWidth = 50 * frac;
                stackPane.setPrefWidth(curWidth);
            }
        };
        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (stackPane.isVisible()) 
                hideSidebar.play();
            else {
                stackPane.setVisible(true);
                showSidebar.play();
            }
            
        }
    }
    public void setTitle(String x){
        title.setText(x);
    }
    @FXML
    private void logout(){
        mainApp.showLoginScene();
    }
    @FXML
    private void exit(){
        System.exit(0);
    }
    @FXML
    private void showUbahPassword(){
        mainApp.showUbahPassword();
    }     
    @FXML
    private void showPengaturanUmum(){
        mainApp.showPengaturanUmum();
    }
    @FXML
    private void showPengaturanDiskon(){
        mainApp.showPengaturanDiskon();
    }
    @FXML
    private void showKategoriBarang(){
        mainApp.showKategoriBarang();
    }
    @FXML
    private void showDataUser(){
        mainApp.showDataUser();
    }
    @FXML
    private void showDataPelanggan(){
        mainApp.showDataPelanggan();
    }
    @FXML
    private void showDataBarang(){
        mainApp.showDataBarang();
    }
    @FXML
    private void showStokBarang(){
        mainApp.showStokBarang();
    }
    @FXML
    private void showDataPenjualan(){
        mainApp.showDataPenjualan();
    }
    @FXML
    private void showDataPembelian(){
        mainApp.showDataPembelian();
    }
    @FXML
    private void showDataKeuangan(){
        mainApp.showDataKeuangan();
    }
    @FXML
    private void showPenjualanGrosir(){
        mainApp.showPenjualanGrosir();
    }
    @FXML
    private void showPenjualanRetail(){
        mainApp.showPenjualanRetail();
    }
    @FXML
    private void showPenjualanGrosirBesar(){
        mainApp.showPenjualanGrosirBesar();
    }
    @FXML
    private void showLaporanBarang(){
        mainApp.showLaporanBarang();
    }
    @FXML
    private void showLaporanStokBarang(){
        mainApp.showLaporanStokBarang();
    }
    @FXML
    private void showLaporanLogBarang(){
        mainApp.showLaporanLogBarang();
    }
    @FXML
    private void showLaporanKartuStokBarang(){
        mainApp.showLaporanKartuStokBarang();
    }
    @FXML
    private void showLaporanPenyesuaianStokBarang(){
        mainApp.showLaporanPenyesuaianStokBarang();
    }
    @FXML
    private void showLaporanPenjualan(){
        mainApp.showLaporanPenjualan();
    }
    @FXML
    private void showLaporanBarangTerjual(){
        mainApp.showLaporanBarangTerjual();
    }
    @FXML
    private void showLaporanBarangHampirHabis(){
        mainApp.showLaporanBarangHampirHabis();
    }
    @FXML
    private void showLaporanBarangTidakLaku(){
        mainApp.showLaporanBarangTidakLaku();
    }
    @FXML
    private void showLaporanPembelian(){
        mainApp.showLaporanPembelian();
    }
    @FXML
    private void showLaporanBarangDibeli(){
        mainApp.showLaporanBarangDibeli();
    }
}
