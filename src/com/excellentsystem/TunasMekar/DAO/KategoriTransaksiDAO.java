/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.KategoriTransaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class KategoriTransaksiDAO {
    public static List<KategoriTransaksi> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori_transaksi");
        ResultSet rs = ps.executeQuery();
        List<KategoriTransaksi> allKategoriTransaksi = new ArrayList<>();
        while(rs.next()){
            KategoriTransaksi k = new KategoriTransaksi();
            k.setKodeKategori(rs.getString(1));
            k.setKategoriKeuangan(rs.getString(2));
            k.setJenisTransaksi(rs.getString(3));
            allKategoriTransaksi.add(k);
        }
        return allKategoriTransaksi;
    }
    public static KategoriTransaksi get(Connection con,String kodeKategori)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori_transaksi where kode_kategori = ?");
        ps.setString(1, kodeKategori);
        ResultSet rs = ps.executeQuery();
        KategoriTransaksi k = null;
        while(rs.next()){
            k = new KategoriTransaksi();
            k.setKodeKategori(rs.getString(1));
            k.setKategoriKeuangan(rs.getString(2));
            k.setJenisTransaksi(rs.getString(3));
        }
        return k;
    }
    public static void insert(Connection con, KategoriTransaksi k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_kategori_transaksi values(?,?,?)");
        ps.setString(1, k.getKodeKategori());
        ps.setString(2, k.getKategoriKeuangan());
        ps.setString(3, k.getJenisTransaksi());
        ps.executeUpdate();
    }
    public static void delete(Connection con, KategoriTransaksi k)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_kategori_transaksi where kode_kategori=?");
        ps.setString(1, k.getKodeKategori());
        ps.executeUpdate();
    }
}
