/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.Pelanggan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PelangganDAO {
    
    public static Pelanggan get(Connection con, String kodePelanggan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_pelanggan where kode_pelanggan = ?");
        ps.setString(1, kodePelanggan);
        ResultSet rs = ps.executeQuery();
        Pelanggan p = null;
        if(rs.next()){
            p = new Pelanggan();
            p.setKodePelanggan(rs.getString(1));
            p.setNama(rs.getString(2));
            p.setAlamat(rs.getString(3));
            p.setNoTelp(rs.getString(4));
            p.setHutang(rs.getDouble(5));
            p.setStatus(rs.getString(6));
        }
        return p;
    }
    public static List<Pelanggan> getAllByStatus(Connection con, String status)throws Exception{
        String sql = "select * from tm_pelanggan ";
        if(!status.equals("%")) 
            sql = sql + " where status = '"+status+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Pelanggan> allPelanggan = new ArrayList<>();
        while(rs.next()){
            Pelanggan p = new Pelanggan();
            p.setKodePelanggan(rs.getString(1));
            p.setNama(rs.getString(2));
            p.setAlamat(rs.getString(3));
            p.setNoTelp(rs.getString(4));
            p.setHutang(rs.getDouble(5));
            p.setStatus(rs.getString(6));
            allPelanggan.add(p);
        }
        return allPelanggan;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(kode_pelanggan,4)) from tm_pelanggan ");
        DecimalFormat df = new DecimalFormat("0000");
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "P-"+df.format(rs.getInt(1)+1);
        else
            return "P-"+df.format(1);
    }
    public static void insert(Connection con, Pelanggan b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_pelanggan values(?,?,?,?,?,?)");
        ps.setString(1, b.getKodePelanggan());
        ps.setString(2, b.getNama());
        ps.setString(3, b.getAlamat());
        ps.setString(4, b.getNoTelp());
        ps.setDouble(5, b.getHutang());
        ps.setString(6, b.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, Pelanggan b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_pelanggan set "
                + " nama=?, alamat=?, no_telp=?, hutang=?, status=? "
                + " where kode_pelanggan=? ");
        ps.setString(1, b.getNama());
        ps.setString(2, b.getAlamat());
        ps.setString(3, b.getNoTelp());
        ps.setDouble(4, b.getHutang());
        ps.setString(5, b.getStatus());
        ps.setString(6, b.getKodePelanggan());
        ps.executeUpdate();
    }
    public static void delete(Connection con, String kodePelanggan)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_pelanggan where kode_pelanggan=?");
        ps.setString(1, kodePelanggan);
        ps.executeUpdate();
    }
}
