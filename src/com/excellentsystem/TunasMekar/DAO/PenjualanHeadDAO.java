/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglKode;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class PenjualanHeadDAO {
    
    public static List<PenjualanHead> getAllByDateAndKategoriAndStatus(
            Connection con, String tglMulai, String tglAkhir, String kategori, String status)throws Exception{
        String sql = "select * from tt_penjualan_head where left(tgl_penjualan,10) between ? and ? ";
        if(!kategori.equals("%"))
            sql = sql + " and kategori_penjualan = '"+kategori+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenjualanHead> listPenjualan = new ArrayList<>();
        while(rs.next()){
            PenjualanHead p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKategoriPenjualan(rs.getString(3));
            p.setPelanggan(rs.getString(4));
            p.setTotalPenjualan(rs.getDouble(5));
            p.setTotalDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatus(rs.getString(11));
            p.setTglBatal(rs.getString(12));
            p.setUserBatal(rs.getString(13));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static PenjualanHead get(Connection con, String noPenjualan)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penjualan_head "
                + " where no_penjualan = ?");
        ps.setString(1, noPenjualan);
        PenjualanHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new PenjualanHead();
            p.setNoPenjualan(rs.getString(1));
            p.setTglPenjualan(rs.getString(2));
            p.setKategoriPenjualan(rs.getString(3));
            p.setPelanggan(rs.getString(4));
            p.setTotalPenjualan(rs.getDouble(5));
            p.setTotalDiskon(rs.getDouble(6));
            p.setGrandtotal(rs.getDouble(7));
            p.setPembayaran(rs.getDouble(8));
            p.setSisaPembayaran(rs.getDouble(9));
            p.setKodeUser(rs.getString(10));
            p.setStatus(rs.getString(11));
            p.setTglBatal(rs.getString(12));
            p.setUserBatal(rs.getString(13));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        DateFormat tglKode = new SimpleDateFormat("yyMMdd");
        DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("000");
        PreparedStatement ps = con.prepareStatement("select max(right(no_penjualan,3)) from tt_penjualan_head " 
                + " where left(no_penjualan,9) = ?");
        ps.setString(1, "PJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "PJ-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, PenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penjualan_head values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenjualan());
        ps.setString(2, p.getTglPenjualan());
        ps.setString(3, p.getKategoriPenjualan());
        ps.setString(4, p.getPelanggan());
        ps.setDouble(5, p.getTotalPenjualan());
        ps.setDouble(6, p.getTotalDiskon());
        ps.setDouble(7, p.getGrandtotal());
        ps.setDouble(8, p.getPembayaran());
        ps.setDouble(9, p.getSisaPembayaran());
        ps.setString(10, p.getKodeUser());
        ps.setString(11, p.getStatus());
        ps.setString(12, p.getTglBatal());
        ps.setString(13, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PenjualanHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_penjualan_head "
                + " set tgl_penjualan = ?, kategori_penjualan = ?, pelanggan = ?, total_penjualan = ?, total_diskon = ?, grandtotal = ?, pembayaran = ?, sisa_pembayaran = ?, "
                + " kode_user = ?, status = ?, tgl_batal = ?, user_batal = ? where no_penjualan = ?");
        ps.setString(1, p.getTglPenjualan());
        ps.setString(2, p.getKategoriPenjualan());
        ps.setString(3, p.getPelanggan());
        ps.setDouble(4, p.getTotalPenjualan());
        ps.setDouble(5, p.getTotalDiskon());
        ps.setDouble(6, p.getGrandtotal());
        ps.setDouble(7, p.getPembayaran());
        ps.setDouble(8, p.getSisaPembayaran());
        ps.setString(9, p.getKodeUser());
        ps.setString(10, p.getStatus());
        ps.setString(11, p.getTglBatal());
        ps.setString(12, p.getUserBatal());        
        ps.setString(13, p.getNoPenjualan());
        ps.executeUpdate();
    }
}
