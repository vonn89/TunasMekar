/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.Barang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class BarangDAO {
    public static Barang get(Connection con, String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_barang where kode_barang = ?");
        ps.setString(1, kodeBarang);
        ResultSet rs = ps.executeQuery();
        Barang b = null;
        if(rs.next()){
            b = new Barang();
            b.setKodeBarang(rs.getString(1));
            b.setKodeKategori(rs.getString(2));
            b.setNamaBarang(rs.getString(3));
            b.setSupplier(rs.getString(4));
            b.setStokMinimal(rs.getInt(5));
            b.setHargaBeli(rs.getDouble(6));
            b.setStatus(rs.getString(7));
        }
        return b;
    }
    public static List<Barang> getAllByKategoriAndStatus(Connection con, String kategori, String status)throws Exception{
        String sql = "select * from tm_barang where kode_barang !='' ";
        if(!kategori.equals("%")) 
            sql = sql + " and kode_kategori = '"+kategori+"'";
        if(!status.equals("%")) 
            sql = sql + " and status = '"+status+"'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Barang> allBarang = new ArrayList<>();
        while(rs.next()){
            Barang b = new Barang();
            b.setKodeBarang(rs.getString(1));
            b.setKodeKategori(rs.getString(2));
            b.setNamaBarang(rs.getString(3));
            b.setSupplier(rs.getString(4));
            b.setStokMinimal(rs.getInt(5));
            b.setHargaBeli(rs.getDouble(6));
            b.setStatus(rs.getString(7));
            allBarang.add(b);
        }
        return allBarang;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(kode_barang,5)) from tm_barang ");
        DecimalFormat df = new DecimalFormat("00000");
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "B-"+df.format(rs.getInt(1)+1);
        else
            return "B-"+df.format(1);
    }
    public static void insert(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_barang values(?,?,?,?,?,?,?)");
        ps.setString(1, b.getKodeBarang());
        ps.setString(2, b.getKodeKategori());
        ps.setString(3, b.getNamaBarang());
        ps.setString(4, b.getSupplier());
        ps.setInt(5, b.getStokMinimal());
        ps.setDouble(6, b.getHargaBeli());
        ps.setString(7, b.getStatus());
        ps.executeUpdate();
    }
    public static void update(Connection con, Barang b)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_barang set "
                + " kode_kategori=?, nama_barang=?, supplier=?, stok_minimal=?, harga_beli=?, status=? "
                + " where kode_barang=?");
        ps.setString(1, b.getKodeKategori());
        ps.setString(2, b.getNamaBarang());
        ps.setString(3, b.getSupplier());
        ps.setInt(4, b.getStokMinimal());
        ps.setDouble(5, b.getHargaBeli());
        ps.setString(6, b.getStatus());
        ps.setString(7, b.getKodeBarang());
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_barang");
        ps.executeUpdate();
    }
}
