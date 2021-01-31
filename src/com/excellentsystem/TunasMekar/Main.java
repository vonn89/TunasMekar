/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar;

import com.excellentsystem.TunasMekar.DAO.OtoritasDAO;
import com.excellentsystem.TunasMekar.DAO.UserDAO;
import static com.excellentsystem.TunasMekar.Function.createSecretKey;
import com.excellentsystem.TunasMekar.Model.Otoritas;
import com.excellentsystem.TunasMekar.Model.Sistem;
import com.excellentsystem.TunasMekar.Model.User;
import com.excellentsystem.TunasMekar.Report.LaporanBarangController;
import com.excellentsystem.TunasMekar.Report.LaporanBarangDibeliController;
import com.excellentsystem.TunasMekar.Report.LaporanBarangHampirHabisController;
import com.excellentsystem.TunasMekar.Report.LaporanBarangTerjualController;
import com.excellentsystem.TunasMekar.Report.LaporanBarangTidakLakuController;
import com.excellentsystem.TunasMekar.Report.LaporanKartuStokBarangController;
import com.excellentsystem.TunasMekar.Report.LaporanLogBarangController;
import com.excellentsystem.TunasMekar.Report.LaporanPembelianController;
import com.excellentsystem.TunasMekar.Report.LaporanPenjualanController;
import com.excellentsystem.TunasMekar.Report.LaporanPenyesuaianStokBarangController;
import com.excellentsystem.TunasMekar.Report.LaporanStokBarangController;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.DataBarangController;
import com.excellentsystem.TunasMekar.View.DataPelangganController;
import com.excellentsystem.TunasMekar.View.DataPembelianController;
import com.excellentsystem.TunasMekar.View.DataPenjualanController;
import com.excellentsystem.TunasMekar.View.DataUserController;
import com.excellentsystem.TunasMekar.View.Dialog.KategoriBarangController;
import com.excellentsystem.TunasMekar.View.Dialog.KeuanganController;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import com.excellentsystem.TunasMekar.View.Dialog.PengaturanDiskonController;
import com.excellentsystem.TunasMekar.View.Dialog.PengaturanUmumController;
import com.excellentsystem.TunasMekar.View.Dialog.UbahPasswordController;
import com.excellentsystem.TunasMekar.View.LoginController;
import com.excellentsystem.TunasMekar.View.MainController;
import com.excellentsystem.TunasMekar.View.PenjualanController;
import com.excellentsystem.TunasMekar.View.StokBarangController;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Yunaz
 */
public class Main extends Application {
    public Stage MainStage;
    public Stage message;
    public Stage loading;
    
    public Dimension screenSize;
    public MainController mainController;
    public BorderPane mainLayout;
    
    private double x = 0;
    private double y = 0;
    private double z = 0;
    
    public static DecimalFormat qty = new DecimalFormat("###,##0.##");
    public static DecimalFormat rp = new DecimalFormat("###,##0");
    public static DateFormat tglKode = new SimpleDateFormat("yyMMdd");
    public static DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat tglNormal = new SimpleDateFormat("dd MMM yyyy");
    public static DateFormat tglSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static DateFormat tglLengkap = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
    
    public static Sistem sistem;
    public static User user;
    public static List<User> allUser;
    
    public static long timeout = 0;
    public static final String version = "1.0.0";
    public static SecretKeySpec key;
    @Override
    public void start(Stage stage) {
        try(Connection con = Koneksi.getConnection()){
            String password = "password";
            byte[] salt = "12345678".getBytes();
            key = createSecretKey(password.toCharArray(), salt, 40000, 128);
            
            Service.setSistem(con);
            allUser = UserDAO.getAllByStatus(con, "true");
            List<Otoritas> listOtoritas = OtoritasDAO.getAll(con);
            for(User u : allUser){
                List<Otoritas> otoritas = new ArrayList<>();
                for(Otoritas o : listOtoritas){
                    if(o.getKodeUser().equals(u.getKodeUser()))
                        otoritas.add(o);
                }
                u.setOtoritas(otoritas);
            }
            MainStage = stage;
            MainStage.setTitle(sistem.getNama());
            MainStage.setMaximized(true);
            MainStage.getIcons().add(new Image(Main.class.getResourceAsStream("Resource/icon.png")));
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            showLoginScene();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
            System.exit(0);
        }
    }
    public void showLoginScene() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Login.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            
            Scene scene = new Scene(container);
            final Animation animationshow = new Transition() {
                { setCycleDuration(Duration.millis(1000)); }
                @Override
                protected void interpolate(double frac) {
                    MainStage.setOpacity(1-frac);
                }
            };
            animationshow.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent) -> {
                final Animation animation = new Transition() {
                    { setCycleDuration(Duration.millis(1000)); }
                    @Override
                    protected void interpolate(double frac) {
                        MainStage.setOpacity(frac);
                    }
                };
                animation.play();
                MainStage.hide();
                MainStage.setScene(scene);
                MainStage.show();
            });
            animationshow.play();
            LoginController controller = loader.getController();
            controller.setMainApp(this);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    public void showMainScene(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Main.fxml"));
            mainLayout = (BorderPane) loader.load();
            Scene scene = new Scene(mainLayout);
            
            MainStage.hide();
            MainStage.setScene(scene);
            MainStage.show();
            
            mainController = loader.getController();
            mainController.setMainApp(this);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    public DataPelangganController showDataPelanggan(){
        FXMLLoader loader = changeStage("View/DataPelanggan.fxml");
        DataPelangganController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Data Pelanggan");
        return controller;
    }
    public DataBarangController showDataBarang(){
        FXMLLoader loader = changeStage("View/DataBarang.fxml");
        DataBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Data Barang");
        return controller;
    }
    public StokBarangController showStokBarang(){
        FXMLLoader loader = changeStage("View/StokBarang.fxml");
        StokBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Stok Barang");
        return controller;
    }
    public DataPenjualanController showDataPenjualan(){
        FXMLLoader loader = changeStage("View/DataPenjualan.fxml");
        DataPenjualanController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Data Penjualan");
        return controller;
    }
    public DataPembelianController showDataPembelian(){
        FXMLLoader loader = changeStage("View/DataPembelian.fxml");
        DataPembelianController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Data Pembelian");
        return controller;
    }
    public DataUserController showDataUser(){
        FXMLLoader loader = changeStage("View/DataUser.fxml");
        DataUserController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Data User");
        return controller;
    }
    public PenjualanController showPenjualanGrosirBesar(){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Penjualan.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            BorderPane border = (BorderPane) mainLayout.getCenter();
            border.setCenter(container);
            PenjualanController controller = loader.getController();
            controller.setMainApp(this);
            controller.setKategoriPenjualan("GROSIR BESAR");
            container.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                if (event.getCode() == KeyCode.ENTER) {
                    controller.searchField.requestFocus();
                    controller.searchField.selectAll();
                }
                if (event.getCode() == KeyCode.DOWN) {
                    if(!controller.penjualanDetailTable.isFocused()){
                        Platform.runLater(() -> {
                            controller.penjualanDetailTable.requestFocus();
                            controller.penjualanDetailTable.getSelectionModel().select(0);
                            controller.penjualanDetailTable.getFocusModel().focus(0);
                        });
                    }
                }
                if (event.getCode() == KeyCode.F1) 
                    controller.loadCart();
                if (event.getCode() == KeyCode.F2) 
                    controller.saveCart();
                if (event.getCode() == KeyCode.F3) 
                    controller.reset();
                if (event.getCode() == KeyCode.F4) 
                    controller.checkOut();
                if (event.getCode() == KeyCode.F5) 
                    controller.cariBarang();
                if (event.getCode() == KeyCode.F6){ 
                    if(controller.printCheck.isSelected())
                        controller.printCheck.setSelected(false);
                    else if(!controller.printCheck.isSelected())
                        controller.printCheck.setSelected(true);
                }
                if (event.getCode() == KeyCode.F7){ 
                    controller.diskon();
                }
                if (event.getCode() == KeyCode.F12) {
                    if(!controller.allBarang.isEmpty()){
                        controller.penjualanDetailTable.getSelectionModel().selectFirst();
                        controller.ubahBarang(controller.penjualanDetailTable.getSelectionModel().getSelectedItem());
                    }
                }
            });
            setTitle("Penjualan Grosir Besar");
            return controller;
        }catch(Exception e){
            e.printStackTrace();
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public PenjualanController showPenjualanGrosir(){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Penjualan.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            BorderPane border = (BorderPane) mainLayout.getCenter();
            border.setCenter(container);
            PenjualanController controller = loader.getController();
            controller.setMainApp(this);
            controller.setKategoriPenjualan("GROSIR");
            container.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                if (event.getCode() == KeyCode.ENTER) {
                    controller.searchField.requestFocus();
                    controller.searchField.selectAll();
                }
                if (event.getCode() == KeyCode.DOWN) {
                    if(!controller.penjualanDetailTable.isFocused()){
                        Platform.runLater(() -> {
                            controller.penjualanDetailTable.requestFocus();
                            controller.penjualanDetailTable.getSelectionModel().select(0);
                            controller.penjualanDetailTable.getFocusModel().focus(0);
                        });
                    }
                }
                if (event.getCode() == KeyCode.F1) 
                    controller.loadCart();
                if (event.getCode() == KeyCode.F2) 
                    controller.saveCart();
                if (event.getCode() == KeyCode.F3) 
                    controller.reset();
                if (event.getCode() == KeyCode.F4) 
                    controller.checkOut();
                if (event.getCode() == KeyCode.F5) 
                    controller.cariBarang();
                if (event.getCode() == KeyCode.F6){ 
                    if(controller.printCheck.isSelected())
                        controller.printCheck.setSelected(false);
                    else if(!controller.printCheck.isSelected())
                        controller.printCheck.setSelected(true);
                }
                if (event.getCode() == KeyCode.F7){ 
                    controller.diskon();
                }
                if (event.getCode() == KeyCode.F12) {
                    controller.penjualanDetailTable.getSelectionModel().selectFirst();
                    controller.ubahBarang(controller.penjualanDetailTable.getSelectionModel().getSelectedItem());
                }
            });
            setTitle("Penjualan Grosir");
            return controller;
        }catch(Exception e){
            e.printStackTrace();
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public PenjualanController showPenjualanRetail(){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("View/Penjualan.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            BorderPane border = (BorderPane) mainLayout.getCenter();
            border.setCenter(container);
            PenjualanController controller = loader.getController();
            controller.setMainApp(this);
            controller.setKategoriPenjualan("RETAIL");
            container.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                if (event.getCode() == KeyCode.ENTER) {
                    controller.searchField.requestFocus();
                    controller.searchField.selectAll();
                }
                if (event.getCode() == KeyCode.DOWN) {
                    if(!controller.penjualanDetailTable.isFocused()){
                        Platform.runLater(() -> {
                            controller.penjualanDetailTable.requestFocus();
                            controller.penjualanDetailTable.getSelectionModel().select(0);
                            controller.penjualanDetailTable.getFocusModel().focus(0);
                        });
                    }
                }
                if (event.getCode() == KeyCode.F1) 
                    controller.loadCart();
                if (event.getCode() == KeyCode.F2) 
                    controller.saveCart();
                if (event.getCode() == KeyCode.F3) 
                    controller.reset();
                if (event.getCode() == KeyCode.F4) 
                    controller.checkOut();
                if (event.getCode() == KeyCode.F5) 
                    controller.cariBarang();
                if (event.getCode() == KeyCode.F6){ 
                    if(controller.printCheck.isSelected())
                        controller.printCheck.setSelected(false);
                    else if(!controller.printCheck.isSelected())
                        controller.printCheck.setSelected(true);
                }
                if (event.getCode() == KeyCode.F7){ 
                    controller.diskon();
                }
                if (event.getCode() == KeyCode.F12) {
                    controller.penjualanDetailTable.getSelectionModel().selectFirst();
                    controller.ubahBarang(controller.penjualanDetailTable.getSelectionModel().getSelectedItem());
                }
            });
            setTitle("Penjualan Retail");
            return controller;
        }catch(Exception e){
            e.printStackTrace();
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public KeuanganController showDataKeuangan(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage ,stage, "View/Dialog/Keuangan.fxml");
        KeuanganController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public KategoriBarangController showKategoriBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/KategoriBarang.fxml");
        KategoriBarangController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public PengaturanUmumController showPengaturanUmum(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/PengaturanUmum.fxml");
        PengaturanUmumController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public PengaturanDiskonController showPengaturanDiskon(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/PengaturanDiskon.fxml");
        PengaturanDiskonController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public UbahPasswordController showUbahPassword(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/UbahPassword.fxml");
        UbahPasswordController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public void showLaporanBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanBarang.fxml", "Laporan Barang");
        LaporanBarangController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanStokBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanStokBarang.fxml", "Laporan Stok Barang");
        LaporanStokBarangController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanLogBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanLogBarang.fxml", "Laporan Log Barang");
        LaporanLogBarangController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanKartuStokBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanKartuStokBarang.fxml", "Laporan Kartu Stok Barang");
        LaporanKartuStokBarangController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanPenyesuaianStokBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanPenyesuaianStokBarang.fxml", "Laporan Penyesuaian Stok Barang");
        LaporanPenyesuaianStokBarangController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanPenjualan(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanPenjualan.fxml", "Laporan Penjualan");
        LaporanPenjualanController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanBarangTerjual(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanBarangTerjual.fxml", "Laporan Barang Terjual");
        LaporanBarangTerjualController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanBarangHampirHabis(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanBarangHampirHabis.fxml", "Laporan Barang Hampir Habis");
        LaporanBarangHampirHabisController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanBarangTidakLaku(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanBarangTidakLaku.fxml", "Laporan Barang Tidak Laku");
        LaporanBarangTidakLakuController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanPembelian(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanPembelian.fxml", "Laporan Pembelian");
        LaporanPembelianController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    public void showLaporanBarangDibeli(){
        Stage stage = new Stage();
        FXMLLoader loader = showLaporan(stage, "Report/LaporanBarangDibeli.fxml", "Laporan Barang Dibeli");
        LaporanBarangDibeliController controller = loader.getController();
        stage.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.LEFT) 
                controller.prevPage();
            if (event.getCode() == KeyCode.RIGHT) 
                controller.nextPage();
        });
    }
    private void setTitle(String title){
        mainController.setTitle(title);
        if (mainController.menuPane.getPrefWidth()!=0) 
            mainController.showHideMenu();
    }
    public FXMLLoader changeStage(String URL){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(URL));
            AnchorPane container = (AnchorPane) loader.load();
            BorderPane border = (BorderPane) mainLayout.getCenter();
            border.setCenter(container);
            
//            MainStage.hide();
//            MainStage.show();
            
            return loader;
        }catch(Exception e){
            e.printStackTrace();
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    private FXMLLoader showLaporan(Stage stage, String URL, String title){
        try{
            stage.setMaximized(true);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("Resource/icon.png")));
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(URL));
            BorderPane page = (BorderPane) loader.load();
            
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            
            return loader;
        }catch(Exception e){
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public void showLoadingScreen(){
        try{
            if(loading!=null)
                loading.close();
            loading = new Stage();
            loading.initModality(Modality.APPLICATION_MODAL);
            loading.initOwner(MainStage);
            loading.initStyle(StageStyle.TRANSPARENT);
            loading.setOnCloseRequest((event) -> {
                event.consume();
            });
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Dialog/LoadingScreen.fxml"));
            AnchorPane container = (AnchorPane) loader.load();

            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            
            loading.setOpacity(0.7);
            loading.hide();
            loading.setScene(scene);
            loading.show();
            
            loading.setHeight(MainStage.getHeight());
            loading.setWidth(MainStage.getWidth());
            loading.setX((MainStage.getWidth() - loading.getWidth()) / 2);
            loading.setY((MainStage.getHeight() - loading.getHeight()) / 2);
        }catch(Exception e){
            showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    public void closeLoading(){
        loading.close();
    }
    public FXMLLoader showDialog(Stage owner, Stage dialog, String URL){
        try{
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(owner);
            dialog.initStyle(StageStyle.TRANSPARENT);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(URL));
            AnchorPane container = (AnchorPane) loader.load();

            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            
            dialog.hide();
            dialog.setScene(scene);
            dialog.show();
            
            dialog.setHeight(screenSize.getHeight());
            dialog.setWidth(screenSize.getWidth());
            dialog.setX((screenSize.getWidth() - dialog.getWidth()) / 2);
            dialog.setY((screenSize.getHeight() - dialog.getHeight()) / 2);
            return loader;
        }catch(IOException e){
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public void closeDialog(Stage dialog){
        dialog.close();
    }
    public MessageController showMessage(Modality modal,String type,String content){
        try{
            if(message!=null)
                message.close();
            message = new Stage();
            message.initModality(modal);
            message.initOwner(MainStage);
            message.initStyle(StageStyle.TRANSPARENT);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Dialog/Message.fxml"));
            AnchorPane container = (AnchorPane) loader.load();

            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            
            message.setX(screenSize.getWidth()-410);
            message.setY(screenSize.getHeight()-160);
            
            final Animation animation = new Transition() {
                { setCycleDuration(Duration.millis(250)); }
                @Override
                protected void interpolate(double frac) {
                    final double curPos = (screenSize.getHeight()-160) * (1-frac);
                    container.setTranslateY(curPos);
                }
            };
            animation.play();
            
            message.hide();
            message.setScene(scene);
            message.show();
            
            MessageController controller = loader.getController();
            controller.setMainApp(this,type,content);
            
            message.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                if (event.getCode() == KeyCode.ENTER) {
                    if(type.equals("Confirmation"))
                        controller.OK.fire();
                    else
                        closeMessage();
                }
                if (event.getCode() == KeyCode.ESCAPE) {
                    controller.cancel.fire();
                }
            });
            return controller;
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
            return null;
        }
    }
    public void closeMessage(){
        message.close();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
