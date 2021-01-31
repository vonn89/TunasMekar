/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.StokBarang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class StokBarangDAO {
    public static List<StokBarang> getAllByTanggalAndBarang(Connection con, String tanggal, String kodeBarang)throws Exception{
        String sql = "select * from tt_stok_barang where tanggal = ? ";
        if(!kodeBarang.equals("%"))
            sql = sql + " and kode_barang = '"+kodeBarang+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tanggal);
        ResultSet rs = ps.executeQuery();
        List<StokBarang> allStok = new ArrayList<>();
        while(rs.next()){
            StokBarang s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeBarang(rs.getString(2));
            s.setStokAwal(rs.getDouble(3));
            s.setStokMasuk(rs.getDouble(4));
            s.setStokKeluar(rs.getDouble(5));
            s.setStokAkhir(rs.getDouble(6));
            allStok.add(s);
        }
        return allStok;
    }
    public static List<StokBarang> getAllByDateAndBarang(Connection con, String tglAwal, String tglAkhir, String kodeBarang)throws Exception{
        String sql = "select * from tt_stok_barang where tanggal between ? and ? ";
        if(!kodeBarang.equals("%"))
            sql = sql + " and kode_barang = '"+kodeBarang+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglAwal);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<StokBarang> allStok = new ArrayList<>();
        while(rs.next()){
            StokBarang s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeBarang(rs.getString(2));
            s.setStokAwal(rs.getDouble(3));
            s.setStokMasuk(rs.getDouble(4));
            s.setStokKeluar(rs.getDouble(5));
            s.setStokAkhir(rs.getDouble(6));
            allStok.add(s);
        }
        return allStok;
    }
    public static StokBarang get(Connection con, String tanggal,String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_stok_barang where tanggal= ? and kode_barang=? ");
        ps.setString(1, tanggal);
        ps.setString(2, kodeBarang);
        ResultSet rs = ps.executeQuery();
        StokBarang s = null;
        if(rs.next()){
            s = new StokBarang();
            s.setTanggal(rs.getString(1));
            s.setKodeBarang(rs.getString(2));
            s.setStokAwal(rs.getDouble(3));
            s.setStokMasuk(rs.getDouble(4));
            s.setStokKeluar(rs.getDouble(5));
            s.setStokAkhir(rs.getDouble(6));
        }
        return s;
    }
    public static void insertStokAwal(Connection con, String yesterday, String today)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_stok_barang "
            + " select ?,kode_barang,stok_akhir,0,0,stok_akhir "
            + " from tt_stok_barang where tanggal=? and stok_akhir!=0 ");
        ps.setString(1, today);
        ps.setString(2, yesterday);
        ps.executeUpdate();
    }
    public static void insert(Connection con, StokBarang s)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert tt_stok_barang values(?,?,?,?,?,?)");
        ps.setString(1, s.getTanggal());
        ps.setString(2, s.getKodeBarang());
        ps.setDouble(3, s.getStokAwal());
        ps.setDouble(4, s.getStokMasuk());
        ps.setDouble(5, s.getStokKeluar());
        ps.setDouble(6, s.getStokAkhir());
        ps.executeUpdate();
    }
    public static void update(Connection con, StokBarang s)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_stok_barang set stok_awal=?, stok_masuk=?, stok_keluar=?, stok_akhir=? "
                + "where tanggal=? and kode_barang=? ");
        ps.setDouble(1, s.getStokAwal());
        ps.setDouble(2, s.getStokMasuk());
        ps.setDouble(3, s.getStokKeluar());
        ps.setDouble(4, s.getStokAkhir());
        ps.setString(5, s.getTanggal());
        ps.setString(6, s.getKodeBarang());
        ps.executeUpdate();
    }
}
