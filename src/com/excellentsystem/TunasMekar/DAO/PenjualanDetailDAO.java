/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenjualanDetailDAO {
    
    public static List<PenjualanDetail> getAllByDateAndKategoriAndStatus(
            Connection con, String tglMulai, String tglAkhir, String kategori, String status)throws Exception{
        String sql = "select * from tt_penjualan_detail where no_penjualan in ( "
                + " select no_penjualan from tt_penjualan_head where left(tgl_penjualan,10) between ? and ? ";
        if(!kategori.equals("%"))
            sql = sql + " and kategori_penjualan = '"+kategori+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDetail> listPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanDetail p = new PenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setSatuan(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setQtyKeluar(rs.getDouble(7));
            p.setNilai(rs.getDouble(8));
            p.setTotalNilai(rs.getDouble(9));
            p.setHarga(rs.getDouble(10));
            p.setTotalHarga(rs.getDouble(11));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static List<PenjualanDetail> getAll(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_detail "
                + " where no_penjualan = ? ");
        ps.setString(1, noPenjualan);
        List<PenjualanDetail> listPenjualan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PenjualanDetail p = new PenjualanDetail();
            p.setNoPenjualan(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setNamaBarang(rs.getString(4));
            p.setSatuan(rs.getString(5));
            p.setQty(rs.getDouble(6));
            p.setQtyKeluar(rs.getDouble(7));
            p.setNilai(rs.getDouble(8));
            p.setTotalNilai(rs.getDouble(9));
            p.setHarga(rs.getDouble(10));
            p.setTotalHarga(rs.getDouble(11));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static void insert(Connection con, PenjualanDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_detail values(?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setString(4, p.getNamaBarang());
        ps.setString(5, p.getSatuan());
        ps.setDouble(6, p.getQty());
        ps.setDouble(7, p.getQtyKeluar());
        ps.setDouble(8, p.getNilai());
        ps.setDouble(9, p.getTotalNilai());
        ps.setDouble(10, p.getHarga());
        ps.setDouble(11, p.getTotalHarga());
        ps.executeUpdate();
    }
}
