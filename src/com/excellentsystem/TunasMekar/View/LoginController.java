package com.excellentsystem.TunasMekar.View;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static com.excellentsystem.TunasMekar.Function.decrypt;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.key;
import static com.excellentsystem.TunasMekar.Main.version;
import com.excellentsystem.TunasMekar.Model.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class LoginController {

    @FXML private Label warningLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheck;
    @FXML private Label versionLabel;
    private Main mainApp;
    private int attempt = 0;
    public void initialize() {
        usernameField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                passwordField.requestFocus();
            }
        });
        passwordField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                handleLoginButton();
        });
    }    
    public void setMainApp(Main mainApp){
        try{
            this.mainApp = mainApp;
            versionLabel.setText("Ver. "+version);
            warningLabel.setText("");
            BufferedReader text = new BufferedReader(new FileReader("password"));
            String user = text.readLine();
            if(user!=null){
                usernameField.setText(user);
                passwordField.setText(decrypt(text.readLine(), key));
                rememberMeCheck.setSelected(Boolean.valueOf(text.readLine()));
            }
            usernameField.requestFocus();
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML 
    private void handleLoginButton() {
        if("".equals(usernameField.getText())){
            warningLabel.setText("Username masih kosong");
        }else if(passwordField.getText().equals("")){
            warningLabel.setText("Password masih kosong");
        }else if(attempt>=3){
            System.exit(0);
        }else{
            try {
                User user = null;
                for(User u : Main.allUser){
                    if(u.getKodeUser().equals(usernameField.getText()))
                        user = u;
                }
                if(user==null){
                    warningLabel.setText("Username tidak ditemukan");
                    attempt = attempt +1;
                }else if(!passwordField.getText().equals(decrypt(user.getPassword(), key))){
                    warningLabel.setText("Password masih salah");
                    attempt = attempt +1;
                }else{
                    if(rememberMeCheck.isSelected()){
                        try (FileWriter fw = new FileWriter(new File("password"), false)) {
                            fw.write(user.getKodeUser());
                            fw.write(System.lineSeparator());
                            fw.write(user.getPassword());
                            fw.write(System.lineSeparator());
                            fw.write(String.valueOf(rememberMeCheck.isSelected()));
                        }
                    }else{
                        try (FileWriter fw = new FileWriter(new File("password"), false)) {
                            fw.write("");
                        }
                    }
                    Main.user = user;
                    mainApp.showMainScene();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
            }
        }
    }
}
