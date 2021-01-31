package com.excellentsystem.TunasMekar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Xtreme
 */
public class Koneksi {
    public static Connection getConnection()throws Exception{
        String ipServer = new BufferedReader(new FileReader("koneksi")).readLine();
        String DbName = "jdbc:mysql://"+ipServer+":3306/tunasmekar";
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DbName,"admin","excellentsystem");
    }
}
