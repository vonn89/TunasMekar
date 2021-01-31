/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.Diskon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class DiskonDAO {
    
    public static Diskon get(Connection con, String kodeDiskon)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_diskon where kode_diskon = ?");
        ps.setString(1, kodeDiskon);
        ResultSet rs = ps.executeQuery();
        Diskon b = null;
        if(rs.next()){
            b = new Diskon();
            b.setKodeDiskon(rs.getString(1));
            b.setNamaDiskon(rs.getString(2));
            b.setDiskonPersen(rs.getDouble(3));
            b.setDiskonRp(rs.getDouble(4));
            b.setStatus(rs.getString(5));
        }
        return b;
    }
    public static List<Diskon> getAllByStatus(Connection con, String status)throws Exception{
        String sql = "select * from tm_diskon ";
        if(!status.equals("%")) 
            sql = sql + " where status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Diskon> allDiskon = new ArrayList<>();
        while(rs.next()){
            Diskon b = new Diskon();
            b.setKodeDiskon(rs.getString(1));
            b.setNamaDiskon(rs.getString(2));
            b.setDiskonPersen(rs.getDouble(3));
            b.setDiskonRp(rs.getDouble(4));
            b.setStatus(rs.getString(5));
            allDiskon.add(b);
        }
        return allDiskon;
    }
    public static void insert(Connection con, Diskon b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_diskon values(?,?,?,?,?)");
        ps.setString(1, b.getKodeDiskon());
        ps.setString(2, b.getNamaDiskon());
        ps.setDouble(3, b.getDiskonPersen());
        ps.setDouble(4, b.getDiskonRp());
        ps.setString(5, b.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, Diskon b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_diskon set "
                + " nama_diskon=?, diskon_persen=?, diskon_rp=?, status=? "
                + " where kode_diskon=? ");
        ps.setString(1, b.getNamaDiskon());
        ps.setDouble(2, b.getDiskonPersen());
        ps.setDouble(3, b.getDiskonRp());
        ps.setString(4, b.getStatus());
        ps.setString(5, b.getKodeDiskon());
        ps.executeUpdate();
    }
    public static void delete(Connection con, String kodeDiskon)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_diskon where kode_diskon=?");
        ps.setString(1, kodeDiskon);
        ps.executeUpdate();
    }
}
