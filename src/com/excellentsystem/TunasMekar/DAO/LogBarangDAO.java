/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.LogBarang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yunaz
 */
public class LogBarangDAO {
    public static LogBarang getLastByBarang(Connection con, String kodeBarang)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_log_barang "
                + " where kode_barang = '"+kodeBarang+"' "
                + " order by tanggal desc limit 1");
        ResultSet rs = ps.executeQuery();
        LogBarang l = null;
        while(rs.next()){
            l = new LogBarang();
            l.setTanggal(rs.getString(1));
            l.setKodeBarang(rs.getString(2));
            l.setKategori(rs.getString(3));
            l.setKeterangan(rs.getString(4));
            l.setStokAwal(rs.getDouble(5));
            l.setNilaiAwal(rs.getDouble(6));
            l.setStokMasuk(rs.getDouble(7));
            l.setNilaiMasuk(rs.getDouble(8));
            l.setStokKeluar(rs.getDouble(9));
            l.setNilaiKeluar(rs.getDouble(10));
            l.setStokAkhir(rs.getDouble(11));
            l.setNilaiAkhir(rs.getDouble(12));
        }
        return l;
    }
    public static List<LogBarang> getAllByDateAndBarang(Connection con, String tglMulai, String tglAkhir, String kodeBarang)throws Exception{
        String sql = "select * from tt_log_barang where left(tanggal,10) between ? and ? ";
        if(!kodeBarang.equals("%"))
            sql = sql + " and kode_barang = '"+kodeBarang+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<LogBarang> allLogBarang = new ArrayList<>();
        while(rs.next()){
            LogBarang l = new LogBarang();
            l.setTanggal(rs.getString(1));
            l.setKodeBarang(rs.getString(2));
            l.setKategori(rs.getString(3));
            l.setKeterangan(rs.getString(4));
            l.setStokAwal(rs.getDouble(5));
            l.setNilaiAwal(rs.getDouble(6));
            l.setStokMasuk(rs.getDouble(7));
            l.setNilaiMasuk(rs.getDouble(8));
            l.setStokKeluar(rs.getDouble(9));
            l.setNilaiKeluar(rs.getDouble(10));
            l.setStokAkhir(rs.getDouble(11));
            l.setNilaiAkhir(rs.getDouble(12));
            allLogBarang.add(l);
        }
        return allLogBarang;
    }
    public static void insert(Connection con, LogBarang l)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert tt_log_barang values(?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, l.getTanggal());
        ps.setString(2, l.getKodeBarang());
        ps.setString(3, l.getKategori());
        ps.setString(4, l.getKeterangan());
        ps.setDouble(5, l.getStokAwal());
        ps.setDouble(6, l.getNilaiAwal());
        ps.setDouble(7, l.getStokMasuk());
        ps.setDouble(8, l.getNilaiMasuk());
        ps.setDouble(9, l.getStokKeluar());
        ps.setDouble(10, l.getNilaiKeluar());
        ps.setDouble(11, l.getStokAkhir());
        ps.setDouble(12, l.getNilaiAkhir());
        ps.executeUpdate();
    }
}
