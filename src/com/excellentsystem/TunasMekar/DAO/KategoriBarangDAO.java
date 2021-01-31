/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.KategoriBarang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class KategoriBarangDAO {
    public static List<KategoriBarang> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori_barang");
        ResultSet rs = ps.executeQuery();
        List<KategoriBarang> allKategoriBarang = new ArrayList<>();
        while(rs.next()){
            KategoriBarang k = new KategoriBarang();
            k.setKodeKategori(rs.getString(1));
            allKategoriBarang.add(k);
        }
        return allKategoriBarang;
    }
    public static KategoriBarang get(Connection con, String kodeKategori)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_kategori_barang where kode_kategori = ?");
        ps.setString(1, kodeKategori);
        ResultSet rs = ps.executeQuery();
        KategoriBarang k = null;
        while(rs.next()){
            k = new KategoriBarang();
            k.setKodeKategori(rs.getString(1));
        }
        return k;
    }
    public static void insert(Connection con, KategoriBarang k)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_kategori_barang values(?)");
        ps.setString(1, k.getKodeKategori());
        ps.executeUpdate();
    }
    public static void delete(Connection con, KategoriBarang k)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_kategori_barang where kode_kategori=?");
        ps.setString(1, k.getKodeKategori());
        ps.executeUpdate();
    }
}
