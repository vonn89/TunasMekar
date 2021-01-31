/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.PenjualanDiskon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenjualanDiskonDAO {
    
    public static List<PenjualanDiskon> getAllByDateAndKategoriAndStatus(
            Connection con, String tglMulai, String tglAkhir, String kategori, String status)throws Exception{
        String sql = "select * from tt_penjualan_diskon where no_penjualan in ( "
                + "select no_penjualan from tt_penjualan_head where left(tgl_penjualan,10) between ? and ? ";
        if(!kategori.equals("%"))
            sql = sql + " and kategori_penjualan = '"+kategori+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        sql = sql + " )";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanDiskon> listPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanDiskon p = new PenjualanDiskon();
            p.setNoPenjualan(rs.getString(1));
            p.setKodeDiskon(rs.getString(2));
            p.setNamaDiskon(rs.getString(3));
            p.setDiskonPersen(rs.getDouble(4));
            p.setDiskonRp(rs.getDouble(5));
            p.setQty(rs.getInt(6));
            p.setTotalDiskon(rs.getDouble(7));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static List<PenjualanDiskon> getAll(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_diskon "
                + " where no_penjualan = ? ");
        ps.setString(1, noPenjualan);
        List<PenjualanDiskon> listPenjualan = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            PenjualanDiskon p = new PenjualanDiskon();
            p.setNoPenjualan(rs.getString(1));
            p.setKodeDiskon(rs.getString(2));
            p.setNamaDiskon(rs.getString(3));
            p.setDiskonPersen(rs.getDouble(4));
            p.setDiskonRp(rs.getDouble(5));
            p.setQty(rs.getInt(6));
            p.setTotalDiskon(rs.getDouble(7));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static void insert(Connection con, PenjualanDiskon p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_diskon values(?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setString(2, p.getKodeDiskon());
        ps.setString(3, p.getNamaDiskon());
        ps.setDouble(4, p.getDiskonPersen());
        ps.setDouble(5, p.getDiskonRp());
        ps.setInt(6, p.getQty());
        ps.setDouble(7, p.getTotalDiskon());
        ps.executeUpdate();
    }
}
