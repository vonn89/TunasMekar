package com.excellentsystem.TunasMekar;

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
public class KoneksiHosting {
    public static Connection getConnection()throws Exception{
        String ipServer = "153.92.9.24";
        String DbName = "jdbc:mysql://"+ipServer+":3306/excellentsystem_tunasmekar?"
                + "connectTimeout=1000&socketTimeout=1000";
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DbName,"admin","excellentsystem");
    }
}
