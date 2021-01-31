/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View;

import com.excellentsystem.TunasMekar.DAO.OtoritasDAO;
import com.excellentsystem.TunasMekar.DAO.UserDAO;
import static com.excellentsystem.TunasMekar.Function.decrypt;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.key;
import static com.excellentsystem.TunasMekar.Main.timeout;
import com.excellentsystem.TunasMekar.Model.Otoritas;
import com.excellentsystem.TunasMekar.Model.User;
import com.excellentsystem.TunasMekar.Service.Service;
import com.excellentsystem.TunasMekar.View.Dialog.MessageController;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class DataUserController  {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> kodeUserColumn;
    
    @FXML private TableView<Otoritas> otoritasTable;
    @FXML private TableColumn<Otoritas, String> jenisOtoritasColumn;
    @FXML private TableColumn<Otoritas, Boolean> statusOtoritasColumn;
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox checkOtoritas;
    
    private Main mainApp;
    private User user;
    
    private String status = "";
    private ObservableList<User> allUser = FXCollections.observableArrayList();
    private ObservableList<Otoritas> otoritas = FXCollections.observableArrayList();
    public void initialize() {
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().kodeUserProperty());
        
        jenisOtoritasColumn.setCellValueFactory(cellData -> cellData.getValue().jenisProperty());
        statusOtoritasColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        statusOtoritasColumn.setCellFactory(CheckBoxTableCell.forTableColumn(
                (Integer param) -> otoritasTable.getItems().get(param).statusProperty()));
        
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectUser(newValue));
        final ContextMenu menu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah User Baru");
        addNew.setOnAction((ActionEvent event) -> {
            newUser();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getUser();
        });
        menu.getItems().addAll(addNew, refresh);
        userTable.setContextMenu(menu);
        userTable.setRowFactory(table -> {
            TableRow<User> row = new TableRow<User>(){
                @Override
                public void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah User Baru");
                        addNew.setOnAction((ActionEvent event) -> {
                            newUser();
                        });
                        MenuItem hapus = new MenuItem("Hapus User");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteUser(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getUser();
                        });
                        rowMenu.getItems().addAll(addNew, hapus, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        otoritasTable.setRowFactory(table -> {
            TableRow<Otoritas> row = new TableRow<Otoritas>(){
                @Override
                public void updateItem(Otoritas item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            checkOtoritas();
                        });
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    if(row.getItem().isStatus())
                        row.getItem().setStatus(false);
                    else
                        row.getItem().setStatus(true);
            });
            return row;
        });
    }
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        userTable.setItems(allUser);
        otoritasTable.setItems(otoritas);
        getUser();
    }    
    private void getUser(){
        Task<List<User>> task = new Task<List<User>>() {
            @Override 
            public List<User> call() throws Exception{
                Thread.sleep(timeout);
                try(Connection con = Koneksi.getConnection()){
                    List<User> allUser = UserDAO.getAllByStatus(con, "true");
                    List<Otoritas> allOtoritas = OtoritasDAO.getAll(con);
                    for(User u : allUser){
                        u.setPassword(decrypt(u.getPassword(), key));
                        List<Otoritas> otoritas = new ArrayList<>();
                        for(Otoritas o : allOtoritas){
                            if(u.getKodeUser().equals(o.getKodeUser()))
                                otoritas.add(o);
                        }
                        u.setOtoritas(otoritas);
                    }
                    return allUser;
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((e) -> {
            mainApp.closeLoading();
            allUser.clear();
            allUser.addAll(task.getValue());
            otoritas.clear();
            usernameField.setText("");
            passwordField.setText("");
            usernameField.setDisable(true);
            user=null;
            status="";
        });
        task.setOnFailed((e) -> {
            mainApp.closeLoading();
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
        });
        new Thread(task).start();
    }
    @FXML
    private void checkOtoritas(){
        for(Otoritas o : otoritas){
            o.setStatus(checkOtoritas.isSelected());
        }
        otoritasTable.refresh();
    }
    private void selectUser(User value){
        try{
            user=null;
            usernameField.setText("");
            passwordField.setText("");
            usernameField.setDisable(true);
            status="";
            otoritas.clear();
            if(value!=null){
                status="update";
                user = value;
                usernameField.setText(user.getKodeUser());
                passwordField.setText(user.getPassword());
                otoritas.addAll(user.getOtoritas());
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }    
    private void newUser(){
        status = "new";
        user = new User();
        usernameField.setDisable(false);
        usernameField.setText("");
        passwordField.setText("");

        List<String> allOtoritas = new ArrayList<>();
        allOtoritas.add("Penjualan Grosir Besar");
        allOtoritas.add("Penjualan Grosir");
        allOtoritas.add("Penjualan Retail");
        allOtoritas.add("Data Pelanggan");
        allOtoritas.add("Data Barang");
        allOtoritas.add("Stok Barang");
        allOtoritas.add("Data Penjualan");
        allOtoritas.add("Data Pembelian");
        allOtoritas.add("Keuangan");
        allOtoritas.add("Laporan");
        allOtoritas.add("Pengaturan");
        List<Otoritas> tempOtoritas = new ArrayList<>();
        for(String jns : allOtoritas){
            Otoritas temp = new Otoritas();
            temp.setKodeUser(user.getKodeUser());
            temp.setJenis(jns);
            temp.setStatus(checkOtoritas.isSelected());
            tempOtoritas.add(temp);
        }
        otoritas.clear();
        otoritas.addAll(tempOtoritas);
    }    
    @FXML
    private void cancel(){
        selectUser(null);
    }
    @FXML
    private void saveUser() {
        if(status.equals("update")){
            if(user==null || usernameField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "User belum dipilih");
            }else{
                MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                        "Update user "+user.getKodeUser()+" ?");
                controller.OK.setOnAction((ActionEvent evt) -> {
                    Task<String> task = new Task<String>() {
                        @Override 
                        public String call() throws Exception{
                            Thread.sleep(timeout);
                            try(Connection con = Koneksi.getConnection()){
                                user.setPassword(passwordField.getText());
                                for(Otoritas temp : otoritas){
                                    temp.setKodeUser(user.getKodeUser());
                                }
                                user.setOtoritas(otoritas);
                                return Service.saveUpdateUser(con, user);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((e) -> {
                        mainApp.closeLoading();
                        getUser();
                        String message = task.getValue();
                        if(message.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil disimpan");
                        }else{
                            mainApp.showMessage(Modality.NONE, "Failed", message);
                        }
                    });
                    task.setOnFailed((e) -> {
                        mainApp.closeLoading();
                        mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                    });
                    new Thread(task).start();
                });
            }
        }else if(status.equals("new")){
            if(user==null || usernameField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "username masih kosong");
            }else{
                MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                        "Save new user "+usernameField.getText()+" ?");
                controller.OK.setOnAction((ActionEvent ev) -> {
                    Task<String> task = new Task<String>() {
                        @Override 
                        public String call() throws Exception{
                            Thread.sleep(timeout);
                            try(Connection con = Koneksi.getConnection()){
                                user.setKodeUser(usernameField.getText());
                                user.setPassword(passwordField.getText());
                                user.setOtoritas(otoritas);
                                user.setStatus("true");
                                return Service.saveNewUser(con, user);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((e) -> {
                        mainApp.closeLoading();
                        getUser();
                        String message = task.getValue();
                        if(message.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil disimpan");
                        }else
                            mainApp.showMessage(Modality.NONE, "Failed", message);
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
    private void deleteUser(User user){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Delete user "+user.getKodeUser()+" ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    Thread.sleep(timeout);
                    try(Connection con = Koneksi.getConnection()){
                        return Service.deleteUser(con, user);
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((e) -> {
                mainApp.closeLoading();
                getUser();
                String message = task.getValue();
                if(message.equals("true")){
                    mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil dihapus");
                }else
                    mainApp.showMessage(Modality.NONE, "Failed", message);
            });
            task.setOnFailed((e) -> {
                mainApp.closeLoading();
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            });
            new Thread(task).start();
        });
    }
    
}
