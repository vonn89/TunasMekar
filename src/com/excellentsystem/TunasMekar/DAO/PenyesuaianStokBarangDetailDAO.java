/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenyesuaianStokBarangDetailDAO {
    
    public static List<PenyesuaianStokBarangDetail> getAllByDateAndJenisPenyesuaianAndStatus(
            Connection con, String tglMulai, String tglAkhir, String jenisPenyesuaian, String status)throws Exception{
        String sql = "select * from tt_penyesuaian_stok_barang_detail where no_penyesuaian in ( "
                + "select no_penyesuaian from tt_penyesuaian_stok_barang_head where left(tgl_penyesuaian,10) between ? and ? ";
        if(!jenisPenyesuaian.equals("%"))
            sql = sql + " and jenis_penyesuaian = '"+jenisPenyesuaian+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenyesuaianStokBarangDetail> listPenyesuaianStok = new ArrayList<>();
        while(rs.next()){
            PenyesuaianStokBarangDetail p = new PenyesuaianStokBarangDetail();
            p.setNoPenyesuaian(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setQty(rs.getDouble(4));
            p.setQtyStok(rs.getDouble(5));
            p.setSatuan(rs.getString(6));
            p.setNilai(rs.getDouble(7));
            p.setTotalNilai(rs.getDouble(8));
            listPenyesuaianStok.add(p);
        }
        return listPenyesuaianStok;
    }
    public static List<PenyesuaianStokBarangDetail> getAll(Connection con, String noPenyesuaian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penyesuaian_stok_barang_detail "
                + " where no_penyesuaian = ?");
        ps.setString(1, noPenyesuaian);
        List<PenyesuaianStokBarangDetail> listPenyesuaianStok = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PenyesuaianStokBarangDetail p = new PenyesuaianStokBarangDetail();
            p.setNoPenyesuaian(rs.getString(1));
            p.setNoUrut(rs.getInt(2));
            p.setKodeBarang(rs.getString(3));
            p.setQty(rs.getDouble(4));
            p.setQtyStok(rs.getDouble(5));
            p.setSatuan(rs.getString(6));
            p.setNilai(rs.getDouble(7));
            p.setTotalNilai(rs.getDouble(8));
            listPenyesuaianStok.add(p);
        }
        return listPenyesuaianStok;
    }
    public static void insert(Connection con, PenyesuaianStokBarangDetail p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penyesuaian_stok_barang_detail values(?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenyesuaian());
        ps.setInt(2, p.getNoUrut());
        ps.setString(3, p.getKodeBarang());
        ps.setDouble(4, p.getQty());
        ps.setDouble(5, p.getQtyStok());
        ps.setString(6, p.getSatuan());
        ps.setDouble(7, p.getNilai());
        ps.setDouble(8, p.getTotalNilai());
        ps.executeUpdate();
    }
}
