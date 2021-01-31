/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import static com.excellentsystem.TunasMekar.Function.encrypt;
import static com.excellentsystem.TunasMekar.Main.key;
import com.excellentsystem.TunasMekar.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class UserDAO {
    public static List<User> getAllByStatus(Connection con, String status)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_user where status =?");
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        List<User> allUser = new ArrayList<>();
        while(rs.next()){
            User u = new User();
            u.setKodeUser(rs.getString(1));
            u.setPassword(rs.getString(2));
            u.setStatus(rs.getString(3));
            allUser.add(u);
        }
        return allUser;
    }
    public static User get(Connection con, String username)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_user where kode_user =?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        User u = null;
        while(rs.next()){
            u = new User();
            u.setKodeUser(rs.getString(1));
            u.setPassword(rs.getString(2));
            u.setStatus(rs.getString(3));
        }
        return u;
    }
    public static void insert(Connection con, User u)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_user values(?,?,?)");
        ps.setString(1, u.getKodeUser());
        ps.setString(2, encrypt(u.getPassword(), key));
        ps.setString(3, u.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, User u)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_user set password=?, status=? where kode_user=?");
        ps.setString(1, encrypt(u.getPassword(), key));
        ps.setString(2, u.getStatus());
        ps.setString(3, u.getKodeUser());
        ps.executeUpdate();
    }
}
