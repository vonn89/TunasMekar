/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TunasMekar.View.Dialog;

import static com.excellentsystem.TunasMekar.Function.decrypt;
import com.excellentsystem.TunasMekar.Koneksi;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.key;
import static com.excellentsystem.TunasMekar.Main.timeout;
import static com.excellentsystem.TunasMekar.Main.user;
import com.excellentsystem.TunasMekar.Service.Service;
import java.sql.Connection;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class UbahPasswordController  {

    @FXML private PasswordField passwordLama;
    @FXML private PasswordField passwordBaru;
    @FXML private PasswordField ulangiPasswordBaru;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main main, Stage stage){
        this.mainApp = main;
        this.stage = stage;
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
        passwordLama.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                passwordBaru.requestFocus();
                passwordBaru.selectAll();
            }
        });
        passwordBaru.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                ulangiPasswordBaru.requestFocus();
                ulangiPasswordBaru.selectAll();
            }
        });
        ulangiPasswordBaru.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                save();
        });
        passwordLama.requestFocus();
    }
    @FXML
    private void save(){
        try{
            if(passwordLama.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning","Password lama masih kosong");
            else if(passwordBaru.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning","Password baru masih kosong");
            else if(ulangiPasswordBaru.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning","Ulangi password baru masih kosong");
            else if(!passwordLama.getText().equals(decrypt(user.getPassword(), key)))
                mainApp.showMessage(Modality.NONE, "Warning","Password lama salah");
            else if(!passwordBaru.getText().equals(ulangiPasswordBaru.getText()))
                mainApp.showMessage(Modality.NONE, "Warning","Password baru tidak sama");
            else{
                Task<String> task = new Task<String>() {
                    @Override 
                    public String call() throws Exception{
                        Thread.sleep(timeout);
                        try(Connection con = Koneksi.getConnection()){
                            user.setPassword(passwordBaru.getText());
                            return Service.saveUpdatePassword(con, user);
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((e) -> {
                    mainApp.closeLoading();
                    String status = task.getValue();
                    if(status.equals("true")){
                        mainApp.showMessage(Modality.NONE, "Success", "Password baru berhasil di simpan");
                        close();
                    }else
                        mainApp.showMessage(Modality.NONE, "Failed", status);
                });
                task.setOnFailed((e) -> {
                    mainApp.closeLoading();
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                });
                new Thread(task).start();
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
    
}
