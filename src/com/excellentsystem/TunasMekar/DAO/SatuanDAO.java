/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.Satuan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class SatuanDAO {
    public static List<Satuan> getAllByKategori(Connection con, String kategori)throws Exception{
        String sql = "select * from tm_satuan where kode_satuan !='' ";
        if(!kategori.equals("%")) 
            sql = sql + "  and kode_barang in (select kode_barang from tm_barang where kode_kategori = '"+kategori+"')";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Satuan> allSatuan = new ArrayList<>();
        while(rs.next()){
            Satuan s = new Satuan();
            s.setKodeBarang(rs.getString(1));
            s.setKodeSatuan(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getDouble(4));
            s.setHargaRetail(rs.getDouble(5));
            s.setHargaGrosir(rs.getDouble(6));
            s.setHargaGrosirBesar(rs.getDouble(7));
            allSatuan.add(s);
        }
        return allSatuan;
    }
    public static List<Satuan> getAll(Connection con)throws Exception{
        String sql = "select * from tm_satuan ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Satuan> allSatuan = new ArrayList<>();
        while(rs.next()){
            Satuan s = new Satuan();
            s.setKodeBarang(rs.getString(1));
            s.setKodeSatuan(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getDouble(4));
            s.setHargaRetail(rs.getDouble(5));
            s.setHargaGrosir(rs.getDouble(6));
            s.setHargaGrosirBesar(rs.getDouble(7));
            allSatuan.add(s);
        }
        return allSatuan;
    }
    public static List<Satuan> getAllByKodeBarang(Connection con, String kodeBarang)throws Exception{
        String sql = "select * from tm_satuan where kode_barang=? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, kodeBarang);
        ResultSet rs = ps.executeQuery();
        List<Satuan> allSatuan = new ArrayList<>();
        while(rs.next()){
            Satuan s = new Satuan();
            s.setKodeBarang(rs.getString(1));
            s.setKodeSatuan(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getDouble(4));
            s.setHargaRetail(rs.getDouble(5));
            s.setHargaGrosir(rs.getDouble(6));
            s.setHargaGrosirBesar(rs.getDouble(7));
            allSatuan.add(s);
        }
        return allSatuan;
    }
    public static List<Satuan> getAllByKodeBarcode(Connection con, String kodeBarcode)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_satuan where kode_barcode=?");
        ps.setString(1, kodeBarcode);
        ResultSet rs = ps.executeQuery();
        List<Satuan> allSatuan = new ArrayList<>();
        while(rs.next()){
            Satuan s = new Satuan();
            s.setKodeBarang(rs.getString(1));
            s.setKodeSatuan(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getDouble(4));
            s.setHargaRetail(rs.getDouble(5));
            s.setHargaGrosir(rs.getDouble(6));
            s.setHargaGrosirBesar(rs.getDouble(7));
            allSatuan.add(s);
        }
        return allSatuan;
    }
    public static Satuan get(Connection con, String kodeBarang, String kodeSatuan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_satuan where kode_barang=? and kode_satuan=?");
        ps.setString(1, kodeBarang);
        ps.setString(2, kodeSatuan);
        ResultSet rs = ps.executeQuery();
        Satuan s = null;
        if(rs.next()){
            s = new Satuan();
            s.setKodeBarang(rs.getString(1));
            s.setKodeSatuan(rs.getString(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getDouble(4));
            s.setHargaRetail(rs.getDouble(5));
            s.setHargaGrosir(rs.getDouble(6));
            s.setHargaGrosirBesar(rs.getDouble(7));
        }
        return s;
    }
    public static void insert(Connection con, Satuan s)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_satuan values(?,?,?,?,?,?,?)");
        ps.setString(1, s.getKodeBarang());
        ps.setString(2, s.getKodeSatuan());
        ps.setString(3, s.getKodeBarcode());
        ps.setDouble(4, s.getQty());
        ps.setDouble(5, s.getHargaRetail());
        ps.setDouble(6, s.getHargaGrosir());
        ps.setDouble(7, s.getHargaGrosirBesar());
        ps.executeUpdate();
    }
    public static void update(Connection con, Satuan s)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_satuan set kode_barcode=?, qty=?, "
                + " harga_retail=?, harga_grosir=?, harga_grosir_besar=? "
                + " where kode_satuan=? and kode_barang=?");
        ps.setString(1, s.getKodeBarcode());
        ps.setDouble(2, s.getQty());
        ps.setDouble(3, s.getHargaRetail());
        ps.setDouble(4, s.getHargaGrosir());
        ps.setDouble(5, s.getHargaGrosirBesar());
        ps.setString(6, s.getKodeSatuan());
        ps.setString(7, s.getKodeBarang());
        ps.executeUpdate();
    }
    public static void delete(Connection con, Satuan b)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_satuan where kode_satuan=? and kode_barang=?");
        ps.setString(1, b.getKodeSatuan());
        ps.setString(2, b.getKodeBarang());
        ps.executeUpdate();
    }
    public static void deleteAllByKodeBarang(Connection con, String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_satuan where kode_barang=?");
        ps.setString(1, kodeBarang);
        ps.executeUpdate();
    }
    public static void deleteAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_satuan");
        ps.executeUpdate();
    }
}
